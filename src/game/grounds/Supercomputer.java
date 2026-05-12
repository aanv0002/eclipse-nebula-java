package game.grounds;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.BuyAction;
import game.actions.SellAction;
import game.actors.ActorCapabilities;
import game.items.AccessCardLevel1;
import game.items.AccessCardLevel2;
import game.items.AccessCardLevel3;
import game.items.FirstAidKit;
import game.items.Sellable;
import game.items.SterilisationBox;

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
        super('S', "Supercomputer");
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

        for (Item item : actor.getInventory().getItems()) {
            item.asCapability(Sellable.class)
                    .ifPresent(sellable -> actions.add(new SellAction(item, sellable)));
        }

        actions.add(new BuyAction(new FirstAidKit()));
        actions.add(new BuyAction(new SterilisationBox()));
        actions.add(new BuyAction(new AccessCardLevel1()));
        actions.add(new BuyAction(new AccessCardLevel2()));
        actions.add(new BuyAction(new AccessCardLevel3()));

        return actions;
    }
}
