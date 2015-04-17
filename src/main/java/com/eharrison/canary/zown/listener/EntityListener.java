package com.eharrison.canary.zown.listener;

import net.canarymod.api.entity.Entity;
import net.canarymod.api.entity.living.animal.EntityAnimal;
import net.canarymod.api.entity.living.monster.EntityMob;
import net.canarymod.api.world.position.Location;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.entity.EntityMoveHook;
import net.canarymod.hook.entity.EntitySpawnHook;
import net.canarymod.plugin.PluginListener;
import net.canarymod.plugin.Priority;

import com.eharrison.canary.zown.api.IZown;
import com.eharrison.canary.zown.api.IZownManager;
import com.eharrison.canary.zown.api.impl.Tree;

public class EntityListener implements PluginListener {
	public static final String FLAG_HOSTILEPERMIT = "hostilepermit";
	public static final String FLAG_PASSIVEPERMIT = "passivepermit";
	
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
			final Boolean flag = zownTree.getData().getConfiguration().getFlag(FLAG_HOSTILEPERMIT);
			if (flag != null && !flag) {
				hook.setCanceled();
			}
		} else if (entity instanceof EntityAnimal) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(location);
			final Boolean flag = zownTree.getData().getConfiguration().getFlag(FLAG_PASSIVEPERMIT);
			if (flag != null && !flag) {
				hook.setCanceled();
			}
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onEntitySpawn(final EntitySpawnHook hook) {
		final Entity entity = hook.getEntity();
		final Location location = entity.getLocation();
		
		if (entity instanceof EntityMob) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(location);
			final Boolean flag = zownTree.getData().getConfiguration().getFlag(FLAG_HOSTILEPERMIT);
			if (flag != null && !flag) {
				hook.setCanceled();
			}
		} else if (entity instanceof EntityAnimal) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(location);
			final Boolean flag = zownTree.getData().getConfiguration().getFlag(FLAG_PASSIVEPERMIT);
			if (flag != null && !flag) {
				hook.setCanceled();
			}
		}
	}
}
