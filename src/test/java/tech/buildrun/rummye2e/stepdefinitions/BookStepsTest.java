package tech.buildrun.rummye2e.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import tech.buildrun.rummye2e.config.RestConfig;
import tech.buildrun.rummye2e.config.ScenarioContext;
import tech.buildrun.rummye2e.dto.BookRequestDto;

import java.time.LocalDateTime;

public class BookStepsTest {

    private final ScenarioContext scenarioContext;
    private final RestConfig restConfig;

    public BookStepsTest(ScenarioContext scenarioContext,
                         RestConfig restConfig) {
        this.scenarioContext = scenarioContext;
        this.restConfig = restConfig;
    }

    @And("the room has no bookings for today")
    public void theRoomHasNoBookingsForToday() {
        //1 - Faz chamada para o endpoint 'especial' de testes que deleta todos os agendamentos de uma sala pelo id
        var roomId = scenarioContext.get("room_id", Long.class);

        restConfig.givenBackend()
                .queryParam("room_id", roomId)
                .delete("/test-utils/bookings")
                .then()
                .statusCode(204);
    }

    @When("i book the room for one hour from now")
    public void iBookTheRoomForOneHourFromNow() {
        //1 - Enviar requisição para o endpoint de booking,com o BookRequestDto
        //1.1 - Recuperar o id da room que ja foi verificada que existe
        var roomId = scenarioContext.get("room_id", Long.class);
        var startTime = LocalDateTime.now().plusHours(1);
        var endTime = startTime.plusHours(1);

        var bookRequestDto = new BookRequestDto(roomId, startTime, endTime);

        var response = restConfig.givenBackend()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(bookRequestDto)
                .post("/bookings");

        scenarioContext.put("response", response);
    }

    @Then("the room should be sucessfuly booked")
    public void theRoomShouldBeSucessfulyBooked() {
        var response = scenarioContext.get("response", Response.class);

        response.then().statusCode(200);
    }
}
