package com.alta.bootcamp.laundryapp.services;

import com.alta.bootcamp.laundryapp.dto.SummaryRevenueRequestDTO;
import com.alta.bootcamp.laundryapp.dto.SummaryRevenueResponseDTO;
import com.alta.bootcamp.laundryapp.dto.ResponseDTO;
import com.alta.bootcamp.laundryapp.entities.Admin;
import com.alta.bootcamp.laundryapp.entities.SummaryRevenue;
import com.alta.bootcamp.laundryapp.exceptions.ResourceNotFoundException;
import com.alta.bootcamp.laundryapp.exceptions.ValidationErrorException;
import com.alta.bootcamp.laundryapp.repositories.AdminRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UpdateSummaryRevenueTest {
  @Mock
  SummaryRevenueRepository summaryRevenueRepository;

  @Mock
  AdminRepository adminRepository;

  @InjectMocks
  SummaryRevenueService serviceUnderTest = spy(new SummaryRevenueService());

  ModelMapper modelMapper = spy(new ModelMapper());

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test(expected = ValidationErrorException.class)
  public void givenNullIdAndNullRequest_when_updateSummaryRevenue_then_shouldThrowException() {
    serviceUnderTest.updateSummaryRevenue(null,null);
  }

  @Test(expected = ValidationErrorException.class)
  public void givenNullIdAndValidRequest_when_updateSummaryRevenue_then_shouldThrowException() {
    SummaryRevenueRequestDTO request = new SummaryRevenueRequestDTO();
    request.setAdminId(1L);
    request.setTotalRevenue(BigDecimal.valueOf(15000));

    serviceUnderTest.updateSummaryRevenue(null,request);
  }

  @Test(expected = ResourceNotFoundException.class)
  public void givenValidIdAndRequestNull_when_updateSummaryRevenue_then_shouldThrowException() {
    Long requestParamId = 1L;
    serviceUnderTest.updateSummaryRevenue(requestParamId,null);
  }

  @Test
  public void givenValidIdAndValidRequest_when_updateSummaryRevenue_then_shouldReturnUpdatedSummaryRevenue() {
    Long requestParamId = 1L;

    Optional<Admin> admin1 = Optional.of(new Admin());

    Optional<SummaryRevenue> oldSummaryRevenue = Optional.of(new SummaryRevenue());
    oldSummaryRevenue.get().setId(requestParamId);
    oldSummaryRevenue.get().setAdmin(admin1.get());
    oldSummaryRevenue.get().setTotalRevenue(BigDecimal.valueOf(15000));

    when(summaryRevenueRepository.findById(anyLong())).thenReturn(oldSummaryRevenue);
    when(adminRepository.findById(anyLong())).thenReturn(admin1);

    SummaryRevenueRequestDTO request = new SummaryRevenueRequestDTO();
    request.setAdminId(1L);
    request.setTotalRevenue(BigDecimal.valueOf(15000));

    SummaryRevenue newSummaryRevenue = modelMapper.map(request, SummaryRevenue.class);
    SummaryRevenueResponseDTO newSummaryRevenueDto = convertToDto(newSummaryRevenue);

    when(summaryRevenueRepository.save(any(SummaryRevenue.class))).thenReturn(newSummaryRevenue);

    ResponseDTO<SummaryRevenueResponseDTO> updatedSummaryRevenue = serviceUnderTest.updateSummaryRevenue(requestParamId, request);

    ResponseDTO<SummaryRevenueResponseDTO> response = new ResponseDTO<>();
    response.setData(newSummaryRevenueDto);
    response.setStatus(HttpStatus.OK.value());
    response.setMessage("Summary Revenue updated successfully");

    assertThat(updatedSummaryRevenue).isEqualTo(response);
  }

  private SummaryRevenueResponseDTO convertToDto(SummaryRevenue admin) {
    return modelMapper.map(admin, SummaryRevenueResponseDTO.class);
  }
}
