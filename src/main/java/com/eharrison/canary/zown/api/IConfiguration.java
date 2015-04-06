package com.eharrison.canary.zown.api;

import net.canarymod.api.entity.Entity;
import net.canarymod.api.world.blocks.BlockType;

public interface IConfiguration extends Cloneable {
	boolean addOwnerPermission(final String flag);
	
	boolean removeOwnerPermission(final String flag);
	
	boolean hasOwnerPermission(final String flag);
	
	Boolean getFlag(final String flag);
	
	Boolean setFlag(final String flag, final Boolean allow);
	
	boolean hasFlag(final String flag);
	
	boolean addBlockBuildExclusion(final BlockType blockType);
	
	boolean removeBlockBuildExclusion(final BlockType blockType);
	
	boolean hasBlockBuildExclusion(final BlockType blockType);
	
	boolean addBlockInteractExclusion(final BlockType blockType);
	
	boolean removeBlockInteractExclusion(final BlockType blockType);
	
	boolean hasBlockInteractExclusion(final BlockType blockType);
	
	boolean addEntityCreateExclusion(final Class<? extends Entity> entityClass);
	
	boolean removeEntityCreateExclusion(final Class<? extends Entity> entityClass);
	
	boolean hasEntityCreateExclusion(final Class<? extends Entity> entityClass);
	
	boolean addEntityInteractExclusion(final Class<? extends Entity> entityClass);
	
	boolean removeEntityInteractExclusion(final Class<? extends Entity> entityClass);
	
	boolean hasEntityInteractExclusion(final Class<? extends Entity> entityClass);
	
	boolean addCommandRestriction(final String command);
	
	boolean removeCommandRestriction(final String command);
	
	boolean hasCommandRestriction(final String command);
	
	public IConfiguration clone();
}
