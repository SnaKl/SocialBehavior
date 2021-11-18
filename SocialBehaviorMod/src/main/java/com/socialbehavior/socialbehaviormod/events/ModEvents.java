package com.socialbehavior.socialbehaviormod.events;

import com.socialbehavior.socialbehaviormod.SocialBehaviorMod;
import com.socialbehavior.socialbehaviormod.map.MapHandler;
import com.socialbehavior.socialbehaviormod.screen.gui.MapGui;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SocialBehaviorMod.MOD_ID)
public class ModEvents {
    static Minecraft minecraft = Minecraft.getInstance();
    static MapHandler mapHandler = MapHandler.getInstance();

    @SubscribeEvent
    public static void renderGUI(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            new MapGui(minecraft, event.getMatrixStack());
        }
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
            MapHandler.getInstance().initMapHandler();
        }
    }
*/
}
