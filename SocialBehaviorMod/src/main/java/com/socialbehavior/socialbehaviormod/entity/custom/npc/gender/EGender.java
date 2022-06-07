package com.socialbehavior.socialbehaviormod.entity.custom.npc.gender;

import com.socialbehavior.socialbehaviormod.SocialBehaviorMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum EGender {
    MALE("screen_info.socialbehaviormod.npc.gender.male", new ResourceLocation(SocialBehaviorMod.MOD_ID, "textures/entities/npcmale.png")),
    FEMALE("screen_info.socialbehaviormod.npc.gender.female", new ResourceLocation(SocialBehaviorMod.MOD_ID, "textures/entities/npcfemale.png")),
    TRANSGENDER("screen_info.socialbehaviormod.npc.gender.transgender", new ResourceLocation(SocialBehaviorMod.MOD_ID, "textures/entities/npctransgender.png"));

    private static final List<EGender> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private final String translationKey;
    private final ResourceLocation texture;

    EGender(String translationKey, ResourceLocation texture) {
        this.translationKey = translationKey;
        this.texture = texture;
    }

    /**
     * Get random gender
     *
     * @return gender
     */
    public static EGender getRandomGender() {
        return VALUES.get(new Random().nextInt(SIZE - 1) + 1);
    }

    /**
     * Get gender translation name
     *
     * @return gender name
     */
    public TranslationTextComponent getGenderName() {
        return new TranslationTextComponent(this.translationKey);
    }

    /**
     * Get gender texture
     *
     * @return texture location
     */
    public ResourceLocation getTexture() {
        return this.texture;
    }
}
