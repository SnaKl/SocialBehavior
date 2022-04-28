package com.socialbehavior.socialbehaviormod.entity.custom.npc.gender;

import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum EGender {
    MALE("screen_info.socialbehaviormod.npc.gender.male"),
    FEMALE("screen_info.socialbehaviormod.npc.gender.female"),
    TRANSGENDER("screen_info.socialbehaviormod.npc.gender.transgender");

    private static final List<EGender> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private final String translationKey;

    EGender(String translationKey) {
        this.translationKey = translationKey;
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
}
