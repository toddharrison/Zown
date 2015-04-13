package com.eharrison.canary.zown.api;

import java.util.Collection;

public interface ITemplateManager {
	Collection<? extends ITemplate> getTemplates();
	
	ITemplate getTemplate(String name);
	
	ITemplate createTemplate(String name);
	
	boolean removeTemplate(String name);
	
	boolean renameTemplate(String oldName, String newName);
	
	boolean saveTemplateConfiguration(String name);
}
