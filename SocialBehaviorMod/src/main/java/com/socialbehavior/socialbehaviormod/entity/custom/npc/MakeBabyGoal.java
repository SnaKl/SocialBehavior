package com.socialbehavior.socialbehaviormod.entity.custom.npc;

import com.socialbehavior.socialbehaviormod.entity.ModEntityTypes;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;

import java.util.EnumSet;

/**
 * Npc goal to make a baby
 */
public class MakeBabyGoal extends Goal {
    private final CommandSource commandSource;
    private final ServerWorld serverWorld;
    private final NpcEntity firstParent;
    private final NpcEntity secondParent;
    private boolean babyMake = false;
    private boolean goalError = false;

    /**
     * Initialize this goal
     *
     * @param serverWorld  The world
     * @param firstParent  Reference to the first parent (NpcEntity)
     * @param secondParent Reference to the second parent (NpcEntity)
     */
    public MakeBabyGoal(ServerWorld serverWorld, NpcEntity firstParent, NpcEntity secondParent) {
        this.commandSource = null;
        this.serverWorld = serverWorld;
        this.firstParent = firstParent;
        this.secondParent = secondParent;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
    }

    /**
     * Initialize this goal from command
     *
     * @param commandSource The command source
     * @param firstParent   Reference to the first parent (NpcEntity)
     * @param secondParent  Reference to the second parent (NpcEntity)
     */
    public MakeBabyGoal(CommandSource commandSource, NpcEntity firstParent, NpcEntity secondParent) {
        this.commandSource = commandSource;
        this.serverWorld = commandSource.getLevel();
        this.firstParent = firstParent;
        this.secondParent = secondParent;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
    }

    /**
     * Check if this goal can be executed
     *
     * @return true if npcs can make a baby
     */
    public boolean canUse() {
        return !this.firstParent.isCommandDone && !this.secondParent.isCommandDone;
    }

    /**
     * Check if the goal is completed
     *
     * @return true if goal can continue
     */
    public boolean canContinueToUse() {
        return !this.babyMake && !this.goalError;
    }

    /**
     * On start of the goal
     */
    public void start() {
        MakeParentsLookAtEachOther();
        MoveParentCloser();
    }

    /**
     * On end of the goal
     */
    public void stop() {
        StopParentsNavigation();
    }

    /**
     * Execute the goal every tick
     */
    public void tick() {
        boolean path = MoveParentCloser();
        if (!path && this.commandSource != null) {
            CommandError();
        } else if (!path) {
            stop();
        } else {
            boolean closeEnough = ParentsCloseEnough();
            if (closeEnough) {
                NpcEntity babyNpc = ModEntityTypes.NPC.get().create(serverWorld);
                if (babyNpc != null) {
                    babyNpc.setBaby(true);
                    babyNpc.moveTo(this.firstParent.getX(), this.firstParent.getY(), this.firstParent.getZ(), 0.0F, 0.0F);
                    serverWorld.getLevel().addFreshEntityWithPassengers(babyNpc);
                    babyNpc.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(this.firstParent.blockPosition()), SpawnReason.BREEDING, null, null);
                    babyNpc.setNpcData(babyNpc.getNpcData().setLastName(this.firstParent.getNpcData().getLastName()));
                    this.babyMake = true;
                    ParentsCommandDone();
                } else if (this.commandSource != null) {
                    CommandError();
                }
            }
        }
    }

    /**
     * Make parents Look at each other
     */
    private void MakeParentsLookAtEachOther() {
        this.firstParent.getLookControl().setLookAt(this.secondParent, 10F, 10F);
        this.secondParent.getLookControl().setLookAt(this.firstParent, 10F, 10F);
    }

    /**
     * Move the parents closer to each other
     *
     * @return true if there is a valid path to each other
     */
    private Boolean MoveParentCloser() {
        boolean firstParentPath = false;
        boolean secondParentPath = false;

        StopParentsNavigation();
        firstParentPath = this.firstParent.getNavigation().moveTo(this.secondParent, 1D);
        secondParentPath = this.secondParent.getNavigation().moveTo(this.firstParent, 1D);

        return firstParentPath && secondParentPath;
    }

    /**
     * Check if the parents are close enough to each other
     *
     * @return true if the parents are close enough
     */
    private Boolean ParentsCloseEnough() {
        return this.firstParent.distanceTo(this.secondParent) < 2.0D;
    }

    /**
     * Stop parents navigation
     */
    private void StopParentsNavigation() {
        this.firstParent.getNavigation().stop();
        this.secondParent.getNavigation().stop();
    }

    /**
     * Command error can't make baby
     */
    private void CommandError() {
        if (this.commandSource == null) return;
        StopParentsNavigation();
        ParentsCommandDone();
        String firstParentFullName = this.firstParent.getNpcData().getFullName();
        String secondParentFullName = this.secondParent.getNpcData().getFullName();
        this.commandSource.sendFailure(new TranslationTextComponent("commands.socialbehaviormod.npc.make_baby.cant_make_baby", firstParentFullName, secondParentFullName));
        this.goalError = true;
    }

    /**
     * Set parents command done to true
     */
    private void ParentsCommandDone() {
        if (this.commandSource == null) return;
        this.firstParent.isCommandDone = true;
        this.secondParent.isCommandDone = true;
    }
}
