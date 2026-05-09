package game.items;

/**
 * Represents a worker's wallet that stores credits earned from selling scraps
 * and spent on purchasing equipment from the Supercomputer.
 * Each wallet has a maximum capacity of 1000 credits to enforce the company's
 * strict economic protocols.
 *
 * @author Raima
 * @version 1.0
 */
public class Wallet {

    /**
     * Maximum credits a wallet can hold.
     */
    public static final int MAX_CREDITS = 1000;

    /**
     * Current credits in the wallet.
     */
    private int credits;

    /**
     * Creates a wallet with zero credits.
     */
    public Wallet() {
        this.credits = 0;
    }

    /**
     * Adds credits to the wallet.
     * If the resulting balance would exceed the maximum limit of 1000 credits,
     * the balance is capped at the maximum.
     *
     * @param amount the amount of credits to add
     */
    public void addCredits(int amount) {
        this.credits = Math.min(this.credits + amount, MAX_CREDITS);
    }

    /**
     * Deducts credits from the wallet if sufficient funds are available.
     *
     * @param amount the amount of credits to deduct
     * @return true if deduction was successful, false if insufficient credits
     */
    public boolean deductCredits(int amount) {
        if (this.credits < amount) {
            return false;
        }
        this.credits -= amount;
        return true;
    }

    /**
     * Returns the current credits in the wallet.
     *
     * @return current credits
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Returns whether the wallet has enough credits for a transaction.
     *
     * @param amount the amount to check against
     * @return true if wallet has enough credits, false otherwise
     */
    public boolean hasEnoughCredits(int amount) {
        return this.credits >= amount;
    }

    /**
     * Returns a string representation of the wallet showing current and maximum credits.
     *
     * @return wallet balance string
     */
    @Override
    public String toString() {
        return "Wallet (" + credits + "/" + MAX_CREDITS + " credits)";
    }
}