package com.controller.player.helper;

import com.controller.player.domain.dto.PlayerIdRequestDTO;
import com.controller.player.domain.dto.PlayerRequestDTO;
import com.controller.player.domain.dto.PlayerResponseDTO;

import java.util.ArrayList;
import java.util.List;

public class PlayerHelperContext {
    private final List<PlayerIdRequestDTO> createdPlayersToDelete = new ArrayList<>();
    private PlayerIdRequestDTO createdPlayerIdRequest;
    private PlayerRequestDTO createdPlayerRequest;

    public List<PlayerIdRequestDTO> getCreatedPlayersToDelete() {
        return createdPlayersToDelete;
    }

    public PlayerIdRequestDTO getCreatedPlayerIdRequest() {
        return createdPlayerIdRequest;
    }

    public void setCreatedPlayerIdRequest(PlayerIdRequestDTO idRequest) {
        this.createdPlayerIdRequest = idRequest;
    }

    public PlayerRequestDTO getCreatedPlayerRequest() {
        return createdPlayerRequest;
    }

    public void setCreatedPlayerRequest(PlayerRequestDTO request) {
        this.createdPlayerRequest = request;
    }

    public void setCreatedPlayerIdRequest(PlayerResponseDTO playerSerializedResponse) {
        PlayerIdRequestDTO playerIdRequest = new PlayerIdRequestDTO();
        playerIdRequest.setPlayerId(playerSerializedResponse.getId());
        setCreatedPlayerIdRequest(playerIdRequest);
    }

    public void addCreatedPlayerToDelete(PlayerResponseDTO playerSerializedResponse) {
        PlayerIdRequestDTO playerIdRequest = new PlayerIdRequestDTO();
        playerIdRequest.setPlayerId(playerSerializedResponse.getId());
        createdPlayersToDelete.add(playerIdRequest);
    }

}
