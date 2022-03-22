package com.socialbehavior.socialbehaviormod.entity.custom.npc;

import com.socialbehavior.socialbehaviormod.entity.ModEntityTypes;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.character.Character;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.character.ECharacterType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;

public class NpcEntity extends AbstractNPC {
    private static final DataParameter<String> CHARACTER_NAME = EntityDataManager.defineId(NpcEntity.class, DataSerializers.STRING);
    private ECharacterType characterType;
    private Boolean isInteract;

    public NpcEntity(EntityType<? extends AgeableEntity> entityType, World world) {
        super(entityType, world);
        this.isInteract = false;
        this.setCharacterName(this.getCharacterName());
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(CHARACTER_NAME, "");
    }

    public void onSyncedDataUpdated(DataParameter<?> dataParameter) {
        super.onSyncedDataUpdated(dataParameter);
        if (CHARACTER_NAME.equals(dataParameter)) {
            ECharacterType characterType = ECharacterType.byId(this.getEntityData().get(CHARACTER_NAME));
            if (characterType != null) {
                this.setCharacterType(characterType);
            }
        }
    }

    public String getCharacterName() {
        return this.getEntityData().get(CHARACTER_NAME);
    }

    public void setCharacterName(String characterName) {
        this.getEntityData().set(CHARACTER_NAME, characterName);
    }

    public void readAdditionalSaveData(CompoundNBT compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        this.setCharacterName(compoundNBT.getString("CharacterName"));
    }

    public void addAdditionalSaveData(CompoundNBT compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putString("CharacterName", this.getEntityData().get(CHARACTER_NAME));
    }

    @Nullable
    @Override
    public AgeableEntity getBreedOffspring(ServerWorld serverWorld, AgeableEntity ageableEntity) {
        return ModEntityTypes.NPC.get().create(serverWorld);
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld serverWorld, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData livingEntityData, @Nullable CompoundNBT compoundNBT) {
        ECharacterType.ResultTypeNameWithMatchPercentage result = ECharacterType.getNearestCharacterTypeName(new Character());
        characterType = ECharacterType.byId(result.typeName);
        if (characterType != null) {
            this.setCharacterType(characterType);
        }
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
        this.setCharacterName(characterType.getId());
    }

    @Override
    protected ActionResultType mobInteract(PlayerEntity playerEntity, Hand hand) {
        Screen screen = Minecraft.getInstance().screen;
        if (hand == Hand.MAIN_HAND && (!isInteract || screen == null)) {
            this.isInteract = true;
            if (this.getLevel().isClientSide()) {
                MinecraftForge.EVENT_BUS.post(new OpenInfoNpcEvent(playerEntity, this));
            }
        }
        return super.mobInteract(playerEntity, hand);
    }
}
