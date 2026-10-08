package net.bayruby.contractnpc;

import net.bayruby.contractnpc.client.EmeraldCostTooltip;
import net.bayruby.contractnpc.client.EmeraldCostTooltipClient;
import net.bayruby.contractnpc.client.HumanoidMobRenderer;
import net.bayruby.contractnpc.client.ShippingScreen;
import net.bayruby.contractnpc.entity.ModEntities;
import net.bayruby.contractnpc.menu.ModMenus;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@Mod(
        value = ContractNpc.MODID,
        dist = Dist.CLIENT
)
@EventBusSubscriber(
        modid = ContractNpc.MODID,
        value = Dist.CLIENT
)
public class ContractNpcClient {

    public ContractNpcClient() {
    }

    @SubscribeEvent
    public static void registerRenderers(
            EntityRenderersEvent.RegisterRenderers event
    ) {
        event.registerEntityRenderer(
                ModEntities.HUMANOID_MOB.get(),
                HumanoidMobRenderer::new
        );
    }


    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(
                ModMenus.SHIPPING_MENU.get(),
                ShippingScreen::new
        );
    }

    @SubscribeEvent
    public static void registerTooltipComponents(
            RegisterClientTooltipComponentFactoriesEvent event
    ) {
        event.register(
                EmeraldCostTooltip.class,
                EmeraldCostTooltipClient::new
        );
    }
}