package com.eharrison.canary.zown.api;

import static org.junit.Assert.*;

import org.junit.Test;

public class PointTest {
	@Test
	public void createPoint() {
		Point point = new Point(1, 2, 3);
		assertEquals(1, point.x);
		assertEquals(2, point.y);
		assertEquals(3, point.z);
		
		point = new Point(-1, -2, -3);
		assertEquals(-1, point.x);
		assertEquals(-2, point.y);
		assertEquals(-3, point.z);
	}
	
	@Test
	public void equals() {
		final Point p1 = new Point(1, 1, 1);
		final Point p2 = new Point(1, 1, 1);
		assertEquals(p1, p2);
	}
	
	@Test
	public void distanceSquared() {
		Point point = new Point(0, 0, 0);
		assertEquals(0, point.distanceSquared(new Point(0, 0, 0)));
		assertEquals(1, point.distanceSquared(new Point(1, 0, 0)));
		assertEquals(1, point.distanceSquared(new Point(0, 1, 0)));
		assertEquals(1, point.distanceSquared(new Point(0, 0, 1)));
		assertEquals(1, point.distanceSquared(new Point(-1, 0, 0)));
		assertEquals(1, point.distanceSquared(new Point(0, -1, 0)));
		assertEquals(1, point.distanceSquared(new Point(0, 0, -1)));
		
		point = new Point(1, 1, 1);
		assertEquals(0, point.distanceSquared(new Point(1, 1, 1)));
		assertEquals(1, point.distanceSquared(new Point(2, 1, 1)));
		assertEquals(1, point.distanceSquared(new Point(1, 2, 1)));
		assertEquals(1, point.distanceSquared(new Point(1, 1, 2)));
		assertEquals(1, point.distanceSquared(new Point(0, 1, 1)));
		assertEquals(1, point.distanceSquared(new Point(1, 0, 1)));
		assertEquals(1, point.distanceSquared(new Point(1, 1, 0)));
		
		point = new Point(-1, -1, -1);
		assertEquals(0, point.distanceSquared(new Point(-1, -1, -1)));
		assertEquals(1, point.distanceSquared(new Point(-2, -1, -1)));
		assertEquals(1, point.distanceSquared(new Point(-1, -2, -1)));
		assertEquals(1, point.distanceSquared(new Point(-1, -1, -2)));
		assertEquals(1, point.distanceSquared(new Point(0, -1, -1)));
		assertEquals(1, point.distanceSquared(new Point(-1, 0, -1)));
		assertEquals(1, point.distanceSquared(new Point(-1, -1, 0)));
	}
}
