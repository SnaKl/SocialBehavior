package com.socialbehavior.socialbehaviormod.entity.custom.npc.types;

import com.google.common.collect.ImmutableMap;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public enum ECharacterType {
    BRAVE("brave", new Character(127, -100, 0, -10, 40, 60, 90, 120, 100)),
    FEARFUL("fearful", new Character(-128, 100, 40,80,80, 20, -30, -60, 0));

    public String id;
    public Character character;

    private static final Map<String, ECharacterType> BY_ID = Stream.of(values()).collect(ImmutableMap.toImmutableMap((character) -> {
        return character.id;
    }, Function.identity()));
    private static final List<ECharacterType> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    ECharacterType(String id, Character character) {
        this.id = id;
        this.character = character;
    }

    @Nullable
    public static ECharacterType byId(String id) {
        return BY_ID.get(id);
    }

    public static ECharacterType getRandomCharacterType()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    public static class ResultTypeNameWithMatchPercentage {
        public final String typeName;
        public final float matchPercentage;

        public ResultTypeNameWithMatchPercentage(String typeName, float matchPercentage) {
            this.typeName = typeName;
            this.matchPercentage = matchPercentage;
        }
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
            if(gap == 0) return new ResultTypeNameWithMatchPercentage(characterType.id, 100);

            float gapPercentage = Math.abs((((float)gap/maximumGap)-1)*100);
            if( gapPercentage > maximumMatchPercentage){
                maximumMatchPercentage = gapPercentage;
                idCharacterType = characterType.id;
            }
        }
        return new ResultTypeNameWithMatchPercentage(idCharacterType, maximumMatchPercentage);
    }
}
