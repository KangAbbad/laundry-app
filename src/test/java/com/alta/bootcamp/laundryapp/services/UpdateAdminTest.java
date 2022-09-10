package com.alta.bootcamp.laundryapp.services;

import com.alta.bootcamp.laundryapp.dto.AdminRequestDTO;
import com.alta.bootcamp.laundryapp.dto.AdminResponseDTO;
import com.alta.bootcamp.laundryapp.dto.ResponseDTO;
import com.alta.bootcamp.laundryapp.entities.Admin;
import com.alta.bootcamp.laundryapp.exceptions.ResourceNotFoundException;
import com.alta.bootcamp.laundryapp.exceptions.ValidationErrorException;
import com.alta.bootcamp.laundryapp.repositories.AdminRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UpdateAdminTest {
  @Mock
  AdminRepository adminRepository;

  @InjectMocks
  AdminService serviceUnderTest = spy(new AdminService());

  ModelMapper modelMapper = spy(new ModelMapper());

  PasswordEncoder passwordEncoder = spy(new BCryptPasswordEncoder());

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test(expected = ValidationErrorException.class)
  public void givenNullIdAndNullRequest_when_updateAdmin_then_shouldThrowException() {
    serviceUnderTest.updateAdmin(null,null);
  }

  @Test(expected = ValidationErrorException.class)
  public void givenNullIdAndValidRequest_when_updateAdmin_then_shouldThrowException() {
    AdminRequestDTO request = new AdminRequestDTO();
    request.setUsername("kangabbad");
    request.setEmail("email1@gmail.com");
    request.setPhone("087739999776");
    request.setName("Naufal Abbad");
    request.setAddress("Laweyan, Solo");
    request.setIdCard("3337201117380007");
    request.setPassword("Waduh");

    serviceUnderTest.updateAdmin(null,request);
  }

  @Test(expected = ResourceNotFoundException.class)
  public void givenValidIdAndRequestNull_when_updateAdmin_then_shouldThrowException() {
    Long requestParamId = 1L;
    serviceUnderTest.updateAdmin(requestParamId,null);
  }

  @Test
  public void givenValidIdAndValidRequest_when_updateAdmin_then_shouldReturnUpdatedAdmin() {
    Long requestParamId = 1L;

    Optional<Admin> oldAdmin = Optional.of(new Admin());
    oldAdmin.get().setId(requestParamId);
    oldAdmin.get().setUsername("kangabbad");
    oldAdmin.get().setEmail("email@email.com");
    oldAdmin.get().setPhone("082147823643");
    oldAdmin.get().setIdCard("8734782637846");
    oldAdmin.get().setName("Mas Naufal");
    oldAdmin.get().setAddress("Laweyan, Solo");
    oldAdmin.get().setPassword("Waduh");

    when(adminRepository.findById(anyLong())).thenReturn(oldAdmin);

    AdminRequestDTO request = new AdminRequestDTO();
    request.setUsername("kangabbad9");
    request.setEmail("email1@email.com");
    request.setPhone("087739999776");
    request.setIdCard("333712123918723");
    request.setName("Mas Abbad");
    request.setAddress("Laweyan, Solo");
    request.setPassword("Waduh");

    Admin newAdmin = modelMapper.map(request, Admin.class);
    AdminResponseDTO newAdminDto = convertToDto(newAdmin);

    when(adminRepository.save(any(Admin.class))).thenReturn(newAdmin);

    ResponseDTO<AdminResponseDTO> updatedAdmin = serviceUnderTest.updateAdmin(requestParamId, request);

    ResponseDTO<AdminResponseDTO> response = new ResponseDTO<>();
    response.setData(newAdminDto);
    response.setStatus(HttpStatus.OK.value());
    response.setMessage("Admin updated successfully");

    assertThat(updatedAdmin).isEqualTo(response);
  }

  private AdminResponseDTO convertToDto(Admin admin) {
    return modelMapper.map(admin, AdminResponseDTO.class);
  }
}
