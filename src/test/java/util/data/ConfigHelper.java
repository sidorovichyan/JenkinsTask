package util.data;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;

public class ConfigHelper {

    private static final String KEY_TEST_BROWSER = "test-browser";
    private static final String KEY_MAIN_PAGE = "main-page-url";
    private static final String KEY_EXPLICIT_TIME = "explicit-wait-time";
    private static final String KEY_BROWSER_MODE = "test-browser-mode";
    private static final String PATH_TO_CONFIG_JSON = "configData/ConfigData.json";

    private static HashMap<String, String> configDataMap;

    private static ConfigHelper instance;

    private ConfigHelper() {
    }

    public static ConfigHelper getInstance() {
        if (instance == null) {
            instance = new ConfigHelper();
            instance.init();
        }
        return instance;
    }

    private void init() {
        ObjectMapper mapper = new ObjectMapper();
        URL res = getClass().getClassLoader().getResource(PATH_TO_CONFIG_JSON);
        byte[] jsonData = new byte[0];
        try {
            assert res != null;
            jsonData = Files.readAllBytes(Paths.get(res.toURI()));
            configDataMap = mapper.readValue(jsonData, HashMap.class);
        } catch (IOException | URISyntaxException e) {
            throw new Error("Error while read/open file: " + PATH_TO_CONFIG_JSON + e);
        }
    }

    public String getTestBrowser() {
        return configDataMap.get(KEY_TEST_BROWSER);
    }

    public String getUrlMainPage() {
        return configDataMap.get(KEY_MAIN_PAGE);
    }


    public Duration getExplicitWaitTime() {
        return Duration.ofSeconds(Long.parseLong(configDataMap.get(KEY_EXPLICIT_TIME)));
    }

    public String getBrowserMode() {
        return configDataMap.get(KEY_BROWSER_MODE);
    }


}
