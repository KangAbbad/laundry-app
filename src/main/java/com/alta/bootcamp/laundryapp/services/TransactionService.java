package com.alta.bootcamp.laundryapp.services;

import com.alta.bootcamp.laundryapp.dto.*;
import com.alta.bootcamp.laundryapp.entities.Admin;
import com.alta.bootcamp.laundryapp.entities.Transaction;
import com.alta.bootcamp.laundryapp.enums.TransactionStatusEnum;
import com.alta.bootcamp.laundryapp.exceptions.ResourceNotFoundException;
import com.alta.bootcamp.laundryapp.exceptions.ValidationErrorException;
import com.alta.bootcamp.laundryapp.repositories.AdminRepository;
import com.alta.bootcamp.laundryapp.repositories.TransactionRepository;
import com.alta.bootcamp.laundryapp.utils.ValidationUtils;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
  public ResponseDTO<TransactionResponseDTO> createTransaction(TransactionRequestDTO request) {
    ValidationUtils.validateTransactionRequest(request);

    Optional<Admin> admin = adminRepository.findById(request.getAdminId());

    if (admin.isPresent()) {
//      Transaction newTransaction = new Transaction();
//      newTransaction.setAdmin(admin.get());
//      newTransaction.setWeight(request.getWeight());
//      newTransaction.setNotes(request.getNotes());
//      newTransaction.setTotalPrice(request.getTotalPrice());
//      newTransaction.setStatus(request.getStatus());

      Transaction newTransaction = modelMapper.map(request, Transaction.class);
      newTransaction.setId(null);
      newTransaction.setAdmin(admin.get());
      Transaction createdTransaction = transactionRepository.save(newTransaction);

      ResponseDTO<TransactionResponseDTO> response = new ResponseDTO<>();
      response.setData(convertToDto(createdTransaction));
      response.setStatus(HttpStatus.CREATED.value());
      response.setMessage("Transaction created successfully");

      return response;
    } else {
      throw new ResourceNotFoundException("Admin ID not found");
    }
  }

  @Override
  public ResponseWithMetaDTO<List<TransactionResponseDTO>> getAllTransactions(Pageable pageable) {
    Page<Transaction> transactions = transactionRepository.findAll(pageable);

    List<TransactionResponseDTO> transactionsToDto = transactions.stream()
            .map(transaction -> modelMapper.map(transaction, TransactionResponseDTO.class))
            .collect(Collectors.toList());

    ResponseWithMetaDTO.Meta responseMeta = new ResponseWithMetaDTO.Meta();
    responseMeta.setPage(pageable.getPageNumber() + 1);
    responseMeta.setPerPage(pageable.getPageSize());
    responseMeta.setTotalPage(transactions.getTotalPages());
    responseMeta.setTotalData(transactions.getTotalElements());

    ResponseWithMetaDTO<List<TransactionResponseDTO>> response = new ResponseWithMetaDTO<>();
    response.setData(transactionsToDto);
    response.setMeta(responseMeta);
    response.setStatus(HttpStatus.OK.value());
    response.setMessage("");

    return response;
  }

  @Override
  @Transactional
  public ResponseDTO<TransactionResponseDTO> getTransaction(Long id) {
    if (id == null) throw new ValidationErrorException("Transaction ID cannot be empty");

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

      ResponseDTO<TransactionResponseDTO> response = new ResponseDTO<>();
      response.setData(convertToDto(tempTransaction));
      response.setStatus(HttpStatus.OK.value());
      response.setMessage("");
      return response;
    } else {
      throw new ResourceNotFoundException("Transaction ID not found");
    }
  }

  @Override
  @Transactional
  public ResponseDTO<TransactionResponseDTO> updateTransaction(Long id, TransactionRequestDTO request) {
    if (id == null) throw new ValidationErrorException("Transaction ID cannot be empty");
    if (request == null) throw new ValidationErrorException("Body request cannot be empty");

    Optional<Transaction> transaction = transactionRepository.findById(id);

    if (transaction.isPresent()) {
      Transaction tempTransaction = transaction.get();

      if (request.getAdminId() != null && !Objects.equals(request.getAdminId(), tempTransaction.getAdmin().getId())) {
        Optional<Admin> admin = adminRepository.findById(request.getAdminId());
        if (admin.isPresent()) {
          Admin newAdmin = modelMapper.map(admin, Admin.class);
          tempTransaction.setAdmin(newAdmin);
        } else {
          throw new ResourceNotFoundException("Admin ID not found");
        }
      }

      if (request.getWeight() > 0 && !Objects.equals(request.getWeight(), tempTransaction.getWeight())) {
        tempTransaction.setWeight(request.getWeight());
      }

      if (request.getNotes() != null && !Objects.equals(request.getNotes(), tempTransaction.getNotes())) {
        tempTransaction.setNotes(request.getNotes());
      }

      if (request.getTotalPrice() != null && !Objects.equals(request.getTotalPrice(), tempTransaction.getTotalPrice())) {
        tempTransaction.setTotalPrice(request.getTotalPrice());
      }

      if (
              request.getStatus() != null &&
              (request.getStatus().equals(TransactionStatusEnum.NEW) ||
              request.getStatus().equals(TransactionStatusEnum.DONE) ||
              request.getStatus().equals(TransactionStatusEnum.INVALID))
      ) {
        tempTransaction.setStatus(request.getStatus());
      }

      return convertTransactionEntityToDto(tempTransaction);
    } else {
      throw new ResourceNotFoundException("Transaction ID not found");
    }
  }

  @Override
  public ResponseDTO<TransactionResponseDTO> updateTransactionStatus(Long id, TransactionStatusRequestDTO request) {
    if (id == null) throw new ValidationErrorException("Transaction ID cannot be empty");
    if (request == null) throw new ValidationErrorException("Body request cannot be empty");

    Optional<Transaction> transaction = transactionRepository.findById(id);

    if (transaction.isPresent()) {
      Transaction tempTransaction = transaction.get();

      if (
              request.getStatus() != null &&
              (request.getStatus().equals(TransactionStatusEnum.NEW) ||
              request.getStatus().equals(TransactionStatusEnum.DONE) ||
              request.getStatus().equals(TransactionStatusEnum.INVALID))
      ) {
        tempTransaction.setStatus(request.getStatus());
      }

      return convertTransactionEntityToDto(tempTransaction);
    } else {
      throw new ResourceNotFoundException("Transaction ID not found");
    }
  }

  @Override
  public ResponseDTO<TransactionResponseDTO> deleteTransaction(Long id) {
    if (id == null) throw new ValidationErrorException("Transaction ID cannot be empty");

    Optional<Transaction> transaction = transactionRepository.findById(id);

    ResponseDTO<TransactionResponseDTO> response = new ResponseDTO<>();

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

  private ResponseDTO<TransactionResponseDTO> convertTransactionEntityToDto(Transaction tempTransaction) {
    Transaction updatedTransaction = transactionRepository.save(tempTransaction);

    ResponseDTO<TransactionResponseDTO> response = new ResponseDTO<>();
    response.setData(convertToDto(updatedTransaction));
    response.setStatus(HttpStatus.OK.value());
    response.setMessage("Transaction updated successfully");

    return response;
  }

  private TransactionResponseDTO convertToDto(Transaction transaction) {
    return modelMapper.map(transaction, TransactionResponseDTO.class);
  }
}
