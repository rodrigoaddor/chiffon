package dev.rodrick.chiffon.data

import net.minecraft.util.math.Vec3d

private const val chestOffset = (0.0625 * 5) + 0.0105

val MarkerPositions: Map<MarkerType, Array<Vec3d>> = mapOf(
    MarkerType.SIMPLE to arrayOf(Vec3d(0.5, 0.0, 0.5)),

    MarkerType.NORMAL to arrayOf(),

    MarkerType.CHEST to arrayOf(
        Vec3d(chestOffset, 0.0, chestOffset),
        Vec3d(chestOffset, 0.0, 1 - chestOffset),
        Vec3d(1 - chestOffset, 0.0, chestOffset),
        Vec3d(1 - chestOffset, 0.0, 1 - chestOffset),
        Vec3d(chestOffset, 0.35, chestOffset),
        Vec3d(chestOffset, 0.35, 1 - chestOffset),
        Vec3d(1 - chestOffset, 0.35, chestOffset),
        Vec3d(1 - chestOffset, 0.35, 1 - chestOffset),
    )
)