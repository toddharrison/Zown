package com.eharrison.canary.zown.api;

import java.util.HashMap;
import java.util.Map;

import net.canarymod.api.world.World;

public class WorldZown {
	private static Map<World, WorldZown> worldZowns = new HashMap<World, WorldZown>();
	
	public static final WorldZown getWorldZown(final World world) {
		return worldZowns.get(world);
	}
	
	public static final WorldZown getOrCreateWorldZown(final World world,
			final Template defaultTemplate) {
		WorldZown worldZown = worldZowns.get(world);
		if (worldZown == null) {
			worldZown = new WorldZown(world, defaultTemplate);
			worldZowns.put(world, worldZown);
		}
		return worldZown;
	}
	
	private final World world;
	private final Tree<Zown> zownTree;
	private final Map<String, Tree<Zown>> zownMap;
	private final Template defaultTemplate;
	
	protected WorldZown(final World world, final Template defaultTemplate) {
		this.world = world;
		this.defaultTemplate = defaultTemplate;
		final Zown rootZown = new Zown(null);
		zownTree = new Tree<Zown>(rootZown);
		zownMap = new HashMap<String, Tree<Zown>>();
		zownMap.put(rootZown.getName(), zownTree);
	}
	
	public World getWorld() {
		return world;
	}
	
	public Template getDefaultTemplate() {
		return defaultTemplate;
	}
	
	public Tree<Zown> getRootZown() {
		return zownTree;
	}
	
	public Tree<Zown> getZown(final String name) {
		return zownMap.get(name);
	}
	
	public Tree<Zown> getZown(final Point point) {
		return getZone(zownTree, point);
	}
	
	public boolean hasZown(final String name) {
		return zownMap.containsKey(name);
	}
	
	public boolean addZown(final Zown zown) {
		boolean added = false;
		if (!zownMap.containsKey(zown.getName())) {
			if (!intersectsExistingZown(zown)) {
				final Tree<Zown> targetTree = getTargetContainingZown(zownTree, zown);
				zownMap.put(zown.getName(), targetTree.addChild(zown));
				added = true;
			}
		}
		return added;
	}
	
	public Tree<Zown> createZown(final String name, final Point p1, final Point p2) {
		return createZown(name, defaultTemplate, p1, p2);
	}
	
	public Tree<Zown> createZown(final String name, final Template template, final Point p1,
			final Point p2) {
		Tree<Zown> zownTree = null;
		final Zown zown = new Zown(name, template, p1, p2);
		if (!zownMap.containsKey(zown.getName())) {
			if (!intersectsExistingZown(zown)) {
				final Tree<Zown> targetTree = getTargetContainingZown(this.zownTree, zown);
				zownTree = targetTree.addChild(zown);
				zownMap.put(zown.getName(), zownTree);
			}
		}
		return zownTree;
	}
	
	public boolean removeZown(final String zown) {
		boolean removed = false;
		if (zownMap.containsKey(zown)) {
			final Tree<Zown> tree = zownMap.remove(zown);
			removed = tree.getParent().removeChild(tree);
			if (removed) {
				for (final Tree<Zown> t : tree) {
					zownMap.remove(t.getData().getName());
				}
			}
		}
		return removed;
	}
	
	private boolean intersectsExistingZown(final Zown zown) {
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
	
	private Tree<Zown> getZone(final Tree<Zown> tree, final Point point) {
		Tree<Zown> zone = null;
		if (tree.getData().contains(point)) {
			zone = tree;
			for (final Tree<Zown> t : tree.getChildren()) {
				final Tree<Zown> z = getZone(t, point);
				if (z != null) {
					zone = z;
				}
			}
		}
		return zone;
	}
}
