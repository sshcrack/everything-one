package me.sshcrack.everything_one.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    @Shadow public int selectedSlot;
    @Shadow @Final public DefaultedList<ItemStack> main;
    private int currI;

    @Inject(method="<init>", at=@At("TAIL"))
    private void init(CallbackInfo ci) {
        selectedSlot = 4;
    }

    @Inject(method = "isValidHotbarIndex", at=@At("HEAD"), cancellable = true)
    private static void isValidHotbarIndex(int slot, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(slot == 4);
    }

    @WrapOperation(method="getOccupiedSlotWithRoomForStack", at= @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;get(I)Ljava/lang/Object;"))
    private <E> E getOccupiedSlotWithRoomForStack(DefaultedList<E> instance, int index, Operation<E> original) {
        this.currI = index;
        return original.call(instance, index);
    }

    @WrapOperation(method="getOccupiedSlotWithRoomForStack", at= @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;canStackAddMore(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z"))
    private boolean getOccupiedSlotWithRoomForStack(PlayerInventory instance, ItemStack existingStack, ItemStack stack, Operation<Boolean> original) {
        if(currI == 4)
            return original.call(instance, existingStack, stack);
        return false;
    }

    @Inject(method="setSelectedSlot", at=@At("HEAD"), cancellable = true)
    private void setSelectedSlot(int slot, CallbackInfo ci) {
        selectedSlot = 4;
        ci.cancel();
    }

    @Inject(method="getEmptySlot", at=@At("HEAD"), cancellable = true)
    private void getEmptySlot(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(main.get(4).isEmpty() ? 4 : -1);
    }
}
