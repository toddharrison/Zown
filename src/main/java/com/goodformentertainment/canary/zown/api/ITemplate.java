package com.goodformentertainment.canary.zown.api;

import java.util.Collection;

/**
 * A Template is a named object that contains a Configuration. It represents a reusable
 * Configuration that may be shared by and inherited from many Zowns. A Template is aware of which
 * Zowns inherit from it as well.
 *
 * @author Todd Harrison
 */
public interface ITemplate extends IConfigurable {
    /**
     * Get the name of this Template.
     *
     * @return The Template name.
     */
    String getName();

    /**
     * Get the display String for this Template. This is used to provide the display text of a
     * Template when requested by a Player;
     *
     * @return The Template display text.
     */
    String getDisplay();

    /**
     * Get a Collection of the Zowns that belong to this Template.
     *
     * @return A Collection of the Zowns.
     */
    Collection<? extends IZown> getZowns();
}
