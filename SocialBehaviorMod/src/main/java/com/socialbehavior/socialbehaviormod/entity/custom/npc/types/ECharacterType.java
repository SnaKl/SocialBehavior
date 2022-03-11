package com.socialbehavior.socialbehaviormod.entity.custom.npc.types;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public enum ECharacterType {
    BRAVE("brave", 127, -100, 0, -10, 40, 60, 90, 120, 100),
    FEARFUL("fearful", -127, 100, 40,80,80, 20, -30, -60, 0);

    public final String id;
    public final byte courage;
    public final byte fear;
    public final byte intellect;
    public final byte sensibility;
    public final byte energy;
    public final byte friendliness;
    public final byte positivity;
    public final byte altruism;
    public final byte curiosity;
    private static final Map<String, ECharacterType> BY_ID = Stream.of(values()).collect(ImmutableMap.toImmutableMap((character) -> {
        return character.id;
    }, Function.identity()));

    ECharacterType(String id, int courage, int fear, int intellect, int sensibility, int energy, int friendliness, int positivity, int altruism, int curiosity) {
        this.id = id;
        this.courage = (byte)courage;
        this.fear = (byte)fear;
        this.intellect = (byte)intellect;
        this.sensibility = (byte)sensibility;
        this.energy = (byte)energy;
        this.friendliness = (byte)friendliness;
        this.positivity = (byte)positivity;
        this.altruism = (byte)altruism;
        this.curiosity = (byte)curiosity;
    }

    @Nullable
    public static ECharacterType byId(String id) {
        return BY_ID.get(id);
    }

}
