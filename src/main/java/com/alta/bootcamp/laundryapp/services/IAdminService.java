package com.alta.bootcamp.laundryapp.services;

import com.alta.bootcamp.laundryapp.dto.AdminRequestDTO;
import com.alta.bootcamp.laundryapp.dto.AdminResponseDTO;
import com.alta.bootcamp.laundryapp.dto.ResponseDTO;

import java.util.List;

public interface IAdminService {
  ResponseDTO createAdmin(AdminRequestDTO request);
  ResponseDTO getAllAdmins();
  ResponseDTO getAdmin(Long id);
  ResponseDTO updateAdmin(Long id, AdminRequestDTO request);
  ResponseDTO deleteAdmin(Long id);
}
