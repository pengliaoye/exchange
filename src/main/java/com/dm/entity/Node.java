package com.dm.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tb_tree")
public class Node {

	// ��ʶ��
    @Id
    @GeneratedValue(generator="uuid")
	private String id;

	// �ڵ�����
	private String name;

	// ���
	private int level;

	// �Ƿ�Ҷ�ӽڵ�
	private boolean leaf;

	// ���ڵ� * --- 1
	@ManyToOne
	@JoinColumn(name = "pid")
	private Node parent;

	// �ӽڵ� 1 --- *
	@OneToMany
	@JoinColumn(name = "pid")
	private Set<Node> children;

	public Set<Node> getChildren() {
		return children;
	}

	public void setChildren(Set<Node> children) {
		this.children = children;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}
}
