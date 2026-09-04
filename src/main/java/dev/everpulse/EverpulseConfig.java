package dev.everpulse;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class EverpulseConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue PULSE_INTERVAL_DAYS = BUILDER
            .comment("A pulse occurs every N Minecraft days.")
            .defineInRange("pulseIntervalDays", 3, 1, 30);
    public static final ModConfigSpec.DoubleValue SIGIL_DROP_CHANCE = BUILDER
            .comment("Chance for a hostile mob killed during a pulse to drop its sigil.")
            .defineInRange("sigilDropChance", 0.16D, 0.0D, 1.0D);
    public static final ModConfigSpec.DoubleValue CHAMPION_CHANCE = BUILDER
            .comment("Chance for a hostile mob spawned during an Umbral Pulse to become pulse-touched.")
            .defineInRange("championChance", 0.045D, 0.0D, 1.0D);
    public static final ModConfigSpec.BooleanValue ANNOUNCE_PULSES = BUILDER
            .comment("Broadcast the beginning and ending of each pulse.")
            .define("announcePulses", true);

    public static final ModConfigSpec SPEC = BUILDER.build();

    private EverpulseConfig() {}
}
