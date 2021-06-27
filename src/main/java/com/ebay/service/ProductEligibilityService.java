package com.ebay.service;

import com.ebay.model.ProductDetails;
import com.ebay.model.ProductEligibilityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class ProductEligibilityService {

    @Autowired
    EligibilityCriteriaService eligibilityCriteriaService;

    public ProductEligibilityResponse checkProductEligibility(ProductDetails productDetails) {
        boolean isSellerEligible = false;
        if(StringUtils.hasLength(productDetails.getSeller())) {
            isSellerEligible = checkForSellerEligibility(productDetails.getSeller());
        }
        boolean isCategoryEligible = checkForCategoryEligibility(productDetails.getCategory());
        boolean isPriceEligible = checkForPriceEligibility(productDetails.getPrice());
        ProductEligibilityResponse productEligibilityResponse = new ProductEligibilityResponse();
        productEligibilityResponse.setIsProductEligible(isSellerEligible && isCategoryEligible && isPriceEligible);
        return productEligibilityResponse;
    }

    private boolean checkForSellerEligibility(String seller) {
        List<String> eligibleSellers = eligibilityCriteriaService.getEligibleSellers();
        return !CollectionUtils.isEmpty(eligibleSellers) && eligibleSellers.stream().anyMatch(seller::equalsIgnoreCase);
    }

    private boolean checkForCategoryEligibility(int category) {
        List<Integer> eligibleCategories = eligibilityCriteriaService.getEligibleCategories();
        return !CollectionUtils.isEmpty(eligibleCategories) && eligibleCategories.stream().anyMatch(eligibleCategory -> category == eligibleCategory);
    }

    private boolean checkForPriceEligibility(double price) {
        String minPrice = eligibilityCriteriaService.getMinItemPrice();
        return minPrice != null && price >= Double.parseDouble(minPrice);
    }
}
