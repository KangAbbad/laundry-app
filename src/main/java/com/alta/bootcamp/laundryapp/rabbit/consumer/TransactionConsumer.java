package com.alta.bootcamp.laundryapp.rabbit.consumer;

import com.alta.bootcamp.laundryapp.dto.TransactionRequestDTO;
import com.alta.bootcamp.laundryapp.entities.Admin;
import com.alta.bootcamp.laundryapp.entities.Transaction;
import com.alta.bootcamp.laundryapp.exceptions.ResourceNotFoundException;
import com.alta.bootcamp.laundryapp.repositories.AdminRepository;
import com.alta.bootcamp.laundryapp.repositories.TransactionRepository;
import com.alta.bootcamp.laundryapp.services.TransactionService;
import com.alta.bootcamp.laundryapp.utils.ValidationUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TransactionConsumer {
  private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

  @Autowired
  ModelMapper modelMapper;

  @Autowired
  TransactionRepository transactionRepository;

  @Autowired
  AdminRepository adminRepository;

  @Autowired
  private SimpMessageSendingOperations messagingTemplate;

  @RabbitListener(queues = {"${spring.rabbitmq.template.default-receive-queue}"})
  public void createTransaction(@Payload TransactionRequestDTO request) {
    ValidationUtils.validateTransactionRequest(request);

    Optional<Admin> admin = adminRepository.findById(request.getAdminId());

    if (admin.isPresent()) {
      Transaction newTransaction = modelMapper.map(request, Transaction.class);
      newTransaction.setId(null);
      newTransaction.setAdmin(admin.get());

      transactionRepository.save(newTransaction);

      logger.info("[POST] /api/v1/transactions - [Rabbit] Transaction created successfully");
    } else {
      logger.error("[POST] /api/v1/transactions - [Rabbit] Admin ID not found");
      throw new ResourceNotFoundException("Admin ID not found");
    }
  }
}
