package game.security;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.items.ItemStatistics;
import java.util.ArrayList;
import java.util.List;

/**
 * Imploding industrial scrap causing 3-tile dragging anomaly.
 *
 * @author Shrabonti Majumder
 * @version 1.1
 */
public class GravityCore extends Item implements SecurityHazard {

    private boolean active;

    public GravityCore() {
        super("Gravity Core", 'G');
        this.active = true;
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(15));
        this.makePortable();
    }

    @Override
    public void tick(Location location) {
        if (!active) return;

        boolean targetInRange = false;
        for (Location loc : location.getNearbyLocations(1)) {
            if (loc.containsAnActor()) {
                targetInRange = true;
                break;
            }
        }

        if (targetInRange) {
            new Display().println("The Gravity Core forcefully collapses and implodes!");
            GameMap map = location.map();

            for (Location targetLoc : location.getNearbyLocations(3)) {
                if (targetLoc == location) continue;

                if (targetLoc.containsAnActor()) {
                    Actor dragged = targetLoc.getActor();
                    Location pullDest = calculateDragPath(targetLoc, location, dragged);
                    if (pullDest != null && !pullDest.containsAnActor()) {
                        map.moveActor(dragged, pullDest);
                        new Display().println(dragged + " is dragged forcefully by the gravity anomaly!");
                    }
                } else {
                    Location pullDest = calculateDragPath(targetLoc, location, null);
                    if (pullDest != null) {
                        List<Item> itemsToMove = new ArrayList<>(targetLoc.getItems());
                        for (Item item : itemsToMove) {
                            targetLoc.removeItem(item);
                            pullDest.addItem(item);
                        }
                    }
                }
            }
            location.removeItem(this);
            active = false;
        }
    }

    private Location calculateDragPath(Location current, Location epicentre, Actor actor) {
        int bestDist = Math.abs(current.x() - epicentre.x()) + Math.abs(current.y() - epicentre.y());
        Location bestChoice = null;

        for (Exit exit : current.getExits()) {
            Location adj = exit.getDestination();
            int dist = Math.abs(adj.x() - epicentre.x()) + Math.abs(adj.y() - epicentre.y());
            if (dist < bestDist && (actor == null || adj.canActorEnter(actor))) {
                bestDist = dist;
                bestChoice = adj;
            }
        }
        return bestChoice;
    }

    @Override
    public void disarm(Location location) {
        this.active = false;
        new Display().println("The Gravity Core has been safely neutralized.");
    }

    @Override
    public boolean isActive() {
        return active;
    }
}