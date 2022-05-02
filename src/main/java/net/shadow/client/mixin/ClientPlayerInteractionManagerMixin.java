/*
 * Copyright (c) Shadow client, 0x150, Saturn5VFive 2022. All rights reserved.
 */

package net.shadow.client.mixin;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.shadow.client.feature.module.ModuleRegistry;
import net.shadow.client.feature.module.impl.combat.Reach;
import net.shadow.client.feature.module.impl.world.NoBreakDelay;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Shadow
    private int blockBreakingCooldown;

    @Redirect(method = "updateBlockBreakingProgress", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;blockBreakingCooldown:I", opcode = Opcodes.GETFIELD, ordinal = 0))
    public int overwriteCooldown(ClientPlayerInteractionManager clientPlayerInteractionManager) {
        int cd = this.blockBreakingCooldown;
        return Objects.requireNonNull(ModuleRegistry.getByClass(NoBreakDelay.class)).isEnabled() ? 0 : cd;
    }

    @Inject(method = { "getReachDistance()F" }, at = { @At("HEAD") }, cancellable = true)
    private void onReachDistance(CallbackInfoReturnable<Float> cir) {
        if (ModuleRegistry.getByClass(Reach.class).isEnabled()) {
            cir.setReturnValue((float) ModuleRegistry.getByClass(Reach.class).getReachDistance());
        }
    }

    @Inject(method = { "hasExtendedReach()Z" }, at = { @At("HEAD") }, cancellable = true)
    private void onExtendedReach(CallbackInfoReturnable<Boolean> cir) {
        if (ModuleRegistry.getByClass(Reach.class).isEnabled()) {
            cir.setReturnValue(true);
        }
    }

}
