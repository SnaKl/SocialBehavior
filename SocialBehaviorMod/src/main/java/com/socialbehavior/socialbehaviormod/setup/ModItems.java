package com.socialbehavior.socialbehaviormod.setup;

import com.socialbehavior.socialbehaviormod.SocialBehaviorMod;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

public class ModItems {
    public static final RegistryObject<Item> SILVER_INGOT_ITEM = ModRegistration.ITEMS.register("silver_ingot_item", () ->
            new Item(new Item.Properties().tab(ModItemGroup.SB_ITEM_GROUP)));

    public static final RegistryObject<BlockItem> SILVER_ORE_BLOCK = ModRegistration.ITEMS.register("silver_ore_block", () ->
            new BlockItem(ModBlocks.SILVER_ORE.get(),
                    new Item.Properties().tab(ModItemGroup.SB_ITEM_GROUP)));

    static void register() {
        SocialBehaviorMod.LOGGER.info("REGISTER ITEMS");
    }
}
