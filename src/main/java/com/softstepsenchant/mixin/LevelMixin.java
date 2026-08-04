package com.softstepsenchant.mixin;

import com.softstepsenchant.SoftStepsEnchant;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class LevelMixin {

    @Inject(method = "gameEvent(Lnet/minecraft/core/Holder;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/level/gameevent/GameEvent$Context;)V", at = @At("HEAD"), cancellable = true)
    private void cancelJumpAndFallVibrations(Holder<GameEvent> event, Vec3 position, GameEvent.Context context, CallbackInfo ci) {
        if (context.sourceEntity() instanceof Player player) {
            if (event.is(GameEvent.HIT_GROUND.key())) {

                ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
                if (!boots.isEmpty()) {
                    var softSteps = player.level().registryAccess()
                            .lookupOrThrow(Registries.ENCHANTMENT)
                            .get(SoftStepsEnchant.SOFT_STEPS_KEY);

                    if (softSteps.isPresent() && EnchantmentHelper.getItemEnchantmentLevel(softSteps.get(), boots) > 0) {
                        ci.cancel();
                    }
                }
            }
        }
    }
}
