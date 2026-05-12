package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.displays.Menu;
import edu.monash.fit2099.engine.items.Inventory;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actions.ConsumeAction;
import game.items.Consumable;
import game.items.Wallet;
import game.items.WalletCarrier;

/**
 * This brave soul is capable of performing complex tasks such as picking up trash
 * off the floor, swiping plastic cards at stubborn doors, and drinking mystery
 * fluids to stay alive.
 */
public class ContractedWorker extends Actor implements WalletCarrier {
    /**
     * The worker's wallet for storing and spending credits.
     */
    private final Wallet wallet; // ADDED: wallet field for credit management

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
        this.wallet = new Wallet(); // ADDED: initialise wallet with zero credits
    }

    /**
     * Returns the worker's wallet.
     * Used by the Supercomputer to access the worker's credits
     * for buying and selling transactions.
     *
     * @return the worker's wallet
     */
    public Wallet getWallet() { // ADDED: getter for wallet
        return wallet;
    }

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
}