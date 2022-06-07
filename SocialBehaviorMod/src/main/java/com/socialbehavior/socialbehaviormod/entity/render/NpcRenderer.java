package com.socialbehavior.socialbehaviormod.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.socialbehavior.socialbehaviormod.SocialBehaviorMod;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.NpcEntity;
import com.socialbehavior.socialbehaviormod.entity.model.NpcModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NpcRenderer extends MobRenderer<NpcEntity, NpcModel<NpcEntity>> {
    protected static final ResourceLocation BASE_SKIN = new ResourceLocation(SocialBehaviorMod.MOD_ID, "textures/entities/npctransgender.png");

    public NpcRenderer(EntityRendererManager rendererManagerIn) {
        this(rendererManagerIn, false);
    }

    public NpcRenderer(EntityRendererManager rendererManagerIn, boolean slim) {
        super(rendererManagerIn, new NpcModel<>(0.0F, slim), 0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new BipedModel(0.5F), new BipedModel(1.0F)));
        this.addLayer(new HeldItemLayer<>(this));
        this.addLayer(new HeadLayer<>(this));
        this.addLayer(new ElytraLayer<>(this));
    }

    private static BipedModel.ArmPose getArmPose(AbstractClientPlayerEntity p_241741_0_, Hand p_241741_1_) {
        ItemStack itemstack = p_241741_0_.getItemInHand(p_241741_1_);
        if (itemstack.isEmpty()) {
            return BipedModel.ArmPose.EMPTY;
        } else {
            if (p_241741_0_.getUsedItemHand() == p_241741_1_ && p_241741_0_.getUseItemRemainingTicks() > 0) {
                UseAction useaction = itemstack.getUseAnimation();
                if (useaction == UseAction.BLOCK) {
                    return BipedModel.ArmPose.BLOCK;
                }

                if (useaction == UseAction.BOW) {
                    return BipedModel.ArmPose.BOW_AND_ARROW;
                }

                if (useaction == UseAction.SPEAR) {
                    return BipedModel.ArmPose.THROW_SPEAR;
                }

                if (useaction == UseAction.CROSSBOW && p_241741_1_ == p_241741_0_.getUsedItemHand()) {
                    return BipedModel.ArmPose.CROSSBOW_CHARGE;
                }
            } else if (!p_241741_0_.swinging && itemstack.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(itemstack)) {
                return BipedModel.ArmPose.CROSSBOW_HOLD;
            }

            return BipedModel.ArmPose.ITEM;
        }
    }

    @Override
    public ResourceLocation getTextureLocation(NpcEntity npcEntity) {
        return npcEntity.getNpcData().getGender().getTexture();
    }

    @Override
    public void render(NpcEntity npcEntity, float pEntityYaw, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
        this.drawName(npcEntity, pMatrixStack, pBuffer, pPackedLight);
        super.render(npcEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    public void drawName(NpcEntity npcEntity, MatrixStack matrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
        String pDisplayName = npcEntity.getNpcData().getFullName();
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


    public Vector3d getRenderOffset(NpcEntity pEntity, float pPartialTicks) {
        return pEntity.isCrouching() ? new Vector3d(0.0D, -0.125D, 0.0D) : super.getRenderOffset(pEntity, pPartialTicks);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(AbstractClientPlayerEntity pEntity) {
        return pEntity.getSkinTextureLocation();
    }

    protected void scale(AbstractClientPlayerEntity pLivingEntity, MatrixStack pMatrixStack, float pPartialTickTime) {
        float f = 0.9375F;
        pMatrixStack.scale(0.9375F, 0.9375F, 0.9375F);
    }

    public void renderRightHand(MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pCombinedLight, NpcEntity npcEntity) {
        this.renderHand(pMatrixStack, pBuffer, pCombinedLight, npcEntity, (this.model).rightArm, (this.model).rightSleeve);
    }

    public void renderLeftHand(MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pCombinedLight, NpcEntity npcEntity) {
        this.renderHand(pMatrixStack, pBuffer, pCombinedLight, npcEntity, (this.model).leftArm, (this.model).leftSleeve);
    }

    private void renderHand(MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pCombinedLight, NpcEntity npcEntity, ModelRenderer pRendererArm, ModelRenderer pRendererArmwear) {
        NpcModel<NpcEntity> playermodel = this.getModel();
        playermodel.attackTime = 0.0F;
        playermodel.crouching = false;
        playermodel.swimAmount = 0.0F;
        playermodel.setupAnim(npcEntity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        pRendererArm.xRot = 0.0F;
        pRendererArm.render(pMatrixStack, pBuffer.getBuffer(RenderType.entitySolid(npcEntity.getNpcData().getGender().getTexture())), pCombinedLight, OverlayTexture.NO_OVERLAY);
        pRendererArmwear.xRot = 0.0F;
        pRendererArmwear.render(pMatrixStack, pBuffer.getBuffer(RenderType.entityTranslucent(npcEntity.getNpcData().getGender().getTexture())), pCombinedLight, OverlayTexture.NO_OVERLAY);
    }

    protected void setupRotations(NpcEntity npcEntity, MatrixStack pMatrixStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
        float f = npcEntity.getSwimAmount(pPartialTicks);
        if (npcEntity.isFallFlying()) {
            super.setupRotations(npcEntity, pMatrixStack, pAgeInTicks, pRotationYaw, pPartialTicks);
            float f1 = (float) npcEntity.getFallFlyingTicks() + pPartialTicks;
            float f2 = MathHelper.clamp(f1 * f1 / 100.0F, 0.0F, 1.0F);
            if (!npcEntity.isAutoSpinAttack()) {
                pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(f2 * (-90.0F - npcEntity.xRot)));
            }

            Vector3d vector3d = npcEntity.getViewVector(pPartialTicks);
            Vector3d vector3d1 = npcEntity.getDeltaMovement();
            double d0 = Entity.getHorizontalDistanceSqr(vector3d1);
            double d1 = Entity.getHorizontalDistanceSqr(vector3d);
            if (d0 > 0.0D && d1 > 0.0D) {
                double d2 = (vector3d1.x * vector3d.x + vector3d1.z * vector3d.z) / Math.sqrt(d0 * d1);
                double d3 = vector3d1.x * vector3d.z - vector3d1.z * vector3d.x;
                pMatrixStack.mulPose(Vector3f.YP.rotation((float) (Math.signum(d3) * Math.acos(d2))));
            }
        } else if (f > 0.0F) {
            super.setupRotations(npcEntity, pMatrixStack, pAgeInTicks, pRotationYaw, pPartialTicks);
            float f3 = npcEntity.isInWater() ? -90.0F - npcEntity.xRot : -90.0F;
            float f4 = MathHelper.lerp(f, 0.0F, f3);
            pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(f4));
            if (npcEntity.isVisuallySwimming()) {
                pMatrixStack.translate(0.0D, -1.0D, (double) 0.3F);
            }
        } else {
            super.setupRotations(npcEntity, pMatrixStack, pAgeInTicks, pRotationYaw, pPartialTicks);
        }

    }

}