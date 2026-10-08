package net.bayruby.contractnpc.entity;

import net.bayruby.contractnpc.ContractNpc;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(
                    BuiltInRegistries.ENTITY_TYPE,
                    ContractNpc.MODID
            );

    public static final DeferredHolder<
            EntityType<?>,
            EntityType<HumanoidMob>
            > HUMANOID_MOB = ENTITY_TYPES.register(
            "humanoid_mob",
            () -> EntityType.Builder.of(
                            HumanoidMob::new,
                            MobCategory.CREATURE
                    )
                    .sized(
                            0.6F,
                            1.8F
                    )
                    .build(
                            "contractnpc:humanoid_mob"
                    )
    );

    private ModEntities() {
    }
}