package com.eharrison.canary.zown.api.impl;

import java.util.HashMap;
import java.util.Map;

import net.canarymod.api.world.World;
import net.canarymod.api.world.position.Location;

import com.eharrison.canary.zown.ZownPlugin;
import com.eharrison.canary.zown.api.ITemplate;
import com.eharrison.canary.zown.api.ITemplateManager;
import com.eharrison.canary.zown.api.IZown;
import com.eharrison.canary.zown.api.IZownManager;
import com.eharrison.canary.zown.api.Point;
import com.eharrison.canary.zown.dao.DataManager;

public class ZownManager implements IZownManager {
	private final Map<World, Tree<Zown>> zownTrees;
	private final Map<World, Map<String, Tree<Zown>>> zownMaps;
	private final DataManager dataManager;
	private final ITemplateManager templateManager;
	
	public ZownManager(final DataManager dataManager, final ITemplateManager templateManager) {
		zownTrees = new HashMap<World, Tree<Zown>>();
		zownMaps = new HashMap<World, Map<String, Tree<Zown>>>();
		this.dataManager = dataManager;
		this.templateManager = templateManager;
	}
	
	@Override
	public void loadZowns(final World world) {
		ZownPlugin.LOG.info("Loading zowns for " + world.getFqName());
		
		zownTrees.remove(world);
		zownMaps.remove(world);
		try {
			dataManager.loadZowns(world, templateManager, this);
		} catch (final Exception e) {
			ZownPlugin.LOG.error("Error loading zowns for " + world.getFqName(), e);
		}
	}
	
	@Override
	public void unloadZowns(final World world) {
		ZownPlugin.LOG.info("Unloading zowns for " + world.getFqName());
		
		zownTrees.remove(world);
		zownMaps.remove(world);
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
			
			try {
				dataManager.saveZown(world, zownTree);
			} catch (final Exception e) {
				ZownPlugin.LOG.error("Error saving world zown", e);
			}
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
		Tree<Zown> zownTree = null;
		
		Map<String, Tree<Zown>> zownMap = zownMaps.get(world);
		if (zownMap == null) {
			getZown(world);
			zownMap = zownMaps.get(world);
		}
		
		if (!zownMap.containsKey(name)) {
			final Tree<Zown> rootTree = zownTrees.get(world);
			final Zown zown = new Zown(name, (Template) template, p1, p2);
			if (!intersectsExistingZown(rootTree, zown)) {
				final Tree<Zown> targetTree = getTargetContainingZown(rootTree, zown);
				zownTree = new Tree<Zown>(zown);
				targetTree.addChild(zownTree);
				zownMap.put(name, zownTree);
				
				try {
					dataManager.saveZown(world, zownTree);
				} catch (final Exception e) {
					ZownPlugin.LOG.error("Error saving zown", e);
				}
			}
		}
		
		return zownTree;
	}
	
	public Tree<Zown> addZown(final World world, final String parentName, final String name,
			final ITemplate template, final Point p1, final Point p2) {
		Tree<Zown> zownTree = null;
		
		if (world.getFqName().equals(name)) {
			final Zown worldZown = new Zown(name, (Template) template);
			zownTree = new Tree<Zown>(worldZown);
			final Map<String, Tree<Zown>> zownMap = new HashMap<String, Tree<Zown>>();
			zownMap.put(world.getFqName(), zownTree);
			zownTrees.put(world, zownTree);
			zownMaps.put(world, zownMap);
		} else {
			final Map<String, Tree<Zown>> zownMap = zownMaps.get(world);
			
			if (!zownMap.containsKey(name)) {
				final Tree<Zown> rootTree = zownTrees.get(world);
				final Zown zown = new Zown(name, (Template) template, p1, p2);
				if (!intersectsExistingZown(rootTree, zown)) {
					final Tree<Zown> targetTree = getTargetContainingZown(rootTree, zown);
					if (targetTree.getData().getName().equals(parentName)) {
						zownTree = new Tree<Zown>(zown);
						targetTree.addChild(zownTree);
						zownMap.put(name, zownTree);
					} else {
						ZownPlugin.LOG.error("Zown to add has wrong parent zown");
					}
				} else {
					ZownPlugin.LOG.error("Zown to add intersects another zown");
				}
			} else {
				ZownPlugin.LOG.error("Zown to add has duplicate name");
			}
		}
		
		return zownTree;
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
				
				try {
					dataManager.removeZown(world, tree);
				} catch (final Exception e) {
					ZownPlugin.LOG.error("Error saving zown", e);
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
			
			try {
				dataManager.saveZown(world, zownTree, oldName);
			} catch (final Exception e) {
				ZownPlugin.LOG.error("Error saving zown", e);
			}
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
