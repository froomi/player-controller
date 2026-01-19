package com.controller.player.base;

import com.controller.player.bo.PlayerBO;
import com.controller.player.data.PowerUserBoundaryValuesData;
import com.controller.player.domain.builder.PlayerRequestBuilder;
import com.controller.player.domain.dto.PlayerIdRequestDTO;
import com.controller.player.domain.dto.PlayerRequestDTO;
import com.controller.player.domain.dto.PlayerResponseDTO;
import com.controller.player.enums.PlayerRoleEnum;
import com.controller.player.util.RandomUtil;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import java.util.ArrayList;
import java.util.List;

import static com.controller.player.properties.PlayerControllerProperties.*;
import static com.controller.player.properties.PlayerControllerProperties.MAX_PASSWORD_LENGTH;

public class BasePlayerControllerTest {
    protected ThreadLocal<List<PlayerIdRequestDTO>> createdPlayersToDelete = ThreadLocal.withInitial(ArrayList::new);
    protected ThreadLocal<PlayerIdRequestDTO> createdPlayerIdRequest = new ThreadLocal<>();
    protected ThreadLocal<PlayerRequestDTO> createdPlayerRequest = new ThreadLocal<>();
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
        List<PlayerIdRequestDTO> ids = createdPlayersToDelete.get();
        if (ids != null) {
            for (PlayerIdRequestDTO id : ids) {
                playerBo.deletePlayerById(id, SUPERVISOR_LOGIN);
            }
            ids.clear();
        }
        createdPlayersToDelete.remove();
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

        createdPlayerRequest.set(playerCreateRequest);
        setCreatedPlayerIdRequest(playerSerializedResponse);

        addCreatedPlayerForFutureDeletion(playerSerializedResponse);
    }

    protected void setCreatedPlayerIdRequest(PlayerResponseDTO playerResponse) {
        PlayerIdRequestDTO playerIdRequest = new PlayerIdRequestDTO();
        playerIdRequest.setPlayerId(playerResponse.getId());
        createdPlayerIdRequest.set(playerIdRequest);
    }

    protected void addCreatedPlayerForFutureDeletion(PlayerResponseDTO playerSerializedResponse) {
        PlayerIdRequestDTO playerIdRequest = new PlayerIdRequestDTO();
        playerIdRequest.setPlayerId(playerSerializedResponse.getId());
        createdPlayersToDelete.get().add(playerIdRequest);
    }
}
