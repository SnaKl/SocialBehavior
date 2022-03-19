package com.socialbehavior.socialbehaviormod.entity.custom.npc.character;

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
        this.courage = (byte) courage;
        this.fear = (byte) fear;
        this.intellect = (byte) intellect;
        this.sensibility = (byte) sensibility;
        this.energy = (byte) energy;
        this.friendliness = (byte) friendliness;
        this.positivity = (byte) positivity;
        this.altruism = (byte) altruism;
        this.curiosity = (byte) curiosity;
    }

    private byte randomByte() {
        Random random = new Random();
        return (byte) (random.nextInt(255) - 128);
    }
}
