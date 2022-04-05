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
    BRAVE("brave", new Character(255, 127, 127, 100, 167, 187, 217, 250, 220)),
    FEARFUL("fearful", new Character(0, 255, 127, 220, 200, 147, 100, 20, 0)),
    WISE("wise", new Character(130, 110, 255, 100, 77, 207, 120, 100, 100)),
    DYNAMIC("dynamic", new Character(207, -40, 127, 255, 207, 207, 140, 200, 140)),
    THOUGHTFUL("thoughtful", new Character(210, 100, 207, 255, 130, 100, 190, 255, 20)),
    DECEITFUL("deceitful", new Character(10, 207, 155, 90, 140, 80, 127, 40, 130)),
    LEADER("leader", new Character(240, 100, 100, 100, 100, 70, 120, 77, 100)),
    SELFISH("selfish", new Character(100, 130, 140, 100, 140, 125, 150, 110, 130)),
    LAZY("lazy", new Character(40, 127, 24, 140, 127, 200, 127, 120, 1260));

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
        return VALUES.get(RANDOM.nextInt(SIZE - 1) + 1);
    }

    public static ResultTypeNameWithMatchPercentage getNearestCharacterTypeName(Character character) {
        Field[] fields = Character.class.getDeclaredFields();
        float maximumMatchPercentage = Float.MIN_VALUE;
        int maximumGap = 255 * fields.length;
        ECharacterType eCharacterType = null;

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
            if (gap == 0) return new ResultTypeNameWithMatchPercentage(characterType, 100);

            float gapPercentage = Math.abs((((float) gap / maximumGap) - 1) * 100);
            if (gapPercentage > maximumMatchPercentage) {
                maximumMatchPercentage = gapPercentage;
                eCharacterType = characterType;
            }
        }
        return new ResultTypeNameWithMatchPercentage(eCharacterType, maximumMatchPercentage);
    }

    public String getId() {
        return id;
    }

    public Character getCharacter() {
        return character;
    }

    public static class ResultTypeNameWithMatchPercentage {
        public final ECharacterType type;
        public final float matchPercentage;

        public ResultTypeNameWithMatchPercentage(ECharacterType type, float matchPercentage) {
            this.type = type;
            this.matchPercentage = matchPercentage;
        }
    }
}
