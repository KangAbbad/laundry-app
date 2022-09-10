package com.alta.bootcamp.laundryapp.services;

import com.alta.bootcamp.laundryapp.dto.*;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface ISummaryRevenueService {
  ResponseDTO<SummaryRevenueResponseDTO> createSummaryRevenue(SummaryRevenueRequestDTO request);
  ResponseWithMetaDTO<List<SummaryRevenueResponseDTO>> getAllSummaryRevenue(Pageable pageable);
  ResponseDTO<SummaryRevenueResponseDTO> getSummaryRevenue(Long id);
  ResponseDTO<SummaryRevenueResponseDTO> updateSummaryRevenue(Long id, SummaryRevenueRequestDTO request);
  ResponseDTO<SummaryRevenueResponseDTO> deleteSummaryRevenue(Long id);
  ResponseDTO<List<TodayRevenueDTO>> getTodayRevenue();
  ByteArrayInputStream downloadExcel();
}
