package com.socialbehavior.socialbehaviormod.common.entities;

import com.socialbehavior.socialbehaviormod.SocialBehaviorMod;
import com.socialbehavior.socialbehaviormod.core.init.EntityTypesInit;
import com.socialbehavior.socialbehaviormod.core.init.ItemInit;
import net.minecraft.block.BlockState;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.*;

public class HogEntity extends AnimalEntity {
    private static final Item[] ITEMS = {ItemInit.silver_ingot.get()};
    private static final Ingredient TEMPTATION_ITEMS = Ingredient.of(ITEMS);
    private static final Set<Item> WANTED_ITEMS = new HashSet<>(Arrays.asList(ITEMS));
    private final Inventory inventory = new Inventory(8);
    private EatGrassGoal eatGrassGoal;
    private int hogTimer;

    public HogEntity(EntityType<? extends AnimalEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 12.80)
                .add(Attributes.MOVEMENT_SPEED, 0.4D);
    }

    public Inventory getInventory() {
        return inventory;
    }

    // IA du mob ordre de priorité
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.eatGrassGoal = new EatGrassGoal(this);
        this.goalSelector.addGoal(0, new HogEntity.FindLeapItemGoal());
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, this.eatGrassGoal);
        this.goalSelector.addGoal(3, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1D));
//        this.goalSelector.addGoal(5, new TemptGoal(this, 1D, TEMPTATION_ITEMS, false));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1D));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(9, new LookRandomlyGoal(this));
    }

    @Override
    protected int getExperienceReward(PlayerEntity player) {
        return this.getCommandSenderWorld().random.nextInt(4);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PIGLIN_ANGRY;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BLAZE_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.PIG_STEP, 0.15F, 1.0F);
    }

    @Nullable
    @Override
    public AgeableEntity getBreedOffspring(ServerWorld worldIn, AgeableEntity ageable) {
        return EntityTypesInit.HOG.get().create(worldIn);
    }

    protected void customServerAiStep() {
        this.hogTimer = this.eatGrassGoal.getEatAnimationTick();
        super.customServerAiStep();
    }

    public void aiStep() {
        if (this.level.isClientSide)
            this.hogTimer = Math.max(0, this.hogTimer - 1);
        super.aiStep();
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte id) {
        if (id == 10) this.hogTimer = 40;
        else super.handleEntityEvent(id);
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource p_213333_1_, int p_213333_2_, boolean p_213333_3_) {
        super.dropCustomDeathLoot(p_213333_1_, p_213333_2_, p_213333_3_);

        for (int i = 0; i < this.getInventory().getContainerSize(); ++i) {
            ItemStack itemstack = this.getInventory().getItem(i);
            this.spawnAtLocation(itemstack);
        }
        this.getInventory().removeAllItems();
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        return super.hurt(damageSource, damage);
    }

    public boolean wantsToPickUp(ItemStack itemstack) {
        Item item = itemstack.getItem();
        return WANTED_ITEMS.contains(item) && this.getInventory().canAddItem(itemstack);
    }

    private void pickUpWantedItem(ItemEntity item) {
        ItemStack itemstack = item.getItem().copy();
        if (this.wantsToPickUp(itemstack)) {

            item.remove();
            Inventory inventory = this.getInventory();
            boolean flag = inventory.canAddItem(itemstack);
            if (!flag) {
                return;
            }

            ItemStack newStack = inventory.addItem(itemstack);
            if (!newStack.isEmpty()) {
                itemstack.setCount(newStack.getCount());
            }
        }
    }

    private void pickUpAllWantedNearItems(double pickUpItemBounding) {
        List<ItemEntity> listPickUpItemEntity = this.level.getEntitiesOfClass(ItemEntity.class, HogEntity.this.getBoundingBox().inflate(pickUpItemBounding, pickUpItemBounding, pickUpItemBounding));
        if (listPickUpItemEntity.isEmpty()) return;

        for (ItemEntity itemEntity : listPickUpItemEntity) {
            pickUpWantedItem(itemEntity);
        }
    }

    private ItemEntity getNearWantedItem(double detectionBounding) {
        List<ItemEntity> list = HogEntity.this.level.getEntitiesOfClass(ItemEntity.class, HogEntity.this.getBoundingBox().inflate(detectionBounding, detectionBounding, detectionBounding));
        if (list.isEmpty()) return null;

        for (ItemEntity itemEntity : list) {
            ItemStack itemstack = itemEntity.getItem();
            boolean wantToPickUp = HogEntity.this.wantsToPickUp(itemstack);
            if (wantToPickUp) return itemEntity;
        }

        return null;
    }

    //FindItemsGoal
    class FindLeapItemGoal extends Goal {
        static final double detectionStartBounding = 20.0D;
        static final double detectionJumpingBounding = 4.0D;
        static final double detectionPickUpItemBounding = 1.5D;
        final double speed = Objects.requireNonNull(HogEntity.this.getAttribute(Attributes.MOVEMENT_SPEED)).getValue() * 2.0D;
        Boolean asJump = false;

        public FindLeapItemGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.JUMP, Goal.Flag.MOVE));
        }

        public boolean canUse() {
            ItemEntity itemEntity = HogEntity.this.getNearWantedItem(detectionStartBounding);
            return itemEntity != null;
        }

        public boolean canContinueToUse() {
            return this.canUse();
        }

        public void start() {
            this.asJump = false;

            ItemEntity itemEntity = HogEntity.this.getNearWantedItem(detectionStartBounding);
            assert itemEntity != null;

            ItemStack itemstack = itemEntity.getItem();
            HogEntity.this.getLookControl().setLookAt(itemEntity, 60.0F, 30.0F);
            HogEntity.this.getNavigation().moveTo(itemEntity, speed);

            SocialBehaviorMod.LOGGER.info("START");
        }

        public void stop() {
            //playSound(SoundEvent, volume, pitch)
            HogEntity.this.playSound(SoundEvents.VILLAGER_NO,1F,1F);
        }

        public void tick() {
            ItemEntity jumpItemEntity = HogEntity.this.getNearWantedItem(detectionJumpingBounding);
            if (jumpItemEntity != null) {
                if(HogEntity.this.isOnGround() && !this.asJump){
                    this.asJump = true;
                    HogEntity.this.getNavigation().stop();
                    Vector3d vector3d = (new Vector3d(jumpItemEntity.getX() - HogEntity.this.getX(), jumpItemEntity.getY() - HogEntity.this.getY(), jumpItemEntity.getZ() - HogEntity.this.getZ())).normalize();
                    HogEntity.this.setDeltaMovement(HogEntity.this.getDeltaMovement().add(vector3d.x * 0.7D, 0.7D, vector3d.z * 0.7D));
                }
                if(HogEntity.this.isOnGround()){
                    ItemEntity itemEntity = HogEntity.this.getNearWantedItem(detectionStartBounding);
                    assert itemEntity != null;
                    HogEntity.this.getNavigation().moveTo(itemEntity, speed);
                    HogEntity.this.pickUpAllWantedNearItems(detectionPickUpItemBounding);
                }
            } else {
                ItemEntity itemEntity = HogEntity.this.getNearWantedItem(detectionStartBounding);
                if(itemEntity != null) HogEntity.this.getNavigation().moveTo(itemEntity, speed);
            }
        }
    }
}
