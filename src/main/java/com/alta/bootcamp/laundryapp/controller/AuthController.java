package com.alta.bootcamp.laundryapp.controller;

import com.alta.bootcamp.laundryapp.dto.*;
import com.alta.bootcamp.laundryapp.repositories.AdminRepository;
import com.alta.bootcamp.laundryapp.services.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
  @Autowired
  IAdminService adminService;

  @Autowired
  AdminRepository adminRepository;

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
}
