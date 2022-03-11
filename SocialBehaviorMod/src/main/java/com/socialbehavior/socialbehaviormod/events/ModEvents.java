package com.socialbehavior.socialbehaviormod.events;

import com.socialbehavior.socialbehaviormod.SocialBehaviorMod;
import com.socialbehavior.socialbehaviormod.minimap.MiniMapHandler;
import com.socialbehavior.socialbehaviormod.minimap.data.ChunkMiniMapData;
import com.socialbehavior.socialbehaviormod.minimap.data.MiniMapData;
import com.socialbehavior.socialbehaviormod.screen.gui.MapGui;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
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

    }

    @SubscribeEvent
    public static void onWorldLoaded(final WorldEvent.Load event) {
        if (!event.getWorld().isClientSide() && event.getWorld() instanceof ServerWorld) {
            ServerWorld world = (ServerWorld) event.getWorld();
            MiniMapData.getInstance(world);
        }
    }

    @SubscribeEvent
    public static void onChunkLoad(final ChunkDataEvent.Load event) {
        if (MiniMapHandler.getWorld() == null) return;
        ServerWorld world = (ServerWorld) event.getChunk().getWorldForge();

        if (world == null) return;
        MiniMapData miniMapData = MiniMapData.getInstance(world);

        ChunkPos chunkPos = event.getChunk().getPos();
        String chunkPosString = chunkPos.x + "," + chunkPos.z;

        miniMapData.addChunk(chunkPosString, (Chunk) event.getChunk());
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        MiniMapHandler.updateBlockInMiniMap((ServerWorld) event.getWorld(), event.getPos());
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        MiniMapHandler.updateBlockInMiniMap((ServerWorld) event.getWorld(), event.getPos());
    }

    @SubscribeEvent
    public static void test(EntityJoinWorldEvent event) {
        SocialBehaviorMod.LOGGER.info("TEST");
    }

}
