package com.socialbehavior.socialbehaviormod.world;

import com.socialbehavior.socialbehaviormod.SocialBehaviorMod;
import com.socialbehavior.socialbehaviormod.world.generation.OreGeneration;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SocialBehaviorMod.MOD_ID)
public class ModWorldEvents {
    @SubscribeEvent
    public static void biomeLoadingEvent(final BiomeLoadingEvent event) {
        OreGeneration.generateOres(event);
    }
}
