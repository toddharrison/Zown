package com.goodformentertainment.canary.zown;

/**
 * The built-in flags supported by the PluginListeners included in the Zown Plugin.
 *
 * @author Todd Harrison
 */
public enum Flag {

    /**
     * DENY prevents players from leaving a Zown by trying to move outside. If they leave they will be
     * halted.
     */
    playerexit,

    /**
     * DENY prevents hostile mobs from entering or spawning within a Zown. If they enter they will be
     * removed.
     */
    hostilepermit,

    /**
     * DENY prevents passive mobs from entering or spawning within a Zown. If they enter they will be
     * halted.
     */
    passivepermit,

    /**
     * DENY prevents placement or destruction of blocks and entities within a Zown. This includes
     * explosions.
     */
    build,

    /**
     * DENY prevents liquids from flowing within a Zown.
     */
    flow,

    /**
     * DENY prevents setting or spreading of fire within a Zown.
     */
    firespread,

    /**
     * DENY prevents interaction with entities and objects within a Zown.
     */
    interact,

    /**
     * ALLOW makes a Player immune to environmental damage within a Zown.
     */
    playerimmune,

    /**
     * ALLOW lets Players attack other Players within a Zown.
     */
    pvp,

    /**
     * ALLOW lets Players and mobs combat within a Zown.
     */
    hostilecombat,

    /**
     * ALLOW lets Players create new Zowns within this Zown.
     */
    playerclaim,

    /**
     * ALLOW makes all Animals immune to attacks within a Zown;
     */
    animalimmune,

    /**
     * ALLOW makes all Villagers immune to attacks within a Zown.
     */
    villagerimmune
}
