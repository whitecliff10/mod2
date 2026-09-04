package dev.everpulse.item;

import dev.everpulse.Everpulse;
import dev.everpulse.WorldPulse;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;

import java.util.List;

public final class PulseSigilItem extends Item {
    private final WorldPulse pulse;

    public PulseSigilItem(Properties properties, WorldPulse pulse) {
        super(properties);
        this.pulse = pulse;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null || !context.getLevel().getBlockState(context.getClickedPos()).is(Everpulse.PULSE_ALTAR)) {
            return InteractionResult.PASS;
        }

        if (context.getLevel() instanceof ServerLevel level) {
            grantBlessing(player);
            level.playSound(null, context.getClickedPos(), SoundEvents.BEACON_POWER_SELECT,
                    SoundSource.BLOCKS, 1.0F, 0.75F + pulse.ordinal() * 0.12F);
            player.displayClientMessage(Component.translatable(
                    "message.everpulse.altar.blessing", pulse.displayName()).withStyle(ChatFormatting.GOLD), false);
            if (!player.getAbilities().instabuild) context.getItemInHand().shrink(1);
        }
        return InteractionResult.sidedSuccess(context.getLevel().isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.everpulse.sigil", pulse.displayName())
                .withStyle(ChatFormatting.GRAY));
    }

    private void grantBlessing(Player player) {
        switch (pulse) {
            case VERDANT -> {
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 6000, 0));
                player.addEffect(new MobEffectInstance(MobEffects.LUCK, 6000, 1));
            }
            case EMBER -> {
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 6000, 0));
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 6000, 0));
            }
            case ZEPHYR -> {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 6000, 1));
                player.addEffect(new MobEffectInstance(MobEffects.JUMP, 6000, 1));
                player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 6000, 0));
            }
            case UMBRAL -> {
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 6000, 0));
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 0));
            }
        }
    }
}
