package game.grounds;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.BuyAction;
import game.actions.DepositAction;
import game.actions.SellAction;
import game.actors.ActorCapabilities;
import game.items.*;
import game.quota.QuotaSystem;

/**
 * A Supercomputer terminal located inside the armoured ship.
 * Workers can interact with it to sell scraps found on the moon or purchase
 * equipment using credits stored in their wallet.
 * Also manages the company quota cycle, firing workers on deadline
 * and resetting quota when met.
 *
 * @author Raima
 * @author Shrabonti Majumder
 * @version 3.0
 */
public class Supercomputer extends Ground {

    /**
     * The quota system managing company credits and quota cycle.
     */
    private final QuotaSystem quotaSystem;

    /**
     * Creates a Supercomputer terminal with a quota system.
     *
     * @param quotaSystem the quota system to manage company credits
     */
    public Supercomputer(QuotaSystem quotaSystem) {
        super('≡', "Supercomputer");
        this.quotaSystem = quotaSystem;
    }

    /**
     * Returns the list of actions available when a worker is adjacent to the
     * Supercomputer.
     *
     * @param actor the actor interacting with the Supercomputer
     * @param location the location of the Supercomputer
     * @param direction the direction of the Supercomputer from the actor
     * @return list of available actions
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        ActionList actions = new ActionList();

        if (!actor.hasAbility(ActorCapabilities.WORKER)) {
            return actions;
        }

        if (direction.isEmpty()) {
            return actions;
        }

        WalletCarrier walletCarrier = actor.asCapability(WalletCarrier.class).orElse(null);
        if (walletCarrier == null) {
            return actions;
        }

        for (Item item : actor.getInventory().getItems()) {
            item.asCapability(Sellable.class).ifPresent(sellable -> {
                actions.add(new SellAction(item, sellable));
            });
        }

        // ADDED: deposit actions for each depositable item in inventory
        for (Item item : actor.getInventory().getItems()) {
            item.asCapability(Depositable.class).ifPresent(depositable ->
                    actions.add(new DepositAction(item, depositable, quotaSystem))
            );
        }

        actions.add(new BuyAction(new FirstAidKit()));
        actions.add(new BuyAction(new SterilisationBox()));
        actions.add(new BuyAction(new AccessCardLevel1()));
        actions.add(new BuyAction(new AccessCardLevel2()));
        actions.add(new BuyAction(new AccessCardLevel3()));
        actions.add(new BuyAction(new PlasmaCutter())); // ADDED: buy PlasmaCutter

        return actions;
    }

    /**
     * Ticks the Supercomputer every turn.
     * Checks if quota is met or deadline is reached and acts accordingly.
     * If quota is met, resets and increases quota and time limit.
     * If deadline is reached, fires adjacent workers.
     * QuotaSystem.tick() is called from EclipseNebula.gameLoop() not here
     * to avoid double ticking from multiple Supercomputers.
     *
     * @param location the location of the Supercomputer
     */
    @Override
    public void tick(Location location) {
        if (quotaSystem.isQuotaMet()) {
            quotaSystem.resetAndIncrease();
        } else if (quotaSystem.isDeadlineReached()) {
            quotaSystem.fireAdjacentWorkers(location, location.map());
        }
    }

    /**
     * Prevents actors from entering the Supercomputer tile.
     * Workers must stand adjacent to interact with the terminal.
     *
     * @param actor the actor attempting to enter
     * @return false always, the Supercomputer is impassable
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return false;
    }
}