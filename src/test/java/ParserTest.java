import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.semenov.Parser;
import org.semenov.WeatherService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParserTest {
    private Parser parser;

    @Mock
    private WeatherService weatherService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        parser = new Parser();
        parser.weatherService = weatherService;
    }

    @Test
    public void testParseWeatherData() {
        Document mockedDocument = mock(Document.class);
        when(weatherService.getPage()).thenReturn(mockedDocument);

        Elements mockedElements = mock(Elements.class);
        when(mockedDocument.select("a.row-item")).thenReturn(mockedElements);

        Element mockedElement = mock(Element.class);
        when(mockedElements.iterator()).thenReturn(java.util.Collections.singleton(mockedElement).iterator());

        when(weatherService.extractDate(mockedElement)).thenReturn("2024-02-01");
        when(weatherService.extractMaxTemperature(mockedElement)).thenReturn("25");
        when(weatherService.extractMinTemperature(mockedElement)).thenReturn("10");

        parser.parseWeatherData();

    }
}
