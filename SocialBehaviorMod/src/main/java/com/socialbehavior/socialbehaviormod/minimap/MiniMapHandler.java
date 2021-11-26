package com.socialbehavior.socialbehaviormod.minimap;

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

public class MiniMapHandler {
    public static Map<String, Integer> minecraftMapBlockColor = ForgeRegistries.BLOCKS.getValues()
            .stream()
            .distinct()
            .filter(block -> block.defaultMaterialColor().col != 0)
            .collect(Collectors.toMap(
                    Block::getDescriptionId,
                    block -> new Color(block.defaultMaterialColor().col, false).getRGB(),
                    (a1, a2) -> a2));
    private static MiniMapHandler instance;
    private final Minecraft minecraft;

    public MiniMapHandler() {
        instance = this;
        this.minecraft = Minecraft.getInstance();
    }

    public static MiniMapHandler getInstance() {
        if (instance == null) new MiniMapHandler();
        return instance;
    }

    public World getWorld() {
        return this.minecraft.level;
    }

    public ClientPlayerEntity getPlayer() {
        assert this.minecraft.player != null;
        return this.minecraft.player;
    }

    public Vector3d getPlayerPosition() {
        return this.getPlayer().position();
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

    private BlockState getCorrectStateForFluidBlock(BlockState blockState, BlockPos blockPos) {
        FluidState fluidstate = blockState.getFluidState();
        return !fluidstate.isEmpty() && !blockState.isFaceSturdy(this.getWorld(), blockPos, Direction.UP) ? fluidstate.createLegacyBlock() : blockState;
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
        if (this.getWorld().dimension() != this.getPlayer().level.dimension()) return null;

        final Color[][] arrayColor = new Color[16][16];

        int xColor = 0;
        for (int x = 0; x < 16; x++) {
            int zColor = 0;
            for (int z = 0; z < 16; z++) {
                final int xChunkPos = (chunk.getPos().x * 16) + x;
                final int zChunkPos = (chunk.getPos().z * 16) + z;
                final int height = chunk.getHeight(Heightmap.Type.WORLD_SURFACE, xChunkPos, zChunkPos);

                BlockState blockState;
                final BlockPos blockPos = new BlockPos(xChunkPos, height, zChunkPos);
                if (height <= 1) {
                    blockState = Blocks.BEDROCK.defaultBlockState();
                } else {
                    blockState = this.getWorld().getBlockState(blockPos);
                    if (!blockState.getFluidState().isEmpty()) {
                        blockState = this.getCorrectStateForFluidBlock(blockState, blockPos);
                    }
                }
                final MaterialColor materialColor = blockState.getMapColor(chunk, blockPos);

                final Color color = new Color(materialColor.col, false);
                arrayColor[xColor][zColor] = color;
                zColor++;
            }
            xColor++;
        }

        return arrayColor;
    }
}


/*
    public static void saveBlockColorData(File file, Map<String, Integer> mapBlockColor) throws IOException {
        CompoundNBT colorData = new CompoundNBT();
        for (Map.Entry<String, Integer> blockColor : mapBlockColor.entrySet()) {
            colorData.putInt(blockColor.getKey(), blockColor.getValue());
        }
        CompressedStreamTools.writeCompressed(colorData, file);
    }

    public static Map<String, Integer> getBlockColorDate(File file) throws IOException {
        Map<String, Integer> mapBlockColor = new HashMap<>();
        CompoundNBT pip = CompressedStreamTools.readCompressed(file);

        for (String blockString : pip.getAllKeys()) {
            mapBlockColor.put(blockString, pip.getInt(blockString));
        }

        return mapBlockColor;
    }
*/
