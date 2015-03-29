package com.eharrison.canary.zown;

import net.canarymod.Canary;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.logger.Logman;
import net.canarymod.plugin.Plugin;

public class ZownPlugin extends Plugin {
	public static Logman LOG;
	
	public ZownPlugin() throws DatabaseReadException {
		ZownPlugin.LOG = getLogman();
	}
	
	@Override
	public boolean enable() {
		final boolean success = true;
		
		LOG.info("Enabling " + getName() + " Version " + getVersion());
		LOG.info("Authored by " + getAuthor());
		
		try {
		} catch (final Exception e) {
			LOG.error("Error running", e);
		}
		
		Canary.getServer().initiateShutdown("Stopping");
		return success;
	}
	
	@Override
	public void disable() {
		LOG.info("Disabling " + getName());
		
		Canary.commands().unregisterCommands(this);
		Canary.hooks().unregisterPluginListeners(this);
	}
}
