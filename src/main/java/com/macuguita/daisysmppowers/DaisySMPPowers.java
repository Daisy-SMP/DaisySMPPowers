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

package com.macuguita.daisysmppowers;

import com.macuguita.daisysmppowers.component.DaisyComponents;
import com.macuguita.daisysmppowers.conditions.DaisyEntityConditionFactories;
import com.macuguita.daisysmppowers.trial.ChangeTrialOriginCommand;
import com.macuguita.lib.platform.registry.GuitaRegistries;
import com.macuguita.lib.platform.registry.GuitaRegistry;
import com.macuguita.lib.platform.registry.GuitaRegistryEntry;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.origins.Origins;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaisySMPPowers implements ModInitializer {

	public static final String MOD_ID = "daisy_powers";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final GuitaRegistry<Item> ITEMS = GuitaRegistries.create(Registries.ITEM, MOD_ID);

	public static final GuitaRegistryEntry<Item> SHARD_OF_ORIGIN = ITEMS.register("shard_of_origin", () -> new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));

	public static final GameRules.Key<GameRules.IntRule> TRIAL_TIME =
			GameRuleRegistry.register("setTrialTime", GameRules.Category.MISC, GameRuleFactory.createIntRule(30 * 60 * 1000)); // default 30 minutes in milliseconds

	public static final PowerType<Power> MORE_CROP_DROPS = new PowerTypeReference<>(id("more_crop_drops"));
	public static final PowerType<Power> LONGER_POTIONS = new PowerTypeReference<>(id("longer_potions"));
	public static final PowerType<Power> BETTER_FISHING_ROD = new PowerTypeReference<>(id("better_fishing_rod"));
	public static final PowerType<Power> FRIEND_WITH_THE_BEES = new PowerTypeReference<>(id("friend_with_the_bees"));
	public static final PowerType<Power> ITEM_COLLECTOR = new PowerTypeReference<>(id("item_collector"));
	public static final PowerType<Power> CAREFUL_GATHERER = new PowerTypeReference<>(id("careful_gatherer"));

	@Override
	public void onInitialize() {
		ITEMS.init();
		DaisyEntityConditionFactories.init();
		ChangeTrialOriginCommand.init();

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity player = handler.getPlayer();
			if (!DaisyComponents.ORIGIN_TRIAL.get(player).hasUsedTrial()) {
				player.sendMessage(Text.translatable("message.daisy_powers.trial_begin"));
			}
		});
	}

	public static Identifier id(String name) {
		return new Identifier(MOD_ID, name);
	}

}
