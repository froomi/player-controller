package com.controller.player.util;

import java.util.Objects;

import static com.controller.player.properties.PlayerControllerProperties.FEMALE;
import static com.controller.player.properties.PlayerControllerProperties.MALE;

public class GenderUtil {

    public static String changeGender(String oldGender) {
        return Objects.equals(oldGender, MALE) ? FEMALE : MALE;
    }
}
