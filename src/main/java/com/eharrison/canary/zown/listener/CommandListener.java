package com.eharrison.canary.zown.listener;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.ChatFormat;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.command.PlayerCommandHook;
import net.canarymod.plugin.PluginListener;

import com.eharrison.canary.zown.api.IZown;
import com.eharrison.canary.zown.api.IZownManager;
import com.eharrison.canary.zown.api.impl.Tree;

public class CommandListener implements PluginListener {
	private final IZownManager zownManager;
	
	public CommandListener(final IZownManager zownManager) {
		this.zownManager = zownManager;
	}
	
	@HookHandler
	public void onPlayerCommand(final PlayerCommandHook hook) {
		final Player player = hook.getPlayer();
		final String[] command = hook.getCommand();
		
		final Tree<? extends IZown> zownTree = zownManager.getZown(player.getLocation());
		if (zownTree.getData().getConfiguration().hasCommandRestriction(command[0])) {
			hook.setCanceled();
			player.message(ChatFormat.GOLD + "That command has been restricted.");
		}
	}
}
