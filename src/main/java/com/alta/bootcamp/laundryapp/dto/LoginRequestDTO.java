package com.alta.bootcamp.laundryapp.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequestDTO {
  @NotBlank
  private String username;

  @NotBlank
  private String password;
}
