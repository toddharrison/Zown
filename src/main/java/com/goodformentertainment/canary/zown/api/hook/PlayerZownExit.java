package com.goodformentertainment.canary.zown.api.hook;

import com.goodformentertainment.canary.zown.api.IZown;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.hook.Hook;

public class PlayerZownExit extends Hook {
    private final Player player;
    private final IZown zown;

    public PlayerZownExit(final Player player, final IZown zown) {
        this.player = player;
        this.zown = zown;
    }

    public Player getPlayer() {
        return player;
    }

    public IZown getZown() {
        return zown;
    }
}
