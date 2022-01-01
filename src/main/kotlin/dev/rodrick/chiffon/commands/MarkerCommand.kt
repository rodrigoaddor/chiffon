package dev.rodrick.chiffon.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import dev.rodrick.chiffon.utils.Marker
import net.minecraft.command.argument.BlockPosArgumentType
import net.minecraft.command.argument.BlockPosArgumentType.getBlockPos

import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource

val MarkerCommand: LiteralArgumentBuilder<ServerCommandSource> = literal("marker")
    .then(argument("position", BlockPosArgumentType.blockPos()).executes { ctx ->
        val pos = getBlockPos(ctx, "position")

        Marker(ctx.source.world, pos).spawn()

        0
    })