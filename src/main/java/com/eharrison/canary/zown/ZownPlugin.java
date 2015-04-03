package com.eharrison.canary.zown;

import net.canarymod.Canary;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.logger.Logman;
import net.canarymod.plugin.Plugin;

import com.eharrison.canary.zown.dao.TemplateDao;

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
			
			final TemplateDao template1 = new TemplateDao();
			// template1.templateName = "template1";
			// System.out.println("Read template1: " + template1.read());
			// template1.allowList = new ArrayList<String>();
			// template1.allowList.add("build");
			// template1.allowList.add("mobDamage");
			// System.out.println("Saved template1: " + template1.save());
			
			// System.out.println("Saved template id: " + template1.id);
			
			// final ZownDao zown1 = new ZownDao();
			// zown1.worldName = "world1";
			// zown1.zownName = "zown1";
			// System.out.println("Read zown1: " + zown1.read());
			// zown1.templateName = "template1";
			// zown1.allowList = new ArrayList<String>();
			// zown1.allowList.add("build");
			// System.out.println("Saved zown1: " + zown1.save());
			
			// // TODO: I should not have to specify the template id
			// template1.id = 1;
			// template1.templateName = "template2";
			// System.out.println("Saved template2: " + template1.save());
			
			final TemplateDao templateDao = new TemplateDao();
			templateDao.id = 1;
			System.out.println("Found: " + templateDao.read());
			System.out.println("Delete: " + templateDao.delete());
			
			// templateDao.id = 1;
			// templateDao.templateName = "foo";
			// System.out.println("Saved: " + templateDao.save());
			//
			// System.out.println("Delete: " + templateDao.delete());
			
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
