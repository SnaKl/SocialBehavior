package com.socialbehavior.socialbehaviormod.entity.custom.npc.relation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Relation {
    private final Map<ERelation, List<UUID>> relations;

    public Relation() {
        this.relations = new HashMap<>(ERelation.values().length);
        for (ERelation relation : ERelation.values()) {
            this.relations.put(relation, new java.util.ArrayList<>());
        }
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
}
