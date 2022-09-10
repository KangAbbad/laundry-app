package com.alta.bootcamp.laundryapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class SummaryRevenueResponseDTO implements Serializable {
  private Long id;

  @JsonProperty("admin_id")
  private Long adminId;

  @JsonProperty("total_revenue")
  private BigDecimal totalRevenue;

  @JsonProperty("created_at")
  private Date createdAt;

  @JsonProperty("updated_at")
  private Date updatedAt;
}
