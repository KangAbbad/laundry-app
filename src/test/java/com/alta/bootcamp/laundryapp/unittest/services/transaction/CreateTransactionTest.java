package com.alta.bootcamp.laundryapp.unittest.services.transaction;

import com.alta.bootcamp.laundryapp.dto.ResponseDTO;
import com.alta.bootcamp.laundryapp.dto.TransactionRequestDTO;
import com.alta.bootcamp.laundryapp.dto.TransactionResponseDTO;
import com.alta.bootcamp.laundryapp.entities.Transaction;
import com.alta.bootcamp.laundryapp.enums.TransactionStatusEnum;
import com.alta.bootcamp.laundryapp.exceptions.ValidationErrorException;
import com.alta.bootcamp.laundryapp.repositories.TransactionRepository;
import com.alta.bootcamp.laundryapp.services.TransactionService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreateTransactionTest {
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
  public void givenNullRequest_when_createNewTransaction_then_shouldThrowException() {
    serviceUnderTest.createTransaction(null);
  }

  @Test
  public void givenValidRequest_when_createNewTransaction_then_transactionShouldBeCreated() {
    TransactionRequestDTO request = new TransactionRequestDTO();
    request.setAdminId(1L);
    request.setWeight(3);
    request.setNotes("Catatan Transaksi");
    request.setTotalPrice(BigDecimal.valueOf(15000));
    request.setStatus(TransactionStatusEnum.valueOf("NEW"));

    Transaction newTransaction = modelMapper.map(request, Transaction.class);
    newTransaction.setId(1L);

    ResponseDTO<TransactionResponseDTO> response = new ResponseDTO<>();
    response.setData(convertToDto(newTransaction));
    response.setStatus(HttpStatus.CREATED.value());
    response.setMessage("Transaction created successfully");

    when(transactionRepository.save(any(Transaction.class))).thenReturn(newTransaction);
    ResponseDTO<TransactionResponseDTO> createdTransaction = serviceUnderTest.createTransaction(request);

    assertThat(createdTransaction.getData()).isEqualTo(response.getData());
  }

  @Test(expected = ValidationErrorException.class)
  public void givenEmptyAdminId_when_createNewTransaction_then_shouldThrowException() {
    TransactionRequestDTO request = new TransactionRequestDTO();
    request.setAdminId(null);
    request.setWeight(3);
    request.setNotes("Catatan Transaksi");
    request.setTotalPrice(BigDecimal.valueOf(15000));
    request.setStatus(TransactionStatusEnum.valueOf("NEW"));

    Transaction newTransaction = modelMapper.map(request, Transaction.class);
    newTransaction.setId(1L);

    serviceUnderTest.createTransaction(request);
  }

  @Test(expected = ValidationErrorException.class)
  public void givenEmptyWeight_when_createNewTransaction_then_shouldThrowException() {
    TransactionRequestDTO request = new TransactionRequestDTO();
    request.setAdminId(1L);
    request.setWeight(0);
    request.setNotes("Catatan Transaksi");
    request.setTotalPrice(BigDecimal.valueOf(15000));
    request.setStatus(TransactionStatusEnum.valueOf("NEW"));

    Transaction newTransaction = modelMapper.map(request, Transaction.class);
    newTransaction.setId(1L);

    serviceUnderTest.createTransaction(request);
  }

  @Test(expected = ValidationErrorException.class)
  public void givenEmptyTotalPrice_when_createNewTransaction_then_shouldThrowException() {
    TransactionRequestDTO request = new TransactionRequestDTO();
    request.setAdminId(1L);
    request.setWeight(3);
    request.setNotes("Catatan Transaksi");
    request.setTotalPrice(null);
    request.setStatus(TransactionStatusEnum.valueOf("NEW"));

    Transaction newTransaction = modelMapper.map(request, Transaction.class);
    newTransaction.setId(1L);

    serviceUnderTest.createTransaction(request);
  }

  @Test(expected = ValidationErrorException.class)
  public void givenEmptyStatus_when_createNewTransaction_then_shouldThrowException() {
    TransactionRequestDTO request = new TransactionRequestDTO();
    request.setAdminId(1L);
    request.setWeight(3);
    request.setNotes("Catatan Transaksi");
    request.setTotalPrice(BigDecimal.valueOf(15000));
    request.setStatus(null);

    Transaction newTransaction = modelMapper.map(request, Transaction.class);
    newTransaction.setId(1L);

    serviceUnderTest.createTransaction(request);
  }

  private TransactionResponseDTO convertToDto(Transaction transaction) {
    return modelMapper.map(transaction, TransactionResponseDTO.class);
  }
}
