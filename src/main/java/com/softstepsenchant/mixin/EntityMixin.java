package com.softstepsenchant.mixin;

import com.softstepsenchant.SoftStepsEnchant;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(method = "isSteppingCarefully", at = @At("HEAD"), cancellable = true)
    private void fakeCrouchForSteps(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof Player player) {
            ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
            if (!boots.isEmpty()) {
                var softSteps = player.level().registryAccess()
                        .lookupOrThrow(Registries.ENCHANTMENT)
                        .get(SoftStepsEnchant.SOFT_STEPS_KEY);

                if (softSteps.isPresent() && EnchantmentHelper.getItemEnchantmentLevel(softSteps.get(), boots) > 0) {
                    cir.setReturnValue(true);
                }
            }
        }
    }
}