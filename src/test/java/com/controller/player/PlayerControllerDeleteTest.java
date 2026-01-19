package com.controller.player;

import com.controller.player.base.BasePlayerControllerTest;
import com.controller.player.domain.dto.PlayerIdRequestDTO;
import com.controller.player.helper.PlayerHelperContext;
import io.qameta.allure.Issue;
import org.testng.annotations.Test;

import static com.controller.player.properties.PlayerControllerProperties.SUPERVISOR_LOGIN;

public class PlayerControllerDeleteTest extends BasePlayerControllerTest {

    @Test(description = "Delete player by ID successfully", groups = "PlayerCreatedAsUser")
    public void testDeletePlayerById() {
        PlayerHelperContext context = playerHelperContext.get();

        playerBo.deletePlayerById(context.getCreatedPlayerIdRequest(), SUPERVISOR_LOGIN)
                .then().statusCode(204)
                .log().all();

        playerBo.deletePlayerById(context.getCreatedPlayerIdRequest(), SUPERVISOR_LOGIN)
                .then().statusCode(403)
                .log().all();
    }

    @Test(description = "Admin deletes own account successfully", groups = "PlayerCreatedAsAdmin")
    public void testAdminDeletesOwnAccount() {
        PlayerHelperContext context = playerHelperContext.get();

        playerBo.deletePlayerById(context.getCreatedPlayerIdRequest(), context.getCreatedPlayerRequest().getLogin())
                .then().statusCode(204)
                .log().all();

        playerBo.deletePlayerById(context.getCreatedPlayerIdRequest(), context.getCreatedPlayerRequest().getLogin())
                .then().statusCode(403)
                .log().all();
    }

    @Test(description = "Supervisor deletes admin's account successfully", groups = "PlayerCreatedAsAdmin")
    public void testSupervisorDeletesAdminAccount() {
        PlayerHelperContext context = playerHelperContext.get();

        playerBo.deletePlayerById(context.getCreatedPlayerIdRequest(), SUPERVISOR_LOGIN)
                .then().statusCode(204)
                .log().all();

        playerBo.deletePlayerById(context.getCreatedPlayerIdRequest(), SUPERVISOR_LOGIN)
                .then().statusCode(403)
                .log().all();
    }

    @Test(description = "Supervisor account deletion is forbidden")
    public void testSupervisorAccountDeletionForbidden() {
        Long supervisorId = 1L;
        PlayerIdRequestDTO playerIdRequest = new PlayerIdRequestDTO();
        playerIdRequest.setPlayerId(supervisorId);

        playerBo.deletePlayerById(playerIdRequest, SUPERVISOR_LOGIN)
                .then().statusCode(403)
                .log().all();
    }

    @Issue("BUG-10") //Regular user can delete other user's account
    @Test(description = "Regular user cannot delete other user's account", groups = "PlayerCreatedAsUser")
    public void testUserCannotDeleteOtherUserAccount() {
        PlayerHelperContext context = playerHelperContext.get();

        playerBo.deletePlayerById(context.getCreatedPlayerIdRequest(), context.getCreatedPlayerRequest().getLogin())
                .then().statusCode(403)
                .log().all();
    }

    @Test(description = "Cannot delete user with empty playerId parameter")
    public void testDeleteUserWithEmptyId() {
        PlayerIdRequestDTO playerIdRequest = new PlayerIdRequestDTO();

        playerBo.deletePlayerById(playerIdRequest, SUPERVISOR_LOGIN)
                .then().statusCode(403)
                .log().all();
    }

    @Issue("BUG-11") //Regular user can delete itself
    @Test(description = "Regular user cannot delete itself", groups = "PlayerCreatedAsUser")
    public void testUserCannotDeleteItself() {
        PlayerHelperContext context = playerHelperContext.get();

        playerBo.deletePlayerById(context.getCreatedPlayerIdRequest(), context.getCreatedPlayerRequest().getLogin())
                .then().statusCode(403)
                .log().all();
    }
}
