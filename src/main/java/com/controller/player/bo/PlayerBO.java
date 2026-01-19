package com.controller.player.bo;

import com.controller.player.client.PlayerClient;
import com.controller.player.domain.dto.PlayerIdRequestDTO;
import com.controller.player.domain.dto.PlayerRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class PlayerBO {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final PlayerClient playerClient = new PlayerClient();
    private static final Logger log = LoggerFactory.getLogger(PlayerBO.class);

    @Step("Create valid player")
    public Response createPlayer(PlayerRequestDTO playerCreateRequest, String editor) {
        Map<String, Object> queryParams = MAPPER.convertValue(playerCreateRequest, Map.class);
        Response createPlayerResponse = playerClient.createPlayer(queryParams, editor);
        log.info("Created player with screenName: {}", playerCreateRequest.getScreenName());
        return createPlayerResponse;
    }

    @Step("Get player by id")
    public Response getPlayerById(PlayerIdRequestDTO playerIdRequest) {
        Response getPlayerByIdResponse = playerClient.getPlayerById(playerIdRequest);
        log.info("Retrieved player with id: {}", playerIdRequest.getPlayerId());
        return getPlayerByIdResponse;
    }

    @Step("Delete player by id")
    public Response deletePlayerById(PlayerIdRequestDTO playerIdRequest, String editor) {
        Response deletePlayerResponse = playerClient.deletePlayerById(playerIdRequest, editor);
        log.info("Deleted player with id: {}", playerIdRequest.getPlayerId());
        return deletePlayerResponse;
    }

    @Step("Update player by id")
    public Response updatePlayerById(Long id, PlayerRequestDTO playerRequest, String editor) {
        Response updatePlayerResponse = playerClient.updatePlayer(id, playerRequest, editor);
        log.info("Updated player with id: {}", id);
        return updatePlayerResponse;
    }

}
