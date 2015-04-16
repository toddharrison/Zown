package com.eharrison.canary.zown.command;

import static com.eharrison.canary.zown.ZownMessenger.*;

import java.util.Collection;

import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.Command;
import net.canarymod.commandsys.CommandListener;

import com.eharrison.canary.zown.api.IConfiguration;
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
	}, toolTip = "/zown template <list | info | create | delete | rename | ownerperm | flag>")
	public void templateCommand(final MessageReceiver caller, final String[] parameters) {
		sendMessage(caller,
				"Usage: /zown template <list | info | create | delete | rename | ownerperm | flag>");
	}
	
	@Command(aliases = {
		"list"
	}, parent = "template", description = "zown template list", permissions = {
		"zown.template.list"
	}, toolTip = "/zown template list")
	public void templateListCommand(final MessageReceiver caller, final String[] parameters) {
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
	public void templateInfoCommand(final MessageReceiver caller, final String[] parameters) {
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
	public void templateCreateCommand(final MessageReceiver caller, final String[] parameters) {
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
	public void templateDeleteCommand(final MessageReceiver caller, final String[] parameters) {
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
	public void templateRenameCommand(final MessageReceiver caller, final String[] parameters) {
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
	
	@Command(aliases = {
		"ownerperm"
	}, parent = "template", description = "zown template ownerperm", permissions = {
		"zown.template.ownerperm"
	}, toolTip = "/zown template ownerperm <template> <add | remove> <flag>")
	public void templateOwnerPermissionCommand(final MessageReceiver caller, final String[] parameters) {
		if (parameters.length != 4) {
			sendMessage(caller, "Usage: /zown template ownerperm <template> <add | remove> <flag>");
		} else {
			final String name = parameters[1];
			final ITemplate template = templateManager.getTemplate(name);
			if (template == null) {
				sendMessage(caller, "Template '" + parameters[1] + "' doesn't exist.");
			} else {
				if ("add".equalsIgnoreCase(parameters[2])) {
					final String flag = parameters[3];
					final IConfiguration config = template.getConfiguration();
					if (config.addOwnerPermission(flag)) {
						sendMessage(caller, "Added owner permission '" + flag + "' to template '"
								+ parameters[1] + "'.");
						templateManager.saveTemplateConfiguration(name);
					} else {
						sendMessage(caller, "Owner permission '" + flag + "' already exists on template.");
					}
				} else if ("remove".equalsIgnoreCase(parameters[2])) {
					final String flag = parameters[3];
					final IConfiguration config = template.getConfiguration();
					if (config.removeOwnerPermission(flag)) {
						sendMessage(caller, "Removed owner permission '" + flag + "' from template '"
								+ parameters[1] + "'.");
						templateManager.saveTemplateConfiguration(name);
					} else {
						sendMessage(caller, "Owner permission '" + flag + "' does not exist on template.");
					}
				} else {
					sendMessage(caller, "Usage: /zown template ownerperm <template> <add | remove> <flag>");
				}
			}
		}
	}
	
	@Command(aliases = {
		"flag"
	}, parent = "template", description = "zown template flag", permissions = {
		"zown.template.flag"
	}, toolTip = "/zown template flag <template> <flag>:<ALLOW | DENY>...")
	public void templateFlagCommand(final MessageReceiver caller, final String[] parameters) {
		if (parameters.length < 3) {
			sendMessage(caller, "Usage: /zown template flag <template> <flag>:<ALLOW | DENY>...");
		} else {
			final String name = parameters[1];
			final ITemplate template = templateManager.getTemplate(name);
			if (template == null) {
				sendMessage(caller, "Template '" + parameters[1] + "' doesn't exist.");
			} else {
				for (int i = 2; i < parameters.length; i++) {
					final String[] flag = parameters[i].split(":");
					if (flag.length == 2) {
						final IConfiguration config = template.getConfiguration();
						config.setFlag(flag[0], "allow".equalsIgnoreCase(flag[1]));
					} else {
						sendMessage(caller, "Bad flag: " + parameters[i]);
					}
				}
				templateManager.saveTemplateConfiguration(name);
				sendMessage(caller, "Added flags to template '" + parameters[1] + "'.");
			}
		}
	}
	
	@Command(aliases = {
		"restrictcommand"
	}, parent = "template", description = "zown template restrictcommand", permissions = {
		"zown.template.restrictcommand"
	}, toolTip = "/zown template restrictcommand <template> <add | remove> <command>")
	public void templateCommandRestrictionCommand(final MessageReceiver caller,
			final String[] parameters) {
		if (parameters.length != 4) {
			sendMessage(caller,
					"Usage: /zown template restrictcommand <template> <add | remove> <command>");
		} else {
			final String name = parameters[1];
			final ITemplate template = templateManager.getTemplate(name);
			if (template == null) {
				sendMessage(caller, "Template '" + parameters[1] + "' doesn't exist.");
			} else {
				if ("add".equalsIgnoreCase(parameters[2])) {
					final String command = parameters[3].toLowerCase();
					final IConfiguration config = template.getConfiguration();
					if (config.addCommandRestriction(command)) {
						sendMessage(caller, "Added command restriction '" + command + "' to template '"
								+ parameters[1] + "'.");
						templateManager.saveTemplateConfiguration(name);
					} else {
						sendMessage(caller, "Command restriction '" + command + "' already exists on template.");
					}
				} else if ("remove".equalsIgnoreCase(parameters[2])) {
					final String command = parameters[3].toLowerCase();
					final IConfiguration config = template.getConfiguration();
					if (config.removeCommandRestriction(command)) {
						sendMessage(caller, "Removed command restriction '" + command + "' from template '"
								+ parameters[1] + "'.");
						templateManager.saveTemplateConfiguration(name);
					} else {
						sendMessage(caller, "Command restriction '" + command + "' does not exist on template.");
					}
				} else {
					sendMessage(caller,
							"Usage: /zown template restrictcommand <template> <add | remove> <command>");
				}
			}
		}
	}
}
