package com.socialbehavior.socialbehaviormod.utils;

import com.socialbehavior.socialbehaviormod.entity.custom.npc.character.ECharacterType;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.data.NpcData;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.relation.ERelation;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.relation.Relation;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.IDataSerializer;

import java.util.*;
import java.util.stream.Collectors;

public class DataSerializers extends net.minecraft.network.datasync.DataSerializers {
    public static final IDataSerializer<NpcData> NPC_DATA = new IDataSerializer<NpcData>() {
        public void write(PacketBuffer pBuffer, NpcData pValue) {
            pBuffer.writeEnum(pValue.getCharacterType());
            pBuffer.writeUUID(pValue.getUIID());
            pBuffer.writeUtf(pValue.getFirstName());
            pBuffer.writeUtf(pValue.getLastName());
            pBuffer.writeUtf(pValue.getRelation().getAllRelationsString());
        }

        public NpcData read(PacketBuffer pBuffer) {
            ECharacterType characterType = pBuffer.readEnum(ECharacterType.class);
            UUID uuid = pBuffer.readUUID();
            String firstName = pBuffer.readUtf();
            String lastName = pBuffer.readUtf();
            String allRelationsString = pBuffer.readUtf();
            return new NpcData(characterType, uuid, firstName, lastName, new Relation(allRelationsString));
        }

        public NpcData copy(NpcData pValue) {
            return pValue;
        }
    };

    static {
        registerSerializer(NPC_DATA);
    };
}
