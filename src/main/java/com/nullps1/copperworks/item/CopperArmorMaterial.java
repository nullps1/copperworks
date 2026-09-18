package com.nullps1.copperworks.item;

import com.nullps1.copperworks.Copperworks;
import java.util.EnumMap;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class CopperArmorMaterial {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(
        Registries.ARMOR_MATERIAL,
        Copperworks.MOD_ID
    );

    public static final Holder<ArmorMaterial> COPPER = ARMOR_MATERIALS.register(
        "copper",
        () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), defense -> {
                defense.put(ArmorItem.Type.HELMET, 2);
                defense.put(ArmorItem.Type.CHESTPLATE, 5);
                defense.put(ArmorItem.Type.LEGGINGS, 4);
                defense.put(ArmorItem.Type.BOOTS, 1);
                defense.put(ArmorItem.Type.BODY, 0);
            }),
            12,
            SoundEvents.ARMOR_EQUIP_IRON,
            () -> Ingredient.of(Items.COPPER_INGOT),
            List.of(new ArmorMaterial.Layer(Copperworks.id("copper"))),
            0.0F,
            0.0F
        )
    );

    private CopperArmorMaterial() {
    }
}
