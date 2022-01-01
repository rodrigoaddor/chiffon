package dev.rodrick.chiffon.utils

import dev.rodrick.chiffon.data.MarkerPositions
import dev.rodrick.chiffon.data.MarkerType
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class Marker(private val world: World, pos: BlockPos) {
    private val basePosition = pos.toVec3d()

    fun spawn(type: MarkerType? = MarkerType.SIMPLE) {
        MarkerPositions.getOrDefault(type, MarkerPositions[MarkerType.SIMPLE])!!.forEach { offset ->
            val marker =
                EntityType.MAGMA_CUBE.create(world)
                    ?: throw IllegalStateException("Could not spawn MagmaCube")

            marker.applyRotation(BlockRotation.NONE)
            marker.isAiDisabled = true
            marker.isInvulnerable = true
            marker.isGlowing = true
            marker.activeStatusEffects[StatusEffects.INVISIBILITY] =
                StatusEffectInstance(StatusEffects.INVISIBILITY, Int.MAX_VALUE, 0, true, false)
            marker.headYaw = 0.0f

            marker.setPosition(basePosition.add(offset))
            world.spawnEntity(marker)

            TaskScheduler.scheduleDelayed(20 * 5) {
                marker.remove(Entity.RemovalReason.DISCARDED)
            }
        }
    }
}
