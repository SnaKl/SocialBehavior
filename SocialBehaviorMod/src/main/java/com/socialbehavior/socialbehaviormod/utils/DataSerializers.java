package com.socialbehavior.socialbehaviormod.utils;

import com.socialbehavior.socialbehaviormod.entity.custom.npc.data.NpcData;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.IDataSerializer;

public class DataSerializers extends net.minecraft.network.datasync.DataSerializers {
    public static final IDataSerializer<NpcData> NPC_DATA = new IDataSerializer<NpcData>() {
        public void write(PacketBuffer pBuffer, NpcData pValue) {
            pBuffer.writeUtf(pValue.getCharacterNameType());
            pBuffer.writeUtf(pValue.getFirstName());
            pBuffer.writeUtf(pValue.getLastName());
        }

        public NpcData read(PacketBuffer pBuffer) {
            String characterId = pBuffer.readUtf();
            String firstName = pBuffer.readUtf();
            String lastName = pBuffer.readUtf();
            return new NpcData(characterId, firstName, lastName);
        }

        public NpcData copy(NpcData pValue) {
            return pValue;
        }
    };

    static{
      registerSerializer(NPC_DATA);
    };
}
