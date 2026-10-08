package net.bayruby.contractnpc.item;

import net.bayruby.contractnpc.ContractNpc;
import net.bayruby.contractnpc.block.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(ContractNpc.MODID);

    public static final DeferredItem<Item> STONE_SMITHING_TEMPLATE =
            ITEMS.register("stone_smithing_template",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> IRON_SMITHING_TEMPLATE =
            ITEMS.register("iron_smithing_template",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> GOLD_SMITHING_TEMPLATE =
            ITEMS.register("gold_smithing_template",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> DIAMOND_SMITHING_TEMPLATE =
            ITEMS.register("diamond_smithing_template",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RECIPE =
            ITEMS.register("recipe",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SHIPPING_CONTAINER =
            ITEMS.register(
                    "shipping_container",
                    () -> new BlockItem(
                            ModBlocks.SHIPPING_CONTAINER.get(),
                            new Item.Properties()
                    )
            );
}