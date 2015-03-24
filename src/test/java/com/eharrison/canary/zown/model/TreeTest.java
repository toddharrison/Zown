package com.eharrison.canary.zown.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TreeTest {
	@Test
	public void tree() {
		final Tree<String> tree = new Tree<String>("root");
		final Tree<String> foo = tree.addChild("foo");
		foo.addChild("bar");
		final Tree<String> baz = tree.addChild("baz");
		
		assertEquals("root", tree.getData());
		assertTrue(tree.isRoot());
		assertFalse(tree.isLeaf());
		assertEquals(2, tree.getChildren().size());
		assertTrue(tree.getChildren().contains(foo));
		assertTrue(tree.getChildren().contains(baz));
		assertEquals(1, foo.getChildren().size());
		assertEquals("bar", foo.getChildren().get(0).getData());
		
		assertEquals("foo", foo.getChildren().get(0).getParent().getData());
	}
}
