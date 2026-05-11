package game.statuses;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;
import game.infection.InfectionEffect;

/**
 * Status that delegates all infection behaviour to the host-specific effect.
 */
public class InfectedStatus implements Status {

    private final InfectionEffect infectionEffect;

    public InfectedStatus(InfectionEffect infectionEffect) {
        this.infectionEffect = infectionEffect;
    }

    public String onInfected(Location location) {
        return infectionEffect.onInfected(location);
    }

    @Override
    public void tickStatus(GameEntity currEntity, Location location) {
        infectionEffect.tick(location);
    }

    @Override
    public boolean isStatusActive() {
        return infectionEffect.isActive();
    }
}
