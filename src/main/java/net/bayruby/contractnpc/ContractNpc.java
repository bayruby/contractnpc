package net.bayruby.contractnpc;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.bayruby.contractnpc.entity.ModEntities;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(ContractNpc.MODID)
public class ContractNpc {
    public static final String MODID = "contractnpc";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredItem<Item> MERCHANT_CONTRACT = ITEMS.register(
            "merchant_contract",
            () -> new MerchantContractItem(new Item.Properties().stacksTo(1))
    );

    public ContractNpc(IEventBus modEventBus, ModContainer modContainer) {
        ITEMS.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(MERCHANT_CONTRACT.get());
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}
