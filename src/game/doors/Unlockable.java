    package game.doors;

    import edu.monash.fit2099.engine.actors.Actor;
    import edu.monash.fit2099.engine.positions.Location;

    /**
     * A contract for grounds that can be unlocked.
     *
     * @author Tan Hong Sze
     * @version 1.0
     */
    public interface Unlockable {

        void unlock();

        boolean isUnlocked();

        ClearanceLevel getRequiredLevel();

        void applyEffect(Actor actor, Location doorLocation);
    }