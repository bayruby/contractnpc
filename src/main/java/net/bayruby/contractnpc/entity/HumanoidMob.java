package net.bayruby.contractnpc.entity;

import net.bayruby.contractnpc.Config;
import net.bayruby.contractnpc.worker.WorkerManager;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class HumanoidMob
        extends PathfinderMob
        implements Merchant, MenuProvider {

    private static final String OWNER_UUID_TAG =
            "ContractNpcOwner";

    private static final String HOME_X_TAG =
            "ContractNpcHomeX";

    private static final String HOME_Y_TAG =
            "ContractNpcHomeY";

    private static final String HOME_Z_TAG =
            "ContractNpcHomeZ";

    private UUID ownerUUID;

    private BlockPos homePos;

    private String homeDimension;

    private long nextHomeReturnTick;

    private String lastSavedDimension;

    private int lastSavedChunkX =
            Integer.MIN_VALUE;

    private int lastSavedChunkZ =
            Integer.MIN_VALUE;

    /*
     * Merchant data
     */
    private final MerchantOffers offers =
            new MerchantOffers();

    private Player tradingPlayer;

    private int villagerXp = 0;

    public HumanoidMob(
            EntityType<? extends HumanoidMob> entityType,
            Level level
    ) {
        super(
                entityType,
                level
        );

        setPersistenceRequired();

        /*
         * Trade:
         *
         * 1 Emerald -> 1 Stick
         *
         * 9999 uses means the trade effectively
         * does not run out during normal use.
         */
        this.offers.add(
                new MerchantOffer(
                        new ItemCost(
                                Items.EMERALD,
                                1
                        ),
                        new ItemStack(
                                Items.STICK,
                                1
                        ),
                        9999,
                        1,
                        0.05F
                )
        );
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(
                1,
                new FloatGoal(this)
        );

        this.goalSelector.addGoal(
                2,
                new RandomStrollGoal(
                        this,
                        1.0D
                )
        );

        this.goalSelector.addGoal(
                3,
                new LookAtPlayerGoal(
                        this,
                        Player.class,
                        8.0F
                )
        );

        this.goalSelector.addGoal(
                4,
                new RandomLookAroundGoal(
                        this
                )
        );
    }

    /*
     * Right-click interaction
     */
    @Override
    public InteractionResult mobInteract(
            Player player,
            InteractionHand hand
    ) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }

        if (!level().isClientSide
                && player instanceof ServerPlayer serverPlayer) {

            /*
             * Remember who is currently trading.
             */
            setTradingPlayer(serverPlayer);

            /*
             * Open the vanilla Merchant UI.
             */
            serverPlayer.openMenu(this);

            /*
             * Explicitly send the merchant offers to the client.
             *
             * This is important for a custom Merchant implementation
             * because the client needs the MerchantOffers before the
             * trade list can be displayed.
             */
            serverPlayer.sendMerchantOffers(
                    serverPlayer.containerMenu.containerId,
                    getOffers(),
                    1,
                    getVillagerXp(),
                    false,
                    false
            );

            return InteractionResult.CONSUME;
        }

        return InteractionResult.SUCCESS;
    }

    /*
     * Creates the vanilla merchant container.
     */
    @Override
    public AbstractContainerMenu createMenu(
            int containerId,
            Inventory inventory,
            Player player
    ) {
        return new MerchantMenu(
                containerId,
                inventory,
                this
        );
    }

    /*
     * Name shown at the top of the trading screen.
     */
    @Override
    public Component getDisplayName() {
        return Component.literal(
                "Contract Worker"
        );
    }

    /*
     * Merchant interface
     */

    @Override
    public void setTradingPlayer(
            Player player
    ) {
        this.tradingPlayer = player;
    }

    @Override
    public Player getTradingPlayer() {
        return tradingPlayer;
    }

    @Override
    public MerchantOffers getOffers() {
        return offers;
    }

    @Override
    public void overrideOffers(
            MerchantOffers offers
    ) {
        this.offers.clear();

        this.offers.addAll(
                offers
        );
    }

    @Override
    public void notifyTrade(
            MerchantOffer offer
    ) {
        this.villagerXp +=
                offer.getXp();
    }

    @Override
    public void notifyTradeUpdated(
            ItemStack stack
    ) {
        // Nothing needed here.
    }

    @Override
    public int getVillagerXp() {
        return villagerXp;
    }

    @Override
    public void overrideXp(
            int xp
    ) {
        this.villagerXp =
                xp;
    }

    @Override
    public boolean showProgressBar() {
        return false;
    }

    @Override
    public boolean canRestock() {
        return false;
    }

    @Override
    public boolean isClientSide() {
        return level().isClientSide;
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return SoundEvents.VILLAGER_YES;
    }

    /*
     * Owner
     */

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(
            UUID ownerUUID
    ) {
        this.ownerUUID =
                ownerUUID;

        this.lastSavedDimension =
                null;

        this.lastSavedChunkX =
                Integer.MIN_VALUE;

        this.lastSavedChunkZ =
                Integer.MIN_VALUE;
    }

    /*
     * Home
     */

    public BlockPos getHomePos() {
        return homePos;
    }

    public String getHomeDimension() {
        return homeDimension;
    }

    public void setHome(
            BlockPos homePos,
            String homeDimension
    ) {
        this.homePos =
                homePos.immutable();

        this.homeDimension =
                homeDimension;

        restrictTo(
                this.homePos,
                Config.WORKER_HOME_RADIUS.get()
        );

        long intervalTicks =
                Config
                        .WORKER_RETURN_INTERVAL_SECONDS
                        .get()
                        * 20L;

        this.nextHomeReturnTick =
                this.tickCount
                        + intervalTicks;
    }

    /*
     * Save data
     */

    @Override
    public void addAdditionalSaveData(
            CompoundTag compound
    ) {
        super.addAdditionalSaveData(
                compound
        );

        if (ownerUUID != null) {
            compound.putUUID(
                    OWNER_UUID_TAG,
                    ownerUUID
            );
        }

        if (homePos != null) {
            compound.putInt(
                    HOME_X_TAG,
                    homePos.getX()
            );

            compound.putInt(
                    HOME_Y_TAG,
                    homePos.getY()
            );

            compound.putInt(
                    HOME_Z_TAG,
                    homePos.getZ()
            );
        }
    }

    @Override
    public void readAdditionalSaveData(
            CompoundTag compound
    ) {
        super.readAdditionalSaveData(
                compound
        );

        if (compound.hasUUID(
                OWNER_UUID_TAG
        )) {
            ownerUUID =
                    compound.getUUID(
                            OWNER_UUID_TAG
                    );
        }

        if (
                compound.contains(HOME_X_TAG)
                        && compound.contains(HOME_Y_TAG)
                        && compound.contains(HOME_Z_TAG)
        ) {
            homePos =
                    new BlockPos(
                            compound.getInt(
                                    HOME_X_TAG
                            ),
                            compound.getInt(
                                    HOME_Y_TAG
                            ),
                            compound.getInt(
                                    HOME_Z_TAG
                            )
                    );

            if (
                    level()
                            instanceof ServerLevel serverLevel
            ) {
                homeDimension =
                        serverLevel
                                .dimension()
                                .location()
                                .toString();

                restrictTo(
                        homePos,
                        Config.WORKER_HOME_RADIUS.get()
                );
            }
        }
    }

    /*
     * Tick
     */

    @Override
    public void tick() {
        super.tick();

        if (
                !(level()
                        instanceof ServerLevel serverLevel)
                        || ownerUUID == null
        ) {
            return;
        }

        WorkerManager.updateWorkerLocation(
                serverLevel.getServer(),
                ownerUUID,
                getUUID(),
                serverLevel
                        .dimension()
                        .location()
                        .toString(),
                blockPosition().getX() >> 4,
                blockPosition().getZ() >> 4
        );

        if (homePos != null) {
            restrictTo(
                    homePos,
                    Config.WORKER_HOME_RADIUS.get()
            );
        }

        if (
                homePos != null
                        && tickCount >= nextHomeReturnTick
        ) {
            returnHome();

            long intervalTicks =
                    Config
                            .WORKER_RETURN_INTERVAL_SECONDS
                            .get()
                            * 20L;

            nextHomeReturnTick =
                    tickCount
                            + intervalTicks;
        }
    }

    /*
     * Return to home location
     */

    private void returnHome() {
        if (homePos == null) {
            return;
        }

        double distanceSquared =
                distanceToSqr(
                        homePos.getX() + 0.5D,
                        homePos.getY(),
                        homePos.getZ() + 0.5D
                );

        if (distanceSquared < 9.0D) {
            return;
        }

        getNavigation().moveTo(
                homePos.getX() + 0.5D,
                homePos.getY(),
                homePos.getZ() + 0.5D,
                1.0D
        );
    }
}