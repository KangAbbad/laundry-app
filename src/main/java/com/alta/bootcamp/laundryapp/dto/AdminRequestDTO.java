package com.alta.bootcamp.laundryapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
public class AdminRequestDTO implements Serializable {
  @NotBlank
  private String username;
  @NotBlank
  private String email;
  @NotBlank
  private String phone;
  @JsonProperty("id_card")
  private String idCard;

  private String name;
  private String address;
  @NotBlank
  private String password;
}
