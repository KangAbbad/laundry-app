package com.alta.bootcamp.laundryapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class JwtAuthenticationResponseDTO {
  @JsonProperty("access_token")
  private final String accessToken;

  @JsonProperty("token_type")
  private String tokenType = "Bearer";
}
