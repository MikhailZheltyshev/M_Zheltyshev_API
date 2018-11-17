import beans.YandexSpellerAnswer;
import core.YandexSpellerApi;
import core.YandexSpellerConstants;
import dataProviders.DataProviders;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;


import java.util.List;

import static core.YandexSpellerConstants.*;
import static core.YandexSpellerConstants.Language.*;

public class HWCheckTextsYaSpellerJSON {

    //validate an object we've got in API response
    @Test
    public void validateSpellerAnswerAsAnObject() {
        List<List<YandexSpellerAnswer>> answers =
                YandexSpellerApi.getYandexSpellerAnswersArray(
                        YandexSpellerApi.with().texts("кизак", "яблако").language(UK).callApi());
        for (List<YandexSpellerAnswer> list : answers) {
            for (YandexSpellerAnswer answer : list) {
                System.out.println(answer);
            }
        }
    }

    @Test(dataProvider = "capitalizationDataProvider", dataProviderClass = DataProviders.class)
    public void checkCapitalizationCorrection(String[] texts, Language lang, List[] expectedSuggestions) {
        List<List<YandexSpellerAnswer>> answers =
                YandexSpellerApi.getYandexSpellerAnswersArray(
                        YandexSpellerApi.with().texts(texts).language(lang).callApi());
        for (int i = 0; i < texts.length; i++) {
            MatcherAssert.assertThat(answers.get(i).get(0).s, Matchers.equalTo(expectedSuggestions[i]));
        }
    }
}
