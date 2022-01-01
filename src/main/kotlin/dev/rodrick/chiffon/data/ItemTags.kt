package dev.rodrick.chiffon.data

import net.fabricmc.fabric.api.tag.TagFactory
import net.minecraft.block.Block
import net.minecraft.tag.Tag
import net.minecraft.util.Identifier

class ItemTags {
    val REAPERS: Tag<Block> = TagFactory.BLOCK.create(Identifier("chifron", "reapers"))
}