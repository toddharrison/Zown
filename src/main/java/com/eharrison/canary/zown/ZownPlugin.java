package com.eharrison.canary.zown;

import net.canarymod.Canary;
import net.canarymod.commandsys.CommandDependencyException;
import net.canarymod.logger.Logman;
import net.canarymod.plugin.Plugin;
import net.canarymod.plugin.PluginListener;

public class ZownPlugin extends Plugin implements PluginListener {
	protected static Logman LOG;
	private final ZownCommand command;
	
	// private boolean enabled;
	
	public ZownPlugin() {
		ZownPlugin.LOG = getLogman();
		command = new ZownCommand(this);
		// enabled = true;
	}
	
	@Override
	public boolean enable() {
		boolean success = true;
		
		LOG.info("Enabling " + getName() + " Version " + getVersion());
		LOG.info("Authored by " + getAuthor());
		
		Canary.hooks().registerListener(this, this);
		
		try {
			Canary.commands().registerCommands(command, this, false);
		} catch (final CommandDependencyException e) {
			LOG.error("Error registering commands: ", e);
			success = false;
		}
		
		return success;
	}
	
	@Override
	public void disable() {
		LOG.info("Disabling " + getName());
		
		Canary.commands().unregisterCommands(this);
		Canary.hooks().unregisterPluginListeners(this);
	}
	
	// protected void hands(final Player player) {
	// player.message("Target: " + player.getTargetLookingAt());
	// }
	//
	// protected void handsOn(final Player player, final Entity entity) {
	// if (entity != null) {
	// final CompoundTag nbt = entity.getNBT();
	// nbt.put("Invulnerable", (byte) 0);
	// entity.setNBT(nbt);
	//
	// if (entity instanceof ArmorStand) {
	// final ArmorStand armorStand = (ArmorStand) entity;
	// armorStand.enableSlot(Slot.BODY, Disability.PLACE);
	// armorStand.enableSlot(Slot.BODY, Disability.REMOVE);
	// armorStand.enableSlot(Slot.BODY, Disability.REPLACE);
	// armorStand.enableSlot(Slot.FEET, Disability.PLACE);
	// armorStand.enableSlot(Slot.FEET, Disability.REMOVE);
	// armorStand.enableSlot(Slot.FEET, Disability.REPLACE);
	// armorStand.enableSlot(Slot.HEAD, Disability.PLACE);
	// armorStand.enableSlot(Slot.HEAD, Disability.REMOVE);
	// armorStand.enableSlot(Slot.HEAD, Disability.REPLACE);
	// armorStand.enableSlot(Slot.HOLDING, Disability.PLACE);
	// armorStand.enableSlot(Slot.HOLDING, Disability.REMOVE);
	// armorStand.enableSlot(Slot.HOLDING, Disability.REPLACE);
	// armorStand.enableSlot(Slot.LEGS, Disability.PLACE);
	// armorStand.enableSlot(Slot.LEGS, Disability.REMOVE);
	// armorStand.enableSlot(Slot.LEGS, Disability.REPLACE);
	// }
	//
	// player.message(ChatFormat.GRAY + entity.getName() + " may be edited");
	// }
	// }
	//
	// protected void handsOff(final Player player, final Entity entity) {
	// if (entity != null) {
	// final CompoundTag nbt = entity.getNBT();
	// nbt.put("Invulnerable", (byte) 1);
	// entity.setNBT(nbt);
	//
	// if (entity instanceof ArmorStand) {
	// final ArmorStand armorStand = (ArmorStand) entity;
	// armorStand.disableSlot(Slot.BODY, Disability.PLACE);
	// armorStand.disableSlot(Slot.BODY, Disability.REMOVE);
	// armorStand.disableSlot(Slot.BODY, Disability.REPLACE);
	// armorStand.disableSlot(Slot.FEET, Disability.PLACE);
	// armorStand.disableSlot(Slot.FEET, Disability.REMOVE);
	// armorStand.disableSlot(Slot.FEET, Disability.REPLACE);
	// armorStand.disableSlot(Slot.HEAD, Disability.PLACE);
	// armorStand.disableSlot(Slot.HEAD, Disability.REMOVE);
	// armorStand.disableSlot(Slot.HEAD, Disability.REPLACE);
	// armorStand.disableSlot(Slot.HOLDING, Disability.PLACE);
	// armorStand.disableSlot(Slot.HOLDING, Disability.REMOVE);
	// armorStand.disableSlot(Slot.HOLDING, Disability.REPLACE);
	// armorStand.disableSlot(Slot.LEGS, Disability.PLACE);
	// armorStand.disableSlot(Slot.LEGS, Disability.REMOVE);
	// armorStand.disableSlot(Slot.LEGS, Disability.REPLACE);
	// }
	//
	// player.message(ChatFormat.RED + "Hands off my " + entity.getName() + "!");
	// }
	// }
	
	// @HookHandler
	// public void onHit(final DamageHook hook) {
	// }
	
	// public void setEnabled(final boolean enabled) {
	// this.enabled = enabled;
	// }
	
	// public void setEnabled(final boolean enabled) {
	// this.enabled = enabled;
	//
	// for (final Entity entity : Canary.getServer().getDefaultWorld().getEntityTracker()
	// .getTrackedEntities()) {
	// if (entity instanceof ArmorStand) {
	// final ArmorStand armorStand = (ArmorStand) entity;
	// if (enabled) {
	// final CompoundTag nbt = armorStand.getNBT();
	// nbt.put("Invulnerable", (byte) 1);
	// armorStand.setNBT(nbt);
	//
	// armorStand.disableSlot(Slot.BODY, Disability.PLACE);
	// armorStand.disableSlot(Slot.BODY, Disability.REMOVE);
	// armorStand.disableSlot(Slot.BODY, Disability.REPLACE);
	// armorStand.disableSlot(Slot.FEET, Disability.PLACE);
	// armorStand.disableSlot(Slot.FEET, Disability.REMOVE);
	// armorStand.disableSlot(Slot.FEET, Disability.REPLACE);
	// armorStand.disableSlot(Slot.HEAD, Disability.PLACE);
	// armorStand.disableSlot(Slot.HEAD, Disability.REMOVE);
	// armorStand.disableSlot(Slot.HEAD, Disability.REPLACE);
	// armorStand.disableSlot(Slot.HOLDING, Disability.PLACE);
	// armorStand.disableSlot(Slot.HOLDING, Disability.REMOVE);
	// armorStand.disableSlot(Slot.HOLDING, Disability.REPLACE);
	// armorStand.disableSlot(Slot.LEGS, Disability.PLACE);
	// armorStand.disableSlot(Slot.LEGS, Disability.REMOVE);
	// armorStand.disableSlot(Slot.LEGS, Disability.REPLACE);
	// } else {
	// final CompoundTag nbt = armorStand.getNBT();
	// nbt.put("Invulnerable", (byte) 0);
	// armorStand.setNBT(nbt);
	//
	// armorStand.enableSlot(Slot.BODY, Disability.PLACE);
	// armorStand.enableSlot(Slot.BODY, Disability.REMOVE);
	// armorStand.enableSlot(Slot.BODY, Disability.REPLACE);
	// armorStand.enableSlot(Slot.FEET, Disability.PLACE);
	// armorStand.enableSlot(Slot.FEET, Disability.REMOVE);
	// armorStand.enableSlot(Slot.FEET, Disability.REPLACE);
	// armorStand.enableSlot(Slot.HEAD, Disability.PLACE);
	// armorStand.enableSlot(Slot.HEAD, Disability.REMOVE);
	// armorStand.enableSlot(Slot.HEAD, Disability.REPLACE);
	// armorStand.enableSlot(Slot.HOLDING, Disability.PLACE);
	// armorStand.enableSlot(Slot.HOLDING, Disability.REMOVE);
	// armorStand.enableSlot(Slot.HOLDING, Disability.REPLACE);
	// armorStand.enableSlot(Slot.LEGS, Disability.PLACE);
	// armorStand.enableSlot(Slot.LEGS, Disability.REMOVE);
	// armorStand.enableSlot(Slot.LEGS, Disability.REPLACE);
	// }
	// } else if (entity instanceof ItemFrame) {
	// final ItemFrame itemFrame = (ItemFrame) entity;
	// if (enabled) {
	// final CompoundTag nbt = itemFrame.getNBT();
	// nbt.put("Invulnerable", (byte) 1);
	// itemFrame.setNBT(nbt);
	// } else {
	// final CompoundTag nbt = itemFrame.getNBT();
	// nbt.put("Invulnerable", (byte) 0);
	// itemFrame.setNBT(nbt);
	// }
	// }
	// }
	// }
	
	// @HookHandler
	// public void onConnect(final ConnectionHook hook) {
	// final Player player = hook.getPlayer();
	// player.setHealth(new Double(player.getMaxHealth()).floatValue());
	// }
	//
	// @HookHandler
	// public void onDamage(final DamageHook hook) {
	// if (enabled) {
	// hook.setCanceled();
	// // LOG.info("DAMAGE");
	// }
	// }
	//
	// @HookHandler
	// public void onEntityDeath(final EntityDeathHook hook) {
	// if (enabled) {
	// LOG.info("ENTITY_DEATH");
	// }
	// }
	//
	// @HookHandler
	// public void onEntityDespawn(final EntityDespawnHook hook) {
	// if (enabled) {
	// // TODO *** Doesn't work
	// if (hook.getEntity() instanceof ArmorStand) {
	// hook.setCanceled();
	// LOG.info("ENTITY_DESPAWN");
	// }
	// }
	// }
	//
	// @HookHandler
	// public void onEntityMove(final EntityMoveHook hook) {
	// if (hook.getEntity() instanceof ArmorStand) {
	// LOG.info("ENTITY_MOVE");
	// }
	// }
	//
	// @HookHandler
	// public void onEntitySpawn(final EntitySpawnHook hook) {
	// if (hook.getEntity() instanceof ArmorStand || hook.getEntity() instanceof ItemFrame) {
	// hook.setCanceled();
	// }
	// // if (hook.getEntity() instanceof ArmorStand) {
	// // final ArmorStand armorStand = (ArmorStand) hook.getEntity();
	// //
	// // final CompoundTag nbt = armorStand.getNBT();
	// // nbt.put("Invulnerable", (byte) 1);
	// // armorStand.setNBT(nbt);
	// //
	// // armorStand.disableSlot(Slot.BODY, Disability.PLACE);
	// // armorStand.disableSlot(Slot.BODY, Disability.REMOVE);
	// // armorStand.disableSlot(Slot.BODY, Disability.REPLACE);
	// // armorStand.disableSlot(Slot.FEET, Disability.PLACE);
	// // armorStand.disableSlot(Slot.FEET, Disability.REMOVE);
	// // armorStand.disableSlot(Slot.FEET, Disability.REPLACE);
	// // armorStand.disableSlot(Slot.HEAD, Disability.PLACE);
	// // armorStand.disableSlot(Slot.HEAD, Disability.REMOVE);
	// // armorStand.disableSlot(Slot.HEAD, Disability.REPLACE);
	// // armorStand.disableSlot(Slot.HOLDING, Disability.PLACE);
	// // armorStand.disableSlot(Slot.HOLDING, Disability.REMOVE);
	// // armorStand.disableSlot(Slot.HOLDING, Disability.REPLACE);
	// // armorStand.disableSlot(Slot.LEGS, Disability.PLACE);
	// // armorStand.disableSlot(Slot.LEGS, Disability.REMOVE);
	// // armorStand.disableSlot(Slot.LEGS, Disability.REPLACE);
	// //
	// // // hook.setCanceled();
	// // // LOG.info("ENTITY_SPAWN");
	// // } else if (hook.getEntity() instanceof ItemFrame) {
	// // final ItemFrame itemFrame = (ItemFrame) hook.getEntity();
	// //
	// // final CompoundTag nbt = itemFrame.getNBT();
	// // nbt.put("Invulnerable", (byte) 1);
	// // itemFrame.setNBT(nbt);
	// // }
	// }
	//
	// @HookHandler
	// public void onEnderGrief(final EndermanPickupBlockHook hook) {
	// if (enabled) {
	// hook.setCanceled();
	// // LOG.info("ENDER_GRIEF");
	// }
	// }
	//
	// @HookHandler
	// public void onMount(final EntityMountHook hook) {
	// if (enabled) {
	// final Location loc = hook.getRider().getLocation();
	// hook.getRider().teleportTo(loc);
	// hook.setCanceled();
	// // LOG.info("MOUNT");
	// }
	// }
	//
	// @HookHandler
	// public void onTame(final EntityTameHook hook) {
	// if (enabled) {
	// hook.setCanceled();
	// // LOG.info("TAME");
	// }
	// }
	//
	// @HookHandler
	// public void onHangingDestroyed(final HangingEntityDestroyHook hook) {
	// if (enabled) {
	// hook.setCanceled();
	// // LOG.info("HANGING_DESTROYED");
	// }
	// }
	//
	// @HookHandler
	// public void onTarget(final MobTargetHook hook) {
	// if (enabled) {
	// hook.setCanceled();
	// // LOG.info("MOB_TARGET");
	// }
	// }
	//
	// @HookHandler
	// public void onPotion(final PotionEffectAppliedHook hook) {
	// if (enabled) {
	// hook.setPotionEffect(null);
	// // LOG.info("POTION");
	// }
	// }
	//
	// @HookHandler
	// public void onProjectile(final ProjectileHitHook hook) {
	// if (enabled) {
	// hook.setCanceled();
	// // LOG.info("PROJECTILE");
	// }
	// }
	
	// @HookHandler
	// public void onVehicleDamage(final VehicleDamageHook hook) {
	// if (enabled) {
	// hook.setCanceled();
	// // LOG.info("VEHICLE_DAMAGE");
	// }
	// }
	
	// @HookHandler
	// public void onBlockDestroy(final BlockDestroyHook hook) {
	// if (enabled) {
	// hook.setCanceled();
	// // LOG.info("BLOCK_DESTORY");
	// }
	// }
	
	// @HookHandler
	// public void onBlockLeftClick(final BlockLeftClickHook hook) {
	// if (enabled) {
	// LOG.info("BLOCK_LEFT");
	// }
	// }
	
	// @HookHandler
	// public void onBlockRightClick(final BlockRightClickHook hook) {
	// if (enabled) {
	// LOG.info("BLOCK_RIGHT");
	// }
	// }
	
	// @HookHandler
	// public void onBlockPlace(final BlockPlaceHook hook) {
	// if (enabled) {
	// hook.setCanceled();
	// // LOG.info("BLOCK_PLACE");
	// }
	// }
	
	// @HookHandler
	// public void onPlayerArmSwing(final PlayerArmSwingHook hook) {
	// if (enabled) {
	// LOG.info("LEFT_ARM_SWING");
	// }
	// }
	
	// @HookHandler
	// public void onHeldItemChange(final HeldItemChangeHook hook) {
	// if (enabled) {
	// // LOG.info("HELD_ITEM_CHANGE");
	// }
	// }
	
	// @HookHandler
	// public void onEntityRightClick(final EntityRightClickHook hook) {
	// // if (enabled) {
	// if (hook.getEntity() instanceof ItemFrame) {
	// hook.setCanceled();
	// }
	// LOG.info("ENTITY_RIGHT");
	// // }
	// }
	
	// @HookHandler
	// public void onHealthChange(final HealthChangeHook hook) {
	// if (enabled) {
	// // hook.setCanceled();
	// LOG.info("HEATH");
	// }
	// }
	
	// @HookHandler
	// public void onInventory(final InventoryHook hook) {
	// if (enabled) {
	// final Inventory inventory = hook.getInventory();
	// if (inventory != null && inventory.getInventoryType() != null) {
	// switch (inventory.getInventoryType()) {
	// case CHEST:
	// case MINECART_CHEST:
	// break;
	// default:
	// hook.setCanceled();
	// }
	// }
	// // LOG.info("INVENTORY");
	// }
	// }
	
	// @HookHandler
	// public void onItemPickup(final ItemPickupHook hook) {
	// if (enabled) {
	// LOG.info("ITEM_PICKUP");
	// }
	// }
	
	// @HookHandler
	// public void onItemUse(final ItemUseHook hook) {
	// if (enabled) {
	// LOG.info("ITEM_USE");
	// }
	// }
	
	// @HookHandler
	// public void onPortalUse(final PortalUseHook hook) {
	// if (enabled) {
	// hook.setCanceled();
	// // LOG.info("PORTAL_USE");
	// }
	// }
	
	// @HookHandler
	// public void onSignChange(final SignChangeHook hook) {
	// if (enabled) {
	// hook.setCanceled();
	// // LOG.info("SIGN_CHANGE");
	// }
	// }
	
	// @HookHandler
	// public void onSignShowHook(final SignShowHook hook) {
	// if (enabled) {
	// LOG.info("SIGN_SHOW");
	// }
	// }
	
	// @HookHandler
	// public void onVillagerTrade(final VillagerTradeHook hook) {
	// if (enabled) {
	// hook.setCanceled();
	// // LOG.info("VILLAGER_TRADE");
	// }
	// }
	
	// @HookHandler
	// public void onBlockGrow(final BlockGrowHook hook) {
	// if (enabled) {
	// hook.setCanceled();
	// // LOG.info("BLOCK_GROW");
	// }
	// }
	
	// @HookHandler
	// public void onBlockPhysics(final BlockPhysicsHook hook) {
	// if (enabled) {
	// // LOG.info("BLOCK_PHYSICS");
	// }
	// }
	
	// @HookHandler
	// public void onBlockUpdate(final BlockUpdateHook hook) {
	// if (enabled) {
	// // LOG.info("BLOCK_UPDATE");
	// }
	// }
	
	// @HookHandler
	// public void onExplosion(final ExplosionHook hook) {
	// if (enabled) {
	// hook.setCanceled();
	// // LOG.info("EXPLOSION");
	// }
	// }
	
	// @HookHandler
	// public void onFlow(final FlowHook hook) {
	// if (enabled) {
	// // LOG.info("FLOW");
	// }
	// }
	
	// @HookHandler
	// public void onIgnition(final IgnitionHook hook) {
	// if (enabled) {
	// hook.setCanceled();
	// // LOG.info("IGNITION");
	// }
	// }
	
	// @HookHandler
	// public void onLeafDecay(final LeafDecayHook hook) {
	// if (enabled) {
	// // LOG.info("LEAF_DECAY");
	// }
	// }
	
	// @HookHandler
	// public void onLightningStrike(final LightningStrikeHook hook) {
	// if (enabled) {
	// // LOG.info("LIGHTNING");
	// }
	// }
	
	// @HookHandler
	// public void onLiquidDestroy(final LiquidDestroyHook hook) {
	// if (enabled) {
	// hook.setCanceled();
	// // LOG.info("LIQUID_DESTROY");
	// }
	// }
	
	// @HookHandler
	// public void onPortalCreate(final PortalCreateHook hook) {
	// if (enabled) {
	// hook.setCanceled();
	// // LOG.info("PORTAL_CREATE");
	// }
	// }
	
	// @HookHandler
	// public void onPortalDestroy(final PortalDestroyHook hook) {
	// if (enabled) {
	// hook.setCanceled();
	// // LOG.info("PORTAL_DESTROY");
	// }
	// }
	
	// @HookHandler
	// public void onTntActivate(final TNTActivateHook hook) {
	// if (enabled) {
	// hook.setCanceled();
	// // LOG.info("TNT");
	// }
	// }
	
	// @HookHandler
	// public void onTreeGrow(final TreeGrowHook hook) {
	// if (enabled) {
	// hook.setCanceled();
	// // LOG.info("TREE_GROW");
	// }
	// }
	
	// @HookHandler
	// public void onWeatherChange(final WeatherChangeHook hook) {
	// if (enabled) {
	// hook.setCanceled();
	// // LOG.info("WEATHER_CHANGE");
	// }
	// }
	
	// @HookHandler
	// public void onDispense(final DispenseHook hook) {
	// if (enabled) {
	// LOG.info("DISPENSE");
	// }
	// }
}
