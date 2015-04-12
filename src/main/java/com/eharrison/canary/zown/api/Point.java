package com.eharrison.canary.zown.api;

import net.canarymod.api.world.position.Location;

public class Point implements Cloneable {
	public int x;
	public int y;
	public int z;
	
	public static Point parse(final String sPoint) {
		if (sPoint == null) {
			throw new IllegalArgumentException("Null String Point");
		}
		final String[] values = sPoint.split(":");
		if (values.length == 1 && values[0].trim().equalsIgnoreCase("null")) {
			return null;
		}
		if (values.length != 4) {
			throw new IllegalArgumentException("Invalid Point String format");
		}
		
		Point point = null;
		if ("point".equals(values[0])) {
			point = new Point(Integer.parseInt(values[1]), Integer.parseInt(values[2]),
					Integer.parseInt(values[3]));
		} else {
			throw new IllegalArgumentException("Not a serialized Point");
		}
		return point;
	}
	
	public Point(final int x, final int y, final int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point(final Location location) {
		x = location.getBlockX();
		y = location.getBlockY();
		z = location.getBlockZ();
	}
	
	public int distanceSquared(final Point p) {
		final int dX = x - p.x;
		final int dY = y - p.y;
		final int dZ = z - p.z;
		return dX * dX + dY * dY + dZ * dZ;
	}
	
	@Override
	public Point clone() {
		return new Point(x, y, z);
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
	
	@Override
	public String toString() {
		return "point:" + x + ":" + y + ":" + z;
	}
}
