package dataProviders;

import core.YandexSpellerConstants;
import org.testng.annotations.DataProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static core.YandexSpellerConstants.Language.*;

public class DataProviders {

    @DataProvider(parallel = true)
    public Object[][] wrongWordDataProvider() {
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

    @DataProvider(parallel = true)
    public Object[][] capitalizationDataProvider() {
        return new Object[][]{
                //Ukrainian case
                {new String[]{"олександр", "київ"},
                        UK,
                        new List[]{Arrays.asList("Олександр"),
                                Arrays.asList("Київ")}},

                //Russian case
                {new String[]{"евгений", "москва"},
                        RU,
                        new List[]{Arrays.asList("Евгений"),
                                Arrays.asList("Москва")}},

                //English case
                {new String[]{"john", "washington"},
                        EN,
                        new List[]{Arrays.asList("john"),
                                Arrays.asList("Washington")}}
        };
    }

    @DataProvider(parallel = true)
    public Object[][] correctWordsDataProvider() {
        return new Object[][]{
                //Ukrainian case
                {new String[]{"вечеря", "коханка", "рідина"}, UK},

                //Russian case
                {new String[]{"атмосфера", "ужин", "море"}, RU},

                //English case
                {new String[]{"better", "use", "airplane"}, EN}
        };
    }

    @DataProvider(parallel = true)
    public Object[][] alphaNumericStringsDataProvider() {
        return new Object[][]{
                //Ukrainian case
                {new String[]{"1вечеря", "коха2нка", "рідина3"}, UK},

                //Russian case
                {new String[]{"1атмосфера", "уж2ин", "море3"}, RU},

                //English case
                {new String[]{"1better", "us2e", "airplane3"}, EN}
        };
    }

    @DataProvider(parallel = true)
    public Object[][] languagesDataProvider() {
        return new Object[][]{
                //Ukrainian case
                {UK},

                //Russian case
                {RU},

                //English case
                {EN}
        };
    }
}
