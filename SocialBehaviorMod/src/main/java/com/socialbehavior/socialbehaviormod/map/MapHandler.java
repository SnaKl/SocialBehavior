package com.socialbehavior.socialbehaviormod.map;

import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MapHandler {
    private static MapHandler instance;
    private final Minecraft minecraft;

    public MapHandler() {
        instance = this;
        this.minecraft = Minecraft.getInstance();
    }

    public static MapHandler getInstance() {
        if (instance == null) new MapHandler();
        return instance;
    }

    public World getWorld() {
        return this.minecraft.level;
    }

    public Vector3d getPlayerPosition() {
        assert this.minecraft.player != null;
        return this.minecraft.player.position();
    }

    public BlockPos getBlockPlayerPosition() {
        return new BlockPos(this.getPlayerPosition());
    }

    public Chunk getChunkPlayer() {
        return this.getWorld().getChunkAt(this.getBlockPlayerPosition());
    }

    public ChunkPos getChunkPlayerPosition() {
        return this.getChunkPlayer().getPos();
    }

    public Map<ChunkPos, Color[][]> createMap(int radius) {
        Map<ChunkPos, Color[][]> mapChunkColorMap = new HashMap<>();
        ChunkPos chunkPlayerPosition = this.getChunkPlayerPosition();

        for (int chunkX = -radius; chunkX <= radius; chunkX++) {
            for (int chunkZ = -radius; chunkZ <= radius; chunkZ++) {
                Chunk chunk = this.getWorld().getChunk(chunkPlayerPosition.x + chunkX, chunkPlayerPosition.z + chunkZ);
                Color[][] chunkColorMap = this.getChunkColorMap(chunk);
                mapChunkColorMap.put(chunk.getPos(), chunkColorMap);
            }
        }

        return mapChunkColorMap;
    }

    public Color[][] getChunkColorMap(Chunk chunk) {
        final Color[][] arrayColor = new Color[16][16];

        int xColor = 0;
        for (int x = 0; x < 16; x++) {
            int zColor = 0;
            for (int z = 0; z < 16; z++) {
                final int xChunkPos = (chunk.getPos().x * 16) + x;
                final int zChunkPos = (chunk.getPos().z * 16) + z;
                final int y = chunk.getHeight(Heightmap.Type.WORLD_SURFACE, xChunkPos, zChunkPos);

                final BlockPos blockPos = new BlockPos(xChunkPos, y, zChunkPos);
                final MaterialColor materialColor = this.getWorld().getBlockState(blockPos).getMapColor(chunk, blockPos);

                final Color color = new Color(materialColor.col, false);
                arrayColor[xColor][zColor] = color;
                zColor++;
            }
            xColor++;
        }

        return arrayColor;
    }
}
