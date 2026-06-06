package game.lunar;

/**
 * Stores parsed moon phase data retrieved from the Moon Phase API.
 * Contains phase name, illumination percentage and blood moon flag
 * used by MoonPhaseController to select the correct LunarInfluence.
 *
 * @author Raima
 * @version 1.0
 */
public class MoonPhaseData {

    /**
     * The name of the current moon phase e.g. Full Moon, New Moon.
     */
    private final String phaseName;

    /**
     * The illumination of the moon as a value between 0.0 and 1.0.
     */
    private final double illumination;

    /**
     * Whether the current phase is a blood moon total lunar eclipse.
     */
    private final boolean isBloodMoon;

    /**
     * Creates a moon phase data object.
     *
     * @param phaseName the name of the moon phase
     * @param illumination the illumination value between 0.0 and 1.0
     * @param isBloodMoon true if the current phase is a blood moon
     */
    public MoonPhaseData(String phaseName, double illumination, boolean isBloodMoon) {
        this.phaseName = phaseName;
        this.illumination = illumination;
        this.isBloodMoon = isBloodMoon;
    }

    /**
     * Returns the name of the current moon phase.
     *
     * @return phase name
     */
    public String getPhaseName() {
        return phaseName;
    }

    /**
     * Returns the illumination of the moon.
     *
     * @return illumination between 0.0 and 1.0
     */
    public double getIllumination() {
        return illumination;
    }

    /**
     * Returns whether the current phase is a blood moon.
     *
     * @return true if blood moon
     */
    public boolean isBloodMoon() {
        return isBloodMoon;
    }
}