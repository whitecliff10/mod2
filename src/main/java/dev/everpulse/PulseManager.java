package dev.everpulse;

import net.minecraft.server.level.ServerLevel;

public final class PulseManager {
    private static final long DAY_TICKS = 24_000L;
    private static final long PULSE_START = 13_000L;
    private static final long PULSE_END = 23_000L;

    private PulseManager() {}

    public static PulseSnapshot snapshot(ServerLevel level) {
        long dayTime = level.getDayTime();
        long day = Math.floorDiv(dayTime, DAY_TICKS);
        long timeOfDay = Math.floorMod(dayTime, DAY_TICKS);
        int interval = EverpulseConfig.PULSE_INTERVAL_DAYS.getAsInt();
        boolean pulseDay = Math.floorMod(day + 1, interval) == 0;
        boolean active = pulseDay && timeOfDay >= PULSE_START && timeOfDay < PULSE_END;
        WorldPulse pulse = pulseForDay(day, interval);

        if (active) {
            return new PulseSnapshot(true, pulse, PULSE_END - timeOfDay, 0);
        }

        int nightsUntil = 0;
        long probe = day;
        if (timeOfDay >= PULSE_END || (pulseDay && timeOfDay >= PULSE_START)) probe++;
        while (Math.floorMod(probe + 1, interval) != 0) {
            probe++;
            nightsUntil++;
        }
        return new PulseSnapshot(false, pulseForDay(probe, interval), 0, nightsUntil);
    }

    public static long currentDay(ServerLevel level) {
        return Math.floorDiv(level.getDayTime(), DAY_TICKS);
    }

    private static WorldPulse pulseForDay(long day, int interval) {
        long pulseIndex = Math.floorDiv(day + 1, interval);
        WorldPulse[] values = WorldPulse.values();
        return values[Math.floorMod((int) pulseIndex, values.length)];
    }
}
