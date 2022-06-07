package dev.rodrick.chiffon.commands

import dev.rodrick.chiffon.utils.InventoryFinder
import dev.rodrick.chiffon.utils.Marker
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.argument.ItemStackArgumentType
import net.minecraft.command.argument.ItemStackArgumentType.getItemStackArgument
import net.minecraft.item.Item
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object FindCommand : BaseCommand {
    override fun register() {
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, registryAccess, environment ->
            dispatcher.register(literal("find").executes { ctx ->
                if (ctx.source.player != null) {
                    val world = ctx.source.world
                    val position = ctx.source.player!!.blockPos
                    val item = ctx.source.player!!.getStackInHand(Hand.MAIN_HAND).item

                    val count = showInventoriesWithItem(world, position, item)

                    ctx.source.player!!.sendMessage(Text.literal("Found $count inventories"), true)

                    count
                } else {
                    return@executes -1
                }
            }.then(argument("item", ItemStackArgumentType.itemStack(registryAccess)).executes { ctx ->
                if (ctx.source.player != null) {
                    val world = ctx.source.world
                    val position = ctx.source.player!!.blockPos
                    val item = getItemStackArgument(ctx, "item").item

                    return@executes showInventoriesWithItem(world, position, item)
                } else {
                    return@executes -1
                }
            })
            )
        })
    }

    private fun showInventoriesWithItem(world: World, position: BlockPos, item: Item): Int {
        val inventories = InventoryFinder(world).withItem(position, 10, item).toList()

        inventories.forEach { (position) ->
            Marker(world, position).spawn()
        }

        return inventories.size
    }
}


