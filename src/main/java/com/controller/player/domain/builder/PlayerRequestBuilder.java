package com.controller.player.domain.builder;

import com.controller.player.domain.dto.PlayerRequestDTO;

public class PlayerRequestBuilder {

    private PlayerRequestDTO playerCreateRequest;

    public PlayerRequestBuilder() {
        this.playerCreateRequest = new PlayerRequestDTO();
    }

    public PlayerRequestBuilder withLogin(String login) {
        this.playerCreateRequest.setLogin(login);
        return this;
    }

    public PlayerRequestBuilder withPassword(String password) {
        this.playerCreateRequest.setPassword(password);
        return this;
    }

    public PlayerRequestBuilder withScreenName(String screenName) {
        this.playerCreateRequest.setScreenName(screenName);
        return this;
    }

    public PlayerRequestBuilder withAge(Integer age) {
        this.playerCreateRequest.setAge(age);
        return this;
    }

    public PlayerRequestBuilder withGender(String gender) {
        this.playerCreateRequest.setGender(gender);
        return this;
    }

    public PlayerRequestBuilder withRole(String role) {
        this.playerCreateRequest.setRole(role);
        return this;
    }

    public PlayerRequestDTO build() {
        return playerCreateRequest;
    }

}
