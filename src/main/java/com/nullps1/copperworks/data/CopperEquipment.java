package com.nullps1.copperworks.data;

import com.nullps1.copperworks.Copperworks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class CopperEquipment {
    private static final TagKey<Item> COPPER_EQUIPMENT = TagKey.create(
        Registries.ITEM,
        Copperworks.id("copper_equipment")
    );
    public static final int MAX_STAGE = 3;
    public static final int CHECK_INTERVAL = 600;
    public static final double BASE_CHANCE = 0.0025D;
    public static final double WET_MULTIPLIER = 3.0D;

    private CopperEquipment() {
    }

    public static boolean isOxidizable(ItemStack stack) {
        return !stack.isEmpty() && stack.is(COPPER_EQUIPMENT);
    }

    public static int stage(ItemStack stack) {
        return Math.min(MAX_STAGE, Math.max(0, stack.getOrDefault(ModDataComponents.COPPER_OXIDATION, 0)));
    }

    public static boolean isWaxed(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.COPPER_WAXED, false);
    }

    public static MutableComponent stageName(ItemStack stack) {
        return Component.translatable(switch (stage(stack)) {
            case 1 -> "tooltip.copperworks.exposed";
            case 2 -> "tooltip.copperworks.weathered";
            case 3 -> "tooltip.copperworks.oxidized";
            default -> "tooltip.copperworks.fresh";
        });
    }

    public static void setStage(ItemStack stack, int stage) {
        stack.set(ModDataComponents.COPPER_OXIDATION, Math.min(MAX_STAGE, Math.max(0, stage)));
    }

    public static void setWaxed(ItemStack stack, boolean waxed) {
        stack.set(ModDataComponents.COPPER_WAXED, waxed);
    }
}
