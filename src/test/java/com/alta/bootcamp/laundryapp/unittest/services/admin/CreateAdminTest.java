package com.alta.bootcamp.laundryapp.unittest.services.admin;

import com.alta.bootcamp.laundryapp.dto.AdminRequestDTO;
import com.alta.bootcamp.laundryapp.dto.AdminResponseDTO;
import com.alta.bootcamp.laundryapp.dto.ResponseDTO;
import com.alta.bootcamp.laundryapp.entities.Admin;
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

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreateAdminTest {
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
  public void givenNullRequest_when_createNewAdmin_then_shouldThrowException() {
    serviceUnderTest.createAdmin(null);
  }

  @Test
  public void givenValidRequest_when_createNewAdmin_then_adminShouldBeCreated() {
    AdminRequestDTO request = new AdminRequestDTO();
    request.setUsername("kangabbad");
    request.setEmail("email1@gmail.com");
    request.setPhone("087739999776");
    request.setName("Naufal Abbad");
    request.setAddress("Laweyan, Solo");
    request.setIdCard("3337201117380007");
    request.setPassword("Waduh");

    Admin newAdmin = modelMapper.map(request, Admin.class);
    newAdmin.setId(1L);

    ResponseDTO<AdminResponseDTO> response = new ResponseDTO<>();
    response.setData(convertToDto(newAdmin));
    response.setStatus(HttpStatus.CREATED.value());
    response.setMessage("Admin created successfully");

    when(adminRepository.save(any(Admin.class))).thenReturn(newAdmin);
    ResponseDTO<AdminResponseDTO> createdAdmin = serviceUnderTest.createAdmin(request);

    assertThat(createdAdmin).isEqualTo(response);
  }

  @Test(expected = ValidationErrorException.class)
  public void givenEmptyUsername_when_createNewAdmin_then_shouldThrowException() {
    AdminRequestDTO request = new AdminRequestDTO();
    request.setUsername("");
    request.setEmail("email1@gmail.com");
    request.setPhone("087739999776");
    request.setName("Naufal Abbad");
    request.setAddress("Laweyan, Solo");
    request.setIdCard("3337201117380007");
    request.setPassword("Waduh");

    Admin newAdmin = modelMapper.map(request, Admin.class);
    newAdmin.setId(1L);

    serviceUnderTest.createAdmin(request);
  }

  @Test(expected = ValidationErrorException.class)
  public void givenEmptyEmail_when_createNewAdmin_then_shouldThrowException() {
    AdminRequestDTO request = new AdminRequestDTO();
    request.setUsername("kangabbad");
    request.setEmail("");
    request.setPhone("087739999776");
    request.setName("Naufal Abbad");
    request.setAddress("Laweyan, Solo");
    request.setIdCard("3337201117380007");
    request.setPassword("Waduh");

    Admin newAdmin = modelMapper.map(request, Admin.class);
    newAdmin.setId(1L);

    serviceUnderTest.createAdmin(request);
  }

  @Test(expected = ValidationErrorException.class)
  public void givenEmptyPhone_when_createNewAdmin_then_shouldThrowException() {
    AdminRequestDTO request = new AdminRequestDTO();
    request.setUsername("kangabbad");
    request.setEmail("email1@email.com");
    request.setPhone("");
    request.setName("Naufal Abbad");
    request.setAddress("Laweyan, Solo");
    request.setIdCard("3337201117380007");
    request.setPassword("Waduh");

    Admin newAdmin = modelMapper.map(request, Admin.class);
    newAdmin.setId(1L);

    serviceUnderTest.createAdmin(request);
  }

  @Test(expected = ValidationErrorException.class)
  public void givenEmptyIDCard_when_createNewAdmin_then_shouldThrowException() {
    AdminRequestDTO request = new AdminRequestDTO();
    request.setUsername("kangabbad");
    request.setEmail("email1@email.com");
    request.setPhone("087739999776");
    request.setName("Naufal Abbad");
    request.setAddress("Laweyan, Solo");
    request.setIdCard("");
    request.setPassword("Waduh");

    Admin newAdmin = modelMapper.map(request, Admin.class);
    newAdmin.setId(1L);

    serviceUnderTest.createAdmin(request);
  }

  private AdminResponseDTO convertToDto(Admin admin) {
    return modelMapper.map(admin, AdminResponseDTO.class);
  }
}
