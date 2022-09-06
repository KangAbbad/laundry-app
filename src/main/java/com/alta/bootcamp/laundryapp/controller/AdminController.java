package com.alta.bootcamp.laundryapp.controller;

import com.alta.bootcamp.laundryapp.dto.AdminRequestDTO;
import com.alta.bootcamp.laundryapp.dto.AdminResponseDTO;
import com.alta.bootcamp.laundryapp.dto.ResponseDTO;
import com.alta.bootcamp.laundryapp.dto.ResponseWithMetaDTO;
import com.alta.bootcamp.laundryapp.services.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admins")
public class AdminController {
  @Autowired
  IAdminService adminService;

  @PostMapping
  public ResponseEntity<ResponseDTO<AdminResponseDTO>> createAdmin(@RequestBody AdminRequestDTO request) {
    ResponseDTO<AdminResponseDTO> response = adminService.createAdmin(request);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @GetMapping
  public ResponseEntity<ResponseWithMetaDTO<List<AdminResponseDTO>>> getAllAdmins(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(name = "per_page", defaultValue = "5") int perPage
  ) {
    Pageable pages;
    if (page < 1) {
      pages = PageRequest.of(0, perPage);
    } else {
      pages = PageRequest.of(page - 1, perPage);
    }
    ResponseWithMetaDTO<List<AdminResponseDTO>> response = adminService.getAllAdmins(pages);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ResponseDTO<AdminResponseDTO>> getAdmin(@PathVariable("id") Long id) {
    ResponseDTO<AdminResponseDTO> response = adminService.getAdmin(id);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ResponseDTO<AdminResponseDTO>> updateAdmin(@PathVariable("id") Long id, @RequestBody AdminRequestDTO request) {
    ResponseDTO<AdminResponseDTO> response = adminService.updateAdmin(id, request);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ResponseDTO<AdminResponseDTO>> deleteAdmin(@PathVariable("id") Long id) {
    ResponseDTO<AdminResponseDTO> response = adminService.deleteAdmin(id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}

