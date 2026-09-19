package com.nullps1.copperworks;

import com.nullps1.copperworks.data.CopperEquipment;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public final class CopperEquipmentEvents {
    private static final ResourceLocation OXIDATION_ATTACK_DAMAGE = Copperworks.id("oxidation_attack_damage");

    private CopperEquipmentEvents() {
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        ItemStack stack = event.getEntity().getMainHandItem();
        if (!CopperEquipment.hasToolPerformance(stack) || event.getEntity().hasInfiniteMaterials()) {
            return;
        }
        // Swords only have meaningful mining performance on their native efficient blocks.
        if (stack.getItem() instanceof SwordItem && stack.getDestroySpeed(event.getState()) <= 1.0F) {
            return;
        }
        event.setNewSpeed((float) (event.getNewSpeed() * CopperEquipment.miningSpeedMultiplier(stack)));
    }

    @SubscribeEvent
    public static void onItemAttributes(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        if (!CopperEquipment.hasToolPerformance(stack)) {
            return;
        }
        double multiplier = CopperEquipment.attackDamageMultiplier(stack);
        if (multiplier != 1.0D) {
            // A stable ID lets vanilla remove the old stack's modifier when equipment/components change.
            event.replaceModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(
                OXIDATION_ATTACK_DAMAGE, multiplier - 1.0D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            ), EquipmentSlotGroup.MAINHAND);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide() || player.tickCount % CopperEquipment.CHECK_INTERVAL != 0) {
            return;
        }

        tryOxidize(player, player.getMainHandItem());
        tryOxidize(player, player.getOffhandItem());
        for (ItemStack armor : player.getArmorSlots()) {
            tryOxidize(player, armor);
        }
    }

    private static void tryOxidize(Player player, ItemStack stack) {
        if (!CopperEquipment.isOxidizable(stack)
            || CopperEquipment.isWaxed(stack)
            || CopperEquipment.stage(stack) >= CopperEquipment.MAX_STAGE
            || player.getRandom().nextDouble() >= oxidationChance(player)) {
            return;
        }

        CopperEquipment.setStage(stack, CopperEquipment.stage(stack) + 1);
        if (CopperEquipment.stage(stack) == CopperEquipment.MAX_STAGE && player instanceof ServerPlayer serverPlayer) {
            award(serverPlayer, "it_builds_character", "fully_oxidized");
        }
    }

    private static double oxidationChance(Player player) {
        boolean wet = player.isInWater() || player.isSwimming() || player.level().isRainingAt(player.blockPosition());
        return CopperEquipment.BASE_CHANCE * (wet ? CopperEquipment.WET_MULTIPLIER : 1.0D);
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) {
            return;
        }

        ItemStack used = event.getItemStack();
        InteractionHand otherHand = event.getHand() == InteractionHand.MAIN_HAND
            ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack target = player.getItemInHand(otherHand);

        if (used.is(Items.HONEYCOMB) && wax(player, target)) {
            if (!player.getAbilities().instabuild) {
                used.shrink(1);
            }
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        } else if (used.getItem() instanceof AxeItem && scrape(player, target, event.getHand())) {
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }

    private static boolean wax(Player player, ItemStack target) {
        if (!CopperEquipment.isOxidizable(target) || CopperEquipment.isWaxed(target)) {
            return false;
        }
        CopperEquipment.setWaxed(target, true);
        player.level().playSound(null, player.blockPosition(), SoundEvents.HONEYCOMB_WAX_ON, SoundSource.PLAYERS, 1.0F, 1.0F);
        if (player instanceof ServerPlayer serverPlayer) {
            award(serverPlayer, "not_getting_any_older", "wax_equipment");
        }
        return true;
    }

    private static boolean scrape(Player player, ItemStack target, InteractionHand axeHand) {
        if (!CopperEquipment.isOxidizable(target)) {
            return false;
        }
        if (CopperEquipment.isWaxed(target)) {
            CopperEquipment.setWaxed(target, false);
            player.level().playSound(null, player.blockPosition(), SoundEvents.AXE_WAX_OFF, SoundSource.PLAYERS, 1.0F, 1.0F);
        } else if (CopperEquipment.stage(target) > 0) {
            CopperEquipment.setStage(target, CopperEquipment.stage(target) - 1);
            player.level().playSound(null, player.blockPosition(), SoundEvents.AXE_SCRAPE, SoundSource.PLAYERS, 1.0F, 1.0F);
        } else {
            return false;
        }

        if (!player.getAbilities().instabuild) {
            ItemStack axe = player.getItemInHand(axeHand);
            axe.hurtAndBreak(1, player, axeHand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
        }
        return true;
    }

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!CopperEquipment.isOxidizable(stack)) {
            return;
        }
        ChatFormatting color = switch (CopperEquipment.stage(stack)) {
            case 2 -> ChatFormatting.DARK_AQUA;
            case 3 -> ChatFormatting.AQUA;
            default -> ChatFormatting.GOLD;
        };
        event.getToolTip().add(CopperEquipment.stageName(stack).withStyle(color));
        event.getToolTip().add(CopperEquipment.durabilityWearDescription(stack).withStyle(ChatFormatting.GRAY));
        if (CopperEquipment.hasToolPerformance(stack)) {
            event.getToolTip().add(CopperEquipment.miningSpeedDescription(stack).withStyle(ChatFormatting.GRAY));
            event.getToolTip().add(CopperEquipment.attackDamageDescription(stack).withStyle(ChatFormatting.GRAY));
        }
        if (CopperEquipment.isWaxed(stack)) {
            event.getToolTip().add(Component.translatable("tooltip.copperworks.waxed").withStyle(ChatFormatting.GRAY));
        }
    }

    private static void award(ServerPlayer player, String advancementId, String criterion) {
        var holder = player.server.getAdvancements().get(Copperworks.id(advancementId));
        if (holder != null) {
            player.getAdvancements().award(holder, criterion);
        }
    }
}
