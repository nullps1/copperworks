package com.nullps1.copperworks.item;

import com.nullps1.copperworks.Copperworks;
import com.nullps1.copperworks.data.CopperEquipment;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class CopperArmorItem extends ArmorItem {
    public CopperArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
        return CopperEquipment.scaleDurabilityDamage(stack, amount, entity);
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
