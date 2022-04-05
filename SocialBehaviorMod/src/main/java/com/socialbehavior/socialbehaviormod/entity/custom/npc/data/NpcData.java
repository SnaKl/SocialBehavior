package com.socialbehavior.socialbehaviormod.entity.custom.npc.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.socialbehavior.socialbehaviormod.SocialBehaviorMod;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.character.ECharacterType;

import java.util.UUID;

public class NpcData {
    public static final Codec<NpcData> CODEC = RecordCodecBuilder.create((value) -> {
        return value.group(Codec.INT.fieldOf("character_type").orElseGet(ECharacterType.BRAVE::ordinal).forGetter((npcData) -> {
                    return npcData.characterType.ordinal();
                }), Codec.STRING.fieldOf("uuid").orElseGet(() -> {
                    return UUID.randomUUID().toString();
                }).forGetter((npcData) -> {
                    return npcData.uuid;
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
    private final ECharacterType characterType;
    private final String firstName;
    private final String lastName;
    private final String uuid;

    public NpcData(ECharacterType characterType, String uuid, String firstName, String lastName) {
        this.characterType = characterType;
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public NpcData(Integer characterType, String uuid, String firstName, String lastName) {
        this.characterType = ECharacterType.values()[characterType];
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public ECharacterType getCharacterType() {
        return this.characterType;
    }

    public NpcData setCharacterType(ECharacterType characterType) {
        return new NpcData(characterType, this.uuid, this.firstName, this.lastName);
    }

    public String getUIID() {
        return this.uuid;
    }

    public NpcData setUUID(String uuid) {
        return new NpcData(this.characterType, uuid, this.firstName, this.lastName);
    }

    public String getFirstName() {
        return this.firstName;
    }

    public NpcData setFirstName(String firstName) {
        return new NpcData(this.characterType, this.uuid, firstName, this.lastName);
    }

    public String getLastName() {
        return this.lastName;
    }

    public NpcData setLastName(String lastName) {
        return new NpcData(this.characterType, this.uuid, this.firstName, lastName);
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public NpcData setFullName(String fullName) {
        if (fullName.equals("")) {
            return new NpcData(this.characterType, this.uuid, "", "");
        }

        String[] name = fullName.split(" ");
        if (name.length == 2) {
            return new NpcData(this.characterType, this.uuid, name[0], name[1]);
        }
        return null;
    }
}
