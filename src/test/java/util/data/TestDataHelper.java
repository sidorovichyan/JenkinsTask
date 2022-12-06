package util.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.ProductAndOS;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class TestDataHelper {

    private static final String PATH_TO_PRODUCT_AND_OS_JSON = "testData/ProductAndOS.json";

    private static final String PATH_TO_SEARCH = "testData/TestData.json";

    private List<ProductAndOS> productAndOSList;

    private static HashMap<String, String> testData;

    private static TestDataHelper instance;

    private TestDataHelper() {
        this.init();
    }

    public static TestDataHelper getInstance() {
        if (instance == null) {
            instance = new TestDataHelper();
            instance.init();
        }

        return instance;
    }

    private void init() {
        loadProductAndOSList();
        loadTestData();

    }

    private void loadTestData() {
        ObjectMapper mapper = new ObjectMapper();
        URL res = getClass().getClassLoader().getResource(PATH_TO_SEARCH);
        byte[] jsonData = new byte[0];
        try {
            assert res != null;
            jsonData = Files.readAllBytes(Paths.get(res.toURI()));
            ObjectMapper objectMapper = new ObjectMapper();
            testData = mapper.readValue(jsonData, HashMap.class);
        } catch (IOException | URISyntaxException e) {
            throw new Error("Error while read/open file: " + PATH_TO_SEARCH + e);
        }
    }

    private void loadProductAndOSList() {
        URL res = getClass().getClassLoader().getResource(PATH_TO_PRODUCT_AND_OS_JSON);
        byte[] jsonData = new byte[0];
        try {
            jsonData = Files.readAllBytes(Paths.get(res.toURI()));
            ObjectMapper objectMapper = new ObjectMapper();
            ProductAndOS[] usersArrayJSON = objectMapper.readValue(jsonData, ProductAndOS[].class);
            productAndOSList = new ArrayList<>(Arrays.asList(usersArrayJSON));
        } catch (IOException | URISyntaxException e) {
            throw new Error("Error while read/open file: " + PATH_TO_PRODUCT_AND_OS_JSON + e);
        }
    }

    public List<ProductAndOS> getProductAndOSList() {
        return productAndOSList;
    }

    public String getValue(String key)
    {
        return testData.get(key);
    }

}
