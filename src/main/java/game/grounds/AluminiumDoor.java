package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.doors.*;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import game.items.AluminiumScrap;
import game.items.Cuttable;
import java.util.Random;
import edu.monash.fit2099.engine.positions.Exit;

/**
 * A level 1 aluminium door.
 *
 * This door can be unlocked by an access card with level 1 or higher clearance.
 * Unlocking it applies a shock effect to the actor.
 *
 * @author Tan Hong Sze
 * @author Raima
 * @version 2.0
 */
public class AluminiumDoor extends Ground implements Unlockable, Cuttable {

    /**
     * Whether this door has been unlocked.
     */
    private boolean unlocked;

    /**
     * Effect applied when this door is unlocked.
     */
    private final DoorEffect effect;

    /**
     * Chance of explosion when cut.
     */
    private static final double EXPLOSION_CHANCE = 0.25;

    /**
     * Damage dealt to adjacent entities on explosion.
     */
    private static final int EXPLOSION_DAMAGE = 100;

    /**
     * Random number generator for explosion chance.
     */
    private final Random random = new Random();

    /**
     * Creates a locked aluminium door.
     */
    public AluminiumDoor() {
        super('=', "Aluminium Door");
        this.unlocked = false;
        this.effect = new ShockDoorEffect();
    }

    /**
     * Unlocks this door.
     */
    @Override
    public void unlock() {
        unlocked = true;
    }

    /**
     * Returns whether this door is unlocked.
     *
     * @return true if unlocked
     */
    @Override
    public boolean isUnlocked() {
        return unlocked;
    }

    /**
     * Returns the clearance level required to unlock this door.
     *
     * @return level 1 clearance
     */
    @Override
    public ClearanceLevel getRequiredLevel() {
        return ClearanceLevel.LEVEL_1;
    }

    /**
     * Applies this door's unlock effect.
     *
     * @param actor the actor unlocking the door
     * @param doorLocation the location of this door
     */
    @Override
    public void applyEffect(Actor actor, Location doorLocation) {
        effect.apply(actor, doorLocation);
    }

    /**
     * Allows actors to enter only after the door is unlocked.
     *
     * @param actor the actor attempting to enter
     * @return true if the door is unlocked
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return unlocked;
    }

    /**
     * Cuts the aluminium door using a PlasmaCutter.
     * Drops AluminiumScrap on the floor and transforms the tile into a Floor.
     * There is a 25% chance of an explosion dealing 100 damage to all
     * adjacent entities.
     *
     * @param actor the actor performing the cut
     * @param map the current game map
     * @return the result message of the cut including any side effects
     */
    @Override
    public String cut(Actor actor, GameMap map) { // ADDED
        Location doorLocation = map.locationOf(actor);

        // find door location from actor's exits
        for (Exit exit : map.locationOf(actor).getExits()) {
            if (exit.getDestination().getGround() == this) {
                doorLocation = exit.getDestination();
                break;
            }
        }

        // ADDED: drop AluminiumScrap on floor
        doorLocation.addItem(new AluminiumScrap());

        // ADDED: transform tile into Floor
        doorLocation.setGround(new Floor());

        String result = actor + " cuts the Aluminium Door! "
                + "Aluminium Scrap is dropped on the floor.";

        // ADDED: 25% chance of explosion
        if (random.nextDouble() < EXPLOSION_CHANCE) {
            result += " The door EXPLODES dealing " + EXPLOSION_DAMAGE
                    + " damage to nearby entities!";
            for (Exit exit : doorLocation.getExits()) {
                Location adjacent = exit.getDestination();
                if (adjacent.containsAnActor()) {
                    adjacent.getActor().hurt(EXPLOSION_DAMAGE);
                    new Display().println(adjacent.getActor()
                            + " is caught in the explosion and takes "
                            + EXPLOSION_DAMAGE + " damage!");
                }
            }
        }
        return result;
    }

    /**
     * Returns the menu text fragment for the cut action.
     *
     * @return menu text fragment
     */
    @Override
    public String getCutDescription() { // ADDED
        return "Aluminium Door";
    }
}