package game.grounds;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.BuyAction;
import game.actions.SellAction;
import game.actors.ActorCapabilities;
import game.items.*;

/**
 * A Supercomputer terminal located inside the armoured ship.
 * Workers can interact with it to sell scraps found on the moon or purchase
 * equipment using credits stored in their wallet.
 *
 * @author Raima
 * @author Shrabonti Majumder
 * @version 2.0
 */
public class Supercomputer extends Ground {

    /**
     * Creates a Supercomputer terminal.
     */
    public Supercomputer() {
        super('≡', "Supercomputer");
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

        actions.add(new BuyAction(new FirstAidKit()));
        actions.add(new BuyAction(new SterilisationBox()));
        actions.add(new BuyAction(new AccessCardLevel1()));
        actions.add(new BuyAction(new AccessCardLevel2()));
        actions.add(new BuyAction(new AccessCardLevel3()));

        return actions;
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
        return false; }
}
