package com.socialbehavior.socialbehaviormod.minimap.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/*
    "00":{
        Pos:{X,Y,Z}
        color:-1234
    }
}
*/
public class ChunkMiniMapData {
    private final Map<String, BlockContent> chunkData;

    public ChunkMiniMapData(Map<String, BlockContent> chunk) {
        this.chunkData = chunk;
    }

    public static ChunkMiniMapData load(CompoundNBT compoundNBT) {
        Map<String, BlockContent> chunk = new HashMap<>();

        for (String blockPosInChunk : compoundNBT.getAllKeys()) {
            CompoundNBT block = compoundNBT.getCompound(blockPosInChunk);

            BlockPos blockPos = NBTUtil.readBlockPos(block.getCompound("Pos"));
            int blockColor = block.getInt("Color");
            BlockContent blockContent = new BlockContent(blockPos, blockColor);
            chunk.put(blockPosInChunk, blockContent);
        }

        return new ChunkMiniMapData(chunk);
    }

    public CompoundNBT save() {
        CompoundNBT compoundnbt = new CompoundNBT();
        for (Map.Entry<String, BlockContent> block : this.chunkData.entrySet()) {
            BlockContent blockContent = block.getValue();

            CompoundNBT content = new CompoundNBT();
            content.putInt("Color", blockContent.getBlockColor());
            content.put("Pos", NBTUtil.writeBlockPos(blockContent.getBlockPos()));

            compoundnbt.put(block.getKey(), content);
        }

        return compoundnbt;
    }

    public static ChunkMiniMapData createChunkMiniMapData(String chunkPos, Color[][] colorArray){

    }

//    public Map<String, BlockContent> getChunkData() {
//        return chunkData;
//    }

    public void setBlockContent(String block, BlockContent blockContent) {
        this.chunkData.replace(block, blockContent);
    }

    public BlockContent getBlockContent(String block) {
        return this.chunkData.get(block);
    }

    public static class BlockContent {
        private BlockPos blockPos;
        private int blockColor;

        public BlockContent(BlockPos blockPos, int blockColor) {
            this.blockPos = blockPos;
            this.blockColor = blockColor;
        }

        public BlockPos getBlockPos() {
            return blockPos;
        }

        public void setBlockPos(BlockPos blockPos) {
            this.blockPos = blockPos;
        }

        public int getBlockColor() {
            return blockColor;
        }

        public void setBlockColor(int blockColor) {
            this.blockColor = blockColor;
        }
    }
}
