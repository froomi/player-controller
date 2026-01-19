package com.controller.player.assertion;

import com.controller.player.domain.dto.PlayerRequestDTO;
import com.controller.player.domain.dto.PlayerResponseDTO;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public class PlayerAssertion {

    @Step("Assert Player response isn't null")
    public static void assertPlayerResponseNotNull(PlayerResponseDTO playerResponse) {
        Assert.assertNotNull(playerResponse, "Player response is null");
    }

    @Step("Assert screenName matches expected value")
    public static void assertScreenNameMatchesExpectedValue(SoftAssert softAssert, String actualScreenName, String expectedScreenName) {
        softAssert.assertEquals(actualScreenName, expectedScreenName, "User screenName does not match the create request screenName value");
    }

    @Step("Assert password matches expected value")
    public static void assertLoginMatchesExpectedValue(SoftAssert softAssert, String actualLogin, String expectedLogin) {
        softAssert.assertEquals(actualLogin, expectedLogin, "User login does not match the create request login value");
    }

    @Step("Assert gender matches expected value")
    public static void assertGenderMatchesExpectedValue(SoftAssert softAssert, String actualGender, String expectedGender) {
        softAssert.assertEquals(actualGender, expectedGender, "User gender does not match the create request gender value");
    }

    @Step("Assert age matches expected value")
    public static void assertAgeMatchesExpectedValue(SoftAssert softAssert, Integer actualAge, Integer expectedAge) {
        softAssert.assertEquals(actualAge, expectedAge, "User age does not match the create request age value");
    }

    @Step("Assert role matches expected value")
    public static void assertRoleMatchesExpectedValue(SoftAssert softAssert, String actualRole, String expectedRole) {
        softAssert.assertEquals(actualRole, expectedRole, "User role does not match the create request role value");
    }

    @Step("Assert id is not null")
    public static void assertIdIsNotNull(SoftAssert softAssert, Long actualId) {
        softAssert.assertNotNull(actualId, "User id is null in the response");
    }

    @Step("Assert that all required fields are present for player response body and match the create request")
    public static void assertRequiredFieldsPresentInPlayerResponse(SoftAssert softAssert, PlayerRequestDTO playerCreateRequest,
                                                                   PlayerResponseDTO playerResponse) {
        assertPlayerResponseNotNull(playerResponse);
        assertIdIsNotNull(softAssert, playerResponse.getId());
        assertScreenNameMatchesExpectedValue(softAssert, playerResponse.getScreenName(), playerCreateRequest.getScreenName());
        assertLoginMatchesExpectedValue(softAssert, playerResponse.getLogin(), playerCreateRequest.getLogin());
        assertGenderMatchesExpectedValue(softAssert, playerResponse.getGender(), playerCreateRequest.getGender());
        assertAgeMatchesExpectedValue(softAssert, playerResponse.getAge(), playerCreateRequest.getAge());
        assertRoleMatchesExpectedValue(softAssert, playerResponse.getRole(), playerCreateRequest.getRole());
    }

}
