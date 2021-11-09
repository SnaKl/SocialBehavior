package com.socialbehavior.socialbehaviormod.item;

import com.socialbehavior.socialbehaviormod.SocialBehaviorMod;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SocialBehaviorMod.MOD_ID);

    public static final RegistryObject<Item> silver_ingot = ITEMS.register("silver_ingot", () ->
            new Item(new Item.Properties().tab(ModItemGroup.ITEM_GROUP)));

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }


}
