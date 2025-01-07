package me.sshcrack.everything_one.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {
    @WrapOperation(method = "onMouseClick(I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;onMouseClick(Lnet/minecraft/screen/slot/Slot;IILnet/minecraft/screen/slot/SlotActionType;)V", opcode = 1))
    private void cancelMouseClick(HandledScreen<?> instance, Slot slot, int slotId, int button, SlotActionType actionType, Operation<Void> original) {
        if (button != 4 || isInvalidSlot(slot))
            return;

        original.call(instance, slot, slotId, button, actionType);
    }

    @WrapOperation(method = "handleHotbarKeyPressed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;onMouseClick(Lnet/minecraft/screen/slot/Slot;IILnet/minecraft/screen/slot/SlotActionType;)V", opcode = 1))
    private void cancelHotbarKeyPressed(HandledScreen<?> instance, Slot slot, int slotId, int button, SlotActionType actionType, Operation<Void> original) {
        if (button != 4 || isInvalidSlot(slot))
            return;

        original.call(instance, slot, slotId, button, actionType);
    }

    @WrapOperation(method="drawSlotHighlightBack", at= @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;canBeHighlighted()Z"))
    private boolean cancelHighlightBack(Slot slot, Operation<Boolean> original) {
        if(isInvalidSlot(slot))
            return false;

        return original.call(slot);
    }


    @WrapOperation(method="drawSlotHighlightFront", at= @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;canBeHighlighted()Z"))
    private boolean cancelHighlightFront(Slot slot, Operation<Boolean> original) {
        if(isInvalidSlot(slot))
            return false;

        return original.call(slot);
    }

    @Unique
    private boolean isInvalidSlot(Slot slot) {
        if(!(slot.inventory instanceof PlayerInventory pInv) || slot.getClass().getName().equals("net.minecraft.screen.slot.ArmorSlot"))
            return false;

        // 40 is offhand slot
        return slot.getIndex() != 4 && slot.getIndex() != 40;
    }
}
