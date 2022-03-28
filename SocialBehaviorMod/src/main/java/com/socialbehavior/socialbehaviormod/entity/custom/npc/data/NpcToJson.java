package com.socialbehavior.socialbehaviormod.entity.custom.npc.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.NpcEntity;

import java.lang.reflect.Field;
import java.util.List;

//** NpcEntity to Json from all fields return json*/
public class NpcToJson {
    public static JsonObject NpcToJson(NpcEntity npcEntity) {
        JsonObject json = new JsonObject();
        NpcData npcData = npcEntity.getNpcData();

        json.addProperty("id", npcEntity.getNpcData().getUIID());

        JsonObject jsonEntityPosition = new JsonObject();
        jsonEntityPosition.addProperty("x", npcEntity.position().x);
        jsonEntityPosition.addProperty("y", npcEntity.position().y);
        jsonEntityPosition.addProperty("z", npcEntity.position().z);
        json.add("position", jsonEntityPosition);

        JsonObject jsonNpcData = new JsonObject();
        Field[] fields = npcData.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if(field.getName().equals("CODEC")) continue;
                jsonNpcData.addProperty(field.getName(), field.get(npcData).toString());
            } catch (Exception ignored) {
            }
        }
        json.add("npcData", jsonNpcData);

        return json;
    }

    public static JsonObject NpcsToJson(List<NpcEntity> npcEntities) {
        JsonObject json = new JsonObject();
        JsonArray jsonNpcs = new JsonArray();
        for (NpcEntity npcEntity : npcEntities) {
            jsonNpcs.add(NpcToJson(npcEntity));
        }
        json.add("npcs", jsonNpcs);
        return json;
    }
}
