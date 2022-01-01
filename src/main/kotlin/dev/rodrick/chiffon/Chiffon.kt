package dev.rodrick.chiffon

import com.mojang.brigadier.CommandDispatcher
import dev.rodrick.chiffon.commands.FindCommand
import dev.rodrick.chiffon.commands.MarkerCommand
import dev.rodrick.chiffon.events.Reap
import dev.rodrick.chiffon.events.TorchTicker
import dev.rodrick.chiffon.utils.Logger
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.server.command.ServerCommandSource

@Suppress("UNUSED")
object Chiffon : ModInitializer {
    private const val MOD_ID = ModInfo.MODID
    override fun onInitialize() {
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher: CommandDispatcher<ServerCommandSource>, _: Boolean ->
            dispatcher.register(FindCommand)
            dispatcher.register(MarkerCommand)
        })

        UseBlockCallback.EVENT.register(Reap)

        TorchTicker.register()

        Logger.info("Initialized.")
    }
}
