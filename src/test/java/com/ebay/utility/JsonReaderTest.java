package com.ebay.utility;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static com.ebay.constants.ProductEligibilityConstants.ELIGIBLE_CATEGORIES_KEY;
import static com.ebay.constants.ProductEligibilityConstants.ELIGIBLE_SELLERS_KEY;
import static com.ebay.constants.ProductEligibilityConstants.MIN_ITEM_PRICE_KEY;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class JsonReaderTest {

    private JsonReader jsonReader;

    private static final String ELIGIBILITY_CRITERIA_TEST_FILE = "eligibilityCriteria_unittest.json";

    @Before
    public void setup() {
        jsonReader = new JsonReader();
        ReflectionTestUtils.setField(jsonReader, "eligibilityCriteriaFile", ELIGIBILITY_CRITERIA_TEST_FILE);
    }

    @Test
    public void shouldReadEligibilityCriteriaFromFile() {
        List<String> sellerList = jsonReader.getEligibleCriteria(ELIGIBLE_SELLERS_KEY);
        List<String> categoriesList = jsonReader.getEligibleCriteria(ELIGIBLE_CATEGORIES_KEY);
        List<String> minPriceList = jsonReader.getEligibleCriteria(MIN_ITEM_PRICE_KEY);
        assertEquals(2, sellerList.size());
        assertEquals("peter", sellerList.get(0));
        assertEquals("adam", sellerList.get(1));
        assertEquals(2, categoriesList.size());
        assertEquals("1", categoriesList.get(0));
        assertEquals("4", categoriesList.get(1));
        assertEquals("12.50", minPriceList.get(0));
    }
}
