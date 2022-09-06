package com.alta.bootcamp.laundryapp.unittest.services.admin;

import com.alta.bootcamp.laundryapp.dto.AdminRequestDTO;
import com.alta.bootcamp.laundryapp.dto.AdminResponseDTO;
import com.alta.bootcamp.laundryapp.dto.ResponseDTO;
import com.alta.bootcamp.laundryapp.entities.Admin;
import com.alta.bootcamp.laundryapp.exceptions.ResourceNotFoundException;
import com.alta.bootcamp.laundryapp.exceptions.ValidationErrorException;
import com.alta.bootcamp.laundryapp.repositories.AdminRepository;
import com.alta.bootcamp.laundryapp.services.AdminService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdateAdminTest {
  @Mock
  AdminRepository adminRepository;

  @InjectMocks
  AdminService serviceUnderTest = spy(new AdminService());

  ModelMapper modelMapper = spy(new ModelMapper());

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

    Optional<Admin> admin = Optional.of(new Admin());
    admin.get().setUsername("kangabbad");
    admin.get().setEmail("email1@gmail.com");
    admin.get().setPhone("087739999776");
    admin.get().setName("Naufal Abbad");
    admin.get().setAddress("Laweyan, Solo");
    admin.get().setIdCard("3337201117380007");
    admin.get().setPassword("Waduh");

    when(adminRepository.findById(requestParamId)).thenReturn(admin);

    AdminRequestDTO request = new AdminRequestDTO();
    request.setUsername("kangabbad");
    request.setEmail("email1@gmail.com");
    request.setPhone("087739999776");
    request.setName("Naufal Abbad");
    request.setAddress("Laweyan, Solo");
    request.setIdCard("3337201117380007");
    request.setPassword("Waduh");

    Admin futureDataAdmin = modelMapper.map(request, Admin.class);
    futureDataAdmin.setId(requestParamId);

    ResponseDTO<AdminResponseDTO> response = new ResponseDTO<>();
    response.setData(convertToDto(futureDataAdmin));
    response.setStatus(HttpStatus.OK.value());
    response.setMessage("Admin updated successfully");

    when(adminRepository.save(any(Admin.class))).thenReturn(futureDataAdmin);

    ResponseDTO<AdminResponseDTO> updatedAdmin = serviceUnderTest.updateAdmin(requestParamId, request);

    assertThat(updatedAdmin).isEqualTo(response);
  }

  private AdminResponseDTO convertToDto(Admin admin) {
    return modelMapper.map(admin, AdminResponseDTO.class);
  }
}
