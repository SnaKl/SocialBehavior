package com.socialbehavior.socialbehaviormod.entity;

import com.socialbehavior.socialbehaviormod.SocialBehaviorMod;
import com.socialbehavior.socialbehaviormod.entity.custom.HogEntity;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.NpcEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, SocialBehaviorMod.MOD_ID);

    //Entities types
    public static final RegistryObject<EntityType<HogEntity>> HOG = ENTITY_TYPES.register("hog",
            () -> EntityType.Builder.of(HogEntity::new, EntityClassification.CREATURE)
                    .sized(0.9F, 1.4F)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(SocialBehaviorMod.MOD_ID, "hog").toString()));

    public static final EntityType<NpcEntity> NPC_TYPE = EntityType.Builder.of(NpcEntity::new, EntityClassification.CREATURE)
            .sized(0.6F, 1.98F)
            .build(new ResourceLocation("textures/entity/villager/villager.png").toString());
    public static final RegistryObject<EntityType<NpcEntity>> NPC = ENTITY_TYPES.register("npc", () -> NPC_TYPE);

    public static void register(IEventBus modEventBus) {
        ENTITY_TYPES.register(modEventBus);
    }
}
