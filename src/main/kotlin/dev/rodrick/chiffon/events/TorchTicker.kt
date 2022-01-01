package dev.rodrick.chiffon.events

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityType
import net.minecraft.item.Items

object TorchTicker {
    private val torches = mapOf(
        Items.TORCH to Blocks.TORCH,
        Items.SOUL_TORCH to Blocks.SOUL_TORCH,
        Items.REDSTONE_TORCH to Blocks.REDSTONE_TORCH
    )

    fun register() {
        ServerTickEvents.START_WORLD_TICK.register {
            val entities = it.getEntitiesByType(EntityType.ITEM) { entity ->
                entity.thrower != null && entity.stack.item in torches && entity.stack.count == 1 && entity.isOnGround && entity.age > 20 * 5 && entity.blockStateAtPos.isAir
            }

            entities.forEach { entity ->
                entity.setDespawnImmediately()
                entity.world.setBlockState(entity.blockPos, torches[entity.stack.item]!!.defaultState)
            }
        }
    }
}