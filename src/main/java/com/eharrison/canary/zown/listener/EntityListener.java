package com.eharrison.canary.zown.listener;

import net.canarymod.api.entity.Entity;
import net.canarymod.api.entity.living.LivingBase;
import net.canarymod.api.entity.living.animal.EntityAnimal;
import net.canarymod.api.entity.living.humanoid.Villager;
import net.canarymod.api.entity.living.monster.EntityMob;
import net.canarymod.api.world.position.Location;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.entity.DamageHook;
import net.canarymod.hook.entity.EntityLightningStruckHook;
import net.canarymod.hook.entity.EntityMoveHook;
import net.canarymod.hook.entity.EntitySpawnHook;
import net.canarymod.hook.entity.MobTargetHook;
import net.canarymod.hook.entity.ProjectileHitHook;
import net.canarymod.plugin.PluginListener;
import net.canarymod.plugin.Priority;

import com.eharrison.canary.zown.Flag;
import com.eharrison.canary.zown.api.IZown;
import com.eharrison.canary.zown.api.IZownManager;
import com.eharrison.canary.zown.api.impl.Tree;

public class EntityListener implements PluginListener {
	private final IZownManager zownManager;
	
	public EntityListener(final IZownManager zownManager) {
		this.zownManager = zownManager;
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onEntityMove(final EntityMoveHook hook) {
		final Entity entity = hook.getEntity();
		final Location location = hook.getTo();
		
		if (entity instanceof EntityMob) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(location);
			final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.hostilepermit.name());
			if (flag != null && !flag) {
				entity.destroy();
				// hook.setCanceled();
			}
		} else if (entity instanceof EntityAnimal) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(location);
			final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.passivepermit.name());
			if (flag != null && !flag) {
				// entity.destroy();
				// TODO causes entity tick error
				// hook.setCanceled();
				
				entity.teleportTo(hook.getFrom());
				
				// // TODO efficiency?
				// TODO this no work for long thin zowns
				// final Location loc = entity.getLocation();
				// final Point centerPoint = zownTree.getData().getCenterPoint();
				// final Vector3D v = new Vector3D(centerPoint.x, loc.getBlockY(), centerPoint.z);
				// Vector3D diff = loc.subtract(v);
				// diff = diff.multiply(1 / diff.getMagnitude());
				//
				// // Move the entity out of the zown
				// entity.teleportTo(loc.add(diff));
			}
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onEntitySpawn(final EntitySpawnHook hook) {
		final Entity entity = hook.getEntity();
		final Location location = entity.getLocation();
		
		if (entity instanceof EntityMob) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(location);
			final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.hostilepermit.name());
			if (flag != null && !flag) {
				hook.setCanceled();
			}
		} else if (entity instanceof EntityAnimal) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(location);
			final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.passivepermit.name());
			if (flag != null && !flag) {
				hook.setCanceled();
			}
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onTarget(final MobTargetHook hook) {
		final Entity entity = hook.getEntity();
		final LivingBase target = hook.getTarget();
		
		if (entity instanceof EntityMob) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(target.getLocation());
			final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.hostilepermit.name());
			if (flag != null && !flag) {
				hook.setCanceled();
			}
		} else if (entity instanceof EntityAnimal) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(target.getLocation());
			final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.passivepermit.name());
			if (flag != null && !flag) {
				hook.setCanceled();
			}
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onProjectileHit(final ProjectileHitHook hook) {
		final Entity target = hook.getEntityHit();
		
		if (target != null) {
			if (target.isAnimal()) {
				final Tree<? extends IZown> zownTree = zownManager.getZown(target.getLocation());
				final Boolean flag = zownTree.getData().getConfiguration()
						.getFlag(Flag.animalimmune.name());
				if (flag != null && flag) {
					hook.setCanceled();
				}
			} else if (target instanceof Villager) {
				final Tree<? extends IZown> zownTree = zownManager.getZown(target.getLocation());
				final Boolean flag = zownTree.getData().getConfiguration()
						.getFlag(Flag.villagerimmune.name());
				if (flag != null && flag) {
					hook.setCanceled();
				}
			}
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onEntityLightningStruck(final EntityLightningStruckHook hook) {
		final Entity target = hook.getStruckEntity();
		
		if (target.isAnimal()) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(target.getLocation());
			final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.animalimmune.name());
			if (flag != null && flag) {
				hook.setCanceled();
			}
		} else if (target instanceof Villager) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(target.getLocation());
			final Boolean flag = zownTree.getData().getConfiguration()
					.getFlag(Flag.villagerimmune.name());
			if (flag != null && flag) {
				hook.setCanceled();
			}
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onDamage(final DamageHook hook) {
		final Entity target = hook.getDefender();
		
		if (target.isAnimal()) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(target.getLocation());
			final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.animalimmune.name());
			if (flag != null && flag) {
				hook.setCanceled();
			}
		} else if (target instanceof Villager) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(target.getLocation());
			final Boolean flag = zownTree.getData().getConfiguration()
					.getFlag(Flag.villagerimmune.name());
			if (flag != null && flag) {
				hook.setCanceled();
			}
		}
	}
}
