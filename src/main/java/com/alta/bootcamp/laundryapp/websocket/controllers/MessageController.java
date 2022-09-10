package com.alta.bootcamp.laundryapp.websocket.controllers;

import com.alta.bootcamp.laundryapp.dto.TodayRevenueDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {
  @MessageMapping("/chat/sendMessage")
  @SendTo("/topic/messages")
  public TodayRevenueDTO sendMessage(@Payload TodayRevenueDTO message) {
    return message;
  }
}
