package com.alta.bootcamp.laundryapp.controller;

import com.alta.bootcamp.laundryapp.dto.*;
import com.alta.bootcamp.laundryapp.repositories.AdminRepository;
import com.alta.bootcamp.laundryapp.services.IAdminService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
  @Autowired
  IAdminService adminService;

  @Autowired
  AdminRepository adminRepository;

  @Autowired
  ModelMapper modelMapper;

  @PostMapping("/signup")
  public ResponseEntity<ResponseDTO<AdminResponseDTO>> createAdmin(@RequestBody AdminRequestDTO request) {
    ResponseDTO<AdminResponseDTO> response = adminService.createAdmin(request);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @PostMapping("/signin")
  public ResponseEntity<ResponseDTO<JwtAuthenticationResponseDTO>> authenticateAdmin(@Valid @RequestBody LoginRequestDTO request) {
    ResponseDTO<JwtAuthenticationResponseDTO> response = adminService.authenticateAdmin(request);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @GetMapping("/me")
  public ResponseEntity<ResponseDTO<AdminResponseDTO>> getMySelf() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Object currentPrincipal = authentication.getPrincipal();

    AdminResponseDTO currentAdmin = modelMapper.map(currentPrincipal, AdminResponseDTO.class);
    ResponseDTO<AdminResponseDTO> response = adminService.getAdmin(currentAdmin.getId());

    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }
}
