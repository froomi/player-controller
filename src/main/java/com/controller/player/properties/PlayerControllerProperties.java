package com.controller.player.properties;

import com.controller.player.config.Config;

public class PlayerControllerProperties {
    public static final String BASE_URL = Config.getInstance().getBaseUrl();
    public static final int MIN_VALID_AGE = 17;
    public static final int MAX_VALID_AGE = 59;
    public static final int MIN_PASSWORD_LENGTH = 7;
    public static final int MAX_PASSWORD_LENGTH = 15;
    public static final String MALE = "male";
    public static final String FEMALE = "female";
    public static final String SUPERVISOR_LOGIN = "supervisor";
    public static final String ADMIN_LOGIN = "admin";
    public static final String USER_LOGIN = "user";

}
