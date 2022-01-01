package dev.rodrick.chiffon.utils

import net.minecraft.block.entity.BlockEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.Item
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World

class InventoryFinder(private val world: World) {
    private fun area(center: BlockPos, radius: Int) = sequence {
        for (x in center.x - radius..center.x + radius) {
            for (y in center.y - radius..center.y + radius) {
                for (z in center.z - radius..center.z + radius) {
                    yield(BlockPos(x, y, z))
                }
            }
        }
    }

    private fun sphere(center: BlockPos, radius: Int): Sequence<BlockPos> = area(center, radius).filter {
        it.isWithinDistance(Vec3i(center.x, center.y, center.z), radius.toDouble())
    }

    private fun find(center: BlockPos, radius: Int): Sequence<Pair<BlockPos, BlockEntity>> =
        sphere(center, radius).mapNotNull {
            val block = world.getBlockEntity(it)
            if (block is Inventory) Pair(it, block) else null
        }

    fun withItem(center: BlockPos, radius: Int, item: Item): Sequence<Pair<BlockPos, BlockEntity>> =
        find(center, radius).filter { (_, block) ->
            (block as Inventory).containsAny(setOf(item))
        }
}
