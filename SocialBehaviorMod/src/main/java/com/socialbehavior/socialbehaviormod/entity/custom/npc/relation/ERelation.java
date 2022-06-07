package com.socialbehavior.socialbehaviormod.entity.custom.npc.relation;

import net.minecraft.util.text.TranslationTextComponent;

public enum ERelation {
    FAMILY("screen_info.socialbehaviormod.npc.relation.family"),
    PARENT("screen_info.socialbehaviormod.npc.relation.parent"),
    CHILD("screen_info.socialbehaviormod.npc.relation.child"),
    FRIENDSHIP("screen_info.socialbehaviormod.npc.relation.friendship"),
    LOVER("screen_info.socialbehaviormod.npc.relation.lover"),
    ENEMY("screen_info.socialbehaviormod.npc.relation.enemy"),
    NEUTRAL("screen_info.socialbehaviormod.npc.relation.neutral"),
    UNKNOWN("screen_info.socialbehaviormod.npc.relation.unknown");

    private final String translationKey;

    ERelation(String translationKey) {
        this.translationKey = translationKey;
    }

    public TranslationTextComponent getRelationName() {
        return new TranslationTextComponent(this.translationKey);
    }
}


