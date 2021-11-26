package com.socialbehavior.socialbehaviormod.minimap.data;

import com.socialbehavior.socialbehaviormod.SocialBehaviorMod;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

public class TestData extends WorldSavedData {
    public static final String NAME_FILE = (SocialBehaviorMod.MOD_ID + "_map");
    private RegistryKey<World> dimension;

    public TestData() {
        super(NAME_FILE);
    }

    public TestData(String name) {
        super(name);
    }

    public static TestData getInstance(ServerWorld world){
        DimensionSavedDataManager dimensionSavedDataManager = world.getDataStorage();
        TestData testData = dimensionSavedDataManager.computeIfAbsent(TestData::new, TestData.NAME_FILE);
        testData.setDimension(world.dimension());
        return testData;
    }

//    public static TestData forWorld(ServerWorld world) {
//        SocialBehaviorMod.LOGGER.info("forWorld forWorld");
//        String dimension = world.dimension().location().toString();
//        DimensionSavedDataManager dimensionSavedDataManager = world.getDataStorage();
//        return dimensionSavedDataManager.computeIfAbsent(TestData::new, TestData.NAME_FILE);
//    }

    @Override
    public void load(CompoundNBT compoundNBT) {


    }

    @Override
    public CompoundNBT save(CompoundNBT compoundNBT) {
        SocialBehaviorMod.LOGGER.info("SAVE SAVE");

        compoundNBT.putString("dimension", this.dimension.location().toString());

        return compoundNBT;
    }

    public void setDimension(RegistryKey<World> dimension) {
        this.dimension = dimension;
        this.setDirty();
    }
}
