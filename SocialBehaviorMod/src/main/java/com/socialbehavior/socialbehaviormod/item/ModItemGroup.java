package com.socialbehavior.socialbehaviormod.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup {
    public static final ItemGroup ITEM_GROUP = new ItemGroup("SocialBehaviorModItemGroup") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.silver_ingot.get());
        }
    };
}
