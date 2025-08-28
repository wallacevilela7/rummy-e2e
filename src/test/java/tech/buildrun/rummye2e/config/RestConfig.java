package tech.buildrun.rummye2e.config;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RestConfig {

    private final PropertiesConfig propertiesConfig;

    public RestConfig(PropertiesConfig propertiesConfig) {
        this.propertiesConfig = propertiesConfig;
    }

    public RequestSpecification givenBackend() {
        return RestAssured.given()
                .baseUri(propertiesConfig.getBackendUrl())
                .filters(List.of(new RequestLoggingFilter() , new ResponseLoggingFilter()));
    }
}
