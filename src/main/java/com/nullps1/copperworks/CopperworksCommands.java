package com.nullps1.copperworks;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.nullps1.copperworks.data.CopperEquipment;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public final class CopperworksCommands {
    private CopperworksCommands() {
    }

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("copperworks")
            .requires(source -> source.hasPermission(2))
            .then(Commands.literal("oxidation")
                .then(Commands.literal("set")
                    .then(Commands.argument("stage", IntegerArgumentType.integer(0, CopperEquipment.MAX_STAGE))
                        .executes(context -> setStage(context, IntegerArgumentType.getInteger(context, "stage")))))
                .then(Commands.literal("wax")
                    .executes(context -> setWax(context, true)))
                .then(Commands.literal("unwax")
                    .executes(context -> setWax(context, false)))
            )
        );
    }

    private static int setStage(CommandContext<CommandSourceStack> context, int stage) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack stack = player.getMainHandItem();
        if (!CopperEquipment.isOxidizable(stack)) {
            context.getSource().sendFailure(Component.translatable("commands.copperworks.not_equipment"));
            return 0;
        }
        CopperEquipment.setStage(stack, stage);
        context.getSource().sendSuccess(() -> Component.translatable("commands.copperworks.oxidation_set", stage), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int setWax(CommandContext<CommandSourceStack> context, boolean waxed) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack stack = player.getMainHandItem();
        if (!CopperEquipment.isOxidizable(stack)) {
            context.getSource().sendFailure(Component.translatable("commands.copperworks.not_equipment"));
            return 0;
        }
        CopperEquipment.setWaxed(stack, waxed);
        context.getSource().sendSuccess(() -> Component.translatable(
            waxed ? "commands.copperworks.waxed" : "commands.copperworks.unwaxed"
        ), true);
        return Command.SINGLE_SUCCESS;
    }
}
