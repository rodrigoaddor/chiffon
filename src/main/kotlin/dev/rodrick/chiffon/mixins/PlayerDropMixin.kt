package dev.rodrick.chiffon.mixins

import dev.rodrick.chiffon.callbacks.PlayerDropCallback
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(PlayerEntity::class)
class PlayerDropMixin {
    @Inject(
        method = ["Lnet/minecraft/entity/player/PlayerEntity;dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;"],
        at = [At("RETURN")],
        cancellable = true
    )
    private fun drop(
        stack: ItemStack,
        throwRandomly: Boolean,
        retainOwnership: Boolean,
        cir: CallbackInfoReturnable<ItemEntity>
    ) {
        val player = this as Any

        @Suppress("KotlinConstantConditions")
        if (cir.returnValue != null && PlayerDropCallback.EVENT.invoker()
                .drop(player as PlayerEntity, cir.returnValue).isAccepted
        ) {
            cir.returnValue = null
        }
    }
}