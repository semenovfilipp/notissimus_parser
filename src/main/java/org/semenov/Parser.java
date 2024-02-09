package org.semenov;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * Класс Parser предназначен для парсинга данных о погоде с веб-сайта и сохранения их в файл.
 */
public class Parser {
    public WeatherService weatherService;

    /**
     * Конструктор класса Parser инициализирует объект WeatherService для использования при парсинге данных о погоде.
     */
    public Parser() {
        this.weatherService = new WeatherService();
    }

    /**
     * Метод parseWeatherData осуществляет парсинг данных о погоде с веб-сайта и сохраняет их в файл.
     * В случае ошибки при получении страницы или записи данных в файл, метод выводит сообщение об ошибке.
     */
    public void parseWeatherData() {
        Document page = getPageFromWebsite();
        if (page == null) {
            System.out.println("Failed to fetch the page. Parsing aborted.");
            return;
        }

        Elements elements = page.select("a.row-item");

        Path filePath = createOutputFile();
        if (filePath == null) {
            System.out.println("Failed to create output file. Parsing aborted.");
            return;
        }

        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            for (Element element : elements) {
                String date = weatherService.extractDate(element);
                String maxTemp = weatherService.extractMaxTemperature(element);
                String minTemp = weatherService.extractMinTemperature(element);

                WeatherData weatherData = new WeatherData(date, minTemp, maxTemp);
                printWeatherInfo(weatherData, writer);
            }
            System.out.println("Parsing completed. Data saved in file: " + filePath.getFileName());
        } catch (IOException e) {
            System.out.println("Failed to write data to file. Parsing aborted.");
            e.printStackTrace();
        }
    }

    /**
     * Вспомогательный метод getPageFromWebsite получает содержимое веб-страницы с данными о погоде.
     *
     * @return объект Document с содержимым веб-страницы или null, если произошла ошибка при получении страницы
     */
    private Document getPageFromWebsite() {
        try {
            return weatherService.getPage();
        } catch (RuntimeException e) {
            System.out.println("Failed to fetch the page. Parsing aborted.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Вспомогательный метод createOutputFile создает файл для сохранения данных о погоде.
     *
     * @return объект Path, представляющий путь к созданному файлу, или null в случае ошибки при создании файла
     */
    private Path createOutputFile() {
        try {
            Path directory = Paths.get("files");
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            String fileName = "weather_data_" + currentTime.format(formatter) + ".txt";
            return directory.resolve(fileName);
        } catch (IOException e) {
            System.out.println("Failed to create output file. Parsing aborted.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Вспомогательный метод printWeatherInfo форматирует и печатает информацию о погоде в файл.
     *
     * @param weatherData объект WeatherData с данными о погоде
     * @param writer      объект FileWriter для записи данных в файл
     * @throws IOException если возникает ошибка ввода-вывода при записи в файл
     */
    private void printWeatherInfo(WeatherData weatherData, FileWriter writer) throws IOException {
        String formattedOutput = String.format("Date: %-10s | Min Temp: %-10s | Max Temp: %-10s",
                weatherData.getDate(), weatherData.getMinTemperature(), weatherData.getMaxTemperature());

        String delimiter = "______________________________________________________";

        writer.write(formattedOutput + "\n");
        writer.write(delimiter + "\n");
    }
}
