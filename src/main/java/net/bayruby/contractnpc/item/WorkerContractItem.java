package net.bayruby.contractnpc.item;

import net.bayruby.contractnpc.worker.WorkerManager;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

public class WorkerContractItem
        extends Item {

    public WorkerContractItem(
            Properties properties
    ) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(
            UseOnContext context
    ) {
        if (
                !(context.getPlayer()
                        instanceof ServerPlayer player)
        ) {
            return InteractionResult.SUCCESS;
        }

        BlockPos clickedPos =
                context.getClickedPos();

        /*
         * Spawn on the face that was clicked,
         * just like a normal spawn egg.
         */
        BlockPos spawnPos =
                clickedPos.relative(
                        context.getClickedFace()
                );

        /*
         * The spawn location becomes home.
         */
        BlockPos homePos =
                spawnPos;

        boolean success =
                WorkerManager.recallWorker(
                        player,
                        spawnPos,
                        homePos
                );

        if (!success) {
            return InteractionResult.FAIL;
        }

        player.level().playSound(
                null,
                spawnPos.getX() + 0.5D,
                spawnPos.getY(),
                spawnPos.getZ() + 0.5D,
                SoundEvents.PLAYER_LEVELUP,
                player.getSoundSource(),
                1.0F,
                1.0F
        );

        ItemStack stack =
                context.getItemInHand();

        if (
                !player.getAbilities()
                        .instabuild
        ) {
            stack.shrink(1);
        }

        return InteractionResult.CONSUME;
    }
}