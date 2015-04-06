package com.eharrison.canary.zown.api.impl;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import net.canarymod.api.world.DimensionType;
import net.canarymod.api.world.World;
import net.canarymod.api.world.position.Location;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import com.eharrison.canary.zown.api.IZown;
import com.eharrison.canary.zown.api.Point;

public class ZownManagerTest extends EasyMockSupport {
	private ZownManager zownManager;
	private Template template;
	
	private World worldMock;
	
	@Before
	public void init() {
		worldMock = createMock(World.class);
		template = new Template("template");
		zownManager = new ZownManager();
	}
	
	@Test
	public void testWorldZown() {
		expect(worldMock.getFqName()).andReturn("foo").anyTimes();
		replayAll();
		
		final Tree<? extends IZown> worldZown = zownManager.getZown(worldMock);
		assertNotNull(worldZown);
		assertNotNull(worldZown.getData());
		assertTrue(worldZown.isRoot());
		assertTrue(worldZown.isLeaf());
		assertEquals("foo", worldZown.getData().getName());
		
		assertEquals(worldZown, zownManager.getZown(worldMock, "foo"));
		assertNull(zownManager.getZown(worldMock, "bar"));
		
		verifyAll();
	}
	
	@Test
	public void testCreateZown() {
		expect(worldMock.getFqName()).andReturn("foo").anyTimes();
		replayAll();
		
		final Tree<? extends IZown> zown1 = zownManager.createZown(worldMock, "zown1", template,
				new Point(0, 0, 0), new Point(10, 10, 10));
		assertNotNull(zown1);
		assertFalse(zown1.isRoot());
		assertTrue(zown1.isLeaf());
		assertEquals("zown1", zown1.getData().getName());
		
		final Tree<? extends IZown> zown2 = zownManager.createZown(worldMock, "zown2", template,
				new Point(5, 5, 5), new Point(15, 15, 15));
		assertNull(zown2);
		
		final Tree<? extends IZown> zown3 = zownManager.createZown(worldMock, "zown3", template,
				new Point(0, 0, 0), new Point(5, 5, 5));
		assertNotNull(zown3);
		assertFalse(zown1.isRoot());
		assertFalse(zown1.isLeaf());
		assertFalse(zown3.isRoot());
		assertTrue(zown3.isLeaf());
		assertEquals(zown1, zown3.getParent());
		assertTrue(zown1.getChildren().contains(zown3));
		assertEquals("zown3", zown3.getData().getName());
		
		verifyAll();
	}
	
	@Test
	public void testGetZownByLocation() {
		expect(worldMock.getName()).andReturn("foo");
		expect(worldMock.getType()).andReturn(DimensionType.NORMAL);
		expect(worldMock.getFqName()).andReturn("foo_normal").anyTimes();
		replayAll();
		
		final Location location = new Location(worldMock, 5, 5, 5, 0, 0);
		
		final Tree<? extends IZown> rootZown = zownManager.getZown(worldMock);
		assertEquals(rootZown, zownManager.getZown(location));
		
		final Tree<? extends IZown> zown1 = zownManager.createZown(worldMock, "zown1", template,
				new Point(0, 0, 0), new Point(10, 10, 10));
		assertNotEquals(rootZown, zown1);
		assertEquals(zown1, zownManager.getZown(location));
		
		final Tree<? extends IZown> zown2 = zownManager.createZown(worldMock, "zown2", template,
				new Point(0, 0, 0), new Point(5, 5, 5));
		assertNotEquals(zown1, zown2);
		assertEquals(zown2, zownManager.getZown(location));
		
		final Tree<? extends IZown> zown3 = zownManager.createZown(worldMock, "zown3", template,
				new Point(0, 0, 0), new Point(2, 2, 2));
		assertNotEquals(zown2, zown3);
		assertNotEquals(zown3, zownManager.getZown(location));
		
		verifyAll();
	}
	
	@Test
	public void testRemoveZown() {
		expect(worldMock.getFqName()).andReturn("foo").anyTimes();
		replayAll();
		
		final Tree<? extends IZown> zown1 = zownManager.createZown(worldMock, "zown1", template,
				new Point(0, 0, 0), new Point(10, 10, 10));
		final Tree<? extends IZown> zown2 = zownManager.createZown(worldMock, "zown2", template,
				new Point(0, 0, 0), new Point(5, 5, 5));
		final Tree<? extends IZown> zown3 = zownManager.createZown(worldMock, "zown3", template,
				new Point(0, 0, 0), new Point(2, 2, 2));
		final Tree<? extends IZown> zown4 = zownManager.createZown(worldMock, "zown4", template,
				new Point(15, 15, 15), new Point(20, 20, 20));
		
		assertEquals(zown1, zown2.getParent());
		
		zownManager.removeZown(worldMock, "zown2");
		
		assertEquals(zown1, zownManager.getZown(worldMock, "zown1"));
		assertNull(zownManager.getZown(worldMock, "zown2"));
		assertNull(zownManager.getZown(worldMock, "zown3"));
		assertEquals(zown4, zownManager.getZown(worldMock, "zown4"));
		assertFalse(zown1.getChildren().contains(zown2));
		assertNull(zown2.getParent());
		assertEquals(zown2, zown3.getParent());
		
		verifyAll();
	}
	
	@Test
	public void testRenameZown() {
		expect(worldMock.getFqName()).andReturn("foo").anyTimes();
		replayAll();
		
		final Tree<? extends IZown> zown = zownManager.createZown(worldMock, "zown1", template,
				new Point(0, 0, 0), new Point(10, 10, 10));
		
		assertEquals(zown, zownManager.getZown(worldMock, "zown1"));
		assertNull(zownManager.getZown(worldMock, "zown2"));
		
		assertTrue(zownManager.renameZown(worldMock, "zown1", "zown2"));
		assertNull(zownManager.getZown(worldMock, "zown1"));
		assertEquals(zown, zownManager.getZown(worldMock, "zown2"));
		
		verifyAll();
	}
}
