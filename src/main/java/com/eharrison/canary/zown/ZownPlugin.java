package com.eharrison.canary.zown;

import net.canarymod.Canary;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.system.LoadWorldHook;
import net.canarymod.hook.system.UnloadWorldHook;
import net.canarymod.logger.Logman;
import net.canarymod.plugin.Plugin;
import net.canarymod.plugin.PluginListener;
import net.canarymod.tasks.ServerTask;

import com.eharrison.canary.zown.api.ITemplateManager;
import com.eharrison.canary.zown.api.IZownManager;
import com.eharrison.canary.zown.api.impl.TemplateManager;
import com.eharrison.canary.zown.api.impl.ZownManager;
import com.eharrison.canary.zown.dao.DataManager;

public class ZownPlugin extends Plugin implements PluginListener {
	public static Logman LOG;
	
	private ITemplateManager templateManager;
	private IZownManager zownManager;
	
	public ZownPlugin() throws DatabaseReadException {
		ZownPlugin.LOG = getLogman();
	}
	
	@Override
	public boolean enable() {
		final boolean success = true;
		
		LOG.info("Enabling " + getName() + " Version " + getVersion());
		LOG.info("Authored by " + getAuthor());
		
		Canary.hooks().registerListener(this, this);
		
		final DataManager dataManager = new DataManager();
		templateManager = new TemplateManager(dataManager);
		zownManager = new ZownManager(dataManager, templateManager);
		
		Canary.getServer().addSynchronousTask(new ServerTask(this, 20) {
			@Override
			public void run() {
				try {
					
					// zownManager.createZown(world, name, template, p1, p2);
					
				} catch (final Exception e) {
					LOG.error("Error running", e);
				}
				
				Canary.getServer().initiateShutdown("Stopping");
			}
		});
		
		return success;
	}
	
	@Override
	public void disable() {
		LOG.info("Disabling " + getName());
		
		templateManager = null;
		zownManager = null;
		
		Canary.commands().unregisterCommands(this);
		Canary.hooks().unregisterPluginListeners(this);
	}
	
	@HookHandler
	public void onWorldLoad(final LoadWorldHook hook) {
		zownManager.loadZowns(hook.getWorld());
	}
	
	@HookHandler
	public void onWorldUnload(final UnloadWorldHook hook) {
		zownManager.unloadZowns(hook.getWorld());
	}
}
