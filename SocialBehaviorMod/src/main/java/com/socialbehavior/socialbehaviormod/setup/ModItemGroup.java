package com.socialbehavior.socialbehaviormod.setup;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

public class ModItemGroup extends ItemGroup {
    public static final ModItemGroup SB_ITEM_GROUP = new ModItemGroup("SocialBehaviorItemGroup", () -> new ItemStack(ModItems.SILVER_INGOT_ITEM.get()));
    private final Supplier<ItemStack> displayStack;

    private ModItemGroup(String label, Supplier<ItemStack> displayStack) {
        super(label);
        this.displayStack = displayStack;
    }

    @Override
    public ItemStack makeIcon() {
        return displayStack.get();
    }
}
