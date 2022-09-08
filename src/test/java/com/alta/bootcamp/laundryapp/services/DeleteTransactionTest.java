package com.alta.bootcamp.laundryapp.services;

import com.alta.bootcamp.laundryapp.dto.ResponseDTO;
import com.alta.bootcamp.laundryapp.dto.TransactionResponseDTO;
import com.alta.bootcamp.laundryapp.entities.Transaction;
import com.alta.bootcamp.laundryapp.exceptions.ValidationErrorException;
import com.alta.bootcamp.laundryapp.repositories.TransactionRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DeleteTransactionTest {
  @Mock
  TransactionRepository transactionRepository;

  @InjectMocks
  TransactionService serviceUnderTest = spy(new TransactionService());

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test(expected = ValidationErrorException.class)
  public void givenNullId_when_deleteTransaction_then_shouldThrowException() {
    serviceUnderTest.deleteTransaction(null);
  }

  @Test
  public void givenValidId_when_deleteTransaction_then_shouldDeleteTransaction() {
    Long requestParamId = 1L;

    Optional<Transaction> transaction = Optional.of(new Transaction());
    transaction.get().setId(1L);

    when(transactionRepository.findById(anyLong())).thenReturn(transaction);

    ResponseDTO<TransactionResponseDTO> deletedTransaction = serviceUnderTest.deleteTransaction(requestParamId);

    ResponseDTO<TransactionResponseDTO> response = new ResponseDTO<>();
    response.setData(null);
    response.setStatus(HttpStatus.NO_CONTENT.value());
    response.setMessage("Transaction ID: " + requestParamId + " deleted successfully");

    assertThat(deletedTransaction).isEqualTo(response);
  }
}
