package com.alta.bootcamp.laundryapp.services;

import com.alta.bootcamp.laundryapp.dto.ResponseDTO;
import com.alta.bootcamp.laundryapp.dto.TransactionRequestDTO;
import com.alta.bootcamp.laundryapp.dto.TransactionResponseDTO;
import com.alta.bootcamp.laundryapp.dto.TransactionStatusRequestDTO;
import com.alta.bootcamp.laundryapp.entities.Admin;
import com.alta.bootcamp.laundryapp.entities.Transaction;
import com.alta.bootcamp.laundryapp.enums.TransactionStatusEnum;
import com.alta.bootcamp.laundryapp.exceptions.ValidationErrorException;
import com.alta.bootcamp.laundryapp.repositories.TransactionRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdateTransactionTest {
  @Mock
  TransactionRepository transactionRepository;

  @InjectMocks
  TransactionService serviceUnderTest = spy(new TransactionService());

  ModelMapper modelMapper = spy(new ModelMapper());

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test(expected = ValidationErrorException.class)
  public void givenNullIdAndNullRequest_when_updateTransaction_then_shouldThrowException() {
    serviceUnderTest.updateTransaction(null, null);
  }

  @Test(expected = ValidationErrorException.class)
  public void givenNullIdAndValidRequest_when_updateTransaction_then_shouldThrowException() {
    TransactionRequestDTO request = new TransactionRequestDTO();
    request.setAdminId(1L);
    request.setWeight(3);
    request.setNotes("Catatan Transaksi");
    request.setTotalPrice(BigDecimal.valueOf(15000));
    request.setStatus(TransactionStatusEnum.valueOf("NEW"));
    serviceUnderTest.updateTransaction(null, request);
  }

  @Test(expected = ValidationErrorException.class)
  public void givenValidIdAndNullRequest_when_updateTransaction_then_shouldThrowException() {
    serviceUnderTest.updateTransaction(1L, null);
  }

  @Test
  public void givenValidIdAndValidRequest_when_updateTransaction_then_shouldReturnUpdatedTransaction() {
    Long requestParamId = 1L;

    List<Transaction> transactions = new ArrayList<>();

    Optional<Admin> optionalAdmin = Optional.of(new Admin());
    optionalAdmin.get().setId(1L);
    optionalAdmin.get().setUsername("kangabbad");
    optionalAdmin.get().setEmail("email1@gmail.com");
    optionalAdmin.get().setPhone("087739999776");
    optionalAdmin.get().setIdCard("3337201117380007");
    optionalAdmin.get().setName("Naufal Abbad");
    optionalAdmin.get().setAddress("Laweyan, Solo");
    optionalAdmin.get().setTransactions(transactions);
    optionalAdmin.get().setPassword("Waduh");

    Optional<Transaction> optionalTransaction = Optional.of(new Transaction());
    optionalTransaction.get().setId(1L);
    optionalTransaction.get().setAdmin(optionalAdmin.get());
    optionalTransaction.get().setWeight(3);
    optionalTransaction.get().setNotes("Catatan Transaksi");
    optionalTransaction.get().setTotalPrice(BigDecimal.valueOf(15000));
    optionalTransaction.get().setStatus(TransactionStatusEnum.valueOf("NEW"));

    when(transactionRepository.findById(anyLong())).thenReturn(optionalTransaction);

    TransactionRequestDTO request = new TransactionRequestDTO();
    request.setAdminId(1L);
    request.setWeight(3);
    request.setTotalPrice(BigDecimal.valueOf(15000));
    request.setNotes("Catatan Transaksi");
    request.setStatus(TransactionStatusEnum.valueOf("NEW"));

    Transaction newTransaction = modelMapper.map(request, Transaction.class);
    newTransaction.setId(1L);
    TransactionResponseDTO newTransactionDto = convertToDto(newTransaction);

    when(transactionRepository.save(any(Transaction.class))).thenReturn(newTransaction);

    ResponseDTO<TransactionResponseDTO> updatedTransaction = serviceUnderTest.updateTransaction(requestParamId, request);

    ResponseDTO<TransactionResponseDTO> response = new ResponseDTO<>();
    response.setData(newTransactionDto);
    response.setStatus(HttpStatus.OK.value());
    response.setMessage("Transaction updated successfully");

    assertThat(updatedTransaction).isEqualTo(response);
  }

  @Test(expected = ValidationErrorException.class)
  public void givenNullIdAndNullRequest_when_updateTransactionStatus_then_shouldThrowException() {
    serviceUnderTest.updateTransactionStatus(null, null);
  }

  @Test(expected = ValidationErrorException.class)
  public void givenNullIdAndValidRequest_when_updateTransactionStatus_then_shouldThrowException() {
    TransactionStatusRequestDTO request = new TransactionStatusRequestDTO();
    request.setStatus(TransactionStatusEnum.valueOf("NEW"));
    serviceUnderTest.updateTransactionStatus(null, request);
  }

  @Test(expected = ValidationErrorException.class)
  public void givenValidIdAndNullRequest_when_updateTransactionStatus_then_shouldThrowException() {
    serviceUnderTest.updateTransactionStatus(1L, null);
  }

  @Test
  public void givenValidIdAndValidRequest_when_updateTransactionStatus_then_shouldReturnUpdatedTransactionStatus() {
    Long requestParamId = 1L;

    List<Transaction> transactions = new ArrayList<>();

    Optional<Admin> optionalAdmin = Optional.of(new Admin());
    optionalAdmin.get().setId(1L);
    optionalAdmin.get().setUsername("kangabbad");
    optionalAdmin.get().setEmail("email1@gmail.com");
    optionalAdmin.get().setPhone("087739999776");
    optionalAdmin.get().setIdCard("3337201117380007");
    optionalAdmin.get().setName("Naufal Abbad");
    optionalAdmin.get().setAddress("Laweyan, Solo");
    optionalAdmin.get().setTransactions(transactions);
    optionalAdmin.get().setPassword("Waduh");

    Optional<Transaction> optionalTransaction = Optional.of(new Transaction());
    optionalTransaction.get().setId(1L);
    optionalTransaction.get().setAdmin(optionalAdmin.get());
    optionalTransaction.get().setWeight(3);
    optionalTransaction.get().setNotes("Catatan Transaksi");
    optionalTransaction.get().setTotalPrice(BigDecimal.valueOf(15000));
    optionalTransaction.get().setStatus(TransactionStatusEnum.valueOf("NEW"));

    when(transactionRepository.findById(anyLong())).thenReturn(optionalTransaction);

    TransactionStatusRequestDTO request = new TransactionStatusRequestDTO();
    request.setStatus(TransactionStatusEnum.valueOf("NEW"));

    Transaction newTransaction = modelMapper.map(request, Transaction.class);
    newTransaction.setId(1L);
    TransactionResponseDTO newTransactionDto = convertToDto(newTransaction);

    when(transactionRepository.save(any(Transaction.class))).thenReturn(newTransaction);

    ResponseDTO<TransactionResponseDTO> updatedTransaction = serviceUnderTest.updateTransactionStatus(requestParamId, request);

    ResponseDTO<TransactionResponseDTO> response = new ResponseDTO<>();
    response.setData(newTransactionDto);
    response.setStatus(HttpStatus.OK.value());
    response.setMessage("Transaction updated successfully");

    assertThat(updatedTransaction).isEqualTo(response);
  }

  private TransactionResponseDTO convertToDto(Transaction transaction) {
    return modelMapper.map(transaction, TransactionResponseDTO.class);
  }
}
