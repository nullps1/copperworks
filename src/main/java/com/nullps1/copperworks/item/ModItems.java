package com.nullps1.copperworks.item;

import com.nullps1.copperworks.Copperworks;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    private static final int ARMOR_DURABILITY_MULTIPLIER = 12;

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Copperworks.MOD_ID);

    public static final DeferredItem<SwordItem> COPPER_SWORD = ITEMS.registerItem(
        "copper_sword",
        properties -> new SwordItem(CopperToolTier.COPPER, properties.attributes(
            SwordItem.createAttributes(CopperToolTier.COPPER, 3, -2.4F)
        ))
    );

    public static final DeferredItem<PickaxeItem> COPPER_PICKAXE = ITEMS.registerItem(
        "copper_pickaxe",
        properties -> new PickaxeItem(CopperToolTier.COPPER, properties.attributes(
            PickaxeItem.createAttributes(CopperToolTier.COPPER, 1, -2.8F)
        ))
    );

    public static final DeferredItem<AxeItem> COPPER_AXE = ITEMS.registerItem(
        "copper_axe",
        properties -> new AxeItem(CopperToolTier.COPPER, properties.attributes(
            AxeItem.createAttributes(CopperToolTier.COPPER, 5, -3.1F)
        ))
    );

    public static final DeferredItem<ShovelItem> COPPER_SHOVEL = ITEMS.registerItem(
        "copper_shovel",
        properties -> new ShovelItem(CopperToolTier.COPPER, properties.attributes(
            ShovelItem.createAttributes(CopperToolTier.COPPER, 1.5F, -3.0F)
        ))
    );

    public static final DeferredItem<HoeItem> COPPER_HOE = ITEMS.registerItem(
        "copper_hoe",
        properties -> new HoeItem(CopperToolTier.COPPER, properties.attributes(
            HoeItem.createAttributes(CopperToolTier.COPPER, -2, -1.0F)
        ))
    );

    public static final DeferredItem<ArmorItem> COPPER_HELMET = ITEMS.registerItem(
        "copper_helmet",
        properties -> new CopperArmorItem(
            CopperArmorMaterial.COPPER,
            ArmorItem.Type.HELMET,
            properties.durability(ArmorItem.Type.HELMET.getDurability(ARMOR_DURABILITY_MULTIPLIER))
        )
    );

    public static final DeferredItem<ArmorItem> COPPER_CHESTPLATE = ITEMS.registerItem(
        "copper_chestplate",
        properties -> new CopperArmorItem(
            CopperArmorMaterial.COPPER,
            ArmorItem.Type.CHESTPLATE,
            properties.durability(ArmorItem.Type.CHESTPLATE.getDurability(ARMOR_DURABILITY_MULTIPLIER))
        )
    );

    public static final DeferredItem<ArmorItem> COPPER_LEGGINGS = ITEMS.registerItem(
        "copper_leggings",
        properties -> new CopperArmorItem(
            CopperArmorMaterial.COPPER,
            ArmorItem.Type.LEGGINGS,
            properties.durability(ArmorItem.Type.LEGGINGS.getDurability(ARMOR_DURABILITY_MULTIPLIER))
        )
    );

    public static final DeferredItem<ArmorItem> COPPER_BOOTS = ITEMS.registerItem(
        "copper_boots",
        properties -> new CopperArmorItem(
            CopperArmorMaterial.COPPER,
            ArmorItem.Type.BOOTS,
            properties.durability(ArmorItem.Type.BOOTS.getDurability(ARMOR_DURABILITY_MULTIPLIER))
        )
    );

    private ModItems() {
    }

    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabs.TOOLS_AND_UTILITIES)) {
            event.accept(COPPER_SWORD);
            event.accept(COPPER_PICKAXE);
            event.accept(COPPER_AXE);
            event.accept(COPPER_SHOVEL);
            event.accept(COPPER_HOE);
        } else if (event.getTabKey().equals(CreativeModeTabs.COMBAT)) {
            event.accept(COPPER_SWORD);
            event.accept(COPPER_AXE);
            event.accept(COPPER_HELMET);
            event.accept(COPPER_CHESTPLATE);
            event.accept(COPPER_LEGGINGS);
            event.accept(COPPER_BOOTS);
        }
    }
}
