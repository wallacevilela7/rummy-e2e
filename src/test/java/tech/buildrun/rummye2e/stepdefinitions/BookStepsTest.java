package tech.buildrun.rummye2e.stepdefinitions;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import tech.buildrun.rummye2e.config.RestConfig;
import tech.buildrun.rummye2e.config.ScenarioContext;
import tech.buildrun.rummye2e.dto.BookRequestDto;
import tech.buildrun.rummye2e.dto.BookResponseDto;
import tech.buildrun.rummye2e.dto.RoomDto;

import java.time.LocalDateTime;
import java.util.List;

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

    @And("one user book the room for one hour from now")
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

    @Then("the room should be successfully booked")
    public void theRoomShouldBeSuccessfullyBooked() {
        var response = scenarioContext.get("response", Response.class);

        response.then().statusCode(HttpStatus.OK.value());
    }

    @Then("the booking should conflict")
    public void theBookingShouldConflict() {
        var response = scenarioContext.get("response", Response.class);

        response.then().statusCode(HttpStatus.CONFLICT.value());
    }

    @And("the room has at least one booking for today")
    public void theRoomHasAtLeastOneBookingForToday() {
        var roomId = scenarioContext.get("room_id", Long.class);
        var startTime = LocalDateTime.now().plusHours(1);
        var endTime = startTime.plusHours(1);

        var bookRequestDto = new BookRequestDto(roomId, startTime, endTime);

        //2 - criando o agendamento e extraindo o id para aplicar no contexto
        var response = restConfig.givenBackend()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(bookRequestDto)
                .post("/bookings")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(new TypeRef<BookResponseDto>() {});

        var bookingId = response.id();

        scenarioContext.put("booking_id", bookingId);
    }

    @When("i try to delete the booking")
    public void iTryToDeleteTheBooking() {
        var bookingId = scenarioContext.get("booking_id", Long.class);

        var response = restConfig.givenBackend()
                .delete("/bookings/{id}", bookingId).then()
                .extract()
                .response();

        scenarioContext.put("response", response);
    }

    @Then("the booking should be successfully deleted")
    public void theBookingShouldBeSuccessfullyDeleted() {
        var response = scenarioContext.get("response", Response.class);

        response.then().statusCode(HttpStatus.OK.value());
    }

    @And("the room has at least one booking for today that is already finished")
    public void theRoomHasAtLeastOneBookingForTodayThatIsAlreadyFinished() throws InterruptedException {
        var roomId = scenarioContext.get("room_id", Long.class);
        var startTime = LocalDateTime.now().plusSeconds(2);
        var endTime = startTime.plusSeconds(3);

        var bookRequestDto = new BookRequestDto(roomId, startTime, endTime);

        var response = restConfig.givenBackend()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(bookRequestDto)
                .post("/bookings")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(new TypeRef<BookResponseDto>() {});

        Thread.sleep(5000);

        var bookingId = response.id();

        scenarioContext.put("booking_id", bookingId);
        scenarioContext.put("response", response);
    }

    @Then("the booking deletion should not be processed")
    public void theBookingDeletionShouldNotBeProcessed() {
        var response = scenarioContext.get("response", Response.class);

        response.then().statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @And("the room has at least one booking for today that is already in progress")
    public void theRoomHasAtLeastOneBookingForTodayThatIsAlreadyInProgress() throws InterruptedException {
        var roomId = scenarioContext.get("room_id", Long.class);
        var startTime = LocalDateTime.now().plusSeconds(5);
        var endTime = startTime.plusHours(1);

        var bookRequestDto = new BookRequestDto(roomId, startTime, endTime);

        var response = restConfig.givenBackend()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(bookRequestDto)
                .post("/bookings")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(new TypeRef<BookResponseDto>() {});

        Thread.sleep(5000);

        var bookingId = response.id();

        scenarioContext.put("booking_id", bookingId);
        scenarioContext.put("response", response);
    }
}
