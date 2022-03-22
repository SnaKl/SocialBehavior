package com.socialbehavior.socialbehaviormod.entity.custom.npc.character;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.NpcEntity;
import com.socialbehavior.socialbehaviormod.utils.Serializer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

//public abstract class AbstractFurnaceScreen<T extends AbstractFurnaceContainer> extends ContainerScreen<T> implements IRecipeShownListener
//public class DeathScreen extends Screen
@OnlyIn(Dist.CLIENT)
public class InfoNpcScreen extends Screen {
    private final NpcEntity npcEntity;
    private final Map<String, Object> charAttributesMap;
    private int actualScrollPos = 0;
    private int scrollSpeed = 20;

    public InfoNpcScreen(PlayerEntity playerEntity, NpcEntity npcEntity) {
        super(new TranslationTextComponent("NPC infos"));
        this.npcEntity = npcEntity;
        this.charAttributesMap = Serializer.object2Map(npcEntity.getCharacterType().getCharacter());
    }

    protected void init() {

    }


    public boolean shouldCloseOnEsc() {
        return true;
    }

    public void render(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        renderBackground(pMatrixStack);
        int actualYPos = 0;
        int lineHeight = this.minecraft.font.lineHeight;
        String characterName = "Character : " + npcEntity.getCharacterName();
        int characterNameWidth = this.minecraft.font.width(characterName);
        int characterNameHeight = this.width / 2 - characterNameWidth / 2;
        drawString(pMatrixStack, this.font, characterName, characterNameHeight, actualScrollPos + lineHeight + actualYPos, 0xFFFFFF);
        actualYPos += lineHeight;

        for (Map.Entry<String, Object> entry : this.charAttributesMap.entrySet()) {
            String text = entry.getKey() + " : " + entry.getValue();
            int textWidth = this.minecraft.font.width(text);
            int textHeight = this.width / 2 - textWidth / 2;
            drawString(pMatrixStack, this.font, text, textHeight, actualScrollPos + lineHeight + actualYPos, 0xFFFFFF);
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
