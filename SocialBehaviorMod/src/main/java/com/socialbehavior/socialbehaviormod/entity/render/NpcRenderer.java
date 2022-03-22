package com.socialbehavior.socialbehaviormod.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.NpcEntity;
import com.socialbehavior.socialbehaviormod.entity.model.NpcModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NpcRenderer extends MobRenderer<NpcEntity, NpcModel<NpcEntity>> {
    private static final ResourceLocation VILLAGER_BASE_SKIN = new ResourceLocation("textures/entity/villager/villager.png");
    private MatrixStack.Entry last;

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

    @Override
    public void render(NpcEntity npcEntity, float p_225623_2_, float p_225623_3_, MatrixStack matrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
        this.drawName(npcEntity, matrixStack, pBuffer, pPackedLight);
        super.render(npcEntity, p_225623_2_, p_225623_3_, matrixStack, pBuffer, pPackedLight);
    }

    public void drawName(NpcEntity npcEntity, MatrixStack matrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
        String pDisplayName = npcEntity.getCharacterName();
        double d0 = this.entityRenderDispatcher.distanceToSqr(npcEntity);
        if (net.minecraftforge.client.ForgeHooksClient.isNameplateInRenderDistance(npcEntity, d0)) {
            boolean flag = !npcEntity.isDiscrete();
            float f = npcEntity.getBbHeight() + 0.5F;
            matrixStack.pushPose();
            matrixStack.translate(0.0D, (double) f, 0.0D);
            matrixStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            matrixStack.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = matrixStack.last().pose();
            float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
            int j = (int) (f1 * 255.0F) << 24;
            FontRenderer fontrenderer = this.getFont();
            float f2 = (float) (-fontrenderer.width(pDisplayName) / 2);
            fontrenderer.drawInBatch(pDisplayName, f2, (float) 0, 553648127, false, matrix4f, pBuffer, flag, j, pPackedLight);
            if (flag) {
                fontrenderer.drawInBatch(pDisplayName, f2, (float) 0, -1, false, matrix4f, pBuffer, false, 0, pPackedLight);
            }

            matrixStack.popPose();
        }
    }

}