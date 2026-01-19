package com.controller.player.enums;

public enum PlayerRoleEnum {
    SUPERVISOR("supervisor"),
    USER("user"),
    ADMIN("admin");
    private final String role;

    PlayerRoleEnum(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
