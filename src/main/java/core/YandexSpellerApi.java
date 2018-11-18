package core;

import beans.YandexSpellerAnswer;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;

import java.util.*;

import static core.YandexSpellerConstants.*;
import static io.restassured.http.Method.*;
import static org.hamcrest.Matchers.lessThan;

public class YandexSpellerApi {


    //builder pattern
    private YandexSpellerApi() {
    }

    private HashMap<String, String> params = new HashMap<>();
    //Collection to store some multi params (e.g. collection of texts)
    private HashMap<String, List<String>> multiParams = new HashMap<>();

    //Default value of request method is GET
    public Method currentMethod = GET;

    public static class ApiBuilder {
        YandexSpellerApi spellerApi;

        private ApiBuilder(YandexSpellerApi gcApi) {
            spellerApi = gcApi;
        }

        public ApiBuilder text(String text) {
            spellerApi.params.put(PARAM_TEXT, text);
            return this;
        }

        public ApiBuilder texts(String... texts) {
            List<String> textsContainer = new ArrayList<>();
            textsContainer.addAll(Arrays.asList(texts));
            spellerApi.multiParams.put(PARAM_TEXT, textsContainer);
            return this;
        }

        public ApiBuilder options(String options) {
            spellerApi.params.put(PARAM_OPTIONS, options);
            return this;
        }

        public ApiBuilder options(Options options) {
            spellerApi.params.put(PARAM_OPTIONS, options.toString());
            return this;
        }

        public ApiBuilder language(Language language) {
            spellerApi.params.put(PARAM_LANG, language.langCode());
            return this;
        }

        public ApiBuilder requestWithMethod(Method method) {
            spellerApi.currentMethod = method;
            return this;
        }

        public Response callApi() {
            return RestAssured.with()
                    .queryParams(spellerApi.params)
                    .queryParams(spellerApi.multiParams)
                    .log().all()
                    //get() method is replaced with more common request() to allow changing of request method type
                    .request(spellerApi.currentMethod, YANDEX_SPELLER_API_URI_TEXTS)
                    .prettyPeek();
        }
    }

    public static ApiBuilder with() {
        YandexSpellerApi api = new YandexSpellerApi();
        return new ApiBuilder(api);
    }


    //get ready Speller answers list from api response
    public static List<YandexSpellerAnswer> getYandexSpellerAnswers(Response response) {
        return new Gson().fromJson(response.asString().trim(), new TypeToken<List<YandexSpellerAnswer>>() {
        }.getType());
    }

    //get ready Speller answers array
    public static List<List<YandexSpellerAnswer>> getYandexSpellerAnswersArray(Response response) {
        try {
            return new Gson().fromJson(response.asString().trim(), new TypeToken<List<List<YandexSpellerAnswer>>>() {
            }.getType());
        } catch (JsonSyntaxException ex) {
            return new ArrayList<>();
        }
    }


    //set base request and response specifications tu use in tests
    public static ResponseSpecification successResponse() {
        return new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectHeader("Connection", "keep-alive")
                .expectResponseTime(lessThan(20000L))
                .expectStatusCode(HttpStatus.SC_OK)
                .build();
    }

    public static RequestSpecification baseRequestConfiguration() {
        return new RequestSpecBuilder()
                .setAccept(ContentType.XML)
                .setRelaxedHTTPSValidation()
                .addHeader("custom header2", "header2.value")
                .addQueryParam("requestID", new Random().nextLong())
                .setBaseUri(YANDEX_SPELLER_API_URI_TEXT)
                .build();
    }
}
