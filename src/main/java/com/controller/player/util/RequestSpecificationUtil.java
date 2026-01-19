package com.controller.player.util;

import com.controller.player.properties.PlayerControllerProperties;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
public class RequestSpecificationUtil {
    private static final RequestSpecification DEFAULT_SPEC = new RequestSpecBuilder()
            .setBaseUri(PlayerControllerProperties.BASE_URL)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .build();

    public static RequestSpecification getDefaultSpecification() {
        return DEFAULT_SPEC;
    }
}
