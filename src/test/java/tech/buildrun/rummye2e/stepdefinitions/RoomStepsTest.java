package tech.buildrun.rummye2e.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import tech.buildrun.rummye2e.config.RestConfig;
import tech.buildrun.rummye2e.config.ScenarioContext;
import tech.buildrun.rummye2e.dto.RoomDto;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RoomStepsTest {

    private ScenarioContext scenarioContext;
    private final RestConfig restConfig;

    public RoomStepsTest(ScenarioContext scenarioContext,
                         RestConfig restConfig) {
        this.scenarioContext = scenarioContext;
        this.restConfig = restConfig;
    }


    @When("i list all the rooms")
    public void iListAllTheRooms() {
        Response response = restConfig.givenBackend()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/rooms");

        scenarioContext.put("response", response);
    }

    @Then("i detect that the room {string} exists")
    public void iDetectThatTheRoomExists(String name) {
        var response = scenarioContext.get("response", Response.class);

        var rooms = response.then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<List<RoomDto>>() {
                });

        assertTrue(rooms.stream().anyMatch(room -> room.name().equalsIgnoreCase(name)));
    }

    @Given("the room {string} exists")
    public void theRoomExists(String roomName) {
        //1 - Get no endpoint de rooms
        var rooms = restConfig.givenBackend()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/rooms")
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<List<RoomDto>>() {});

        //2 - Verifica se existe filtrando pelo nome
        Optional<RoomDto> roomOpt = rooms.stream()
                .filter(room -> room.name().equalsIgnoreCase(roomName))
                .findFirst();

        //3 - Assegura que existe
        assertTrue(roomOpt.isPresent());

        //4 - Adiciona o roomId ao contexto para compartilhar com outros scenarios
        scenarioContext.put("room_id", roomOpt.get().id());
    }
}