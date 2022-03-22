package com.socialbehavior.socialbehaviormod.entity.custom.npc.character;

import com.google.common.collect.ImmutableMap;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public enum ECharacterType {
    BRAVE("brave", new Character(127, -100, 0, -10, 40, 60, 90, 120, 100)),
    FEARFUL("fearful", new Character(-128, 100, 40, 80, 80, 20, -30, -60, 0)),
    WISE("wise", new Character(40, -40, 127, 100, -50, 80, 120, 100, 100)),
    DYNAMIC("dynamic", new Character(80, -40, 0, 127, 80, 80, -30, -50, 20)),
    THOUGHTFUL("thoughtful", new Character(90, -20, 80, 127, 20, 100, 50, 120, -80)),
    DECEITFUL("deceitful", new Character(-100, 80, 70, -90, 30, -70, 90, -120, 75)),
    LEADER("leader", new Character(100, -90, 100, -80, 100, 70, 120, -50, -90)),
    SELFISH("selfish", new Character(-30, 30, 70, -80, 50, -80, 70, -120, 30)),
    LAZY("lazy", new Character(-60, 30, 24, 53, -127, 70, 0, -30, 90));

    private static final Map<String, ECharacterType> BY_ID = Stream.of(values()).collect(ImmutableMap.toImmutableMap((character) -> {
        return character.id;
    }, Function.identity()));
    public static final SuggestionProvider<CommandSource> TYPE = (context, builder) -> ISuggestionProvider.suggest(BY_ID.keySet(), builder);
    private static final List<ECharacterType> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    private final String id;
    private final Character character;

    ECharacterType(String id, Character character) {
        this.id = id;
        this.character = character;
    }

    @Nullable
    public static ECharacterType byId(String id) {
        return BY_ID.get(id);
    }

    public static ECharacterType getRandomCharacterType() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    public static ResultTypeNameWithMatchPercentage getNearestCharacterTypeName(Character character) {
        Field[] fields = Character.class.getDeclaredFields();
        float maximumMatchPercentage = Float.MIN_VALUE;
        int maximumGap = 255 * fields.length;
        String idCharacterType = "";

        for (ECharacterType characterType : ECharacterType.values()) {
            int gap = 0;
            for (Field field : fields) {
                String name = field.getName();
                int value1 = 0;
                int value2 = 0;
                int result = 0;
                try {
                    value1 = field.getInt(characterType.character);
                    value2 = field.getInt(character);
                    result = Math.abs(value2 - value1);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                gap += result;
            }
            if (gap == 0) return new ResultTypeNameWithMatchPercentage(characterType.id, 100);

            float gapPercentage = Math.abs((((float) gap / maximumGap) - 1) * 100);
            if (gapPercentage > maximumMatchPercentage) {
                maximumMatchPercentage = gapPercentage;
                idCharacterType = characterType.id;
            }
        }
        return new ResultTypeNameWithMatchPercentage(idCharacterType, maximumMatchPercentage);
    }

    public String getId() {
        return id;
    }

    public Character getCharacter() {
        return character;
    }

    public static class ResultTypeNameWithMatchPercentage {
        public final String typeName;
        public final float matchPercentage;

        public ResultTypeNameWithMatchPercentage(String typeName, float matchPercentage) {
            this.typeName = typeName;
            this.matchPercentage = matchPercentage;
        }
    }
}
