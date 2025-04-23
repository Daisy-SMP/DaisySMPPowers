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

package com.macuguita.daisysmppowers.trial;

import com.macuguita.daisysmppowers.component.DaisyComponents;
import com.macuguita.daisysmppowers.component.OriginTrialComponent;
import com.mojang.brigadier.CommandDispatcher;
import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.networking.ModPackets;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.origin.OriginLayers;
import io.github.apace100.origins.registry.ModComponents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ChangeTrialOriginCommand {

    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            registerCommands(dispatcher);
        });
    }

    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("trialorigin")
                .requires(source -> source.hasPermissionLevel(0))
                .executes(context -> {
                    ServerPlayerEntity player = context.getSource().getPlayer();
                    assert player != null;
                    OriginTrialComponent trial = DaisyComponents.ORIGIN_TRIAL.get(player);

                    if (trial.hasUsedTrial()) {
                        context.getSource().sendFeedback(() -> Text.translatable("commands.daisy_powers.trialorigin.expired"), false);
                        return 1;
                    }

                    openOriginGui(player);
                    trial.setUsedTrial(true);
                    context.getSource().sendFeedback(() -> Text.translatable("commands.daisy_powers.trialorigin.succes"), false);
                    return 1;
                }));
    }

    private static void openOriginGui(ServerPlayerEntity player) {
        OriginComponent component = ModComponents.ORIGIN.get(player);

        // Reset enabled layers to Origin.EMPTY
        for (OriginLayer layer : OriginLayers.getLayers()) {
            if (layer.isEnabled()) {
                component.setOrigin(layer, Origin.EMPTY);
            }
        }

        // Recalculate and sync
        component.checkAutoChoosingLayers(player, false);
        component.sync();

        // Send the packet to open the origin selection GUI
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(false); // mimic Origins command behavior
        ServerPlayNetworking.send(player, ModPackets.OPEN_ORIGIN_SCREEN, buf);
    }
}
