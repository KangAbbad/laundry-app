package com.alta.bootcamp.laundryapp.controller;

import com.alta.bootcamp.laundryapp.dto.AdminRequestDTO;
import com.alta.bootcamp.laundryapp.dto.AdminResponseDTO;
import com.alta.bootcamp.laundryapp.dto.ResponseDTO;
import com.alta.bootcamp.laundryapp.dto.ResponseWithMetaDTO;
import com.alta.bootcamp.laundryapp.services.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admins")
public class AdminController {
  @Autowired
  IAdminService adminService;

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

  @GetMapping("/download-excel")
  public ResponseEntity<Resource> downloadExcel() {
    String fileName = "admins.xlsx";
    String headerValue = "attachment; filename=" + fileName;
    InputStreamResource excelResource = new InputStreamResource(adminService.downloadExcel());

    return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
            .body(excelResource);

//    ResponseDTO<Resource> response = new ResponseDTO<>();
//    response.setData(excelResource);
//    response.setStatus(HttpStatus.OK.value());
//    response.setMessage("Excel downloaded successfully");

//    HttpHeaders responseHeaders = new HttpHeaders();
//    responseHeaders.add(HttpHeaders.CONTENT_DISPOSITION, headerValue);
//    responseHeaders.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));

//    return new ResponseEntity<>(response, responseHeaders, HttpStatus.valueOf(response.getStatus()));
  }
}

