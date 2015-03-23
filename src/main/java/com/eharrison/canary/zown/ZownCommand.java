package com.eharrison.canary.zown;

import net.canarymod.LineTracer;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.commandsys.CommandListener;

public class ZownCommand implements CommandListener {
	private final ZownPlugin plugin;
	
	public ZownCommand(final ZownPlugin plugin) {
		this.plugin = plugin;
	}
	
	// @Command(aliases = {
	// "hands"
	// }, description = "Hands Off!", permissions = {
	// "handsoff.command"
	// }, toolTip = "/hands", min = 1, max = 1)
	// public void handsCommand(final MessageReceiver caller, final String[] parameters) {
	// if (caller instanceof Player) {
	// plugin.hands((Player) caller);
	// }
	// }
	//
	// @Command(aliases = {
	// "hands"
	// }, parent = "hands", description = "Hands Off!", permissions = {
	// "handsoff.command"
	// }, toolTip = "/hands <on/off>", min = 2, max = 2)
	// public void handsOffCommand(final MessageReceiver caller, final String[] parameters) {
	// if (caller instanceof Player) {
	// final Player player = (Player) caller;
	// final Entity entity = player.getTargetLookingAt();
	//
	// player.message(ChatFormat.YELLOW + "Looking at: " + entity);
	//
	// if (parameters[1].equalsIgnoreCase("on")) {
	// // Canary.getServer().broadcastMessage(ChatFormat.GRAY + "Editing enabled.");
	// plugin.handsOn(player, entity);
	// } else if (parameters[1].equalsIgnoreCase("off")) {
	// // Canary.getServer().broadcastMessage(ChatFormat.RED + "Get yer hands off!");
	// plugin.handsOff(player, entity);
	// } else {
	// InZonePlugin.LOG.error("Bad request");
	// }
	// }
	// }
	
	private Block getBlockLookingAt(final Player player) {
		final LineTracer lineTracer = new LineTracer(player);
		return lineTracer.getNextBlock();
	}
}
