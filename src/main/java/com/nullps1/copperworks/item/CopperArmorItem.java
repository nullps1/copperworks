package com.nullps1.copperworks.item;

import com.nullps1.copperworks.Copperworks;
import com.nullps1.copperworks.data.CopperEquipment;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

public final class CopperArmorItem extends ArmorItem {
    public CopperArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot,
                                           ArmorMaterial.Layer layer, boolean innerModel) {
        String suffix = switch (CopperEquipment.stage(stack)) {
            case 1 -> "_exposed";
            case 2 -> "_weathered";
            case 3 -> "_oxidized";
            default -> "";
        };
        return Copperworks.id("textures/models/armor/copper_layer_" + (innerModel ? 2 : 1) + suffix + ".png");
    }
}
