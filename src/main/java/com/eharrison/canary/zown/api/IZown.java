package com.eharrison.canary.zown.api;

import net.canarymod.api.entity.living.humanoid.Player;

public interface IZown extends IConfigurable {
	String getName();
	
	// World getWorld();
	//
	// IZown getParent();
	
	ITemplate getTemplate();
	
	boolean addOwner(final Player player);
	
	boolean removeOwner(final Player player);
	
	boolean isOwner(final Player player);
	
	boolean addMember(final Player player);
	
	boolean removeMember(final Player player);
	
	boolean isMember(final Player player);
	
	boolean isOwnerOrMember(final Player player);
	
	boolean addEntryExclusion(final Player player);
	
	boolean removeEntryExclusion(final Player player);
	
	boolean hasEntryExclusion(final Player player);
	
	Point getMinPoint();
	
	Point getMaxPoint();
	
	// boolean setBounds(Point point1, Point point2);
	
	boolean contains(final Point point);
	
	boolean contains(final IZown zown);
	
	boolean intersects(final IZown zown);
	
	int getBlockCount();
	
	int getColumnCount();
}
