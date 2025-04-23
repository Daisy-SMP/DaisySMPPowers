/*
 * Copyright (c) 2025 macuguita.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.macuguita.daisysmppowers.component;

import com.macuguita.daisysmppowers.DaisySMPPowers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

// Implementation of the trial component
public class OriginTrialComponentImpl implements OriginTrialComponent {
    private final PlayerEntity player;
    private boolean usedTrial = false;
    private long originSelectionTime = 0L;

    public OriginTrialComponentImpl(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public boolean hasUsedTrial() {
        return usedTrial;
    }

    @Override
    public void setUsedTrial(boolean used) {
        this.usedTrial = used;
    }

    @Override
    public long getOriginSelectionTime() {
        return originSelectionTime;
    }

    @Override
    public void setOriginSelectionTime(long time) {
        this.originSelectionTime = time;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.usedTrial = tag.getBoolean("UsedTrial");
        this.originSelectionTime = tag.getLong("OriginSelectionTime");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("UsedTrial", this.usedTrial);
        tag.putLong("OriginSelectionTime", this.originSelectionTime);
    }

    @Override
    public void serverTick() {
        if (!usedTrial && originSelectionTime != 0) {
            long elapsed = System.currentTimeMillis() - originSelectionTime;

            if (player instanceof ServerPlayerEntity serverPlayer) {
                int trialTime = serverPlayer.getWorld().getGameRules().get(DaisySMPPowers.TRIAL_TIME).get();

                if (elapsed > trialTime) {
                    usedTrial = true;
                    serverPlayer.sendMessage(Text.translatable("message.daisy_powers.trial_end"), false);
                }
            }
        }
    }
}
