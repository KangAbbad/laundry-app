package com.alta.bootcamp.laundryapp.websocket.client;

import com.alta.bootcamp.laundryapp.websocket.listener.StompListener;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.Scanner;

public class StompClient {
  public static void main(String[] args) {
    WebSocketClient client = new StandardWebSocketClient();
    WebSocketStompClient stompClient = new WebSocketStompClient(client);

    stompClient.setMessageConverter(new MappingJackson2MessageConverter());

    StompListener stompListener = new StompListener();
    // Listen to Localhost
    String URL = "ws://localhost:8080/chat";

    // Listen to ngrok
    // String URL = "ws://9899-140-213-162-102.ap.ngrok.io/chat";
    stompClient.connect(URL, stompListener);

    new Scanner(System.in).nextLine();
  }
}
