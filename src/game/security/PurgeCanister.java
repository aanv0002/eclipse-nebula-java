package game.security;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.PurgeTerrainAction;
import game.items.ItemStatistics;

/**
 * Canister allowing environmental reclamation of tiles.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public class PurgeCanister extends Item implements GridCountermeasure {

    public PurgeCanister() {
        super("Purge Canister", 'p');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(4));
        this.makePortable();
    }

    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = new ActionList();
        actions.add(new PurgeTerrainAction(this));
        return actions;
    }
}