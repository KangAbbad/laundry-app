package com.alta.bootcamp.laundryapp.websocket.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MessageDTO {
  private Long adminId;
  private BigDecimal totalRevenue;
  private String notes;
}
