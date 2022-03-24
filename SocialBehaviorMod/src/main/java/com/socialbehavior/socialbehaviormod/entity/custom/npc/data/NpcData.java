package com.socialbehavior.socialbehaviormod.entity.custom.npc.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.socialbehavior.socialbehaviormod.SocialBehaviorMod;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.character.ECharacterType;

public class NpcData {
    public static final Codec<NpcData> CODEC = RecordCodecBuilder.create((value) -> {
        return value.group(Codec.STRING.fieldOf("character").orElseGet(() -> {
                    return ECharacterType.getRandomCharacterType().getId();
                }).forGetter((npcData) -> {
                    return npcData.characterNameType;
                }), Codec.STRING.fieldOf("firstName").orElseGet(() -> {
                    return SocialBehaviorMod.FAKER.name().firstName();
                }).forGetter((npcData) -> {
                    return npcData.firstName;
                }), Codec.STRING.fieldOf("lastName").orElseGet(() -> {
                    return SocialBehaviorMod.FAKER.name().lastName();
                }).forGetter((npcData) -> {
                    return npcData.lastName;
                })
        ).apply(value, NpcData::new);

    });
    private final String characterNameType;
    private final String firstName;
    private final String lastName;

    public NpcData(String characterNameType, String firstName, String lastName) {
        this.characterNameType = characterNameType;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getCharacterNameType() {
        return this.characterNameType;
    }

    public NpcData setCharacterNameType(String characterNameType) {
        return new NpcData(characterNameType, this.firstName, this.lastName);
    }

    public String getFirstName() {
        return this.firstName;
    }

    public NpcData setFirstName(String firstName) {
        return new NpcData(this.characterNameType, firstName, this.lastName);
    }

    public String getLastName() {
        return this.lastName;
    }

    public NpcData setLastName(String lastName) {
        return new NpcData(this.characterNameType, this.firstName, lastName);
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public NpcData setFullName(String fullName) {
        if (fullName.equals("")) {
            return new NpcData(this.characterNameType, "", "");
        }

        String[] name = fullName.split(" ");
        if (name.length == 2) {
            return new NpcData(this.characterNameType, name[0], name[1]);
        }
        return null;
    }
}
