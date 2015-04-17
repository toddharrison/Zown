package com.eharrison.canary.zown;

import net.canarymod.Canary;
import net.canarymod.api.world.World;
import net.canarymod.commandsys.CommandDependencyException;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.system.LoadWorldHook;
import net.canarymod.hook.system.UnloadWorldHook;
import net.canarymod.logger.Logman;
import net.canarymod.plugin.Plugin;
import net.canarymod.plugin.PluginListener;
import net.canarymod.plugin.Priority;
import net.canarymod.tasks.ServerTask;

import com.eharrison.canary.zown.api.ITemplate;
import com.eharrison.canary.zown.api.ITemplateManager;
import com.eharrison.canary.zown.api.IZownManager;
import com.eharrison.canary.zown.api.impl.TemplateManager;
import com.eharrison.canary.zown.api.impl.ZownManager;
import com.eharrison.canary.zown.command.TemplateCommand;
import com.eharrison.canary.zown.command.UserCommand;
import com.eharrison.canary.zown.command.ZownCommand;
import com.eharrison.canary.zown.dao.DataManager;
import com.eharrison.canary.zown.listener.CommandListener;
import com.eharrison.canary.zown.listener.EntityListener;
import com.eharrison.canary.zown.listener.ModifyWorldListener;

public class ZownPlugin extends Plugin implements PluginListener {
	public static Logman LOG;
	
	private ITemplateManager templateManager;
	private IZownManager zownManager;
	private ZownCommand zownCommand;
	private TemplateCommand templateCommand;
	private UserCommand userCommand;
	
	public ZownPlugin() throws DatabaseReadException {
		ZownPlugin.LOG = getLogman();
	}
	
	@Override
	public boolean enable() {
		boolean success = true;
		
		LOG.info("Enabling " + getName() + " Version " + getVersion());
		LOG.info("Authored by " + getAuthor());
		
		final DataManager dataManager = new DataManager();
		templateManager = new TemplateManager(dataManager);
		zownManager = new ZownManager(dataManager, templateManager);
		
		Canary.hooks().registerListener(this, this);
		Canary.hooks().registerListener(new CommandListener(zownManager), this);
		Canary.hooks().registerListener(new ModifyWorldListener(zownManager), this);
		Canary.hooks().registerListener(new EntityListener(zownManager), this);
		
		zownCommand = new ZownCommand(templateManager, zownManager);
		templateCommand = new TemplateCommand(templateManager);
		userCommand = new UserCommand();
		
		try {
			Canary.commands().registerCommands(zownCommand, this, false);
			Canary.commands().registerCommands(templateCommand, this, false);
			Canary.commands().registerCommands(userCommand, this, false);
		} catch (final CommandDependencyException e) {
			LOG.error("Error registering commands: ", e);
			success = false;
		}
		
		Canary.getServer().addSynchronousTask(new ServerTask(this, 20) {
			@Override
			public void run() {
				try {
					
					final World world = Canary.getServer().getDefaultWorld();
					final ITemplate template = templateManager.getTemplate("plotTemplate");
					
					// zownManager.createZown(world, "foo", template, new Point(0, 0, 0), new Point(10, 10,
					// 10));
					
				} catch (final Exception e) {
					LOG.error("Error running", e);
				}
				
				// Canary.getServer().initiateShutdown("Stopping");
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
	
	@HookHandler(priority = Priority.PASSIVE)
	public void onWorldLoad(final LoadWorldHook hook) {
		zownManager.loadZowns(hook.getWorld());
	}
	
	@HookHandler(priority = Priority.PASSIVE)
	public void onWorldUnload(final UnloadWorldHook hook) {
		zownManager.unloadZowns(hook.getWorld());
	}
}
