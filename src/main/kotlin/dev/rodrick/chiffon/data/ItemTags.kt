package dev.rodrick.chiffon.data

import dev.rodrick.chiffon.ModInfo
import net.minecraft.block.Block
import net.minecraft.tag.TagKey
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

class ItemTags {
    val REAPERS: TagKey<Block> = TagKey.of(Registry.BLOCK_KEY, Identifier(ModInfo.MODID, "reaper_tools"))
}