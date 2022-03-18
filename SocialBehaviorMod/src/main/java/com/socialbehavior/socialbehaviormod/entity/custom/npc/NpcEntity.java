package com.socialbehavior.socialbehaviormod.entity.custom.npc;

import com.socialbehavior.socialbehaviormod.entity.ModEntityTypes;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.types.Character;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.types.ECharacterType;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class NpcEntity extends AbstractNPC {
    private ECharacterType characterType;

    public NpcEntity(EntityType<? extends AgeableEntity> entityType, World world) {
        super(entityType, world);
        ECharacterType.ResultTypeNameWithMatchPercentage result = ECharacterType.getNearestCharacterTypeName(new Character());
        characterType = ECharacterType.byId(result.typeName);
    }

    @Nullable
    @Override
    public AgeableEntity getBreedOffspring(ServerWorld serverWorld, AgeableEntity ageableEntity) {
        return ModEntityTypes.NPC.get().create(serverWorld);
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld serverWorld, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData livingEntityData, @Nullable CompoundNBT compoundNBT) {
        /*
        //exemple horse.finalizeSpawn
        CoatColors coatcolors;
        if (livingEntityData instanceof HorseEntity.HorseData) {
            coatcolors = ((HorseEntity.HorseData)livingEntityData).variant;
        } else {
            coatcolors = Util.getRandom(CoatColors.values(), this.random);
            livingEntityData = new HorseEntity.HorseData(coatcolors);
        }
        this.setVariantAndMarkings(coatcolors, Util.getRandom(CoatTypes.values(), this.random));
         */

        return super.finalizeSpawn(serverWorld, difficultyInstance, spawnReason, livingEntityData, compoundNBT);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
    }

    public ECharacterType getCharacterType() {
        return characterType;
    }

    public void setCharacterType(ECharacterType characterType) {
        this.characterType = characterType;
    }
}
