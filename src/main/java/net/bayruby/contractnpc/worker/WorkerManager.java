package net.bayruby.contractnpc.worker;

import net.bayruby.contractnpc.entity.HumanoidMob;
import net.bayruby.contractnpc.entity.ModEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.UUID;

public final class WorkerManager {

    private static final String DATA_NAME =
            "contractnpc_workers";

    private WorkerManager() {
    }

    public static WorkerSavedData getData(
            MinecraftServer server
    ) {
        ServerLevel overworld =
                server.overworld();

        return overworld
                .getDataStorage()
                .computeIfAbsent(
                        new SavedData.Factory<>(
                                WorkerSavedData::new,
                                WorkerSavedData::load
                        ),
                        DATA_NAME
                );
    }

    public static void updateWorkerLocation(
            MinecraftServer server,
            UUID playerUuid,
            UUID workerUuid,
            String dimension,
            int chunkX,
            int chunkZ
    ) {
        getData(server)
                .updateWorkerLocation(
                        playerUuid,
                        workerUuid,
                        dimension,
                        chunkX,
                        chunkZ
                );
    }

    public static HumanoidMob getWorker(
            MinecraftServer server,
            UUID playerUuid
    ) {
        WorkerSavedData data =
                getData(server);

        WorkerSavedData.WorkerEntry entry =
                data.getWorker(
                        playerUuid
                );

        if (entry == null) {
            return null;
        }

        UUID workerUuid =
                entry.workerUuid();

        for (
                ServerLevel level :
                server.getAllLevels()
        ) {
            Entity entity =
                    level.getEntity(
                            workerUuid
                    );

            if (entity instanceof HumanoidMob worker) {

                if (
                        worker.getOwnerUUID() == null
                                || !worker.getOwnerUUID()
                                .equals(playerUuid)
                ) {
                    worker.setOwnerUUID(
                            playerUuid
                    );
                }

                return worker;
            }
        }

        ServerLevel workerLevel =
                getLevel(
                        server,
                        entry.dimension()
                );

        if (workerLevel == null) {
            return null;
        }

        workerLevel
                .getChunkSource()
                .getChunk(
                        entry.chunkX(),
                        entry.chunkZ(),
                        ChunkStatus.FULL,
                        true
                );

        Entity entity =
                workerLevel.getEntity(
                        workerUuid
                );

        if (entity instanceof HumanoidMob worker) {

            if (
                    worker.getOwnerUUID() == null
                            || !worker.getOwnerUUID()
                            .equals(playerUuid)
            ) {
                worker.setOwnerUUID(
                        playerUuid
                );
            }

            return worker;
        }

        return null;
    }

    private static ServerLevel getLevel(
            MinecraftServer server,
            String dimension
    ) {
        ResourceLocation location =
                ResourceLocation.tryParse(
                        dimension
                );

        if (location == null) {
            return null;
        }

        ResourceKey<Level> levelKey =
                ResourceKey.create(
                        Registries.DIMENSION,
                        location
                );

        return server.getLevel(
                levelKey
        );
    }

    private static HumanoidMob createWorker(
            ServerPlayer player,
            BlockPos spawnPos,
            BlockPos homePos,
            WorkerSavedData data,
            UUID workerUuid
    ) {
        ServerLevel level =
                player.serverLevel();

        HumanoidMob worker =
                ModEntities.HUMANOID_MOB
                        .get()
                        .create(level);

        if (worker == null) {
            return null;
        }

        worker.setUUID(
                workerUuid
        );

        worker.setOwnerUUID(
                player.getUUID()
        );

        worker.setHome(
                homePos,
                level
                        .dimension()
                        .location()
                        .toString()
        );

        worker.setCustomName(
                Component.literal(
                        "Worker"
                )
        );

        worker.setCustomNameVisible(
                true
        );

        worker.moveTo(
                spawnPos.getX() + 0.5D,
                spawnPos.getY(),
                spawnPos.getZ() + 0.5D,
                player.getYRot(),
                0.0F
        );

        level.addFreshEntity(
                worker
        );

        ChunkPos chunkPos =
                new ChunkPos(
                        spawnPos
                );

        String dimension =
                level
                        .dimension()
                        .location()
                        .toString();

        data.setWorker(
                player.getUUID(),
                worker.getUUID(),
                dimension,
                chunkPos.x,
                chunkPos.z,
                dimension,
                homePos.getX(),
                homePos.getY(),
                homePos.getZ()
        );

        return worker;
    }

    public static boolean recallWorker(
            ServerPlayer player,
            BlockPos spawnPos,
            BlockPos homePos
    ) {
        MinecraftServer server =
                player.getServer();

        if (server == null) {
            return false;
        }

        WorkerSavedData data =
                getData(server);

        WorkerSavedData.WorkerEntry entry =
                data.getWorker(
                        player.getUUID()
                );

        if (entry == null) {

            return createWorker(
                    player,
                    spawnPos,
                    homePos,
                    data,
                    UUID.randomUUID()
            ) != null;
        }

        HumanoidMob oldWorker =
                getWorker(
                        server,
                        player.getUUID()
                );

        if (oldWorker == null) {

            data.removeWorker(
                    player.getUUID()
            );

            return createWorker(
                    player,
                    spawnPos,
                    homePos,
                    data,
                    UUID.randomUUID()
            ) != null;
        }

        UUID workerUuid =
                oldWorker.getUUID();

        UUID ownerUuid =
                oldWorker.getOwnerUUID();

        if (ownerUuid == null) {
            ownerUuid =
                    player.getUUID();
        }

        oldWorker.remove(
                RemovalReason.DISCARDED
        );

        ServerLevel destination =
                player.serverLevel();

        HumanoidMob newWorker =
                ModEntities.HUMANOID_MOB
                        .get()
                        .create(destination);

        if (newWorker == null) {
            return false;
        }

        newWorker.setUUID(
                workerUuid
        );

        newWorker.setOwnerUUID(
                ownerUuid
        );

        newWorker.setHome(
                homePos,
                destination
                        .dimension()
                        .location()
                        .toString()
        );

        newWorker.setCustomName(
                Component.literal(
                        "Worker"
                )
        );

        newWorker.setCustomNameVisible(
                true
        );

        newWorker.moveTo(
                spawnPos.getX() + 0.5D,
                spawnPos.getY(),
                spawnPos.getZ() + 0.5D,
                player.getYRot(),
                0.0F
        );

        destination.addFreshEntity(
                newWorker
        );

        ChunkPos chunkPos =
                new ChunkPos(
                        spawnPos
                );

        String dimension =
                destination
                        .dimension()
                        .location()
                        .toString();

        data.setWorker(
                player.getUUID(),
                workerUuid,
                dimension,
                chunkPos.x,
                chunkPos.z,
                dimension,
                homePos.getX(),
                homePos.getY(),
                homePos.getZ()
        );

        return true;
    }
}