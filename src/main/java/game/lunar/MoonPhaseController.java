package game.lunar;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * Controls the lunar evolution system by periodically fetching moon
 * phase data from the external API and applying the corresponding
 * lunar influence to the active game map.
 * Depends on the LunarInfluence abstraction rather than concrete
 * influence classes, following the Dependency Inversion Principle.
 * The API request is dynamic — date changes daily and coordinates
 * change based on the active map.
 *
 * @author Raima
 * @version 1.0
 */
public class MoonPhaseController {

    /**
     * Service for fetching moon phase data from the external API.
     */
    private final MoonPhaseApiService apiService;

    /**
     * Number of turns between each lunar influence check.
     */
    private static final int CHECK_INTERVAL = 50;

    /**
     * Current turn counter.
     */
    private int turnCount;

    /**
     * Latitude for map99 — 99-Deprecated.
     */
    private static final double MAP99_LAT = -37.8136;

    /**
     * Longitude for map99 — 99-Deprecated.
     */
    private static final double MAP99_LON = 144.9631;

    /**
     * Latitude for map20 — 20-Overflow.
     */
    private static final double MAP20_LAT = 3.1390;

    /**
     * Longitude for map20 — 20-Overflow.
     */
    private static final double MAP20_LON = 101.6869;

    /**
     * Creates a MoonPhaseController with a MoonPhaseApiService.
     */
    public MoonPhaseController() {
        this.apiService = new MoonPhaseApiService();
        this.turnCount = 0;
    }

    /**
     * Called every turn from EclipseNebula.gameLoop().
     * Every 50 turns fetches moon phase data from the API and applies
     * the corresponding lunar influence to the active game map.
     * The coordinates used in the API request change based on which
     * map is currently active making the request dynamic.
     *
     * @param map the active game map
     * @param isMap99 true if the active map is 99-Deprecated
     */
    public void tick(GameMap map, boolean isMap99) {
        turnCount++;

        if (turnCount % CHECK_INTERVAL != 0) {
            return;
        }

        double lat = isMap99 ? MAP99_LAT : MAP20_LAT;
        double lon = isMap99 ? MAP99_LON : MAP20_LON;

        new Display().println("Lunar system checking moon phase...");

        MoonPhaseData data = apiService.fetchMoonPhase(lat, lon);

        if (data == null) {
            new Display().println("Lunar system: API call failed, skipping influence.");
            return;
        }

        new Display().println("Moon Phase: " + data.getPhaseName()
                + " | Illumination: " + String.format("%.1f", data.getIllumination() * 100)
                + "% | Blood Moon: " + data.isBloodMoon());

        LunarInfluence influence = selectInfluence(data);
        new Display().println("Activating " + influence.getInfluenceName() + "!");
        influence.applyInfluence(map, data);
    }

    /**
     * Selects the appropriate LunarInfluence based on moon phase data.
     * Blood Moon takes priority, then Full Moon, then New Moon.
     * All other phases default to no significant influence.
     * Depends on LunarInfluence abstraction not concrete classes (DIP).
     *
     * @param data the moon phase data from the API
     * @return the selected LunarInfluence
     */
    private LunarInfluence selectInfluence(MoonPhaseData data) {

        if (data.isBloodMoon()) {
            return new BloodMoonInfluence();
        }

        if (data.getPhaseName().equalsIgnoreCase("Full Moon")) {
            return new FullMoonInfluence();
        }

        if (data.getPhaseName().equalsIgnoreCase("New Moon")) {
            return new NewMoonInfluence();
        }

        // default — waning or waxing phases have no major influence
        return new DefaultLunarInfluence();
    }
}