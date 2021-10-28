package com.socialbehavior.socialbehaviormod.client.render;

import com.socialbehavior.socialbehaviormod.client.model.HogModel;
import com.socialbehavior.socialbehaviormod.entities.HogEntity;
import com.socialbehavior.socialbehaviormod.utils.Reference;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class HogRenderer extends MobRenderer<HogEntity, HogModel<HogEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/entities/hog.png");

    public HogRenderer(EntityRendererManager rendererManagerIn) {
        super(rendererManagerIn, new HogModel<>(), 0.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(HogEntity hogEntity) {
        return TEXTURE;
    }
}
