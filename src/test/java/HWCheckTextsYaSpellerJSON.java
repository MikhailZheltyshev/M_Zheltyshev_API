import beans.YandexSpellerAnswer;
import core.YandexSpellerApi;
import dataProviders.DataProviders;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static core.YandexSpellerConstants.*;
import static core.YandexSpellerConstants.Formats.UNSUPPORTED_FORMAT;
import static core.YandexSpellerConstants.Language.*;
import static core.YandexSpellerConstants.Options.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class HWCheckTextsYaSpellerJSON {

    @Test(description = "Check correction of wrong words for all supported languages"
            , dataProvider = "wrongWordDataProvider", dataProviderClass = DataProviders.class)
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


    @Test(description = "Check correction of wrong capitalization for all supported languages",
            dataProvider = "capitalizationDataProvider", dataProviderClass = DataProviders.class)
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

    @Test(description = "Check there is no correction for correct words",
            dataProvider = "correctWordsDataProvider", dataProviderClass = DataProviders.class)
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

    @Test(description = "Check IGNORE_DIGITS option for all supported languages",
            dataProvider = "alphaNumericStringsDataProvider", dataProviderClass = DataProviders.class)
    public void checkIgnoreDigits(String[] texts, Language lang) {
        SoftAssert soft = new SoftAssert();

        List<List<YandexSpellerAnswer>> answers =
                YandexSpellerApi.getYandexSpellerAnswersArray(
                        YandexSpellerApi.with().texts(texts).language(lang).options(IGNORE_CAPITALIZATION).callApi());

        //Assert that there are correct number of answers received in response
        assertThat(answers.size(), equalTo(texts.length));

        //Assert that suggestion are expected
        for (int i = 0; i < texts.length; i++) {
            //Check that current response array item is empty
            soft.assertTrue(answers.get(i).isEmpty(), "Received response is not empty for alphanumeric strings:");
        }
        soft.assertAll();
    }

    @Test(description = "Check the server's response for the request with incorrect options value")
    public void invalidOptionTest() {
        RestAssured
                .given()
                .queryParams(PARAM_TEXT, "test")
                .param(PARAM_OPTIONS, "948359830485")
                .log().all()
                .when()
                .get(YANDEX_SPELLER_API_URI_TEXTS)
                .then()
                //Assert that server returns "Bad Request" in case of incorrect option value was set in the request
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.equalTo("SpellerService: Invalid parameter 'lang'"));
    }

    @Test(description = "Check IGNORE_URLS option for all supported languages",
            dataProvider = "URLDataProvider", dataProviderClass = DataProviders.class)
    public void checkIgnoreURLs(String[] texts, Language lang) {
        SoftAssert soft = new SoftAssert();

        List<List<YandexSpellerAnswer>> answers =
                YandexSpellerApi.getYandexSpellerAnswersArray(
                        YandexSpellerApi.with().texts(texts).language(lang).options(IGNORE_URLS).callApi());

        //Assert that there are correct number of answers received in response
        assertThat(answers.size(), equalTo(texts.length));

        //Assert that suggestion are expected
        for (int i = 0; i < texts.length; i++) {
            //Check that current response array item is empty
            soft.assertTrue(answers.get(i).isEmpty(), "Received response is not empty for URLs:");
        }
        soft.assertAll();
    }

    @Test(description = "Check FIND_REPEAT_WORDS option for all supported languages",
            dataProvider = "repeatWordsDataProvider", dataProviderClass = DataProviders.class)
    public void checkRepeatWords(String[] texts, Language lang) {
        SoftAssert soft = new SoftAssert();

        List<List<YandexSpellerAnswer>> answers =
                YandexSpellerApi.getYandexSpellerAnswersArray(
                        YandexSpellerApi.with().texts(texts).language(lang).options(FIND_REPEAT_WORDS).callApi());

        //Assert that there are correct number of answers received in response
        assertThat(answers.size(), equalTo(texts.length));

        //Assert that suggestion are expected
        for (int i = 0; i < texts.length; i++) {
            //Check that current response array item is not empty
            soft.assertFalse(answers.get(i).isEmpty(), "Received response is empty for strings with repeated words:");
        }
        soft.assertAll();
    }

    @Test(description = "Check the server's response for the request with incorrect language value")
    public void unsupportedLanguageTest() {
        RestAssured
                .given()
                .queryParams(PARAM_TEXT, "This is test")
                .param(PARAM_LANG, UNSUPPORTED_LANG.langCode())
                .log().all()
                .when()
                .get(YANDEX_SPELLER_API_URI_TEXTS)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.equalTo("SpellerService: Invalid parameter 'lang'"));
    }

    @Test(description = "Check the server's response for the request with incorrect text format type")
    public void incorrectFormatTest() {
        RestAssured
                .given()
                .queryParams(PARAM_TEXT, "This is test")
                .param(PARAM_FORMAT, UNSUPPORTED_FORMAT.formatType())
                .log().all()
                .when()
                .get(YANDEX_SPELLER_API_URI_TEXTS)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.equalTo("SpellerService: Invalid parameter 'format'"));
    }


}
