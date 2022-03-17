package com.socialbehavior.socialbehaviormod.entity.custom.npc;

import com.socialbehavior.socialbehaviormod.entity.ModEntityTypes;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.types.Character;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.types.ECharacterType;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class NpcEntity extends AbstractNPC {
    private final ECharacterType characterType;
    public NpcEntity(EntityType<? extends AgeableEntity> entityType, World world) {
        super(entityType, world);
        Character randomCharacter = new Character();
        ECharacterType.ResultTypeNameWithMatchPercentage result = ECharacterType.getNearestCharacterTypeName(randomCharacter);
        System.out.printf("result: %s %s %n", result.typeName, result.matchPercentage);
        characterType = ECharacterType.byId(result.typeName);

        EntityDataManager.defineId(NpcEntity.class, DataSerializers.VILLAGER_DATA);
    }

    @Nullable
    @Override
    public AgeableEntity getBreedOffspring(ServerWorld serverWorld, AgeableEntity ageableEntity) {
        return ModEntityTypes.NPC.get().create(serverWorld);
    }


    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
    }
}
