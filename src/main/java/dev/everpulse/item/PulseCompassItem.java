package dev.everpulse.item;

import dev.everpulse.PulseManager;
import dev.everpulse.PulseSnapshot;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public final class PulseCompassItem extends Item {
    public PulseCompassItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level instanceof ServerLevel serverLevel) {
            PulseSnapshot snapshot = PulseManager.snapshot(serverLevel);
            if (snapshot.active()) {
                long seconds = Math.max(1, snapshot.ticksRemaining() / 20L);
                player.displayClientMessage(Component.translatable(
                        "message.everpulse.compass.active", snapshot.pulse().displayName(), seconds)
                        .withStyle(ChatFormatting.LIGHT_PURPLE), false);
            } else {
                player.displayClientMessage(Component.translatable(
                        "message.everpulse.compass.dormant", snapshot.pulse().displayName(), snapshot.nightsUntil())
                        .withStyle(ChatFormatting.GRAY), false);
            }
            player.getCooldowns().addCooldown(this, 20);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.everpulse.compass.1").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.everpulse.compass.2").withStyle(ChatFormatting.DARK_GRAY));
    }
}
