package game.grounds;

import edu.monash.fit2099.engine.positions.Location;

/**
 * Interface to allow safe deletion of hazardous ground without checking concrete types.
 * * @author Shrabonti Majumder
 * @version 1.0
 */
public interface PurgeableTerrain {
    void purge(Location location);
}