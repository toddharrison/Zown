package com.eharrison.canary.zown.command;

import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.Command;
import net.canarymod.commandsys.CommandListener;

import com.eharrison.canary.zown.ZownPlugin;

public class UserCommand implements CommandListener {
	@Command(aliases = {
		"user"
	}, parent = "zown", description = "zown user", permissions = {
		"zown.user"
	}, toolTip = "/zown user")
	public void zownTemplateCommand(final MessageReceiver caller, final String[] parameters) {
		ZownPlugin.LOG.info("called zown user");
	}
}
