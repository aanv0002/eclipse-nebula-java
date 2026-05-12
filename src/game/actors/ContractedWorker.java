package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.displays.Menu;
import edu.monash.fit2099.engine.items.Inventory;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.ConsumeAction;
import game.infection.Infectable;
import game.infection.WorkerInfectionEffect;
import game.items.Consumable;
import game.items.Wallet;
import game.statuses.InfectedStatus;

/**
 * This brave soul is capable of performing complex tasks such as picking up trash
 * off the floor, swiping plastic cards at stubborn doors, and drinking mystery
 * fluids to stay alive.
 *
 * The worker can also become infected by a Parasite, turning their body into a
 * living hive that takes damage and periodically spawns new Parasites.
 *
 * @author Tan Hong Sze
 * @author Shrabonti Majumder
 * @version 2.0
 */
public class ContractedWorker extends Actor implements Infectable {

    /**
     * The worker's wallet for storing and spending credits.
     */
    private final Wallet wallet;

    /**
     * Creates a contracted worker.
     *
     * @param name the worker name
     * @param displayChar the worker display character
     * @param hitPoints starting hit points
     * @param inventory the worker inventory
     */
    public ContractedWorker(String name, char displayChar, int hitPoints, Inventory inventory) {
        super(name, displayChar, hitPoints, inventory);
        this.enableAbility(ActorCapabilities.WORKER);
        this.wallet = new Wallet();
    }

    /**
     * Returns the worker's wallet.
     *
     * @return the worker's wallet
     */
    public Wallet getWallet() {
        return wallet;
    }

    /**
     * Chooses the worker's action for the current turn.
     *
     * @param actions collection of possible Actions for this Actor
     * @param lastAction the Action this Actor took last turn
     * @param map the map containing the Actor
     * @param display the I/O object
     * @return the chosen action
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        if (!this.isConscious()) {
            this.unconscious(map);
            return new DoNothingAction();
        }

        for (Consumable consumable : this.getInventory().getItemsAs(Consumable.class)) {
            if (consumable.canConsume()) {
                actions.add(new ConsumeAction(consumable));
            }
        }

        if (lastAction.getNextAction() != null) {
            return lastAction.getNextAction();
        }

        Menu menu = new Menu(actions);
        return menu.showMenu(this, display);
    }

    /**
     * Returns whether the worker can currently be infected.
     *
     * @return true if the worker does not already have an infected status
     */
    @Override
    public boolean canBeInfected() {
        return !this.hasStatus(InfectedStatus.class);
    }

    /**
     * Applies Parasite infection to the worker.
     *
     * @param parasite the Parasite applying infection
     * @param location the worker's location
     * @return infection result message
     */
    @Override
    public String infect(Actor parasite, Location location) {
        InfectedStatus infectedStatus = new InfectedStatus(new WorkerInfectionEffect(this));
        this.addStatus(infectedStatus);
        return infectedStatus.onInfected(location);
    }
}
