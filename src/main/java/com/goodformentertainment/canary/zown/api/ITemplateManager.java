package com.goodformentertainment.canary.zown.api;

import java.util.Collection;

/**
 * A TemplateManager manages a Collection of Templates.
 * 
 * @author Todd Harrison
 */
public interface ITemplateManager {
	/**
	 * Get a Collection of all of the registered Templates in this manager.
	 * 
	 * @return A Collection of Templates.
	 */
	Collection<? extends ITemplate> getTemplates();
	
	/**
	 * Get a Template by name that is registered in this manager.
	 * 
	 * @param name
	 *          The name of the template.
	 * @return The Template or null if no Template by that name exists in this manager.
	 */
	ITemplate getTemplate(String name);
	
	/**
	 * Copies an existing template into a new template.
	 * 
	 * @param fromTemplate
	 *          The name of the Template to copy.
	 * @param name
	 *          The name of the new Template to create.
	 * @return A new Template with a copy of fromTemplate configuration or null if from doesn't exist
	 *         or that name already exists.
	 */
	ITemplate copyTemplate(String fromTemplate, String name);
	
	/**
	 * Creates a new Template in this manager.
	 * 
	 * @param name
	 *          The name of the new Template to create.
	 * @return A new Template with the specified name or null if that name already exists.
	 */
	ITemplate createTemplate(String name);
	
	/**
	 * Removes a Template from this manager.
	 * 
	 * @param name
	 *          The name of the Template to remove.
	 * @return True if the Template was removed, false if not present.
	 */
	boolean removeTemplate(String name);
	
	/**
	 * Renames a Template in this manager.
	 * 
	 * @param oldName
	 *          The current name of the Template.
	 * @param newName
	 *          The new name for the Template.
	 * @return True if the Template was renamed, false if not present or if the new name already
	 *         exists.
	 */
	boolean renameTemplate(String oldName, String newName);
	
	/**
	 * Saves a Template to the database. This can be used to persist configuration changes.
	 * 
	 * @param name
	 *          The Template name to save.
	 * @return True if the Template was saved, false otherwise.
	 */
	boolean saveTemplateConfiguration(String name);
}
