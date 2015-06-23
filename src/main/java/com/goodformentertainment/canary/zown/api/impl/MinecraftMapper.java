package com.goodformentertainment.canary.zown.api.impl;

import net.canarymod.api.entity.ArmorStand;
import net.canarymod.api.entity.Entity;
import net.canarymod.api.entity.hanging.ItemFrame;
import net.canarymod.api.entity.hanging.LeashKnot;
import net.canarymod.api.entity.hanging.Painting;
import net.canarymod.api.entity.living.animal.EntityAnimal;
import net.canarymod.api.entity.living.humanoid.Villager;
import net.canarymod.api.entity.living.monster.EntityMob;
import net.canarymod.api.entity.vehicle.Vehicle;
import net.canarymod.api.world.blocks.BlockType;

public final class MinecraftMapper {
    public static BlockType getBlockType(final String block) {
        return BlockType.fromString(block);
    }

    public static String getBlock(final BlockType blockType) {
        return blockType.getMachineName();
    }

    public static Class<? extends Entity> getEntityClass(final String entity) {
        Class<? extends Entity> entityClass = null;
        if ("minecraft:armor_stand".equals(entity)) {
            entityClass = ArmorStand.class;
        } else if ("minecraft:item_frame".equals(entity)) {
            entityClass = ItemFrame.class;
        } else if ("minecraft:leash_knot".equals(entity)) {
            entityClass = LeashKnot.class;
        } else if ("minecraft:painting".equals(entity)) {
            entityClass = Painting.class;
        } else if ("minecraft:entity_animal".equals(entity)) {
            entityClass = EntityAnimal.class;
        } else if ("minecraft:villager".equals(entity)) {
            entityClass = Villager.class;
        } else if ("minecraft:entity_mob".equals(entity)) {
            entityClass = EntityMob.class;
        } else if ("minecraft:vehicle".equals(entity)) {
            entityClass = Vehicle.class;
        }
        return entityClass;
    }

    public static String getEntity(final Class<? extends Entity> entityClass) {
        String entity = null;
        if (entityClass == ArmorStand.class) {
            entity = "minecraft:armor_stand";
        } else if (entityClass == ItemFrame.class) {
            entity = "minecraft:item_frame";
        } else if (entityClass == LeashKnot.class) {
            entity = "minecraft:leash_knot";
        } else if (entityClass == Painting.class) {
            entity = "minecraft:painting";
        } else if (entityClass == EntityAnimal.class) {
            entity = "minecraft:entity_animal";
        } else if (entityClass == Villager.class) {
            entity = "minecraft:villager";
        } else if (entityClass == EntityMob.class) {
            entity = "minecraft:entity_mob";
        } else if (entityClass == Vehicle.class) {
            entity = "minecraft:vehicle";
        }
        return entity;
    }
}
