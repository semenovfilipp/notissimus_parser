
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import org.semenov.WeatherService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class WeatherServiceTest {

    @Test
    void getPage_ReturnsDocument() {
        WeatherService weatherService = new WeatherService();
        Document page = weatherService.getPage();
        assertNotNull(page);
    }

    @Test
    void extractDate_ValidElement_ReturnsDate() {
        WeatherService weatherService = new WeatherService();
        Element element = createMockElement("div.date", "9 фев");

        String date = weatherService.extractDate(element);

        assertEquals("2024-02-09", date);
    }

    @Test
    void extractDate_InvalidElement_ReturnsNull() {
        WeatherService weatherService = new WeatherService();
        Element element = createMockElement("div.invalid", "Some invalid content");

        String date = weatherService.extractDate(element);

        assertEquals(null, date);
    }

    @Test
    void extractMaxTemperature_ValidElement_ReturnsTemperature() {
        WeatherService weatherService = new WeatherService();
        Element element = createMockElement("span.unit.unit_temperature_c", "25");

        String temperature = weatherService.extractMaxTemperature(element);

        assertEquals("25", temperature);
    }

    @Test
    void extractMaxTemperature_InvalidElement_ReturnsNull() {
        WeatherService weatherService = new WeatherService();
        Element element = createMockElement("span.invalid", "Invalid content");

        String temperature = weatherService.extractMaxTemperature(element);

        assertEquals(null, temperature);
    }

    @Test
    void extractMinTemperature_ValidElement_ReturnsTemperature() {
        WeatherService weatherService = new WeatherService();
        Element element = createMockElement("span.unit.unit_temperature_f", "-10");

        String temperature = weatherService.extractMinTemperature(element);

        assertEquals("-10", temperature);
    }

    @Test
    void extractMinTemperature_InvalidElement_ReturnsNull() {
        WeatherService weatherService = new WeatherService();
        Element element = createMockElement("span.invalid", "Invalid content");

        String temperature = weatherService.extractMinTemperature(element);

        assertEquals(null, temperature);
    }

    private Element createMockElement(String cssSelector, String text) {
        Element element = new Element("div");
        element.appendElement(cssSelector).text(text);
        return element;
    }
}
