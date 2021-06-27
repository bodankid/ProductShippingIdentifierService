package com.ebay.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EligibilityCriteriaUpdateDetails {

    private List<String> sellersToBeAdded;

    private List<String> sellersToBeRemoved;

    private List<Integer> categoriesToBeAdded;

    private List<Integer> categoriesToBeRemoved;

    private double newMinPrice;

    public List<String> getSellersToBeAdded() {
        return sellersToBeAdded;
    }

    public void setSellersToBeAdded(List<String> sellersToBeAdded) {
        this.sellersToBeAdded = sellersToBeAdded;
    }

    public List<String> getSellersToBeRemoved() {
        return sellersToBeRemoved;
    }

    public void setSellersToBeRemoved(List<String> sellersToBeRemoved) {
        this.sellersToBeRemoved = sellersToBeRemoved;
    }

    public List<Integer> getCategoriesToBeAdded() {
        return categoriesToBeAdded;
    }

    public void setCategoriesToBeAdded(List<Integer> categoriesToBeAdded) {
        this.categoriesToBeAdded = categoriesToBeAdded;
    }

    public List<Integer> getCategoriesToBeRemoved() {
        return categoriesToBeRemoved;
    }

    public void setCategoriesToBeRemoved(List<Integer> categoriesToBeRemoved) {
        this.categoriesToBeRemoved = categoriesToBeRemoved;
    }

    public double getNewMinPrice() {
        return newMinPrice;
    }

    public void setNewMinPrice(double newMinPrice) {
        this.newMinPrice = newMinPrice;
    }
}
