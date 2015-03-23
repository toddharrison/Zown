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
}
