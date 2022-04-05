package com.socialbehavior.socialbehaviormod.entity.custom.npc.relation;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.*;

public class Relation {
    public static final Codec<Relation> CODEC = RecordCodecBuilder.create((value) -> {
        return value.group(Codec.STRING.fieldOf("relations")
                .orElse("")
                .forGetter(Relation::getAllRelationsString)
        ).apply(value, Relation::new);
    });

    private final Map<ERelation, List<UUID>> relations;

    public Relation() {
        this.relations = new HashMap<>(ERelation.values().length);
        for (ERelation relation : ERelation.values()) {
            this.relations.put(relation, new java.util.ArrayList<>());
        }
    }

    public Relation(String allRelations) {
        this.relations = this.stringToMapRelation(allRelations);
    }

    public Map<ERelation, List<UUID>> getRelations() {
        return relations;
    }

    public List<UUID> getRelation(ERelation relation) {
        return relations.get(relation);
    }

    public void addRelation(ERelation relation, UUID id) {
        relations.get(relation).add(id);
    }

    public void removeRelation(ERelation relation, UUID id) {
        relations.get(relation).remove(id);
    }

    public void clearRelation(ERelation relation) {
        relations.get(relation).clear();
    }

    public void removeAllRelations() {
        relations.clear();
        for (ERelation relation : ERelation.values()) {
            this.relations.put(relation, new java.util.ArrayList<>());
        }
    }

    public boolean hasRelation(ERelation relation, UUID id) {
        return relations.get(relation).contains(id);
    }

    public String getAllRelationsString() {
        List<String> allRelationListString = new ArrayList<>();
        for (ERelation eRelation : ERelation.values()) {
            List<String> relationListString = Lists.transform(relations.get(eRelation), UUID::toString);
            String relationString = String.join(",", relationListString);
            allRelationListString.add(relationString);
        }
        return String.join(";", allRelationListString);
    }

    public Map<ERelation, List<UUID>> stringToMapRelation(String allRelations) {
        Map<ERelation, List<UUID>> allRelationMap = new HashMap<>();
        for (ERelation relation : ERelation.values()) {
            allRelationMap.put(relation, new ArrayList<>());
        }
        String[] allRelationArray = allRelations.split(";");
        for (int i = 0; i < allRelationArray.length; i++) {
            if (allRelationArray[i].length() == 0) continue;
            String[] relationArray = allRelationArray[i].split(",");
            for (String s : relationArray) {
                allRelationMap.get(ERelation.values()[i]).add(UUID.fromString(s));
            }
        }
        return allRelationMap;
    }
}
