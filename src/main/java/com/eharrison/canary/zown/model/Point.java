package com.eharrison.canary.zown.model;

public class Point {
	public int x;
	public int y;
	public int z;
	
	public Point(final int x, final int y, final int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int distanceSquared(final Point p) {
		final int dX = x - p.x;
		final int dY = y - p.y;
		final int dZ = z - p.z;
		return dX * dX + dY * dY + dZ * dZ;
	}
	
	@Override
	public boolean equals(final Object o) {
		boolean equal = false;
		if (o instanceof Point) {
			final Point p = (Point) o;
			if (p.x == x && p.y == y && p.z == z) {
				equal = true;
			}
		}
		return equal;
	}
}
