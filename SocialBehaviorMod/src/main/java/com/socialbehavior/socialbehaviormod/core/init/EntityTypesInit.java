package com.socialbehavior.socialbehaviormod.core.init;

import com.socialbehavior.socialbehaviormod.common.entities.HogEntity;
import com.socialbehavior.socialbehaviormod.core.util.Reference;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityTypesInit {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Reference.MOD_ID);

    //Entities types
    public static final RegistryObject<EntityType<HogEntity>> HOG = ENTITY_TYPES.register("hog",
            () -> EntityType.Builder.of(HogEntity::new, EntityClassification.CREATURE)
                    .sized(0.9F, 1.4F)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(Reference.MOD_ID, "hog").toString()));

    public static void register(IEventBus modEventBus) {
        ENTITY_TYPES.register(modEventBus);
    }
}
