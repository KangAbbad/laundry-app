package com.alta.bootcamp.laundryapp.services;

import com.alta.bootcamp.laundryapp.dto.*;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface ITransactionService {
  ResponseDTO<TransactionResponseDTO> createTransaction(TransactionRequestDTO request);
  ResponseWithMetaDTO<List<TransactionResponseDTO>> getAllTransactions(Pageable pageable);
  ResponseDTO<TransactionResponseDTO> getTransaction(Long id);
  ResponseDTO<TransactionResponseDTO> updateTransaction(Long id, TransactionRequestDTO request);
  ResponseDTO<TransactionResponseDTO> updateTransactionStatus(Long id, TransactionStatusRequestDTO request);
  ResponseDTO<TransactionResponseDTO> deleteTransaction(Long id);
  ByteArrayInputStream downloadExcel();
}
