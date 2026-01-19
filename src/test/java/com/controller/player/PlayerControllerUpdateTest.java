package com.controller.player;

import com.controller.player.assertion.PlayerAssertion;
import com.controller.player.base.BasePlayerControllerTest;
import com.controller.player.domain.builder.PlayerRequestBuilder;
import com.controller.player.domain.dto.PlayerRequestDTO;
import com.controller.player.domain.dto.PlayerResponseDTO;
import com.controller.player.enums.PlayerRoleEnum;
import com.controller.player.helper.PlayerHelperContext;
import com.controller.player.util.RandomUtil;
import io.qameta.allure.Issue;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.controller.player.util.GenderUtil.changeGender;
import static com.controller.player.properties.PlayerControllerProperties.*;

public class PlayerControllerUpdateTest extends BasePlayerControllerTest {

    @Test(description = "Supervisor updates existing player's screenName successfully", groups = {"PlayerCreatedAsUser", "CleanUpAfterCreation"})
    public void testUpdatePlayerAsSupervisor() {
        SoftAssert softAssert = new SoftAssert();
        PlayerHelperContext context = playerHelperContext.get();

        String updatedScreenName = RandomUtil.randomScreenName();

        PlayerRequestDTO updateScreenNameRequest = new PlayerRequestBuilder()
                .withScreenName(updatedScreenName)
                .build();

        PlayerResponseDTO playerUpdateResponse = playerBo.updatePlayerById(context.getCreatedPlayerIdRequest().getPlayerId(), updateScreenNameRequest, SUPERVISOR_LOGIN)
                .then().statusCode(200)
                .log().all()
                .extract().as(PlayerResponseDTO.class);

        PlayerAssertion.assertPlayerResponseNotNull(playerUpdateResponse);
        PlayerAssertion.assertScreenNameMatchesExpectedValue(softAssert, playerUpdateResponse.getScreenName(), updatedScreenName);
        softAssert.assertAll();
    }

    @Test(description = "Admin updates own login successfully", groups = {"PlayerCreatedAsAdmin", "CleanUpAfterCreation"})
    public void testUpdateOwnPasswordAsAdmin() {
        SoftAssert softAssert = new SoftAssert();
        PlayerHelperContext context = playerHelperContext.get();

        String updatedLogin = RandomUtil.randomLogin(PlayerRoleEnum.ADMIN.getRole());

        PlayerRequestDTO updatePasswordRequest = new PlayerRequestBuilder()
                .withLogin(updatedLogin)
                .build();

        PlayerResponseDTO playerUpdateResponse = playerBo.updatePlayerById(context.getCreatedPlayerIdRequest().getPlayerId(), updatePasswordRequest, SUPERVISOR_LOGIN)
                .then().statusCode(200)
                .log().all()
                .extract().as(PlayerResponseDTO.class);

        PlayerAssertion.assertPlayerResponseNotNull(playerUpdateResponse);
        PlayerAssertion.assertLoginMatchesExpectedValue(softAssert, playerUpdateResponse.getLogin(), updatedLogin);
        softAssert.assertAll();
    }

    @Test(description = "User updates own gender successfully", groups = {"PlayerCreatedAsUser", "CleanUpAfterCreation"})
    public void testUpdateOwnGenderAsUser() {
        SoftAssert softAssert = new SoftAssert();
        PlayerHelperContext context = playerHelperContext.get();

        Long userId = context.getCreatedPlayerIdRequest().getPlayerId();
        String oldGender = context.getCreatedPlayerRequest().getGender();
        String newGender = changeGender(oldGender);

        PlayerRequestDTO updateGenderRequest = new PlayerRequestBuilder()
                .withGender(newGender)
                .build();

        PlayerResponseDTO playerUpdateResponse = playerBo.updatePlayerById(userId, updateGenderRequest, context.getCreatedPlayerRequest().getLogin())
                .then().statusCode(200)
                .log().all()
                .extract().as(PlayerResponseDTO.class);

        PlayerAssertion.assertPlayerResponseNotNull(playerUpdateResponse);
        PlayerAssertion.assertGenderMatchesExpectedValue(softAssert, playerUpdateResponse.getGender(), newGender);
        softAssert.assertAll();
    }

    @Issue("BUG-6") // Supervisor can update user's role, but should not be able to
    @Test(description = "Supervisor attempt to update user's role is rejected (Role field cannot be updated)", groups = {"PlayerCreatedAsUser", "CleanUpAfterCreation"})
    public void testUpdateUserRoleAsSupervisorAndFails() {
        String updatedRole = PlayerRoleEnum.ADMIN.getRole();
        PlayerHelperContext context = playerHelperContext.get();

        PlayerRequestDTO updateRequest = new PlayerRequestBuilder()
                .withRole(updatedRole)
                .build();

        playerBo.updatePlayerById(context.getCreatedPlayerIdRequest().getPlayerId(), updateRequest, SUPERVISOR_LOGIN)
                .then().statusCode(400)
                .log().all();
    }

    @Issue("BUG-7") // User can update another user's data, but should not be able to
    @Test(description = "User cannot update another user's data", groups = {"PlayerCreatedAsUser", "CleanUpAfterCreation"})
    public void testUpdateAnotherUserAsUserAndFails() {
        PlayerHelperContext context = playerHelperContext.get();

        PlayerRequestDTO secondUserRequest = new PlayerRequestBuilder()
                .withAge(RandomUtil.randomAge(MIN_VALID_AGE, MAX_VALID_AGE))
                .withGender(RandomUtil.randomGender())
                .withLogin(RandomUtil.randomLogin(PlayerRoleEnum.USER.getRole()))
                .withPassword(RandomUtil.randomPassword(MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH))
                .withRole(PlayerRoleEnum.USER.getRole())
                .withScreenName(RandomUtil.randomScreenName())
                .build();

        PlayerResponseDTO secondUserResponse = playerBo.createPlayer(secondUserRequest, SUPERVISOR_LOGIN)
                .then().statusCode(200).log().all().extract().as(PlayerResponseDTO.class);

        context.addCreatedPlayerToDelete(secondUserResponse);

        String updatedScreenName = RandomUtil.randomScreenName();
        PlayerRequestDTO updateRequest = new PlayerRequestBuilder()
                .withScreenName(updatedScreenName)
                .build();

        playerBo.updatePlayerById(context.getCreatedPlayerIdRequest().getPlayerId(), updateRequest, secondUserRequest.getLogin())
                .then().statusCode(403)
                .log().all();
    }

    @Test(description = "Supervisor updates existing player's login with the login of other existing player and fails", groups = {"PlayerCreatedAsUser", "CleanUpAfterCreation"})
    public void testUpdatePlayerLoginToExistingLoginAsSupervisorAndFails() {
        PlayerHelperContext context = playerHelperContext.get();

        PlayerRequestDTO secondUserRequest = new PlayerRequestBuilder()
                .withAge(RandomUtil.randomAge(MIN_VALID_AGE, MAX_VALID_AGE))
                .withGender(RandomUtil.randomGender())
                .withLogin(RandomUtil.randomLogin(PlayerRoleEnum.USER.getRole()))
                .withPassword(RandomUtil.randomPassword(MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH))
                .withRole(PlayerRoleEnum.USER.getRole())
                .withScreenName(RandomUtil.randomScreenName())
                .build();

        PlayerResponseDTO secondUserResponse = playerBo.createPlayer(secondUserRequest, SUPERVISOR_LOGIN)
                .then().statusCode(200).log().all().extract().as(PlayerResponseDTO.class);

        context.addCreatedPlayerToDelete(secondUserResponse);

        PlayerRequestDTO updateLoginRequest = new PlayerRequestBuilder()
                .withLogin(secondUserRequest.getLogin())
                .build();

        playerBo.updatePlayerById(context.getCreatedPlayerIdRequest().getPlayerId(), updateLoginRequest, SUPERVISOR_LOGIN)
                .then().statusCode(409)
                .log().all();
    }

}
