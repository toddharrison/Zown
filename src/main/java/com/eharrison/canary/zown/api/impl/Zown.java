package com.eharrison.canary.zown.api.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.canarymod.api.entity.living.humanoid.Player;

import com.eharrison.canary.zown.api.IConfiguration;
import com.eharrison.canary.zown.api.IZown;
import com.eharrison.canary.zown.api.Point;

public class Zown implements IZown {
	private String name;
	private Template template;
	private Configuration configuration;
	private final Set<String> owners;
	private final Set<String> members;
	private final Set<String> entryExclusions;
	
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
	public Template getTemplate() {
		return template;
	}
	
	public void setTemplate(final Template template) {
		if (template == null && configuration == null) {
			configuration = this.template.getConfiguration().clone();
		}
		this.template = template;
	}
	
	public void loadTemplate(final Template template) {
		if (template != null) {
			this.template = template;
			configuration = null;
		}
	}
	
	@Override
	public IConfiguration getConfiguration() {
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
	public Point getMinPoint() {
		return minPoint;
	}
	
	@Override
	public Point getMaxPoint() {
		return maxPoint;
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
	public boolean intersects(final IZown z) {
		boolean intersects = false;
		if (centerPoint != null && contains(z.getMinPoint()) ^ contains(z.getMaxPoint())) {
			intersects = true;
		}
		return intersects;
	}
}
