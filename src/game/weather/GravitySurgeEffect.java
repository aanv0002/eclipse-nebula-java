package game.weather;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.GravityAnchor;

import java.util.ArrayList;
import java.util.List;

/**
 * Weather effect that immediately pulls nearby actors and loose items
 * toward newly spawned GravityAnchor locations.
 *
 * @author Tan Hong Sze
 * @version 2.0
 */
public class GravitySurgeEffect implements WeatherEffect<GravityAnchor> {

    @Override
    public String apply(GameMap map, List<Location> locations, List<GravityAnchor> anchors) {
        int actorsPulled = 0;
        int itemsPulled = 0;

        for (Location anchorLocation : locations) {
            actorsPulled += pullOneAdjacentActor(map, anchorLocation);
            itemsPulled += pullAdjacentItems(anchorLocation);
        }

        return "Gravity surge activates immediately. "
                + actorsPulled + " actor(s) and "
                + itemsPulled + " item(s) are pulled toward the anchor(s).";
    }

    private int pullOneAdjacentActor(GameMap map, Location anchorLocation) {
        if (anchorLocation.containsAnActor()) {
            return 0;
        }

        for (Exit exit : anchorLocation.getExits()) {
            Location adjacent = exit.getDestination();

            if (adjacent.containsAnActor()) {
                Actor actor = adjacent.getActor();
                map.moveActor(actor, anchorLocation);
                return 1;
            }
        }

        return 0;
    }

    private int pullAdjacentItems(Location anchorLocation) {
        int count = 0;

        for (Exit exit : anchorLocation.getExits()) {
            Location adjacent = exit.getDestination();

            for (Item item : new ArrayList<>(adjacent.getItems())) {
                adjacent.removeItem(item);
                anchorLocation.addItem(item);
                count++;
            }
        }

        return count;
    }
}