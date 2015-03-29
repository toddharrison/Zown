package com.eharrison.canary.zown.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Template implements IConfigurable {
	private static Map<String, Template> templates = new HashMap<String, Template>();
	
	public static final Template create(final String name) {
		Template template = null;
		if (!templates.containsKey(name)) {
			template = new Template(name);
		}
		return template;
	}
	
	public static final Template get(final String name) {
		return templates.get(name);
	}
	
	public static final boolean remove(final String name) {
		boolean removed = false;
		final Template template = templates.get(name);
		if (template != null) {
			if (template.getZowns().isEmpty()) {
				templates.remove(name);
				removed = true;
			}
		}
		return removed;
	}
	
	private String name;
	private final Configuration configuration;
	private final Set<Zown> zowns;
	
	protected Template(final String name) {
		super();
		this.name = name;
		configuration = new Configuration();
		zowns = new HashSet<Zown>();
	}
	
	public String getName() {
		return name;
	}
	
	public boolean setName(final String name) {
		boolean added = false;
		if (!templates.containsKey(name)) {
			templates.remove(this.name);
			this.name = name;
			templates.put(name, this);
			added = true;
		}
		return added;
	}
	
	@Override
	public Configuration getConfiguration() {
		return configuration;
	}
	
	public Collection<Zown> getZowns() {
		return zowns;
	}
}
