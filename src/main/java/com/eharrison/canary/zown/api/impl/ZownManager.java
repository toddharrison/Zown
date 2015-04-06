package com.eharrison.canary.zown.api.impl;

import java.util.HashMap;
import java.util.Map;

import net.canarymod.api.world.World;
import net.canarymod.api.world.position.Location;

import com.eharrison.canary.zown.api.ITemplate;
import com.eharrison.canary.zown.api.IZown;
import com.eharrison.canary.zown.api.IZownManager;
import com.eharrison.canary.zown.api.Point;

public class ZownManager implements IZownManager {
	private final Map<World, Tree<Zown>> zownTrees;
	private final Map<World, Map<String, Tree<Zown>>> zownMaps;
	
	public ZownManager() {
		zownTrees = new HashMap<World, Tree<Zown>>();
		zownMaps = new HashMap<World, Map<String, Tree<Zown>>>();
	}
	
	@Override
	public Tree<? extends IZown> getZown(final World world) {
		final Tree<Zown> zownTree;
		if (!zownTrees.containsKey(world)) {
			final Zown worldZown = new Zown(world.getFqName());
			zownTree = new Tree<Zown>(worldZown);
			final Map<String, Tree<Zown>> zownMap = new HashMap<String, Tree<Zown>>();
			zownMap.put(world.getFqName(), zownTree);
			zownTrees.put(world, zownTree);
			zownMaps.put(world, zownMap);
		} else {
			zownTree = zownTrees.get(world);
		}
		return zownTree;
	}
	
	@Override
	public Tree<? extends IZown> getZown(final World world, final String name) {
		Tree<Zown> zownTree = null;
		final Map<String, Tree<Zown>> zownMap = zownMaps.get(world);
		if (zownMap != null) {
			zownTree = zownMap.get(name);
		}
		return zownTree;
	}
	
	@Override
	public Tree<? extends IZown> getZown(final Location location) {
		final Tree<? extends IZown> rootTree = getZown(location.getWorld());
		return findZown(rootTree, new Point(location));
	}
	
	@Override
	public Tree<? extends IZown> createZown(final World world, final String name,
			final ITemplate template, final Point p1, final Point p2) {
		Tree<Zown> zoneTree = null;
		
		Map<String, Tree<Zown>> zownMap = zownMaps.get(world);
		if (zownMap == null) {
			getZown(world);
			zownMap = zownMaps.get(world);
		}
		
		if (!zownMap.containsKey(name)) {
			final Tree<Zown> rootTree = zownTrees.get(world);
			final Zown z = new Zown(name, (Template) template, p1, p2);
			if (!intersectsExistingZown(rootTree, z)) {
				final Tree<Zown> targetTree = getTargetContainingZown(rootTree, z);
				zoneTree = new Tree<Zown>(z);
				targetTree.addChild(zoneTree);
				zownMap.put(name, zoneTree);
			}
		}
		
		return zoneTree;
	}
	
	@Override
	public boolean removeZown(final World world, final String name) {
		boolean removed = false;
		final Map<String, Tree<Zown>> zownMap = zownMaps.get(world);
		if (zownMap != null && zownMap.containsKey(name)) {
			final Tree<Zown> tree = zownMap.remove(name);
			removed = tree.removeParent();
			if (removed) {
				for (final Tree<Zown> t : tree) {
					zownMap.remove(t.getData().getName());
				}
			}
		}
		return removed;
	}
	
	@Override
	public boolean renameZown(final World world, final String oldName, final String newName) {
		boolean renamed = false;
		final Map<String, Tree<Zown>> zownMap = zownMaps.get(world);
		if (zownMap != null && zownMap.containsKey(oldName) && !zownMap.containsKey(newName)) {
			final Tree<Zown> zownTree = zownMap.remove(oldName);
			zownTree.getData().setName(newName);
			zownMap.put(newName, zownTree);
			renamed = true;
		}
		return renamed;
	}
	
	private boolean intersectsExistingZown(final Tree<Zown> zownTree, final Zown zown) {
		boolean intersects = false;
		for (final Tree<Zown> t : zownTree) {
			if (t.getData().intersects(zown)) {
				intersects = true;
				break;
			}
		}
		return intersects;
	}
	
	private Tree<Zown> getTargetContainingZown(final Tree<Zown> tree, final Zown zown) {
		Tree<Zown> containingZown = null;
		if (tree.getData().contains(zown)) {
			containingZown = tree;
			for (final Tree<Zown> t : tree.getChildren()) {
				final Tree<Zown> z = getTargetContainingZown(t, zown);
				if (z != null) {
					containingZown = z;
				}
			}
		}
		return containingZown;
	}
	
	private Tree<? extends IZown> findZown(final Tree<? extends IZown> tree, final Point point) {
		Tree<? extends IZown> zone = null;
		if (tree.getData().contains(point)) {
			zone = tree;
			for (final Tree<? extends IZown> t : tree.getChildren()) {
				final Tree<? extends IZown> z = findZown(t, point);
				if (z != null) {
					zone = z;
				}
			}
		}
		return zone;
	}
}
