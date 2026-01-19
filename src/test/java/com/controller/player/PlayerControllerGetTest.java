package com.controller.player;

import com.controller.player.assertion.PlayerAssertion;
import com.controller.player.base.BasePlayerControllerTest;
import com.controller.player.domain.builder.PlayerRequestBuilder;
import com.controller.player.domain.dto.PlayerIdRequestDTO;
import com.controller.player.domain.dto.PlayerRequestDTO;
import com.controller.player.domain.dto.PlayerResponseDTO;
import com.controller.player.enums.PlayerRoleEnum;
import com.controller.player.util.RandomUtil;
import io.qameta.allure.Issue;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.controller.player.properties.PlayerControllerProperties.*;

public class PlayerControllerGetTest extends BasePlayerControllerTest {

    @Test(description = "Get player by ID returns correct data", groups = {"PlayerCreatedAsUser", "CleanUpAfterCreation"})
    public void testGetPlayerById() {
        SoftAssert softAssert = new SoftAssert();

        PlayerResponseDTO getPlayerResponse = playerBo.getPlayerById(createdPlayerIdRequest.get())
                .then().statusCode(200)
                .log().all()
                .extract().as(PlayerResponseDTO.class);

        PlayerAssertion.assertRequiredFieldsPresentInPlayerResponse(softAssert, createdPlayerRequest.get(), getPlayerResponse);

        softAssert.assertAll();
    }

    @Test(description = "Get user after recent update reflects latest data", groups = {"PlayerCreatedAsUser", "CleanUpAfterCreation"})
    public void testGetSupervisorById() {
        SoftAssert softAssert = new SoftAssert();

        String updatedScreenName = RandomUtil.randomScreenName();
        String updatedLogin = RandomUtil.randomLogin(PlayerRoleEnum.USER.getRole());
        int updatedAge = RandomUtil.randomAge(MIN_VALID_AGE, MAX_VALID_AGE);

        PlayerRequestDTO updateScreenNameRequest = new PlayerRequestBuilder()
                .withScreenName(updatedScreenName)
                .withLogin(updatedLogin)
                .withAge(updatedAge)
                .build();

        playerBo.updatePlayerById(createdPlayerIdRequest.get().getPlayerId(), updateScreenNameRequest, SUPERVISOR_LOGIN)
                .then().statusCode(200)
                .log().all();

        PlayerResponseDTO getPlayerResponse = playerBo.getPlayerById(createdPlayerIdRequest.get())
                .then().statusCode(200)
                .log().all()
                .extract().as(PlayerResponseDTO.class);

        PlayerAssertion.assertPlayerResponseNotNull(getPlayerResponse);
        PlayerAssertion.assertScreenNameMatchesExpectedValue(softAssert, getPlayerResponse.getScreenName(), updatedScreenName);
        PlayerAssertion.assertLoginMatchesExpectedValue(softAssert, getPlayerResponse.getLogin(), updatedLogin);
        PlayerAssertion.assertAgeMatchesExpectedValue(softAssert, getPlayerResponse.getAge(), updatedAge);

        softAssert.assertAll();
    }

    @Test(description = "Get supervisor by ID returns correct data")
    public void testGetSupervisorByIdReturnsCorrectData() {
        SoftAssert softAssert = new SoftAssert();

        Long supervisorId = 1L;

        PlayerIdRequestDTO playerIdRequest = new PlayerIdRequestDTO();
        playerIdRequest.setPlayerId(supervisorId);

        PlayerResponseDTO getPlayerResponse = playerBo.getPlayerById(playerIdRequest)
                .then().statusCode(200)
                .log().all()
                .extract().as(PlayerResponseDTO.class);

        PlayerAssertion.assertPlayerResponseNotNull(getPlayerResponse);
        PlayerAssertion.assertRoleMatchesExpectedValue(softAssert, getPlayerResponse.getRole(), PlayerRoleEnum.SUPERVISOR.getRole());

        softAssert.assertAll();
    }

    @Issue("BUG-8") //Getting player by non-existing ID returns 200 instead of 404
    @Test(description = "Get player by non-existing ID returns 404")
    public void testGetPlayerByNonExistingId() {
        PlayerIdRequestDTO playerIdRequest = new PlayerIdRequestDTO();
        playerIdRequest.setPlayerId(Long.MAX_VALUE);

        playerBo.getPlayerById(playerIdRequest)
                .then().statusCode(404)
                .log().all();
    }

    @Issue("BUG-9") //Getting player without specifying ID returns 200 instead of 400
    @Test(description = "Get player without specifying ID returns 400")
    public void testGetPlayerWithoutId() {
        PlayerIdRequestDTO playerIdRequest = new PlayerIdRequestDTO();

        playerBo.getPlayerById(playerIdRequest)
                .then().statusCode(400)
                .log().all();
    }

}
