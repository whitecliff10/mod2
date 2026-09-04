package dev.everpulse;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = Everpulse.MOD_ID)
public final class EverpulseEvents {
    private static final String AFFECTED_DAY = "everpulse_affected_day";
    private static final String CHAMPION = "everpulse_champion";
    private static boolean wasActive;

    private EverpulseEvents() {}

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        ServerLevel overworld = server.getLevel(Level.OVERWORLD);
        if (overworld == null || overworld.getGameTime() % 20L != 0L) return;

        PulseSnapshot snapshot = PulseManager.snapshot(overworld);
        if (EverpulseConfig.ANNOUNCE_PULSES.getAsBoolean() && snapshot.active() != wasActive) {
            Component message = snapshot.active()
                    ? Component.translatable("message.everpulse.pulse.begin", snapshot.pulse().displayName())
                            .withStyle(ChatFormatting.LIGHT_PURPLE)
                    : Component.translatable("message.everpulse.pulse.end").withStyle(ChatFormatting.GRAY);
            server.getPlayerList().broadcastSystemMessage(message, false);
        }
        wasActive = snapshot.active();

        if (!snapshot.active() || overworld.getGameTime() % 80L != 0L) return;
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            applyPlayerPulse(player, snapshot.pulse());
        }
    }

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level) || !(event.getEntity() instanceof Monster monster)) {
            return;
        }

        PulseSnapshot snapshot = PulseManager.snapshot(level.getServer().overworld());
        if (!snapshot.active()) return;
        long day = PulseManager.currentDay(level);
        if (monster.getPersistentData().getLong(AFFECTED_DAY) == day + 1L) return;
        monster.getPersistentData().putLong(AFFECTED_DAY, day + 1L);

        switch (snapshot.pulse()) {
            case VERDANT -> monster.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 12_000, 0));
            case EMBER -> {
                monster.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 12_000, 0));
                monster.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 12_000, 0));
            }
            case ZEPHYR -> {
                monster.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 12_000, 0));
                monster.addEffect(new MobEffectInstance(MobEffects.JUMP, 12_000, 1));
            }
            case UMBRAL -> tryMakeChampion(monster);
        }
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (!(event.getEntity().level() instanceof ServerLevel level)) return;
        PulseSnapshot snapshot = PulseManager.snapshot(level.getServer().overworld());

        if (event.getEntity().getPersistentData().getBoolean(CHAMPION)) {
            event.getDrops().add(drop(event, new ItemStack(Everpulse.CHAMPION_CORE.get())));
        }

        if (!snapshot.active() || !(event.getEntity() instanceof Monster)) return;
        if (event.getSource().getEntity() instanceof ServerPlayer
                && level.random.nextDouble() < EverpulseConfig.SIGIL_DROP_CHANCE.getAsDouble()) {
            event.getDrops().add(drop(event, new ItemStack(Everpulse.sigilFor(snapshot.pulse()))));
        }
    }

    private static ItemEntity drop(LivingDropsEvent event, ItemStack stack) {
        return new ItemEntity(event.getEntity().level(), event.getEntity().getX(),
                event.getEntity().getY(), event.getEntity().getZ(), stack);
    }

    private static void applyPlayerPulse(ServerPlayer player, WorldPulse pulse) {
        switch (pulse) {
            case VERDANT -> {
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 120, 0, true, false, true));
                player.addEffect(new MobEffectInstance(MobEffects.LUCK, 120, 0, true, false, true));
            }
            case EMBER -> {
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 120, 0, true, false, true));
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 120, 0, true, false, true));
            }
            case ZEPHYR -> {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 120, 0, true, false, true));
                player.addEffect(new MobEffectInstance(MobEffects.JUMP, 120, 1, true, false, true));
                player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 120, 0, true, false, true));
            }
            case UMBRAL -> player.addEffect(
                    new MobEffectInstance(MobEffects.NIGHT_VISION, 260, 0, true, false, true));
        }
    }

    private static void tryMakeChampion(Monster monster) {
        if (monster.getMaxHealth() > 80.0F
                || monster.getRandom().nextDouble() >= EverpulseConfig.CHAMPION_CHANCE.getAsDouble()) {
            return;
        }
        monster.getPersistentData().putBoolean(CHAMPION, true);
        monster.setCustomName(Component.translatable("entity.everpulse.pulse_touched", monster.getName())
                .withStyle(ChatFormatting.DARK_PURPLE));
        monster.setCustomNameVisible(true);

        AttributeInstance health = monster.getAttribute(Attributes.MAX_HEALTH);
        if (health != null) health.setBaseValue(health.getBaseValue() * 1.8D);
        monster.setHealth(monster.getMaxHealth());
        monster.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 24_000, 1));
        monster.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 24_000, 0));
        monster.addEffect(new MobEffectInstance(MobEffects.GLOWING, 24_000, 0));
    }
}
