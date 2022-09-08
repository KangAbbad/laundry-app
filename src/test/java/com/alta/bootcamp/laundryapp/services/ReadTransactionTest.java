package com.alta.bootcamp.laundryapp.services;

import com.alta.bootcamp.laundryapp.dto.ResponseDTO;
import com.alta.bootcamp.laundryapp.dto.ResponseWithMetaDTO;
import com.alta.bootcamp.laundryapp.dto.TransactionResponseDTO;
import com.alta.bootcamp.laundryapp.entities.Admin;
import com.alta.bootcamp.laundryapp.entities.Transaction;
import com.alta.bootcamp.laundryapp.enums.TransactionStatusEnum;
import com.alta.bootcamp.laundryapp.exceptions.ValidationErrorException;
import com.alta.bootcamp.laundryapp.repositories.AdminRepository;
import com.alta.bootcamp.laundryapp.repositories.TransactionRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReadTransactionTest {
  @Mock
  TransactionRepository transactionRepository;

  @Mock
  AdminRepository adminRepository;

  @InjectMocks
  TransactionService serviceUnderTest = spy(new TransactionService());

  ModelMapper modelMapper = spy(new ModelMapper());

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void getAllTransactions_then_shouldReturnListOfTransactions() {
    Admin admin1 = new Admin();
    admin1.setUsername("kangabbad");
    admin1.setEmail("email1@gmail.com");
    admin1.setPhone("087739999776");
    admin1.setName("Naufal Abbad");
    admin1.setAddress("Laweyan, Solo");
    admin1.setIdCard("3337201117380007");
    admin1.setPassword("Waduh");

    Transaction transaction1 = new Transaction();
    transaction1.setId(1L);
    transaction1.setAdmin(admin1);
    transaction1.setWeight(3);
    transaction1.setNotes("Catatan Transaksi");
    transaction1.setTotalPrice(BigDecimal.valueOf(15000));
    transaction1.setStatus(TransactionStatusEnum.valueOf("NEW"));

    List<Transaction> transactions = new ArrayList<>();
    transactions.add(transaction1);
    Page<Transaction> pagedTransactions = new PageImpl<>(transactions);

    Pageable pageable = PageRequest.of(0, 5);

    when(transactionRepository.findAll(pageable)).thenReturn(pagedTransactions);

    List<TransactionResponseDTO> transactionsToDto = transactions.stream()
            .map(transaction -> modelMapper.map(transaction, TransactionResponseDTO.class))
            .collect(Collectors.toList());

    ResponseWithMetaDTO.Meta responseMeta = new ResponseWithMetaDTO.Meta();
    responseMeta.setPage(pageable.getPageNumber() + 1);
    responseMeta.setPerPage(pageable.getPageSize());
    responseMeta.setTotalPage(pagedTransactions.getTotalPages());
    responseMeta.setTotalData(pagedTransactions.getTotalElements());

    ResponseWithMetaDTO<List<TransactionResponseDTO>> response = new ResponseWithMetaDTO<>();
    response.setData(transactionsToDto);
    response.setMeta(responseMeta);
    response.setStatus(HttpStatus.OK.value());
    response.setMessage("");

    ResponseWithMetaDTO<List<TransactionResponseDTO>> listOfTransactions = serviceUnderTest.getAllTransactions(pageable);

    assertThat(listOfTransactions).isEqualTo(response);
  }

  @Test(expected = ValidationErrorException.class)
  public void getTransaction_givenNullId_then_shouldThrowException() {
    serviceUnderTest.getTransaction(null);
  }

  @Test
  public void getTransaction_givenValidId_then_shouldReturnDetailTransaction() {
    Long requestParamId = 1L;

    List<Transaction> adminTransactions = new ArrayList<>();

    Optional<Admin> admin = Optional.of(new Admin());
    admin.get().setId(1L);
    admin.get().setUsername("kangabbad");
    admin.get().setEmail("email1@email.com");
    admin.get().setPhone("087739999776");
    admin.get().setIdCard("3339837222934645");
    admin.get().setName("Mas Abbad");
    admin.get().setAddress("Laweyan, Solo");
    admin.get().setTransactions(adminTransactions);

    when(adminRepository.findById(anyLong())).thenReturn(admin);

    Optional<Transaction> transaction = Optional.of(new Transaction());
    transaction.get().setId(requestParamId);
    transaction.get().setAdmin(admin.get());
    transaction.get().setWeight(3);
    transaction.get().setTotalPrice(BigDecimal.valueOf(15000));
    transaction.get().setNotes("Catatan Transaksi");
    transaction.get().setStatus(TransactionStatusEnum.valueOf("NEW"));

    ResponseDTO<TransactionResponseDTO> response = new ResponseDTO<>();
    response.setData(convertToDto(transaction.get()));
    response.setStatus(HttpStatus.OK.value());
    response.setMessage("");

    when(transactionRepository.findById(anyLong())).thenReturn(transaction);
    ResponseDTO<TransactionResponseDTO> transactionDetail = serviceUnderTest.getTransaction(requestParamId);

    assertThat(transactionDetail).isEqualTo(response);
  }

  private TransactionResponseDTO convertToDto(Transaction transaction) { return modelMapper.map(transaction, TransactionResponseDTO.class); }
}
