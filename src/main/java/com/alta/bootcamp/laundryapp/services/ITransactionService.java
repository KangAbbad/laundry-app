package com.alta.bootcamp.laundryapp.services;

import com.alta.bootcamp.laundryapp.dto.ResponseDTO;
import com.alta.bootcamp.laundryapp.dto.TransactionRequestDTO;
import com.alta.bootcamp.laundryapp.dto.TransactionStatusRequestDTO;
import com.alta.bootcamp.laundryapp.enums.TransactionStatusEnum;

public interface ITransactionService {
  ResponseDTO createTransaction(TransactionRequestDTO request);
  ResponseDTO getAllTransactions();
  ResponseDTO getTransaction(Long id);
  ResponseDTO updateTransaction(Long id, TransactionRequestDTO request);
  ResponseDTO updateTransactionStatus(Long id, TransactionStatusRequestDTO request);
  ResponseDTO deleteTransaction(Long id);
}
