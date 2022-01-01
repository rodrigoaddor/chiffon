package dev.rodrick.chiffon.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import dev.rodrick.chiffon.utils.InventoryFinder
import dev.rodrick.chiffon.utils.Marker
import net.minecraft.command.argument.ItemStackArgumentType
import net.minecraft.command.argument.ItemStackArgumentType.getItemStackArgument
import net.minecraft.item.Item
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.LiteralText
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

private fun showInventoriesWithItem(world: World, position: BlockPos, item: Item): Int {
    val inventories = InventoryFinder(world)
        .withItem(position, 10, item)
        .toList()

    inventories.forEach { (position) ->
        Marker(world, position).spawn()
    }

    return inventories.size
}

val FindCommand: LiteralArgumentBuilder<ServerCommandSource> =
    literal("find")
        .executes { ctx ->
            val world = ctx.source.world
            val position = ctx.source.player.blockPos
            val item = ctx.source.player.getStackInHand(Hand.MAIN_HAND).item

            val count = showInventoriesWithItem(world, position, item)

            ctx.source.player.sendMessage(LiteralText("Found $count inventories"), true)

            count
        }
        .then(argument("item", ItemStackArgumentType.itemStack())
            .executes { ctx ->
                val world = ctx.source.world
                val position = ctx.source.player.blockPos
                val item = getItemStackArgument(ctx, "item").item
                showInventoriesWithItem(world, position, item)
            })

