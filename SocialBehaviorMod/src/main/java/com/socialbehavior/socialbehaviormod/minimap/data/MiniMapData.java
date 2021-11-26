package com.socialbehavior.socialbehaviormod.minimap.data;

import com.socialbehavior.socialbehaviormod.SocialBehaviorMod;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import java.util.HashMap;
import java.util.Map;

/*
    dimension : "minecraft:overworld"
    Chunks:{
        "00":{
            "00":{
                Pos:{X,Y,Z}
                color:-1234
            }
            ...
        "01":{
            "00":{
                Pos:{X,Y,Z}
                color:-1234
            }
            ...
        "10": ChunkMiniMapData
    }
 */
public class MiniMapData extends WorldSavedData {
    public static final String NAME_FILE = (SocialBehaviorMod.MOD_ID + "_map");
    public final Map<String, ChunkMiniMapData> chunksData = new HashMap<>();
    public RegistryKey<World> dimension;

    public MiniMapData() {
        super(NAME_FILE);
    }

    public MiniMapData(String nameFile) {
        super(nameFile);
    }

    public static MiniMapData getInstance(ServerWorld world) {
        DimensionSavedDataManager dimensionSavedDataManager = world.getDataStorage();
        MiniMapData miniMapData = dimensionSavedDataManager.computeIfAbsent(MiniMapData::new, MiniMapData.NAME_FILE);
        miniMapData.setDimension(world.dimension());
        return miniMapData;
    }

    public void setDimension(RegistryKey<World> dimension) {
        this.dimension = dimension;
        this.setDirty();
    }

    @Override
    public void load(CompoundNBT compoundNBT) {
        if (compoundNBT.contains("Chunks")) {
            CompoundNBT chunks = compoundNBT.getCompound("Chunks");
            for (String chunkPos : chunks.getAllKeys()) {
                ChunkMiniMapData chunkMiniMapData = ChunkMiniMapData.load(chunks.getCompound(chunkPos));
                this.chunksData.put(chunkPos, chunkMiniMapData);
            }
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compoundNBT) {
        compoundNBT.putString("dimension", this.dimension.location().toString());

        CompoundNBT chunks = new CompoundNBT();
        for (Map.Entry<String, ChunkMiniMapData> chunk : this.chunksData.entrySet()) {
            ChunkMiniMapData chunkMiniMapData = chunk.getValue();
            chunks.put(chunk.getKey(), chunkMiniMapData.save());
        }

        compoundNBT.put("Chunks", chunks);

        return compoundNBT;
    }

    public void updateBlockPos(String chunk, String block, BlockPos blockPos) {
        this.chunksData.get(chunk).getBlockContent(block).setBlockPos(blockPos);
        this.setDirty();
    }

    public void updateBlockColor(String chunk, String block, int color) {
        this.chunksData.get(chunk).getBlockContent(block).setBlockColor(color);
        this.setDirty();
    }

    public void setBlockContent(String chunk, String block, ChunkMiniMapData.BlockContent blockContent) {
        this.chunksData.get(chunk).setBlockContent(block, blockContent);
        this.setDirty();
    }

    public ChunkMiniMapData.BlockContent getBlockContent(String chunk, String block) {
        return this.chunksData.get(block).getBlockContent(block);
    }

    public void setChunkData(String chunk, ChunkMiniMapData chunkData) {
        this.chunksData.replace(chunk, chunkData);
        this.setDirty();
    }

    public ChunkMiniMapData getChunkData(String chunk) {
        return this.chunksData.get(chunk);
    }

    public void addChunk(String chunk, ChunkMiniMapData chunkData) {
        this.chunksData.putIfAbsent(chunk, chunkData);
        this.setDirty();
    }

    public Boolean chunkIsPresent(String chunk) {
        return this.chunksData.containsKey(chunk);
    }
}
