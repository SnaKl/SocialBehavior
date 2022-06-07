package com.socialbehavior.socialbehaviormod.utils;

import com.socialbehavior.socialbehaviormod.entity.custom.npc.character.ECharacterType;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.data.NpcData;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.gender.EGender;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.relation.Relation;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.IDataSerializer;

import java.util.UUID;

public class DataSerializers extends net.minecraft.network.datasync.DataSerializers {
    public static final IDataSerializer<NpcData> NPC_DATA = new IDataSerializer<NpcData>() {
        public void write(PacketBuffer packetBuffer, NpcData npcData) {
            packetBuffer.writeUUID(npcData.getUUID());
            packetBuffer.writeUtf(npcData.getFirstName());
            packetBuffer.writeUtf(npcData.getLastName());
            packetBuffer.writeEnum(npcData.getCharacterType());
            packetBuffer.writeEnum(npcData.getGender());
            packetBuffer.writeUtf(npcData.getRelation().getAllRelationsString());
        }

        public NpcData read(PacketBuffer packetBuffer) {
            UUID uuid = packetBuffer.readUUID();
            String firstName = packetBuffer.readUtf();
            String lastName = packetBuffer.readUtf();
            ECharacterType characterType = packetBuffer.readEnum(ECharacterType.class);
            EGender gender = packetBuffer.readEnum(EGender.class);
            String allRelationsString = packetBuffer.readUtf();
            return new NpcData(uuid, firstName, lastName, characterType, gender, new Relation(allRelationsString));
        }

        public NpcData copy(NpcData pValue) {
            return pValue;
        }
    };

    static {
        registerSerializer(NPC_DATA);
    };
}
