package com.macuguita.daisysmppowers.mixin.origins;

import com.macuguita.daisysmppowers.component.DaisyComponents;
import io.github.apace100.origins.component.PlayerOriginComponent;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerOriginComponent.class)
public abstract class PlayerOriginComponentMixin {

    @Shadow(remap = false) public abstract boolean hadOriginBefore();

    @Shadow(remap = false) private PlayerEntity player;

    @Inject(
            method = "setOrigin",
            at = @At(
                    value = "INVOKE",
                    target = "Lio/github/apace100/origins/component/PlayerOriginComponent;hasAllOrigins()Z",
                    remap = false
            ),
            remap = false
    )
    private void daisypowers$setTrialTime(OriginLayer layer, Origin origin, CallbackInfo ci) {
        if (!this.hadOriginBefore()) {
            DaisyComponents.ORIGIN_TRIAL.get(this.player).setOriginSelectionTime(System.currentTimeMillis());
        }
    }
}
