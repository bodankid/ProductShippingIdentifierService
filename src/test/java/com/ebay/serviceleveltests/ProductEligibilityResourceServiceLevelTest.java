package com.ebay.serviceleveltests;

import com.ebay.Application;
import com.ebay.model.EligibilityCriteriaUpdateDetails;
import com.ebay.model.ProductDetails;
import com.ebay.model.ProductEligibilityResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductEligibilityResourceServiceLevelTest {

    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    WebApplicationContext wac;

    ObjectMapper mapper = new ObjectMapper();

    private static final String ELIGIBILITY_CHECK_URL = "http://localhost:%s%s/"+"product/eligibility";

    private static final String ELIGIBILITY_UPDATE_URL = "http://localhost:%s%s/"+"product/eligibility/update";

    private static final String CONTENT_TYPE = "Content-Type";

    @Test
    public void eligibilityCheckSLT() throws Exception{
        ProductDetails productDetails = getRequestBodyFromFile("serviceleveltests/requests/eligibility_check_request.json", ProductDetails.class);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> httpEntity = new HttpEntity<>(mapper.writeValueAsString(productDetails), httpHeaders);
        ResponseEntity<ProductEligibilityResponse> response = restTemplate.postForEntity(buildUrl(ELIGIBILITY_CHECK_URL), httpEntity, ProductEligibilityResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getIsProductEligible());
    }

    @Test
    public void updateEligibilitySLT() throws Exception{
        EligibilityCriteriaUpdateDetails eligibilityCriteriaUpdateDetails = getRequestBodyFromFile("serviceleveltests/requests/update_eligibility_request.json", EligibilityCriteriaUpdateDetails.class);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> httpEntity = new HttpEntity<>(mapper.writeValueAsString(eligibilityCriteriaUpdateDetails), httpHeaders);
        ResponseEntity<ProductEligibilityResponse> response = restTemplate.postForEntity(buildUrl(ELIGIBILITY_UPDATE_URL), httpEntity, ProductEligibilityResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ProductDetails productDetails = getRequestBodyFromFile("serviceleveltests/requests/eligibility_check_request_after_update_for_success_response.json", ProductDetails.class);
        httpEntity = new HttpEntity<>(mapper.writeValueAsString(productDetails), httpHeaders);
        response = restTemplate.postForEntity(buildUrl(ELIGIBILITY_CHECK_URL), httpEntity, ProductEligibilityResponse.class);
        assertTrue(response.getBody().getIsProductEligible());
        productDetails = getRequestBodyFromFile("serviceleveltests/requests/eligibility_check_request_after_update_for_failure_response.json", ProductDetails.class);
        httpEntity = new HttpEntity<>(mapper.writeValueAsString(productDetails), httpHeaders);
        response = restTemplate.postForEntity(buildUrl(ELIGIBILITY_CHECK_URL), httpEntity, ProductEligibilityResponse.class);
        assertFalse(response.getBody().getIsProductEligible());
    }

    private <T> T getRequestBodyFromFile(String filePath, Class<T> valueType) throws IOException {
        File file = new File(this.getClass().getClassLoader().getResource(filePath).getFile());
        return mapper.readValue(file, valueType);
    }

    private String buildUrl(String urlTemplate, String... params) {
        return String.format(urlTemplate, Lists.asList(String.valueOf(port), wac.getServletContext().getContextPath(), params).toArray());
    }
}
