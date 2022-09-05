package com.alta.bootcamp.laundryapp.services;

import com.alta.bootcamp.laundryapp.dto.AdminRequestDTO;
import com.alta.bootcamp.laundryapp.dto.AdminResponseDTO;
import com.alta.bootcamp.laundryapp.enums.TransactionStatusEnum;

import java.util.List;

public interface ITransactionService {
  AdminResponseDTO createTransaction(AdminRequestDTO request);
  List<AdminResponseDTO> getAllTransactions();
  AdminResponseDTO updateTransaction(Long id, AdminRequestDTO request);
  AdminResponseDTO updateTransactionStatus(Long id, TransactionStatusEnum status);
  String deleteTransaction(Long id);
}
