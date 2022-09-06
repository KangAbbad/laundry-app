package com.alta.bootcamp.laundryapp.services;

import com.alta.bootcamp.laundryapp.dto.AdminRequestDTO;
import com.alta.bootcamp.laundryapp.dto.AdminResponseDTO;
import com.alta.bootcamp.laundryapp.dto.ResponseDTO;
import com.alta.bootcamp.laundryapp.dto.ResponseWithMetaDTO;
import com.alta.bootcamp.laundryapp.entities.Admin;
import com.alta.bootcamp.laundryapp.exceptions.DataAlreadyExistException;
import com.alta.bootcamp.laundryapp.exceptions.ResourceNotFoundException;
import com.alta.bootcamp.laundryapp.repositories.AdminRepository;
import com.alta.bootcamp.laundryapp.utils.ValidationUtils;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminService implements IAdminService {
  @Autowired
  ModelMapper modelMapper;
  @Autowired
  AdminRepository adminRepository;

  @SneakyThrows
  @Override
  @Transactional
  public ResponseDTO<AdminResponseDTO> createAdmin(AdminRequestDTO request) {
    ValidationUtils.validateAdminRequest(request);

    Admin admin = convertToEntity(request);
    Optional<Admin> adminUsername = adminRepository.findByUsername(request.getUsername());
    Optional<Admin> adminEmail = adminRepository.findByEmail(request.getEmail());
    Optional<Admin> adminPhone = adminRepository.findByPhone(request.getPhone());

    ResponseDTO<AdminResponseDTO> response = new ResponseDTO<>();

    if (adminUsername.isPresent()) {
      throw new DataAlreadyExistException("Username is already exists");
    } else if (adminEmail.isPresent()) {
      throw new DataAlreadyExistException("Email is already exists");
    } else if (adminPhone.isPresent()) {
      throw new DataAlreadyExistException("Phone is already exists");
    }

    Admin createdAdmin = adminRepository.save(admin);

    response.setData(convertToDto(Optional.of(createdAdmin)));
    response.setStatus(HttpStatus.CREATED.value());
    response.setMessage("Admin created successfully");

    return response;
  }

  @Override
  public ResponseWithMetaDTO<List<AdminResponseDTO>> getAllAdmins(Pageable pageable) {
    Page<Admin> admins = adminRepository.findAll(pageable);

    List<AdminResponseDTO> adminsToDto = admins.stream()
            .map(admin -> modelMapper.map(admin, AdminResponseDTO.class))
            .collect(Collectors.toList());

    ResponseWithMetaDTO.Meta responseMeta = new ResponseWithMetaDTO.Meta();
    responseMeta.setPage(pageable.getPageNumber() + 1);
    responseMeta.setPerPage(pageable.getPageSize());
    responseMeta.setTotalPage(admins.getTotalPages());
    responseMeta.setTotalData(admins.getTotalElements());

    ResponseWithMetaDTO<List<AdminResponseDTO>> response = new ResponseWithMetaDTO<>();
    response.setData(adminsToDto);
    response.setMeta(responseMeta);
    response.setStatus(HttpStatus.OK.value());
    response.setMessage("");

    return response;
  }

  @Override
  public ResponseDTO<AdminResponseDTO> getAdmin(Long id) {
    Optional<Admin> admin = adminRepository.findById(id);
    if (admin.isPresent()) {
      ResponseDTO<AdminResponseDTO> response = new ResponseDTO<>();
      response.setData(convertToDto(admin));
      response.setStatus(HttpStatus.OK.value());
      response.setMessage("");
      return response;
    } else {
      throw new ResourceNotFoundException("Admin ID not found");
    }
  }

  @Override
  public ResponseDTO<AdminResponseDTO> updateAdmin(Long id, AdminRequestDTO request) {
    Optional<Admin> admin = adminRepository.findById(id);

    if (admin.isPresent()) {
      Admin tempAdmin = admin.get();

      Optional<Admin> adminUsername = adminRepository.findByUsername(request.getUsername());
      Optional<Admin> adminEmail = adminRepository.findByEmail(request.getEmail());
      Optional<Admin> adminPhone = adminRepository.findByPhone(request.getPhone());

      if (adminUsername.isPresent() && !Objects.equals(request.getUsername(), tempAdmin.getUsername())) {
        throw new DataAlreadyExistException("Username is already exists");
      } else if (adminEmail.isPresent() && !Objects.equals(request.getEmail(), tempAdmin.getEmail())) {
        throw new DataAlreadyExistException("Email is already exists");
      } else if (adminPhone.isPresent() && !Objects.equals(request.getPhone(), tempAdmin.getPhone())) {
        throw new DataAlreadyExistException("Phone is already exists");
      }

      if (request.getUsername() != null) {
        tempAdmin.setUsername(request.getUsername());
      }

      if (request.getEmail() != null) {
        tempAdmin.setEmail(request.getEmail());
      }

      if (request.getPhone() != null) {
        tempAdmin.setPhone(request.getPhone());
      }

      if (request.getIdCard() != null) {
        tempAdmin.setIdCard(request.getIdCard());
      }

      if (request.getName() != null) {
        tempAdmin.setName(request.getName());
      }

      if (request.getAddress() != null) {
        tempAdmin.setAddress(request.getAddress());
      }

      Admin convertToEntity = modelMapper.map(tempAdmin, Admin.class);
      Admin updatedAdmin = adminRepository.save(convertToEntity);

      ResponseDTO<AdminResponseDTO> response = new ResponseDTO<>();
      response.setData(convertToDto(Optional.of(updatedAdmin)));
      response.setStatus(HttpStatus.OK.value());
      response.setMessage("Admin updated successfully");

      return response;
    } else {
      throw new ResourceNotFoundException("Admin ID not found");
    }
  }

  @Override
  public ResponseDTO<AdminResponseDTO> deleteAdmin(Long id) {
    Optional<Admin> admin = adminRepository.findById(id);

    ResponseDTO<AdminResponseDTO> response = new ResponseDTO<>();

    if (admin.isPresent()) {
      adminRepository.deleteById(id);

      response.setData(null);
      response.setStatus(HttpStatus.NO_CONTENT.value());
      response.setMessage("Admin ID: " + id + " (" + admin.get().getUsername() + ") deleted successfully");
    } else {
      throw new ResourceNotFoundException("Admin ID not found");
    }

    return response;
  }

  private Admin convertToEntity(AdminRequestDTO request) {
    return modelMapper.map(request, Admin.class);
  }

  private AdminResponseDTO convertToDto(Optional<Admin> admin) {
    return modelMapper.map(admin, AdminResponseDTO.class);
  }
}
