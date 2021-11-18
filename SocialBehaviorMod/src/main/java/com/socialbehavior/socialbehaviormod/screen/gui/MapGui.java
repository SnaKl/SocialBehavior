package com.socialbehavior.socialbehaviormod.screen.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.socialbehavior.socialbehaviormod.SocialBehaviorMod;
import com.socialbehavior.socialbehaviormod.map.MapHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.ForgeIngameGui;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class MapGui extends ForgeIngameGui {
    private final MatrixStack matrixStack;
    private final MapHandler mapHandler;

    public MapGui(Minecraft minecraft, MatrixStack matrixStack) {
        super(minecraft);
        this.matrixStack = matrixStack;
        this.mapHandler = MapHandler.getInstance();

        this.init();
    }

    private void init() {
        this.drawMap(new Point2D.Double(0, 12), 2, 1);
//        this.drawPlayerChunk(new Point2D.Double(0,10), 4);

        this.drawPlayerCoords(new Point2D.Double(1, 1), 0);
    }

    private void drawPlayerChunk(Point2D startPoint, int blockPixelSize) {
        final Color[][] mapColor = this.mapHandler.getChunkColorMap(this.mapHandler.getChunkPlayer());
        this.drawChunkMap(mapColor, startPoint, blockPixelSize);
    }

    private void drawMap(Point2D pointStart, int radius, int blockPixelSize) {
        Map<ChunkPos, Color[][]> map = this.mapHandler.createMap(radius);
        ChunkPos chunkPlayerPosition = this.mapHandler.getChunkPlayerPosition();
        int xMap = 0;
        for (int chunkX = -radius; chunkX <= radius; chunkX++) {
            int yMap = 0;
            for (int chunkZ = -radius; chunkZ <= radius; chunkZ++) {
                Chunk chunk = this.mapHandler.getWorld().getChunk(chunkPlayerPosition.x + chunkX, chunkPlayerPosition.z + chunkZ);
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

    private void drawPlayerCoords(Point2D startPoint, int shadow) {
        StringTextComponent textComponent = new StringTextComponent("");
        textComponent.append(new StringTextComponent(Integer.toString((int) this.mapHandler.getPlayerPosition().x())).withStyle(TextFormatting.RED));
        textComponent.append(new StringTextComponent(", ").withStyle(TextFormatting.WHITE));
        textComponent.append(new StringTextComponent(Integer.toString((int) this.mapHandler.getPlayerPosition().y())).withStyle(TextFormatting.BLUE));
        textComponent.append(new StringTextComponent(", ").withStyle(TextFormatting.WHITE));
        textComponent.append(new StringTextComponent(Integer.toString((int) this.mapHandler.getPlayerPosition().z())).withStyle(TextFormatting.GREEN));

        this.getFont().drawShadow(this.matrixStack, textComponent, (float) startPoint.getX(), (float) startPoint.getY(), shadow);
    }
}
