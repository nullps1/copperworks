package com.nullps1.copperworks.data;

import com.mojang.serialization.Codec;
import com.nullps1.copperworks.Copperworks;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(
        Registries.DATA_COMPONENT_TYPE,
        Copperworks.MOD_ID
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> COPPER_OXIDATION = DATA_COMPONENTS.registerComponentType(
        "copper_oxidation",
        builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> COPPER_WAXED = DATA_COMPONENTS.registerComponentType(
        "copper_waxed",
        builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
    );

    private ModDataComponents() {
    }
}
