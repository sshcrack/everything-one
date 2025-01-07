package me.sshcrack.everything_one.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    @Final
    public GameOptions options;

    @WrapOperation(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;wasPressed()Z", opcode = 2))
    private boolean onlyAcceptMiddleSlot(KeyBinding instance, Operation<Boolean> original) {
        int index = -1;
        for (int i = 0; i < 9; i++) {
            if (
                    this.options.hotbarKeys[i] == instance) {
                index = i;
                break;
            }
        }

        if (index == 4 || index == -1)
            return original.call(instance);

        return false;
    }
}
