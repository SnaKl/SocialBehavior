package com.socialbehavior.socialbehaviormod.entity.custom.npc.relation;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.*;

/**
 * Relation between NPC's.
 *
 * @author SnaKi
 */
public class Relation {
    /**
     * The codec for this class. With relations as strings.
     */
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

    /**
     * Constructors with all relations string as base.
     *
     * @param allRelations String of all relations.
     */
    public Relation(String allRelations) {
        this.relations = this.stringToMapRelation(allRelations);
    }

    /**
     * Get all relations as map.
     *
     * @return Map of relations.
     */
    public Map<ERelation, List<UUID>> getRelations() {
        return relations;
    }

    /**
     * Get Relation for specific type.
     *
     * @param eRelation Type of relation.
     * @return List of UUID's.
     */
    public List<UUID> getRelation(ERelation eRelation) {
        return relations.get(eRelation);
    }

    /**
     * Add relation if not already in.
     *
     * @param eRelation Type of Relation to add.
     * @param uuid      UUID to add.
     */
    public void addRelation(ERelation eRelation, UUID uuid) {
        if (!relations.get(eRelation).contains(uuid)) {
            relations.get(eRelation).add(uuid);
        }
    }

    /**
     * Remove relation for specific uuid.
     *
     * @param eRelation Type of relation.
     * @param uuid      UUID to remove.
     */
    public void removeRelation(ERelation eRelation, UUID uuid) {
        relations.get(eRelation).remove(uuid);
    }

    /**
     * Clear eRelation
     *
     * @param eRelation ERelation to clear.
     */
    public void clearRelation(ERelation eRelation) {
        relations.get(eRelation).clear();
    }

    /**
     * Remove all relations.
     */
    public void removeAllRelations() {
        relations.clear();
        for (ERelation eRelation : ERelation.values()) {
            clearRelation(eRelation);
        }
    }

    /**
     * Check if has relation for specific uuid
     *
     * @param eRelation Relation to check.
     * @param uuid      UUID to check.
     * @return True if has relation.
     */
    public boolean hasRelation(ERelation eRelation, UUID uuid) {
        return relations.get(eRelation).contains(uuid);
    }

    /**
     * Get all relations as string.
     *
     * @return String of all relations. Format: relation1,relation2,relation3;relation4,relation5,relation6;...
     */
    public String getAllRelationsString() {
        List<String> allRelationListString = new ArrayList<>();
        for (ERelation eRelation : ERelation.values()) {
            List<String> relationListString = Lists.transform(relations.get(eRelation), UUID::toString);
            String relationString = String.join(",", relationListString);
            allRelationListString.add(relationString);
        }
        return String.join(";", allRelationListString);
    }

    /**
     * Convert string relations to map relation.
     *
     * @param allRelations String of all relations. Format: relation1,relation2,relation3;relation4,relation5,relation6;...
     * @return Map of relations with Enum Relation has key and list of UUIDs has value.
     */
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
