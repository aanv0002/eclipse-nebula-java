package game.lunar;

import game.actors.ContractedWorker;
import game.actors.Slime;
import game.inventory.WeightLimitedInventory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CorruptionGroundTest {
    /**
     * Tests that corruption ground allows workers to enter.
     * *
     * Verifies accessibility of corrupted terrain.
     */
    @Test
    void corruptionGroundAllowsWorkersToEnter() {

        CorruptionGround ground = new CorruptionGround();

        ContractedWorker worker =
                new ContractedWorker(
                        "#1 Worker",
                        'ඞ',
                        10,
                        new WeightLimitedInventory(50)
                );

        assertTrue(ground.canActorEnter(worker)
        );
    }

    /**
     * Tests that corruption ground allows hostile creatures to enter.
     * *
     * Verifies accessibility for all actor types.
     */
    @Test
    void corruptionGroundAllowsCreaturesToEnter() {

        CorruptionGround ground = new CorruptionGround();

        Slime slime = new Slime();

        assertTrue(ground.canActorEnter(slime)
        );
    }}