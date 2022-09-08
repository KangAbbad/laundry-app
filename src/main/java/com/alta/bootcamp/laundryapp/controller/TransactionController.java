package com.alta.bootcamp.laundryapp.controller;

import com.alta.bootcamp.laundryapp.dto.*;
import com.alta.bootcamp.laundryapp.services.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
  @Autowired
  ITransactionService transactionService;

  @PostMapping
  public ResponseEntity<ResponseDTO<TransactionResponseDTO>> createTransaction(@RequestBody TransactionRequestDTO request) {
    ResponseDTO<TransactionResponseDTO> response = transactionService.createTransaction(request);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @GetMapping
  public ResponseEntity<ResponseWithMetaDTO<List<TransactionResponseDTO>>> getAllTransactions(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(name = "per_page", defaultValue = "5") int perPage
  ) {
    Pageable pages;
    if (page < 1) {
      pages = PageRequest.of(0, perPage);
    } else {
      pages = PageRequest.of(page - 1, perPage);
    }
    ResponseWithMetaDTO<List<TransactionResponseDTO>> response = transactionService.getAllTransactions(pages);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ResponseDTO<TransactionResponseDTO>> getTransaction(@PathVariable("id") Long id) {
    ResponseDTO<TransactionResponseDTO> response = transactionService.getTransaction(id);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ResponseDTO<TransactionResponseDTO>> updateTransaction(@PathVariable("id") Long id, @RequestBody TransactionRequestDTO request) {
    ResponseDTO<TransactionResponseDTO> response = transactionService.updateTransaction(id, request);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @PutMapping("/{id}/status")
  public ResponseEntity<ResponseDTO<TransactionResponseDTO>> updateTransactionStatus(@PathVariable("id") Long id, @RequestBody TransactionStatusRequestDTO request) {
    ResponseDTO<TransactionResponseDTO> response = transactionService.updateTransactionStatus(id, request);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ResponseDTO<TransactionResponseDTO>> deleteTransaction(@PathVariable("id") Long id) {
    ResponseDTO<TransactionResponseDTO> response = transactionService.deleteTransaction(id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}