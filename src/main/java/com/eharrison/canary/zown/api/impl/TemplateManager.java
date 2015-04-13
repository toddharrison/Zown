package com.eharrison.canary.zown.api.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.eharrison.canary.zown.ZownPlugin;
import com.eharrison.canary.zown.api.ITemplateManager;
import com.eharrison.canary.zown.dao.DataManager;

public class TemplateManager implements ITemplateManager {
	private final Map<String, Template> templates;
	private final DataManager dataManager;
	
	public TemplateManager(final DataManager dataManager) {
		templates = new HashMap<String, Template>();
		this.dataManager = dataManager;
		
		try {
			dataManager.loadTemplates(this);
		} catch (final Exception e) {
			ZownPlugin.LOG.error("Error loading templates", e);
		}
	}
	
	@Override
	public Collection<Template> getTemplates() {
		return Collections.unmodifiableCollection(templates.values());
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
			
			try {
				dataManager.saveTemplate(template);
			} catch (final Exception e) {
				ZownPlugin.LOG.error("Error saving template", e);
			}
		}
		return template;
	}
	
	public Template addTemplate(final String name) {
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
			
			try {
				dataManager.removeTemplate(template);
			} catch (final Exception e) {
				ZownPlugin.LOG.error("Error removing template", e);
			}
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
				
				try {
					dataManager.saveTemplate(template, oldName);
				} catch (final Exception e) {
					ZownPlugin.LOG.error("Error renaming template", e);
				}
			}
		}
		return renamed;
	}
	
	@Override
	public boolean saveTemplateConfiguration(final String name) {
		boolean saved = false;
		final Template template = templates.get(name);
		if (template != null) {
			try {
				dataManager.saveTemplate(template);
				saved = true;
			} catch (final Exception e) {
				ZownPlugin.LOG.error("Error saving template", e);
			}
		}
		return saved;
	}
}
