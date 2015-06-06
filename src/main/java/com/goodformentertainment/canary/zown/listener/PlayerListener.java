package com.goodformentertainment.canary.zown.listener;

import java.util.HashSet;
import java.util.Set;

import net.canarymod.Canary;
import net.canarymod.api.GameMode;
import net.canarymod.api.entity.Arrow;
import net.canarymod.api.entity.Entity;
import net.canarymod.api.entity.Projectile;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.entity.throwable.EntityThrowable;
import net.canarymod.api.inventory.ItemType;
import net.canarymod.api.world.position.Location;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.entity.DamageHook;
import net.canarymod.hook.entity.EntityLightningStruckHook;
import net.canarymod.hook.entity.ProjectileHitHook;
import net.canarymod.hook.player.ItemUseHook;
import net.canarymod.hook.player.PlayerMoveHook;
import net.canarymod.hook.player.TeleportHook;
import net.canarymod.hook.player.TeleportHook.TeleportCause;
import net.canarymod.plugin.PluginListener;
import net.canarymod.plugin.Priority;
import net.canarymod.tasks.ServerTask;

import com.goodformentertainment.canary.zown.Flag;
import com.goodformentertainment.canary.zown.api.IZown;
import com.goodformentertainment.canary.zown.api.IZownManager;
import com.goodformentertainment.canary.zown.api.Point;
import com.goodformentertainment.canary.zown.api.impl.Tree;

public class PlayerListener implements PluginListener {
	private final IZownManager zownManager;
	private final Set<Player> pearlingPlayers;
	
	public PlayerListener(final IZownManager zownManager) {
		this.zownManager = zownManager;
		pearlingPlayers = new HashSet<Player>();
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onPlayerMove(final PlayerMoveHook hook) {
		final Player player = hook.getPlayer();
		final Tree<? extends IZown> fromZownTree = zownManager.getZown(hook.getFrom());
		final Tree<? extends IZown> targetZownTree = zownManager.getZown(hook.getTo());
		
		// Check if the player is authorized to be in the target zown
		if (!player.isOperator() && targetZownTree.getData().hasEntryExclusion(player)) {
			final Location fromLoc = hook.getFrom();
			final Point minPoint = targetZownTree.getData().getMinPoint();
			final Point maxPoint = targetZownTree.getData().getMaxPoint();
			
			double fromX = fromLoc.getX();
			double fromY = fromLoc.getY();
			double fromZ = fromLoc.getZ();
			
			final double insideMinX = fromX - minPoint.x;
			final double insideMaxX = maxPoint.x - fromX;
			final double insideMinY = fromY - minPoint.y;
			final double insideMaxY = maxPoint.y - fromY;
			final double insideMinZ = fromZ - minPoint.z;
			final double insideMaxZ = maxPoint.z - fromZ;
			
			final double bump = 1.0;
			
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
			
			// Position toward outside of the zown
			player.teleportTo(fromX, fromY, fromZ, fromLoc.getPitch(), fromLoc.getRotation());
		} else if (fromZownTree != targetZownTree) {
			// Changed zown
			final Boolean flag = fromZownTree.getData().getConfiguration()
					.getFlag(Flag.playerexit.name());
			if (!player.isOperator() && flag != null && !flag) {
				final Location fromLoc = hook.getFrom();
				hook.setCanceled();
				// TODO: Remove when Canary fixes bug of popping up when move is cancelled
				Canary.getServer().addSynchronousTask(new ServerTask(Canary.getServer(), 0) {
					@Override
					public void run() {
						player.teleportTo(fromLoc);
					}
				});
			} else {
				final IZown zown = fromZownTree.getData();
				final IZown targetZown = targetZownTree.getData();
				if (zown.getFarewellMessage() != null && !zown.getFarewellMessage().isEmpty()
						&& !zown.getFarewellMessage().equals("null")) {
					player.message(zown.getFarewellMessage());
				}
				if (targetZown.getWelcomeMessage() != null && !targetZown.getWelcomeMessage().isEmpty()
						&& !targetZown.getWelcomeMessage().equals("null")) {
					player.message(targetZown.getWelcomeMessage());
				}
				// TODO send PlayerZownExit and PlayerZownEntry
				// TODO allow for cancel of player move
			}
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onProjectileHit(final ProjectileHitHook hook) {
		final Entity target = hook.getEntityHit();
		final Projectile projectile = (Projectile) hook.getProjectile();
		
		Entity attacker = null;
		if (projectile instanceof Arrow) {
			attacker = ((Arrow) projectile).getOwner();
		} else if (projectile instanceof EntityThrowable) {
			attacker = ((EntityThrowable) projectile).getThrower();
		}
		
		if (target != null && target.isPlayer()) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(target.getLocation());
			if (attacker == null) {
				final Boolean flag = zownTree.getData().getConfiguration()
						.getFlag(Flag.playerimmune.name());
				if (flag != null && flag) {
					hook.setCanceled();
				}
			} else if (attacker.isPlayer()) {
				final Tree<? extends IZown> attackerZownTree = zownManager.getZown(attacker.getLocation());
				final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.pvp.name());
				final Boolean attackerFlag = attackerZownTree.getData().getConfiguration()
						.getFlag(Flag.pvp.name());
				if (flag != null && !flag || attackerFlag != null && !attackerFlag) {
					hook.setCanceled();
				}
			} else if (attacker.isMob()) {
				final Boolean flag = zownTree.getData().getConfiguration()
						.getFlag(Flag.hostilecombat.name());
				if (flag != null && !flag) {
					hook.setCanceled();
				}
			}
		}
	}
	
	// @HookHandler(priority = Priority.CRITICAL)
	// public void onPotionEffectApplied(final PotionEffectAppliedHook hook) {
	// final Entity target = hook.getEntity();
	//
	// if (target != null && target.isPlayer()) {
	// final Tree<? extends IZown> zownTree = zownManager.getZown(target.getLocation());
	// final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.playerimmune.name());
	// if (flag != null && flag) {
	// // hook.setCanceled();
	// }
	// }
	// }
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onEntityLightningStruck(final EntityLightningStruckHook hook) {
		final Entity target = hook.getStruckEntity();
		
		if (target.isPlayer()) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(target.getLocation());
			final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.playerimmune.name());
			if (flag != null && flag) {
				hook.setCanceled();
			}
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onDamage(final DamageHook hook) {
		final Entity target = hook.getDefender();
		final Entity attacker = hook.getAttacker();
		
		if (target.isPlayer()) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(target.getLocation());
			if (attacker == null) {
				final Boolean flag = zownTree.getData().getConfiguration()
						.getFlag(Flag.playerimmune.name());
				if (flag != null && flag) {
					hook.setCanceled();
				}
			} else if (attacker.isPlayer()) {
				final Tree<? extends IZown> attackerZownTree = zownManager.getZown(attacker.getLocation());
				final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.pvp.name());
				final Boolean attackerFlag = attackerZownTree.getData().getConfiguration()
						.getFlag(Flag.pvp.name());
				if (flag != null && !flag || attackerFlag != null && !attackerFlag) {
					hook.setCanceled();
				}
			} else if (attacker.isMob()) {
				final Boolean flag = zownTree.getData().getConfiguration()
						.getFlag(Flag.hostilecombat.name());
				if (flag != null && !flag) {
					hook.setCanceled();
				}
			}
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onItemUse(final ItemUseHook hook) {
		final Player player = hook.getPlayer();
		if (player.getMode() != GameMode.CREATIVE && hook.getItem().getType() == ItemType.EnderPearl) {
			pearlingPlayers.add(player);
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onTeleport(final TeleportHook hook) {
		// Prevent use of ender pearls to violate playerexit and entry exclusion restrictions
		final Player player = hook.getPlayer();
		if (hook.getTeleportReason() == TeleportCause.MOVEMENT && pearlingPlayers.remove(player)) {
			final Tree<? extends IZown> fromZown = zownManager.getZown(hook.getCurrentLocation());
			final Tree<? extends IZown> toZown = zownManager.getZown(hook.getDestination());
			if (fromZown != toZown) {
				final Boolean flag = fromZown.getData().getConfiguration().getFlag(Flag.playerexit.name());
				if (flag != null && !flag || toZown.getData().hasEntryExclusion(player)) {
					hook.setCanceled();
				}
			}
		}
	}
}
