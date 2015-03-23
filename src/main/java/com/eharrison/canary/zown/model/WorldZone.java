package com.eharrison.canary.zown.model;

import java.util.HashMap;
import java.util.Map;

public class WorldZone {
	private final Map<String, Zone> zonesByName;
	private final Tree<Zone> zones;
	
	public WorldZone() {
		zonesByName = new HashMap<String, Zone>();
		final Zone worldZone = new Zone("world");
		zones = new Tree<Zone>(worldZone);
		zonesByName.put(worldZone.getName(), worldZone);
	}
	
	public boolean addZone(final Zone zone) {
		final boolean added;
		switch (addZone(zones, zone)) {
			case ADDED:
				added = true;
				zonesByName.put(zone.getName(), zone);
				break;
			default:
				added = false;
		}
		return added;
	}
	
	public Zone getZone(final String name) {
		return zonesByName.get(name);
	}
	
	public Zone getZone(final Point point) {
		return getZone(zones, point);
	}
	
	private AddZoneResult addZone(final Tree<Zone> tree, final Zone zone) {
		final Zone parent = tree.getData();
		if (parent.intersects(zone)) {
			return AddZoneResult.INTERSECTED;
		}
		
		AddZoneResult added = null;
		if (parent.contains(zone)) {
			for (final Tree<Zone> t : tree.getChildren()) {
				added = addZone(t, zone);
				if (added != null) {
					break;
				}
			}
			
			if (added == null) {
				tree.addChild(zone);
				added = AddZoneResult.ADDED;
			}
		}
		
		return added;
	}
	
	private Zone getZone(final Tree<Zone> tree, final Point point) {
		Zone zone = null;
		if (tree.getData().contains(point)) {
			zone = tree.getData();
			for (final Tree<Zone> t : tree.getChildren()) {
				final Zone z = getZone(t, point);
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
