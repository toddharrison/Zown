package com.eharrison.canary.zown.listener;

import net.canarymod.api.entity.Entity;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.entity.DamageHook;
import net.canarymod.hook.entity.EntityLightningStruckHook;
import net.canarymod.hook.entity.ProjectileHitHook;
import net.canarymod.plugin.PluginListener;
import net.canarymod.plugin.Priority;

import com.eharrison.canary.zown.Flag;
import com.eharrison.canary.zown.api.IZown;
import com.eharrison.canary.zown.api.IZownManager;
import com.eharrison.canary.zown.api.impl.Tree;

public class PlayerListener implements PluginListener {
	private final IZownManager zownManager;
	
	public PlayerListener(final IZownManager zownManager) {
		this.zownManager = zownManager;
	}
	
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
