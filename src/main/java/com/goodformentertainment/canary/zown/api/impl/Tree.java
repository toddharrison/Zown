package com.goodformentertainment.canary.zown.api.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Tree<T> implements Iterable<Tree<T>> {
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

    public boolean removeParent() {
        boolean removed = false;
        if (parent != null) {
            removed = parent.removeChild(this);
            parent = null;
        }
        return removed;
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

    public boolean removeChild(final Tree<T> childTree) {
        return children.remove(childTree);
    }

    public List<Tree<T>> getChildren() {
        return children;
    }

    public boolean hasDecendant(final T decendant) {
        boolean present = false;
        for (final Tree<T> t : this) {
            if (t.getData().equals(decendant)) {
                present = true;
                break;
            }
        }
        return present;
    }

    public boolean removeDecendant(final T decendant) {
        boolean removed = false;
        for (final Tree<T> t : this) {
            if (t.getData().equals(decendant)) {
                t.getParent().removeChild(t);
                removed = true;
                break;
            }
        }
        return removed;
    }

    // TODO
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        String prefix = null;
        for (final Tree<T> t : this) {
            if (prefix == null) {
                prefix = "  ";
            } else {
                sb.append(prefix);
            }
            sb.append(t.getData());
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public Iterator<Tree<T>> iterator() {
        final List<Tree<T>> list = new LinkedList<Tree<T>>();
        iterate(this, list);
        return list.iterator();
    }

    private void iterate(final Tree<T> parent, final List<Tree<T>> list) {
        list.add(parent);
        for (final Tree<T> child : parent.children) {
            iterate(child, list);
        }
    }
}
