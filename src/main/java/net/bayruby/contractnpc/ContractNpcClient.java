package net.bayruby.contractnpc;

import net.bayruby.contractnpc.client.PlayerLikeNPCRenderer;
import net.bayruby.contractnpc.entity.ModEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.event.RegisterRenderersEvent;

@Mod(value = ContractNpc.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = ContractNpc.MODID, value = Dist.CLIENT)
public class ContractNpcClient {
    public ContractNpcClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
    }

    @SubscribeEvent
    static void registerRenderers(RegisterRenderersEvent event) {
        event.registerEntityRenderer(ModEntities.PLAYER_LIKE_NPC.get(), PlayerLikeNPCRenderer::new);
    }
}
