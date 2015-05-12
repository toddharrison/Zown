package com.goodformentertainment.canary.zown.api.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.goodformentertainment.canary.zown.api.ITemplate;

public class Template implements ITemplate {
	private String name;
	private final Configuration configuration;
	private final Set<Zown> zowns;
	
	protected Template(final String name) {
		super();
		this.name = name;
		configuration = new Configuration();
		zowns = new HashSet<Zown>();
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	protected void setName(final String name) {
		this.name = name;
	}
	
	@Override
	public Configuration getConfiguration() {
		return configuration;
	}
	
	@Override
	public Collection<Zown> getZowns() {
		return Collections.unmodifiableCollection(zowns);
	}
	
	protected boolean addZown(final Zown zown) {
		final boolean added = zowns.add(zown);
		if (added) {
			zown.setTemplate(this);
		}
		return added;
	}
	
	protected boolean removeZown(final Zown zown) {
		final boolean removed = zowns.remove(zown);
		if (removed) {
			zown.setTemplate(null);
		}
		return removed;
	}
}
