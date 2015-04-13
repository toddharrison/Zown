package com.eharrison.canary.zown.command;

import static com.eharrison.canary.zown.ZownMessenger.*;
import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.World;
import net.canarymod.api.world.WorldManager;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.Command;
import net.canarymod.commandsys.CommandListener;

import com.eharrison.canary.zown.api.ITemplate;
import com.eharrison.canary.zown.api.ITemplateManager;
import com.eharrison.canary.zown.api.IZown;
import com.eharrison.canary.zown.api.IZownManager;
import com.eharrison.canary.zown.api.Point;
import com.eharrison.canary.zown.api.impl.Tree;

public class ZownCommand implements CommandListener {
	private final WorldManager worldManager;
	private final IZownManager zownManager;
	private final ITemplateManager templateManager;
	
	public ZownCommand(final ITemplateManager templateManager, final IZownManager zownManager) {
		worldManager = Canary.getServer().getWorldManager();
		this.zownManager = zownManager;
		this.templateManager = templateManager;
	}
	
	@Command(aliases = {
		"zown"
	}, description = "zown", permissions = {
		"zown.zown"
	}, toolTip = "/zown <list | info | show | create | expand | delete | rename | editpoints | template>")
	public void zownCommand(final MessageReceiver caller, final String[] parameters) {
		sendMessage(caller,
				"Usage: /zown <list | info | show | create | expand | delete | rename | editpoints | template>");
	}
	
	@Command(aliases = {
		"list"
	}, parent = "zown", description = "zown list", permissions = {
		"zown.zown.list"
	}, toolTip = "/zown list")
	public void zownListCommand(final MessageReceiver caller, final String[] parameters) {
		World world = null;
		
		if (caller instanceof Player) {
			final Player player = caller.asPlayer();
			switch (parameters.length) {
				case 1:
					world = player.getWorld();
					break;
				default:
					sendMessage(caller, "Usage: /zown list");
			}
		} else {
			switch (parameters.length) {
				case 2:
					world = worldManager.getWorld(parameters[1], false);
					break;
				default:
					sendMessage(caller, "Usage: /zown list <world>");
			}
		}
		
		if (world != null) {
			final Tree<? extends IZown> zownRootTree = zownManager.getZown(world);
			for (final Tree<? extends IZown> zownTree : zownRootTree) {
				sendMessage(caller, zownTree.getData().getName());
			}
		}
	}
	
	@Command(aliases = {
		"info"
	}, parent = "zown", description = "zown info", permissions = {
		"zown.zown.info"
	}, toolTip = "/zown info <zown>")
	public void zownInfoCommand(final MessageReceiver caller, final String[] parameters) {
		World world = null;
		String zown = null;
		
		if (caller instanceof Player) {
			final Player player = caller.asPlayer();
			switch (parameters.length) {
				case 2:
					world = player.getWorld();
					zown = parameters[1];
					break;
				default:
					sendMessage(caller, "Usage: /zown info <zown>");
			}
		} else {
			switch (parameters.length) {
				case 3:
					world = worldManager.getWorld(parameters[1], false);
					zown = parameters[2];
					break;
				default:
					sendMessage(caller, "Usage: /zown info <world> <zown>");
			}
		}
		
		if (world != null && zown != null) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(world, zown);
			if (zownTree == null) {
				sendMessage(caller, "There is no zown '" + zown + "' in world " + world.getFqName() + ".");
			} else {
				sendMessage(caller, zownTree.getData());
			}
		}
	}
	
	@Command(aliases = {
		"show"
	}, parent = "zown", description = "zown show", permissions = {
		"zown.zown.show"
	}, toolTip = "/zown show [zown]")
	public void zownShowCommand(final MessageReceiver caller, final String[] parameters) {
		// TODO
		if (caller instanceof Player) {
			sendMessage(caller, "called zown show");
		}
	}
	
	@Command(aliases = {
		"create"
	}, parent = "zown", description = "zown create", permissions = {
		"zown.zown.create"
	}, toolTip = "/zown create <zown> [template] <x1 y1 z1> <x2 y2 z2>")
	public void zownCreateCommand(final MessageReceiver caller, final String[] parameters) {
		final ParameterTokenizer pTokens = new ParameterTokenizer(parameters);
		World world = null;
		String zown = null;
		ITemplate template = null;
		Point p1 = null;
		Point p2 = null;
		
		if (caller instanceof Player) {
			final Player player = caller.asPlayer();
			switch (parameters.length) {
				case 8:
					world = player.getWorld();
					zown = pTokens.readString();
					p1 = pTokens.readPoint();
					p2 = pTokens.readPoint();
					break;
				case 9:
					world = player.getWorld();
					zown = pTokens.readString();
					template = pTokens.readTemplate(templateManager);
					p1 = pTokens.readPoint();
					p2 = pTokens.readPoint();
					break;
				default:
					sendMessage(caller, "Usage: /zown create <zown> [template] <x1 y1 z1> <x2 y2 z2>");
			}
		} else {
			switch (parameters.length) {
				case 9:
					world = pTokens.readWorld(worldManager);
					zown = pTokens.readString();
					p1 = pTokens.readPoint();
					p2 = pTokens.readPoint();
					break;
				case 10:
					world = pTokens.readWorld(worldManager);
					zown = pTokens.readString();
					template = pTokens.readTemplate(templateManager);
					p1 = pTokens.readPoint();
					p2 = pTokens.readPoint();
					break;
				default:
					sendMessage(caller, "Usage: /zown create <world> <zown> [template] <x1 y1 z1> <x2 y2 z2>");
			}
		}
		
		if (world != null && zown != null && p1 != null && p2 != null) {
			final Tree<? extends IZown> zownTree = zownManager.createZown(world, zown, template, p1, p2);
			if (zownTree == null) {
				sendMessage(caller, "Failed to create zown '" + zown + "'.");
			} else {
				sendMessage(caller, "Created zown '" + zown + "'.");
			}
		}
	}
	
	@Command(aliases = {
		"expand"
	}, parent = "zown", description = "zown expand", permissions = {
		"zown.zown.expand"
	}, toolTip = "/zown expand <zown>")
	public void zownExpandCommand(final MessageReceiver caller, final String[] parameters) {
		World world = null;
		String zown = null;
		
		if (caller instanceof Player) {
			final Player player = caller.asPlayer();
			switch (parameters.length) {
				case 2:
					world = player.getWorld();
					zown = parameters[1];
					break;
				default:
					sendMessage(caller, "Usage: /zown expand <zown>");
			}
		} else {
			switch (parameters.length) {
				case 3:
					world = worldManager.getWorld(parameters[1], false);
					zown = parameters[2];
					break;
				default:
					sendMessage(caller, "Usage: /zown expand <world> <zown>");
			}
		}
		
		if (world != null && zown != null) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(world, zown);
			if (zownTree != null) {
				final Point p1 = zownTree.getData().getMinPoint().clone();
				p1.y = 0;
				final Point p2 = zownTree.getData().getMaxPoint().clone();
				p2.y = 255;
				if (zownManager.resizeZown(world, zown, p1, p2)) {
					sendMessage(caller, "Expanded zown '" + zown + "'.");
				} else {
					sendMessage(caller, "Failed to expand zown '" + zown + "'.");
				}
			} else {
				sendMessage(caller, "No zown '" + zown + "' exists.");
			}
		}
	}
	
	@Command(aliases = {
		"delete"
	}, parent = "zown", description = "zown delete", permissions = {
		"zown.zown.delete"
	}, toolTip = "/zown delete <zown>")
	public void zownDeleteCommand(final MessageReceiver caller, final String[] parameters) {
		World world = null;
		String zown = null;
		
		if (caller instanceof Player) {
			final Player player = caller.asPlayer();
			switch (parameters.length) {
				case 2:
					world = player.getWorld();
					zown = parameters[1];
					break;
				default:
					sendMessage(caller, "Usage: /zown delete <zown>");
			}
		} else {
			switch (parameters.length) {
				case 3:
					world = worldManager.getWorld(parameters[1], false);
					zown = parameters[2];
					break;
				default:
					sendMessage(caller, "Usage: /zown delete <world> <zown>");
			}
		}
		
		if (world != null && zown != null) {
			if (zownManager.removeZown(world, zown)) {
				sendMessage(caller, "Deleted zown '" + zown + "'.");
			} else {
				sendMessage(caller, "Failed to delete zown '" + zown + "'.");
			}
		}
	}
	
	@Command(aliases = {
		"rename"
	}, parent = "zown", description = "zown rename", permissions = {
		"zown.zown.rename"
	}, toolTip = "/zown rename <zown> <newZown>")
	public void zownRenameCommand(final MessageReceiver caller, final String[] parameters) {
		World world = null;
		String zown = null;
		String newZown = null;
		
		if (caller instanceof Player) {
			final Player player = caller.asPlayer();
			switch (parameters.length) {
				case 3:
					world = player.getWorld();
					zown = parameters[1];
					newZown = parameters[2];
					break;
				default:
					sendMessage(caller, "Usage: /zown rename <zown> <newZown>");
			}
		} else {
			switch (parameters.length) {
				case 4:
					world = worldManager.getWorld(parameters[1], false);
					zown = parameters[2];
					newZown = parameters[3];
					break;
				default:
					sendMessage(caller, "Usage: /zown rename <world> <zown> <newZown>");
			}
		}
		
		if (world != null && zown != null && newZown != null) {
			if (zownManager.renameZown(world, zown, newZown)) {
				sendMessage(caller, "Renamed zown to '" + newZown + "'.");
			} else {
				sendMessage(caller, "Failed to rename zown '" + zown + "'.");
			}
		}
	}
	
	@Command(aliases = {
		"editpoints"
	}, parent = "zown", description = "zown editpoints", permissions = {
		"zown.zown.editpoints"
	}, toolTip = "/zown editpoints <zown> <x1 y1 z1> <x2 y2 z2>")
	public void zownEditPointsCommand(final MessageReceiver caller, final String[] parameters) {
		final ParameterTokenizer pTokens = new ParameterTokenizer(parameters);
		World world = null;
		String zown = null;
		Point p1 = null;
		Point p2 = null;
		
		if (caller instanceof Player) {
			final Player player = caller.asPlayer();
			switch (parameters.length) {
				case 8:
					world = player.getWorld();
					zown = pTokens.readString();
					p1 = pTokens.readPoint();
					p2 = pTokens.readPoint();
					break;
				default:
					sendMessage(caller, "Usage: /zown editpoints <zown> <x1 y1 z1> <x2 y2 z2>");
			}
		} else {
			switch (parameters.length) {
				case 9:
					world = pTokens.readWorld(worldManager);
					zown = pTokens.readString();
					p1 = pTokens.readPoint();
					p2 = pTokens.readPoint();
					break;
				default:
					sendMessage(caller, "Usage: /zown editpoints <world> <zown> <x1 y1 z1> <x2 y2 z2>");
			}
		}
		
		if (world != null && zown != null && p1 != null && p2 != null) {
			final Tree<? extends IZown> zownTree = zownManager.getZown(world, zown);
			if (zownTree != null) {
				if (zownManager.resizeZown(world, zown, p1, p2)) {
					sendMessage(caller, "Resized zown '" + zown + "'.");
				} else {
					sendMessage(caller, "Failed to resize zown '" + zown + "'.");
				}
			} else {
				sendMessage(caller, "No zown '" + zown + "' exists.");
			}
		}
	}
}
