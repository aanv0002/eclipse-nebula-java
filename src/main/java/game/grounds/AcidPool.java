package game.grounds;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import java.util.ArrayList;

/**
 * Melted terrain that dissolves all items dropped into it.
 * * @author Shrabonti Majumder
 * @version 1.0
 */
public class AcidPool extends Ground implements PurgeableTerrain {

    public AcidPool() {
        super('a', "Acid Pool");
    }

    @Override
    public void tick(Location location) {
        if (!location.getItems().isEmpty()) {
            new Display().println("The Acid Pool fiercely dissolves objects on the floor.");
            for (Item item : new ArrayList<>(location.getItems())) {
                location.removeItem(item);
            }
        }
    }

    @Override
    public void purge(Location location) {
        location.setGround(new Floor());
    }
}