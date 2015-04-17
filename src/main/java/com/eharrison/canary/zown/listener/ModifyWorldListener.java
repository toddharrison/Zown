package com.eharrison.canary.zown.listener;

import java.util.List;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.inventory.ItemType;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.position.Location;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.entity.EndermanDropBlockHook;
import net.canarymod.hook.entity.EndermanPickupBlockHook;
import net.canarymod.hook.player.BlockDestroyHook;
import net.canarymod.hook.player.BlockPlaceHook;
import net.canarymod.hook.player.ItemUseHook;
import net.canarymod.hook.world.ExplosionHook;
import net.canarymod.hook.world.FlowHook;
import net.canarymod.hook.world.IgnitionHook;
import net.canarymod.hook.world.LiquidDestroyHook;
import net.canarymod.plugin.PluginListener;
import net.canarymod.plugin.Priority;

import com.eharrison.canary.zown.ZownPlugin;
import com.eharrison.canary.zown.api.IZown;
import com.eharrison.canary.zown.api.IZownManager;
import com.eharrison.canary.zown.api.impl.Tree;

public class ModifyWorldListener implements PluginListener {
	public static final String FLAG_BUILD = "build";
	
	private final IZownManager zownManager;
	
	public ModifyWorldListener(final IZownManager zownManager) {
		this.zownManager = zownManager;
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onBlockPlace(final BlockPlaceHook hook) {
		final Player player = hook.getPlayer();
		final Block block = hook.getBlockPlaced();
		
		if (!player.isOperator()) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(block.getLocation());
			final Boolean flag = zownTree.getData().getConfiguration().getFlag(FLAG_BUILD);
			if (flag != null && !flag) {
				hook.setCanceled();
			}
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onBlockDestroy(final BlockDestroyHook hook) {
		final Player player = hook.getPlayer();
		final Block block = hook.getBlock();
		
		if (!player.isOperator()) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(block.getLocation());
			final Boolean flag = zownTree.getData().getConfiguration().getFlag(FLAG_BUILD);
			if (flag != null && !flag) {
				hook.setCanceled();
			}
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onItemUse(final ItemUseHook hook) {
		final Player player = hook.getPlayer();
		final Block block = hook.getBlockClicked();
		
		if (!player.isOperator()) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(block.getLocation());
			final Boolean flag = zownTree.getData().getConfiguration().getFlag(FLAG_BUILD);
			if (flag != null && !flag) {
				final ItemType type = hook.getItem().getType();
				if (type == ItemType.FlintAndSteel) {
					hook.setCanceled();
				}
			}
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onLiquidDestroy(final LiquidDestroyHook hook) {
		final Block block = hook.getBlock();
		
		final Tree<? extends IZown> zownTree = zownManager.getZown(block.getLocation());
		final Boolean flag = zownTree.getData().getConfiguration().getFlag(FLAG_BUILD);
		if (flag != null && !flag) {
			ZownPlugin.LOG.info("Cancelling LiquidDestroy");
			hook.setCanceled();
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onFlow(final FlowHook hook) {
		final Block block = hook.getBlockTo();
		
		final Tree<? extends IZown> zownTree = zownManager.getZown(block.getLocation());
		final Boolean flag = zownTree.getData().getConfiguration().getFlag(FLAG_BUILD);
		if (flag != null && !flag) {
			ZownPlugin.LOG.info("Cancelling Flow");
			hook.setCanceled();
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onIgnition(final IgnitionHook hook) {
		final Player player = hook.getPlayer();
		final Block block = hook.getBlock();
		
		if (!player.isOperator()) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(block.getLocation());
			final Boolean flag = zownTree.getData().getConfiguration().getFlag(FLAG_BUILD);
			if (flag != null && !flag) {
				hook.setCanceled();
			}
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onExplosion(final ExplosionHook hook) {
		final List<Block> blocks = hook.getAffectedBlocks();
		
		for (final Block block : blocks) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(block.getLocation());
			final Boolean flag = zownTree.getData().getConfiguration().getFlag(FLAG_BUILD);
			if (flag != null && !flag) {
				hook.setCanceled();
				break;
			}
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onEnder(final EndermanPickupBlockHook hook) {
		final Block block = hook.getBlock();
		
		final Tree<? extends IZown> zownTree = zownManager.getZown(block.getLocation());
		final Boolean flag = zownTree.getData().getConfiguration().getFlag(FLAG_BUILD);
		if (flag != null && !flag) {
			hook.setCanceled();
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onEnder(final EndermanDropBlockHook hook) {
		final Location location = hook.getEnderman().getLocation();
		
		final Tree<? extends IZown> zownTree = zownManager.getZown(location);
		final Boolean flag = zownTree.getData().getConfiguration().getFlag(FLAG_BUILD);
		if (flag != null && !flag) {
			hook.setCanceled();
		}
	}
}
