package game.security;

import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.ChargedFloor;
import game.statuses.StunnedStatus;
import edu.monash.fit2099.engine.displays.Display;

/**
 * High-voltage structural pillar permanently converting path terrain.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public class StaticPylon extends Ground implements SecurityHazard {

    private int tickCount;
    private boolean active;

    public StaticPylon() {
        super('P', "Static Pylon");
        this.tickCount = 0;
        this.active = true;
    }

    @Override
    public void tick(Location location) {
        if (!active) return;
        tickCount++;

        if (tickCount >= 8) {
            tickCount = 0;
            if (!location.getExits().isEmpty()) {
                Exit direction = location.getExits().get(0);
                new Display().println("The Static Pylon discharges violently towards the " + direction.getName() + "!");
                propagateArc(direction.getDestination(), direction.getName(), 3);
            }
        }
    }

    private void propagateArc(Location current, String dirName, int remainingRange) {
        if (remainingRange <= 0) return;

        current.setGround(new ChargedFloor());
        if (current.containsAnActor()) {
            current.getActor().addStatus(new StunnedStatus(1));
        }

        for (Exit exit : current.getExits()) {
            if (exit.getName().equals(dirName)) {
                propagateArc(exit.getDestination(), dirName, remainingRange - 1);
                break;
            }
        }
    }

    @Override
    public void disarm(Location location) {
        this.active = false;
        new Display().println("The Static Pylon's internal breaker was tripped. Disarmed safely.");
    }

    @Override
    public boolean isActive() {
        return active;
    }
}