package com.alta.bootcamp.laundryapp.dto;

import com.alta.bootcamp.laundryapp.enums.TransactionStatusEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionStatusRequestDTO {
  private TransactionStatusEnum status;
}
