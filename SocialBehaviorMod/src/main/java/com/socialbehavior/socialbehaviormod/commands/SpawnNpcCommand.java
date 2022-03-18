package com.socialbehavior.socialbehaviormod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.socialbehavior.socialbehaviormod.entity.ModEntityTypes;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.NpcEntity;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.types.ECharacterType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.Vec3Argument;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class SpawnNpcCommand {
    public SpawnNpcCommand(CommandDispatcher<CommandSource> dispatcher) {

        dispatcher.register(Commands.literal("npc")
                .then(Commands.literal("spawn")
                        .then(Commands.argument("character", StringArgumentType.string())
                                .suggests(ECharacterType.TYPE)
                                .executes((commandContext) -> {
                                    String character = StringArgumentType.getString(commandContext, "character");
                                    return spawnNpc(commandContext.getSource(), character, null);
                                })
                                .then(Commands.argument("pos", Vec3Argument.vec3()).executes((commandContext) -> {
                                            String character = StringArgumentType.getString(commandContext, "character");
                                            return spawnNpc(commandContext.getSource(), character, Vec3Argument.getVec3(commandContext, "pos"));
                                        })
                                ))));
    }

    private int spawnNpc(CommandSource commandSource, String character, @Nullable Vector3d position) throws CommandSyntaxException {
        ECharacterType characterType = ECharacterType.byId(character);
        if (characterType == null) {
            commandSource.sendFailure(ITextComponent.nullToEmpty("Bad character"));
            return 0;
        }

        ServerWorld serverworld = commandSource.getLevel();
        if (position == null) {
            position = commandSource.getPosition();
        }
        BlockPos blockpos = new BlockPos(position);

        NpcEntity npcEntity = new NpcEntity(ModEntityTypes.NPC_TYPE, serverworld);
        npcEntity.setCharacterType(characterType);
        npcEntity.setPos(position.x, position.y, position.z);
        npcEntity.finalizeSpawn(serverworld, serverworld.getCurrentDifficultyAt(blockpos), SpawnReason.COMMAND, (ILivingEntityData) null, (CompoundNBT) null);

        if (!serverworld.tryAddFreshEntityWithPassengers(npcEntity)) {
            commandSource.sendFailure(ITextComponent.nullToEmpty("FAIL spawn NPC"));
            return 0;
        } else {
            commandSource.sendSuccess(ITextComponent.nullToEmpty("Spawn NPC with " + character + " as character"), true);
        }

        return 1;
    }
}
