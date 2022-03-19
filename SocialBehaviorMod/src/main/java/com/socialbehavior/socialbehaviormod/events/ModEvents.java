package com.socialbehavior.socialbehaviormod.events;

import com.socialbehavior.socialbehaviormod.SocialBehaviorMod;
import com.socialbehavior.socialbehaviormod.commands.SpawnNpcCommand;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.NpcEntity;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.character.ECharacterType;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid = SocialBehaviorMod.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new SpawnNpcCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onNpcHit(AttackEntityEvent event) {
        Entity entity = event.getTarget();
        SocialBehaviorMod.LOGGER.info(entity.toString());
        if (entity instanceof NpcEntity) {
            NpcEntity npcEntity = (NpcEntity) entity;
            ECharacterType characterType = npcEntity.getCharacterType();
            if (characterType != null)
                SocialBehaviorMod.LOGGER.info(characterType.getId().toString());
            else {
                SocialBehaviorMod.LOGGER.info("NULL");
            }
        }
    }
}
