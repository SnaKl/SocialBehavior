package com.socialbehavior.socialbehaviormod.core.init;

import com.socialbehavior.socialbehaviormod.core.util.Reference;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);

    public static final RegistryObject<Item> silver_ingot = ITEMS.register("silver_ingot", () ->
            new Item(new Item.Properties().tab(ItemGroupInit.ITEM_GROUP)));

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }


}
