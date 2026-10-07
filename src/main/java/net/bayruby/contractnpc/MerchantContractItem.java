package net.bayruby.contractnpc;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MerchantContractItem extends Item {
    public MerchantContractItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        Vec3 spawnPos = player.getEyePosition().add(player.getLookAngle().scale(2.0D));
        Villager villager = EntityType.VILLAGER.create(level);
        if (villager == null) {
            return InteractionResult.FAIL;
        }

        villager.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
        villager.setYRot(player.getYRot());
        villager.setCustomName(Component.literal("Merchant"));

        if (!level.addFreshEntity(villager)) {
            return InteractionResult.FAIL;
        }

        // Do not consume the item; the contract acts like a spawn tool.
        return InteractionResult.SUCCESS;
    }
}
