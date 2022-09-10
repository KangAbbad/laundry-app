package com.alta.bootcamp.laundryapp.services;

import com.alta.bootcamp.laundryapp.dto.*;
import com.alta.bootcamp.laundryapp.entities.Admin;
import com.alta.bootcamp.laundryapp.entities.SummaryRevenue;
import com.alta.bootcamp.laundryapp.exceptions.ResourceNotFoundException;
import com.alta.bootcamp.laundryapp.exceptions.ValidationErrorException;
import com.alta.bootcamp.laundryapp.repositories.AdminRepository;
import com.alta.bootcamp.laundryapp.repositories.SummaryRevenueRepository;
import com.alta.bootcamp.laundryapp.utils.ValidationUtils;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SummaryRevenueService implements ISummaryRevenueService {
  private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

  @Autowired
  ModelMapper modelMapper;

  @Autowired
  SummaryRevenueRepository summaryRevenueRepository;

  @Autowired
  AdminRepository adminRepository;

  @SneakyThrows
  @Override
  @Transactional
  public ResponseDTO<SummaryRevenueResponseDTO> createSummaryRevenue(SummaryRevenueRequestDTO request) {
    ValidationUtils.validateSummaryRevenueRequest(request);

    SummaryRevenue summaryRevenue = convertToEntity(request);
    SummaryRevenue createdSummaryRevenue = summaryRevenueRepository.save(summaryRevenue);

    ResponseDTO<SummaryRevenueResponseDTO> response = new ResponseDTO<>();
    response.setData(convertToDto(createdSummaryRevenue));
    response.setStatus(HttpStatus.CREATED.value());
    response.setMessage("Summary Revenue created successfully");

    logger.info("[POST] /api/v1/summary-revenue - Summary Revenue created successfully");

    return response;
  }

  @Override
  public ResponseWithMetaDTO<List<SummaryRevenueResponseDTO>> getAllSummaryRevenue(Pageable pageable) {
    Page<SummaryRevenue> summaryRevenueList = summaryRevenueRepository.findAll(pageable);

    List<SummaryRevenueResponseDTO> summaryRevenueListToDto = summaryRevenueList.stream()
            .map(summaryRevenue -> modelMapper.map(summaryRevenue, SummaryRevenueResponseDTO.class))
            .collect(Collectors.toList());

    ResponseWithMetaDTO.Meta responseMeta = new ResponseWithMetaDTO.Meta();
    responseMeta.setPage(pageable.getPageNumber() + 1);
    responseMeta.setPerPage(pageable.getPageSize());
    responseMeta.setTotalPage(summaryRevenueList.getTotalPages());
    responseMeta.setTotalData(summaryRevenueList.getTotalElements());

    ResponseWithMetaDTO<List<SummaryRevenueResponseDTO>> response = new ResponseWithMetaDTO<>();
    response.setData(summaryRevenueListToDto);
    response.setMeta(responseMeta);
    response.setStatus(HttpStatus.OK.value());
    response.setMessage("");

    logger.info("[GET] /api/v1/summary-revenue - status " + HttpStatus.OK.value());

    return response;
  }

  @Override
  public ResponseDTO<SummaryRevenueResponseDTO> getSummaryRevenue(Long id) {
    if (id == null) throw new ValidationErrorException("Summary Revenue ID cannot be empty");

    Optional<SummaryRevenue> summaryRevenue = summaryRevenueRepository.findById(id);

    if (summaryRevenue.isPresent()) {
      ResponseDTO<SummaryRevenueResponseDTO> response = new ResponseDTO<>();
      response.setData(convertToDto(summaryRevenue.get()));
      response.setStatus(HttpStatus.OK.value());
      response.setMessage("");

      String logMsg = "[GET] /api/v1/summary-revenue/" + "{" + id + "}" + " - status " + HttpStatus.OK.value();
      logger.info(logMsg);

      return response;
    } else {
      String logMsg = "[GET] /api/v1/summary-revenue/" + "{" + id + "}" + " - Summary Revenue ID not found";
      logger.error(logMsg);
      throw new ResourceNotFoundException("Summary Revenue ID not found");
    }
  }

  @Override
  @Transactional
  public ResponseDTO<List<TodayRevenueDTO>> getTodayRevenue() {
    List<Object[]> todayRevenues = summaryRevenueRepository.getTodayRevenue();

    List<TodayRevenueDTO> todayRevenueToDto = new ArrayList<>();

    todayRevenues.forEach(revenue -> {
      BigInteger adminId = (BigInteger) Arrays.stream(revenue).toList().get(0);
      BigDecimal todayRevenue = (BigDecimal) Arrays.stream(revenue).toList().get(1);

      TodayRevenueDTO adminDailyRevenue = new TodayRevenueDTO();
      adminDailyRevenue.setAdminId(adminId.longValue());
      adminDailyRevenue.setTotalRevenue(todayRevenue);

      todayRevenueToDto.add(adminDailyRevenue);
    });

    ResponseDTO<List<TodayRevenueDTO>> response = new ResponseDTO<>();
    response.setData(todayRevenueToDto);
    response.setStatus(HttpStatus.OK.value());
    response.setMessage("");

    String logMsg = "[GET] /api/v1/summary-revenue/today - status " + HttpStatus.OK.value();
    logger.info(logMsg);

    return response;
  }

  @Override
  public ResponseDTO<SummaryRevenueResponseDTO> updateSummaryRevenue(Long id, SummaryRevenueRequestDTO request) {
    if (id == null) throw new ValidationErrorException("ID cannot be empty");

    Optional<SummaryRevenue> summaryRevenue = summaryRevenueRepository.findById(id);

    if (summaryRevenue.isPresent()) {
      SummaryRevenue tempSummaryRevenue = summaryRevenue.get();

      if (request.getAdminId() != null) {
        Optional<Admin> admin = adminRepository.findById(request.getAdminId());
        if (admin.isPresent()) {
          tempSummaryRevenue.setAdmin(admin.get());
        } else {
          String logMsg = "[PUT] /api/v1/summary-revenue/" + "{" + id + "}" + " - Admin ID not found";
          logger.error(logMsg);
          throw new ResourceNotFoundException("Admin ID not found");
        }
      }

      if (request.getTotalRevenue() != null) {
        tempSummaryRevenue.setTotalRevenue(request.getTotalRevenue());
      }

      SummaryRevenue updatedSummaryRevenue = summaryRevenueRepository.save(tempSummaryRevenue);

      ResponseDTO<SummaryRevenueResponseDTO> response = new ResponseDTO<>();
      response.setData(convertToDto(updatedSummaryRevenue));
      response.setStatus(HttpStatus.OK.value());
      response.setMessage("Summary Revenue updated successfully");

      String logMsg = "[GET] /api/v1/summary-revenue/" + "{" + id + "}" + " - Summary Revenue updated successfully";
      logger.info(logMsg);

      return response;
    } else {
      String logMsg = "[PUT] /api/v1/summary-revenue/" + "{" + id + "}" + " - Summary Revenue ID not found";
      logger.error(logMsg);
      throw new ResourceNotFoundException("Summary Revenue ID not found");
    }
  }

  @Override
  public ResponseDTO<SummaryRevenueResponseDTO> deleteSummaryRevenue(Long id) {
    if (id == null) throw new ValidationErrorException("Summary Revenue ID cannot be empty");

    Optional<SummaryRevenue> summaryRevenue = summaryRevenueRepository.findById(id);

    if (summaryRevenue.isPresent()) {
      summaryRevenueRepository.deleteById(id);

      ResponseDTO<SummaryRevenueResponseDTO> response = new ResponseDTO<>();
      response.setData(null);
      response.setStatus(HttpStatus.OK.value());
      response.setMessage("Summary Revenue ID: " + id + " deleted successfully");

      String logMsg = "[DELETE] /api/v1/summary-revenue/" + "{" + id + "}" + " - Summary Revenue deleted successfully";
      logger.info(logMsg);

      return response;
    } else {
      String logMsg = "[DELETE] /api/v1/summary-revenue/" + "{" + id + "}" + " - Summary Revenue ID not found";
      logger.error(logMsg);
      throw new ResourceNotFoundException("Summary Revenue ID not found");
    }
  }

  private SummaryRevenue convertToEntity(SummaryRevenueRequestDTO request) {
    return modelMapper.map(request, SummaryRevenue.class);
  }

  private SummaryRevenueResponseDTO convertToDto(SummaryRevenue summaryRevenue) {
    return modelMapper.map(summaryRevenue, SummaryRevenueResponseDTO.class);
  }
}
