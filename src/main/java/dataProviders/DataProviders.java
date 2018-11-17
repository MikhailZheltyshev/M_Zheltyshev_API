package dataProviders;

import core.YandexSpellerConstants;
import org.testng.annotations.DataProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static core.YandexSpellerConstants.Language.*;

public class DataProviders {

    @DataProvider(parallel = true)
    public Object[][] capitalizationDataProvider() {
        return new Object[][]{
                //Ukrainian case
                {new String[]{"кизак", "творина", "яблако"},
                        UK,
                        new List[]{Arrays.asList("козак", "казак", "кизяк"),
                                Arrays.asList("тварина", "твори на", "тваринна"),
                                Arrays.asList("яблоко", "яблочко", "яблуко", "яблока")}},

                //Russian case
                {new String[]{"оптемизация", "зделка", "цырк"},
                        RU,
                        new List[]{Arrays.asList("оптимизация", "оптимизации", "оптимизацию"),
                                Arrays.asList("сделка", "заделка", "разделка"),
                                Arrays.asList("цирк", "цирка", "циркъ")}},

                //English case
                {new String[]{"suport", "rusia", "spange"},
                        EN,
                        new List[]{Arrays.asList("support", "supports", "supported"),
                                Arrays.asList("russia", "russian", "rusian"),
                                Arrays.asList("sponge", "spanje", "spanked")}}
        };
    }
}
