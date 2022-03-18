package com.socialbehavior.socialbehaviormod.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.NpcEntity;
import com.socialbehavior.socialbehaviormod.entity.model.NpcModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NpcRenderer extends MobRenderer<NpcEntity, NpcModel<NpcEntity>> {
    private static final ResourceLocation VILLAGER_BASE_SKIN = new ResourceLocation("textures/entity/villager/villager.png");

    public NpcRenderer(EntityRendererManager rendererManagerIn) {
        super(rendererManagerIn, new NpcModel<>(0.0F), 0.5F);
    }

    public ResourceLocation getTextureLocation(NpcEntity npcEntity) {
        return VILLAGER_BASE_SKIN;
    }

    protected void scale(NpcEntity npcEntity, MatrixStack matrixStack, float scale) {
        float f = 0.9375F;
        if (npcEntity.isBaby()) {
            f = (float) ((double) f * 0.5D);
            this.shadowRadius = 0.25F;
        } else {
            this.shadowRadius = 0.5F;
        }

        matrixStack.scale(f, f, f);
    }
}