package com.example.testing;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TreeNode<T> implements Iterable<TreeNode<T>> {

    T data;
    TreeNode<T> parent;
    List<TreeNode<T>> children;
    public TreeNode()
    {
    	
    }
    public TreeNode(T data) {
        this.data = data;
        this.children = new LinkedList<TreeNode<T>>();
    }

    public TreeNode<T> addChild(T child) {
        TreeNode<T> childNode = new TreeNode<T>(child);
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }
    public Iterator<TreeNode<T>> iterator() {
        return new MyIterator<TreeNode<T>>();
    }

    // other features ...
    public class MyIterator <T> implements Iterator<T> {

        public boolean hasNext() {
        
            //implement...
        	return true;
        }

        public T next() {
            //implement...;
        	T reT;
        	return null;
        }

        public void remove() {
            //implement... if supported.
        }
    }
}