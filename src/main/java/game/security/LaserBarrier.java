package game.security;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.DeployBarrierAction;
import game.items.ItemStatistics;

/**
 * Countermeasure object projecting a laser barrier to defend a coordinate.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public class LaserBarrier extends Item implements GridCountermeasure {

    public LaserBarrier() {
        super("Laser Barrier Kit", 'L');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(6));
        this.makePortable();
    }

    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = new ActionList();
        Location here = map.locationOf(owner);

        for (Exit exit : here.getExits()) {
            Location adj = exit.getDestination();
            if (adj.canActorEnter(owner) && !adj.containsAnActor()) {
                actions.add(new DeployBarrierAction(this, adj, exit.getName()));
            }
        }
        return actions;
    }
    @Override
    public String getCountermeasureType() { return "Barrier"; }
}