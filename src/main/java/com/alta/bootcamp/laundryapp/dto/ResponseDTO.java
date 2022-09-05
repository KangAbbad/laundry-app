package com.alta.bootcamp.laundryapp.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO implements Serializable {
  private Object data;
  private int status;
  private String message;
}
