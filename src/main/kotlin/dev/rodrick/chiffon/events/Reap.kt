package dev.rodrick.chiffon.events

import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.block.CropBlock
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items
import net.minecraft.loot.context.LootContext
import net.minecraft.loot.context.LootContextParameters
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.tag.BlockTags
import net.minecraft.text.TranslatableText
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

object Reap : UseBlockCallback {
    override fun interact(player: PlayerEntity, world: World, hand: Hand, hitResult: BlockHitResult): ActionResult {
        val pos = hitResult.blockPos
        val state = world.getBlockState(pos)
        val blockEntity = world.getBlockEntity(pos)
        val block = state.block

        val heldItem = player.getStackInHand(hand)

        val crops = BlockTags.CROPS
//        val seeds = ItemTags.

        // TODO: "reaper" tag
        val hoes = arrayOf(
            Items.WOODEN_HOE,
            Items.STONE_HOE,
            Items.IRON_HOE,
            Items.GOLDEN_HOE,
            Items.DIAMOND_HOE,
            Items.NETHERITE_HOE
        )

        if (heldItem.item in hoes && state.isIn(crops) && (block as CropBlock).isMature(state)) {
            if (world is ServerWorld) {
                val lootContext = LootContext.Builder(world).random(world.random)
                    .parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos))
                    .parameter(LootContextParameters.TOOL, heldItem)
                    .optionalParameter(LootContextParameters.THIS_ENTITY, player)
                    .optionalParameter(LootContextParameters.BLOCK_ENTITY, blockEntity)

                val items = state.getDroppedStacks(lootContext).map {
                    // TODO: use tag
                    if ((it.name as TranslatableText).key.contains("seed")) {
                        it.count -= 1
                    }
                    ItemEntity(world, pos.x + .5, pos.y + .0, pos.z.toDouble() + .5, it)
                }

                items.forEach { world.spawnEntity(it) }

                heldItem.damage(1, player ) {
                    world.playSound(null, it.blockPos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1.0f, 1.0f)
                }

                world.playSound(null, pos, state.soundGroup.breakSound, SoundCategory.BLOCKS, 1.0f, 1.0f)
                world.setBlockState(pos, block.defaultState)
            }
            return ActionResult.SUCCESS
        }

        return ActionResult.PASS
    }
}