package com.ebay.resource;

import com.ebay.model.EligibilityCriteriaUpdateDetails;
import com.ebay.model.ProductDetails;
import com.ebay.model.ProductEligibilityResponse;
import com.ebay.service.EligibilityCriteriaService;
import com.ebay.service.ProductEligibilityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductEligibilityResourceTest {

    @Mock
    ProductEligibilityService productEligibilityService;

    @Mock
    EligibilityCriteriaService eligibilityCriteriaService;

    @InjectMocks
    ProductEligibilityResource productEligibilityResource;

    @Test
    public void shouldCheckIfProductIsEligibleForNewShippingService() throws Exception {
        ProductDetails productDetails = new ProductDetails();
        ProductEligibilityResponse productEligibilityResponse = new ProductEligibilityResponse();
        when(productEligibilityService.checkProductEligibility(productDetails)).thenReturn(productEligibilityResponse);
        ResponseEntity<ProductEligibilityResponse> responseEntity = productEligibilityResource.checkIfProductIsEligibleForNewShippingService(productDetails);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(productEligibilityResponse, responseEntity.getBody());
        verify(productEligibilityService, times(1)).checkProductEligibility(productDetails);
    }

    @Test
    public void shouldUpdateEligibilityCriteriaForNewShippingService() throws Exception {
        EligibilityCriteriaUpdateDetails eligibilityCriteriaUpdateDetails = new EligibilityCriteriaUpdateDetails();
        doNothing().when(eligibilityCriteriaService).updateEligibilityCriteria(eligibilityCriteriaUpdateDetails);
        ResponseEntity<HttpStatus> responseEntity = productEligibilityResource.updateEligibilityCriteriaForNewShippingService(eligibilityCriteriaUpdateDetails);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(eligibilityCriteriaService, times(1)).updateEligibilityCriteria(eligibilityCriteriaUpdateDetails);
    }
}
