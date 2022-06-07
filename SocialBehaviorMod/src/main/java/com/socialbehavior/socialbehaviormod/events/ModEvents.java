package com.socialbehavior.socialbehaviormod.events;

import com.socialbehavior.socialbehaviormod.SocialBehaviorMod;
import com.socialbehavior.socialbehaviormod.commands.MakeBabyNpcCommand;
import com.socialbehavior.socialbehaviormod.commands.SpawnNpcCommand;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.NpcEntity;
import com.socialbehavior.socialbehaviormod.utils.WriteToFile;
import net.minecraft.world.World;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.server.command.ConfigCommand;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = SocialBehaviorMod.MOD_ID)
public class ModEvents {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new SpawnNpcCommand(event.getDispatcher());
        new MakeBabyNpcCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onTickEvent(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.WorldTickEvent.Phase.START) return;
        if (event.world.dimension().compareTo(World.OVERWORLD) != 0) return;

        long worldTime = event.world.getGameTime();
        if (worldTime % 24000 != 0) return;
        if (NpcEntity.NPC_MAP == null) return;
        List<NpcEntity> listEntities = new ArrayList<NpcEntity>(NpcEntity.NPC_MAP.values());
        if (listEntities.size() > 0) {
            WriteToFile.WriteJsonNpcEntities(listEntities, worldTime / 24000 + "_npc");
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onNpcDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof NpcEntity && NpcEntity. NPC_MAP != null) {
            NpcEntity npcEntity = (NpcEntity) event.getEntity();
            npcEntity.removeAllRelationLink();
            NpcEntity.NPC_MAP.remove(npcEntity.getNpcData().getUUID().toString(), npcEntity);
        }
    }

    @SubscribeEvent
    public static void onShutDown(FMLServerStoppingEvent event){
        System.out.println("Server is shutting down");
        NpcEntity.NPC_MAP = null;
    }
}
