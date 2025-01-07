package me.sshcrack.everything_one.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.sshcrack.everything_one.EverythingOne;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @ModifyConstant(method = "renderFood", constant = @Constant(intValue = 10))
    private int changeFoodValue(int original) {
        return EverythingOne.MAX_STUFF / 2;
    }

    @ModifyConstant(method = "renderArmor", constant = @Constant(intValue = 10, ordinal = 1))
    private static int changeArmorValue(int original) {
        return EverythingOne.MAX_STUFF / 2;
    }

    @WrapOperation(method="renderHealthBar", at= @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"))
    private int changeHealthBarValue(Random instance, int i, Operation<Integer> original, @Local(argsOnly = true, ordinal = 4) int lastHealth, @Local(argsOnly = true, ordinal = 5) int absorption) {
        int add = lastHealth + absorption;
        if(add <= EverythingOne.MAX_STUFF)
            return original.call(instance, i);

        return 0;
    }
}
