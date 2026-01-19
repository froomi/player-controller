package com.controller.player.domain.dto;

public class PlayerResponseDTO {
    private Integer age;
    private String gender;
    private Long id;
    private String login;
    private String password;
    private String screenName;
    private String role;

    public Integer getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getLogin() {
        return login;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getRole() {
        return role;
    }
}
