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

    public void updateBlockPos(String chunkPosString, String blockPosString, BlockPos blockPos) {
        this.chunksData.get(chunkPosString).getBlockContent(blockPosString).setBlockPos(blockPos);
        this.setDirty();
    }

    public void updateBlockColor(String chunkPosString, String blockPosString, int color) {
        this.chunksData.get(chunkPosString).getBlockContent(blockPosString).setBlockColor(color);
        this.setDirty();
    }

    public void setBlockContent(String chunkPosString, String blockPosString, ChunkMiniMapData.BlockContent blockContent) {
        this.chunksData.get(chunkPosString).setBlockContent(blockPosString, blockContent);
        this.setDirty();
    }

    public ChunkMiniMapData.BlockContent getBlockContent(String chunkPosString, String blockChunkPosString, Boolean modify) {
        ChunkMiniMapData chunkMiniMapData = this.chunksData.get(chunkPosString);
        if(chunkMiniMapData == null) return null;
        if(modify) this.setDirty();
        return chunkMiniMapData.getBlockContent(blockChunkPosString);
    }

    public void setChunkData(String chunkPosString, ChunkMiniMapData chunkData) {
        this.chunksData.replace(chunkPosString, chunkData);
        this.setDirty();
    }

    public ChunkMiniMapData getChunkData(String chunkPosString) {
        return this.chunksData.get(chunkPosString);
    }

    public void addChunk(String chunkPosString, ChunkMiniMapData chunkData) {
        this.chunksData.putIfAbsent(chunkPosString, chunkData);
        this.setDirty();
    }

    public Boolean chunkIsPresent(String chunkPosString) {
        return this.chunksData.containsKey(chunkPosString);
    }
}
