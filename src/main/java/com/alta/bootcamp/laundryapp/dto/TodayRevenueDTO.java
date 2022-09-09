package com.alta.bootcamp.laundryapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
public class TodayRevenueDTO {
  @JsonProperty("admin_id")
  private Long adminId;
  @JsonProperty("today_revenue")
  private BigDecimal todayRevenue;
}
