package com.nullps1.copperworks.data;

import com.nullps1.copperworks.Copperworks;
import javax.annotation.Nullable;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

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

    public static double durabilityWearMultiplier(ItemStack stack) {
        return switch (stage(stack)) {
            case 1 -> 1.10D;
            case 2 -> 1.25D;
            case 3 -> 1.50D;
            default -> 1.00D;
        };
    }

    public static boolean hasToolPerformance(ItemStack stack) {
        return isOxidizable(stack) && (stack.getItem() instanceof DiggerItem || stack.getItem() instanceof SwordItem);
    }

    public static double miningSpeedMultiplier(ItemStack stack) {
        return switch (stage(stack)) {
            case 1 -> 0.95D;
            case 2 -> 0.90D;
            case 3 -> 0.80D;
            default -> 1.00D;
        };
    }

    public static double attackDamageMultiplier(ItemStack stack) {
        return switch (stage(stack)) {
            case 1 -> 0.97D;
            case 2 -> 0.93D;
            case 3 -> 0.85D;
            default -> 1.00D;
        };
    }

    public static MutableComponent miningSpeedDescription(ItemStack stack) {
        long percent = Math.round((1.0D - miningSpeedMultiplier(stack)) * 100);
        return percent == 0
            ? Component.translatable("tooltip.copperworks.mining.normal")
            : Component.translatable("tooltip.copperworks.mining.reduced", percent);
    }

    public static MutableComponent attackDamageDescription(ItemStack stack) {
        long percent = Math.round((1.0D - attackDamageMultiplier(stack)) * 100);
        return percent == 0
            ? Component.translatable("tooltip.copperworks.attack.normal")
            : Component.translatable("tooltip.copperworks.attack.reduced", percent);
    }

    public static MutableComponent durabilityWearDescription(ItemStack stack) {
        long percent = Math.round((durabilityWearMultiplier(stack) - 1.0D) * 100);
        return percent == 0
            ? Component.translatable("tooltip.copperworks.durability.normal")
            : Component.translatable("tooltip.copperworks.durability.increased", percent);
    }

    // NeoForge calls damageItem before Unbreaking and before the creative-mode check.
    public static int scaleDurabilityDamage(ItemStack stack, int amount, @Nullable LivingEntity entity) {
        if (amount <= 0 || !isOxidizable(stack)
            || (entity != null && (entity.level().isClientSide() || entity.hasInfiniteMaterials()))) {
            return amount;
        }
        double multiplier = durabilityWearMultiplier(stack);
        if (multiplier == 1.0D) {
            return amount;
        }
        double exactDamage = Math.min(Integer.MAX_VALUE, amount * multiplier);
        int damage = (int) exactDamage;
        double remainder = exactDamage - damage;
        if (remainder > 0) {
            // Entity-less hurtAndBreak calls still run on the server; use its existing RNG.
            var server = entity == null ? ServerLifecycleHooks.getCurrentServer() : null;
            if (entity == null && server == null) {
                return amount;
            }
            var random = entity != null ? entity.getRandom() : server.overworld().getRandom();
            if (random.nextDouble() < remainder) {
                damage++;
            }
        }
        return damage;
    }

    public static void setStage(ItemStack stack, int stage) {
        stack.set(ModDataComponents.COPPER_OXIDATION, Math.min(MAX_STAGE, Math.max(0, stage)));
    }

    public static void setWaxed(ItemStack stack, boolean waxed) {
        stack.set(ModDataComponents.COPPER_WAXED, waxed);
    }
}
