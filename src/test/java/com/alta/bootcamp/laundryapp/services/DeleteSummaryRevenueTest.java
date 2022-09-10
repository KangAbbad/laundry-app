package com.alta.bootcamp.laundryapp.services;

import com.alta.bootcamp.laundryapp.dto.ResponseDTO;
import com.alta.bootcamp.laundryapp.dto.SummaryRevenueResponseDTO;
import com.alta.bootcamp.laundryapp.entities.SummaryRevenue;
import com.alta.bootcamp.laundryapp.exceptions.ValidationErrorException;
import com.alta.bootcamp.laundryapp.repositories.SummaryRevenueRepository;
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
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeleteSummaryRevenueTest {
  @Mock
  SummaryRevenueRepository transactionRepository;

  @InjectMocks
  SummaryRevenueService serviceUnderTest = spy(new SummaryRevenueService());

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test(expected = ValidationErrorException.class)
  public void givenNullId_when_deleteSummaryRevenue_then_shouldThrowException() {
    serviceUnderTest.deleteSummaryRevenue(null);
  }

  @Test
  public void givenValidId_when_deleteSummaryRevenue_then_shouldDeleteSummaryRevenue() {
    Long requestParamId = 1L;

    Optional<SummaryRevenue> transaction = Optional.of(new SummaryRevenue());
    transaction.get().setId(1L);

    when(transactionRepository.findById(anyLong())).thenReturn(transaction);

    ResponseDTO<SummaryRevenueResponseDTO> deletedSummaryRevenue = serviceUnderTest.deleteSummaryRevenue(requestParamId);

    ResponseDTO<SummaryRevenueResponseDTO> response = new ResponseDTO<>();
    response.setData(null);
    response.setStatus(HttpStatus.OK.value());
    response.setMessage("Summary Revenue ID: " + requestParamId + " deleted successfully");

    assertThat(deletedSummaryRevenue).isEqualTo(response);
  }
}
