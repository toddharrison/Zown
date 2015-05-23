package com.goodformentertainment.canary.zown.listener;

import java.util.HashMap;
import java.util.Map;

import net.canarymod.api.entity.Arrow;
import net.canarymod.api.entity.Entity;
import net.canarymod.api.entity.Projectile;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.entity.throwable.EntityThrowable;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.entity.DamageHook;
import net.canarymod.hook.entity.EntityLightningStruckHook;
import net.canarymod.hook.entity.ProjectileHitHook;
import net.canarymod.hook.player.PlayerMoveHook;
import net.canarymod.plugin.PluginListener;
import net.canarymod.plugin.Priority;

import com.goodformentertainment.canary.zown.Flag;
import com.goodformentertainment.canary.zown.api.IZown;
import com.goodformentertainment.canary.zown.api.IZownManager;
import com.goodformentertainment.canary.zown.api.impl.Tree;

public class PlayerListener implements PluginListener {
	private final IZownManager zownManager;
	// TODO: Use this map for player location
	private final Map<String, Tree<? extends IZown>> playerZownMap;
	
	public PlayerListener(final IZownManager zownManager) {
		this.zownManager = zownManager;
		playerZownMap = new HashMap<String, Tree<? extends IZown>>();
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onPlayerMove(final PlayerMoveHook hook) {
		final Player player = hook.getPlayer();
		
		Tree<? extends IZown> zownTree = playerZownMap.get(player.getUUIDString());
		if (zownTree == null) {
			zownTree = zownManager.getZown(player.getLocation());
			playerZownMap.put(player.getUUIDString(), zownTree);
		} else {
			final Tree<? extends IZown> targetZownTree = zownManager.getZown(hook.getTo());
			if (zownTree != targetZownTree) {
				// TODO check if the player is authorized to enter the zown
				
				// Changed zown
				playerZownMap.put(player.getUUIDString(), targetZownTree);
				final IZown zown = zownTree.getData();
				final IZown targetZown = targetZownTree.getData();
				// player.message("Zown " + zown.getName() + " to " + targetZown.getName());
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
	
	// TODO observe tp and connect for zown change
	
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
			// TODO optimization
			// final Player player = (Player) target;
			// final Tree<? extends IZown> zownTree = playerZownMap.get(player.getUUIDString());
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
			// TODO optimization
			// final Player player = (Player) target;
			// final Tree<? extends IZown> zownTree = playerZownMap.get(player.getUUIDString());
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
			// TODO optimization
			// final Player player = (Player) target;
			// final Tree<? extends IZown> zownTree = playerZownMap.get(player.getUUIDString());
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
}