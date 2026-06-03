package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.ActorCapabilities;
import game.spawners.CreatureSpawnService;
import game.spawners.CreatureSpawner;
import game.statuses.PoisonStatus;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.Undead;
import game.items.Cuttable;
import game.items.IndustrialFan;

/**
 * A motion-activated vent in 20-overflow.
 * A vent only spawns when a worker is standing in one of its adjacent tiles.
 * After a successful spawn, it poisons all adjacent actors and the newly spawned
 * creature for 5 turns.
 *
 * @author Shrabonti Majumder
 * @author Raima
 * @version 2.0
 */
public class Vent extends Ground implements Cuttable{

    /**
     * Duration of poison applied by the vent.
     */
    private static final int POISON_DURATION = 5;

    /**
     * Damage per turn from vent poison.
     */
    private static final int POISON_DAMAGE = 1;

    /**
     * The spawner used by this vent.
     */
    private final CreatureSpawner creatureSpawner;

    /**
     * Service used to place creatures and apply spawn reactions.
     */
    private final CreatureSpawnService spawnService;

    /**
     * Creates a vent.
     *
     * @param creatureSpawner the creature spawner
     */
    public Vent(CreatureSpawner creatureSpawner) {
        super('V', "Vent");
        this.creatureSpawner = creatureSpawner;
        this.spawnService = new CreatureSpawnService();
    }

    /**
     * Ticks the vent.
     * If a worker is adjacent, it attempts a motion-triggered spawn.
     *
     * @param location the location of this ground
     */
    @Override
    public void tick(Location location) {
        if (!hasAdjacentWorker(location)) {
            return;
        }

        Actor creature = creatureSpawner.spawnCreature();
        if (spawnService.spawnAt(location, creature)) {
            poisonSpawnArea(location);
            new Display().println(creature + " crawls out of a vent at " + location + ".");
        }
    }

    /**
     * Returns whether a worker is adjacent to the vent.
     *
     * @param location the vent location
     * @return true if a worker is adjacent
     */
    private boolean hasAdjacentWorker(Location location) {
        for (Exit exit : location.getExits()) {
            Actor actor = exit.getDestination().getActor();
            if (actor != null && actor.hasAbility(ActorCapabilities.WORKER)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Poisons the spawned actor and every actor adjacent to the vent.
     *
     * @param location the vent location
     */
    private void poisonSpawnArea(Location location) {
        poisonActorAt(location);
        for (Exit exit : location.getExits()) {
            poisonActorAt(exit.getDestination());
        }
    }

    /**
     * Poisons the actor at a location if one exists.
     *
     * @param location the location to poison
     */
    private void poisonActorAt(Location location) {
        Actor actor = location.getActor();
        if (actor != null) {
            actor.addStatus(new PoisonStatus(POISON_DURATION, POISON_DAMAGE));
        }
    }

    /**
     * Cuts the vent using a PlasmaCutter.
     * Drops an IndustrialFan on the floor and transforms the tile into a Floor.
     * Instantly spawns an Undead on that exact tile using the spawn service.
     *
     * @param actor the actor performing the cut
     * @param map the current game map
     * @return the result message of the cut including any side effects
     */
    @Override
    public String cut(Actor actor, GameMap map) {
        Location ventLocation = map.locationOf(actor);

        // ADDED: find vent location from actor's exits
        for (Exit exit : map.locationOf(actor).getExits()) {
            if (exit.getDestination().getGround() == this) {
                ventLocation = exit.getDestination();
                break;
            }
        }

        // ADDED: drop IndustrialFan on floor
        ventLocation.addItem(new IndustrialFan());

        // ADDED: transform tile into Floor
        ventLocation.setGround(new Floor());

        // ADDED: spawn Undead on that exact tile using spawn service
        Undead undead = new Undead();
        String result = actor + " cuts the Vent! An Industrial Fan is dropped on the floor.";

        if (spawnService.spawnAt(ventLocation, undead)) {
            result += " An Undead crawls out of the vent!";
        } else {
            result += " An Undead tried to crawl out but the tile was occupied!";
        }
        return result;
    }

    /**
     * Returns the menu text fragment for the cut action.
     *
     * @return menu text fragment
     */
    @Override
    public String getCutDescription() {
        return "Vent";
    }
}
