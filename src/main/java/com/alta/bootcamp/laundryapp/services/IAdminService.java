package com.alta.bootcamp.laundryapp.services;

import com.alta.bootcamp.laundryapp.dto.*;

import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface IAdminService {
  ResponseDTO<AdminResponseDTO> createAdmin(AdminRequestDTO request);
  ResponseDTO<JwtAuthenticationResponseDTO> authenticateAdmin(LoginRequestDTO request);
  ResponseWithMetaDTO<List<AdminResponseDTO>> getAllAdmins(Pageable pageable);
  ResponseDTO<AdminResponseDTO> getAdmin(Long id);
  ResponseDTO<AdminResponseDTO> updateAdmin(Long id, AdminRequestDTO request);
  ResponseDTO<AdminResponseDTO> deleteAdmin(Long id);
  ByteArrayInputStream downloadExcel();
}
