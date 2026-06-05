package game.items;

/**
 * A contract for actors that carry a wallet for economic transactions.
 * Allows the Supercomputer and buy/sell actions to access the wallet
 * without casting to a concrete actor type.
 *
 * @author Raima
 * @version 1.0
 */
public interface WalletCarrier {

    /**
     * Returns the wallet carried by this actor.
     *
     * @return the actor's wallet
     */
    Wallet getWallet();
}