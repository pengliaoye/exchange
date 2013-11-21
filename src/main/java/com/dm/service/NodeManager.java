package com.dm.service;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import com.dm.entity.Node;
import com.dm.util.JpaUtils;

public class NodeManager {

	private static NodeManager nodeManager;

	private NodeManager() {
	}
 
	public static synchronized NodeManager getInstance() {
		if (nodeManager == null) {
			nodeManager = new NodeManager();
		}

		return nodeManager;
	}

	// 创建树型结构
	public void createTree(String dir) {
		EntityManager em = JpaUtils.getEntityManager();

		try {
			em.getTransaction().begin();

			File root = new File(dir);
			saveTree(root, em, null, 0);

			em.getTransaction().commit();
		} catch (RuntimeException e) {
			e.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			JpaUtils.closeEm(em);
		}
	}

	// 递归创建一棵树
	private void saveTree(File file, EntityManager em, Node parent, int level) {

		if (file == null || !file.exists()) {
			return;
		}

		boolean isLeaf = file.isFile();

		Node node = new Node();
		node.setName(file.getName());
		node.setLevel(level);
		node.setParent(parent);
		node.setLeaf(isLeaf);
		em.persist(node);

		File[] subs = file.listFiles();
		if (subs != null && subs.length > 0) {
			for (int i = 0; i < subs.length; i++) {
				saveTree(subs[i], em, node, level + 1);
			}
		}
	}

	public void printTree(int id) {
		EntityManager em = JpaUtils.getEntityManager();

		try {
			em.getTransaction().begin();

			Node root = (Node) em.find(Node.class, id);
			printNode(root);

			em.getTransaction().commit();
		} catch (RuntimeException e) {
			e.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			JpaUtils.closeEm(em);
		}
	}

	public void printNode(Node node) {

		if (node == null) {
			return;
		}
		int level = node.getLevel();
		if (level > 0) {
			for (int i = 0; i < level; i++) {
				System.out.print("  |");
			}
			System.out.print("--");
		}

		System.out.println(node.getName()
				+ (node.isLeaf() ? "" : "[" + node.getChildren().size() + "]"));

		Set<Node> children = node.getChildren();
		for (Iterator<Node> iter = children.iterator(); iter.hasNext();) {
			Node child = iter.next();
			printNode(child);
		}
	}
	
	public Node buildTree(List<Node> nodes){
		Iterator<Node> iterator = nodes.iterator();
		Node root = null;
		Node parentNode = null;
		Node nearParentNode = null;
		while (iterator.hasNext()) {
			Node node = iterator.next();
			
			// 如果父节点为根节点
			if (node.getParent() == null) {
				root = node;
				parentNode = root;
				nearParentNode = parentNode;
			} else if (node.getParent().getId() == nearParentNode.getId()) {// 最近一次循环的父节点
				parentNode = nearParentNode;
			} else {
				parentNode = findParentNode(root, node.getParent().getId());// 查找父节点
				nearParentNode = parentNode;				
			}
			
			if(parentNode.getChildren() == null){
				parentNode.setChildren(new HashSet<Node>());
			} 
			parentNode.getChildren().add(node);
		}
		return root;
	}
	
	private Node findParentNode(Node root, int pid) {
		
		if(root.getId() == pid){
			return root;
		}
		
		Set<Node> children = root.getChildren();
		if (children != null) {
			for (Node node : children) {
				Node childFind = findParentNode(node, pid);
				if (childFind != null) {
					return childFind;
				}
			}
		}
		return null;
	}
        
        public String parseCheckTree(Node node){
            return null;
        }
}
