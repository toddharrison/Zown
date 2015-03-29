package com.eharrison.canary.zown.api;

import java.util.HashSet;
import java.util.Set;

import net.canarymod.api.entity.living.humanoid.Player;

public class Zown implements IConfigurable {
	private String name;
	private Template template;
	private Configuration configuration;
	private final Set<String> owners;
	private final Set<String> members;
	private final Set<String> entryExclusion;
	
	private Point minPoint;
	private Point maxPoint;
	private Point centerPoint;
	private int minRadiusSquared;
	private int maxRadiusSquared;
	
	protected Zown(final String name) {
		this(name, null, null, null);
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
			template.getZowns().add(this);
		}
		owners = new HashSet<String>();
		members = new HashSet<String>();
		entryExclusion = new HashSet<String>();
		
		if (p1 != null && p2 != null) {
			setBounds(p1, p2);
		}
	}
	
	protected void setName(final String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public Template getTemplate() {
		return template;
	}
	
	public void setTemplate(final Template template) {
		if (this.template != null) {
			this.template.getZowns().remove(this);
		}
		this.template = template;
		configuration = null;
		template.getZowns().add(this);
	}
	
	public void removeTemplate() {
		if (template != null) {
			template.getZowns().remove(this);
		}
		if (configuration == null) {
			configuration = template.getConfiguration().clone();
		}
		template = null;
	}
	
	@Override
	public Configuration getConfiguration() {
		if (configuration == null) {
			return template.getConfiguration();
		}
		return configuration;
	}
	
	public Point getMinPoint() {
		return minPoint;
	}
	
	public Point getMaxPoint() {
		return maxPoint;
	}
	
	public void setBounds(final Point p1, final Point p2) {
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
	
	public boolean addOwner(final Player player) {
		return owners.add(player.getUUIDString());
	}
	
	public boolean removeOwner(final Player player) {
		return owners.remove(player.getUUIDString());
	}
	
	public boolean isOwner(final Player player) {
		return owners.contains(player.getUUIDString());
	}
	
	public boolean addMember(final Player player) {
		boolean added = false;
		final String uuid = player.getUUIDString();
		if (!owners.contains(uuid)) {
			added = members.add(uuid);
		}
		return added;
	}
	
	public boolean removeMember(final Player player) {
		return members.remove(player.getUUIDString());
	}
	
	public boolean isMember(final Player player) {
		return members.contains(player.getUUIDString());
	}
	
	public boolean isOwnerOrMember(final Player player) {
		return isOwner(player) || isMember(player);
	}
	
	public boolean addEntryExclusion(final Player player) {
		return entryExclusion.add(player.getUUIDString());
	}
	
	public boolean removeEntryExclusion(final Player player) {
		return entryExclusion.remove(player.getUUIDString());
	}
	
	public boolean hasEntryExclusion(final Player player) {
		return entryExclusion.contains(player.getUUIDString());
	}
	
	public int getBlockCount() {
		final int x = maxPoint.x - minPoint.x;
		final int y = maxPoint.y - minPoint.y;
		final int z = maxPoint.z - minPoint.z;
		return x * y * z;
	}
	
	public int getColumnCount() {
		final int x = maxPoint.x - minPoint.x;
		final int z = maxPoint.z - minPoint.z;
		return x * z;
	}
	
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
	
	public boolean contains(final Zown z) {
		boolean contained = false;
		if (centerPoint == null || contains(z.getMinPoint()) && contains(z.getMaxPoint())) {
			contained = true;
		}
		return contained;
	}
	
	public boolean intersects(final Zown z) {
		boolean intersects = false;
		if (centerPoint != null && contains(z.getMinPoint()) ^ contains(z.getMaxPoint())) {
			intersects = true;
		}
		return intersects;
	}
}
