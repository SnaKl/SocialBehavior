package com.socialbehavior.socialbehaviormod.world;

import com.socialbehavior.socialbehaviormod.core.util.Reference;
import com.socialbehavior.socialbehaviormod.world.gen.OreGeneration;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class WorldEvents {
    @SubscribeEvent
    public static void biomeLoadingEvent(final BiomeLoadingEvent event){
        OreGeneration.generateOres(event);
    }
}
