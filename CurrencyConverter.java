import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CurrencyConverter {
    private static final String API_KEY = "YOUR_API_KEY"; // Replace with your ExchangeRate-API key
    private static final String API_URL = "https://open.er-api.com/v6/latest/";

    public static void main(String[] args) {
        
        String baseCurrency = getUserInput("Enter the base currency code (e.g., USD): ");
        String targetCurrency = getUserInput("Enter the target currency code (e.g., EUR): ");
        double amount = getAmount("Enter the amount to convert: ");

       
        double exchangeRate = getExchangeRate(baseCurrency, targetCurrency);

        
        double convertedAmount = amount * exchangeRate;

     
        System.out.printf("%.2f %s is equal to %.2f %s%n", amount, baseCurrency, convertedAmount, targetCurrency);
    }

    private static String getUserInput(String prompt) {
        System.out.print(prompt);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            return reader.readLine().toUpperCase();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static double getAmount(String prompt) {
        System.out.print(prompt);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            return Double.parseDouble(reader.readLine());
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    private static double getExchangeRate(String baseCurrency, String targetCurrency) {
        try {
            URL url = new URL(API_URL + baseCurrency);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

    
            String jsonString = response.toString();
            int startIndex = jsonString.indexOf(targetCurrency) + 5;
            int endIndex = jsonString.indexOf(",", startIndex);
            String rateString = jsonString.substring(startIndex, endIndex);
            return Double.parseDouble(rateString);

        } catch (IOException e) {
            e.printStackTrace();
            return 0.0;
        }
    }
}
