package com.alta.bootcamp.laundryapp.controller;

import com.alta.bootcamp.laundryapp.dto.AdminRequestDTO;
import com.alta.bootcamp.laundryapp.dto.AdminResponseDTO;
import com.alta.bootcamp.laundryapp.dto.ResponseDTO;
import com.alta.bootcamp.laundryapp.services.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
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
  public ResponseEntity<ResponseDTO> createAdmin(@RequestBody AdminRequestDTO request) {
    ResponseDTO response = adminService.createAdmin(request);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @GetMapping
  public ResponseEntity<ResponseDTO> getAllAdmins() {
    ResponseDTO response = adminService.getAllAdmins();
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ResponseDTO> getAdmin(@PathVariable("id") Long id) {
    ResponseDTO response = adminService.getAdmin(id);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ResponseDTO> updateAdmin(@PathVariable("id") Long id, @RequestBody AdminRequestDTO request) {
    ResponseDTO response = adminService.updateAdmin(id, request);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ResponseDTO> deleteAdmin(@PathVariable("id") Long id) {
    ResponseDTO response = adminService.deleteAdmin(id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
