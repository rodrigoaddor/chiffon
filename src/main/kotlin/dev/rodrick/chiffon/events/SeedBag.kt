package dev.rodrick.chiffon.events

import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object SeedBag : UseBlockCallback {
    private val seeds = arrayOf(
        Items.WHEAT_SEEDS,
        Items.MELON_SEEDS,
        Items.PUMPKIN_SEEDS,
        Items.BEETROOT_SEEDS,
        Items.CARROT,
        Items.POTATO
    )

    override fun interact(player: PlayerEntity, world: World, hand: Hand, hitResult: BlockHitResult): ActionResult {
        val itemStack = player.getStackInHand(hand)
        val blockState = world.getBlockState(hitResult.blockPos)

        if (blockState.block == Blocks.FARMLAND && itemStack.item == Items.BUNDLE) {
            val contents = getBundleItems(itemStack)

            if (contents != null && contents.size == 1 && contents.first().item in seeds) {
                val centerBlock = hitResult.blockPos.offset(hitResult.side)
                val placeableBlocks = centerBlock.getAreaAround(x = 1, z = 1).filter {
                    world.isAir(it) && world.getBlockState(it.add(0, -1, 0)).block == Blocks.FARMLAND
                }

                placeableBlocks.take(contents.first().count).forEach {
                    world.setBlockState(it, Blocks.WHEAT.defaultState)
                }

                contents.first().count -= placeableBlocks.size
                setBundleItems(itemStack, contents)

                return ActionResult.SUCCESS
            }
        }

        return ActionResult.PASS
    }

    private fun getBundleItems(bundle: ItemStack): List<ItemStack>? {
        return (bundle.nbt?.get("Items") as? NbtList)?.map { ItemStack.fromNbt(it as NbtCompound) }
    }

    private fun setBundleItems(bundle: ItemStack, items: List<ItemStack>) {
        val itemsNbt = NbtList()
        items.filter{ it.item != Items.AIR && it.count > 0 }.forEach {
            val compound = NbtCompound()
            it.writeNbt(compound)
            itemsNbt.add(compound)
        }

        if (itemsNbt.isNotEmpty()) {
            bundle.setSubNbt("Items", itemsNbt)
        } else {
            bundle.removeSubNbt("Items")
        }

    }

    private fun BlockPos.getAreaAround(x: Int = 0, y: Int = 0, z: Int = 0): List<BlockPos> {
        val origin = this
        return sequence {
            for (xPos in origin.x - x..origin.x + x) {
                for (yPos in origin.y - y..origin.y + y) {
                    for (zPos in origin.z - z..origin.z + z) {
                        yield(BlockPos(xPos, yPos, zPos))
                    }
                }
            }
        }.toList()
    }
}

