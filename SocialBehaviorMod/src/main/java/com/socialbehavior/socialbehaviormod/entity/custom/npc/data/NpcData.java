package com.socialbehavior.socialbehaviormod.entity.custom.npc.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.socialbehavior.socialbehaviormod.SocialBehaviorMod;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.character.ECharacterType;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.gender.EGender;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.relation.Relation;

import java.util.UUID;

public class NpcData {
    public static final Codec<NpcData> CODEC = RecordCodecBuilder.create((value) -> {
        return value.group(Codec.STRING.fieldOf("uuid").orElseGet(() -> {
                    return UUID.randomUUID().toString();
                }).forGetter((npcData) -> {
                    return npcData.uuid.toString();
                }), Codec.STRING.fieldOf("firstName").orElseGet(() -> {
                    return SocialBehaviorMod.FAKER.name().firstName();
                }).forGetter((npcData) -> {
                    return npcData.firstName;
                }), Codec.STRING.fieldOf("lastName").orElseGet(() -> {
                    return SocialBehaviorMod.FAKER.name().lastName();
                }).forGetter((npcData) -> {
                    return npcData.lastName;
                }), Codec.INT.fieldOf("character_type").orElse(ECharacterType.BRAVE.ordinal()).forGetter(npcData -> {
                    return npcData.characterType.ordinal();
                }), Codec.INT.fieldOf("gender").orElse(EGender.MALE.ordinal()).forGetter(npcDate -> {
                    return npcDate.gender.ordinal();
                }), Relation.CODEC.fieldOf("relation").orElseGet(Relation::new).forGetter((npcData) -> {
                    return npcData.relation;
                })
        ).apply(value, NpcData::new);
    });

    private final UUID uuid;
    private final String firstName;
    private final String lastName;
    private final ECharacterType characterType;
    private final EGender gender;
    private final Relation relation;

    public NpcData(UUID uuid, String firstName, String lastName, ECharacterType characterType, EGender gender, Relation relation) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.characterType = characterType;
        this.gender = gender;
        this.relation = relation;
    }

    public NpcData(String uuid, String firstName, String lastName, int characterType, int gender, Relation relation) {
        this.uuid = UUID.fromString(uuid);
        this.firstName = firstName;
        this.lastName = lastName;
        this.characterType = ECharacterType.values()[characterType];
        this.gender = EGender.values()[gender];
        this.relation = relation;
    }

    public ECharacterType getCharacterType() {
        return this.characterType;
    }

    public NpcData setCharacterType(ECharacterType characterType) {
        return new NpcData(this.uuid, this.firstName, this.lastName, characterType, this.gender, this.relation);
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public NpcData setUUID(UUID uuid) {
        return new NpcData(uuid, this.firstName, this.lastName, this.characterType, this.gender, this.relation);
    }

    public String getFirstName() {
        return this.firstName;
    }

    public NpcData setFirstName(String firstName) {
        return new NpcData(this.uuid, firstName, this.lastName, this.characterType, this.gender, this.relation);
    }

    public String getLastName() {
        return this.lastName;
    }

    public NpcData setLastName(String lastName) {
        return new NpcData(this.uuid, this.firstName, lastName, this.characterType, this.gender, this.relation);
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public NpcData setFullName(String fullName) {
        if (fullName.equals("")) {
            return new NpcData(this.uuid, "", "", this.characterType, this.gender, this.relation);
        }

        String[] name = fullName.split(" ");
        if (name.length == 2) {
            return new NpcData(this.uuid, name[0], name[1], this.characterType, this.gender, this.relation);
        }
        return null;
    }

    public EGender getGender() {
        return this.gender;
    }

    public NpcData setGender(EGender gender) {
        return new NpcData(this.uuid, this.firstName, this.lastName, this.characterType, gender, this.relation);
    }

    public Relation getRelation() {
        return this.relation;
    }

    public NpcData setRelation(Relation relation) {
        return new NpcData(this.uuid, this.firstName, this.lastName, this.characterType, this.gender, relation);
    }
}
