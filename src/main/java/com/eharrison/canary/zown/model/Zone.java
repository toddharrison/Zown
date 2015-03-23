package com.eharrison.canary.zown.model;

public class Zone {
	private final String name;
	private final Point minPoint;
	private final Point maxPoint;
	private final Point centerPoint;
	private final int minRadiusSquared;
	private final int maxRadiusSquared;
	
	public Zone(final String name) {
		this.name = name;
		minPoint = null;
		maxPoint = null;
		centerPoint = null;
		minRadiusSquared = 0;
		maxRadiusSquared = 0;
	}
	
	public Zone(final String name, final Point p1, final Point p2) {
		this.name = name;
		
		minPoint = new Point(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y), Math.min(p1.z, p2.z));
		maxPoint = new Point(Math.max(p1.x, p2.x), Math.max(p1.y, p2.y), Math.max(p1.z, p2.z));
		centerPoint = new Point((int) Math.floor((minPoint.x + maxPoint.x) / 2.0),
				(int) Math.floor((minPoint.y + maxPoint.y) / 2.0),
				(int) Math.floor((minPoint.z + maxPoint.z) / 2.0));
		
		final int minRadius = Math.min(
				Math.min(centerPoint.x - minPoint.x, centerPoint.y - minPoint.y), centerPoint.z
						- minPoint.z);
		minRadiusSquared = minRadius * minRadius;
		maxRadiusSquared = centerPoint.distanceSquared(maxPoint);
	}
	
	public String getName() {
		return name;
	}
	
	public Point getMinPoint() {
		return minPoint;
	}
	
	public Point getMaxPoint() {
		return maxPoint;
	}
	
	public boolean contains(final Point p) {
		boolean contained = false;
		if (centerPoint == null) {
			contained = true;
		} else {
			if (centerPoint.distanceSquared(p) <= minRadiusSquared) {
				// Definite collision, inside of minimum radius
				contained = true;
			} else if (centerPoint.distanceSquared(p) <= maxRadiusSquared) {
				// Possible collision, verify precisely
				if (p.x >= minPoint.x && p.x <= maxPoint.x && p.y >= minPoint.y && p.y <= maxPoint.y
						&& p.z >= minPoint.z && p.z <= maxPoint.z) {
					contained = true;
				}
			}
		}
		return contained;
	}
	
	public boolean contains(final Zone z) {
		boolean contained = false;
		if (centerPoint == null || contains(z.getMinPoint()) && contains(z.getMaxPoint())) {
			contained = true;
		}
		return contained;
	}
	
	public boolean intersects(final Zone z) {
		boolean intersects = false;
		if (centerPoint != null && contains(z.getMinPoint()) ^ contains(z.getMaxPoint())) {
			intersects = true;
		}
		return intersects;
	}
}
