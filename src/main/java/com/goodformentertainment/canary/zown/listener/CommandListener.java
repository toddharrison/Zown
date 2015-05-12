package com.goodformentertainment.canary.zown.listener;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.ChatFormat;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.command.PlayerCommandHook;
import net.canarymod.plugin.PluginListener;
import net.canarymod.plugin.Priority;

import com.goodformentertainment.canary.zown.api.IZown;
import com.goodformentertainment.canary.zown.api.IZownManager;
import com.goodformentertainment.canary.zown.api.impl.Tree;

public class CommandListener implements PluginListener {
	private final IZownManager zownManager;
	
	public CommandListener(final IZownManager zownManager) {
		this.zownManager = zownManager;
	}
	
	@HookHandler(priority = Priority.CRITICAL)
	public void onPlayerCommand(final PlayerCommandHook hook) {
		final Player player = hook.getPlayer();
		final String[] command = hook.getCommand();
		
		if (!player.isOperator()) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(player.getLocation());
			if (zownTree.getData().getConfiguration().hasCommandRestriction(command[0])) {
				hook.setCanceled();
				player.message(ChatFormat.GOLD + "That command has been restricted.");
			}
		}
	}
}
