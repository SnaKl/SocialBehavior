package com.socialbehavior.socialbehaviormod.entity.custom.npc;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.relation.ERelation;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Display the information of the npc on right click
 *
 * @author SnaKi
 * @version 1.0
 * date : 28/04/2022
 */
@OnlyIn(Dist.CLIENT)
public class InfoNpcScreen extends Screen {
    private final NpcEntity npcEntity;
    private final Map<String, Integer> charAttributesMap;
    private int actualScrollPos = 0;
    private int scrollSpeed = 20;

    public InfoNpcScreen(PlayerEntity playerEntity, NpcEntity npcEntity) {
        super(new TranslationTextComponent("NPC infos"));
        this.npcEntity = npcEntity;
        this.charAttributesMap = npcEntity.getNpcData().getCharacterType().getCharacter().getCharacterMap();
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

        String npcUUID = "UUID : " + npcEntity.getNpcData().getUUID().toString();
        int npcUUIDWidth = this.font.width(npcUUID);
        int npcUUIDXPos = this.width / 2 - npcUUIDWidth / 2;
        drawString(pMatrixStack, this.font, npcUUID, npcUUIDXPos, this.actualScrollPos + lineHeight + actualYPos, 0xFFFFFF);
        actualYPos += lineHeight + 10;

        String npcFullName = "Fullname : " + npcEntity.getNpcData().getFullName();
        int npcFullNameWidth = this.font.width(npcFullName);
        int npcFullNameXPos = this.width / 2 - npcFullNameWidth / 2;
        drawString(pMatrixStack, this.font, npcFullName, npcFullNameXPos, this.actualScrollPos + lineHeight + actualYPos, 0xFFFFFF);
        actualYPos += lineHeight + 10;

        IFormattableTextComponent npcGender = new TranslationTextComponent("screen_info.socialbehaviormod.npc.gender").append(" : ").append(npcEntity.getNpcData().getGender().getGenderName());
        int npcGenderWidth = this.font.width(npcGender);
        int npcGenderXPos = this.width / 2 - npcGenderWidth / 2;
        drawString(pMatrixStack, this.font, npcGender, npcGenderXPos, this.actualScrollPos + lineHeight + actualYPos, 0xFFFFFF);
        actualYPos += lineHeight + 10;

        String characterTypeName = "Character : " + npcEntity.getNpcData().getCharacterType().getId();
        int characterNameWidth = this.font.width(characterTypeName);
        int characterNameXPos = this.width / 2 - characterNameWidth / 2;
        drawString(pMatrixStack, this.font, characterTypeName, characterNameXPos, this.actualScrollPos + lineHeight + actualYPos, 0xFFFFFF);
        actualYPos += lineHeight + 10;

        actualYPos = drawCharacterStats(pMatrixStack, actualYPos, lineHeight);
        actualYPos += lineHeight + 10;

        actualYPos = drawLinkedNpc(pMatrixStack, actualYPos, lineHeight);
        actualYPos += lineHeight + 10;

    }

    /**
     * Draws the character stats
     *
     * @param matrixStack the matrix stack
     * @param actualYPos  the actual Y position
     * @param lineHeight  the line height
     * @return actualYPos
     */
    private int drawCharacterStats(MatrixStack matrixStack, int actualYPos, int lineHeight) {
        for (Map.Entry<String, Integer> entry : this.charAttributesMap.entrySet()) {
            int value = (int) (((float) entry.getValue() / 255.0) * 100.0);
            String text = entry.getKey() + " : " + value + "/100";
            int textWidth = this.font.width(text);
            int textXPos = this.width / 2 - textWidth / 2;
            drawString(matrixStack, this.font, text, textXPos, this.actualScrollPos + lineHeight + actualYPos, 0xFFFFFF);
            actualYPos += lineHeight;
        }
        return actualYPos;
    }

    /**
     * Draw linked NPC
     *
     * @param matrixStack matrix stack
     * @param actualYPos  actual Y position
     * @param lineHeight  line height
     * @return actual Y position
     */
    private int drawLinkedNpc(MatrixStack matrixStack, int actualYPos, int lineHeight) {
        for (Map.Entry<ERelation, List<UUID>> entry : this.npcEntity.getNpcData().getRelation().getRelations().entrySet()) {
            ERelation eRelation = entry.getKey();
            List<UUID> uuidList = entry.getValue();
            if (uuidList.isEmpty()) continue;

            IFormattableTextComponent textRelationName = eRelation.getRelationName().append(" : ");
            int textWidthRelationName = this.font.width(textRelationName);
            int textRelationNameXPos = this.width / 2 - textWidthRelationName / 2;
            drawString(matrixStack, this.font, textRelationName, textRelationNameXPos, this.actualScrollPos + lineHeight + actualYPos, 0xFFFFFF);
            actualYPos += lineHeight;

            for (UUID uuid : uuidList) {
                NpcEntity npc = NpcEntity.FindByUUID(uuid);
                if (npc == null) continue;

                String npcFullName = npc.getNpcData().getFullName();
                int textNpcFullName = this.font.width(npcFullName);
                int textNpcFullNameXPos = this.width / 2 - textNpcFullName / 2;
                drawString(matrixStack, this.font, npcFullName, textNpcFullNameXPos, this.actualScrollPos + lineHeight + actualYPos, 0xFFFFFF);
                actualYPos += lineHeight;
            }
        }
        return actualYPos;
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
        if (actualScrollPos < 0) actualScrollPos = 0;
        return super.mouseScrolled(pMouseX, pMouseY, pDelta);
    }

    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    public boolean isPauseScreen() {
        return true;
    }
}
