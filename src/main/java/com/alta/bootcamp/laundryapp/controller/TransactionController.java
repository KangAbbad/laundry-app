package com.alta.bootcamp.laundryapp.controller;

import com.alta.bootcamp.laundryapp.dto.*;
import com.alta.bootcamp.laundryapp.services.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @GetMapping("/download-excel")
  public ResponseEntity<Resource> downloadExcel() {
    String fileName = "transactions.xlsx";
    String headerValue = "attachment; filename=" + fileName;
    InputStreamResource excelResource = new InputStreamResource(transactionService.downloadExcel());

    return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
            .body(excelResource);

//    ResponseDTO<Resource> response = new ResponseDTO<>();
//    response.setData(excelResource);
//    response.setStatus(HttpStatus.OK.value());
//    response.setMessage("Excel downloaded successfully");

//    HttpHeaders responseHeaders = new HttpHeaders();
//    responseHeaders.add(HttpHeaders.CONTENT_DISPOSITION, headerValue);
//    responseHeaders.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));

//    return new ResponseEntity<>(response, responseHeaders, HttpStatus.valueOf(response.getStatus()));
  }

  @GetMapping("/today-revenue")
  public ResponseEntity<ResponseDTO<List<TodayRevenueDTO>>> getTodayRevenue() {
    ResponseDTO<List<TodayRevenueDTO>> response = transactionService.getTodayRevenue();
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }
}
