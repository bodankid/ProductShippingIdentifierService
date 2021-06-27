package com.ebay.utility;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.ebay.constants.ProductEligibilityConstants.ELIGIBLE_CATEGORIES_KEY;
import static com.ebay.constants.ProductEligibilityConstants.ELIGIBLE_SELLERS_KEY;
import static com.ebay.constants.ProductEligibilityConstants.MIN_ITEM_PRICE_KEY;

@SuppressWarnings("unchecked")
@Component
public class JsonReader {

    @Value("${eligibility.criteria.file}")
    private String eligibilityCriteriaFile;

    private JSONObject readEligibilityCriteriaFromFile() {
        JSONParser jsonParser = new JSONParser();
        Resource resource = new ClassPathResource(eligibilityCriteriaFile);
        try {
            InputStream inputStream = resource.getInputStream();
            byte[] byteData = FileCopyUtils.copyToByteArray(inputStream);
            String data = new String(byteData, StandardCharsets.UTF_8);
            Object obj = jsonParser.parse(data);
            return (JSONObject) obj;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getEligibleCriteria(String criteria) {
        JSONObject jsonObject = readEligibilityCriteriaFromFile();
        JSONArray jsonArray = (JSONArray) jsonObject.get(criteria);
        List<String> stringList = new ArrayList<>();
        jsonArray.forEach(val -> stringList.add(val.toString()));
        return stringList;
    }

    public void updateEligibilityCriteriaInFile(List<String> eligibleSellers, List<String> eligibleCategories, List<String> minPrice) {
        JSONObject jsonObject = readEligibilityCriteriaFromFile();
        jsonObject.put(ELIGIBLE_SELLERS_KEY, eligibleSellers);
        jsonObject.put(ELIGIBLE_CATEGORIES_KEY, eligibleCategories);
        jsonObject.put(MIN_ITEM_PRICE_KEY, minPrice);
        Resource resource = new ClassPathResource(eligibilityCriteriaFile);
        try {
            FileWriter fileWriter = new FileWriter(new File(resource.getURI()));
            fileWriter.write(jsonObject.toJSONString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
