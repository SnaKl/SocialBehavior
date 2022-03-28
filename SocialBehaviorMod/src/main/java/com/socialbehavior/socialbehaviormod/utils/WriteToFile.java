package com.socialbehavior.socialbehaviormod.utils;

import com.google.gson.JsonObject;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.NpcEntity;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.data.NpcToJson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public final class WriteToFile {
    public static void WriteJson(JsonObject jsonObject, String fileName) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String date = dateFormat.format(new Date());
            FileWriter fileWriter = new FileWriter(date + "_" + fileName + ".json", true);
            fileWriter.write(jsonObject.toString());
            fileWriter.close();

            File file = new File(date + "_" + fileName + ".json");
            System.out.println("File created: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void WriteJsonNpcEntity(NpcEntity npcEntity, String fileName) {
        JsonObject npcJson = NpcToJson.NpcToJson(npcEntity);
        WriteJson(npcJson, fileName);
    }

    public static void WriteJsonNpcEntities(List<NpcEntity> npcEntities, String fileName) {
        JsonObject npcsJson = NpcToJson.NpcsToJson(npcEntities);
        WriteJson(npcsJson, fileName);
    }
}
