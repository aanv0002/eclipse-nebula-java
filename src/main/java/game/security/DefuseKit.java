package game.security;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.DefuseSecurityAction;
import game.items.ItemStatistics;

/**
 * Used to rewrite safety flags of active corporate infrastructure.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public class DefuseKit extends Item implements GridCountermeasure {

    public static final int WEIGHT = 2;

    public DefuseKit() {
        super("Defuse Kit", 'k');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }

    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = new ActionList();
        Location here = map.locationOf(owner);

        for (Exit exit : here.getExits()) {
            Location adj = exit.getDestination();

            SecurityHazard groundHazard = adj.getGroundAs(SecurityHazard.class);
            if (groundHazard != null && groundHazard.isActive()) {
                actions.add(new DefuseSecurityAction(groundHazard, adj));
            }

            SecurityHazard actorHazard = adj.getActorAs(SecurityHazard.class);
            if (actorHazard != null && actorHazard.isActive()) {
                actions.add(new DefuseSecurityAction(actorHazard, adj));
            }

            for (Item item : adj.getItems()) {
                item.asCapability(SecurityHazard.class).ifPresent(itemHazard -> {
                    if (itemHazard.isActive()) {
                        actions.add(new DefuseSecurityAction(itemHazard, adj));
                    }
                });
            }
        }
        return actions;
    }
    @Override
    public String getCountermeasureType() { return "Defuse"; }
}