package com.socialbehavior.socialbehaviormod.entity.custom.npc;

import com.socialbehavior.socialbehaviormod.entity.custom.npc.character.InfoNpcScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.eventbus.api.Event;

public class OpenInfoNpcEvent extends Event {
    private final PlayerEntity entityPlayer;

    public OpenInfoNpcEvent(PlayerEntity playerEntity, NpcEntity npcEntity) {
        super();
        entityPlayer = playerEntity;
        Minecraft.getInstance().setScreen(new InfoNpcScreen(playerEntity, npcEntity));
    }

    public PlayerEntity getPlayer() {
        return entityPlayer;
    }
}
