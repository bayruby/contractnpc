package net.bayruby.contractnpc.worker;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WorkerSavedData extends SavedData {

    private static final String DATA_KEY =
            "workers";

    private final Map<UUID, WorkerEntry> workers =
            new HashMap<>();

    public WorkerSavedData() {
    }

    public static WorkerSavedData load(
            CompoundTag tag,
            HolderLookup.Provider registries
    ) {
        WorkerSavedData data =
                new WorkerSavedData();

        if (!tag.contains(DATA_KEY)) {
            return data;
        }

        CompoundTag workersTag =
                tag.getCompound(DATA_KEY);

        for (String playerUuidString :
                workersTag.getAllKeys()) {

            try {
                UUID playerUuid =
                        UUID.fromString(
                                playerUuidString
                        );

                CompoundTag workerTag =
                        workersTag.getCompound(
                                playerUuidString
                        );

                if (!workerTag.hasUUID("WorkerUUID")) {
                    continue;
                }

                UUID workerUuid =
                        workerTag.getUUID(
                                "WorkerUUID"
                        );

                String dimension =
                        workerTag.getString(
                                "Dimension"
                        );

                int chunkX =
                        workerTag.getInt(
                                "ChunkX"
                        );

                int chunkZ =
                        workerTag.getInt(
                                "ChunkZ"
                        );

                String homeDimension =
                        workerTag.getString(
                                "HomeDimension"
                        );

                int homeX =
                        workerTag.getInt(
                                "HomeX"
                        );

                int homeY =
                        workerTag.getInt(
                                "HomeY"
                        );

                int homeZ =
                        workerTag.getInt(
                                "HomeZ"
                        );

                data.workers.put(
                        playerUuid,
                        new WorkerEntry(
                                workerUuid,
                                dimension,
                                chunkX,
                                chunkZ,
                                homeDimension,
                                homeX,
                                homeY,
                                homeZ
                        )
                );

            } catch (IllegalArgumentException ignored) {
                // Ignore invalid UUID entries.
            }
        }

        return data;
    }

    @Override
    public CompoundTag save(
            CompoundTag tag,
            HolderLookup.Provider registries
    ) {
        CompoundTag workersTag =
                new CompoundTag();

        for (
                Map.Entry<UUID, WorkerEntry> entry :
                workers.entrySet()
        ) {
            WorkerEntry worker =
                    entry.getValue();

            CompoundTag workerTag =
                    new CompoundTag();

            workerTag.putUUID(
                    "WorkerUUID",
                    worker.workerUuid()
            );

            workerTag.putString(
                    "Dimension",
                    worker.dimension()
            );

            workerTag.putInt(
                    "ChunkX",
                    worker.chunkX()
            );

            workerTag.putInt(
                    "ChunkZ",
                    worker.chunkZ()
            );

            workerTag.putString(
                    "HomeDimension",
                    worker.homeDimension()
            );

            workerTag.putInt(
                    "HomeX",
                    worker.homeX()
            );

            workerTag.putInt(
                    "HomeY",
                    worker.homeY()
            );

            workerTag.putInt(
                    "HomeZ",
                    worker.homeZ()
            );

            workersTag.put(
                    entry.getKey().toString(),
                    workerTag
            );
        }

        tag.put(
                DATA_KEY,
                workersTag
        );

        return tag;
    }

    public WorkerEntry getWorker(
            UUID playerUuid
    ) {
        return workers.get(
                playerUuid
        );
    }

    public void setWorker(
            UUID playerUuid,
            UUID workerUuid,
            String dimension,
            int chunkX,
            int chunkZ,
            String homeDimension,
            int homeX,
            int homeY,
            int homeZ
    ) {
        workers.put(
                playerUuid,
                new WorkerEntry(
                        workerUuid,
                        dimension,
                        chunkX,
                        chunkZ,
                        homeDimension,
                        homeX,
                        homeY,
                        homeZ
                )
        );

        setDirty();
    }

    public void updateWorkerLocation(
            UUID playerUuid,
            UUID workerUuid,
            String dimension,
            int chunkX,
            int chunkZ
    ) {
        WorkerEntry existing =
                workers.get(
                        playerUuid
                );

        if (existing == null) {
            return;
        }

        if (
                !existing.workerUuid()
                        .equals(workerUuid)
                        || !existing.dimension()
                        .equals(dimension)
                        || existing.chunkX() != chunkX
                        || existing.chunkZ() != chunkZ
        ) {
            workers.put(
                    playerUuid,
                    new WorkerEntry(
                            workerUuid,
                            dimension,
                            chunkX,
                            chunkZ,
                            existing.homeDimension(),
                            existing.homeX(),
                            existing.homeY(),
                            existing.homeZ()
                    )
            );

            setDirty();
        }
    }

    public void removeWorker(
            UUID playerUuid
    ) {
        if (
                workers.remove(
                        playerUuid
                ) != null
        ) {
            setDirty();
        }
    }

    public boolean hasWorker(
            UUID playerUuid
    ) {
        return workers.containsKey(
                playerUuid
        );
    }

    public record WorkerEntry(
            UUID workerUuid,
            String dimension,
            int chunkX,
            int chunkZ,
            String homeDimension,
            int homeX,
            int homeY,
            int homeZ
    ) {
    }
}