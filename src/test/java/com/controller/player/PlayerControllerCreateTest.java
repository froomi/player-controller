package com.controller.player;

import com.controller.player.assertion.PlayerAssertion;
import com.controller.player.base.BasePlayerControllerTest;
import com.controller.player.data.PowerUserBoundaryValuesData;
import com.controller.player.domain.builder.PlayerRequestBuilder;
import com.controller.player.domain.dto.PlayerRequestDTO;
import com.controller.player.domain.dto.PlayerResponseDTO;
import com.controller.player.enums.PlayerRoleEnum;
import com.controller.player.util.RandomUtil;
import io.qameta.allure.Issue;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.controller.player.properties.PlayerControllerProperties.*;

public class PlayerControllerCreateTest extends BasePlayerControllerTest {

    @Issue("BUG-1") //All fields (login, screenName, gender, age, role) except for id are null in the response
    @Test(dataProvider = "powerUserBoundaryValues", description = "power user (supervisor/admin) creates valid player with boundary required fields values", groups = "CleanUpAfterCreation")
    public void powerUserCreatesValidPlayerBoundaryValues(PowerUserBoundaryValuesData powerUserBoundaryValues) {
        SoftAssert softAssert = new SoftAssert();

        PlayerRequestDTO playerCreateRequest = new PlayerRequestBuilder()
                .withAge(powerUserBoundaryValues.getAge())
                .withGender(powerUserBoundaryValues.getGender())
                .withLogin(RandomUtil.randomLogin(PlayerRoleEnum.USER.getRole()))
                .withPassword(powerUserBoundaryValues.getPassword())
                .withRole(PlayerRoleEnum.USER.getRole())
                .withScreenName(RandomUtil.randomScreenName())
                .build();

        PlayerResponseDTO playerSerializedResponse = playerBo
                .createPlayer(playerCreateRequest, powerUserBoundaryValues.getLogin())
                .then()
                .statusCode(200)
                .log().all()
                .extract().as(PlayerResponseDTO.class);

        addCreatedPlayerForFutureDeletion(playerSerializedResponse);

        PlayerAssertion.assertRequiredFieldsPresentInPlayerResponse(softAssert, playerCreateRequest, playerSerializedResponse);
        softAssert.assertAll();
    }

    @Issue("BUG-2") //User creation succeeds even when a required field (password) is missing
    @Test(description = "User creation fails when a required field (password) is missing")
    public void supervisorCreatesUserWithoutPassword() {
        PlayerRequestDTO playerCreateRequest = new PlayerRequestBuilder()
                .withAge(RandomUtil.randomAge(MIN_VALID_AGE, MAX_VALID_AGE))
                .withGender(RandomUtil.randomGender())
                .withLogin(RandomUtil.randomLogin(PlayerRoleEnum.USER.getRole()))
                .withRole(PlayerRoleEnum.USER.getRole())
                .withScreenName(RandomUtil.randomScreenName())
                .build();

        playerBo
                .createPlayer(playerCreateRequest, SUPERVISOR_LOGIN)
                .then()
                .statusCode(400)
                .log().all();
    }

    @Test(description = "User creates user and fails due to insufficient permissions")
    public void userCreatesUserAndFailsDueToInsufficientPermissions() {
        PlayerRequestDTO playerCreateRequest = new PlayerRequestBuilder()
                .withAge(RandomUtil.randomAge(MIN_VALID_AGE, MAX_VALID_AGE))
                .withGender(RandomUtil.randomGender())
                .withLogin(RandomUtil.randomLogin(PlayerRoleEnum.USER.getRole()))
                .withPassword(RandomUtil.randomPassword(MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH))
                .withRole(PlayerRoleEnum.USER.getRole())
                .withScreenName(RandomUtil.randomScreenName())
                .build();

        playerBo
                .createPlayer(playerCreateRequest, USER_LOGIN)
                .then()
                .statusCode(403)
                .log().all();
    }

    @Test(description = "Admin creates supervisor with valid field values and fails due to insufficient permissions")
    public void adminCreatesSupervisorAndFailsDueToInsufficientPermissions() {
        PlayerRequestDTO playerCreateRequest = new PlayerRequestBuilder()
                .withAge(RandomUtil.randomAge(MIN_VALID_AGE, MAX_VALID_AGE))
                .withGender(RandomUtil.randomGender())
                .withLogin(RandomUtil.randomLogin(PlayerRoleEnum.SUPERVISOR.getRole()))
                .withPassword(RandomUtil.randomPassword(MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH))
                .withRole(PlayerRoleEnum.SUPERVISOR.getRole())
                .withScreenName(RandomUtil.randomScreenName())
                .build();

        playerBo
                .createPlayer(playerCreateRequest, ADMIN_LOGIN)
                .then()
                .statusCode(400)
                .log().all();
    }

    @Issue("BUG-3") //System allows creation of users with duplicate logins
    @Test(description = "Supervisor creates user with existing login and fails", groups = {"PlayerCreatedAsUser", "CleanUpAfterCreation"})
    public void supervisorCreatesUserWithExistingLoginAndFails() {
        PlayerRequestDTO secondPlayerCreateRequest = new PlayerRequestBuilder()
                .withAge(RandomUtil.randomAge(MIN_VALID_AGE, MAX_VALID_AGE))
                .withGender(RandomUtil.randomGender())
                .withLogin(createdPlayerRequest.get().getLogin())
                .withPassword(RandomUtil.randomPassword(MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH))
                .withRole(PlayerRoleEnum.USER.getRole())
                .withScreenName(RandomUtil.randomScreenName())
                .build();

        playerBo
                .createPlayer(secondPlayerCreateRequest, SUPERVISOR_LOGIN)
                .then()
                .statusCode(400)
                .log().all();
    }

    @Issue("BUG-4") //System allows creation of user with invalid gender
    @Test(description = "Supervisor creates user with invalid gender and fails")
    public void supervisorCreatesUserWithInvalidGenderAndFails() {
        PlayerRequestDTO playerCreateRequest = new PlayerRequestBuilder()
                .withAge(RandomUtil.randomAge(MIN_VALID_AGE, MAX_VALID_AGE))
                .withGender("unknown")
                .withLogin(RandomUtil.randomLogin(PlayerRoleEnum.USER.getRole()))
                .withPassword(RandomUtil.randomPassword(MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH))
                .withRole(PlayerRoleEnum.USER.getRole())
                .withScreenName(RandomUtil.randomScreenName())
                .build();

        playerBo
                .createPlayer(playerCreateRequest, SUPERVISOR_LOGIN)
                .then()
                .statusCode(400)
                .log().all();
    }

    @Issue("BUG-5") //System allows creation of users with age above the maximum valid age value
    @Test(dataProvider = "supervisorCreatesUserWithAgeNotInRange", description = "Admin creates user with ages below minimum and above maximum and fails")
    public void supervisorCreatesUserWithAgeNotInRange(PowerUserBoundaryValuesData powerUserBoundaryValues) {
        PlayerRequestDTO playerCreateRequest = new PlayerRequestBuilder()
                .withAge(powerUserBoundaryValues.getAge())
                .withGender(RandomUtil.randomGender())
                .withLogin(RandomUtil.randomLogin(PlayerRoleEnum.USER.getRole()))
                .withPassword(RandomUtil.randomPassword(MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH))
                .withRole(PlayerRoleEnum.USER.getRole())
                .withScreenName(RandomUtil.randomScreenName())
                .build();

        playerBo
                .createPlayer(playerCreateRequest, SUPERVISOR_LOGIN)
                .then()
                .statusCode(400)
                .log().all();
    }

}
