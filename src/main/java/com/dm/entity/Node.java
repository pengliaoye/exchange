package com.dm.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tree")
public class Node {

	// 标识符
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	// 节点名称
	private String name;

	// 层次
	private int level;

	// 是否叶子节点
	private boolean leaf;

	// 父节点 * --- 1
	@ManyToOne
	@JoinColumn(name = "pid")
	private Node parent;

	// 子节点 1 --- *
	@OneToMany
	@JoinColumn(name = "pid")
	private Set<Node> children;

	public Set<Node> getChildren() {
		return children;
	}

	public void setChildren(Set<Node> children) {
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
