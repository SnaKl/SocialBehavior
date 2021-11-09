package com.socialbehavior.socialbehaviormod.entity.render;

import com.socialbehavior.socialbehaviormod.SocialBehaviorMod;
import com.socialbehavior.socialbehaviormod.entity.model.HogModel;
import com.socialbehavior.socialbehaviormod.entity.custom.HogEntity;
import com.socialbehavior.socialbehaviormod.core.util.Reference;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class HogRenderer extends MobRenderer<HogEntity, HogModel<HogEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(SocialBehaviorMod.MOD_ID, "textures/entities/hog.png");

    public HogRenderer(EntityRendererManager rendererManagerIn) {
        super(rendererManagerIn, new HogModel<>(), 0.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(HogEntity hogEntity) {
        return TEXTURE;
    }
}
