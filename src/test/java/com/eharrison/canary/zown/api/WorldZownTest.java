package com.eharrison.canary.zown.api;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.canarymod.api.world.World;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

public class WorldZownTest extends EasyMockSupport {
	private WorldZown worldZown;
	
	private World world;
	private Template template;
	
	@Before
	public void init() {
		world = createMock(World.class);
		template = createMock(Template.class);
		worldZown = new WorldZown(world, template);
	}
	
	@Test
	public void worldZownCreation() {
		replayAll();
		
		WorldZown worldZown = WorldZown.getWorldZown(world);
		assertNull(worldZown);
		
		worldZown = WorldZown.getOrCreateWorldZown(world, template);
		assertNotNull(worldZown);
		final Tree<Zown> root = worldZown.getRootZown();
		assertTrue(root.isRoot());
		assertTrue(root.isLeaf());
		assertNull(root.getData().getTemplate());
		
		assertEquals(worldZown, WorldZown.getWorldZown(world));
		
		assertEquals(world, worldZown.getWorld());
		
		verifyAll();
	}
	
	@Test
	public void createGlobalZowns() {
		final List<Zown> zowns = new ArrayList<Zown>();
		expect(template.getZowns()).andReturn(zowns).anyTimes();
		replayAll();
		
		final Tree<Zown> zown1 = worldZown.createZown("zown1", null, null);
		assertNotNull(zown1);
		assertTrue(zown1.isLeaf());
		assertFalse(zown1.isRoot());
		assertEquals(worldZown.getRootZown(), zown1.getParent());
		assertEquals(template, zown1.getData().getTemplate());
		
		assertEquals(1, zowns.size());
		assertTrue(zowns.contains(zown1.getData()));
		
		assertEquals(zown1, worldZown.getZown("zown1"));
		assertFalse(worldZown.addZown(zown1.getData()));
		
		final Tree<Zown> zown2 = worldZown.createZown("zown2", null, null);
		assertNotNull(zown2);
		assertEquals(zown1, zown2.getParent());
		
		assertEquals(2, zowns.size());
		assertTrue(zowns.contains(zown2.getData()));
		
		verifyAll();
	}
	
	@Test
	public void createSeparateZowns() {
		final Collection<Zown> zowns = new ArrayList<Zown>();
		expect(template.getZowns()).andReturn(zowns).anyTimes();
		replayAll();
		
		final Tree<Zown> root = worldZown.getRootZown();
		
		final Tree<Zown> zown1 = worldZown.createZown("zown1", new Point(0, 0, 0),
				new Point(10, 10, 10));
		assertEquals(root, zown1.getParent());
		
		final Tree<Zown> zown2 = worldZown.createZown("zown2", new Point(15, 15, 15), new Point(20, 20,
				20));
		assertEquals(root, zown2.getParent());
		
		assertTrue(zown1.isLeaf());
		assertTrue(zown2.isLeaf());
		
		verifyAll();
	}
	
	@Test
	public void createContainedZowns() {
		final Collection<Zown> zowns = new ArrayList<Zown>();
		expect(template.getZowns()).andReturn(zowns).anyTimes();
		replayAll();
		
		final Tree<Zown> zown1 = worldZown.createZown("zown1", new Point(0, 0, 0),
				new Point(10, 10, 10));
		
		final Tree<Zown> zown2 = worldZown.createZown("zown2", new Point(5, 5, 5),
				new Point(10, 10, 10));
		assertEquals(zown1, zown2.getParent());
		
		assertFalse(zown1.isLeaf());
		assertTrue(zown2.isLeaf());
		
		verifyAll();
	}
	
	@Test
	public void createIntersectingZowns() {
		final Collection<Zown> zowns = new ArrayList<Zown>();
		expect(template.getZowns()).andReturn(zowns).anyTimes();
		replayAll();
		
		final Tree<Zown> root = worldZown.getRootZown();
		
		final Tree<Zown> zown1 = worldZown.createZown("zown1", new Point(0, 0, 0),
				new Point(10, 10, 10));
		
		final Tree<Zown> zown2 = worldZown.createZown("zown2", new Point(5, 5, 5),
				new Point(20, 20, 20));
		assertNull(zown2);
		
		assertTrue(zown1.isLeaf());
		assertEquals(1, root.getChildren().size());
		
		verifyAll();
	}
	
	@Test
	public void removeZown() {
		final Collection<Zown> zowns = new ArrayList<Zown>();
		expect(template.getZowns()).andReturn(zowns).anyTimes();
		replayAll();
		
		final Tree<Zown> root = worldZown.getRootZown();
		
		final Tree<Zown> zown1 = worldZown.createZown("zown1", new Point(0, 0, 0),
				new Point(10, 10, 10));
		assertTrue(root.getChildren().contains(zown1));
		
		final Tree<Zown> zown2 = worldZown.createZown("zown2", new Point(5, 5, 5),
				new Point(10, 10, 10));
		assertTrue(zown1.getChildren().contains(zown2));
		
		assertFalse(worldZown.removeZown("foo"));
		assertTrue(worldZown.removeZown("zown1"));
		
		assertTrue(root.getChildren().isEmpty());
		assertNull(worldZown.getZown("zown1"));
		assertNull(worldZown.getZown("zown2"));
		
		verifyAll();
	}
	
	@Test
	public void getZownByPoint() {
		final Collection<Zown> zowns = new ArrayList<Zown>();
		expect(template.getZowns()).andReturn(zowns).anyTimes();
		replayAll();
		
		final Tree<Zown> root = worldZown.getRootZown();
		
		final Tree<Zown> zown1 = worldZown.createZown("zown1", new Point(0, 0, 0),
				new Point(10, 10, 10));
		
		final Tree<Zown> zown2 = worldZown.createZown("zown2", new Point(5, 5, 5),
				new Point(10, 10, 10));
		
		assertEquals(root, worldZown.getZown(new Point(20, 20, 20)));
		assertEquals(zown2, worldZown.getZown(new Point(10, 10, 10)));
		assertEquals(zown1, worldZown.getZown(new Point(0, 0, 0)));
		
		verifyAll();
	}
}
