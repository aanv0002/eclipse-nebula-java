package game.weather;

import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.GravityAnchor;

import java.util.List;

/**
 * Weather effect that activates newly spawned GravityAnchor grounds.
 *
 * The anchor does not pull immediately. It remains visible for one round,
 * then pulls nearby actors and items on its next ground tick before disappearing.
 *
 * @author Tan Hong Sze
 * @version 2.5
 */
public class GravitySurgeEffect implements WeatherEffect<GravityAnchor> {

    @Override
    public String apply(GameMap map, List<Location> locations, List<GravityAnchor> anchors) {
        return "GravityAnchor ground(s) are stabilising. "
                + "They will pull nearby items or actors next turn before disappearing.";
    }
}