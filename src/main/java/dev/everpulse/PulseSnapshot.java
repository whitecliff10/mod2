package dev.everpulse;

public record PulseSnapshot(boolean active, WorldPulse pulse, long ticksRemaining, int nightsUntil) {}
