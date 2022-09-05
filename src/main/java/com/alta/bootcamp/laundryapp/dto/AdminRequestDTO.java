package com.alta.bootcamp.laundryapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AdminRequestDTO implements Serializable {
  private String username;
  private String email;
  private String phone;
  private String name;

  @JsonProperty("id_card")
  private String idCard;

  private String address;
  private String password;
}
