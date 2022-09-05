package com.alta.bootcamp.laundryapp.services;

import com.alta.bootcamp.laundryapp.dto.*;
import com.alta.bootcamp.laundryapp.entities.Admin;
import com.alta.bootcamp.laundryapp.entities.Transaction;
import com.alta.bootcamp.laundryapp.enums.TransactionStatusEnum;
import com.alta.bootcamp.laundryapp.exceptions.ResourceNotFoundException;
import com.alta.bootcamp.laundryapp.repositories.AdminRepository;
import com.alta.bootcamp.laundryapp.repositories.TransactionRepository;
import com.alta.bootcamp.laundryapp.utils.ValidationUtils;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService implements ITransactionService {
  @Autowired
  ModelMapper modelMapper;

  @Autowired
  TransactionRepository transactionRepository;

  @Autowired
  AdminRepository adminRepository;

  @SneakyThrows
  @Override
  @Transactional
  public ResponseDTO createTransaction(TransactionRequestDTO request) {
    ValidationUtils.validateTransactionRequest(request);

    Transaction transaction = convertToEntity(request);
    Transaction createdTransaction = transactionRepository.save(transaction);

    ResponseDTO response = new ResponseDTO();
    response.setData(convertToDto(Optional.of(createdTransaction)));
    response.setStatus(HttpStatus.CREATED.value());
    response.setMessage("Transaction created successfully");

    return response;
  }

  @Override
  public ResponseDTO getAllTransactions() {
    List<Transaction> transactions = transactionRepository.findAll();

    List<TransactionResponseDTO> transactionsToDto = transactions.stream()
            .map(transaction -> modelMapper.map(transaction, TransactionResponseDTO.class))
            .collect(Collectors.toList());

    ResponseDTO response = new ResponseDTO();
    response.setData(transactionsToDto);
    response.setStatus(HttpStatus.OK.value());
    response.setMessage("");

    return response;
  }

  @Override
  public ResponseDTO getTransaction(Long id) {
    Optional<Transaction> transaction = transactionRepository.findById(id);
    if (transaction.isPresent()) {
      Transaction tempTransaction = transaction.get();

      Optional<Admin> admin = adminRepository.findById(tempTransaction.getAdmin().getId());

      if (admin.isPresent()) {
        Admin newAdmin = modelMapper.map(admin, Admin.class);
        tempTransaction.setAdmin(newAdmin);
      } else {
        throw new ResourceNotFoundException("Admin not found");
      }

      Transaction convertToEntity = modelMapper.map(tempTransaction, Transaction.class);
      Transaction updatedOrder = transactionRepository.save(convertToEntity);

      ResponseDTO response = new ResponseDTO();
      response.setData(convertToDto(Optional.of(updatedOrder)));
      response.setStatus(HttpStatus.OK.value());
      response.setMessage("");
      return response;
    } else {
      throw new ResourceNotFoundException("Transaction ID not found");
    }
  }

  @Override
  public ResponseDTO updateTransaction(Long id, TransactionRequestDTO request) {
    Optional<Transaction> transaction = transactionRepository.findById(id);

    if (transaction.isPresent()) {
      Transaction tempTransaction = transaction.get();

      if (request.getAdminId() != null) {
        Optional<Admin> admin = adminRepository.findById(request.getAdminId());
        if (admin.isPresent()) {
          Admin newAdmin = modelMapper.map(admin, Admin.class);
          tempTransaction.setAdmin(newAdmin);
        } else {
          throw new ResourceNotFoundException("Admin ID not found");
        }
      }

      if (!Objects.equals(request.getWeight(), tempTransaction.getWeight())) {
        tempTransaction.setWeight(request.getWeight());
      }

      if (request.getNotes() != null) {
        tempTransaction.setNotes(request.getNotes());
      }

      if (!Objects.equals(request.getTotalPrice(), tempTransaction.getTotalPrice())) {
        tempTransaction.setTotalPrice(request.getTotalPrice());
      }

      if (request.getStatus().equals(TransactionStatusEnum.NEW) || request.getStatus().equals(TransactionStatusEnum.DONE) || request.getStatus().equals(TransactionStatusEnum.INVALID)) {
        tempTransaction.setStatus(request.getStatus());
      }

      return convertTransactionDtoToEntity(tempTransaction);
    } else {
      throw new ResourceNotFoundException("Transaction ID not found");
    }
  }

  @Override
  public ResponseDTO updateTransactionStatus(Long id, TransactionStatusRequestDTO request) {
    Optional<Transaction> transaction = transactionRepository.findById(id);

    if (transaction.isPresent()) {
      Transaction tempTransaction = transaction.get();

      if (request.getStatus().equals(TransactionStatusEnum.NEW) || request.getStatus().equals(TransactionStatusEnum.DONE) || request.getStatus().equals(TransactionStatusEnum.INVALID)) {
        tempTransaction.setStatus(request.getStatus());
      }

      return convertTransactionDtoToEntity(tempTransaction);
    } else {
      throw new ResourceNotFoundException("Transaction ID not found");
    }
  }

  @Override
  public ResponseDTO deleteTransaction(Long id) {
    Optional<Transaction> transaction = transactionRepository.findById(id);

    ResponseDTO response = new ResponseDTO();

    if (transaction.isPresent()) {
      transactionRepository.deleteById(id);

      response.setData(null);
      response.setStatus(HttpStatus.NO_CONTENT.value());
      response.setMessage("Transaction ID: " + id + " deleted successfully");

      return response;
    } else {
      throw new ResourceNotFoundException("Transaction ID not found");
    }
  }

  private Transaction convertToEntity(TransactionRequestDTO request) {
    return modelMapper.map(request, Transaction.class);
  }

  private ResponseDTO convertTransactionDtoToEntity(Transaction tempTransaction) {
    Transaction convertToEntity = modelMapper.map(tempTransaction, Transaction.class);
    Transaction updatedTransaction = transactionRepository.save(convertToEntity);

    ResponseDTO response = new ResponseDTO();
    response.setData(convertToDto(Optional.of(updatedTransaction)));
    response.setStatus(HttpStatus.OK.value());
    response.setMessage("Transaction updated successfully");

    return response;
  }

  private TransactionResponseDTO convertToDto(Optional<Transaction> transaction) {
    return modelMapper.map(transaction, TransactionResponseDTO.class);
  }
}
