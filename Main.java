import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        // Отримання погоди для трьох міст
        CompletableFuture<WeatherData> city1Weather = CompletableFuture.supplyAsync(() -> fetchWeather("Місто 1"));
        CompletableFuture<WeatherData> city2Weather = CompletableFuture.supplyAsync(() -> fetchWeather("Місто 2"));
        CompletableFuture<WeatherData> city3Weather = CompletableFuture.supplyAsync(() -> fetchWeather("Місто 3"));

        CompletableFuture<Void> compareWeather = CompletableFuture.allOf(city1Weather, city2Weather, city3Weather).thenRun(() -> {
            try {
                WeatherData city1 = city1Weather.get();
                WeatherData city2 = city2Weather.get();
                WeatherData city3 = city3Weather.get();

                System.out.println("Погода в Місто 1: " + city1);
                System.out.println("Погода в Місто 2: " + city2);
                System.out.println("Погода в Місто 3: " + city3);

                String recommendation = recommendActivity(city1, city2, city3);
                System.out.println("Рекомендація: " + recommendation);

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        // Очікуємо завершення всіх задач
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static WeatherData fetchWeather(String city) {
        try {
            System.out.println("Отримання погоди для " + city + "...");
            TimeUnit.SECONDS.sleep(2); // Імітація затримки
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Імітація даних про погоду
        return new WeatherData(city, 25 + (int) (Math.random() * 10), 60 + (int) (Math.random() * 20), 5 + (int) (Math.random() * 10));
    }

    private static String recommendActivity(WeatherData... cities) {
        WeatherData bestCity = null;
        for (WeatherData city : cities) {
            if (city.getTemperature() > 25 && city.getHumidity() < 70 && city.getWindSpeed() < 10) {
                bestCity = city;
                break;
            }
        }
        return bestCity != null
                ? "На пляж можна сходити в " + bestCity.getCity()
                : "Погода не підходить для пляжу. Одягайтесь тепліше.";
    }

    static class WeatherData {
        private final String city;
        private final int temperature;
        private final int humidity;
        private final int windSpeed;

        public WeatherData(String city, int temperature, int humidity, int windSpeed) {
            this.city = city;
            this.temperature = temperature;
            this.humidity = humidity;
            this.windSpeed = windSpeed;
        }

        public String getCity() {
            return city;
        }

        public int getTemperature() {
            return temperature;
        }

        public int getHumidity() {
            return humidity;
        }

        public int getWindSpeed() {
            return windSpeed;
        }

        @Override
        public String toString() {
            return "Температура: " + temperature + "°C, Вологість: " + humidity + "%, Швидкість вітру: " + windSpeed + " км/год";
        }
    }
}
