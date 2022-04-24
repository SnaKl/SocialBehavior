package com.socialbehavior.socialbehaviormod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.MakeBabyGoal;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.NpcEntity;
import com.socialbehavior.socialbehaviormod.entity.custom.npc.data.NpcData;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Command to make a baby NPC from 2 parents
 */
public class MakeBabyNpcCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(new TranslationTextComponent("commands.socialbehaviormod.npc.make_baby.failed"));
    private static final SimpleCommandExceptionType ERROR_NO_NPC = new SimpleCommandExceptionType(new TranslationTextComponent("commands.socialbehaviormod.npc.no_NPC"));

    public MakeBabyNpcCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("npc")
                .then(Commands.literal("make_baby")
                        .then(Commands.argument("first_parent_firstName", StringArgumentType.string())
                                .suggests((context, builder) -> NpcFirstNameSuggestion(context, builder, null))
                                .then(Commands.argument("first_parent_lastName", StringArgumentType.string())
                                        .suggests((context, builder) -> NpcLastNameSuggestion(context, builder, StringArgumentType.getString(context, "first_parent_firstName"), null))
                                        .then(Commands.argument("second_parent_firstName", StringArgumentType.string())
                                                .suggests((context, builder) -> NpcFirstNameSuggestion(context, builder, StringArgumentType.getString(context, "first_parent_firstName")))
                                                .then(Commands.argument("second_parent_lastName", StringArgumentType.string())
                                                        .suggests((context, builder) -> NpcLastNameSuggestion(context, builder, StringArgumentType.getString(context, "second_parent_firstName"), StringArgumentType.getString(context, "first_parent_lastName")))
                                                        .executes((commandContext) -> {
                                                            String firstParentFirstName = StringArgumentType.getString(commandContext, "first_parent_firstName");
                                                            String firstParentLastName = StringArgumentType.getString(commandContext, "first_parent_lastName");
                                                            String firstParentFullName = firstParentFirstName + " " + firstParentLastName;

                                                            String secondParentFirstName = StringArgumentType.getString(commandContext, "second_parent_firstName");
                                                            String secondParentLastName = StringArgumentType.getString(commandContext, "second_parent_lastName");
                                                            String secondParentFullName = secondParentFirstName + " " + secondParentLastName;

                                                            NpcEntity firstParent = NpcEntity.FindByFullName(firstParentFullName);
                                                            NpcEntity secondParent = NpcEntity.FindByFullName(secondParentFullName);

                                                            if (firstParent == null || !firstParent.isAlive()) {
                                                                TranslationTextComponent translationTextComponent = new TranslationTextComponent("commands.socialbehaviormod.npc.not_found", firstParentFullName);
                                                                throw new SimpleCommandExceptionType(translationTextComponent).create();
                                                            } else if (secondParent == null || !secondParent.isAlive()) {
                                                                TranslationTextComponent translationTextComponent = new TranslationTextComponent("commands.socialbehaviormod.npc.not_found", secondParentFullName);
                                                                throw new SimpleCommandExceptionType(translationTextComponent).create();
                                                            }
                                                            //TODO: Check if parents are already married
                                                            //TODO: Check if parents are already siblings
                                                            //TODO: Make family tree
                                                            //TODO: Make marriage
                                                            //TODO: Add gender
                                                            //else if(firstParent.getFamilyTree().getParents().contains(secondParent))
                                                            else if (firstParent.isCommandRunning) {
                                                                throw new SimpleCommandExceptionType(new TranslationTextComponent("commands.socialbehaviormod.npc.command_running", firstParentFullName)).create();
                                                            } else if (secondParent.isCommandRunning) {
                                                                throw new SimpleCommandExceptionType(new TranslationTextComponent("commands.socialbehaviormod.npc.command_running", secondParentFullName)).create();
                                                            } else if (firstParent.isBaby() || secondParent.isBaby()) {
                                                                throw new SimpleCommandExceptionType(new TranslationTextComponent("commands.socialbehaviormod.npc.baby_parents")).create();
                                                            }

                                                            firstParent.isCommandDone = false;
                                                            secondParent.isCommandDone = false;

                                                            firstParent.goalSelector.getRunningGoals().forEach(firstParent.goalSelector::removeGoal);
                                                            secondParent.goalSelector.getRunningGoals().forEach(secondParent.goalSelector::removeGoal);

                                                            firstParent.goalSelector.addGoal(0, new MakeBabyGoal(commandContext.getSource(), firstParent, secondParent));
                                                            Optional<PrioritizedGoal> makeBabyGoal = firstParent.goalSelector.getRunningGoals().filter(goal -> goal.getGoal() instanceof MakeBabyGoal).findFirst();
                                                            makeBabyGoal.ifPresent(prioritizedGoal -> firstParent.goalSelector.removeGoal(prioritizedGoal.getGoal()));
                                                            return 1;
                                                        })
                                                ))))));
    }

    /**
     * Get all firstnames of all NPCs with possibility to filter one firstname
     *
     * @param sourceCommandContext
     * @param suggestionsBuilder
     * @param firstNameAlreadyTake
     * @return Suggestions
     */
    private CompletableFuture<Suggestions> NpcFirstNameSuggestion(CommandContext<CommandSource> sourceCommandContext, SuggestionsBuilder suggestionsBuilder, @Nullable String firstNameAlreadyTake) {
        List<String> arrayNpcFirstNames = new ArrayList<>();
        if (NpcEntity.NPC_MAP == null || NpcEntity.NPC_MAP.isEmpty()) {
            arrayNpcFirstNames.add("NONE");
        } else {
            for (NpcEntity npc : NpcEntity.NPC_MAP.values()) {
                String fullName = npc.getNpcData().getFirstName();
                if (!fullName.equals(firstNameAlreadyTake) && !npc.isBaby())
                    arrayNpcFirstNames.add(fullName);
            }
        }

        return ISuggestionProvider.suggest(arrayNpcFirstNames, suggestionsBuilder);
    }

    /**
     * Get all lastname from given firstname of all NPCs
     *
     * @param sourceCommandContext
     * @param suggestionsBuilder
     * @param firstName
     * @return
     */
    private CompletableFuture<Suggestions> NpcLastNameSuggestion(CommandContext<CommandSource> sourceCommandContext, SuggestionsBuilder suggestionsBuilder, String firstName, @Nullable String lastNameAlreadyTake) {
        List<String> arrayNpcLastNames = new ArrayList<>();
        if (NpcEntity.NPC_MAP == null || NpcEntity.NPC_MAP.isEmpty() || firstName.equals("NONE")) {
            arrayNpcLastNames.add("NONE");
        } else {
            for (NpcEntity npc : NpcEntity.NPC_MAP.values()) {
                NpcData npcData = npc.getNpcData();
                String lastName = npcData.getLastName();
                if (npc.getNpcData().getFirstName().equalsIgnoreCase(firstName) && !lastName.equals(lastNameAlreadyTake) && !npc.isBaby())
                    arrayNpcLastNames.add(lastName);
            }
        }
        return ISuggestionProvider.suggest(arrayNpcLastNames, suggestionsBuilder);
    }
}
