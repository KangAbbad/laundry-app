package com.alta.bootcamp.laundryapp.controller;

import com.alta.bootcamp.laundryapp.dto.TransactionRequestDTO;
import com.alta.bootcamp.laundryapp.dto.ResponseDTO;
import com.alta.bootcamp.laundryapp.dto.TransactionStatusRequestDTO;
import com.alta.bootcamp.laundryapp.enums.TransactionStatusEnum;
import com.alta.bootcamp.laundryapp.services.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
  @Autowired
  ITransactionService transactionService;

  @PostMapping
  public ResponseEntity<ResponseDTO> createTransaction(@RequestBody TransactionRequestDTO request) {
    ResponseDTO response = transactionService.createTransaction(request);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @GetMapping
  public ResponseEntity<ResponseDTO> getAllTransactions() {
    ResponseDTO response = transactionService.getAllTransactions();
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ResponseDTO> getTransaction(@PathVariable("id") Long id) {
    ResponseDTO response = transactionService.getTransaction(id);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ResponseDTO> updateTransaction(@PathVariable("id") Long id, @RequestBody TransactionRequestDTO request) {
    ResponseDTO response = transactionService.updateTransaction(id, request);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @PutMapping("/{id}/status")
  public ResponseEntity<ResponseDTO> updateTransactionStatus(@PathVariable("id") Long id, @RequestBody TransactionStatusRequestDTO request) {
    ResponseDTO response = transactionService.updateTransactionStatus(id, request);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ResponseDTO> deleteTransaction(@PathVariable("id") Long id) {
    ResponseDTO response = transactionService.deleteTransaction(id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
