package com.alta.bootcamp.laundryapp.services;

import com.alta.bootcamp.laundryapp.dto.AdminRequestDTO;
import com.alta.bootcamp.laundryapp.dto.AdminResponseDTO;
import com.alta.bootcamp.laundryapp.dto.ResponseDTO;
import com.alta.bootcamp.laundryapp.dto.ResponseWithMetaDTO;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAdminService {
  ResponseDTO<AdminResponseDTO> createAdmin(AdminRequestDTO request);
  ResponseWithMetaDTO<List<AdminResponseDTO>> getAllAdmins(Pageable pageable);
  ResponseDTO<AdminResponseDTO> getAdmin(Long id);
  ResponseDTO<AdminResponseDTO> updateAdmin(Long id, AdminRequestDTO request);
  ResponseDTO<AdminResponseDTO> deleteAdmin(Long id);
  ResponseDTO<String> downloadExcel();
}
