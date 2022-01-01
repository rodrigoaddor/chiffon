package dev.rodrick.chiffon.callbacks

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult

fun interface PlayerDropCallback {
    companion object {
        val EVENT: Event<PlayerDropCallback> = EventFactory.createArrayBacked(
            PlayerDropCallback::class.java
        ) { listeners ->
            PlayerDropCallback { player, item ->
                listeners.forEach {
                    val result = it.drop(player, item)
                    if (result != ActionResult.PASS) {
                        return@PlayerDropCallback result
                    }
                }

                ActionResult.PASS
            }
        }
    }

    fun drop(player: PlayerEntity, item: ItemEntity): ActionResult
}