package com.goodformentertainment.canary.zown.listener;

import java.util.List;

import net.canarymod.api.entity.ArmorStand;
import net.canarymod.api.entity.Entity;
import net.canarymod.api.entity.hanging.HangingEntity;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.inventory.ItemType;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.blocks.TileEntity;
import net.canarymod.api.world.blocks.properties.helpers.DoorProperties;
import net.canarymod.api.world.position.Location;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.entity.EndermanDropBlockHook;
import net.canarymod.hook.entity.EndermanPickupBlockHook;
import net.canarymod.hook.entity.HangingEntityDestroyHook;
import net.canarymod.hook.player.ArmorStandModifyHook;
import net.canarymod.hook.player.BlockDestroyHook;
import net.canarymod.hook.player.BlockPlaceHook;
import net.canarymod.hook.player.BlockRightClickHook;
import net.canarymod.hook.player.EntityRightClickHook;
import net.canarymod.hook.player.ItemFrameRotateHook;
import net.canarymod.hook.player.ItemFrameSetItemHook;
import net.canarymod.hook.player.ItemUseHook;
import net.canarymod.hook.world.ExplosionHook;
import net.canarymod.hook.world.FlowHook;
import net.canarymod.hook.world.IgnitionHook;
import net.canarymod.hook.world.LiquidDestroyHook;
import net.canarymod.plugin.PluginListener;
import net.canarymod.plugin.Priority;

import com.goodformentertainment.canary.zown.Flag;
import com.goodformentertainment.canary.zown.ZownPlugin;
import com.goodformentertainment.canary.zown.api.IZown;
import com.goodformentertainment.canary.zown.api.IZownManager;
import com.goodformentertainment.canary.zown.api.impl.Tree;

public class ModifyWorldListener implements PluginListener {
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
			if (!zownTree.getData().isOwnerOrMember(player)) {
				final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.build.name());
				if (flag != null) {
					final boolean excluded = zownTree.getData().getConfiguration()
							.hasBlockBuildExclusion(block.getType());
					if (flag) {
						if (excluded) {
							hook.setCanceled();
							ZownPlugin.LOG.info("Cancelled block place: " + flag + ":" + excluded);
						}
					} else {
						if (!excluded) {
							hook.setCanceled();
							ZownPlugin.LOG.info("Cancelled block place: " + flag + ":" + excluded);
						}
					}
				}
			}
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onBlockDestroy(final BlockDestroyHook hook) {
		final Player player = hook.getPlayer();
		final Block block = hook.getBlock();
		
		if (!player.isOperator()) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(block.getLocation());
			if (!zownTree.getData().isOwnerOrMember(player)) {
				final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.build.name());
				if (flag != null) {
					final boolean excluded = zownTree.getData().getConfiguration()
							.hasBlockBuildExclusion(block.getType());
					if (flag) {
						if (excluded) {
							hook.setCanceled();
						}
					} else {
						if (!excluded) {
							hook.setCanceled();
						}
					}
				}
			}
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onHangingEntityDestroy(final HangingEntityDestroyHook hook) {
		final Player player = hook.getPlayer();
		final HangingEntity entity = hook.getPainting();
		
		if (player == null || !player.isOperator()) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(entity.getLocation());
			if (player == null || !zownTree.getData().isOwnerOrMember(player)) {
				final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.build.name());
				if (flag != null) {
					final boolean excluded = zownTree.getData().getConfiguration()
							.hasEntityCreateExclusion(entity.getClass());
					if (flag) {
						if (excluded) {
							hook.setCanceled();
						}
					} else {
						if (!excluded) {
							hook.setCanceled();
						}
					}
				}
			}
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onItemUse(final ItemUseHook hook) {
		final Player player = hook.getPlayer();
		final Block block = hook.getBlockClicked();
		
		if (!player.isOperator()) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(block.getLocation());
			if (!zownTree.getData().isOwnerOrMember(player)) {
				final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.build.name());
				if (flag != null && !flag) {
					final ItemType type = hook.getItem().getType();
					if (type == ItemType.FlintAndSteel || type == ItemType.WaterBucket
							|| type == ItemType.LavaBucket || type == ItemType.Bonemeal) {
						hook.setCanceled();
					}
				}
			}
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onLiquidDestroy(final LiquidDestroyHook hook) {
		final Block block = hook.getBlock();
		
		final Tree<? extends IZown> zownTree = zownManager.getZown(block.getLocation());
		final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.flow.name());
		if (flag != null && !flag) {
			hook.setCanceled();
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onFlow(final FlowHook hook) {
		final Block block = hook.getBlockTo();
		
		final Tree<? extends IZown> zownTree = zownManager.getZown(block.getLocation());
		final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.flow.name());
		if (flag != null && !flag) {
			hook.setCanceled();
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onIgnition(final IgnitionHook hook) {
		final Player player = hook.getPlayer();
		final Block block = hook.getBlock();
		
		if (player == null) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(block.getLocation());
			final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.firespread.name());
			if (flag != null && !flag) {
				hook.setCanceled();
			}
		} else if (!player.isOperator()) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(block.getLocation());
			if (!zownTree.getData().isOwnerOrMember(player)) {
				final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.build.name());
				if (flag != null && !flag) {
					hook.setCanceled();
				}
			}
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onExplosion(final ExplosionHook hook) {
		final List<Block> blocks = hook.getAffectedBlocks();
		
		for (final Block block : blocks) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(block.getLocation());
			final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.build.name());
			if (flag != null && !flag) {
				hook.setCanceled();
				break;
			}
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onEnderPickupBlock(final EndermanPickupBlockHook hook) {
		final Block block = hook.getBlock();
		
		final Tree<? extends IZown> zownTree = zownManager.getZown(block.getLocation());
		final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.build.name());
		if (flag != null && !flag) {
			hook.setCanceled();
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onEnderDropBlock(final EndermanDropBlockHook hook) {
		final Location location = hook.getEnderman().getLocation();
		
		final Tree<? extends IZown> zownTree = zownManager.getZown(location);
		final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.build.name());
		if (flag != null && !flag) {
			hook.setCanceled();
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onItemFrameRotate(final ItemFrameRotateHook hook) {
		final Player player = hook.getPlayer();
		final Entity entity = hook.getItemFrame();
		
		if (!player.isOperator()) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(entity.getLocation());
			if (!zownTree.getData().isOwnerOrMember(player)) {
				final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.interact.name());
				if (flag != null) {
					final boolean excluded = zownTree.getData().getConfiguration()
							.hasEntityInteractExclusion(entity.getClass());
					if (flag) {
						if (excluded) {
							hook.setCanceled();
						}
					} else {
						if (!excluded) {
							hook.setCanceled();
						}
					}
				}
			}
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onItemFrameSetItem(final ItemFrameSetItemHook hook) {
		final Player player = hook.getPlayer();
		final Entity entity = hook.getItemFrame();
		
		if (!player.isOperator()) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(entity.getLocation());
			if (!zownTree.getData().isOwnerOrMember(player)) {
				final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.interact.name());
				if (flag != null) {
					final boolean excluded = zownTree.getData().getConfiguration()
							.hasEntityInteractExclusion(entity.getClass());
					if (flag) {
						if (excluded) {
							hook.setCanceled();
						}
					} else {
						if (!excluded) {
							hook.setCanceled();
						}
					}
				}
			}
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onArmorStandModify(final ArmorStandModifyHook hook) {
		final Player player = hook.getPlayer();
		// TODO need access to the armor stand being modified
		// final Entity entity = hook.getArmorStand();
		final Entity armorStand = player;
		
		if (!player.isOperator()) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(armorStand.getLocation());
			if (!zownTree.getData().isOwnerOrMember(player)) {
				final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.interact.name());
				if (flag != null) {
					final boolean excluded = zownTree.getData().getConfiguration()
							.hasEntityInteractExclusion(ArmorStand.class);
					if (flag) {
						if (excluded) {
							hook.setCanceled();
						}
					} else {
						if (!excluded) {
							hook.setCanceled();
						}
					}
				}
			}
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onBlockRightClick(final BlockRightClickHook hook) {
		final Player player = hook.getPlayer();
		final Block block = hook.getBlockClicked();
		
		final Item item = player.getItemHeld();
		final TileEntity tileEntity = block.getTileEntity();
		final BlockType type = block.getType();
		
		if (item == null || tileEntity != null || type == BlockType.AcaciaDoor
				|| type == BlockType.AcaciaFenceGate || type == BlockType.Bed
				|| type == BlockType.BirchDoor || type == BlockType.BirchFenceGate
				|| type == BlockType.Cake || type == BlockType.Cauldron || type == BlockType.DarkOakDoor
				|| type == BlockType.DarkOakFenceGate || type == BlockType.EndPortalFrame
				|| type == BlockType.Farmland || type == BlockType.FenceGate || type == BlockType.Flowerpot
				|| type == BlockType.JungleDoor || type == BlockType.JungleFenceGate
				|| type == BlockType.Lever || type == BlockType.MobSpawner || type == BlockType.OakDoor
				|| type == BlockType.RedstoneRepeaterOff || type == BlockType.RedstoneRepeaterOn
				|| type == BlockType.SpruceDoor || type == BlockType.SpruceFenceGate
				|| type == BlockType.StoneButton || type == BlockType.Trapdoor
				|| type == BlockType.WoodenButton || type == BlockType.WoodenDoor) {
			if (!player.isOperator() && !player.safeHasPermission("zown.admin.mod")) {
				final Tree<? extends IZown> zownTree = zownManager.getZown(block.getLocation());
				if (!zownTree.getData().isOwnerOrMember(player)) {
					final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.interact.name());
					if (flag != null) {
						final boolean excluded = zownTree.getData().getConfiguration()
								.hasBlockInteractExclusion(block.getType());
						if (flag) {
							if (excluded) {
								hook.setCanceled();
								closeDoor(block);
								// TODO
								ZownPlugin.LOG.info("Cancelled right click: " + flag + ":" + excluded);
							}
						} else {
							if (!excluded) {
								hook.setCanceled();
								closeDoor(block);
								// TODO
								ZownPlugin.LOG.info("Cancelled right click: " + flag + ":" + excluded);
							}
						}
					}
				}
			}
		}
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onEntityRightClick(final EntityRightClickHook hook) {
		final Player player = hook.getPlayer();
		final Entity entity = hook.getEntity();
		
		if (!player.isOperator()) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(entity.getLocation());
			if (!zownTree.getData().isOwnerOrMember(player)) {
				final Boolean flag = zownTree.getData().getConfiguration().getFlag(Flag.interact.name());
				if (flag != null) {
					final boolean excluded = zownTree.getData().getConfiguration()
							.hasEntityInteractExclusion(entity.getClass());
					if (flag) {
						if (excluded) {
							hook.setCanceled();
						}
					} else {
						if (!excluded) {
							hook.setCanceled();
						}
					}
				}
			}
		}
	}
	
	private void closeDoor(final Block block) {
		final BlockType type = block.getType();
		if (type == BlockType.OakDoor || type == BlockType.SpruceDoor || type == BlockType.BirchDoor
				|| type == BlockType.AcaciaDoor || type == BlockType.DarkOakDoor
				|| type == BlockType.JungleDoor) {
			block.setPropertyValue(DoorProperties.open, false);
		}
	}
}
