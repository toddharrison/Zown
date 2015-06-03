package com.goodformentertainment.canary.zown.listener;

import net.canarymod.api.entity.Arrow;
import net.canarymod.api.entity.Entity;
import net.canarymod.api.entity.living.LivingBase;
import net.canarymod.api.entity.living.animal.EntityAnimal;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.entity.living.humanoid.Villager;
import net.canarymod.api.entity.living.monster.EntityMob;
import net.canarymod.api.entity.throwable.EntityThrowable;
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

import com.goodformentertainment.canary.zown.Flag;
import com.goodformentertainment.canary.zown.api.IZown;
import com.goodformentertainment.canary.zown.api.IZownManager;
import com.goodformentertainment.canary.zown.api.Point;
import com.goodformentertainment.canary.zown.api.impl.Tree;

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
				final Location fromLoc = hook.getFrom();
				final Point minPoint = zownTree.getData().getMinPoint();
				final Point maxPoint = zownTree.getData().getMaxPoint();
				
				double fromX = fromLoc.getX();
				double fromY = fromLoc.getY();
				double fromZ = fromLoc.getZ();
				
				final double insideMinX = fromX - minPoint.x;
				final double insideMaxX = maxPoint.x - fromX;
				final double insideMinY = fromY - minPoint.y;
				final double insideMaxY = maxPoint.y - fromY;
				final double insideMinZ = fromZ - minPoint.z;
				final double insideMaxZ = maxPoint.z - fromZ;
				
				final double bump = 0.5;
				
				final double insideX = Math.min(insideMinX, insideMaxX);
				final double insideY = Math.min(insideMinY, insideMaxY);
				final double insideZ = Math.min(insideMinZ, insideMaxZ);
				
				if (insideX < insideY && insideX < insideZ) {
					if (insideMinX < insideMaxX) {
						// Closer to min x
						fromX -= bump;
					} else {
						// Closer to max x
						fromX += bump;
					}
				} else if (insideY < insideZ) {
					if (insideMinY < insideMaxY) {
						// Closer to min y
						fromY -= bump;
					} else {
						// Closer to max y
						fromY += bump;
					}
				} else {
					if (insideMinZ < insideMaxZ) {
						// Closer to min z
						fromZ -= bump;
					} else {
						// Closer to max z
						fromZ += bump;
					}
				}
				
				// Position outside of the zown
				entity.teleportTo(fromX, fromY, fromZ);
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
		final Entity projectile = hook.getProjectile();
		final Entity target = hook.getEntityHit();
		
		if (target != null) {
			Player player = null;
			if (projectile instanceof Arrow) {
				final Entity shooter = ((Arrow) projectile).getOwner();
				if (shooter.isPlayer()) {
					player = (Player) shooter;
				}
			} else if (projectile instanceof EntityThrowable) {
				final Entity thrower = ((EntityThrowable) projectile).getThrower();
				if (thrower.isPlayer()) {
					player = (Player) thrower;
				}
			}
			
			if (player == null || !player.isOperator()) {
				if (target.isAnimal()) {
					final Tree<? extends IZown> zownTree = zownManager.getZown(target.getLocation());
					if (player == null || !zownTree.getData().isOwnerOrMember(player)) {
						final Boolean flag = zownTree.getData().getConfiguration()
								.getFlag(Flag.animalimmune.name());
						if (flag != null && flag) {
							hook.setCanceled();
						}
					}
				} else if (target instanceof Villager) {
					final Tree<? extends IZown> zownTree = zownManager.getZown(target.getLocation());
					if (player == null || !zownTree.getData().isOwnerOrMember(player)) {
						final Boolean flag = zownTree.getData().getConfiguration()
								.getFlag(Flag.villagerimmune.name());
						if (flag != null && flag) {
							hook.setCanceled();
						}
					}
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
		final Entity attacker = hook.getAttacker();
		final Entity target = hook.getDefender();
		
		Player player = null;
		if (attacker != null && attacker.isPlayer()) {
			player = (Player) attacker;
		}
		
		if (player == null || !player.isOperator()) {
			if (target.isAnimal()) {
				final Tree<? extends IZown> zownTree = zownManager.getZown(target.getLocation());
				if (player == null || !zownTree.getData().isOwnerOrMember(player)) {
					final Boolean flag = zownTree.getData().getConfiguration()
							.getFlag(Flag.animalimmune.name());
					if (flag != null && flag) {
						hook.setCanceled();
					}
				}
			} else if (target instanceof Villager) {
				final Tree<? extends IZown> zownTree = zownManager.getZown(target.getLocation());
				if (player == null || !zownTree.getData().isOwnerOrMember(player)) {
					final Boolean flag = zownTree.getData().getConfiguration()
							.getFlag(Flag.villagerimmune.name());
					if (flag != null && flag) {
						hook.setCanceled();
					}
				}
			}
		}
	}
}
