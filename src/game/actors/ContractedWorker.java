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
import game.items.WalletCarrier;
import game.statuses.InfectedStatus;
import game.statuses.StunnedStatus;

/**
 * This brave soul is capable of performing complex tasks such as picking up trash
 * off the floor, swiping plastic cards at stubborn doors, and drinking mystery
 * fluids to stay alive.
 *
 * @author Tan Hong Sze
 * @author Shrabonti Majumder
 * @version 3.0
 */
public class ContractedWorker extends Actor implements WalletCarrier, Infectable {

    private final Wallet wallet;

    public ContractedWorker(String name, char displayChar, int hitPoints, Inventory inventory) {
        super(name, displayChar, hitPoints, inventory);
        this.enableAbility(ActorCapabilities.WORKER);
        this.wallet = new Wallet();
    }

    public Wallet getWallet() {
        return wallet;
    }

    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        if (!this.isConscious()) {
            this.unconscious(map);
            return new DoNothingAction();
        }

        if (this.hasStatus(StunnedStatus.class)) {
            display.println(this + " is stunned by high voltage and cannot act this turn!");
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

    @Override
    public boolean canBeInfected() {
        return this.isConscious();
    }

    @Override
    public String infect(Actor parasite, Location location) {
        InfectedStatus infectedStatus = new InfectedStatus(new WorkerInfectionEffect(this));
        this.addStatus(infectedStatus);
        return infectedStatus.onInfected(location);
    }
}