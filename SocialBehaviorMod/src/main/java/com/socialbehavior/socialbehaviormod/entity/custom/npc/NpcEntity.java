package com.socialbehavior.socialbehaviormod.entity.custom.npc;

import com.socialbehavior.socialbehaviormod.SocialBehaviorMod;
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
    private static final DataParameter<String> NPC_CHARACTER_TYPE = EntityDataManager.defineId(NpcEntity.class, DataSerializers.STRING);
    private static final DataParameter<String> NPC_NAME = EntityDataManager.defineId(NpcEntity.class, DataSerializers.STRING);
    private ECharacterType characterType;
    private Boolean isInteract;
    private String firstName;
    private String lastName;

    public NpcEntity(EntityType<? extends AgeableEntity> entityType, World world) {
        super(entityType, world);
        this.isInteract = false;
        this.setCharacterTypeData(this.getCharacterTypeData());
        this.setFullName(this.getFullNameData());
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(NPC_CHARACTER_TYPE, "");
        this.getEntityData().define(NPC_NAME, "");
    }

    public void onSyncedDataUpdated(DataParameter<?> dataParameter) {
        super.onSyncedDataUpdated(dataParameter);
        if (NPC_CHARACTER_TYPE.equals(dataParameter)) {
            ECharacterType characterType = ECharacterType.byId(this.getEntityData().get(NPC_CHARACTER_TYPE));
            if (characterType != null) {
                this.setCharacterType(characterType);
            }
        }
        if (NPC_NAME.equals(dataParameter)) {
            String fullName = this.getEntityData().get(NPC_NAME);
            if (!fullName.isEmpty()) {
                this.setFullName(fullName);
            } else if (fullName.equals("")) {
                this.setFirstName("");
                this.setLastName("");
            }
        }
    }

    public void readAdditionalSaveData(CompoundNBT compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        this.setCharacterTypeData(compoundNBT.getString("NpcCharacterType"));
        this.setFullName(compoundNBT.getString("NpcName"));
    }

    public void addAdditionalSaveData(CompoundNBT compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putString("NpcCharacterType", this.getEntityData().get(NPC_CHARACTER_TYPE));
        compoundNBT.putString("NpcName", this.getEntityData().get(NPC_NAME));
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
        String lastName = SocialBehaviorMod.FAKER.name().lastName();
        String firstName = SocialBehaviorMod.FAKER.name().firstName();
        this.setFullName(firstName + " " + lastName);

        return super.finalizeSpawn(serverWorld, difficultyInstance, spawnReason, livingEntityData, compoundNBT);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
    }

    @Override
    protected ActionResultType mobInteract(PlayerEntity playerEntity, Hand hand) {
        Screen screen = Minecraft.getInstance().screen;
        if (screen == null) {
            this.isInteract = false;
        }
        if (hand == Hand.MAIN_HAND && !isInteract) {
            if (this.getLevel().isClientSide()) {
                MinecraftForge.EVENT_BUS.post(new OpenInfoNpcEvent(playerEntity, this));
            }
        }
        return super.mobInteract(playerEntity, hand);
    }

    public String getCharacterTypeData() {
        return this.getEntityData().get(NPC_CHARACTER_TYPE);
    }

    public void setCharacterTypeData(String characterName) {
        this.getEntityData().set(NPC_CHARACTER_TYPE, characterName);
    }

    public String getFullNameData() {
        return this.getEntityData().get(NPC_NAME);
    }

    public void setFullNameData(String fullName) {
        this.getEntityData().set(NPC_NAME, fullName);
    }

    public ECharacterType getCharacterType() {
        return characterType;
    }

    public void setCharacterType(ECharacterType characterType) {
        this.characterType = characterType;
        this.setCharacterTypeData(characterType.getId());
    }

    public Boolean getInteract() {
        return this.isInteract;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        this.setFullNameData(this.getFullName());
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        this.setFullNameData(this.getFullName());
    }

    public String getFullName() {
        return this.getFirstName() + " " + this.getLastName();
    }

    public void setFullName(String fullName) {
        String[] name = fullName.split(" ");
        if (name.length == 2) {
            this.setFirstName(name[0]);
            this.setLastName(name[1]);
            this.setFullNameData(this.getFullName());
        }
    }
}
