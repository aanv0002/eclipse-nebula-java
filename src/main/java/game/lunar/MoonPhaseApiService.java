package game.lunar;

import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

/**
 * Handles communication with the FreeAstroAPI Moon Phase endpoint.
 * Builds a dynamic API request using current game state variables
 * such as the current date and active map coordinates.
 * Parses the JSON response into a MoonPhaseData object.
 * The API key is loaded from a local .env file and never hardcoded.
 *
 * @author Raima
 * @version 1.0
 */
public class MoonPhaseApiService {

    /**
     * Base URL for the Moon Phase API endpoint.
     */
    private static final String BASE_URL =
            "https://api.freeastroapi.com/api/v1/moon/phase";

    /**
     * The API key loaded from the .env file.
     */
    private final String apiKey;

    /**
     * Creates a MoonPhaseApiService and loads the API key from .env file.
     */
    public MoonPhaseApiService() {
        Dotenv dotenv = Dotenv.load();
        this.apiKey = dotenv.get("MOON_API_KEY");
    }

    /**
     * Fetches moon phase data from the API using the current date
     * and the provided map coordinates as dynamic parameters.
     * The date changes every real day and the coordinates change
     * based on which map is currently active.
     *
     * @param latitude the latitude of the active map
     * @param longitude the longitude of the active map
     * @return parsed MoonPhaseData or null if the request fails
     */
    public MoonPhaseData fetchMoonPhase(double latitude, double longitude) {
        try {
            String date = LocalDate.now().toString(); // dynamic — changes daily
            String urlString = BASE_URL
                    + "?date=" + date
                    + "&lat=" + latitude
                    + "&lon=" + longitude
                    + "&include_eclipse=true";

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("x-api-key", apiKey);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                return getDefaultMoonPhaseData();
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return parseResponse(response.toString());

        } catch (Exception e) {
            return getDefaultMoonPhaseData();
        }
    }

    /**
     * Parses the JSON response from the API into a MoonPhaseData object.
     *
     * @param jsonResponse the raw JSON string from the API
     * @return parsed MoonPhaseData
     */
    private MoonPhaseData parseResponse(String jsonResponse) {
        JSONObject json = new JSONObject(jsonResponse);
        JSONObject phase = json.getJSONObject("phase");

        String phaseName = phase.getString("name");
        double illumination = phase.getDouble("illumination");

        boolean isBloodMoon = false;
        if (json.has("eclipse")) {
            JSONObject eclipse = json.getJSONObject("eclipse");
            isBloodMoon = eclipse.optBoolean("is_blood_moon", false);
        }

        return new MoonPhaseData(phaseName, illumination, isBloodMoon);
    }

    /**
     * Returns a default MoonPhaseData when the API call fails.
     * Defaults to a Waning Gibbous phase with moderate illumination.
     *
     * @return default MoonPhaseData
     */
    private MoonPhaseData getDefaultMoonPhaseData() {
        return new MoonPhaseData("Waning Gibbous", 0.5, false);
    }
}