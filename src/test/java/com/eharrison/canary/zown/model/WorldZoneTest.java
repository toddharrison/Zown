package com.eharrison.canary.zown.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class WorldZoneTest {
	@Test
	public void worldRoot() {
		final WorldZone worldZone = new WorldZone();
		
		assertNull(worldZone.getZone("foo"));
		final Zone root = worldZone.getZone(WorldZone.WORLD_ROOT).getData();
		assertEquals("world", root.getName());
		assertEquals(root, worldZone.getZone(new Point(0, 0, 0)).getData());
	}
	
	@Test
	public void addZones() {
		final WorldZone worldZone = new WorldZone();
		
		final Zone zone1 = new Zone("foo", new Point(0, 0, 0), new Point(10, 10, 10));
		final Zone zone2 = new Zone("bar", new Point(5, 5, 5), new Point(10, 10, 10));
		final Zone zone3 = new Zone("baz", new Point(-1, -1, -1), new Point(5, 5, 5));
		assertTrue(worldZone.addZone(zone1));
		assertTrue(worldZone.addZone(zone2));
		assertFalse(worldZone.addZone(zone3));
		assertEquals("world", worldZone.getZone(new Point(-1, -1, -1)).getData().getName());
		assertEquals(zone1, worldZone.getZone(new Point(0, 0, 0)).getData());
		assertEquals(zone2, worldZone.getZone(new Point(7, 7, 7)).getData());
		assertEquals(zone1, worldZone.getZone(new Point(7, 7, 7)).getParent().getData());
		assertEquals("world", worldZone.getZone(new Point(7, 7, 7)).getParent().getParent().getData()
				.getName());
	}
}
