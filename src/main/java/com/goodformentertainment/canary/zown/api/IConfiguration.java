package com.goodformentertainment.canary.zown.api;

import net.canarymod.api.entity.Entity;
import net.canarymod.api.world.blocks.BlockType;

/**
 * A Configuration is a composition object that contains configuration information. This
 * Configuration includes things like owner permissions, flags, exclusions and command restrictions.
 * Additionally, it implements Cloneable to make copies of this Configuration.
 * 
 * @author Todd Harrison
 */
public interface IConfiguration extends Cloneable {
	/**
	 * Adds an owner permission to change the specified flag to this Configuration.
	 * 
	 * @param flag
	 *          The flag name.
	 * @return True if the owner permission was added, false if it already exists.
	 */
	boolean addOwnerPermission(final String flag);
	
	/**
	 * Removes an owner permission to change the specified flag in this Configuration.
	 * 
	 * @param flag
	 *          The flag name.
	 * @return True if the owner permission was removed, false if it was not present.
	 */
	boolean removeOwnerPermission(final String flag);
	
	/**
	 * Determine if an owner has permission to change the specified flag in this Configuration.
	 * 
	 * @param flag
	 *          The flag name.
	 * @return True if the owner can change the flag, false otherwise.
	 */
	boolean hasOwnerPermission(final String flag);
	
	/**
	 * Gets a flag value from this Configuration.
	 * 
	 * @param flag
	 *          The flag name.
	 * @return The value of the flag, or null if not set.
	 */
	Boolean getFlag(final String flag);
	
	/**
	 * Sets a flag on this Configuration.
	 * 
	 * @param flag
	 *          The flag name.
	 * @param allow
	 *          The value of the flag, or null to remove.
	 * @return The original value of the flag.
	 */
	Boolean setFlag(final String flag, final Boolean allow);
	
	/**
	 * Determine if a flag has been set in this Configuration.
	 * 
	 * @param flag
	 *          The flag name.
	 * @return True if the flag is set, false otherwise.
	 */
	boolean hasFlag(final String flag);
	
	/**
	 * Adds a block build exclusion to this Configuration.
	 * 
	 * @param blockType
	 *          The BlockType to exclude.
	 * @return True if the block build exclusion was added, false if it already exists.
	 */
	boolean addBlockBuildExclusion(final BlockType blockType);
	
	/**
	 * Removes a block build exclusion from this Configuration.
	 * 
	 * @param blockType
	 *          The BlockType to exclude.
	 * @return True if the block build exclusion was removed, false if it was not present.
	 */
	boolean removeBlockBuildExclusion(final BlockType blockType);
	
	/**
	 * Determine if a block build exclusion has been set in this Configuration.
	 * 
	 * @param blockType
	 *          The BlockType to exclude.
	 * @return True if the block build exclusion exists, false otherwise.
	 */
	boolean hasBlockBuildExclusion(final BlockType blockType);
	
	/**
	 * Adds a block interaction exclusion to this Configuration.
	 * 
	 * @param blockType
	 *          The BlockType to exclude.
	 * @return True if the block interaction exclusion was added, false if it already exists.
	 */
	boolean addBlockInteractExclusion(final BlockType blockType);
	
	/**
	 * Removes a block interaction exclusion from this Configuration.
	 * 
	 * @param blockType
	 *          The BlockType to exclude.
	 * @return True if the block interaction exclusion was removed, false if it was not present.
	 */
	boolean removeBlockInteractExclusion(final BlockType blockType);
	
	/**
	 * Determine if a block interaction exclusion has been set in this Configuration.
	 * 
	 * @param blockType
	 *          The BlockType to exclude.
	 * @return True if the block interaction exclusion exists, false otherwise.
	 */
	boolean hasBlockInteractExclusion(final BlockType blockType);
	
	/**
	 * Adds an entity creation exclusion to this Configuration.
	 * 
	 * @param entityClass
	 *          The Entity class to exclude.
	 * @return True if the entity creation exclusion was added, false if it already exists.
	 */
	boolean addEntityCreateExclusion(final Class<? extends Entity> entityClass);
	
	/**
	 * Removes an entity creation exclusion from this Configuration.
	 * 
	 * @param entityClass
	 *          The Entity class to exclude.
	 * @return True if the entity creation exclusion was removed, false if it was not present.
	 */
	boolean removeEntityCreateExclusion(final Class<? extends Entity> entityClass);
	
	/**
	 * Determine if an entity creation exclusion has been set in this Configuration.
	 * 
	 * @param entityClass
	 *          The Entity class to exclude.
	 * @return True if the entity creation exclusion exists, false otherwise.
	 */
	boolean hasEntityCreateExclusion(final Class<? extends Entity> entityClass);
	
	/**
	 * Adds an entity interaction exclusion to this Configuration.
	 * 
	 * @param entityClass
	 *          The Entity class to exclude.
	 * @return True if the entity interaction exclusion was added, false if it already exists.
	 */
	boolean addEntityInteractExclusion(final Class<? extends Entity> entityClass);
	
	/**
	 * Removes an entity interaction exclusion from this Configuration.
	 * 
	 * @param entityClass
	 *          The Entity class to exclude.
	 * @return True if the entity interaction exclusion was removed, false if it was not present.
	 */
	boolean removeEntityInteractExclusion(final Class<? extends Entity> entityClass);
	
	/**
	 * Determine if an entity interaction exclusion has been set in this Configuration.
	 * 
	 * @param entityClass
	 *          The Entity class to exclude.
	 * @return True if the entity interaction exclusion exists, false otherwise.
	 */
	boolean hasEntityInteractExclusion(final Class<? extends Entity> entityClass);
	
	/**
	 * Adds a command restriction to this Configuration.
	 * 
	 * @param command
	 *          The command string, starting with a "/".
	 * @return True if the command restriction was added, false if it already exists.
	 */
	boolean addCommandRestriction(final String command);
	
	/**
	 * Removes a command restriction from this Configuration.
	 * 
	 * @param command
	 *          The command string, starting with a "/".
	 * @return True if the command restriction was removed, false if it was not present.
	 */
	boolean removeCommandRestriction(final String command);
	
	/**
	 * Determine if a command restriction has been set in this Configuration.
	 * 
	 * @param command
	 *          The command string, starting with a "/".
	 * @return True if the command is restricted, false otherwise.
	 */
	boolean hasCommandRestriction(final String command);
	
	/**
	 * Clone this Configuration for use elsewhere.
	 * 
	 * @return A copy of this Configuration.
	 */
	public IConfiguration clone();
}
