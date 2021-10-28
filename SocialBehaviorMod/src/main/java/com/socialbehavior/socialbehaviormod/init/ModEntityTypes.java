package com.socialbehavior.socialbehaviormod.init;

import com.socialbehavior.socialbehaviormod.SocialBehaviorMod;
import com.socialbehavior.socialbehaviormod.entities.HogEntity;
import com.socialbehavior.socialbehaviormod.utils.ModRegistration;
import com.socialbehavior.socialbehaviormod.utils.Reference;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes {
    public static void register() {
        SocialBehaviorMod.LOGGER.info("REGISTER ENTITY");
    }

    //Entities types
    public static final RegistryObject<EntityType<HogEntity>> HOG = ModRegistration.ENTITY_TYPES.register("hog",
            () -> EntityType.Builder.of(HogEntity::new, EntityClassification.CREATURE)
                    .sized(0.9F, 1.4F)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(Reference.MOD_ID, "hog").toString()));

}
