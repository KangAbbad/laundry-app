package com.alta.bootcamp.laundryapp.services;

import com.alta.bootcamp.laundryapp.dto.ResponseDTO;
import com.alta.bootcamp.laundryapp.dto.ResponseWithMetaDTO;
import com.alta.bootcamp.laundryapp.dto.SummaryRevenueResponseDTO;
import com.alta.bootcamp.laundryapp.entities.Admin;
import com.alta.bootcamp.laundryapp.entities.SummaryRevenue;
import com.alta.bootcamp.laundryapp.entities.Transaction;
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
public class ReadSummaryRevenueTest {
  @Mock
  SummaryRevenueRepository summaryRevenueRepository;

  @InjectMocks
  SummaryRevenueService serviceUnderTest = spy(new SummaryRevenueService());

  ModelMapper modelMapper = spy(new ModelMapper());

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void getAllSummaryRevenue_then_shouldReturnListOfSummaryRevenue() {
    Admin admin1 = new Admin();
    admin1.setUsername("kangabbad");
    admin1.setEmail("email1@gmail.com");
    admin1.setPhone("087739999776");
    admin1.setName("Naufal Abbad");
    admin1.setAddress("Laweyan, Solo");
    admin1.setIdCard("3337201117380007");
    admin1.setPassword("Waduh");

    SummaryRevenue summaryRevenue1 = new SummaryRevenue();
    summaryRevenue1.setId(1L);
    summaryRevenue1.setAdmin(admin1);
    summaryRevenue1.setTotalRevenue(BigDecimal.valueOf(15000));

    List<SummaryRevenue> summaryRevenueList = new ArrayList<>();
    summaryRevenueList.add(summaryRevenue1);
    Page<SummaryRevenue> pagedSummaryRevenue = new PageImpl<>(summaryRevenueList);

    Pageable pageable = PageRequest.of(0, 5);

    when(summaryRevenueRepository.findAll(pageable)).thenReturn(pagedSummaryRevenue);

    List<SummaryRevenueResponseDTO> summaryRevenueListToDto = summaryRevenueList.stream()
            .map(summaryRevenue -> modelMapper.map(summaryRevenue, SummaryRevenueResponseDTO.class))
            .collect(Collectors.toList());

    ResponseWithMetaDTO.Meta responseMeta = new ResponseWithMetaDTO.Meta();
    responseMeta.setPage(pageable.getPageNumber() + 1);
    responseMeta.setPerPage(pageable.getPageSize());
    responseMeta.setTotalPage(pagedSummaryRevenue.getTotalPages());
    responseMeta.setTotalData(pagedSummaryRevenue.getTotalElements());

    ResponseWithMetaDTO<List<SummaryRevenueResponseDTO>> response = new ResponseWithMetaDTO<>();
    response.setData(summaryRevenueListToDto);
    response.setMeta(responseMeta);
    response.setStatus(HttpStatus.OK.value());
    response.setMessage("");

    ResponseWithMetaDTO<List<SummaryRevenueResponseDTO>> listOfSummaryRevenues = serviceUnderTest.getAllSummaryRevenue(pageable);

    assertThat(listOfSummaryRevenues).isEqualTo(response);
  }

  @Test(expected = ValidationErrorException.class)
  public void getSummaryRevenue_givenNullId_then_shouldThrowException() {
    serviceUnderTest.getSummaryRevenue(null);
  }

  @Test
  public void getSummaryRevenue_givenValidId_then_shouldReturnDetailSummaryRevenue() {
    Long requestParamId = 1L;

    List<Transaction> transactions = new ArrayList<>();

    Optional<Admin> admin = Optional.of(new Admin());
    admin.get().setId(1L);
    admin.get().setUsername("kangabbad");
    admin.get().setEmail("email1@email.com");
    admin.get().setPhone("087739999776");
    admin.get().setIdCard("3339837222934645");
    admin.get().setName("Mas Abbad");
    admin.get().setAddress("Laweyan, Solo");
    admin.get().setTransactions(transactions);

    Optional<SummaryRevenue> summaryRevenue = Optional.of(new SummaryRevenue());
    summaryRevenue.get().setId(requestParamId);
    summaryRevenue.get().setAdmin(admin.get());
    summaryRevenue.get().setTotalRevenue(BigDecimal.valueOf(15000));

    ResponseDTO<SummaryRevenueResponseDTO> response = new ResponseDTO<>();
    response.setData(convertToDto(summaryRevenue.get()));
    response.setStatus(HttpStatus.OK.value());
    response.setMessage("");

    when(summaryRevenueRepository.findById(anyLong())).thenReturn(summaryRevenue);
    ResponseDTO<SummaryRevenueResponseDTO> summaryRevenueDetail = serviceUnderTest.getSummaryRevenue(requestParamId);

    assertThat(summaryRevenueDetail).isEqualTo(response);
  }

  private SummaryRevenueResponseDTO convertToDto(SummaryRevenue summaryRevenue) { return modelMapper.map(summaryRevenue, SummaryRevenueResponseDTO.class); }
}
