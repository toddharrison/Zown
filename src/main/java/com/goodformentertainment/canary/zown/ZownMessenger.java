package com.goodformentertainment.canary.zown;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.ChatFormat;
import net.canarymod.chat.MessageReceiver;

public final class ZownMessenger {
    public static void sendMessage(final MessageReceiver caller, final Object message) {
        sendMessage(caller, message.toString());
    }

    public static void sendMessage(final MessageReceiver caller, final String message) {
        if (caller instanceof Player) {
            caller.asPlayer().message(message);
        } else {
            ZownPlugin.LOG.info(ChatFormat.removeFormatting(message));
        }
    }
}
