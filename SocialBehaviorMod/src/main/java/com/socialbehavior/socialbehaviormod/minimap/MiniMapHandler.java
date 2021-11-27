package com.socialbehavior.socialbehaviormod.minimap;

import com.socialbehavior.socialbehaviormod.minimap.data.ChunkMiniMapData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class MiniMapHandler {
    private static final Minecraft minecraft = Minecraft.getInstance();

/*    public static Map<String, Integer> minecraftMapBlockColor = ForgeRegistries.BLOCKS.getValues()
            .stream()
            .distinct()
            .filter(block -> block.defaultMaterialColor().col != 0)
            .collect(Collectors.toMap(
                    Block::getDescriptionId,
                    block -> new Color(block.defaultMaterialColor().col, false).getRGB(),
                    (a1, a2) -> a2));
                    */

    public static int getWorldBlockPos(int chunkPos, int blockPosInChunk) {
        return (chunkPos * 16) + blockPosInChunk;
    }

    public static int getBlockPosInChunk(int chunkPos, int blockPosInWorld) {
        return blockPosInWorld - (chunkPos * 16);
    }

    public static int getTopBlockPosition(Chunk chunk, int xBlockPosInChunk, int zBlockPosInChunk) {
        return chunk.getHeight(Heightmap.Type.WORLD_SURFACE, xBlockPosInChunk, zBlockPosInChunk);
    }

    public static Color getBlockColor(Chunk chunk, BlockPos blockPos) {
        BlockState blockState;
        if (blockPos.getY() <= 1) {
            blockState = Blocks.BEDROCK.defaultBlockState();
        } else {
            blockState = getWorld().getBlockState(blockPos);
            if (!blockState.getFluidState().isEmpty()) {
                blockState = getCorrectStateForFluidBlock(blockState, blockPos);
            }
        }
        final MaterialColor materialColor = blockState.getMapColor(chunk, blockPos);
        return new Color(materialColor.col, false);
    }

    public static World getWorld() {
        World world = minecraft.level;
        assert world != null;
        return world;
    }

    public static BlockState getCorrectStateForFluidBlock(BlockState blockState, BlockPos blockPos) {
        FluidState fluidstate = blockState.getFluidState();
        return !fluidstate.isEmpty() && !blockState.isFaceSturdy(getWorld(), blockPos, Direction.UP) ? fluidstate.createLegacyBlock() : blockState;
    }

    public static ChunkMiniMapData getChunkData(Chunk chunk) {
        Map<String, ChunkMiniMapData.BlockContent> chunkData = new HashMap<>();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int y = getTopBlockPosition(chunk, x, z);
                BlockPos blockPos = new BlockPos(x, y, z);
                int color = getBlockColor(chunk, blockPos).getRGB();

                ChunkMiniMapData.BlockContent blockContent = new ChunkMiniMapData.BlockContent(blockPos, color);
                chunkData.put(Integer.toString(x) + "," + Integer.toString(z), blockContent);
            }
        }

        return new ChunkMiniMapData(chunkData);
    }

    public static ClientPlayerEntity getPlayer() {
        assert minecraft.player != null;
        return minecraft.player;
    }

    public static Vector3d getPlayerPosition() {
        return getPlayer().position();
    }

    public static BlockPos getBlockPlayerPosition() {
        return new BlockPos(getPlayerPosition());
    }

    public static Chunk getChunkPlayer() {
        return getWorld().getChunkAt(getBlockPlayerPosition());
    }

    public static ChunkPos getChunkPlayerPosition() {
        return getChunkPlayer().getPos();
    }

    public static Map<ChunkPos, Color[][]> createMap(int radius) {
        Map<ChunkPos, Color[][]> mapChunkColorMap = new HashMap<>();
        ChunkPos chunkPlayerPosition = getChunkPlayerPosition();

        for (int chunkX = -radius; chunkX <= radius; chunkX++) {
            for (int chunkZ = -radius; chunkZ <= radius; chunkZ++) {
                Chunk chunk = getWorld().getChunk(chunkPlayerPosition.x + chunkX, chunkPlayerPosition.z + chunkZ);
                Color[][] chunkColorMap = getChunkColorMap(chunk);
                mapChunkColorMap.put(chunk.getPos(), chunkColorMap);
            }
        }

        return mapChunkColorMap;
    }

    public static Color[][] getChunkColorMap(Chunk chunk) {
        if (getWorld().dimension() != getPlayer().level.dimension()) return null;

        final Color[][] arrayColor = new Color[16][16];
        ChunkPos chunkPos = chunk.getPos();

        int xColor = 0;
        for (int x = 0; x < 16; x++) {
            int zColor = 0;
            for (int z = 0; z < 16; z++) {
                int xWorldBlockPos = getWorldBlockPos(chunkPos.x, x);
                int zWorldBlockPos = getWorldBlockPos(chunkPos.z, z);
                int yWorldBlockPos = getTopBlockPosition(chunk, xWorldBlockPos, zWorldBlockPos);
                BlockPos blockPos = new BlockPos(xWorldBlockPos, yWorldBlockPos, zWorldBlockPos);
                Color color = getBlockColor(chunk, blockPos);

                arrayColor[xColor][zColor] = color;
                zColor++;
            }
            xColor++;
        }

        return arrayColor;
    }
}