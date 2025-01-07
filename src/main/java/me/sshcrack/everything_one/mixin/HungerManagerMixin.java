package me.sshcrack.everything_one.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.sshcrack.everything_one.EverythingOne;
import net.minecraft.entity.player.HungerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HungerManager.class)
public abstract class HungerManagerMixin {
	@Shadow public abstract void setFoodLevel(int foodLevel);

	@Shadow private int foodLevel;

	@Inject(at = @At("TAIL"), method = "<init>")
	private void init(CallbackInfo info) {
		this.setFoodLevel(EverythingOne.MAX_STUFF);
	}

	@Inject(method="setFoodLevel", at=@At("HEAD"), cancellable = true)
	private void setFoodLevel(int foodLevel, CallbackInfo info) {
		if (foodLevel > EverythingOne.MAX_STUFF) {
			this.setFoodLevel(EverythingOne.MAX_STUFF);
			info.cancel();
		}
	}

	@WrapOperation(method="addInternal", at= @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(III)I", opcode = 0))
	private int addInternal(int value, int min, int max, Operation<Integer> original) {
		return original.call(value, min, EverythingOne.MAX_STUFF);
	}

	@Inject(method="isNotFull", at=@At("HEAD"), cancellable = true)
	private void isNotFull(CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(this.foodLevel < EverythingOne.MAX_STUFF);
	}
}