package com.eharrison.canary.zown.api;

import net.canarymod.api.entity.living.humanoid.Player;

/**
 * A Zown is an object that specifies a discrete three-dimensional location in the World. It can
 * inherit Configuration from a Template, override that template, or define its own Configuration.
 * A Zown has owner and member Players and may specify entry restrictions for Players.
 * 
 * @author Todd Harrison
 *
 */
public interface IZown extends IConfigurable {
	/**
	 * Get the name of this Zown.
	 * 
	 * @return The Zown name.
	 */
	String getName();
	
	/**
	 * Get the Template of this Zown, or null if there is no Template.
	 * 
	 * @return The Template.
	 */
	ITemplate getTemplate();
	
	/**
	 * Determine if this Zown overrides the Configuration from its parent Template.
	 * 
	 * @return True if the Template is overridden, false if not or no Template present.
	 */
	boolean overridesConfiguration();
	
	/**
	 * Sets if this Zown overrides the Template or not. This will copy the Template Configuration into
	 * the Zown if overrideConfiguration is true and a Template is present. Any overridden Zown
	 * Configuration values will be lost if overridesConfiguration is false.
	 * 
	 * @param overridesConfiguration
	 *          If this Zown overrides its Template.
	 * @return True if the override value was changed, false otherwise.
	 */
	boolean setOverridesConfiguration(final boolean overridesConfiguration);
	
	/**
	 * Adds an owner Player to this Zown. Will remove a Player from the members list.
	 * 
	 * @param player
	 *          The Player to add.
	 * @return True if the Player was added, false if already present.
	 */
	boolean addOwner(final Player player);
	
	/**
	 * Removes an owner Player from this Zown.
	 * 
	 * @param player
	 *          The Player to remove.
	 * @return True if the Player was removed, false if not present.
	 */
	boolean removeOwner(final Player player);
	
	/**
	 * Determine if the specified Player is an owner of this Zown.
	 * 
	 * @param player
	 *          The Player.
	 * @return True if the Player is an owner, false otherwise.
	 */
	boolean isOwner(final Player player);
	
	/**
	 * Adds a member Player to this Zown. Will remove a Player from the owners list.
	 * 
	 * @param player
	 *          The Player to add.
	 * @return True if the Player was added, false if already present.
	 */
	boolean addMember(final Player player);
	
	/**
	 * Removes a member Player from this Zown.
	 * 
	 * @param player
	 *          The Player to remove.
	 * @return True if the Player was removed, false if not present.
	 */
	boolean removeMember(final Player player);
	
	/**
	 * Determine if the specified Player is a member of this Zown.
	 * 
	 * @param player
	 *          The Player.
	 * @return True if the Player is a member, false otherwise.
	 */
	boolean isMember(final Player player);
	
	/**
	 * Determine if the specified Player is an owner or member of this Zown.
	 * 
	 * @param player
	 *          The Player.
	 * @return True if the Player is a member or owner, false otherwise.
	 */
	boolean isOwnerOrMember(final Player player);
	
	/**
	 * Adds an entry exclusion for the specified Player to this Zown.
	 * 
	 * @param player
	 *          The Player to exclude.
	 * @return True if the entry exclusion is added, false if already present.
	 */
	boolean addEntryExclusion(final Player player);
	
	/**
	 * Removes an entry exclusion for the specified Player from this Zown.
	 * 
	 * @param player
	 *          The Player.
	 * @return True if the entry exclusion is removed, false if not present.
	 */
	boolean removeEntryExclusion(final Player player);
	
	/**
	 * Determine if the specified Player has an entry exclusion for this Zown.
	 * 
	 * @param player
	 *          The Player.
	 * @return True if there is an entry exclusion, false otherwise.
	 */
	boolean hasEntryExclusion(final Player player);
	
	/**
	 * Get the minimum three-dimensional World inclusive Point for this Zown.
	 * 
	 * @return The minimum inclusive Point.
	 */
	Point getMinPoint();
	
	/**
	 * Get the maximum three-dimensional World inclusive Point for this Zown.
	 * 
	 * @return The maximum inclusive Point.
	 */
	Point getMaxPoint();
	
	/**
	 * Get the center three-dimensional World Point for this Zown. If there is no exact center Point
	 * then it will be in the smaller direction.
	 * 
	 * @return The center Point of this Zown.
	 */
	Point getCenterPoint();
	
	/**
	 * Determine if a Point is contained by this Zown.
	 * 
	 * @param point
	 *          The Point.
	 * @return True if this Zown contains the Point, false otherwise.
	 */
	boolean contains(final Point point);
	
	/**
	 * Determine if two Points are contained by this Zown.
	 * 
	 * @param p1
	 *          The first Point.
	 * @param p2
	 *          The second Point.
	 * @return True if this Zown contains both Points, false otherwise.
	 */
	boolean contains(final Point p1, final Point p2);
	
	/**
	 * Determine if a Zown is contained completely by this Zown. The contained Zown may completely
	 * overlap.
	 * 
	 * @param zown
	 *          The Zown.
	 * @return True if this Zown completely contains the specifed Zown, false otherwise.
	 */
	boolean contains(final IZown zown);
	
	/**
	 * Determine if one of two Points, but not both, are contained by this Zown.
	 * 
	 * @param p1
	 *          The first Point.
	 * @param p2
	 *          The second Point.
	 * @return True if the Zown contains one of the Points, but not both.
	 */
	boolean intersects(final Point p1, final Point p2);
	
	/**
	 * Determine if a Zown intersects this Zown. To intersect it must overlap this Zown but not be
	 * completely contained by it.
	 * 
	 * @param zown
	 *          The Zown.
	 * @return True if the Zown overlaps this Zown, false otherwise.
	 */
	boolean intersects(final IZown zown);
	
	/**
	 * Get the number of block coordinates contained by this Zown. This includes x, y, and z
	 * coordinates.
	 * 
	 * @return The number of blocks in this Zown.
	 */
	int getBlockCount();
	
	/**
	 * Get the number of columns of block coordinates contained by this Zown. This includes x and z
	 * coordinates.
	 * 
	 * @return The number of block columns in this Zown.
	 */
	int getColumnCount();
}
