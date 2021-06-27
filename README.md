

ebay - ProductShippingIdentifierService
====================
This service determines whether a product is eligible for the new shipping service or not.
Following Rules are validated to determine the products eligibility:
- seller of the produt should be in the predefined seller list.
- product category should belong to a predefined category list.
- product price should be greater than a minimum price.<br />

the rules are maintained in `eligibilityCriteria.json`, the ideal way is to maintain the rules in the database and cache the values in the service but for convinenece sake we are using a file
```
{
  "eligibleSellers" : ["Tom", "Jack"],
  "eligibleCategories": ["2","3"],
  "minItemPrice": ["10.50"]
}
```

there is also an endpoint created to update the rules whithout making any changes to the service.

Requirements
------------
- `Java 8`

Building project
----------------
To build the project, run:
```
./gradlew clean build
```
this will build the project as well as run the test suite.

#### Managing properties and profiles
this service uses properties files to manage application properties.
These properties files can be found in `src/main/resources`.

All default properties values are set in application.properties, along with default profile.
Profile specific properties are set on each `application-<PROFILE_NAME>.properties`.

Running tests
-------------
To test the project, run:
```
./gradlew test
```

Starting application
--------------------
From project base directory, run:
```
java -jar build/libs/product-shipping-identifier-service-1.0.0.jar
```
or 
```
./gradlew bootRun
```
this will start the application by default on port 8080.

Accessing application
---------------------
Based on the properties specified to start application, Service healthcheck can be accessed at:
```
http://localhost:8080/shipping/system/health
```

Swagger documentation
---------------------------
Swagger documentation for the service can be found at
```
http://localhost:8080/shipping/swagger-ui.html
```

Sample Requests
---------------------------

#### Sample Eligibility check request
```
http://localhost:8080/shipping/product/eligibility

{
    "title": "test",
    "seller": "tom",
    "category": 2,
    "price": 16.00
}
```
#### Sample Eligibility update request
```
http://localhost:8080/shipping/product/eligibility/update

{
    "newMinPrice": 16.00,
    "sellersToBeAdded": ["peter"],
    "sellersToBeRemoved": ["tom"],
    "categoriesToBeAdded": [1, 5],
    "categoriesToBeRemoved": [2]
}
```

