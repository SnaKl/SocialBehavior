package com.socialbehavior.socialbehaviormod.init;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

public class ModItemGroups extends ItemGroup {
    public static final ModItemGroups SB_ITEM_GROUP = new ModItemGroups("SocialBehaviorItemGroup", () -> new ItemStack(ModItems.SILVER_INGOT_ITEM.get()));
    private final Supplier<ItemStack> displayStack;

    private ModItemGroups(String label, Supplier<ItemStack> displayStack) {
        super(label);
        this.displayStack = displayStack;
    }

    @Override
    public ItemStack makeIcon() {
        return displayStack.get();
    }
}
