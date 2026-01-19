package com.controller.player.base;

import com.controller.player.bo.PlayerBO;
import com.controller.player.data.PowerUserBoundaryValuesData;
import com.controller.player.domain.builder.PlayerRequestBuilder;
import com.controller.player.domain.dto.PlayerIdRequestDTO;
import com.controller.player.domain.dto.PlayerRequestDTO;
import com.controller.player.domain.dto.PlayerResponseDTO;
import com.controller.player.enums.PlayerRoleEnum;
import com.controller.player.helper.PlayerHelperContext;
import com.controller.player.util.RandomUtil;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import static com.controller.player.properties.PlayerControllerProperties.*;
import static com.controller.player.properties.PlayerControllerProperties.MAX_PASSWORD_LENGTH;

public class BasePlayerControllerTest {
    protected ThreadLocal<PlayerHelperContext> playerHelperContext = ThreadLocal.withInitial(PlayerHelperContext::new);
    protected PlayerBO playerBo = new PlayerBO();

    @BeforeSuite(description = "Create admin player before running tests")
    public void createAdminPlayer() {
        PlayerRequestDTO playerCreateRequest = new PlayerRequestBuilder()
                .withAge(RandomUtil.randomAge(MIN_VALID_AGE, MAX_VALID_AGE))
                .withGender(RandomUtil.randomGender())
                .withLogin(ADMIN_LOGIN)
                .withPassword(RandomUtil.randomPassword(MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH))
                .withRole(PlayerRoleEnum.ADMIN.getRole())
                .withScreenName(RandomUtil.randomScreenName())
                .build();

        playerBo.createPlayer(playerCreateRequest, SUPERVISOR_LOGIN);
    }

    @BeforeMethod(onlyForGroups = "PlayerCreatedAsUser")
    public void createUserTestPlayer() {
        createTestPlayerAsSupervisor(PlayerRoleEnum.USER.getRole());
    }

    @BeforeMethod(onlyForGroups = "PlayerCreatedAsAdmin")
    public void createAdminTestPlayer() {
        createTestPlayerAsSupervisor(PlayerRoleEnum.ADMIN.getRole());
    }

    @AfterMethod(alwaysRun = true, onlyForGroups = "CleanUpAfterCreation")
    public void deletePlayerAfterCreation() {
        PlayerHelperContext context = playerHelperContext.get();
        for (PlayerIdRequestDTO id : context.getCreatedPlayersToDelete()) {
            playerBo.deletePlayerById(id, SUPERVISOR_LOGIN);
        }
        context.getCreatedPlayersToDelete().clear();
        playerHelperContext.remove();
    }

    @DataProvider
    protected Object[][] powerUserBoundaryValues() {
        return PowerUserBoundaryValuesData.getAllBoundaryValuesData();
    }

    @DataProvider
    protected Object[][] supervisorCreatesUserWithAgeNotInRange() {
        return PowerUserBoundaryValuesData.getInvalidAgeBoundaryValuesData();
    }

    protected void createTestPlayerAsSupervisor(String role) {
        PlayerRequestDTO playerCreateRequest = new PlayerRequestBuilder()
                .withAge(RandomUtil.randomAge(MIN_VALID_AGE, MAX_VALID_AGE))
                .withGender(RandomUtil.randomGender())
                .withLogin(RandomUtil.randomLogin(role))
                .withPassword(RandomUtil.randomPassword(MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH))
                .withRole(role)
                .withScreenName(RandomUtil.randomScreenName())
                .build();

        PlayerResponseDTO playerSerializedResponse = playerBo.createPlayer(playerCreateRequest,
                SUPERVISOR_LOGIN).as(PlayerResponseDTO.class);

        PlayerHelperContext context = playerHelperContext.get();
        context.setCreatedPlayerRequest(playerCreateRequest);

        context.setCreatedPlayerIdRequest(playerSerializedResponse);
        context.getCreatedPlayersToDelete().add(context.getCreatedPlayerIdRequest());
    }
}
