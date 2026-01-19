package com.controller.player.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.concurrent.ThreadLocalRandom;

import static com.controller.player.properties.PlayerControllerProperties.*;

public class RandomUtil {

    public static String randomLogin(String role) {
        return role + RandomStringUtils.randomAlphanumeric(4, 8);
    }

    public static String randomPassword(int minPasswordLength, int maxPasswordLength) {
        return "pass" + RandomStringUtils.randomAlphanumeric(minPasswordLength, maxPasswordLength);
    }

    public static int randomAge(int minAge, int maxAge) {
        return ThreadLocalRandom.current().nextInt(minAge, maxAge + 1);
    }

    public static String randomScreenName() {
        return "Player_" + RandomStringUtils.randomAlphanumeric(5, 15);
    }

    public static String randomGender() {
        return ThreadLocalRandom.current().nextBoolean() ? MALE : FEMALE;
    }
}
