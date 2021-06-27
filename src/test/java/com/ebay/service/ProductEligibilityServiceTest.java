package com.ebay.service;

import com.ebay.model.ProductDetails;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductEligibilityServiceTest {

    @Mock
    EligibilityCriteriaService eligibilityCriteriaService;

    @InjectMocks
    ProductEligibilityService productEligibilityService;

    @Test
    public void shouldCheckProductEligibilityTest() throws Exception {
        List<String> sellerList = buildSellerList();
        List<Integer> categoriesList = buildCategoriesList();
        ProductDetails productDetails= buildProductDetails("test", 2, 15.00);
        when(eligibilityCriteriaService.getEligibleSellers()).thenReturn(sellerList);
        when(eligibilityCriteriaService.getEligibleCategories()).thenReturn(categoriesList);
        when(eligibilityCriteriaService.getMinItemPrice()).thenReturn("10.00");
        assertTrue(productEligibilityService.checkProductEligibility(productDetails).getIsProductEligible());
        //not eligible seller
        productDetails= buildProductDetails("test3", 2, 15.00);
        assertFalse(productEligibilityService.checkProductEligibility(productDetails).getIsProductEligible());
        //not eligible category
        productDetails= buildProductDetails("test1", 3, 15.00);
        assertFalse(productEligibilityService.checkProductEligibility(productDetails).getIsProductEligible());
        //not eligible price
        productDetails= buildProductDetails("test1", 2, 9.99);
        assertFalse(productEligibilityService.checkProductEligibility(productDetails).getIsProductEligible());
        //null value for seller
        productDetails= buildProductDetails(null, 2, 15.00);
        assertFalse(productEligibilityService.checkProductEligibility(productDetails).getIsProductEligible());
    }

    private ProductDetails buildProductDetails(String seller, int category, double price) {
        ProductDetails productDetails = new ProductDetails();
        productDetails.setSeller(seller);
        productDetails.setCategory(category);
        productDetails.setPrice(price);
        return productDetails;
    }

    private List<String> buildSellerList() {
        List<String> sellerList = new ArrayList<>();
        sellerList.add("TEST");
        sellerList.add("TEST1");
        return sellerList;
    }

    private List<Integer> buildCategoriesList() {
        List<Integer> categoriesList = new ArrayList<>();
        categoriesList.add(1);
        categoriesList.add(2);
        return categoriesList;
    }
}
