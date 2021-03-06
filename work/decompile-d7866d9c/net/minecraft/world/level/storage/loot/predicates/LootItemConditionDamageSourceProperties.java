package net.minecraft.world.level.storage.loot.predicates;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Set;
import net.minecraft.advancements.critereon.CriterionConditionDamageSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.storage.loot.LootSerializer;
import net.minecraft.world.level.storage.loot.LootTableInfo;
import net.minecraft.world.level.storage.loot.parameters.LootContextParameter;
import net.minecraft.world.level.storage.loot.parameters.LootContextParameters;
import net.minecraft.world.phys.Vec3D;

public class LootItemConditionDamageSourceProperties implements LootItemCondition {

    private final CriterionConditionDamageSource a;

    private LootItemConditionDamageSourceProperties(CriterionConditionDamageSource criterionconditiondamagesource) {
        this.a = criterionconditiondamagesource;
    }

    @Override
    public LootItemConditionType b() {
        return LootItemConditions.l;
    }

    @Override
    public Set<LootContextParameter<?>> a() {
        return ImmutableSet.of(LootContextParameters.ORIGIN, LootContextParameters.DAMAGE_SOURCE);
    }

    public boolean test(LootTableInfo loottableinfo) {
        DamageSource damagesource = (DamageSource) loottableinfo.getContextParameter(LootContextParameters.DAMAGE_SOURCE);
        Vec3D vec3d = (Vec3D) loottableinfo.getContextParameter(LootContextParameters.ORIGIN);

        return vec3d != null && damagesource != null && this.a.a(loottableinfo.getWorld(), vec3d, damagesource);
    }

    public static LootItemCondition.a a(CriterionConditionDamageSource.a criterionconditiondamagesource_a) {
        return () -> {
            return new LootItemConditionDamageSourceProperties(criterionconditiondamagesource_a.b());
        };
    }

    public static class a implements LootSerializer<LootItemConditionDamageSourceProperties> {

        public a() {}

        public void a(JsonObject jsonobject, LootItemConditionDamageSourceProperties lootitemconditiondamagesourceproperties, JsonSerializationContext jsonserializationcontext) {
            jsonobject.add("predicate", lootitemconditiondamagesourceproperties.a.a());
        }

        @Override
        public LootItemConditionDamageSourceProperties a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
            CriterionConditionDamageSource criterionconditiondamagesource = CriterionConditionDamageSource.a(jsonobject.get("predicate"));

            return new LootItemConditionDamageSourceProperties(criterionconditiondamagesource);
        }
    }
}
