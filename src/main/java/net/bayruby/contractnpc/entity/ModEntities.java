package net.bayruby.contractnpc.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, "contractnpc");

    public static final DeferredHolder<EntityType<?>, EntityType<PlayerLikeNPC>> PLAYER_LIKE_NPC =
            ENTITIES.register("player_like_npc",
                    () -> EntityType.Builder.<PlayerLikeNPC>of(PlayerLikeNPC::new, MobCategory.MISC)
                            .sized(0.6F, 1.8F)
                            .clientTrackingRange(8)
                            .updateInterval(20)
                            .build("contractnpc:player_like_npc"));
}
