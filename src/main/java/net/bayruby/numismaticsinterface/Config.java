package net.bayruby.numismaticsinterface;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class Config {

    private static final ModConfigSpec.Builder BUILDER =
            new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue LOG_DIRT_BLOCK =
            BUILDER
                    .comment(
                            "Whether to log the dirt block on common setup"
                    )
                    .define(
                            "logDirtBlock",
                            true
                    );

    public static final ModConfigSpec.IntValue MAGIC_NUMBER =
            BUILDER
                    .comment("A magic number")
                    .defineInRange(
                            "magicNumber",
                            42,
                            0,
                            Integer.MAX_VALUE
                    );

    public static final ModConfigSpec.ConfigValue<String>
            MAGIC_NUMBER_INTRODUCTION =
            BUILDER
                    .comment(
                            "What you want the introduction message to be for the magic number"
                    )
                    .define(
                            "magicNumberIntroduction",
                            "The magic number is... "
                    );

    public static final ModConfigSpec.ConfigValue<
            List<? extends String>
            > ITEM_STRINGS =
            BUILDER
                    .comment(
                            "Items and their shipping cost in emeralds.",
                            "Format: item_id=value",
                            "Example: minecraft:diamond_pickaxe=20000"
                    )
                    .defineListAllowEmpty(
                            "items",
                            List.of(
                                    "minecraft:diamond_pickaxe=20000"
                            ),
                            () -> "minecraft:diamond_pickaxe=20000",
                            Config::validateShippingItem
                    );

    public static final ModConfigSpec.IntValue WORKER_HOME_RADIUS =
            BUILDER
                    .comment(
                            "Maximum distance in blocks that a worker can roam from its home."
                    )
                    .defineInRange(
                            "workerHomeRadius",
                            30,
                            1,
                            256
                    );

    public static final ModConfigSpec.IntValue
            WORKER_RETURN_INTERVAL_SECONDS =
            BUILDER
                    .comment(
                            "How often, in seconds, the worker returns toward home."
                    )
                    .defineInRange(
                            "workerReturnIntervalSeconds",
                            60,
                            1,
                            3600
                    );

    static final ModConfigSpec SPEC =
            BUILDER.build();

    private static boolean validateShippingItem(
            final Object obj
    ) {
        if (!(obj instanceof String entry)) {
            return false;
        }

        String[] parts = entry.split("=", 2);

        if (parts.length != 2) {
            return false;
        }

        String itemName = parts[0];
        String valueString = parts[1];

        // Validate the item ID.
        try {
            ResourceLocation itemId =
                    ResourceLocation.parse(itemName);

            if (!BuiltInRegistries.ITEM.containsKey(itemId)) {
                return false;
            }
        } catch (IllegalArgumentException e) {
            return false;
        }

        // Validate the emerald cost.
        try {
            int value = Integer.parseInt(valueString);

            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}