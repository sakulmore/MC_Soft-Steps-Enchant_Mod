package com.softstepsenchant.mixin;

import com.softstepsenchant.SoftStepsEnchant;
import com.softstepsenchant.config.SoftStepsConfig;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

    @Inject(method = "getAvailableEnchantmentResults", at = @At("RETURN"))
    private static void filterSoftStepsConfig(int value, net.minecraft.world.item.ItemStack itemStack, java.util.stream.Stream<Holder<Enchantment>> source, CallbackInfoReturnable<List<EnchantmentInstance>> cir) {
        if (!SoftStepsConfig.INSTANCE.obtainInEnchantingTable) {
            List<EnchantmentInstance> results = cir.getReturnValue();

            results.removeIf(instance -> instance.enchantment().is(SoftStepsEnchant.SOFT_STEPS_KEY));
        }
    }
}