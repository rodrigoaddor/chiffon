package dev.rodrick.chiffon.commands

import dev.rodrick.chiffon.utils.Marker
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.argument.BlockPosArgumentType
import net.minecraft.command.argument.BlockPosArgumentType.getBlockPos

import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal

object MarkerCommand : BaseCommand {
    override fun register() {
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, registryAccess, environment ->
            dispatcher.register(
                literal("marker").then(argument("position", BlockPosArgumentType.blockPos()).executes { ctx ->
                        val pos = getBlockPos(ctx, "position")
                        Marker(ctx.source.world, pos).spawn()

                        0
                    })
            )
        })
    }
}
