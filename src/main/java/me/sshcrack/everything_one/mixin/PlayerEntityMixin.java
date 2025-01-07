package me.sshcrack.everything_one.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.sshcrack.everything_one.EverythingOne;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class PlayerEntityMixin {
    @WrapOperation(method = "getArmor", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D"))
    private double capMaxArmor(LivingEntity instance, RegistryEntry<EntityAttribute> attribute, Operation<Double> original) {
        return Math.min(original.call(instance, attribute), EverythingOne.MAX_STUFF);
    }
}
