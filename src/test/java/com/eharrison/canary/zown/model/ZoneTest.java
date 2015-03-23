package com.eharrison.canary.zown.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class ZoneTest {
	@Test
	public void globalZone() {
		final Zone zone = new Zone("foo");
		assertEquals("foo", zone.getName());
		assertNull(zone.getMinPoint());
		assertNull(zone.getMaxPoint());
		
		assertTrue(zone.contains(new Point(0, 0, 0)));
		assertTrue(zone.contains(new Point(1000, -10, 50)));
		assertTrue(zone.contains(new Zone("bar")));
		assertTrue(zone.contains(new Zone("baz", new Point(0, 0, 0), new Point(5, 5, 5))));
		
		assertFalse(zone.intersects(new Zone("bar")));
		assertFalse(zone.intersects(new Zone("baz", new Point(0, 0, 0), new Point(5, 5, 5))));
	}
	
	@Test
	public void zone() {
		final Zone zone = new Zone("foo", new Point(5, 5, 5), new Point(10, 10, 10));
		assertEquals(new Point(5, 5, 5), zone.getMinPoint());
		assertEquals(new Point(10, 10, 10), zone.getMaxPoint());
		
		assertFalse(zone.contains(new Point(0, 0, 0)));
		assertFalse(zone.contains(new Point(4, 4, 4)));
		assertTrue(zone.contains(new Point(5, 5, 5)));
		assertTrue(zone.contains(new Point(10, 10, 10)));
		assertFalse(zone.contains(new Point(11, 11, 11)));
		assertFalse(zone.contains(new Zone("bar")));
		assertFalse(zone.contains(new Zone("baz", new Point(0, 0, 0), new Point(4, 4, 4))));
		assertFalse(zone.contains(new Zone("baz", new Point(0, 0, 0), new Point(5, 5, 5))));
		assertTrue(zone.contains(new Zone("baz", new Point(6, 6, 6), new Point(7, 7, 7))));
		assertTrue(zone.contains(new Zone("baz", new Point(5, 5, 5), new Point(10, 10, 10))));
		
		assertFalse(zone.intersects(new Zone("bar")));
		assertFalse(zone.intersects(new Zone("baz", new Point(0, 0, 0), new Point(4, 4, 4))));
		assertTrue(zone.intersects(new Zone("baz", new Point(0, 0, 0), new Point(5, 5, 5))));
		assertFalse(zone.intersects(new Zone("baz", new Point(6, 6, 6), new Point(7, 7, 7))));
		assertFalse(zone.intersects(new Zone("baz", new Point(5, 5, 5), new Point(10, 10, 10))));
	}
}
