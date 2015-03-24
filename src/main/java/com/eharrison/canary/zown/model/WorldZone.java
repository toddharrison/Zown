package com.eharrison.canary.zown.model;

import java.util.HashMap;
import java.util.Map;

public class WorldZone {
	public static final String WORLD_ROOT = "world";
	
	private final Map<String, Tree<Zone>> zonesByName;
	private final Tree<Zone> root;
	
	public WorldZone() {
		zonesByName = new HashMap<String, Tree<Zone>>();
		final Zone worldZone = new Zone(WORLD_ROOT);
		root = new Tree<Zone>(worldZone);
		zonesByName.put(worldZone.getName(), root);
	}
	
	public boolean addZone(final Zone zone) {
		final boolean added;
		final Tree<Zone> treeZone = new Tree<Zone>(zone);
		switch (addZone(root, treeZone)) {
			case ADDED:
				added = true;
				zonesByName.put(zone.getName(), treeZone);
				break;
			default:
				added = false;
		}
		return added;
	}
	
	public Tree<Zone> getZone(final String name) {
		return zonesByName.get(name);
	}
	
	public Tree<Zone> getZone(final Point point) {
		return getZone(root, point);
	}
	
	private AddZoneResult addZone(final Tree<Zone> parent, final Tree<Zone> child) {
		final Zone parentZone = parent.getData();
		final Zone childZone = child.getData();
		if (parentZone.intersects(childZone)) {
			return AddZoneResult.INTERSECTED;
		}
		
		AddZoneResult added = null;
		if (parentZone.contains(childZone)) {
			for (final Tree<Zone> t : parent.getChildren()) {
				added = addZone(t, child);
				if (added != null) {
					break;
				}
			}
			
			if (added == null) {
				parent.addChild(child);
				added = AddZoneResult.ADDED;
			}
		}
		
		return added;
	}
	
	private Tree<Zone> getZone(final Tree<Zone> tree, final Point point) {
		Tree<Zone> zone = null;
		if (tree.getData().contains(point)) {
			zone = tree;
			for (final Tree<Zone> t : tree.getChildren()) {
				final Tree<Zone> z = getZone(t, point);
				if (z != null) {
					zone = z;
				}
			}
		}
		return zone;
	}
	
	private enum AddZoneResult {
		ADDED, INTERSECTED
	}
}
