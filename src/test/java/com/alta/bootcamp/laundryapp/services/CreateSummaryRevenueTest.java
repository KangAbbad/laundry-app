package com.alta.bootcamp.laundryapp.services;

import com.alta.bootcamp.laundryapp.dto.*;
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
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreateSummaryRevenueTest {
  @Mock
  SummaryRevenueRepository summaryRevenueRepository;

  @InjectMocks
  SummaryRevenueService serviceUnderTest = spy(new SummaryRevenueService());

  ModelMapper modelMapper = spy(new ModelMapper());

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test(expected = ValidationErrorException.class)
  public void givenNullRequest_when_createNewSummaryRevenue_then_shouldThrowException() {
    serviceUnderTest.createSummaryRevenue(null);
  }

  @Test
  public void givenValidRequest_when_createNewSummaryRevenue_then_summaryRevenueShouldBeCreated() {
    Long requestAdminId = 1L;

    SummaryRevenueRequestDTO request = new SummaryRevenueRequestDTO();
    request.setAdminId(requestAdminId);
    request.setTotalRevenue(BigDecimal.valueOf(15000));

    SummaryRevenue newSummaryRevenue = modelMapper.map(request, SummaryRevenue.class);

    ResponseDTO<SummaryRevenueResponseDTO> response = new ResponseDTO<>();
    response.setData(convertToDto(newSummaryRevenue));
    response.setStatus(HttpStatus.CREATED.value());
    response.setMessage("Summary Revenue created successfully");

    when(summaryRevenueRepository.save(any(SummaryRevenue.class))).thenReturn(newSummaryRevenue);
    ResponseDTO<SummaryRevenueResponseDTO> createdSummaryRevenue = serviceUnderTest.createSummaryRevenue(request);

    assertThat(createdSummaryRevenue).isEqualTo(response);
  }

  @Test(expected = ValidationErrorException.class)
  public void givenEmptyAdminId_when_createNewSummaryRevenue_then_shouldThrowException() {
    SummaryRevenueRequestDTO request = new SummaryRevenueRequestDTO();
    request.setAdminId(null);
    request.setTotalRevenue(BigDecimal.valueOf(15000));

    SummaryRevenue newSummaryRevenue = modelMapper.map(request, SummaryRevenue.class);
    newSummaryRevenue.setId(1L);

    serviceUnderTest.createSummaryRevenue(request);
  }

  @Test(expected = ValidationErrorException.class)
  public void givenEmptyTotalRevenue_when_createNewSummaryRevenue_then_shouldThrowException() {
    SummaryRevenueRequestDTO request = new SummaryRevenueRequestDTO();
    request.setAdminId(1L);
    request.setTotalRevenue(null);

    SummaryRevenue newSummaryRevenue = modelMapper.map(request, SummaryRevenue.class);
    newSummaryRevenue.setId(1L);

    serviceUnderTest.createSummaryRevenue(request);
  }

  private SummaryRevenueResponseDTO convertToDto(SummaryRevenue summaryRevenue) {
    return modelMapper.map(summaryRevenue, SummaryRevenueResponseDTO.class);
  }
}
