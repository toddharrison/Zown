package com.eharrison.canary.zown.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.canarymod.api.entity.Entity;
import net.canarymod.api.world.blocks.BlockType;

public class Configuration implements Cloneable {
	private final Set<String> ownerPermissions;
	private final Map<String, Boolean> flags;
	private final Set<BlockType> blockBuildExclusions;
	private final Set<BlockType> blockInteractExclusions;
	private final HashSet<Class<? extends Entity>> entityCreateExclusions;
	private final HashSet<Class<? extends Entity>> entityInteractExclusions;
	private final HashSet<String> commandRestrictions;
	
	protected Configuration() {
		ownerPermissions = new HashSet<String>();
		flags = new HashMap<String, Boolean>();
		blockBuildExclusions = new HashSet<BlockType>();
		blockInteractExclusions = new HashSet<BlockType>();
		entityCreateExclusions = new HashSet<Class<? extends Entity>>();
		entityInteractExclusions = new HashSet<Class<? extends Entity>>();
		commandRestrictions = new HashSet<String>();
	}
	
	public boolean addOwnerPermission(final String flag) {
		return ownerPermissions.add(flag);
	}
	
	public boolean removeOwnerPermission(final String flag) {
		return ownerPermissions.remove(flag);
	}
	
	public boolean hasOwnerPermission(final String flag) {
		return ownerPermissions.contains(flag);
	}
	
	public Boolean getFlag(final String flag) {
		return flags.get(flag);
	}
	
	public Boolean setFlag(final String flag, final Boolean allow) {
		return flags.put(flag, allow);
	}
	
	public boolean hasFlag(final String flag) {
		return flags.containsKey(flag);
	}
	
	public boolean addBlockBuildExclusion(final BlockType blockType) {
		return blockBuildExclusions.add(blockType);
	}
	
	public boolean removeBlockBuildExclusion(final BlockType blockType) {
		return blockBuildExclusions.remove(blockType);
	}
	
	public boolean hasBlockBuildExclusion(final BlockType blockType) {
		return blockBuildExclusions.contains(blockType);
	}
	
	public boolean addBlockInteractExclusion(final BlockType blockType) {
		return blockInteractExclusions.add(blockType);
	}
	
	public boolean removeBlockInteractExclusion(final BlockType blockType) {
		return blockInteractExclusions.remove(blockType);
	}
	
	public boolean hasBlockInteractExclusion(final BlockType blockType) {
		return blockInteractExclusions.contains(blockType);
	}
	
	public boolean addEntityCreateExclusion(final Class<? extends Entity> entityClass) {
		return entityCreateExclusions.add(entityClass);
	}
	
	public boolean removeEntityCreateExclusion(final Class<? extends Entity> entityClass) {
		return entityCreateExclusions.remove(entityClass);
	}
	
	public boolean hasEntityCreateExclusion(final Class<? extends Entity> entityClass) {
		return entityCreateExclusions.contains(entityClass);
	}
	
	public boolean addEntityInteractExclusion(final Class<? extends Entity> entityClass) {
		return entityInteractExclusions.add(entityClass);
	}
	
	public boolean removeEntityInteractExclusion(final Class<? extends Entity> entityClass) {
		return entityInteractExclusions.remove(entityClass);
	}
	
	public boolean hasEntityInteractExclusion(final Class<? extends Entity> entityClass) {
		return entityInteractExclusions.contains(entityClass);
	}
	
	public boolean addCommandRestriction(final String command) {
		return commandRestrictions.add(command);
	}
	
	public boolean removeCommandRestriction(final String command) {
		return commandRestrictions.remove(command);
	}
	
	public boolean hasCommandRestriction(final String command) {
		return commandRestrictions.contains(command);
	}
	
	@Override
	public Configuration clone() {
		final Configuration config = new Configuration();
		config.ownerPermissions.addAll(ownerPermissions);
		config.flags.putAll(flags);
		config.blockBuildExclusions.addAll(blockBuildExclusions);
		config.blockInteractExclusions.addAll(blockInteractExclusions);
		config.entityCreateExclusions.addAll(entityCreateExclusions);
		config.entityInteractExclusions.addAll(entityInteractExclusions);
		config.commandRestrictions.addAll(commandRestrictions);
		return config;
	}
}
