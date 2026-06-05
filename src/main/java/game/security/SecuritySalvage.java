package game.security;

import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.items.ItemStatistics;

/**
 * A reward dropped when successfully bypassing a security hazard.
 *
 * @author Shrabonti Majumder
 * @version 1.0
 */
public class SecuritySalvage extends Item {
    public SecuritySalvage() {
        super("Security Salvage", '$');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(1));
        this.makePortable();
    }
}