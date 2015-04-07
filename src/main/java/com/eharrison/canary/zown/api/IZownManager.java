package com.eharrison.canary.zown.api;

import net.canarymod.api.world.World;
import net.canarymod.api.world.position.Location;

import com.eharrison.canary.zown.api.impl.Tree;

public interface IZownManager {
	void loadZowns(World world);
	
	void unloadZowns(World world);
	
	Tree<? extends IZown> getZown(World world);
	
	Tree<? extends IZown> getZown(World world, String name);
	
	Tree<? extends IZown> getZown(Location location);
	
	Tree<? extends IZown> createZown(World world, String name, ITemplate template, Point p1, Point p2);
	
	boolean removeZown(World world, String name);
	
	boolean renameZown(World world, String oldName, String newName);
}
