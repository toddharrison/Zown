package com.goodformentertainment.canary.zown.api.impl;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import net.canarymod.api.world.DimensionType;
import net.canarymod.api.world.World;
import net.canarymod.api.world.position.Location;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import com.goodformentertainment.canary.zown.api.ITemplateManager;
import com.goodformentertainment.canary.zown.api.IZown;
import com.goodformentertainment.canary.zown.api.Point;
import com.goodformentertainment.canary.zown.dao.DataManager;

@SuppressWarnings("unchecked")
public class ZownManagerTest extends EasyMockSupport {
	private Template template;
	
	private DataManager dataManagerMock;
	private ITemplateManager templateManagerMock;
	private World worldMock;
	private World world2Mock;
	
	@Before
	public void init() {
		dataManagerMock = createMock(DataManager.class);
		templateManagerMock = createMock(ITemplateManager.class);
		worldMock = createMock(World.class);
		world2Mock = createMock(World.class);
		template = new Template("template");
	}
	
	@Test
	public void testWorldZown() throws Exception {
		expect(worldMock.getFqName()).andReturn("foo").anyTimes();
		expect(dataManagerMock.saveZown(eq(worldMock), isA(Tree.class))).andReturn(true);
		replayAll();
		
		final ZownManager zownManager = new ZownManager(dataManagerMock, templateManagerMock);
		
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
	public void testCreateZown() throws Exception {
		expect(worldMock.getFqName()).andReturn("foo").anyTimes();
		expect(dataManagerMock.saveZown(eq(worldMock), isA(Tree.class))).andReturn(true).anyTimes();
		replayAll();
		
		final ZownManager zownManager = new ZownManager(dataManagerMock, templateManagerMock);
		
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
	public void testGetZownByLocation() throws Exception {
		expect(worldMock.getName()).andReturn("foo");
		expect(worldMock.getType()).andReturn(DimensionType.NORMAL);
		expect(worldMock.getFqName()).andReturn("foo_normal").anyTimes();
		expect(dataManagerMock.saveZown(eq(worldMock), isA(Tree.class))).andReturn(true).anyTimes();
		replayAll();
		
		final ZownManager zownManager = new ZownManager(dataManagerMock, templateManagerMock);
		
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
	public void testRemoveZown() throws Exception {
		expect(worldMock.getFqName()).andReturn("foo").anyTimes();
		expect(dataManagerMock.saveZown(eq(worldMock), isA(Tree.class))).andReturn(true).anyTimes();
		expect(dataManagerMock.removeZown(eq(worldMock), isA(Tree.class))).andReturn(true);
		replayAll();
		
		final ZownManager zownManager = new ZownManager(dataManagerMock, templateManagerMock);
		
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
	public void testRenameZown() throws Exception {
		expect(worldMock.getFqName()).andReturn("foo").anyTimes();
		expect(dataManagerMock.saveZown(eq(worldMock), isA(Tree.class))).andReturn(true).times(2);
		expect(dataManagerMock.saveZown(eq(worldMock), isA(Tree.class), eq("zown1"))).andReturn(true);
		replayAll();
		
		final ZownManager zownManager = new ZownManager(dataManagerMock, templateManagerMock);
		
		final Tree<? extends IZown> zown = zownManager.createZown(worldMock, "zown1", template,
				new Point(0, 0, 0), new Point(10, 10, 10));
		
		assertEquals(zown, zownManager.getZown(worldMock, "zown1"));
		assertNull(zownManager.getZown(worldMock, "zown2"));
		
		assertTrue(zownManager.renameZown(worldMock, "zown1", "zown2"));
		assertNull(zownManager.getZown(worldMock, "zown1"));
		assertEquals(zown, zownManager.getZown(worldMock, "zown2"));
		
		verifyAll();
	}
	
	@Test
	public void testResizeZown() throws Exception {
		expect(worldMock.getFqName()).andReturn("foo").anyTimes();
		expect(dataManagerMock.saveZown(eq(worldMock), isA(Tree.class))).andReturn(true).times(4);
		replayAll();
		
		final ZownManager zownManager = new ZownManager(dataManagerMock, templateManagerMock);
		
		final Tree<? extends IZown> rootZown = zownManager.getZown(worldMock);
		
		final Tree<? extends IZown> zown1 = zownManager.createZown(worldMock, "zown1", template,
				new Point(0, 0, 0), new Point(10, 10, 10));
		final Tree<? extends IZown> zown2 = zownManager.createZown(worldMock, "zown2", template,
				new Point(15, 15, 15), new Point(20, 20, 20));
		
		assertNotNull(zown1);
		assertEquals(rootZown, zown1.getParent());
		assertNotNull(zown2);
		assertEquals(rootZown, zown2.getParent());
		
		assertTrue(zownManager
				.resizeZown(worldMock, "zown1", new Point(5, 5, 5), new Point(10, 10, 10)));
		
		assertEquals(new Point(5, 5, 5), zown1.getData().getMinPoint());
		assertEquals(new Point(10, 10, 10), zown1.getData().getMaxPoint());
		
		assertFalse(zownManager.resizeZown(worldMock, "zown1", new Point(5, 5, 5),
				new Point(15, 15, 15)));
		
		assertEquals(new Point(5, 5, 5), zown1.getData().getMinPoint());
		assertEquals(new Point(10, 10, 10), zown1.getData().getMaxPoint());
		
		verifyAll();
	}
	
	@Test
	public void testSettingTemplate() throws Exception {
		expect(worldMock.getFqName()).andReturn("foo").anyTimes();
		expect(dataManagerMock.saveZown(eq(worldMock), isA(Tree.class))).andReturn(true).times(3);
		replayAll();
		
		final ZownManager zownManager = new ZownManager(dataManagerMock, templateManagerMock);
		
		final Tree<? extends IZown> zown1 = zownManager.createZown(worldMock, "zown1", null, new Point(
				0, 0, 0), new Point(10, 10, 10));
		
		assertNull(zown1.getData().getTemplate());
		
		zownManager.applyTemplate(worldMock, "zown1", template);
		
		assertEquals(template, zown1.getData().getTemplate());
		
		verifyAll();
	}
	
	@Test
	public void testMultipleWorldsSameZownName() throws Exception {
		expect(worldMock.getFqName()).andReturn("world1").anyTimes();
		expect(world2Mock.getFqName()).andReturn("world2").anyTimes();
		expect(dataManagerMock.saveZown(eq(worldMock), isA(Tree.class))).andReturn(true).times(3);
		expect(dataManagerMock.saveZown(eq(world2Mock), isA(Tree.class))).andReturn(true).times(3);
		replayAll();
		
		final ZownManager zownManager = new ZownManager(dataManagerMock, templateManagerMock);
		
		final Tree<? extends IZown> zown1 = zownManager.createZown(worldMock, "hub", null, null, null);
		final Tree<? extends IZown> zown2 = zownManager.createZown(worldMock, "child1", null, null,
				null);
		
		final Tree<? extends IZown> zown3 = zownManager.createZown(world2Mock, "hub", null, null, null);
		final Tree<? extends IZown> zown4 = zownManager.createZown(world2Mock, "child2", null, null,
				null);
		
		final Tree<IZown> worldTree1 = (Tree<IZown>) zownManager.getZown(worldMock);
		final Tree<IZown> worldTree2 = (Tree<IZown>) zownManager.getZown(world2Mock);
		
		assertTrue(worldTree1.hasDecendant(zown1.getData()));
		assertTrue(worldTree1.hasDecendant(zown2.getData()));
		assertFalse(worldTree1.hasDecendant(zown3.getData()));
		assertFalse(worldTree1.hasDecendant(zown4.getData()));
		
		assertFalse(worldTree2.hasDecendant(zown1.getData()));
		assertFalse(worldTree2.hasDecendant(zown2.getData()));
		assertTrue(worldTree2.hasDecendant(zown3.getData()));
		assertTrue(worldTree2.hasDecendant(zown4.getData()));
		
		verifyAll();
	}
}
