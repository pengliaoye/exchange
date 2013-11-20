package com.bjsxt.hibernate;

import java.util.Set;

public class Node {

	// 标识符
	private int id;

	// 节点名称
	private String name;

	// 层次
	private int level;

	// 是否叶子节点
	private boolean leaf;

	// 父节点 * --- 1
	private Node parent;

	// 子节点 1 --- *
	private Set children;

	public Set getChildren() {
		return children;
	}

	public void setChildren(Set children) {
		this.children = children;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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
