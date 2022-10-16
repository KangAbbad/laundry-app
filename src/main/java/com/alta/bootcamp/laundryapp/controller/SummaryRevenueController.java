package com.alta.bootcamp.laundryapp.controller;

import com.alta.bootcamp.laundryapp.dto.*;
import com.alta.bootcamp.laundryapp.services.ISummaryRevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/summary-revenue")
public class SummaryRevenueController {
  @Autowired
  ISummaryRevenueService summaryRevenueService;

  @PostMapping
  public ResponseEntity<ResponseDTO<SummaryRevenueResponseDTO>> createTransaction(@RequestBody SummaryRevenueRequestDTO request) {
    ResponseDTO<SummaryRevenueResponseDTO> response = summaryRevenueService.createSummaryRevenue(request);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @GetMapping
  public ResponseEntity<ResponseWithMetaDTO<List<SummaryRevenueResponseDTO>>> getAllSummaryRevenue(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(name = "per_page", defaultValue = "5") int perPage,
          @RequestParam(defaultValue = "desc") String sort
  ) {
    Pageable pages;
    Sort.Direction sortBy = Sort.Direction.DESC;

    if (Objects.equals(sort, "asc")) {
      sortBy = Sort.Direction.ASC;
    }

    if (page < 1) {
      pages = PageRequest.of(0, perPage, Sort.by(sortBy, "createdAt"));
    } else {
      pages = PageRequest.of(page - 1, perPage, Sort.by(sortBy, "createdAt"));
    }
    ResponseWithMetaDTO<List<SummaryRevenueResponseDTO>> response = summaryRevenueService.getAllSummaryRevenue(pages);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ResponseDTO<SummaryRevenueResponseDTO>> getSummaryRevenue(@PathVariable("id") Long id) {
    ResponseDTO<SummaryRevenueResponseDTO> response = summaryRevenueService.getSummaryRevenue(id);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @GetMapping("/today")
  public ResponseEntity<ResponseDTO<List<TodayRevenueDTO>>> getTodayRevenue() {
    ResponseDTO<List<TodayRevenueDTO>> response = summaryRevenueService.getTodayRevenue();
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ResponseDTO<SummaryRevenueResponseDTO>> updateSummaryRevenue(@PathVariable("id") Long id, @RequestBody SummaryRevenueRequestDTO request) {
    ResponseDTO<SummaryRevenueResponseDTO> response = summaryRevenueService.updateSummaryRevenue(id, request);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ResponseDTO<SummaryRevenueResponseDTO>> deleteSummaryRevenue(@PathVariable("id") Long id) {
    ResponseDTO<SummaryRevenueResponseDTO> response = summaryRevenueService.deleteSummaryRevenue(id);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @GetMapping("/download-excel")
  public ResponseEntity<Resource> downloadExcel() {
    String fileName = "summary-revenue.xlsx";
    String headerValue = "attachment; filename=" + fileName;
    InputStreamResource excelResource = new InputStreamResource(summaryRevenueService.downloadExcel());

    return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
            .body(excelResource);
  }
}
