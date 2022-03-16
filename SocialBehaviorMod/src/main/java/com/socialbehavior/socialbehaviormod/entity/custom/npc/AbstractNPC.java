package com.socialbehavior.socialbehaviormod.entity.custom.npc;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class AbstractNPC extends AgeableEntity {
    protected AbstractNPC(EntityType<? extends AgeableEntity> entityType, World world) {
        super(entityType, world);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 12.80)
                .add(Attributes.FOLLOW_RANGE, 48.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.4D);
    }

    private final Inventory inventory = new Inventory(8);

    public Inventory getInventory() {
        return this.inventory;
    }

    public boolean canBeLeashed(PlayerEntity playerEntity) {
        return false;
    }

    public boolean setSlot(int p_174820_1_, ItemStack itemStack) {
        if (super.setSlot(p_174820_1_, itemStack)) {
            return true;
        } else {
            int i = p_174820_1_ - 300;
            if (i >= 0 && i < this.inventory.getContainerSize()) {
                this.inventory.setItem(i, itemStack);
                return true;
            } else {
                return false;
            }
        }
    }

    public World getLevel() {
        return this.level;
    }
}
