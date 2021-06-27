package com.ebay.resource;

import com.ebay.model.EligibilityCriteriaUpdateDetails;
import com.ebay.model.ProductDetails;
import com.ebay.model.ProductEligibilityResponse;
import com.ebay.service.EligibilityCriteriaService;
import com.ebay.service.ProductEligibilityService;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.ebay.constants.ProductEligibilityConstants.EXCEPTION_MESSAGE;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class ProductEligibilityResource {

    @Autowired
    ProductEligibilityService productEligibilityService;

    @Autowired
    EligibilityCriteriaService eligibilityCriteriaService;

    private Logger logger = getLogger(ProductEligibilityResource.class);

    @PostMapping(value = "/product/eligibility")
    @ResponseBody
    public ResponseEntity<ProductEligibilityResponse> checkIfProductIsEligibleForNewShippingService
            (@RequestBody ProductDetails productDetails) {
        ProductEligibilityResponse productEligibilityResponse = productEligibilityService.checkProductEligibility(productDetails);
        return new ResponseEntity<>(productEligibilityResponse, OK);
    }

    @PostMapping(value = "/product/eligibility/update")
    @ResponseBody
    public ResponseEntity<HttpStatus> updateEligibilityCriteriaForNewShippingService
            (@RequestBody EligibilityCriteriaUpdateDetails eligibilityCriteriaUpdateDetails) {
        eligibilityCriteriaService.updateEligibilityCriteria(eligibilityCriteriaUpdateDetails);
        return new ResponseEntity<>(OK);
    }

    @ExceptionHandler({NullPointerException.class, ParseException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return EXCEPTION_MESSAGE;
    }
}
