package com.socialbehavior.socialbehaviormod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.socialbehavior.socialbehaviormod.SocialBehaviorMod;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.NpcEntity;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.character.ECharacterType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.Vec3Argument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;

public class SpawnNpcCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(new TranslationTextComponent("commands.summon.failed"));
    private static final SimpleCommandExceptionType ERROR_DUPLICATE_UUID = new SimpleCommandExceptionType(new TranslationTextComponent("commands.summon.failed.uuid"));

    public SpawnNpcCommand(CommandDispatcher<CommandSource> dispatcher) {

        dispatcher.register(Commands.literal("npc")
                .then(Commands.literal("spawn")
                        .then(Commands.argument("character", StringArgumentType.string())
                                .suggests(ECharacterType.TYPE)
                                .executes((commandContext) -> {
                                    String character = StringArgumentType.getString(commandContext, "character");
                                    return spawnNpc(commandContext.getSource(), character, commandContext.getSource().getPosition());
                                })
                                .then(Commands.argument("pos", Vec3Argument.vec3()).executes((commandContext) -> {
                                            String character = StringArgumentType.getString(commandContext, "character");
                                            return spawnNpc(commandContext.getSource(), character, Vec3Argument.getVec3(commandContext, "pos"));
                                        })
                                ))));
    }

    private int spawnNpc(CommandSource commandSource, String character, Vector3d position) throws CommandSyntaxException {
        ECharacterType characterType = ECharacterType.byId(character);
        if (characterType == null) {
            commandSource.sendFailure(ITextComponent.nullToEmpty("Bad character"));
            return 0;
        }

        ServerWorld serverworld = commandSource.getLevel();
        CompoundNBT compoundNBT = new CompoundNBT();

        compoundNBT.putString("id", SocialBehaviorMod.MOD_ID + ":npc");
        Entity entity = EntityType.loadEntityRecursive(compoundNBT, serverworld, (ent) -> {
            ent.moveTo(position.x, position.y, position.z, ent.yRot, ent.xRot);
            return ent;
        });

        if (entity == null) {
            throw ERROR_FAILED.create();
        } else {
            if (entity instanceof NpcEntity) {
                ((NpcEntity) entity).finalizeSpawn(serverworld, serverworld.getCurrentDifficultyAt(entity.blockPosition()), SpawnReason.COMMAND, (ILivingEntityData) null, (CompoundNBT) null);
                ((NpcEntity) entity).setCharacterType(characterType);
            }

            if (!serverworld.tryAddFreshEntityWithPassengers(entity)) {
                throw ERROR_DUPLICATE_UUID.create();
            } else {
                commandSource.sendSuccess(new TranslationTextComponent("commands.summon.success", entity.getDisplayName()), true);
                return 1;
            }
        }
    }
}
