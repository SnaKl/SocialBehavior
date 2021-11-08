package com.socialbehavior.socialbehaviormod.core.init;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ItemGroupInit {
    public static final ItemGroup ITEM_GROUP = new ItemGroup("SocialBehaviorModItemGroup") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemInit.silver_ingot.get());
        }
    };
}
