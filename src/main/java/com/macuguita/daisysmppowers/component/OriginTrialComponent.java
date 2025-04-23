package com.macuguita.daisysmppowers.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;

// Interface for the trial component
public interface OriginTrialComponent extends Component, ServerTickingComponent {
    boolean hasUsedTrial();
    void setUsedTrial(boolean used);
    long getOriginSelectionTime();
    void setOriginSelectionTime(long time);
}

