package game.weather;

import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.SolarCrystal;

import java.util.List;

/**
 * Effect that energises solar crystals and causes nearby spreading.
 *
 * @author Tan Hong Sze
 * @version 2.0
 */
public class CrystalBloomEffect implements WeatherEffect<SolarCrystal> {

    private static final double SPREAD_CHANCE = 0.25;

    @Override
    public String apply(GameMap map, List<Location> locations, List<SolarCrystal> crystals) {
        int spreadCount = 0;

        for (Location source : locations) {
            for (Exit exit : source.getExits()) {
                Location adjacent = exit.getDestination();

                if (!adjacent.containsAnActor()
                        && adjacent.getItems().isEmpty()
                        && Math.random() < SPREAD_CHANCE) {
                    adjacent.setGround(new SolarCrystal(adjacent.getGround()));
                    spreadCount++;
                }
            }
        }

        return "Solar crystals pulse and spread to "
                + spreadCount + " nearby tile(s).";
    }
}