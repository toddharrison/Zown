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
		
		Canary.commands().unregisterCommands(this);
		Canary.hooks().unregisterPluginListeners(this);
	}
}
