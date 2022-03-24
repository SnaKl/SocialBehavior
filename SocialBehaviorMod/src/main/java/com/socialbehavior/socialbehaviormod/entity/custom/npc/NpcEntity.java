package com.socialbehavior.socialbehaviormod.entity.custom.npc;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.socialbehavior.socialbehaviormod.SocialBehaviorMod;
import com.socialbehavior.socialbehaviormod.entity.ModEntityTypes;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.character.Character;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.character.ECharacterType;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.data.NpcData;
import com.socialbehavior.socialbehaviormod.utils.DataSerializers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.network.datasync.DataParameter;
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
    //private static final DataParameter<String> NPC_CHARACTER_TYPE = EntityDataManager.defineId(NpcEntity.class, DataSerializers.STRING);
    //private static final DataParameter<String> NPC_NAME = EntityDataManager.defineId(NpcEntity.class, DataSerializers.STRING);
    private static final DataParameter<NpcData> DATA_NPC_DATA = EntityDataManager.defineId(VillagerEntity.class, DataSerializers.NPC_DATA);
    private ECharacterType characterType;
    private Boolean isInteract;

    public NpcEntity(EntityType<? extends AgeableEntity> entityType, World world) {
        super(entityType, world);
        this.isInteract = false;
        this.setNpcData(this.getNpcData().setCharacterNameType("").setFullName(""));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_NPC_DATA, new NpcData("brave", "firstname", "lastname"));
    }

    public NpcData getNpcData() {
        return this.entityData.get(DATA_NPC_DATA);
    }

    public void setNpcData(NpcData npcData) {
        this.entityData.set(DATA_NPC_DATA, npcData);
    }

    public void onSyncedDataUpdated(DataParameter<?> pKey) {
        super.onSyncedDataUpdated(pKey);
        if (DATA_NPC_DATA.equals(pKey)) {
            this.setNpcData(this.getEntityData().get(DATA_NPC_DATA));
        }
    }

    public void readAdditionalSaveData(CompoundNBT compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);

        if (compoundNBT.contains("NpcData", 10)) {
            DataResult<NpcData> dataResult = NpcData.CODEC.parse(new Dynamic<>(NBTDynamicOps.INSTANCE, compoundNBT.get("NpcData")));
            dataResult.resultOrPartial(LOGGER::error).ifPresent(this::setNpcData);
        }
    }

    public void addAdditionalSaveData(CompoundNBT compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        NpcData.CODEC.encodeStart(NBTDynamicOps.INSTANCE, this.getNpcData()).resultOrPartial(LOGGER::error).ifPresent((villagerData) -> {
            compoundNBT.put("NpcData", villagerData);
        });
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
            this.setNpcData(this.getNpcData().setCharacterNameType(characterType.getId()));
        }

        String firstName = SocialBehaviorMod.FAKER.name().firstName();
        String lastName = SocialBehaviorMod.FAKER.name().lastName();
        this.setNpcData(this.getNpcData().setFullName(firstName + " " + lastName));

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

    public ECharacterType getCharacterType() {
        return ECharacterType.byId(this.getNpcData().getCharacterNameType());
    }

    public void setCharacterType(ECharacterType characterType) {
        this.characterType = characterType;
        this.setNpcData(this.getNpcData().setCharacterNameType(characterType.getId()));
    }

    public Boolean getInteract() {
        return this.isInteract;
    }
}
