package com.controller.player.data;

import com.controller.player.util.RandomUtil;

import static com.controller.player.properties.PlayerControllerProperties.*;

public class PowerUserBoundaryValuesData {
    private String login;
    private String password;
    private Integer age;
    private String gender;

    public String getGender() {
        return gender;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Integer getAge() {
        return age;
    }

    private static PowerUserBoundaryValuesData getAdminBoundaryValuesData() {
        PowerUserBoundaryValuesData data = new PowerUserBoundaryValuesData();
        data.login = ADMIN_LOGIN;
        data.password = RandomUtil.randomPassword(MAX_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH);
        data.age = MAX_VALID_AGE;
        data.gender = MALE;
        return data;
    }

    private static PowerUserBoundaryValuesData getSupervisorBoundaryValuesData() {
        PowerUserBoundaryValuesData data = new PowerUserBoundaryValuesData();
        data.login = SUPERVISOR_LOGIN;
        data.password = RandomUtil.randomPassword(MIN_PASSWORD_LENGTH, MIN_PASSWORD_LENGTH);
        data.age = MIN_VALID_AGE;
        data.gender = FEMALE;
        return data;
    }

    private static PowerUserBoundaryValuesData getSupervisorWithAgeBelowMinValue() {
        PowerUserBoundaryValuesData data = new PowerUserBoundaryValuesData();
        data.age = MIN_VALID_AGE - 1;
        return data;
    }

    private static PowerUserBoundaryValuesData getAdminWithAgeAboveMaxValue() {
        PowerUserBoundaryValuesData data = new PowerUserBoundaryValuesData();
        data.age = MAX_VALID_AGE + 1;
        return data;
    }

    public static Object[][] getAllBoundaryValuesData() {
        return new Object[][]{
                {getAdminBoundaryValuesData()},
                {getSupervisorBoundaryValuesData()}
        };
    }

    public static Object[][] getInvalidAgeBoundaryValuesData() {
        return new Object[][]{
                {getSupervisorWithAgeBelowMinValue()},
                {getAdminWithAgeAboveMaxValue()}
        };
    }

    @Override
    public String toString() {
        return "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age + '\'' +
                ", gender=" + gender;
    }
}
