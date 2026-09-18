package com.nullps1.copperworks;

import com.nullps1.copperworks.item.ModItems;
import com.nullps1.copperworks.item.CopperArmorMaterial;
import com.nullps1.copperworks.data.ModDataComponents;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Copperworks.MOD_ID)
public class Copperworks {
    public static final String MOD_ID = "copperworks";

    public Copperworks(IEventBus modEventBus) {
        CopperArmorMaterial.ARMOR_MATERIALS.register(modEventBus);
        ModDataComponents.DATA_COMPONENTS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        modEventBus.addListener(ModItems::addCreative);
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.register(CopperEquipmentEvents.class);
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.register(CopperworksCommands.class);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
