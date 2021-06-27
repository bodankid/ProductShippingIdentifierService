package com.ebay.service;

import com.ebay.model.EligibilityCriteriaUpdateDetails;
import com.ebay.utility.JsonReader;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ebay.constants.ProductEligibilityConstants.ELIGIBLE_CATEGORIES_KEY;
import static com.ebay.constants.ProductEligibilityConstants.ELIGIBLE_SELLERS_KEY;
import static com.ebay.constants.ProductEligibilityConstants.MIN_ITEM_PRICE_KEY;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class EligibilityCriteriaService {

    @Autowired
    JsonReader jsonReader;

    private static final Logger logger = getLogger(EligibilityCriteriaService.class);

    private LoadingCache<String, List<String>> eligibilityCriteria;

    public EligibilityCriteriaService() {
        eligibilityCriteria = setupCache();
    }

    private LoadingCache<String, List<String>> setupCache() {
        return CacheBuilder
                .newBuilder()
                .build(new CacheLoader<String, List<String>>() {
                    @Override
                    public List<String> load(String criteria) throws IOException {
                        return jsonReader.getEligibleCriteria(criteria);
                    }
                });
    }

    public List<String> getEligibleSellers() {
        try {
            return eligibilityCriteria.get(ELIGIBLE_SELLERS_KEY);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    public List<Integer> getEligibleCategories() {
        try {
            List<String> categories = eligibilityCriteria.get(ELIGIBLE_CATEGORIES_KEY);
            if(!CollectionUtils.isEmpty(categories)) {
                return categories.stream().map(Integer::parseInt).collect(Collectors.toList());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    public String getMinItemPrice() {
        try {
            List<String> priceList = eligibilityCriteria.get(MIN_ITEM_PRICE_KEY);
            return priceList.get(0);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public void updateEligibilityCriteria(EligibilityCriteriaUpdateDetails eligibilityCriteriaUpdateDetails) {
        List<String> eligibleSellers = updateEligibleSellers(eligibilityCriteriaUpdateDetails);
        List<String> eligibleCategories = updateEligibleCategories(eligibilityCriteriaUpdateDetails);
        List<String> minPrice = new ArrayList<>();
        if (eligibilityCriteriaUpdateDetails.getNewMinPrice() != 0) {
            minPrice = updateMinPrice(eligibilityCriteriaUpdateDetails);
        } else {
            minPrice.add(getMinItemPrice());
        }
        jsonReader.updateEligibilityCriteriaInFile(eligibleSellers, eligibleCategories, minPrice);
    }

    private List<String> updateEligibleSellers(EligibilityCriteriaUpdateDetails eligibilityCriteriaUpdateDetails) {
        List<String> eligibleSellers = collectionToLowerCase(getEligibleSellers());
        if (!CollectionUtils.isEmpty(eligibilityCriteriaUpdateDetails.getSellersToBeAdded())) {
            List<String> sellerListToBeAdded = collectionToLowerCase(eligibilityCriteriaUpdateDetails.getSellersToBeAdded());
            sellerListToBeAdded = sellerListToBeAdded.stream().filter(seller -> !eligibleSellers.contains(seller))
                    .collect(Collectors.toList());
            eligibleSellers.addAll(sellerListToBeAdded);
        }
        if (!CollectionUtils.isEmpty(eligibilityCriteriaUpdateDetails.getSellersToBeRemoved())) {
            eligibleSellers.removeAll(collectionToLowerCase(eligibilityCriteriaUpdateDetails.getSellersToBeRemoved()));
        }
        eligibilityCriteria.put(ELIGIBLE_SELLERS_KEY, eligibleSellers);
        return eligibleSellers;
    }

    private List<String> updateEligibleCategories(EligibilityCriteriaUpdateDetails eligibilityCriteriaUpdateDetails) {
        List<Integer> eligibleCategories = getEligibleCategories();
        if (!CollectionUtils.isEmpty(eligibilityCriteriaUpdateDetails.getCategoriesToBeAdded())) {
            List<Integer> categoriesListToBeAdded = eligibilityCriteriaUpdateDetails.getCategoriesToBeAdded()
                    .stream().filter(category -> !eligibleCategories.contains(category)).collect(Collectors.toList());
            eligibleCategories.addAll(categoriesListToBeAdded);
        }
        if (!CollectionUtils.isEmpty(eligibilityCriteriaUpdateDetails.getCategoriesToBeRemoved())) {
            eligibleCategories.removeAll(eligibilityCriteriaUpdateDetails.getCategoriesToBeRemoved());
        }
        List<String> eligibleCategoriesStringList = eligibleCategories.stream().map(Object::toString).collect(Collectors.toList());
        eligibilityCriteria.put(ELIGIBLE_CATEGORIES_KEY, eligibleCategoriesStringList);
        return eligibleCategoriesStringList;
    }

    private List<String> updateMinPrice(EligibilityCriteriaUpdateDetails eligibiltyCriteriaUpdateDetails) {
        List<String> minPriceList = new ArrayList<>();
        minPriceList.add(String.valueOf(eligibiltyCriteriaUpdateDetails.getNewMinPrice()));
        eligibilityCriteria.put(MIN_ITEM_PRICE_KEY, minPriceList);
        return minPriceList;
    }

    private List<String> collectionToLowerCase(List<String> collection) {
        if (!CollectionUtils.isEmpty(collection)) {
            collection = collection.stream().map(String::toLowerCase).collect(Collectors.toList());
        }
        return collection;
    }
}
