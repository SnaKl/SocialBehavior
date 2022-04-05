package com.socialbehavior.socialbehaviormod.entity.custom.npc.character;

import com.socialbehavior.socialbehaviormod.entity.custom.npc.relation.ERelation;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Character {
    public byte courage;
    public byte fear;
    public byte intellect;
    public byte sensibility;
    public byte energy;
    public byte friendliness;
    public byte positivity;
    public byte altruism;
    public byte curiosity;

    public Character() {
        this.courage = this.randomByte();
        this.fear = this.randomByte();
        this.intellect = this.randomByte();
        this.sensibility = this.randomByte();
        this.energy = this.randomByte();
        this.friendliness = this.randomByte();
        this.positivity = this.randomByte();
        this.altruism = this.randomByte();
        this.curiosity = this.randomByte();
    }

    public Character(int courage, int fear, int intellect, int sensibility, int energy, int friendliness, int positivity, int altruism, int curiosity) {
        this.courage = unsignedIntToByte(courage);
        this.fear = unsignedIntToByte(fear);
        this.intellect = unsignedIntToByte(intellect);
        this.sensibility = unsignedIntToByte(sensibility);
        this.energy = unsignedIntToByte(energy);
        this.friendliness = unsignedIntToByte(friendliness);
        this.positivity = unsignedIntToByte(positivity);
        this.altruism = unsignedIntToByte(altruism);
        this.curiosity = unsignedIntToByte(curiosity);
    }

    private byte unsignedIntToByte(int value) {
        return (byte) (value - 128);
    }

    private int byteToUnsignedInt(byte value) {
        return value + 128;
    }

    private byte randomByte() {
        Random random = new Random();
        return (byte) (random.nextInt(255) - 128);
    }

    public Map<String, Integer> getCharacterMap() {
        Field[] fields = this.getClass().getDeclaredFields();
        Map<String, Integer> ret = new HashMap<>();
        for (Field field : fields) {
            String attributeName = field.getName();
            try {
                byte valByte = field.getByte(this);
                int valInt = byteToUnsignedInt(valByte);
                ret.put(attributeName, valInt);
            } catch (Exception ignored) {
            }
        }
        return ret;
    }
}
