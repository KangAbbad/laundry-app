package com.alta.bootcamp.laundryapp.services;

import com.alta.bootcamp.laundryapp.dto.AdminRequestDTO;
import com.alta.bootcamp.laundryapp.dto.AdminResponseDTO;
import com.alta.bootcamp.laundryapp.dto.ResponseDTO;
import com.alta.bootcamp.laundryapp.entities.Admin;
import com.alta.bootcamp.laundryapp.entities.Role;
import com.alta.bootcamp.laundryapp.enums.RoleName;
import com.alta.bootcamp.laundryapp.exceptions.ValidationErrorException;
import com.alta.bootcamp.laundryapp.repositories.AdminRepository;
import com.alta.bootcamp.laundryapp.repositories.RoleRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CreateAdminTest {
  @Mock
  AdminRepository adminRepository;

  @Mock
  RoleRepository roleRepository;

  @InjectMocks
  AdminService serviceUnderTest = spy(new AdminService());

  ModelMapper modelMapper = spy(new ModelMapper());

  PasswordEncoder passwordEncoder = spy(new BCryptPasswordEncoder());

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

    Optional<Role> adminRole = Optional.of(new Role());
    adminRole.get().setId(1L);
    adminRole.get().setName(RoleName.ROLE_ADMIN);

    when(roleRepository.findByName(RoleName.ROLE_ADMIN)).thenReturn(adminRole);

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
