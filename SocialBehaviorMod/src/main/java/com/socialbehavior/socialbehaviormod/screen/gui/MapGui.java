package com.socialbehavior.socialbehaviormod.screen.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.socialbehavior.socialbehaviormod.minimap.MiniMapHandler;
import com.socialbehavior.socialbehaviormod.minimap.data.ChunkMiniMapData;
import com.socialbehavior.socialbehaviormod.minimap.data.MiniMapData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class MapGui extends ForgeIngameGui {
    private final MatrixStack matrixStack;
    private final ServerWorld serverWorld;
    private final MiniMapData miniMapData;

    public MapGui(Minecraft minecraft, MatrixStack matrixStack) {
        super(minecraft);
        this.matrixStack = matrixStack;
        assert minecraft.level != null;
        this.serverWorld = ServerLifecycleHooks.getCurrentServer().getLevel(minecraft.level.dimension());
        assert serverWorld != null;
        this.miniMapData = MiniMapData.getInstance(serverWorld);

        this.init();
    }

    private void init() {
        this.drawMap(new Point2D.Double(0, 12), 2, 1);
//        this.drawPlayerChunk(new Point2D.Double(0, 10), 4);

        this.drawPlayerCoords(new Point2D.Double(1, 1), 0);
    }

    private void drawPlayerChunk(Point2D startPoint, int blockPixelSize) {
        Chunk chunk = MiniMapHandler.getChunkPlayer();
        ChunkPos chunkPos = chunk.getPos();
        String chunkPosString = chunkPos.x + "," + chunkPos.z;

//        only add if not exist
        this.miniMapData.addChunk(chunkPosString, chunk);

        ChunkMiniMapData chunkMiniMapData = this.miniMapData.getChunkData(chunkPosString);
        Map<String, ChunkMiniMapData.BlockContent> mapChunkData = chunkMiniMapData.getChunkData();

        final int[][] arrayColor = new int[16][16];

        int xColor = 0;
        for (int x = 0; x < 16; x++) {
            int zColor = 0;
            for (int z = 0; z < 16; z++) {
                String blockPosString = x + "," + z;

                int color = mapChunkData.get(blockPosString).getBlockColor();

                arrayColor[xColor][zColor] = color;
                zColor++;
            }
            xColor++;
        }

        this.drawChunkMap(arrayColor, startPoint, blockPixelSize);
    }

    /*
        private void drawMap(Point2D pointStart, int radius, int blockPixelSize) {
            Map<ChunkPos, Color[][]> map = MiniMapHandler.createMap(radius);
            ChunkPos chunkPlayerPosition = MiniMapHandler.getChunkPlayerPosition();
            int xMap = 0;
            for (int chunkX = -radius; chunkX <= radius; chunkX++) {
                int yMap = 0;
                for (int chunkZ = -radius; chunkZ <= radius; chunkZ++) {
                    Chunk chunk = MiniMapHandler.getWorld().getChunk(chunkPlayerPosition.x + chunkX, chunkPlayerPosition.z + chunkZ);
                    Color[][] colorMap = map.get(chunk.getPos());

                    int xPointStart = (int) pointStart.getX();
                    int yPointStart = (int) pointStart.getY();

                    Point2D point = new Point2D.Double((xMap * 16 * blockPixelSize) + xPointStart, (yMap * 16 * blockPixelSize) + yPointStart);
                    this.drawChunkMap(colorMap, point, blockPixelSize);
                    yMap++;
                }
                xMap++;
            }
        }
    */
    private void drawMap(Point2D pointStart, int radius, int blockPixelSize) {
        Map<ChunkPos, Color[][]> map = MiniMapHandler.createMap(radius);
        ChunkPos chunkPlayerPosition = MiniMapHandler.getChunkPlayerPosition();
        int xMap = 0;
        for (int chunkX = -radius; chunkX <= radius; chunkX++) {
            int yMap = 0;
            for (int chunkZ = -radius; chunkZ <= radius; chunkZ++) {
                Chunk chunk = MiniMapHandler.getWorld().getChunk(chunkPlayerPosition.x + chunkX, chunkPlayerPosition.z + chunkZ);

                ChunkPos chunkPos = chunk.getPos();
                String chunkPosString = chunkPos.x + "," + chunkPos.z;

//        only add if not exist
                this.miniMapData.addChunk(chunkPosString, chunk);

                ChunkMiniMapData chunkMiniMapData = this.miniMapData.getChunkData(chunkPosString);
                Map<String, ChunkMiniMapData.BlockContent> mapChunkData = chunkMiniMapData.getChunkData();

                final int[][] arrayColor = new int[16][16];

                int xColor = 0;
                for (int x = 0; x < 16; x++) {
                    int zColor = 0;
                    for (int z = 0; z < 16; z++) {
                        String blockPosString = x + "," + z;

                        int color = mapChunkData.get(blockPosString).getBlockColor();

                        arrayColor[xColor][zColor] = color;
                        zColor++;
                    }
                    xColor++;
                }

                int xPointStart = (int) pointStart.getX();
                int yPointStart = (int) pointStart.getY();

                Point2D point = new Point2D.Double((xMap * 16 * blockPixelSize) + xPointStart, (yMap * 16 * blockPixelSize) + yPointStart);
                this.drawChunkMap(arrayColor, point, blockPixelSize);
                yMap++;
            }
            xMap++;
        }
    }

    private void drawChunkMap(Color[][] arrayColor, Point2D startPoint, int blockPixelSize) {
        int xColor = 0;
        for (Color[] colors : arrayColor) {
            int zColor = 0;
            for (Color color : colors) {
                if (color == null) continue;
                AbstractGui.fill(this.matrixStack,
                        blockPixelSize * xColor + (int) startPoint.getX(),
                        blockPixelSize * zColor + (int) startPoint.getY(),
                        blockPixelSize + (blockPixelSize * xColor + (int) startPoint.getX()),
                        blockPixelSize + (blockPixelSize * zColor + (int) startPoint.getY()),
                        color.getRGB());
                zColor++;
            }
            xColor++;
        }
    }

    private void drawChunkMap(int[][] arrayColor, Point2D startPoint, int blockPixelSize) {
        int xColor = 0;
        for (int[] colors : arrayColor) {
            int zColor = 0;
            for (int color : colors) {
                AbstractGui.fill(this.matrixStack,
                        blockPixelSize * xColor + (int) startPoint.getX(),
                        blockPixelSize * zColor + (int) startPoint.getY(),
                        blockPixelSize + (blockPixelSize * xColor + (int) startPoint.getX()),
                        blockPixelSize + (blockPixelSize * zColor + (int) startPoint.getY()),
                        color);
                zColor++;
            }
            xColor++;
        }
    }

    private void drawPlayerCoords(Point2D startPoint, int shadow) {
        StringTextComponent textComponent = new StringTextComponent("");
        Vector3d playerPosition = MiniMapHandler.getPlayerPosition();
        textComponent.append(new StringTextComponent(Integer.toString((int) playerPosition.x())).withStyle(TextFormatting.RED));
        textComponent.append(new StringTextComponent(", ").withStyle(TextFormatting.WHITE));
        textComponent.append(new StringTextComponent(Integer.toString((int) playerPosition.y())).withStyle(TextFormatting.BLUE));
        textComponent.append(new StringTextComponent(", ").withStyle(TextFormatting.WHITE));
        textComponent.append(new StringTextComponent(Integer.toString((int) playerPosition.z())).withStyle(TextFormatting.GREEN));

        this.getFont().drawShadow(this.matrixStack, textComponent, (float) startPoint.getX(), (float) startPoint.getY(), shadow);
    }
}
