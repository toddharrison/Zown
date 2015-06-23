package com.goodformentertainment.canary.zown.command;

import com.goodformentertainment.canary.zown.api.ITemplate;
import com.goodformentertainment.canary.zown.api.ITemplateManager;
import com.goodformentertainment.canary.zown.api.Point;
import net.canarymod.api.world.World;
import net.canarymod.api.world.WorldManager;

public class ParameterTokenizer {
    private final String[] parameters;
    private int index;

    public ParameterTokenizer(final String[] parameters) {
        this.parameters = parameters;
        index = 1;
    }

    public boolean hasToken() {
        return index < parameters.length;
    }

    public String readString() {
        return parameters[index++];
    }

    public int readInteger() {
        return Integer.parseInt(parameters[index++]);
    }

    public double readDouble() {
        return Double.parseDouble(parameters[index++]);
    }

    public Point readPoint() {
        try {
            final int x = readInteger();
            final int y = readInteger();
            final int z = readInteger();
            return new Point(x, y, z);
        } catch (final Exception e) {
            return null;
        }
    }

    public World readWorld(final WorldManager worldManager) {
        try {
            return worldManager.getWorld(readString(), false);
        } catch (final Exception e) {
            return null;
        }
    }

    public ITemplate readTemplate(final ITemplateManager templateManager) {
        try {
            return templateManager.getTemplate(readString());
        } catch (final Exception e) {
            return null;
        }
    }
}
