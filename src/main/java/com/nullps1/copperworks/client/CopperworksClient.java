package com.nullps1.copperworks.client;

import com.nullps1.copperworks.Copperworks;
import com.nullps1.copperworks.data.CopperEquipment;
import com.nullps1.copperworks.item.ModItems;
import java.util.List;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = Copperworks.MOD_ID, dist = Dist.CLIENT)
public final class CopperworksClient {
    public CopperworksClient(IEventBus modEventBus) {
        modEventBus.addListener(CopperworksClient::onClientSetup);
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            var propertyId = Copperworks.id("oxidation");
            // Keep stages 2 and 3: ClampedItemPropertyFunction would clamp them to 1.
            ItemPropertyFunction oxidation = (stack, level, entity, seed) -> CopperEquipment.stage(stack);
            for (var item : List.of(
                ModItems.COPPER_SWORD, ModItems.COPPER_PICKAXE, ModItems.COPPER_AXE,
                ModItems.COPPER_SHOVEL, ModItems.COPPER_HOE, ModItems.COPPER_HELMET,
                ModItems.COPPER_CHESTPLATE, ModItems.COPPER_LEGGINGS, ModItems.COPPER_BOOTS
            )) {
                ItemProperties.register(item.get(), propertyId, oxidation);
            }
        });
    }
}
