package com.alta.bootcamp.laundryapp.rabbit.services;

import com.alta.bootcamp.laundryapp.dto.TransactionRequestDTO;

public interface IPublisherService {
  void send(TransactionRequestDTO request);
  void sendUsingExchange(TransactionRequestDTO request);
}
