package com.assignment.stockapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
@RestController
public class SecurityController {

    @Operation(summary = "this endpoint is used by Okta to send back the JWT access token")
    @PostMapping("/authorization-code/callback")
    public Map<String, String> callbackToken(@RequestBody MultiValueMap<String, String> queryMap) {
        return queryMap.toSingleValueMap();
    }
}
