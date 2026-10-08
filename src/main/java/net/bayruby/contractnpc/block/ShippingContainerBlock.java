package net.bayruby.contractnpc.block;

import net.bayruby.contractnpc.menu.ShippingMenu;
import net.bayruby.contractnpc.shipping.ModAttachments;
import net.bayruby.contractnpc.shipping.ShippingInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ShippingContainerBlock extends Block {

    public ShippingContainerBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hit
    ) {

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        ServerPlayer serverPlayer = (ServerPlayer) player;

        ShippingInventory shippingInventory =
                serverPlayer.getData(
                        ModAttachments.SHIPPING_INVENTORY.get()
                );

        MenuProvider menuProvider =
                new SimpleMenuProvider(
                        (containerId, playerInventory, p) ->
                                new ShippingMenu(
                                        containerId,
                                        playerInventory,
                                        shippingInventory.getInventory()
                                ),
                        Component.literal("Shipping Container")
                );

        serverPlayer.openMenu(menuProvider);

        return InteractionResult.CONSUME;
    }
}