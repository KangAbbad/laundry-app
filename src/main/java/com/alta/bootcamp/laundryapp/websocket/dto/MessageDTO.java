package com.alta.bootcamp.laundryapp.websocket.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDTO {
  private String type;
  private String title;
  private String description;
}
