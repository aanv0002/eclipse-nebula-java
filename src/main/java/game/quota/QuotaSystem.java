package game.quota;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Manages the company quota cycle for the Eclipse Nebula facility.
 * Tracks company credits deposited by workers, the current quota target,
 * the turn counter and the time limit per quota cycle.
 * When the quota is met, the quota and time limit are increased and reset.
 * When the deadline is reached without meeting the quota, adjacent workers
 * are fired and made unconscious.
 *
 * @author Raima
 * @version 1.0
 */
public class QuotaSystem {

    /**
     * Base quota target in company credits.
     */
    public static final int BASE_QUOTA = 100;

    /**
     * Base time limit in game turns.
     */
    public static final int BASE_TIME_LIMIT = 200;

    /**
     * Percentage increase applied to quota when met.
     */
    private static final double QUOTA_INCREASE = 0.05;

    /**
     * Percentage increase applied to time limit when quota is met.
     */
    private static final double TIME_LIMIT_INCREASE = 0.10;

    /**
     * Current quota target in company credits.
     */
    private int quotaTarget;

    /**
     * Current company credits deposited this cycle.
     */
    private int companyCredits;

    /**
     * Current turn counter.
     */
    private int turnCount;

    /**
     * Current time limit in turns.
     */
    private int timeLimit;

    /**
     * Tracks whether workers have already been fired this cycle
     * to prevent double firing from multiple Supercomputers.
     */
    private boolean firedThisCycle; // ADDED

    /**
     * Creates a new QuotaSystem with base quota and time limit.
     */
    public QuotaSystem() {
        this.quotaTarget = BASE_QUOTA;
        this.companyCredits = 0;
        this.turnCount = 0;
        this.timeLimit = BASE_TIME_LIMIT;
        this.firedThisCycle = false; // ADDED
    }

    /**
     * Adds company credits toward the current quota.
     *
     * @param amount the amount of company credits to add
     */
    public void addCompanyCredits(int amount) {
        this.companyCredits += amount;
        new Display().println("Company Credits: " + companyCredits + "/" + quotaTarget);
    }

    /**
     * Returns whether the current quota has been met.
     *
     * @return true if company credits >= quota target
     */
    public boolean isQuotaMet() {
        return companyCredits >= quotaTarget;
    }

    /**
     * Returns whether the deadline has been reached.
     *
     * @return true if turn count >= time limit
     */
    public boolean isDeadlineReached() {
        return turnCount >= timeLimit;
    }

    /**
     * Returns current company credits.
     *
     * @return current company credits
     */
    public int getCompanyCredits() {
        return companyCredits;
    }

    /**
     * Returns current quota target.
     *
     * @return current quota target
     */
    public int getQuotaTarget() {
        return quotaTarget;
    }

    /**
     * Returns remaining turns before deadline.
     *
     * @return remaining turns
     */
    public int getRemainingTurns() {
        return timeLimit - turnCount;
    }

    /**
     * Resets and increases quota by 5% rounded up.
     * Resets and increases time limit by 10% rounded up.
     * Resets company credits to zero.
     */
    public void resetAndIncrease() {
        quotaTarget = (int) Math.ceil(quotaTarget * (1 + QUOTA_INCREASE));
        timeLimit = (int) Math.ceil(timeLimit * (1 + TIME_LIMIT_INCREASE));
        companyCredits = 0;
        turnCount = 0;
        firedThisCycle = false; // ADDED: reset for next cycle
        new Display().println("Quota met! New quota: " + quotaTarget
                + " company credits in " + timeLimit + " turns.");
    }

    /**
     * Fires all workers adjacent to the Supercomputer by dealing
     * lethal damage to make them unconscious.
     * Called when the deadline is reached without meeting the quota.
     * Uses firedThisCycle flag to prevent double firing from multiple
     * Supercomputers sharing the same QuotaSystem instance.
     *
     * @param supercomputerLocation the location of the Supercomputer
     * @param map the current game map
     */
    public void fireAdjacentWorkers(Location supercomputerLocation, GameMap map) {
        if (firedThisCycle) return; // ADDED: prevent double firing
        firedThisCycle = true; // ADDED

        for (Exit exit : supercomputerLocation.getExits()) {
            Location adjacent = exit.getDestination();
            if (adjacent.containsAnActor()) {
                Actor worker = adjacent.getActor();
                worker.hurt(worker.getStatistic(ActorStatistics.HEALTH));
                new Display().println(worker + " has been fired by the Supercomputer"
                        + " for failing to meet the quota!");
            }
        }
        companyCredits = 0;
        turnCount = 0;
        firedThisCycle = false; // ADDED: reset after firing so next cycle works
        new Display().println("Quota deadline reached! New quota cycle begins."
                + " Quota: " + quotaTarget + " company credits in "
                + timeLimit + " turns.");
    }

    /**
     * Increments the turn counter by one.
     * Called once per turn from EclipseNebula.gameLoop().
     */
    public void tick() {
        turnCount++;
        new Display().println("Quota: " + companyCredits + "/" + quotaTarget
                + " | Turns remaining: " + getRemainingTurns());
    }
}