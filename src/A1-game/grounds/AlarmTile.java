package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.alarm.AlarmSystem;
import game.actors.ActorCapabilities;

/**
 * A tile that triggers the facility alarm when a worker steps onto it.
 *
 * Displayed as '!'.
 *
 * @author Tan Hong Sze
 * @version 1.0
 */
public class AlarmTile extends Ground {

    /**
     * Shared alarm system.
     */
    private final AlarmSystem alarmSystem;

    /**
     * Tracks if a worker was previously on the tile.
     */
    private boolean wasOccupiedByWorker;

    /**
     * Creates an alarm tile.
     *
     * @param alarmSystem the shared alarm system
     */
    public AlarmTile(AlarmSystem alarmSystem) {
        super('!', "Alarm Tile");
        this.alarmSystem = alarmSystem;
        this.wasOccupiedByWorker = false;
    }

    /**
     * Triggers the alarm when a worker steps onto this tile.
     *
     * @param location the tile location
     */
    @Override
    public void tick(Location location) {
        boolean isWorkerHere = false;

        if (location.containsAnActor()) {
            Actor actor = location.getActor();

            if (actor.hasAbility(ActorCapabilities.WORKER)) {
                isWorkerHere = true;

                // triggers when stepping ON
                if (!wasOccupiedByWorker) {
                    alarmSystem.activate();
                    new Display().println(actor + " triggers the facility alarm!");
                }
            }
        }

        wasOccupiedByWorker = isWorkerHere;
    }
}