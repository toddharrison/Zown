package com.eharrison.canary.zown;

import net.canarymod.api.world.World;
import net.canarymod.config.Configuration;
import net.visualillusionsent.utils.PropertiesFile;

public class ZownConfiguration {
	private final PropertiesFile cfg;
	
	public ZownConfiguration(final ZownPlugin plugin) {
		cfg = Configuration.getPluginConfig(plugin);
	}
	
	public String getUniversalDefaultTemplate() {
		String template = null;
		final String key = "universal.template.default";
		if (cfg.containsKey(key)) {
			template = cfg.getString(key);
		}
		return template;
	}
	
	public String getDefaultWorldTemplate(final World world) {
		String template = null;
		final String key = "world." + world.getFqName() + ".template.default";
		if (cfg.containsKey(key)) {
			template = cfg.getString(key);
		} else {
			template = getUniversalDefaultTemplate();
		}
		return template;
	}
	
	public String getLoggingLevel() {
		String level = null;
		final String key = "log.level";
		if (cfg.containsKey(key)) {
			level = cfg.getString(key);
		}
		return level;
	}
}
