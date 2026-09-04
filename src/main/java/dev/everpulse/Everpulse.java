package dev.everpulse;

import dev.everpulse.item.PulseCompassItem;
import dev.everpulse.item.PulseSigilItem;
import dev.everpulse.item.WorldheartItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(Everpulse.MOD_ID)
public final class Everpulse {
    public static final String MOD_ID = "everpulse";

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final DeferredBlock<Block> PULSE_ALTAR = block("pulse_altar", MapColor.COLOR_PURPLE, 4, 9, 11);
    public static final DeferredBlock<Block> VERDANT_STONE = block("verdant_stone", MapColor.COLOR_GREEN, 2, 6, 5);
    public static final DeferredBlock<Block> EMBER_STONE = block("ember_stone", MapColor.COLOR_ORANGE, 2, 6, 8);
    public static final DeferredBlock<Block> ZEPHYR_STONE = block("zephyr_stone", MapColor.COLOR_LIGHT_BLUE, 2, 6, 7);
    public static final DeferredBlock<Block> UMBRAL_STONE = block("umbral_stone", MapColor.COLOR_PURPLE, 3, 8, 4);

    public static final DeferredItem<BlockItem> PULSE_ALTAR_ITEM = ITEMS.registerSimpleBlockItem(PULSE_ALTAR);
    public static final DeferredItem<BlockItem> VERDANT_STONE_ITEM = ITEMS.registerSimpleBlockItem(VERDANT_STONE);
    public static final DeferredItem<BlockItem> EMBER_STONE_ITEM = ITEMS.registerSimpleBlockItem(EMBER_STONE);
    public static final DeferredItem<BlockItem> ZEPHYR_STONE_ITEM = ITEMS.registerSimpleBlockItem(ZEPHYR_STONE);
    public static final DeferredItem<BlockItem> UMBRAL_STONE_ITEM = ITEMS.registerSimpleBlockItem(UMBRAL_STONE);

    public static final DeferredItem<PulseSigilItem> VERDANT_SIGIL = sigil("verdant_sigil", WorldPulse.VERDANT);
    public static final DeferredItem<PulseSigilItem> EMBER_SIGIL = sigil("ember_sigil", WorldPulse.EMBER);
    public static final DeferredItem<PulseSigilItem> ZEPHYR_SIGIL = sigil("zephyr_sigil", WorldPulse.ZEPHYR);
    public static final DeferredItem<PulseSigilItem> UMBRAL_SIGIL = sigil("umbral_sigil", WorldPulse.UMBRAL);
    public static final DeferredItem<Item> CHAMPION_CORE = ITEMS.registerSimpleItem(
            "champion_core", new Item.Properties().rarity(Rarity.RARE).fireResistant());
    public static final DeferredItem<PulseCompassItem> PULSE_COMPASS = ITEMS.registerItem(
            "pulse_compass", PulseCompassItem::new, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final DeferredItem<WorldheartItem> WORLDHEART = ITEMS.registerItem(
            "worldheart", WorldheartItem::new, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB =
            TABS.register("everpulse", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.everpulse"))
                    .icon(() -> PULSE_COMPASS.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(PULSE_COMPASS.get());
                        output.accept(WORLDHEART.get());
                        output.accept(CHAMPION_CORE.get());
                        output.accept(VERDANT_SIGIL.get());
                        output.accept(EMBER_SIGIL.get());
                        output.accept(ZEPHYR_SIGIL.get());
                        output.accept(UMBRAL_SIGIL.get());
                        output.accept(PULSE_ALTAR_ITEM.get());
                        output.accept(VERDANT_STONE_ITEM.get());
                        output.accept(EMBER_STONE_ITEM.get());
                        output.accept(ZEPHYR_STONE_ITEM.get());
                        output.accept(UMBRAL_STONE_ITEM.get());
                    }).build());

    public Everpulse(IEventBus modBus, ModContainer container) {
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        TABS.register(modBus);
        container.registerConfig(ModConfig.Type.COMMON, EverpulseConfig.SPEC);
    }

    private static DeferredBlock<Block> block(String name, MapColor color, float strength,
                                               float resistance, int light) {
        return BLOCKS.registerSimpleBlock(name, BlockBehaviour.Properties.of()
                .mapColor(color)
                .strength(strength, resistance)
                .sound(SoundType.DEEPSLATE_TILES)
                .lightLevel(state -> light)
                .requiresCorrectToolForDrops());
    }

    private static DeferredItem<PulseSigilItem> sigil(String name, WorldPulse pulse) {
        return ITEMS.registerItem(name, properties -> new PulseSigilItem(properties, pulse),
                new Item.Properties().rarity(Rarity.UNCOMMON).fireResistant());
    }

    public static Item sigilFor(WorldPulse pulse) {
        return switch (pulse) {
            case VERDANT -> VERDANT_SIGIL.get();
            case EMBER -> EMBER_SIGIL.get();
            case ZEPHYR -> ZEPHYR_SIGIL.get();
            case UMBRAL -> UMBRAL_SIGIL.get();
        };
    }
}
