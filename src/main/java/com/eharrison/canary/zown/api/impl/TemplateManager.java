package com.eharrison.canary.zown.api.impl;

import java.util.HashMap;
import java.util.Map;

import com.eharrison.canary.zown.api.ITemplateManager;

public class TemplateManager implements ITemplateManager {
	private final Map<String, Template> templates;
	
	public TemplateManager() {
		templates = new HashMap<String, Template>();
	}
	
	@Override
	public Template getTemplate(final String name) {
		return templates.get(name);
	}
	
	@Override
	public Template createTemplate(final String name) {
		Template template = null;
		if (!templates.containsKey(name)) {
			template = new Template(name);
			templates.put(name, template);
		}
		return template;
	}
	
	@Override
	public boolean removeTemplate(final String name) {
		boolean removed = false;
		final Template template = templates.get(name);
		if (template != null) {
			for (final Zown zown : template.getZowns()) {
				zown.setTemplate(null);
			}
			templates.remove(name);
			removed = true;
		}
		return removed;
	}
	
	@Override
	public boolean renameTemplate(final String oldName, final String newName) {
		boolean renamed = false;
		if (!oldName.equals(newName)) {
			final Template template = templates.get(oldName);
			if (template != null && !templates.containsKey(newName)) {
				template.setName(newName);
				templates.remove(oldName);
				templates.put(newName, template);
				renamed = true;
			}
		}
		return renamed;
	}
}
