package com.alta.bootcamp.laundryapp.dto;

import com.alta.bootcamp.laundryapp.enums.TransactionStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class TransactionRequestDTO implements Serializable {
  @JsonProperty("admin_id")
  private Long adminId;

  private int weight;
  private String notes;

  @JsonProperty("total_price")
  private BigDecimal totalPrice;

  private TransactionStatusEnum status;
}
