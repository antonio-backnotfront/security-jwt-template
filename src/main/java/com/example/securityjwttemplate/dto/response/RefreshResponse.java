package com.example.securityjwttemplate.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RefreshResponse(@JsonProperty("access_token") String accessToken) {
}
