package com.socialbehavior.socialbehaviormod.events;

import com.socialbehavior.socialbehaviormod.SocialBehaviorMod;
import com.socialbehavior.socialbehaviormod.minimap.MiniMapHandler;
import com.socialbehavior.socialbehaviormod.minimap.data.ChunkMiniMapData;
import com.socialbehavior.socialbehaviormod.minimap.data.MiniMapData;
import com.socialbehavior.socialbehaviormod.screen.gui.MapGui;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;

@Mod.EventBusSubscriber(modid = SocialBehaviorMod.MOD_ID)
public class ModEvents {
    static Minecraft minecraft = Minecraft.getInstance();

    @SubscribeEvent
    public static void renderGUI(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            new MapGui(minecraft, event.getMatrixStack());
        }
    }

    @SubscribeEvent
    public static void onWorldSaved(WorldEvent.Save event) {
/*
        if (!event.getWorld().isClientSide()  && event.getWorld() instanceof ServerWorld)
        {
            ServerWorld world = (ServerWorld) event.getWorld();
            MiniMapData miniMapData = MiniMapData.getInstance(world);
            miniMapData.setDirty();
        }
        */
    }

    @SubscribeEvent
    public static void onWorldLoaded(final WorldEvent.Load event) {
        if (!event.getWorld().isClientSide() && event.getWorld() instanceof ServerWorld) {
            ServerWorld world = (ServerWorld) event.getWorld();
            MiniMapData miniMapData = MiniMapData.getInstance(world);
        }
    }

    @SubscribeEvent
    public static void onChunkLoad(final ChunkDataEvent.Load event){
        if(MiniMapHandler.getWorld() == null) return;
        ServerWorld world = (ServerWorld)event.getChunk().getWorldForge();

        if (world == null) return;
        MiniMapData miniMapData = MiniMapData.getInstance(world);

        ChunkPos chunkPos = event.getChunk().getPos();
        String chunkPosString = Integer.toString(chunkPos.x) + "," + Integer.toString(chunkPos.z);
        if (!miniMapData.chunkIsPresent(chunkPosString)) {
            ChunkMiniMapData chunkMiniMapData = MiniMapHandler.getChunkData((Chunk) event.getChunk());
            miniMapData.addChunk(chunkPosString, chunkMiniMapData);
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        ServerWorld world = (ServerWorld) event.getWorld();
        BlockPos blockPos = event.getPos();
        Chunk chunk = MiniMapHandler.getWorld().getChunkAt(blockPos);

        int yTopBlockPos = MiniMapHandler.getTopBlockPosition(chunk, blockPos.getX(), blockPos.getZ());

        if (yTopBlockPos != blockPos.getY()) return;

        ChunkPos chunkPos = chunk.getPos();
        BlockState blockState = event.getState();
        final MaterialColor materialColor = blockState.getMapColor(chunk, blockPos);
        final int color = new Color(materialColor.col, false).getRGB();

        final int blockPosX = MiniMapHandler.getBlockPosInChunk(chunkPos.x, blockPos.getX());
        final int blockPosZ = MiniMapHandler.getBlockPosInChunk(chunkPos.z, blockPos.getZ());

        String chunkPosString = Integer.toString(chunkPos.x) + "," + Integer.toString(chunkPos.z);
        String blockPosString = Integer.toString(blockPosX) + "," + Integer.toString(blockPosZ);
        MiniMapData miniMapData = MiniMapData.getInstance(world);

        ChunkMiniMapData.BlockContent blockContent = miniMapData.getBlockContent(chunkPosString, blockPosString, true);
        if (blockContent == null) return;

        if (blockContent.getBlockPos() != blockPos) {
            blockContent.setBlockPos(blockPos);
        }
        if (blockContent.getBlockColor() != color) {
            blockContent.setBlockColor(color);
        }

        SocialBehaviorMod.LOGGER.info("TTTESSTT");
    }

    @SubscribeEvent
    public static void onBlockMultiPlace(BlockEvent.EntityMultiPlaceEvent event) {
//      multi placement block (ex bed)
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
//      placement block
    }

/*
    @SubscribeEvent
    public static void onKeyEvent(InputEvent.KeyInputEvent event) {
        if(event.getKey() == 80) Minecraft.getInstance().setScreen(new OverlayGui(Minecraft.getInstance()));
    }
*/
/*
    @SubscribeEvent
    public static void playerJoinWorldEvent(EntityJoinWorldEvent event){
        if(event.getEntity() instanceof ClientPlayerEntity){
            MiniMapHandler.getInstance().initMapHandler();
        }
    }
*/
}
