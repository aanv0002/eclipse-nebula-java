package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.PurgeableTerrain;

/**
 * Wipes out 3x3 radius of mutated terrains entirely.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public class PurgeTerrainAction extends Action {

    private final Item canister;

    public PurgeTerrainAction(Item canister) {
        this.canister = canister;
    }

    @Override
    public String execute(Actor actor, GameMap map) {
        Location here = map.locationOf(actor);
        int cleared = 0;

        for (Location loc : here.getNearbyLocations(1)) {
            PurgeableTerrain pt = loc.getGroundAs(PurgeableTerrain.class);
            if (pt != null) {
                pt.purge(loc);
                cleared++;
            }
        }

        PurgeableTerrain centrePt = here.getGroundAs(PurgeableTerrain.class);
        if (centrePt != null) {
            centrePt.purge(here);
            cleared++;
        }

        actor.getInventory().remove(canister);
        return actor + " unleashes the Purge Canister, neutralizing " + cleared + " hazardous tile(s) in the vicinity.";
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor + " activates the Purge Canister to wipe 3x3 hazardous terrain";
    }
}