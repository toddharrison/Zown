package com.eharrison.canary.zown.command;

import static com.eharrison.canary.zown.ZownMessenger.*;

import java.util.Collection;

import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.Command;
import net.canarymod.commandsys.CommandListener;

import com.eharrison.canary.zown.api.ITemplate;
import com.eharrison.canary.zown.api.ITemplateManager;

public class TemplateCommand implements CommandListener {
	private final ITemplateManager templateManager;
	
	public TemplateCommand(final ITemplateManager templateManager) {
		this.templateManager = templateManager;
	}
	
	@Command(aliases = {
		"template"
	}, parent = "zown", description = "zown template", permissions = {
		"zown.template"
	}, toolTip = "/zown template <list | info | create | delete | rename>")
	public void zownTemplateCommand(final MessageReceiver caller, final String[] parameters) {
		sendMessage(caller, "Usage: /zown template <list | info | create | delete | rename>");
	}
	
	@Command(aliases = {
		"list"
	}, parent = "template", description = "zown template list", permissions = {
		"zown.template.list"
	}, toolTip = "/zown template list")
	public void zownTemplateListCommand(final MessageReceiver caller, final String[] parameters) {
		if (parameters.length > 1) {
			sendMessage(caller, "Usage: /zown template list");
		} else {
			final Collection<? extends ITemplate> templates = templateManager.getTemplates();
			if (templates.isEmpty()) {
				sendMessage(caller, "No templates.");
			} else {
				for (final ITemplate template : templates) {
					sendMessage(caller, template.getName());
				}
			}
		}
	}
	
	@Command(aliases = {
		"info"
	}, parent = "template", description = "zown template info", permissions = {
		"zown.template.info"
	}, toolTip = "/zown template info <template>")
	public void zownTemplateInfoCommand(final MessageReceiver caller, final String[] parameters) {
		if (parameters.length != 2) {
			sendMessage(caller, "Usage: /zown template info <template>");
		} else {
			final ITemplate template = templateManager.getTemplate(parameters[1]);
			if (template == null) {
				sendMessage(caller, "No template '" + parameters[1] + "' exists.");
			} else {
				sendMessage(caller, template);
			}
		}
	}
	
	@Command(aliases = {
		"create"
	}, parent = "template", description = "zown template create", permissions = {
		"zown.template.create"
	}, toolTip = "/zown template create <template>")
	public void zownTemplateCreateCommand(final MessageReceiver caller, final String[] parameters) {
		if (parameters.length != 2) {
			sendMessage(caller, "Usage: /zown template create <template>");
		} else {
			final ITemplate template = templateManager.createTemplate(parameters[1]);
			if (template == null) {
				sendMessage(caller, "Template '" + parameters[1] + "' already exists.");
			} else {
				sendMessage(caller, "Created template '" + parameters[1] + "'.");
			}
		}
	}
	
	@Command(aliases = {
		"delete"
	}, parent = "template", description = "zown template delete", permissions = {
		"zown.template.delete"
	}, toolTip = "/zown template delete <template>")
	public void zownTemplateDeleteCommand(final MessageReceiver caller, final String[] parameters) {
		if (parameters.length != 2) {
			sendMessage(caller, "Usage: /zown template delete <template>");
		} else {
			if (templateManager.removeTemplate(parameters[1])) {
				sendMessage(caller, "Deleted template '" + parameters[1] + "'.");
			} else {
				sendMessage(caller, "Template '" + parameters[1] + "' doesn't exist.");
			}
		}
	}
	
	@Command(aliases = {
		"rename"
	}, parent = "template", description = "zown template rename", permissions = {
		"zown.template.rename"
	}, toolTip = "/zown template rename <template> <newTemplate>")
	public void zownRenameTemplateCommand(final MessageReceiver caller, final String[] parameters) {
		if (parameters.length != 3) {
			sendMessage(caller, "Usage: /zown template rename <template> <newTemplate>");
		} else {
			final String curName = parameters[1];
			final String newName = parameters[2];
			if (templateManager.renameTemplate(curName, newName)) {
				sendMessage(caller, "Renamed template '" + curName + "' to '" + newName + "'.");
			} else {
				sendMessage(caller, "Could not rename template '" + curName + "' to '" + newName + "'.");
			}
		}
	}
}
