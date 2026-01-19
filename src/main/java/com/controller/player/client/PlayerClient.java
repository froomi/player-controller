package com.controller.player.client;

import com.controller.player.domain.dto.PlayerIdRequestDTO;
import com.controller.player.domain.dto.PlayerRequestDTO;
import com.controller.player.util.RequestSpecificationUtil;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class PlayerClient {

    @Step("Create player with editor: {editor}")
    public Response createPlayer(Map<String, Object> queryParams, String editor) {
        return given()
                .spec(RequestSpecificationUtil.getDefaultSpecification())
                .pathParam("editor", editor)
                .queryParams(queryParams)
                .get("/player/create/{editor}");
    }

    @Step("Get player by id: {id}")
    public Response getPlayerById(PlayerIdRequestDTO playerIdRequest) {
        return given()
                .spec(RequestSpecificationUtil.getDefaultSpecification())
                .body(playerIdRequest)
                .post("/player/get");
    }

    @Step("Delete player by id: {id}")
    public Response deletePlayerById(PlayerIdRequestDTO playerIdRequest, String editor) {
        return given()
                .spec(RequestSpecificationUtil.getDefaultSpecification())
                .pathParam("editor", editor)
                .body(playerIdRequest)
                .delete("/player/delete/{editor}");
    }

    @Step("Update player with editor: {editor}")
    public Response updatePlayer(Long id, PlayerRequestDTO playerRequest, String editor) {
        return given()
                .spec(RequestSpecificationUtil.getDefaultSpecification())
                .pathParam("editor", editor)
                .pathParam("id", id)
                .body(playerRequest)
                .patch("/player/update/{editor}/{id}");
    }
}
