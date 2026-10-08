package net.bayruby.numismaticsinterface;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(NumismaticsInterface.MODID)
public class NumismaticsInterface {

    public static final String MODID = "numismaticsinterface";

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(MODID);

    private void addCreative(
            BuildCreativeModeTabContentsEvent event
    ) {
    }

    @SubscribeEvent
    public void onServerStarting(
            ServerStartingEvent event
    ) {
        // Nothing needed yet.
    }
}