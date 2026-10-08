package net.bayruby.contractnpc;

import net.bayruby.contractnpc.block.ModBlocks;
import net.bayruby.contractnpc.entity.ModEntities;
import net.bayruby.contractnpc.item.ModItems;
import net.bayruby.contractnpc.item.WorkerContractItem;

import net.bayruby.contractnpc.menu.ModMenus;
import net.bayruby.contractnpc.shipping.ModAttachments;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(ContractNpc.MODID)
public class ContractNpc {

    public static final String MODID = "contractnpc";

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(MODID);

    public static final DeferredItem<SpawnEggItem> HUMANOID_MOB_SPAWN_EGG =
            ITEMS.register(
                    "humanoid_mob_spawn_egg",
                    () -> new DeferredSpawnEggItem(
                            ModEntities.HUMANOID_MOB,
                            0x4A90E2,
                            0xD8A06A,
                            new Item.Properties()
                    )
            );

    public static final DeferredItem<Item> WORKER_CONTRACT =
            ITEMS.register(
                    "worker_contract",
                    () -> new WorkerContractItem(
                            new Item.Properties()
                    )
            );

    public ContractNpc(
            IEventBus modEventBus,
            ModContainer modContainer
    ) {

        ITEMS.register(modEventBus);

        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);

        ModAttachments.ATTACHMENTS.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);


        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::createEntityAttributes);

        NeoForge.EVENT_BUS.register(this);

        modContainer.registerConfig(
                ModConfig.Type.COMMON,
                Config.SPEC
        );
    }

    private void createEntityAttributes(
            EntityAttributeCreationEvent event
    ) {
        event.put(
                ModEntities.HUMANOID_MOB.get(),
                createHumanoidAttributes().build()
        );
    }

    private static AttributeSupplier.Builder createHumanoidAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D)
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.ARMOR, 0.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.0D);
    }

    private void addCreative(
            BuildCreativeModeTabContentsEvent event
    ) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(
                    HUMANOID_MOB_SPAWN_EGG.get()
            );
        }

        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(WORKER_CONTRACT.get());
            event.accept(ModItems.STONE_SMITHING_TEMPLATE.get());
            event.accept(ModItems.IRON_SMITHING_TEMPLATE.get());
            event.accept(ModItems.GOLD_SMITHING_TEMPLATE.get());
            event.accept(ModItems.DIAMOND_SMITHING_TEMPLATE.get());
            event.accept(ModItems.RECIPE.get());
            event.accept(ModItems.SHIPPING_CONTAINER.get());
        }
    }

    @SubscribeEvent
    public void onServerStarting(
            ServerStartingEvent event
    ) {
        // Nothing needed yet.
    }
}