package com.socialbehavior.socialbehaviormod.entity.custom.npc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.socialbehavior.socialbehaviormod.SocialBehaviorMod;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.character.Character;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.character.ECharacterType;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.data.NpcData;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.relation.Relation;
import com.socialbehavior.socialbehaviormod.utils.DataSerializers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.monster.ZoglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.RangedInteger;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public class NpcEntity extends AbstractNPC {
    private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LIVING_ENTITIES, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN);
    private static final ImmutableList<SensorType<? extends Sensor<? super NpcEntity>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.NEAREST_BED, SensorType.HURT_BY);
    private static final DataParameter<NpcData> DATA_NPC_DATA = EntityDataManager.defineId(NpcEntity.class, DataSerializers.NPC_DATA);
    @Nullable
    public static Map<String, NpcEntity> NPC_MAP = null;
    public boolean isCommandDone = false;
    public boolean isCommandRunning = false;
    private boolean isInteract;

    public NpcEntity(EntityType<? extends AgeableEntity> entityType, World world) {
        super(entityType, world);
        this.isInteract = false;
        this.setNpcData(this.getNpcData());
        if (!world.isClientSide) {
            if (NPC_MAP == null) {
                NPC_MAP = Maps.newHashMap();

            }
            NPC_MAP.putIfAbsent(this.getStringUUID(), this);
        }
    }

    /**
     * Find npc with is fullName in NPC_MAP with no case sensitive
     *
     * @param fullName full name of npc
     * @return npc if found, null if not found
     */
    @Nullable
    public static NpcEntity FindByFullName(String fullName) {
        if (NPC_MAP == null || NPC_MAP.isEmpty()) return null;
        for (NpcEntity npcEntity : NPC_MAP.values()) {
            if (npcEntity.getNpcData().getFullName().equalsIgnoreCase(fullName)) {
                return npcEntity;
            }
        }
        return null;
    }

    /**
     * Find npc with is UUID in NPC_MAP from saved uuid
     *
     * @param uuid uuid of npc
     * @return npc if found, null if not found
     */
    @Nullable
    public static NpcEntity FindByUUID(UUID uuid) {
        if (NPC_MAP == null || NPC_MAP.isEmpty()) return null;
        for (NpcEntity npcEntity : NPC_MAP.values()) {
            if (npcEntity.getNpcData().getUIID().equals(uuid)) {
                return npcEntity;
            }
        }
        return null;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_NPC_DATA, new NpcData(ECharacterType.BRAVE, this.getUUID(), "firstname", "lastname", new Relation()));
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
    public NpcEntity getBreedOffspring(ServerWorld serverWorld, AgeableEntity ageableEntity) {
        return null;
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld serverWorld, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData livingEntityData, @Nullable CompoundNBT compoundNBT) {
        ECharacterType.ResultTypeNameWithMatchPercentage result = ECharacterType.getNearestCharacterTypeName(new Character());
        if (result.type != null) {
            this.setNpcData(this.getNpcData().setCharacterType(result.type));
        } else {
            this.setNpcData(this.getNpcData().setCharacterType(ECharacterType.getRandomCharacterType()));
        }

        String firstName = SocialBehaviorMod.FAKER.name().firstName();
        String lastName = SocialBehaviorMod.FAKER.name().lastName();
        this.setNpcData(this.getNpcData().setFullName(firstName + " " + lastName));

        this.setNpcData(this.getNpcData().setUUID(this.getUUID()));

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

    public void setCharacterType(ECharacterType characterType) {
        this.setNpcData(this.getNpcData().setCharacterType(characterType));
    }

    public Boolean getInteract() {
        return this.isInteract;
    }

    /**
     * Makes the entity despawn if requirements are reached
     */
    @Override
    public void checkDespawn() {
    }

    public Brain<NpcEntity> getBrain() {
        return (Brain<NpcEntity>) super.getBrain();
    }

    protected Brain<?> makeBrain(Dynamic<?> pDynamic) {
        System.out.println("makeBrain");
        Brain<NpcEntity> brain = this.brainProvider().makeBrain(pDynamic);
        this.registerBrainGoals(brain);
        return brain;
    }

    protected Brain.BrainCodec<NpcEntity> brainProvider() {
        System.out.println("brainProvider");
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    public void refreshBrain(ServerWorld pServerLevel) {
        System.out.println("refreshBrain");
        Brain<NpcEntity> brain = this.getBrain();
        brain.stopAll(pServerLevel, this);
        this.brain = brain.copyWithoutBehaviors();
        this.registerBrainGoals(this.getBrain());
    }

    private void registerBrainGoals(Brain<NpcEntity> pVillagerBrain) {
        System.out.println("registerBrainGoals");
        pVillagerBrain.addActivity(Activity.CORE, 0, ImmutableList.of(new LookTask(45, 90), new WalkToTargetTask()));
        pVillagerBrain.addActivity(Activity.IDLE, 10, ImmutableList.<net.minecraft.entity.ai.brain.task.Task<? super ZoglinEntity>>of(new RunSometimesTask(new LookAtEntityTask(8.0F), RangedInteger.of(30, 60)), new FirstShuffledTask(ImmutableList.of(Pair.of(new WalkRandomlyTask(0.4F), 2), Pair.of(new WalkTowardsLookTargetTask(0.4F, 3), 2), Pair.of(new DummyTask(30, 60), 1)))));

        pVillagerBrain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        pVillagerBrain.setDefaultActivity(Activity.IDLE);
        //pVillagerBrain.useDefaultActivity();
        pVillagerBrain.setActiveActivityIfPossible(Activity.IDLE);
    }

    @Override
    protected void customServerAiStep() {
        this.level.getProfiler().push("npcBrain");
        this.getBrain().tick((ServerWorld) this.level, this);
        this.level.getProfiler().pop();
        super.customServerAiStep();
    }


}
