import beans.YandexSpellerAnswer;
import com.google.gson.JsonSyntaxException;
import core.YandexSpellerApi;
import core.YandexSpellerConstants;
import dataProviders.DataProviders;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static core.YandexSpellerConstants.Language;
import static core.YandexSpellerConstants.Language.*;
import static core.YandexSpellerConstants.PARAM_TEXT;
import static core.YandexSpellerConstants.YANDEX_SPELLER_API_URI_TEXT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class HWCheckTextsYaSpellerJSON {

    @Test(dataProvider = "wrongWordDataProvider", dataProviderClass = DataProviders.class)
    public void checkWrongWordsCorrection(String[] texts, Language lang, List[] expectedSuggestions) {
        SoftAssert soft = new SoftAssert();

        List<List<YandexSpellerAnswer>> answers =
                YandexSpellerApi.getYandexSpellerAnswersArray(
                        YandexSpellerApi.with().texts(texts).language(lang).callApi());

        //Assert that there are correct number of suggestions received in response
        assertThat(answers.size(), equalTo(texts.length));

        //Assert that suggestion are expected
        for (int i = 0; i < texts.length; i++) {
            //Check that current response array item is not empty
            if (!answers.get(i).isEmpty()) {
                soft.assertEquals(answers.get(i).get(0).s, expectedSuggestions[i], "Proposed suggestion is not expected:");
            } else {
                soft.assertFalse(answers.get(i).isEmpty(), "Received response is empty:");
            }
        }

        soft.assertAll();
    }


    @Test(dataProvider = "capitalizationDataProvider", dataProviderClass = DataProviders.class)
    public void checkCapitalizationCorrection(String[] texts, Language lang, List[] expectedSuggestions) {
        SoftAssert soft = new SoftAssert();

        List<List<YandexSpellerAnswer>> answers =
                YandexSpellerApi.getYandexSpellerAnswersArray(
                        YandexSpellerApi.with().texts(texts).language(lang).callApi());

        //Assert that there are correct number of suggestions received in response
        assertThat(answers.size(), equalTo(texts.length));

        //Assert that suggestion are expected
        for (int i = 0; i < texts.length; i++) {
            //Check that current response array item is not empty
            if (!answers.get(i).isEmpty()) {
                soft.assertEquals(answers.get(i).get(0).s, expectedSuggestions[i], "Proposed suggestion is not expected:");
            } else {
                soft.assertFalse(answers.get(i).isEmpty(), "Received response is empty:");
            }
        }
        soft.assertAll();
    }

    @Test(dataProvider = "correctWordsDataProvider", dataProviderClass = DataProviders.class)
    public void checkNoSuggestionForCorrectWords(String[] texts, Language lang) {
        SoftAssert soft = new SoftAssert();

        List<List<YandexSpellerAnswer>> answers =
                YandexSpellerApi.getYandexSpellerAnswersArray(
                        YandexSpellerApi.with().texts(texts).language(lang).callApi());

        //Assert that there are correct number of answers received in response
        assertThat(answers.size(), equalTo(texts.length));

        //Assert that suggestion are expected
        for (int i = 0; i < texts.length; i++) {
            //Check that current response array item is empty
            soft.assertTrue(answers.get(i).isEmpty(), "Received response is not empty for correct words:");
        }
        soft.assertAll();
    }

    @Test(dataProvider = "alphaNumericStringsDataProvider", dataProviderClass = DataProviders.class)
    public void checkIgnoreDigits(String[] texts, Language lang) {
        SoftAssert soft = new SoftAssert();

        List<List<YandexSpellerAnswer>> answers =
                YandexSpellerApi.getYandexSpellerAnswersArray(
                        YandexSpellerApi.with().texts(texts).language(lang).options("512").callApi());

        //Assert that there are correct number of answers received in response
        assertThat(answers.size(), equalTo(texts.length));

        //Assert that suggestion are expected
        for (int i = 0; i < texts.length; i++) {
            //Check that current response array item is empty
            soft.assertTrue(answers.get(i).isEmpty(), "Received response is not empty for alphanumeric strings:");
        }
        soft.assertAll();
    }

    @Test
    public void invalidOptionTest() {
        RestAssured
                .given()
                .queryParams("text", "test")
                .param("options", "948359830485")
                .log().all()
                .when()
                .get(YANDEX_SPELLER_API_URI_TEXT)
                .then()
                //Assert that server returns "client-side" error in case of incorrect option was set in the request
                .statusCode(Matchers.is(both(greaterThan(399)).and(lessThan(500))));


    }


}
