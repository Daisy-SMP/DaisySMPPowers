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

package com.macuguita.daisysmppowers.conditions;

import com.macuguita.daisysmppowers.DaisySMPPowers;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.util.Pair;

import java.util.function.Predicate;

public class NearbyEntitiesCondition {
    public static ConditionFactory<Entity> createFactory() {
        return new ConditionFactory<>(DaisySMPPowers.id("nearby_entities"), new SerializableData()
                .add("multiplier", SerializableDataTypes.DOUBLE)
                .add("comparison", ApoliDataTypes.COMPARISON)
                .add("compare_to", SerializableDataTypes.INT)
                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null),
                (data, entity) -> {
                    Predicate<Pair<Entity, Entity>> bientityCondition = data.get("bientity_condition");
                    int amount = entity.getWorld().getOtherEntities(entity, entity.getBoundingBox().expand(data.getDouble("multiplier")), otherEntity -> bientityCondition.test(new Pair<>(entity, otherEntity))).size();
                    Comparison comparison = data.get("comparison");
                    int compareTo = data.getInt("compare_to");
                    return comparison.compare(amount, compareTo);
                });
    }
}