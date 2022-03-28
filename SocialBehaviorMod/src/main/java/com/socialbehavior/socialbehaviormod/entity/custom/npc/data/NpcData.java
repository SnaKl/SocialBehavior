package com.socialbehavior.socialbehaviormod.entity.custom.npc.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.socialbehavior.socialbehaviormod.SocialBehaviorMod;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.character.ECharacterType;
import net.minecraft.util.math.MathHelper;

import java.util.Random;
import java.util.UUID;

public class NpcData {
    public static final Codec<NpcData> CODEC = RecordCodecBuilder.create((value) -> {
        return value.group(Codec.STRING.fieldOf("character").orElseGet(() -> {
                    return ECharacterType.getRandomCharacterType().getId();
                }).forGetter((npcData) -> {
                    return npcData.characterNameType;
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
    private final String characterNameType;
    private final String firstName;
    private final String lastName;
    private final String uuid;

    public NpcData(String characterNameType, String uuid, String firstName, String lastName) {
        this.characterNameType = characterNameType;
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getCharacterNameType() {
        return this.characterNameType;
    }

    public NpcData setCharacterNameType(String characterNameType) {
        return new NpcData(characterNameType, this.uuid, this.firstName, this.lastName);
    }

    public String getUIID() {
        return this.uuid;
    }

    public NpcData setUUID(String uuid) {
        return new NpcData(this.characterNameType, uuid, this.firstName, this.lastName);
    }

    public String getFirstName() {
        return this.firstName;
    }

    public NpcData setFirstName(String firstName) {
        return new NpcData(this.characterNameType, this.uuid, firstName, this.lastName);
    }

    public String getLastName() {
        return this.lastName;
    }

    public NpcData setLastName(String lastName) {
        return new NpcData(this.characterNameType, this.uuid, this.firstName, lastName);
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public NpcData setFullName(String fullName) {
        if (fullName.equals("")) {
            return new NpcData(this.characterNameType, this.uuid, "", "");
        }

        String[] name = fullName.split(" ");
        if (name.length == 2) {
            return new NpcData(this.characterNameType, this.uuid, name[0], name[1]);
        }
        return null;
    }
}
