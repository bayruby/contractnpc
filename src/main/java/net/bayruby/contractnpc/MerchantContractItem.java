package net.bayruby.contractnpc;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MerchantContractItem extends Item {
    public MerchantContractItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (level.isClientSide()) {
            return InteractionResultHolder.success(itemStack);
        }

        Vec3 spawnPos = player.getEyePosition().add(player.getLookAngle().scale(2.0D));
        Villager villager = EntityType.VILLAGER.create(level);
        if (villager == null) {
            return InteractionResultHolder.fail(itemStack);
        }

        villager.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
        villager.setYRot(player.getYRot());
        villager.setCustomName(Component.literal("Merchant"));
        villager.setCustomNameVisible(true);
        villager.setPersistenceRequired();

        if (!level.addFreshEntity(villager)) {
            return InteractionResultHolder.fail(itemStack);
        }

        player.swing(hand, true);
        return InteractionResultHolder.success(itemStack);
    }
}
