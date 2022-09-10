package com.alta.bootcamp.laundryapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class SummaryRevenueRequestDTO implements Serializable {
  @JsonProperty("admin_id")
  private Long adminId;
  @JsonProperty("total_revenue")
  private BigDecimal totalRevenue;
}
