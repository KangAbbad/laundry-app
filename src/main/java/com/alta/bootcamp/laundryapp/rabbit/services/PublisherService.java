package com.alta.bootcamp.laundryapp.rabbit.services;

import com.alta.bootcamp.laundryapp.dto.TransactionRequestDTO;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PublisherService implements IPublisherService {
  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private Queue queue;

  @Value("${spring.rabbitmq.template.exchange}")
  private String exchangeName;

  @Value("${spring.rabbitmq.template.routing-key}")
  private String routingKey;

  @Override
  public void send(TransactionRequestDTO request) {
    rabbitTemplate.convertAndSend(queue.getName(), request);
  }

  @Override
  public void sendUsingExchange(TransactionRequestDTO request) {
    rabbitTemplate.convertAndSend(exchangeName, routingKey, request);
  }
}
