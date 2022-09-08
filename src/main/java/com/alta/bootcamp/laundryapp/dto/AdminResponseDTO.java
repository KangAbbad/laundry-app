package com.alta.bootcamp.laundryapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@JsonPropertyOrder(value = {"id", "username", "email", "phone", "id_card", "name", "address", "transactions", "created_at", "updated_at"})
public class AdminResponseDTO implements Serializable {
  private Long id;
  private String username;
  private String email;
  private String phone;
  @JsonProperty("id_card")
  private String idCard;
  private String name;
  private String address;

  private List<TransactionResponseDTO> transactions;

  @JsonIgnore
  private String password;

  @JsonProperty("created_at")
  private Date createdAt;
  @JsonProperty("updated_at")
  private Date updatedAt;
}
