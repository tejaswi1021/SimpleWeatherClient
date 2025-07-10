import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class SimpleWeatherClient {

    public static void main(String[] args) {
        // Use try-with-resources to auto-close Scanner
        try (Scanner scanner = new Scanner(System.in)) {

            // Take city name as input
            System.out.print("Enter city name: ");
            String city = scanner.nextLine();

            // API key from OpenWeatherMap
            String apiKey = "YOUR_API_KEY_HERE";  // Replace with your actual key
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";

            // Create HTTP client
            HttpClient client = HttpClient.newHttpClient();

            // Build HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            // Send request and get response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Full JSON response
            String json = response.body();
            System.out.println("\nRaw JSON Response:\n" + json);

            // Extract data manually
            String temperature = extractValue(json, "\"temp\":", ",");
            String humidity = extractValue(json, "\"humidity\":", "}");
            String description = extractValue(json, "\"description\":\"", "\"");

            // Print results
            System.out.println("\nParsed Weather Data:");
            System.out.println("City: " + city);
            System.out.println("Temperature: " + temperature + " °C");
            System.out.println("Humidity: " + humidity + " %");
            System.out.println("Weather: " + description);

        } catch (Exception e) {
            System.out.println("Error fetching weather data: " + e.getMessage());
        }
    }

    // Utility method to extract values from simple JSON format
    private static String extractValue(String json, String startKey, String endDelimiter) {
        int startIndex = json.indexOf(startKey);
        if (startIndex == -1) return "Not found";

        startIndex += startKey.length();
        int endIndex = json.indexOf(endDelimiter, startIndex);
        if (endIndex == -1) return "Not found";

        return json.substring(startIndex, endIndex).replaceAll("[\"}]", "").trim();
    }
}
