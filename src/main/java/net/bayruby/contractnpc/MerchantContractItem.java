package net.bayruby.contractnpc;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;

import java.util.UUID;

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
        
        // Create a GameProfile with a unique UUID for the fake player
        UUID fakePlayerUUID = UUID.randomUUID();
        GameProfile gameProfile = new GameProfile(fakePlayerUUID, "FakePlayer_" + fakePlayerUUID.toString().substring(0, 8));
        
        // Create the fake player
        FakePlayer fakePlayer = FakePlayerFactory.get((net.minecraft.server.level.ServerLevel) level, gameProfile);
        fakePlayer.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
        fakePlayer.setYRot(player.getYRot());
        fakePlayer.setCustomName(Component.literal("Player"));
        fakePlayer.setCustomNameVisible(true);

        // Add the fake player to the world
        if (!level.addFreshEntity(fakePlayer)) {
            return InteractionResultHolder.fail(itemStack);
        }

        player.swing(hand, true);
        return InteractionResultHolder.success(itemStack);
    }
}
