package net.bayruby.contractnpc.event;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = "contractnpc")
public class RecipeUnlockEvents {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {

        if (!(event.getEntity() instanceof ServerPlayer player))
            return;

        var data = player.getPersistentData();

        // Run only once per player
        if (data.getBoolean("recipes_initialized"))
            return;

        data.putBoolean("recipes_initialized", true);

        // Give all recipes
        var allRecipes = player.server.getRecipeManager().getRecipes();
        player.awardRecipes(allRecipes);

        // Remove diamond tools and armor
        var lockedRecipes = allRecipes.stream()
                .filter(recipe -> switch (recipe.id().toString()) {
                    case "minecraft:diamond_sword",
                         "minecraft:diamond_pickaxe",
                         "minecraft:diamond_axe",
                         "minecraft:diamond_shovel",
                         "minecraft:diamond_hoe",
                         "minecraft:diamond_helmet",
                         "minecraft:diamond_chestplate",
                         "minecraft:diamond_leggings",
                         "minecraft:diamond_boots" -> true;
                    default -> false;
                })
                .toList();

        player.resetRecipes(lockedRecipes);
    }
}