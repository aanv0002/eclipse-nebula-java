import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.ContractedWorker;
import game.actors.Slime;
import game.grounds.Dirt;
import game.inventory.WeightLimitedInventory;
import game.lunar.*;
import game.statuses.PoisonStatus;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for REQ5: Lunar Evolution System.
 * The following criteria are tested.
 * Lunar Influences
 *  - Full Moon influence identifies clustered Slimes and triggers Colony Evolution.
 *  - Full Moon influence does not affect isolated Slimes.
 *  - New Moon influence identifies creatures in enclosed locations and applies DormantStatus.
 *  - Blood Moon influence identifies isolated creatures and triggersMutation Evolution.
 *  - Blood Moon influence ignores creatures that are not isolated.
 *  *
 *  Evolution Behaviours
 *  - ColonyEvolution spawns additional Slimes near valid targets.
 *  - MutationEvolution removes the original creature and replaces it with a MutatedCreature.
 *  - AmbushEvolution applies DormantStatus to affected creatures.
 *  Status Effects
 *  - DormantStatus remains active while no worker is nearby.
 *  - DormantStatus is removed when a worker enters detection range.
 *  - DormantStatus causes a creature to gain AggressiveStatus when awakened.
 *  - AggressiveStatus poisons adjacent workers.
 *  - AggressiveStatus expires after its intended duration.
 * *
 *  Terrain Effects
 *  - CorruptionGround damages workers standing on it.
 *  - CorruptionGround does not damage non-worker creatures.
 *  - MutatedCreature spreads CorruptionGround while acting.
 *  *
 *  Creature Behaviour
 *  - MutatedCreature successfully replaces evolved creatures.
 *  - MutatedCreature alters surrounding terrain through corruption.
 *  - Lunar evolution mechanics increase creature population when colony growth conditions are satisfied.
 *
 *
 * @author YourName
 */
class REQ5Test {

    private GameMap createTestMap() throws Exception {
        DefaultGroundCreator groundCreator = new DefaultGroundCreator();
        groundCreator.registerGround('.', Dirt::new);

        GameMap map = new GameMap(
                "REQ5 Test Map",
                groundCreator,
                Arrays.asList(
                        ".......",
                        ".......",
                        ".......",
                        ".......",
                        ".......",
                        ".......",
                        "......."
                )
        );

        game.world.EclipseNebula world =
                new game.world.EclipseNebula(new Display());

        world.addGameMap(map);

        return map;
    }

    /**
     * Tests that ColonyEvolution creates an additional slime nearby.
     */
    @Test
    void colonyEvolutionSpawnsAdditionalSlime() throws Exception {

        GameMap map = createTestMap();

        Slime slime = new Slime();
        map.addActor(slime, map.at(3,3));

        int before = countSlimes(map);

        ColonyEvolution evolution = new ColonyEvolution();
        evolution.evolve(slime, map.at(3,3), map);

        int after = countSlimes(map);

        assertTrue(after > before);
    }

    /**
     * Tests that New Moon influence applies DormantStatus
     * to creatures in enclosed areas.
     */
    @Test
    void newMoonInfluenceAppliesDormantStatus() throws Exception {

        GameMap map = createTestMap();

        Slime slime = new Slime();
        map.addActor(slime, map.at(3,3));

        map.at(2,3).setGround(new CorruptionGround());
        map.at(4,3).setGround(new CorruptionGround());

        NewMoonInfluence influence = new NewMoonInfluence();

        influence.applyInfluence(map, new MoonPhaseData("New Moon", 0.0, false));

        assertFalse(slime.statusesOf(DormantStatus.class).isEmpty());
    }

    /**
     * Tests that DormantStatus wakes when worker moves adjacent.
     */
    @Test
    void dormantStatusWakesWhenWorkerNearby() throws Exception {

        GameMap map = createTestMap();

        Slime slime = new Slime();
        map.addActor(slime, map.at(3,3));

        DormantStatus status = new DormantStatus();
        slime.addStatus(status);

        ContractedWorker worker = new ContractedWorker("#1 Worker", 'ඞ', 20, new WeightLimitedInventory(50));

        map.addActor(worker, map.at(3,4));

        status.tickStatus(slime, map.at(3,3));

        assertFalse(status.isStatusActive());
        assertFalse(slime.statusesOf(AggressiveStatus.class).isEmpty());
    }

    /**
     * Tests AggressiveStatus poisons adjacent workers.
     */
    @Test
    void aggressiveStatusPoisonsAdjacentWorker() throws Exception {

        GameMap map = createTestMap();

        Slime slime = new Slime();
        map.addActor(slime, map.at(3,3));

        ContractedWorker worker = new ContractedWorker("#1 Worker", 'ඞ', 20, new WeightLimitedInventory(50));
        map.addActor(worker, map.at(3,4));
        AggressiveStatus status = new AggressiveStatus();

        status.tickStatus(slime, map.at(3,3));
        assertFalse(worker.statusesOf(PoisonStatus.class).isEmpty());
    }

    /**
     * Tests MutationEvolution replaces creature with MutatedCreature.
     */
    @Test
    void mutationEvolutionReplacesCreature() throws Exception {

        GameMap map = createTestMap();

        Slime slime = new Slime();
        map.addActor(slime, map.at(3,3));

        MutationEvolution evolution = new MutationEvolution();
        evolution.evolve(slime, map.at(3,3), map);
        assertTrue(map.at(3,3).containsAnActor());

        String actorName = map.at(3,3).getActor().toString();
        assertTrue(actorName.contains("Mutated Creature"));
    }

    /**
     * Tests Blood Moon influence mutates isolated creatures.
     */
    @Test
    void bloodMoonInfluenceMutatesIsolatedCreature() throws Exception {

        GameMap map = createTestMap();

        Slime slime = new Slime();
        map.addActor(slime, map.at(3,3));

        BloodMoonInfluence influence = new BloodMoonInfluence();

        influence.applyInfluence(map, new MoonPhaseData("Full Moon", 1.0, true));

        assertTrue(map.at(3,3).getActor().toString().contains("Mutated Creature"));
    }

    /**
     * Tests CorruptionGround damages workers standing on it.
     */
    @Test
    void corruptionGroundDamagesWorker() throws Exception {

        GameMap map = createTestMap();
        ContractedWorker worker = new ContractedWorker("#1 Worker", 'ඞ', 10, new WeightLimitedInventory(50));

        map.at(3,3).setGround(new CorruptionGround());
        map.addActor(worker, map.at(3,3));
        map.at(3,3).getGround().tick(map.at(3,3));
        assertTrue(worker.toString().contains("9/10"));
    }

    private int countSlimes(GameMap map) {

        int count = 0;

        for (int x : map.getXRange()) {
            for (int y : map.getYRange()) {

                if (map.at(x, y).containsAnActor() && map.at(x, y).getActor().getDisplayChar() == '⍾') {
                    count++;
                }
            }
        }

        return count;
    }
    /**
     * Tests that Full Moon influence does not trigger colony evolution
     * when a slime has insufficient neighbouring slimes.
     *
     * @throws Exception if map setup fails
     */
    @Test
    void fullMoonDoesNotMutateSingleSlime() throws Exception {

        GameMap map = createTestMap();

        Slime slime = new Slime();
        map.addActor(slime, map.at(3, 3));

        int before = countSlimes(map);

        FullMoonInfluence influence = new FullMoonInfluence();
        influence.applyInfluence(map, new MoonPhaseData("Full Moon", 1.0, false)
        );

        int after = countSlimes(map);

        assertEquals(before, after);
    }
    /**
     * Tests that Full Moon influence triggers colony evolution
     * when a slime belongs to a cluster.
     *
     * @throws Exception if map setup fails
     */
    @Test
    void fullMoonInfluenceCreatesAdditionalSlimeForCluster() throws Exception {

        GameMap map = createTestMap();

        map.addActor(new Slime(), map.at(3, 3));
        map.addActor(new Slime(), map.at(3, 4));
        map.addActor(new Slime(), map.at(4, 3));

        int before = countSlimes(map);

        FullMoonInfluence influence = new FullMoonInfluence();
        influence.applyInfluence(map, new MoonPhaseData("Full Moon", 1.0, false));

        int after = countSlimes(map);

        assertTrue(after > before);
    }
    /**
     * Tests that MutationEvolution removes the original creature
     * before replacing it with a MutatedCreature.
     *
     * @throws Exception if map setup fails
     */
    @Test
    void mutationEvolutionReplacesOriginalActor() throws Exception {

        GameMap map = createTestMap();
        Slime slime = new Slime();

        map.addActor(slime, map.at(3, 3));
        MutationEvolution evolution = new MutationEvolution();
        evolution.evolve(slime, map.at(3, 3), map);

        assertNotEquals(slime, map.at(3, 3).getActor());
    }
    /**
     * Tests that AggressiveStatus expires after its duration.
     */
    @Test
    void aggressiveStatusExpiresAfterFiveTurns() {

        AggressiveStatus status = new AggressiveStatus();
        assertTrue(status.isStatusActive());

        for (int i = 0; i < 5; i++) {
            status.tickStatus(null, null);
        }

        assertFalse(status.isStatusActive());
    }
    /**
     * Tests that DormantStatus remains active when no worker
     * enters detection range.
     *
     * @throws Exception if map setup fails
     */
    @Test
    void dormantStatusRemainsWithoutWorkerNearby() throws Exception {

        GameMap map = createTestMap();
        Slime slime = new Slime();

        map.addActor(slime, map.at(3, 3));
        DormantStatus status = new DormantStatus();
        slime.addStatus(status);
        status.tickStatus(slime, map.at(3, 3));

        assertTrue(status.isStatusActive());
    }
    /**
     * Tests that CorruptionGround only damages workers
     * and does not damage ordinary creatures.
     *
     * @throws Exception if map setup fails
     */
    @Test
    void corruptionGroundDoesNotDamageSlime() throws Exception {

        GameMap map = createTestMap();
        Slime slime = new Slime();

        map.at(3, 3).setGround(new CorruptionGround());
        map.addActor(slime, map.at(3, 3));

        String before = slime.toString();
        map.at(3, 3).getGround().tick(map.at(3, 3));

        String after = slime.toString();
        assertEquals(before, after);
    }
    /**
     * Tests that Blood Moon influence ignores creatures
     * that are not isolated.
     *
     * @throws Exception if map setup fails
     */
    @Test
    void bloodMoonIgnoresGroupedCreature() throws Exception {

        GameMap map = createTestMap();

        Slime slime1 = new Slime();
        Slime slime2 = new Slime();

        map.addActor(slime1, map.at(3, 3));
        map.addActor(slime2, map.at(3, 4));

        BloodMoonInfluence influence = new BloodMoonInfluence();
        influence.applyInfluence(map, new MoonPhaseData("Full Moon", 1.0, true));

        assertFalse(map.at(3, 3).getActor().toString().contains("Mutated Creature"));
    }
    /**
     * Tests that MutatedCreature converts its current tile
     * into CorruptionGround when taking a turn.
     *
     * @throws Exception if map setup fails
     */
    @Test
    void mutatedCreatureCreatesCorruptionGround() throws Exception {

        GameMap map = createTestMap();
        MutatedCreature creature = new MutatedCreature();
        map.addActor(creature, map.at(3, 3));
        creature.playTurn(new ActionList(), new DoNothingAction(), map, new Display());
        assertEquals('¤', map.at(3, 3).getGround().getDisplayChar());
    }

}