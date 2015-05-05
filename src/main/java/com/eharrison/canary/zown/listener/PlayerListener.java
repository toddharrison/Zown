package com.eharrison.canary.zown.listener;

import java.util.HashMap;
import java.util.Map;

import net.canarymod.api.entity.Entity;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.entity.DamageHook;
import net.canarymod.hook.entity.EntityLightningStruckHook;
import net.canarymod.hook.entity.ProjectileHitHook;
import net.canarymod.hook.player.PlayerMoveHook;
import net.canarymod.plugin.PluginListener;
import net.canarymod.plugin.Priority;

import com.eharrison.canary.zown.Flag;
import com.eharrison.canary.zown.api.IZown;
import com.eharrison.canary.zown.api.IZownManager;
import com.eharrison.canary.zown.api.impl.Tree;

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
				player.message("Zown " + zown.getName() + " to " + targetZown.getName());
				if (zown.getFarewellMessage() != null && !zown.getFarewellMessage().isEmpty()) {
					player.message(zown.getFarewellMessage());
				}
				if (targetZown.getWelcomeMessage() != null && !zown.getWelcomeMessage().isEmpty()) {
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
		
		if (target != null && target.isPlayer()) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(target.getLocation());
			final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.playerimmune.name());
			if (flag != null && flag) {
				hook.setCanceled();
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
		
		if (target.isPlayer()) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(target.getLocation());
			final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.playerimmune.name());
			if (flag != null && flag) {
				hook.setCanceled();
			}
		}
	}
}
