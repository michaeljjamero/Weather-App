import weather.WeatherAPI;
import weather.Period;
import java.util.ArrayList;

public class MyWeatherAPI extends WeatherAPI {
    private static ArrayList<Period> cachedForecast;

    public static ArrayList<Period> getForecast(String office, int x, int y) {
        if (cachedForecast == null) {
            System.out.println("Fetching forecast from real API...");
            cachedForecast = WeatherAPI.getForecast(office, x, y);
        } else {
            System.out.println("Returning cached forecast...");
        }
        return cachedForecast;
    }

    public static void clearCache() {
        cachedForecast = null;
    }
}