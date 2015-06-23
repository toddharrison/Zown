package com.goodformentertainment.canary.zown.api.impl;

import com.goodformentertainment.canary.zown.api.ITemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Template implements ITemplate {
    private String name;
    private final Configuration configuration;
    private final Set<Zown> zowns;

    protected Template(final String name) {
        this(name, new Configuration());
    }

    protected Template(final String name, final Configuration configuration) {
        this.name = name;
        this.configuration = configuration;
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
    public String getDisplay() {
        final StringBuilder sb = new StringBuilder();

        sb.append("\n");
        sb.append(name);
        sb.append("\n");

        final Configuration config = getConfiguration();
        sb.append(config.getDisplay());

        return sb.toString();
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
