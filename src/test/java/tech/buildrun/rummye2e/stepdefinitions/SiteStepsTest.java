package tech.buildrun.rummye2e.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import tech.buildrun.rummye2e.config.ScenarioContext;

import java.util.List;

public class SiteStepsTest {

    private ScenarioContext scenarioContext;

    public SiteStepsTest(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    @Given("I want to access {string}")
    public void iWantToAccess(String url) {
        scenarioContext.put("url", url);
    }

    @When("i access this website")
    public void iAccessThisWebsite() {
        var url = scenarioContext.get("url", String.class);

        var response = RestAssured.given()
                .filters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter()))
                .baseUri(url)
                .get();

        scenarioContext.put("response", response);

    }

    @Then("the website is correctly loaded")
    public void theWebsiteIsCorrectlyLoaded() {
        var response = scenarioContext.get("response", Response.class);

        response.then().assertThat().statusCode(200);
    }
}
