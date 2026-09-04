package dev.everpulse;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public enum WorldPulse {
    VERDANT("verdant", ChatFormatting.GREEN),
    EMBER("ember", ChatFormatting.GOLD),
    ZEPHYR("zephyr", ChatFormatting.AQUA),
    UMBRAL("umbral", ChatFormatting.DARK_PURPLE);

    private final String id;
    private final ChatFormatting color;

    WorldPulse(String id, ChatFormatting color) {
        this.id = id;
        this.color = color;
    }

    public String id() {
        return id;
    }

    public Component displayName() {
        return Component.translatable("pulse.everpulse." + id).withStyle(color);
    }
}
