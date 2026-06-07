package game.lunar;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;
import game.actors.ActorCapabilities;
import game.behaviours.AttackNearbyWorkerBehaviour;
import game.behaviours.FollowNearestWorkerBehaviour;
import game.inventory.BasicInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A powerful mutated creature created by MutationEvolution during
 * the Blood Moon lunar influence. Has higher health and combat ability
 * than normal creatures. Chases nearby workers and attacks them when
 * adjacent. Spreads CorruptionGround on tiles it walks through.
 *
 * @author Raima
 * @version 1.0
 */
public class MutatedCreature extends Actor {

    /**
     * Running counter for mutated creature naming.
     */
    private static int counter = 1;

    /**
     * Random number generator for movement decisions.
     */
    private final Random random = new Random();

    /**
     * Behaviour for attacking nearby workers.
     */
    private final AttackNearbyWorkerBehaviour attackBehaviour;

    /**
     * Behaviour for following nearest worker.
     */
    private final FollowNearestWorkerBehaviour followBehaviour;

    /**
     * Creates a MutatedCreature with 50 HP and strong intrinsic weapon.
     */
    public MutatedCreature() {
        super("#" + counter++ + " Mutated Creature", 'Ṃ', 50, new BasicInventory());
        this.enableAbility(ActorCapabilities.HOSTILE_TO_WORKER);
        this.enableAbility(ActorCapabilities.CREATURE);
        this.setIntrinsicWeapon(new IntrinsicWeapon(5, "mutates", 60, "claws") {});
        this.attackBehaviour = new AttackNearbyWorkerBehaviour();
        this.followBehaviour = new FollowNearestWorkerBehaviour();
    }

    /**
     * Each turn the MutatedCreature spreads CorruptionGround on its
     * current tile, then attacks adjacent workers or follows the
     * nearest worker if none are adjacent.
     *
     * @param actions collection of possible actions
     * @param lastAction the last action taken
     * @param map the current game map
     * @param display the display
     * @return the chosen action
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        Location here = map.locationOf(this);

        // ADDED: spread corruption ground on current tile
        if (here.getGroundAs(CorruptionGround.class) == null) {
            here.setGround(new CorruptionGround());
            new Display().println(this + " corrupts the ground at " + here + "!");
        }

        // ADDED: attack nearby workers first
        Action attackAction = attackBehaviour.operate(this, here);
        if (attackAction != null) {
            return attackAction;
        }

        // ADDED: follow nearest worker
        Action followAction = followBehaviour.operate(this, here);
        if (followAction != null) {
            return followAction;
        }

        // ADDED: random movement if no workers nearby
        return getRandomMoveAction(here);
    }

    /**
     * Returns a random valid movement action.
     *
     * @param here the current location
     * @return a random movement action or DoNothingAction
     */
    private Action getRandomMoveAction(Location here) {
        List<Action> validMoves = new ArrayList<>();
        for (Exit exit : here.getExits()) {
            Action moveAction = exit.getDestination().getMoveAction(
                    this, exit.getName(), exit.getHotKey());
            if (moveAction != null) {
                validMoves.add(moveAction);
            }
        }
        if (validMoves.isEmpty()) {
            return new DoNothingAction();
        }
        return validMoves.get(random.nextInt(validMoves.size()));
    }
}