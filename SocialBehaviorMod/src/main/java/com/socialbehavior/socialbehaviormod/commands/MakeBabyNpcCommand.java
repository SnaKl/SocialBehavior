package com.socialbehavior.socialbehaviormod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.NpcEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MakeBabyNpcCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(new TranslationTextComponent("commands.socialbehaviormod.npc.make_baby.failed"));
    //private static final SimpleCommandExceptionType ERROR_DUPLICATE_UUID = new SimpleCommandExceptionType(new TranslationTextComponent("commands.summon.failed.uuid"));
    private static final SimpleCommandExceptionType ERROR_NO_NPC = new SimpleCommandExceptionType(new TranslationTextComponent("commands.socialbehaviormod.npc.no_NPC"));

    public MakeBabyNpcCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("npc")
                .then(Commands.literal("make_baby")
                        .then(Commands.argument("first_parent_firstName", StringArgumentType.string())
                                .suggests(this::NpcFirstNameSuggestion)
                                .then(Commands.argument("first_parent_lastName", StringArgumentType.string())
                                        .suggests((context, builder) -> NpcLastNameSuggestion(context, builder, StringArgumentType.getString(context, "first_parent_firstName")))
                                        .then(Commands.argument("second_parent_firstName", StringArgumentType.string())
                                                .suggests(this::NpcFirstNameSuggestion)
                                                .then(Commands.argument("second_parent_lastName", StringArgumentType.string())
                                                                .suggests((context, builder) -> NpcLastNameSuggestion(context, builder, StringArgumentType.getString(context, "second_parent_firstName")))
                                                                .executes((commandContext) -> {
                                                                    String firstParentFirstName = StringArgumentType.getString(commandContext, "first_parent_firstName");
                                                                    String firstParentLastName = StringArgumentType.getString(commandContext, "first_parent_lastName");
                                                                    String firstParentFullName = firstParentFirstName + " " + firstParentLastName;

                                                                    String secondParentFirstName = StringArgumentType.getString(commandContext, "second_parent_firstName");
                                                                    String secondParentLastName = StringArgumentType.getString(commandContext, "second_parent_lastName");
                                                                    String secondParentFullName = secondParentFirstName + " " + secondParentLastName;

                                                                    NpcEntity firstParent = NpcEntity.FindByFullName(firstParentFullName);
                                                                    NpcEntity secondParent = NpcEntity.FindByFullName(secondParentFullName);

                                                                    if (firstParent == null) {
                                                                        TranslationTextComponent translationTextComponent = new TranslationTextComponent("commands.socialbehaviormod.npc.not_found", firstParentFullName);
                                                                        throw new SimpleCommandExceptionType(translationTextComponent).create();
                                                                    } else if (secondParent == null) {
                                                                        TranslationTextComponent translationTextComponent = new TranslationTextComponent("commands.socialbehaviormod.npc.not_found", secondParentFullName);
                                                                        throw new SimpleCommandExceptionType(translationTextComponent).create();
                                                                    }
                /*
                else if (firstParent.getGender() == secondParent.getGender()) {

                }
                 */
                                                                    System.out.println("make baby");

                                                                    return 1;
                                                                })
                                                ))))));
    }

    private CompletableFuture<Suggestions> NpcFirstNameSuggestion(CommandContext<CommandSource> context, SuggestionsBuilder builder) {
        List<String> arrayNpcFirstNames = new ArrayList<>();
        if (NpcEntity.NPC_MAP == null || NpcEntity.NPC_MAP.isEmpty()) {
            arrayNpcFirstNames.add("NONE");
        } else {
            for (NpcEntity npc : NpcEntity.NPC_MAP.values()) {
                arrayNpcFirstNames.add(npc.getNpcData().getFirstName());
            }
        }

        return ISuggestionProvider.suggest(arrayNpcFirstNames, builder);
    }

    private CompletableFuture<Suggestions> NpcLastNameSuggestion(CommandContext<CommandSource> context, SuggestionsBuilder builder, String firstName) {
        List<String> arrayNpcLastNames = new ArrayList<>();
        if (NpcEntity.NPC_MAP == null || NpcEntity.NPC_MAP.isEmpty() || firstName.equals("NONE")) {
            arrayNpcLastNames.add("NONE");
        } else {
            for (NpcEntity npc : NpcEntity.NPC_MAP.values()) {
                if (npc.getNpcData().getFirstName().equalsIgnoreCase(firstName))
                    arrayNpcLastNames.add(npc.getNpcData().getLastName());
            }
        }
        return ISuggestionProvider.suggest(arrayNpcLastNames, builder);
    }
}
