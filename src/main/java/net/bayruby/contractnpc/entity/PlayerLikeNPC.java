package net.bayruby.contractnpc.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.synced.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import com.mojang.authlib.GameProfile;
import java.util.UUID;

/**
 * A custom humanoid NPC entity that resembles a player model.
 * Uses a GameProfile to support skin rendering and player-like appearance.
 */
public class PlayerLikeNPC extends LivingEntity {
    private static final int DATA_PLAYER_PROFILE_CACHE = 16;
    private GameProfile gameProfile;

    public PlayerLikeNPC(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
        this.gameProfile = new GameProfile(UUID.randomUUID(), "NPC");
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }

    /**
     * Set up AI behaviors for the NPC
     */
    @Override
    protected void registerGoals() {
        // Look at nearby players
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0F));
        // Random looking around when idle
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
        // Wander around randomly
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    }

    /**
     * Create default attributes for the NPC
     */
    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.1D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D);
    }

    /**
     * Set the name and appearance of this NPC
     */
    public void setNPCName(String name) {
        this.gameProfile = new GameProfile(this.gameProfile.getId(), name);
        this.setCustomName(Component.literal(name));
        this.setCustomNameVisible(true);
    }

    /**
     * Get the GameProfile used for rendering (skin display)
     */
    public GameProfile getGameProfile() {
        return this.gameProfile;
    }

    /**
     * Set a custom GameProfile (allows for custom skins)
     */
    public void setGameProfile(GameProfile profile) {
        this.gameProfile = profile;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.gameProfile != null) {
            CompoundTag profileTag = new CompoundTag();
            profileTag.putString("Name", this.gameProfile.getName());
            profileTag.putString("Id", this.gameProfile.getId().toString());
            tag.put("GameProfile", profileTag);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("GameProfile")) {
            CompoundTag profileTag = tag.getCompound("GameProfile");
            String name = profileTag.getString("Name");
            String idString = profileTag.getString("Id");
            try {
                UUID id = UUID.fromString(idString);
                this.gameProfile = new GameProfile(id, name);
            } catch (IllegalArgumentException e) {
                this.gameProfile = new GameProfile(UUID.randomUUID(), name);
            }
        }
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    protected float getStandingEyeHeight(net.minecraft.world.entity.Pose pose, net.minecraft.world.entity.EntityDimensions dimensions) {
        return 1.62F; // Player eye height
    }
}
