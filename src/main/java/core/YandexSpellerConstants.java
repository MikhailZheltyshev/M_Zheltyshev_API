package core;

public class YandexSpellerConstants {

    //useful constants for API under test
    public static final String YANDEX_SPELLER_API_URI_TEXT = "https://speller.yandex.net/services/spellservice.json/checkText";
    public static final String YANDEX_SPELLER_API_URI_TEXTS = "https://speller.yandex.net/services/spellservice.json/checkTexts";
    public static final String PARAM_TEXT = "text";
    public static final String PARAM_OPTIONS = "options";
    public static final String PARAM_LANG = "lang";
    public static final String WRONG_WORD_UK = "питаня";
    public static final String WORD_WITH_WRONG_CAPITAL = "moscow";
    public static final String WORD_WITH_LEADING_DIGITS = "11" + SimpleWord.BROTHER.corrVer;
    public static final String QUOTES = "\"";


    public enum SimpleWord{
        MOTHER("mother", "mottherr"),
        BROTHER("brother", "bbrother");

        private String corrVer;
        private String wrongVer;

        public String corrVer(){return corrVer;}
        public String wrongVer(){return wrongVer;}

        private SimpleWord(String corrVer, String wrongVer){
            this.corrVer = corrVer;
            this.wrongVer = wrongVer;

        }
    }

    public enum Language {
        RU("ru"),
        UK("uk"),
        EN("en"),
        UNSOPORTED("fr");
        private String languageCode;
        public String langCode(){return languageCode;}

        private Language(String lang) {
            this.languageCode = lang;
        }
    }
}