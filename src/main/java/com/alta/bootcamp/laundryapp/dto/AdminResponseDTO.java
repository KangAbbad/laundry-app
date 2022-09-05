package com.alta.bootcamp.laundryapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class AdminResponseDTO implements Serializable {
  private Long id;
  private String name;
  private String username;
  private String email;
  private String phone;

  @JsonProperty("id_card")
  private String idCard;

  private String address;

  @JsonIgnore
  private String password;
  @JsonProperty("created_at")
  private Date createdAt;
  @JsonProperty("updated_at")
  private Date updatedAt;
}
