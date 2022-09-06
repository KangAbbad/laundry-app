package com.alta.bootcamp.laundryapp.dto;

import com.alta.bootcamp.laundryapp.enums.TransactionStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
public class TransactionResponseDTO implements Serializable {
  private Long id;

  @JsonProperty("admin_id")
  private Long adminId;

  private AdminResponseDTO admin;

  private int weight;
  private String notes;

  @JsonProperty("total_price")
  private BigDecimal totalPrice;

  private TransactionStatusEnum status;

  @JsonProperty("created_at")
  private Date createdAt;
  @JsonProperty("updated_at")
  private Date updatedAt;

  @Override
  public String toString() {
    return "TransactionResponseDTO{" +
            "id=" + id +
            ", adminId=" + adminId +
            ", admin=" + admin +
            ", weight=" + weight +
            ", notes='" + notes + '\'' +
            ", totalPrice=" + totalPrice +
            ", status=" + status +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            '}';
  }
}
