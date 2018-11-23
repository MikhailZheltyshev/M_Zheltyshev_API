import beans.YandexSpellerAnswer;
import core.YandexSpellerApi;
import dataProviders.DataProviders;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import org.apache.http.HttpStatus;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.List;

import static core.YandexSpellerConstants.Formats.UNSUPPORTED_FORMAT;
import static core.YandexSpellerConstants.*;
import static core.YandexSpellerConstants.Language.UNSUPPORTED_LANG;
import static core.YandexSpellerConstants.Options.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CheckTextsTestsHW {

    @Test(description = "Check correction of wrong words for all supported languages",
            dataProvider = "wrongWordDataProvider", dataProviderClass = DataProviders.class,
            retryAnalyzer = utils.RetryAnalyzer.class)
    public void checkWrongWordsCorrection(String[] texts, Language lang, List[] expectedSuggestions) {
        List<List<YandexSpellerAnswer>> answers =
                YandexSpellerApi.getYandexSpellerAnswersArray(
                        YandexSpellerApi.with().texts(texts).language(lang).callApi());

        //Assert that there are correct number of suggestions received in response
        assertThat(answers.size(), equalTo(texts.length));

        //Assert that suggestion are expected
        try (AutoCloseableSoftAssertions soft = new AutoCloseableSoftAssertions()) {
            for (int i = 0; i < texts.length; i++) {
                //Check that current response array item is not empty
                if (!answers.get(i).isEmpty()) {
                    soft.assertThat(answers.get(i).get(0).suggestions)
                            .as("Actual suggestions")
                            .isEqualTo(expectedSuggestions[i]);
                } else {
                    soft.assertThat(answers.get(i)).as("Actual suggestions").isEmpty();
                }
            }
        }
    }

    // Seems to be a BUG - no correction for words with wrong capitalization.
    @Test(description = "Check correction of wrong capitalization for all supported languages",
            dataProvider = "capitalizationDataProvider", dataProviderClass = DataProviders.class,
            retryAnalyzer = utils.RetryAnalyzer.class)
    public void checkCapitalizationCorrection(String[] texts, Language lang, List[] expectedSuggestions) {
        List<List<YandexSpellerAnswer>> answers =
                YandexSpellerApi.getYandexSpellerAnswersArray(
                        YandexSpellerApi.with().texts(texts).language(lang).callApi());

        //Assert that there are correct number of suggestions received in response
        assertThat(answers.size(), equalTo(texts.length));

        //Assert that suggestion are expected
        try (AutoCloseableSoftAssertions soft = new AutoCloseableSoftAssertions()) {
            for (int i = 0; i < texts.length; i++) {
                //Check that current response array item is not empty
                if (!answers.get(i).isEmpty()) {
                    soft.assertThat(answers.get(i).get(0).suggestions)
                            .as("Actual suggestions")
                            .isEqualTo(expectedSuggestions[i]);
                } else {
                    soft.assertThat(answers.get(i)).as("Actual suggestions for text <" + texts[i] + ">")
                            .isEqualTo(expectedSuggestions[i]);
                }
            }
        }
    }

    @Test(description = "Check there is no correction for correct words",
            dataProvider = "correctWordsDataProvider", dataProviderClass = DataProviders.class,
            retryAnalyzer = utils.RetryAnalyzer.class)
    public void checkNoSuggestionForCorrectWords(String[] texts, Language lang) {
        List<List<YandexSpellerAnswer>> answers =
                YandexSpellerApi.getYandexSpellerAnswersArray(
                        YandexSpellerApi.with().texts(texts).language(lang).callApi());

        //Assert that there are correct number of answers received in response
        assertThat(answers.size(), equalTo(texts.length));

        //Assert that suggestion are expected
        try (AutoCloseableSoftAssertions soft = new AutoCloseableSoftAssertions()) {
            for (int i = 0; i < texts.length; i++) {
                //Check that current response array item is empty
                soft.assertThat(answers.get(i)).as("Suggestion for the correct word <" + texts[i] + ">")
                        .isEmpty();
            }
        }
    }

    @Test(description = "Check IGNORE_DIGITS option for all supported languages",
            dataProvider = "alphaNumericStringsDataProvider", dataProviderClass = DataProviders.class,
            retryAnalyzer = utils.RetryAnalyzer.class)
    public void checkIgnoreDigits(String[] texts, Language lang) {
        List<List<YandexSpellerAnswer>> answers =
                YandexSpellerApi.getYandexSpellerAnswersArray(
                        YandexSpellerApi.with().texts(texts).language(lang).options(IGNORE_DIGITS).callApi());

        //Assert that there are correct number of answers received in response
        assertThat(answers.size(), equalTo(texts.length));

        //Assert that suggestion are expected
        try (AutoCloseableSoftAssertions soft = new AutoCloseableSoftAssertions()) {
            for (int i = 0; i < texts.length; i++) {
                //Check that current response array item is empty
                soft.assertThat(answers.get(i)).as("Suggestion for the alphanumeric string <" + texts[i] + ">")
                        .isEmpty();
            }
        }
    }

    @Test(description = "Check IGNORE_URLS option for all supported languages",
            dataProvider = "URLDataProvider", dataProviderClass = DataProviders.class,
            retryAnalyzer = utils.RetryAnalyzer.class)
    public void checkIgnoreURLs(String[] texts, Language lang) {
        List<List<YandexSpellerAnswer>> answers =
                YandexSpellerApi.getYandexSpellerAnswersArray(
                        YandexSpellerApi.with().texts(texts).language(lang).options(IGNORE_URLS).callApi());

        //Assert that there are correct number of answers received in response
        assertThat(answers.size(), equalTo(texts.length));

        //Assert that suggestion are expected
        try (AutoCloseableSoftAssertions soft = new AutoCloseableSoftAssertions()) {
            for (int i = 0; i < texts.length; i++) {
                //Check that current response array item is empty
                soft.assertThat(answers.get(i))
                        .as("Suggestion for URL <" + texts[i] + ">")
                        .isEmpty();
            }
        }
    }

    //BUG - server answers with an empty responses for requests with repeated words even if the FIND_REPEAT_WORDS option activated.
    @Test(description = "Check FIND_REPEAT_WORDS option for all supported languages",
            dataProvider = "repeatWordsDataProvider", dataProviderClass = DataProviders.class,
            retryAnalyzer = utils.RetryAnalyzer.class)
    public void checkRepeatWords(String[] texts, String[] expectedSuggestion, Language lang) {
        List<List<YandexSpellerAnswer>> answers =
                YandexSpellerApi.getYandexSpellerAnswersArray(
                        YandexSpellerApi.with().texts(texts).language(lang).options(FIND_REPEAT_WORDS).callApi());

        //Assert that there are correct number of answers received in response
        assertThat(answers.size(), equalTo(texts.length));

        //Assert that suggestion are expected
        try (AutoCloseableSoftAssertions soft = new AutoCloseableSoftAssertions()) {
            for (int i = 0; i < texts.length; i++) {
                //Check that current response array item is not empty
                soft.assertThat(answers.get(i))
                        .as("Suggestion for string with repeated words <" + texts[i] + "> and lang <" + lang + ">")
                        .isEqualTo(expectedSuggestion[i]);
            }
        }
    }

    // Server always throws 504 error for incorrect "options" value - unexpected behavior.
    @Test(description = "Check the server'suggestions response for the request with incorrect options value",
            retryAnalyzer = utils.RetryAnalyzer.class)
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
                .body(Matchers.equalTo("SpellerService: Invalid parameter 'options'"));
    }

    //BUG - server unexpectedly returns "200" status code in case of wrong lang options was sent in request
    @Test(description = "Check the server'suggestions response for the request with incorrect language value",
            retryAnalyzer = utils.RetryAnalyzer.class)
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

    @Test(description = "Check the server's response for the request with incorrect text format type",
            retryAnalyzer = utils.RetryAnalyzer.class)
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
                .body(Matchers.containsString("SpellerService: Invalid parameter 'format'"));
    }

    @Test(description = "Check wrong language selection",
            dataProvider = "wrongLanguageWordsDataProvider", dataProviderClass = DataProviders.class,
            retryAnalyzer = utils.RetryAnalyzer.class)
    public void checkWrongLanguageWords(String[] texts, Language lang) {
        List<List<YandexSpellerAnswer>> answers =
                YandexSpellerApi.getYandexSpellerAnswersArray(
                        YandexSpellerApi.with().texts(texts).language(lang).callApi());

        //Assert that there are correct number of answers received in response
        assertThat(answers.size(), equalTo(texts.length));

        //Assert that suggestion are expected
        try (AutoCloseableSoftAssertions soft = new AutoCloseableSoftAssertions()) {
            for (int i = 0; i < texts.length; i++) {
                //Check that current response array item is not empty
                soft.assertThat(answers.get(i))
                        .as("Response for correct word <" + texts[i] + "> and lang <" + lang + ">")
                        .isEmpty();
            }
        }
    }

    @Test(description = "Check all methods for request and corresponding server responses",
            dataProviderClass = DataProviders.class, dataProvider = "methodsDataProvider",
            retryAnalyzer = utils.RetryAnalyzer.class)
    public void checkRequestWithDifferentMethods(Method requestMethod, int expectedCode, String expectedStatusLine) {
        YandexSpellerApi.with()
                .texts("This", "is", "test")
                .requestWithMethod(requestMethod)
                .callApi()
                .then()
                .assertThat()
                .statusCode(expectedCode)
                .and()
                .statusLine(expectedStatusLine);
    }
}

