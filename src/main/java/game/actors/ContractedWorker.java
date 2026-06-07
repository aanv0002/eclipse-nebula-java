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
import game.inventory.WeightLimitedInventory;
import game.items.Consumable;
import game.items.Wallet;
import game.items.WalletCarrier;
import game.statuses.InfectedStatus;
import game.statuses.StunnedStatus;

/**
 * This brave soul is capable of performing complex tasks such as picking up trash
 * off the floor, swiping plastic cards at stubborn doors, and drinking mystery
 * fluids to stay alive.
 * The worker can also become infected by a Parasite, turning their body into a
 * living hive that takes damage and periodically spawns new Parasites.
 *
 * @author Tan Hong Sze
 * @author Shrabonti Majumder
 * @version 3.0
 */
public class ContractedWorker extends Actor implements WalletCarrier, Infectable, WeightPenaltyTarget {

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

        java.util.List<game.statuses.RootStatus> rootStatuses =
                this.statusesOf(game.statuses.RootStatus.class);

        if (!rootStatuses.isEmpty()) {
            game.statuses.RootStatus rootStatus = rootStatuses.get(0);

            display.println(this + " is rooted for "
                    + rootStatus.getRemainingTurns()
                    + " more turn(s) and cannot move.");

            actions = removeMovementActions(actions);
        }

        if (!this.statusesOf(game.statuses.BlindStatus.class).isEmpty()) {
            Action blindMove = getRandomMoveAction(map.locationOf(this));

            if (blindMove != null) {
                display.println(this + " is blinded and stumbles randomly.");
                return blindMove;
            }

            display.println(this + " is blinded but has nowhere to move.");
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
     * Removes movement actions while keeping non-movement actions available.
     *
     * @param actions original action list
     * @return filtered action list without movement actions
     */
    private ActionList removeMovementActions(ActionList actions) {
        ActionList filteredActions = new ActionList();

        for (Action action : actions) {
            String description = action.menuDescription(this);

            if (!description.contains("moves")) {
                filteredActions.add(action);
            }
        }

        return filteredActions;
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

    /**
     * Returns a random valid movement action.
     *
     * @param location current location
     * @return random move action, or null if none exists
     */
    private Action getRandomMoveAction(Location location) {
        java.util.List<Action> validMoves = new java.util.ArrayList<>();

        for (edu.monash.fit2099.engine.positions.Exit exit : location.getExits()) {
            Action moveAction = exit.getDestination()
                    .getMoveAction(this, exit.getName(), exit.getHotKey());

            if (moveAction != null) {
                validMoves.add(moveAction);
            }
        }

        if (validMoves.isEmpty()) {
            return null;
        }

        return validMoves.get(new java.util.Random().nextInt(validMoves.size()));
    }
    public int applyWeightPenalty(int reduced) {
        return ((WeightLimitedInventory) getInventory()).applyCorrosion(reduced);
    }

    public void removeWeightPenalty(int original) {
        ((WeightLimitedInventory) getInventory()).removeCorrosion(original);
    }

}
