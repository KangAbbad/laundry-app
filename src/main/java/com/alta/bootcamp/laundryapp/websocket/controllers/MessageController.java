package com.alta.bootcamp.laundryapp.websocket.controllers;

import com.alta.bootcamp.laundryapp.websocket.dto.MessageDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {
  @MessageMapping("/chat/sendMessage")
  @SendTo("/topic/messages")
  public MessageDTO sendMessage(@Payload MessageDTO message) {
    return message;
  }
}
