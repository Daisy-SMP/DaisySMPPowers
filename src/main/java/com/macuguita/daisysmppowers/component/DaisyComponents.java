package com.macuguita.daisysmppowers.component;

import com.macuguita.daisysmppowers.DaisySMPPowers;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;

public class DaisyComponents implements EntityComponentInitializer {

    public static final ComponentKey<OriginTrialComponent> ORIGIN_TRIAL =
            ComponentRegistry.getOrCreate(DaisySMPPowers.id("origin_trial"), OriginTrialComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(
                DaisyComponents.ORIGIN_TRIAL,
                OriginTrialComponentImpl::new,
                RespawnCopyStrategy.CHARACTER
        );
    }
}

