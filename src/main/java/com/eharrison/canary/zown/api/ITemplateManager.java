package com.eharrison.canary.zown.api;

public interface ITemplateManager {
	ITemplate getTemplate(String name);
	
	ITemplate createTemplate(String name);
	
	boolean removeTemplate(String name);
	
	boolean renameTemplate(String oldName, String newName);
}
