package com.nullps1.copperworks.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;

public final class CopperToolTier {
    public static final Tier COPPER = new SimpleTier(
        BlockTags.INCORRECT_FOR_IRON_TOOL,
        180,
        5.0F,
        1.5F,
        12,
        () -> Ingredient.of(Items.COPPER_INGOT)
    );

    private CopperToolTier() {
    }
}
