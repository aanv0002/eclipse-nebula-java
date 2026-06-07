package game.security;

/**
 * Governs operational modules or utility tools carried or deployed by the worker.
 *
 * @author Shrabonti Majumder
 * @version 1.1
 */
public interface GridCountermeasure {
    /**
     * Returns the type of countermeasure this module represents.
     * @return a string identifying the countermeasure type
     */
    String getCountermeasureType();
}