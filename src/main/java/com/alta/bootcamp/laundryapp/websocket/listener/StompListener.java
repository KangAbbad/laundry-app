package com.alta.bootcamp.laundryapp.websocket.listener;

import com.alta.bootcamp.laundryapp.dto.TodayRevenueDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

public class StompListener extends StompSessionHandlerAdapter {
  private final Logger logger = LoggerFactory.getLogger(StompListener.class);

  @Override
  public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
    logger.info("--- LOGGER STARTED ---");
    logger.info("New session established: " + session.getSessionId());
    session.subscribe("/topic/messages", this);
    logger.info("Subscribed to /topic/messages");
    logger.info("Message sent to websocket server");
  }

  @Override
  public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
    logger.error("Got an exception", exception);
  }

  @Override
  public Type getPayloadType(StompHeaders headers) { return TodayRevenueDTO.class; }

  @Override
  public void handleFrame(StompHeaders headers, Object payload) {
    TodayRevenueDTO todayRevenue = (TodayRevenueDTO) payload;

    logger.info("===== Daily Revenue =====");
    logger.info("Admin ID: " + todayRevenue.getAdminId());
    logger.info("Total Revenue: " + todayRevenue.getTodayRevenue());
  }
}
