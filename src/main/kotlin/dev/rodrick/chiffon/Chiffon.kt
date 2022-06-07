package dev.rodrick.chiffon

import dev.rodrick.chiffon.commands.FindCommand
import dev.rodrick.chiffon.commands.MarkerCommand
import dev.rodrick.chiffon.events.Reap
import dev.rodrick.chiffon.events.SeedBag
import dev.rodrick.chiffon.events.TorchTicker
import dev.rodrick.chiffon.utils.Logger
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.player.UseBlockCallback

@Suppress("UNUSED")
object Chiffon : ModInitializer {
    private const val MOD_ID = ModInfo.MODID

    private val COMMANDS = listOf(
        FindCommand, MarkerCommand
    )

    override fun onInitialize() {
        COMMANDS.forEach { it.register() }

        UseBlockCallback.EVENT.register(Reap)
        UseBlockCallback.EVENT.register(SeedBag)

        TorchTicker.register()

        Logger.info("Initialized.")
    }
}
