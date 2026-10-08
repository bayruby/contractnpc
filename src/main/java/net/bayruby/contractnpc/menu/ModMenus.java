package net.bayruby.contractnpc.menu;

import net.bayruby.contractnpc.ContractNpc;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenus {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(
                    BuiltInRegistries.MENU,
                    ContractNpc.MODID
            );

    public static final DeferredHolder<MenuType<?>, MenuType<ShippingMenu>>
            SHIPPING_MENU =
            MENUS.register(
                    "shipping_menu",
                    () -> new MenuType<>(
                            ShippingMenu::new,
                            FeatureFlags.DEFAULT_FLAGS
                    )
            );
}