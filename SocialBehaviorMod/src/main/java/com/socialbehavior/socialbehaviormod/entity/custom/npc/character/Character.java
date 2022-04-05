package com.socialbehavior.socialbehaviormod.entity.custom.npc.character;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Character {
    public static final Codec<Character> CODEC = RecordCodecBuilder.create((value) -> {
        return value.group(
                Codec.INT.fieldOf("courage")
                        .orElse(new Random().nextInt(255))
                        .forGetter((character) -> {
                            return character.courage;
                        }),
                Codec.INT.fieldOf("fear")
                        .orElse(new Random().nextInt(255))
                        .forGetter((character) -> {
                            return character.fear;
                        }),
                Codec.INT.fieldOf("intellect")
                        .orElse(new Random().nextInt(255))
                        .forGetter((character) -> {
                            return character.intellect;
                        }),
                Codec.INT.fieldOf("sensibility")
                        .orElse(new Random().nextInt(255))
                        .forGetter((character) -> {
                            return character.sensibility;
                        }),
                Codec.INT.fieldOf("energy")
                        .orElse(new Random().nextInt(255))
                        .forGetter((character) -> {
                            return character.energy;
                        }),
                Codec.INT.fieldOf("friendliness")
                        .orElse(new Random().nextInt(255))
                        .forGetter((character) -> {
                            return character.friendliness;
                        }),
                Codec.INT.fieldOf("positivity")
                        .orElse(new Random().nextInt(255))
                        .forGetter((character) -> {
                            return character.positivity;
                        }),
                Codec.INT.fieldOf("altruism")
                        .orElse(new Random().nextInt(255))
                        .forGetter((character) -> {
                            return character.altruism;
                        }),
                Codec.INT.fieldOf("curiosity")
                        .orElse(new Random().nextInt(255))
                        .forGetter((character) -> {
                            return character.curiosity;
                        })
        ).apply(value, Character::new);
    });

    public int courage;
    public int fear;
    public int intellect;
    public int sensibility;
    public int energy;
    public int friendliness;
    public int positivity;
    public int altruism;
    public int curiosity;

    public Character() {
        this.courage = this.randomUnsignedByte();
        this.fear = this.randomUnsignedByte();
        this.intellect = this.randomUnsignedByte();
        this.sensibility = this.randomUnsignedByte();
        this.energy = this.randomUnsignedByte();
        this.friendliness = this.randomUnsignedByte();
        this.positivity = this.randomUnsignedByte();
        this.altruism = this.randomUnsignedByte();
        this.curiosity = this.randomUnsignedByte();
    }

    public Character(int courage, int fear, int intellect, int sensibility, int energy, int friendliness, int positivity, int altruism, int curiosity) {
        this.courage = courage;
        this.fear = fear;
        this.intellect = intellect;
        this.sensibility = sensibility;
        this.energy = energy;
        this.friendliness = friendliness;
        this.positivity = positivity;
        this.altruism = altruism;
        this.curiosity = curiosity;
    }

    private int randomUnsignedByte() {
        Random random = new Random();
        return random.nextInt(255);
    }

    public Map<String, Integer> getCharacterMap() {
        Field[] fields = this.getClass().getDeclaredFields();
        Map<String, Integer> res = new HashMap<>();
        for (Field field : fields) {
            String attributeName = field.getName();
            try {
                int value = field.getInt(this);
                res.put(attributeName, value);
            } catch (Exception ignored) {
            }
        }
        return res;
    }
}
