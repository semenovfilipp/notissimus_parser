package org.semenov;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс WeatherService предназначен для получения данных о погоде с веб-страницы.
 */
public class WeatherService {
    private static final String URL = "https://www.gismeteo.ru/weather-sankt-peterburg-4079/month/";
    private String month;

    /**
     * Метод для получения содержимого веб-страницы в виде объекта Document с использованием библиотеки Jsoup.
     *
     * @return объект Document, содержащий данные о погоде с веб-страницы
     * @throws RuntimeException если не удается получить страницу или происходит ошибка ввода-вывода
     */
    public Document getPage() {
        try {
            return Jsoup.parse(new URL(URL), 3000);
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch page", e);
        }
    }

    /**
     * Метод для извлечения даты из элемента страницы с помощью регулярного выражения.
     *
     * @param element элемент страницы, содержащий дату
     * @return строка с датой в формате "yyyy-MM-dd"
     */
    public String extractDate(Element element) {
        String dateText = element.select("div.date").text();
        Pattern pattern = Pattern.compile("(\\d{1,2})(?: (\\p{L}+))?");
        Matcher matcher = pattern.matcher(dateText);

        if (matcher.find()) {
            int currentDay = Integer.parseInt(matcher.group(1));
            String currentMonth = matcher.group(2) != null ? getMonthNumber(matcher.group(2)) : null;

            if (currentMonth != null) {
                month = currentMonth;
            }

            String year = String.valueOf(java.time.LocalDate.now().getYear());
            return year + "-" + month + "-" + (currentDay < 10 ? "0" + currentDay : currentDay);
        }
        return null;
    }



    /**
     * Метод для преобразования названия месяца на русском языке в его числовой эквивалент.
     *
     * @param month название месяца на русском языке (в трехбуквенном формате)
     * @return числовой эквивалент месяца в формате "MM"
     */

    private String getMonthNumber(String month) {
        return switch (month.toLowerCase()) {
            case "янв" -> "01";
            case "фев" -> "02";
            case "мар" -> "03";
            case "апр" -> "04";
            case "май" -> "05";
            case "июн" -> "06";
            case "июл" -> "07";
            case "авг" -> "08";
            case "сен" -> "09";
            case "окт" -> "10";
            case "ноя" -> "11";
            case "дек" -> "12";
            default -> null;
        };
    }

    /**
     * Метод для извлечения максимальной температуры из элемента страницы.
     *
     * @param element элемент страницы, содержащий максимальную температуру
     * @return строка с максимальной температурой
     */
    public String extractMaxTemperature(Element element) {
        Element maxTempElement = element.selectFirst("span.unit.unit_temperature_c");
        return maxTempElement != null ? maxTempElement.text() : null;
    }

    /**
     * Метод для извлечения минимальной температуры из элемента страницы.
     *
     * @param element элемент страницы, содержащий минимальную температуру
     * @return строка с минимальной температурой
     */
    public String extractMinTemperature(Element element) {
        Element minTempElement = element.selectFirst("span.unit.unit_temperature_f");
        return minTempElement != null ? minTempElement.text() : null;
    }
}
