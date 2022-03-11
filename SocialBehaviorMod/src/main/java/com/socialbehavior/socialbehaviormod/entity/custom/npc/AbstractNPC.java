package com.socialbehavior.socialbehaviormod.entity.custom.npc;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public abstract class AbstractNPC extends AgeableEntity implements INPC{
    protected AbstractNPC(EntityType<? extends AgeableEntity> p_i48581_1_, World p_i48581_2_) {
        super(p_i48581_1_, p_i48581_2_);
    }
}
