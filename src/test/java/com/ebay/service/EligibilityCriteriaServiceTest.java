package com.ebay.service;

import com.ebay.model.EligibilityCriteriaUpdateDetails;
import com.ebay.utility.JsonReader;
import com.google.common.cache.LoadingCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static com.ebay.constants.ProductEligibilityConstants.ELIGIBLE_CATEGORIES_KEY;
import static com.ebay.constants.ProductEligibilityConstants.ELIGIBLE_SELLERS_KEY;
import static com.ebay.constants.ProductEligibilityConstants.MIN_ITEM_PRICE_KEY;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EligibilityCriteriaServiceTest {

    @Mock
    LoadingCache<String, List<String>> eligibilityCriteria;

    @Mock
    EligibilityCriteriaUpdateDetails eligibilityCriteriaUpdateDetails;

    @Mock
    JsonReader jsonReader;

    @InjectMocks
    EligibilityCriteriaService eligibilityCriteriaService;

    @Test
    public void shouldGetEligibleSellersTest() throws Exception {
        List<String> sellerList = new ArrayList<>();
        sellerList.add("test");
        when(eligibilityCriteria.get(ELIGIBLE_SELLERS_KEY)).thenReturn(sellerList);
        List<String> resp = eligibilityCriteriaService.getEligibleSellers();
        assertEquals("test", resp.get(0));
    }

    @Test
    public void shouldGetEligibleCategoriesTest() throws Exception {
        List<String> categoriesList = new ArrayList<>();
        categoriesList.add("1");
        when(eligibilityCriteria.get(ELIGIBLE_CATEGORIES_KEY)).thenReturn(categoriesList);
        List<Integer> resp = eligibilityCriteriaService.getEligibleCategories();
        assertEquals(new Integer("1"), resp.get(0));
    }

    @Test
    public void shouldGetMinItemPriceTest() throws Exception {
        List<String> minPriceList = new ArrayList<>();
        minPriceList.add("10.00");
        when(eligibilityCriteria.get(MIN_ITEM_PRICE_KEY)).thenReturn(minPriceList);
        String resp = eligibilityCriteriaService.getMinItemPrice();
        assertEquals("10.00", resp);
    }

    @Test
    public void shouldUpdateSellersTest() throws Exception {
        List<String> sellerList = new ArrayList<>();
        sellerList.add("TEST");
        sellerList.add("TEST1");
        List<String> sellerListToBeAdded = new ArrayList<>();
        sellerListToBeAdded.add("test");
        sellerListToBeAdded.add("TEST2");
        List<String> sellerListToBeRemoved = new ArrayList<>();
        sellerListToBeRemoved.add("test1");
        sellerListToBeRemoved.add("TEST3");
        List<String> categoriesList = new ArrayList<>();
        categoriesList.add("1");
        categoriesList.add("2");
        List<Integer> categoriesListToBeAdded = new ArrayList<>();
        categoriesListToBeAdded.add(2);
        categoriesListToBeAdded.add(3);
        List<Integer> categoriesListToBeRemoved = new ArrayList<>();
        categoriesListToBeRemoved.add(1);
        categoriesListToBeRemoved.add(4);
        when(eligibilityCriteria.get(ELIGIBLE_SELLERS_KEY)).thenReturn(sellerList);
        when(eligibilityCriteriaUpdateDetails.getSellersToBeAdded()).thenReturn(sellerListToBeAdded);
        when(eligibilityCriteriaUpdateDetails.getSellersToBeRemoved()).thenReturn(sellerListToBeRemoved);
        when(eligibilityCriteria.get(ELIGIBLE_CATEGORIES_KEY)).thenReturn(categoriesList);
        when(eligibilityCriteriaUpdateDetails.getCategoriesToBeAdded()).thenReturn(categoriesListToBeAdded);
        when(eligibilityCriteriaUpdateDetails.getCategoriesToBeRemoved()).thenReturn(categoriesListToBeRemoved);
        when(eligibilityCriteriaUpdateDetails.getNewMinPrice()).thenReturn(20.00);
        doNothing().when(jsonReader).updateEligibilityCriteriaInFile(anyList(), anyList(), anyList());
        eligibilityCriteriaService.updateEligibilityCriteria(eligibilityCriteriaUpdateDetails);
        verify(eligibilityCriteria, times(1)).get(ELIGIBLE_SELLERS_KEY);
        verify(eligibilityCriteria, times(1)).get(ELIGIBLE_CATEGORIES_KEY);
        verify(eligibilityCriteria, times(1)).put(eq(ELIGIBLE_SELLERS_KEY), anyList());
        verify(eligibilityCriteria, times(1)).put(eq(ELIGIBLE_CATEGORIES_KEY), anyList());
        verify(eligibilityCriteria, times(1)).put(eq(MIN_ITEM_PRICE_KEY), anyList());
    }
}
