package com.goodformentertainment.canary.zown.api;

import com.goodformentertainment.canary.zown.api.impl.Tree;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.World;
import net.canarymod.api.world.position.Location;

/**
 * A ZownManager manages a Collection of Zowns.
 *
 * @author Todd Harrison
 */
public interface IZownManager {
    /**
     * Loads the Zowns from the database for the specified World.
     *
     * @param world The World.
     */
    void loadZowns(World world);

    /**
     * Unloads the Zowns for the specified World. Does not automatically save the Zowns.
     *
     * @param world The World.
     */
    void unloadZowns(World world);

    /**
     * Determine if the specified World has loaded the root Zown Tree.
     *
     * @param world The WOrld.
     * @return True if the Zowns for the World have been loaded, false otherwise.
     */
    boolean isLoaded(World world);

    /**
     * Gets the top-level World Tree of Zowns for the specified World.
     *
     * @param world The World.
     * @return The top-level Tree of Zowns of the specified World.
     */
    Tree<? extends IZown> getZown(World world);

    /**
     * Gets the named Tree of Zowns for the specified World.
     *
     * @param world The World.
     * @param name  The Zown name.
     * @return The named Tree of Zowns.
     */
    Tree<? extends IZown> getZown(World world, String name);

    /**
     * Gets the Tree of Zowns for the specified Location.
     *
     * @param location The Location.
     * @return The Tree of Zowns for this Location.
     */
    Tree<? extends IZown> getZown(Location location);

    /**
     * Create a new Zown.
     *
     * @param world    The World.
     * @param name     The Zown name.
     * @param template The Template for the Zown, or null.
     * @param p1       The first inclusive Point bounds.
     * @param p2       The second inclusive Point bounds.
     * @return The newly created Tree of Zowns, or null if the Zown overlapped or had the same name.
     */
    Tree<? extends IZown> createZown(World world, String name, ITemplate template, Point p1, Point p2);

    Tree<? extends IZown> createZown(World world, String name, ITemplate template, Point p1,
                                     Point p2, Player player);

    /**
     * Remove the named Zown and it's children Zowns from the specified World.
     *
     * @param world The World.
     * @param name  The Zown name.
     * @return True if the Zown was removed, false if not present.
     */
    boolean removeZown(World world, String name);

    boolean removeZown(World world, String name, Player player);

    /**
     * Rename the specified Zown in the specified World.
     *
     * @param world   The World.
     * @param oldName The current name of the Zown.
     * @param newName The new name for the Zown.
     * @return True if the Zown was renamed, false if the name was already used.
     */
    boolean renameZown(World world, String oldName, String newName);

    boolean renameZown(World world, String oldName, String newName, Player player);

    /**
     * Resize the specified Zown to the new bounding Points.
     *
     * @param world The World.
     * @param name  The Zown name.
     * @param p1    The first inclusive Point bounds.
     * @param p2    The second inclusive Point bounds.
     * @return True if the Zown was resized, false if resizing caused an overlap with other Zowns.
     */
    boolean resizeZown(World world, String name, Point p1, Point p2);

    boolean resizeZown(World world, String name, Point p1, Point p2, Player player);

    /**
     * Applies a Template to a Zown, overwriting any current Configuration on the Zown.
     *
     * @param world    The World.
     * @param name     The Zown name.
     * @param template The Template name.
     * @return True if the Template was applied, false if Zone is not present.
     */
    boolean applyTemplate(World world, String name, ITemplate template);

    /**
     * Save this Zown and its Configuration to the database.
     *
     * @param world The World.
     * @param name  The Zown name.
     * @return True if the Zown was saved, false if not present.
     */
    boolean saveZownConfiguration(World world, String name);
}
