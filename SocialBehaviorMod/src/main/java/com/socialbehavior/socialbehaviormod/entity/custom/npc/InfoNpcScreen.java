package com.socialbehavior.socialbehaviormod.entity.custom.npc;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class InfoNpcScreen extends Screen {
    private final NpcEntity npcEntity;
    private final Map<String, Integer> charAttributesMap;
    private int actualScrollPos = 0;
    private int scrollSpeed = 20;

    public InfoNpcScreen(PlayerEntity playerEntity, NpcEntity npcEntity) {
        super(new TranslationTextComponent("NPC infos"));
        this.npcEntity = npcEntity;
        this.charAttributesMap = npcEntity.getCharacterType().getCharacter().getCharacterMap();
    }

    protected void init() {

    }


    public boolean shouldCloseOnEsc() {
        return true;
    }

    public void render(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        renderBackground(pMatrixStack);
        int actualYPos = 0;
        int lineHeight = this.font.lineHeight;

        String npcFullName = npcEntity.getFullName();
        int npcFullNameWidth = this.font.width(npcFullName);
        int npcFullNameXPos = this.width / 2 - npcFullNameWidth / 2;
        drawString(pMatrixStack, this.font, npcFullName, npcFullNameXPos, this.actualScrollPos + lineHeight + actualYPos, 0xFFFFFF);
        actualYPos += lineHeight + 10;

        String characterTypeName = "Character : " + npcEntity.getCharacterTypeData();
        int characterNameWidth = this.font.width(characterTypeName);
        int characterNameXPos = this.width / 2 - characterNameWidth / 2;
        drawString(pMatrixStack, this.font, characterTypeName, characterNameXPos, this.actualScrollPos + lineHeight + actualYPos, 0xFFFFFF);
        actualYPos += lineHeight + 10;

        drawCharacterStats(pMatrixStack, actualYPos, lineHeight);
    }

    private void drawCharacterStats(MatrixStack pMatrixStack, int actualYPos, int lineHeight) {
        for (Map.Entry<String, Integer> entry : this.charAttributesMap.entrySet()) {
            int value = (int) (((float) entry.getValue() / 255.0) * 100.0);
            String text = entry.getKey() + " : " + value + "/100";
            int textWidth = this.font.width(text);
            int textXPos = this.width / 2 - textWidth / 2;
            drawString(pMatrixStack, this.font, text, textXPos, this.actualScrollPos + lineHeight + actualYPos, 0xFFFFFF);
            actualYPos += lineHeight;
        }
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        if (pDelta > 1.0D) {
            pDelta = 1.0D;
        }

        if (pDelta < -1.0D) {
            pDelta = -1.0D;
        }
        actualScrollPos += pDelta * scrollSpeed;
        return super.mouseScrolled(pMouseX, pMouseY, pDelta);
    }


    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    public boolean isPauseScreen() {
        return true;
    }

}
