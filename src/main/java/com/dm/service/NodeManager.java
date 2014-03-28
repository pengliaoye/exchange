package com.dm.service;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
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
		if(children != null){
			for (Iterator<Node> iter = children.iterator(); iter.hasNext();) {
				Node child = iter.next();
				printNode(child);
			}
		}
	}

	public Node buildTree(ResultSet rs) throws SQLException {		
		Node root = null;
		Node parentNode = null;
		Node nearParentNode = null;
		while (rs.next()) {
			Node node = new Node();
			node.setId(rs.getInt("id"));
			node.setName(rs.getString("name"));
			node.setLevel(rs.getInt("level"));
			node.setLeaf(rs.getBoolean("leaf"));
			int pid = rs.getInt("pid");

			// 如果父节点为根节点
			if (pid == 0) {
				root = node;
				parentNode = null;
			} else if (nearParentNode != null && pid == nearParentNode.getId()) {// 最近一次循环的父节点
				parentNode = nearParentNode;
			} else {
				parentNode = findParentNode(root, pid);// 查找父节点				
			}

			if(parentNode != null){
				if (parentNode.getChildren() == null) {
					parentNode.setChildren(new HashSet<Node>());
				}
				parentNode.getChildren().add(node);
			}
			node.setParent(parentNode);							
			nearParentNode = parentNode;
		}
		return root;
	}

	private Node findParentNode(Node root, int pid) {

		if (root.getId() == pid) {
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

	public String parseCheckTree(Node node) {
		StringBuilder builder = new StringBuilder();
		builder.append("<ul id=\"tree-checkmenu\" class=\"checktree\">");
		Node root = new Node();
		Set<Node> children = new HashSet<>();
		children.add(node);
		root.setChildren(children);
		iterateNode(builder, root);
		builder.append("</ul>");
		String html = builder.toString();
		return html;
	}

	private void iterateNode(StringBuilder builder, Node node) {
		Set<Node> children = node.getChildren();
		if (children != null) {
			Iterator<Node> iterator = children.iterator();
			while (iterator.hasNext()) {
				Node child = iterator.next();
				boolean hasChildren = (child.getChildren() != null && child
						.getChildren().size() > 0);
				builder.append("<li");
				if (hasChildren) {
					builder.append(" id=\"show-" + child.getId() + "\"");
				}
				if (!iterator.hasNext()) {
					builder.append(" class=\"last\"");
				}
				builder.append(">");
				builder.append("<input id=\"check-" + child.getId()
						+ "\" type=\"checkbox\" />" + child.getName());
				if (hasChildren) {
					builder.append("<ul id=\"tree-" + child.getId() + "\">");
					iterateNode(builder, child);
					builder.append("</ul>");
				}
				builder.append("</li>");
			}
		}

	}
}
