package net.bayruby.contractnpc.block;

import net.bayruby.contractnpc.ContractNpc;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(ContractNpc.MODID);

    public static final DeferredBlock<Block> SHIPPING_CONTAINER =
            BLOCKS.register(
                    "shipping_container",
                    () -> new ShippingContainerBlock(
                            BlockBehaviour.Properties.of()
                                    .strength(2.5F)
                    )
            );
}
