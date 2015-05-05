package com.eharrison.canary.zown.api.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import net.canarymod.Canary;
import net.canarymod.api.OfflinePlayer;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.ChatFormat;

import com.eharrison.canary.zown.ZownPlugin;
import com.eharrison.canary.zown.api.IZown;
import com.eharrison.canary.zown.api.Point;

public class Zown implements IZown {
	private String name;
	private Template template;
	private Configuration configuration;
	private final Set<String> owners;
	private final Set<String> members;
	private final Set<String> entryExclusions;
	private String welcomeMessage;
	private String farewellMessage;
	
	private Point minPoint;
	private Point maxPoint;
	private Point centerPoint;
	private int minRadiusSquared;
	private int maxRadiusSquared;
	
	protected Zown(final String name) {
		this(name, null, null, null);
	}
	
	protected Zown(final String name, final Template template) {
		this(name, template, null, null);
	}
	
	protected Zown(final String name, final Point p1, final Point p2) {
		this(name, null, p1, p2);
	}
	
	protected Zown(final String name, final Template template, final Point p1, final Point p2) {
		this.name = name;
		this.template = template;
		if (template == null) {
			configuration = new Configuration();
		} else {
			template.addZown(this);
		}
		owners = new HashSet<String>();
		members = new HashSet<String>();
		entryExclusions = new HashSet<String>();
		
		if (p1 != null && p2 != null) {
			setBounds(p1, p2);
		}
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	protected void setName(final String name) {
		this.name = name;
	}
	
	@Override
	public String getDisplay() {
		final StringBuilder sb = new StringBuilder();
		
		sb.append("\n");
		
		sb.append(name);
		if (template != null) {
			sb.append(" (");
			if (overridesConfiguration()) {
				sb.append(ChatFormat.GRAY);
			}
			sb.append(template.getName());
			if (overridesConfiguration()) {
				sb.append(ChatFormat.RESET);
			}
			sb.append(")");
		}
		sb.append(" [");
		sb.append(minPoint.x);
		sb.append(",");
		sb.append(minPoint.y);
		sb.append(",");
		sb.append(minPoint.z);
		sb.append("] to [");
		sb.append(maxPoint.x);
		sb.append(",");
		sb.append(maxPoint.y);
		sb.append(",");
		sb.append(maxPoint.z);
		sb.append("]");
		sb.append("\n");
		
		sb.append("Owners: ");
		if (owners.isEmpty()) {
			sb.append("none");
		} else {
			final Iterator<String> iter = owners.iterator();
			while (iter.hasNext()) {
				final String uuid = iter.next();
				final String name = getPlayerName(uuid);
				if (name != null) {
					sb.append(name);
					if (iter.hasNext()) {
						sb.append(", ");
					}
				}
			}
		}
		sb.append("\n");
		
		sb.append("Members: ");
		if (members.isEmpty()) {
			sb.append("none");
		} else {
			final Iterator<String> iter = members.iterator();
			while (iter.hasNext()) {
				final String uuid = iter.next();
				final String name = getPlayerName(uuid);
				if (name != null) {
					sb.append(name);
					if (iter.hasNext()) {
						sb.append(", ");
					}
				}
			}
		}
		sb.append("\n");
		
		final Configuration config = getConfiguration();
		sb.append("Flags: ");
		sb.append(config.getFlags());
		
		return sb.toString();
	}
	
	@Override
	public String getWelcomeMessage() {
		return welcomeMessage;
	}
	
	@Override
	public void setWelcomeMessage(final String welcomeMessage) {
		this.welcomeMessage = welcomeMessage;
	}
	
	@Override
	public String getFarewellMessage() {
		return farewellMessage;
	}
	
	@Override
	public void setFarewellMessage(final String farewellMessage) {
		this.farewellMessage = farewellMessage;
	}
	
	@Override
	public Template getTemplate() {
		return template;
	}
	
	public void setTemplate(final Template template) {
		setTemplate(template, false);
	}
	
	public void setTemplate(final Template template, final boolean overrideTemplate) {
		if (template == null) {
			if (this.template == null) {
				configuration = new Configuration();
			} else {
				configuration = this.template.getConfiguration().clone();
			}
			this.template = null;
		} else {
			this.template = template;
		}
		
		if (configuration == null && overrideTemplate) {
			configuration = new Configuration();
		}
	}
	
	public void loadTemplate(final Template template) {
		if (template != null) {
			this.template = template;
			configuration = null;
		}
	}
	
	@Override
	public Configuration getConfiguration() {
		if (configuration == null) {
			return template.getConfiguration();
		}
		return configuration;
	}
	
	@Override
	public boolean overridesConfiguration() {
		return configuration != null;
	}
	
	@Override
	public boolean setOverridesConfiguration(final boolean overridesConfiguration) {
		boolean changed = false;
		if (overridesConfiguration) {
			if (configuration == null) {
				configuration = template.getConfiguration().clone();
				changed = true;
			}
		} else {
			if (configuration != null && template != null) {
				configuration = null;
				changed = true;
			}
		}
		return changed;
	}
	
	@Override
	public Point getMinPoint() {
		return minPoint;
	}
	
	@Override
	public Point getMaxPoint() {
		return maxPoint;
	}
	
	@Override
	public Point getCenterPoint() {
		return centerPoint;
	}
	
	protected void setBounds(final Point p1, final Point p2) {
		minPoint = new Point(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y), Math.min(p1.z, p2.z));
		maxPoint = new Point(Math.max(p1.x, p2.x), Math.max(p1.y, p2.y), Math.max(p1.z, p2.z));
		centerPoint = new Point((int) Math.floor((minPoint.x + maxPoint.x) / 2.0),
				(int) Math.floor((minPoint.y + maxPoint.y) / 2.0),
				(int) Math.floor((minPoint.z + maxPoint.z) / 2.0));
		
		final int minRadius = Math.min(
				Math.min(centerPoint.x - minPoint.x, centerPoint.y - minPoint.y), centerPoint.z
						- minPoint.z);
		minRadiusSquared = minRadius * minRadius;
		maxRadiusSquared = centerPoint.distanceSquared(maxPoint);
	}
	
	@Override
	public boolean addOwner(final Player player) {
		removeMember(player);
		return owners.add(player.getUUIDString());
	}
	
	@Override
	public boolean removeOwner(final Player player) {
		return owners.remove(player.getUUIDString());
	}
	
	@Override
	public boolean isOwner(final Player player) {
		return owners.contains(player.getUUIDString());
	}
	
	public Collection<String> getOwnerUUIDs() {
		return owners;
	}
	
	@Override
	public boolean addMember(final Player player) {
		boolean added = false;
		final String uuid = player.getUUIDString();
		if (!owners.contains(uuid)) {
			added = members.add(uuid);
		}
		return added;
	}
	
	@Override
	public boolean removeMember(final Player player) {
		return members.remove(player.getUUIDString());
	}
	
	@Override
	public boolean isMember(final Player player) {
		return members.contains(player.getUUIDString());
	}
	
	public Collection<String> getMemberUUIDs() {
		return members;
	}
	
	@Override
	public boolean isOwnerOrMember(final Player player) {
		return isOwner(player) || isMember(player);
	}
	
	@Override
	public boolean addEntryExclusion(final Player player) {
		return entryExclusions.add(player.getUUIDString());
	}
	
	@Override
	public boolean removeEntryExclusion(final Player player) {
		return entryExclusions.remove(player.getUUIDString());
	}
	
	@Override
	public boolean hasEntryExclusion(final Player player) {
		return entryExclusions.contains(player.getUUIDString());
	}
	
	public Collection<String> getEntryExclusionUUIDs() {
		return entryExclusions;
	}
	
	@Override
	public int getBlockCount() {
		final int x = maxPoint.x - minPoint.x;
		final int y = maxPoint.y - minPoint.y;
		final int z = maxPoint.z - minPoint.z;
		return x * y * z;
	}
	
	@Override
	public int getColumnCount() {
		final int x = maxPoint.x - minPoint.x;
		final int z = maxPoint.z - minPoint.z;
		return x * z;
	}
	
	@Override
	public boolean contains(final Point p) {
		boolean contained = false;
		if (centerPoint == null) {
			contained = true;
		} else if (p != null) {
			if (centerPoint.distanceSquared(p) <= minRadiusSquared) {
				// Definite collision, inside of minimum radius
				contained = true;
			} else if (centerPoint.distanceSquared(p) <= maxRadiusSquared) {
				// Possible collision, verify precisely
				if (p.x >= minPoint.x && p.x <= maxPoint.x && p.y >= minPoint.y && p.y <= maxPoint.y
						&& p.z >= minPoint.z && p.z <= maxPoint.z) {
					contained = true;
				}
			}
		}
		return contained;
	}
	
	@Override
	public boolean contains(final IZown z) {
		boolean contained = false;
		if (centerPoint == null || contains(z.getMinPoint()) && contains(z.getMaxPoint())) {
			contained = true;
		}
		return contained;
	}
	
	@Override
	public boolean contains(final Point p1, final Point p2) {
		boolean contained = false;
		if (centerPoint == null || contains(p1) && contains(p2)) {
			contained = true;
		}
		return contained;
	}
	
	@Override
	public boolean intersects(final Point p1, final Point p2) {
		boolean intersects = false;
		if (centerPoint != null && contains(p1) ^ contains(p2)) {
			intersects = true;
		}
		return intersects;
	}
	
	@Override
	public boolean intersects(final IZown z) {
		boolean intersects = false;
		if (centerPoint != null && contains(z.getMinPoint()) ^ contains(z.getMaxPoint())) {
			intersects = true;
		}
		return intersects;
	}
	
	private String getPlayerName(final String uuid) {
		ZownPlugin.LOG.info("getPlayerName enter(" + uuid + ")");
		
		String name = null;
		final Player player = Canary.getServer().getPlayerFromUUID(uuid);
		if (player != null) {
			// Player online
			ZownPlugin.LOG.info("Found online Player");
			name = player.getName();
		} else {
			// Player offline
			final OfflinePlayer oPlayer = Canary.getServer().getOfflinePlayer(UUID.fromString(uuid));
			if (oPlayer != null) {
				ZownPlugin.LOG.info("Found offline Player");
				name = oPlayer.getName();
			}
		}
		
		ZownPlugin.LOG.info("getPlayerName exit(" + name + ")");
		
		return name;
	}
}
