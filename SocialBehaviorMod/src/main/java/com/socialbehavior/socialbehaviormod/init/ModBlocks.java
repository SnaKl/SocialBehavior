package com.socialbehavior.socialbehaviormod.init;

import com.socialbehavior.socialbehaviormod.SocialBehaviorMod;
import com.socialbehavior.socialbehaviormod.utils.ModRegistration;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;

public class ModBlocks {
    public static final RegistryObject<Block> SILVER_ORE = ModRegistration.BLOCKS.register("silver_ore_block", () ->
            new Block(AbstractBlock.Properties.of(Material.STONE)
                    .harvestLevel(1)
                    .harvestTool(ToolType.PICKAXE)
                    .strength(5f)
                    .requiresCorrectToolForDrops()));

    public static void register() {
        SocialBehaviorMod.LOGGER.info("REGISTER BLOCK");
    }

}
