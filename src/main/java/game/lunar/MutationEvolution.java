package game.lunar;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.inventory.BasicInventory;

/**
 * An evolution behaviour triggered by the Blood Moon influence.
 * Replaces selected creatures with a stronger MutatedCreature
 * that has higher health, aggressive worker chasing behaviour
 * and spreads CorruptionGround when walking.
 *
 * @author Raima
 * @version 1.0
 */
public class MutationEvolution implements EvolutionBehaviour {

    /**
     * Creates a MutationEvolution.
     */
    public MutationEvolution() {
    }

    /**
     * Applies the mutation evolution to the target creature.
     * Removes the target from the map and replaces it with
     * a stronger MutatedCreature at the same location.
     *
     * @param target the creature to mutate
     * @param location the location of the target creature
     * @param map the active game map
     * @return a message describing the mutation evolution effect
     */
    @Override
    public String evolve(Actor target, Location location, GameMap map) {
        map.removeActor(target);

        MutatedCreature mutated = new MutatedCreature();
        try {
            location.addActor(mutated);
            String result = "Blood Moon Mutation Evolution: " + target
                    + " mutates into a fearsome " + mutated + "!";
            new Display().println(result);
            return result;
        } catch (Exception e) {
            String result = "Blood Moon Mutation Evolution: mutation failed"
                    + " — no valid location for " + mutated + "!";
            new Display().println(result);
            return result;
        }
    }

    /**
     * Returns the name of this evolution behaviour.
     *
     * @return evolution name
     */
    @Override
    public String getEvolutionName() {
        return "Mutation Evolution";
    }
}