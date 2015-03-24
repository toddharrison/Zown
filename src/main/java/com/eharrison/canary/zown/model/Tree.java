package com.eharrison.canary.zown.model;

import java.util.ArrayList;
import java.util.List;

public class Tree<T> {
	private Tree<T> parent;
	private final List<Tree<T>> children;
	private final T data;
	
	public Tree(final T data) {
		this.children = new ArrayList<Tree<T>>();
		this.data = data;
	}
	
	public boolean isRoot() {
		return parent == null;
	}
	
	public boolean isLeaf() {
		return children.size() == 0;
	}
	
	public T getData() {
		return data;
	}
	
	public Tree<T> getParent() {
		return parent;
	}
	
	public Tree<T> addChild(final T child) {
		final Tree<T> childTree = new Tree<T>(child);
		return addChild(childTree);
	}
	
	public Tree<T> addChild(final Tree<T> childTree) {
		childTree.parent = this;
		this.children.add(childTree);
		return childTree;
	}
	
	public List<Tree<T>> getChildren() {
		return children;
	}
}
