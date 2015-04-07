package com.eharrison.canary.zown;

import net.canarymod.Canary;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.system.LoadWorldHook;
import net.canarymod.hook.system.UnloadWorldHook;
import net.canarymod.logger.Logman;
import net.canarymod.plugin.Plugin;
import net.canarymod.plugin.PluginListener;

import com.eharrison.canary.zown.api.ITemplate;
import com.eharrison.canary.zown.api.ITemplateManager;
import com.eharrison.canary.zown.api.IZown;
import com.eharrison.canary.zown.api.IZownManager;
import com.eharrison.canary.zown.api.Point;
import com.eharrison.canary.zown.api.impl.TemplateManager;
import com.eharrison.canary.zown.api.impl.Tree;
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
		
		try {
			
			final DataManager dataManager = new DataManager();
			templateManager = new TemplateManager(dataManager);
			zownManager = new ZownManager(dataManager, templateManager);
			
			final ITemplate template = templateManager.getTemplate("worldTemplate");
			
			final Tree<? extends IZown> zownTree = zownManager.createZown(Canary.getServer()
					.getDefaultWorld(), "foo", template, new Point(0, 0, 0), new Point(10, 10, 10));
			System.out.println(zownTree);
			
			// TODO test load zowns from DB
			
			// final ITemplate template = templateManager.getTemplate("worldTemplate");
			// System.out.println("Template: " + template);
			// System.out.println(template.getConfiguration());
			
			// System.out.println(templateManager.removeTemplate("tory"));
			
			// final TemplateDao worldTemplate = new TemplateDao();
			// worldTemplate.templateName = "worldTemplate";
			// worldTemplate.allowList = new ArrayList<String>();
			// worldTemplate.allowList.add("build");
			// worldTemplate.allowList.add("mobDamage");
			// worldTemplate.save();
			//
			// final TemplateDao serverTemplate = new TemplateDao();
			// serverTemplate.templateName = "serverTemplate";
			// serverTemplate.denyList = new ArrayList<String>();
			// serverTemplate.denyList.add("build");
			// serverTemplate.denyList.add("mobDamage");
			// serverTemplate.save();
			//
			// final TemplateDao plotTemplate = new TemplateDao();
			// plotTemplate.templateName = "plotTemplate";
			// plotTemplate.denyList = new ArrayList<String>();
			// plotTemplate.denyList.add("mobDamage");
			// plotTemplate.ownerPermissionList = new ArrayList<String>();
			// plotTemplate.ownerPermissionList.add("mobDamage");
			// plotTemplate.save();
			
			// final TemplateDao template = new TemplateDao();
			// template.templateName = "plotTemplate";
			// System.out.println(template.read());
			// System.out.println(template);
			
			// final TemplateDao template1 = new TemplateDao();
			// template1.templateName = "template1";
			// System.out.println("Read template1: " + template1.read());
			// template1.allowList = new ArrayList<String>();
			// template1.allowList.add("build");
			// template1.allowList.add("mobDamage");
			// System.out.println("Saved template1: " + template1.save());
			//
			// // System.out.println("Saved template id: " + template1.id);
			//
			// final ZownDao zown1 = new ZownDao();
			// zown1.worldName = "world1";
			// // zown1.zownName = "zown1";
			// System.out.println("Read zown1: " + zown1.read());
			// zown1.templateName = "template1";
			// // zown1.allowList = new ArrayList<String>();
			// // zown1.allowList.add("build");
			// System.out.println("Saved zown1: " + zown1.save());
			
			// final ZownDao zown2 = new ZownDao();
			// zown2.worldName = "world1";
			// zown2.zownName = "zown2";
			// System.out.println("Read zown2: " + zown2.read());
			//
			// System.out.println(zown2.delete());
			
			// final ZownDao zown2 = new ZownDao();
			// zown2.worldName = "world1";
			// zown2.zownName = "zown2";
			// zown2.parentZownName = "zown1";
			// // zown2.templateName = "template1";
			// System.out.println("Saved zown2: " + zown2.save());
			//
			// final ZownDao zown3 = new ZownDao();
			// zown3.worldName = "world1";
			// zown3.zownName = "zown3";
			// zown3.parentZownName = "zown2";
			// // zown3.templateName = "template1";
			// System.out.println("Saved zown3: " + zown3.save());
			
		} catch (final Exception e) {
			LOG.error("Error running", e);
		}
		
		Canary.getServer().initiateShutdown("Stopping");
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
		// zownManager.loadZowns(hook.getWorld());
	}
	
	@HookHandler
	public void onWorldUnload(final UnloadWorldHook hook) {
		// zownManager.unloadZowns(hook.getWorld());
	}
}
