package com.alta.bootcamp.laundryapp.services;

import com.alta.bootcamp.laundryapp.dto.AdminResponseDTO;
import com.alta.bootcamp.laundryapp.dto.ResponseDTO;
import com.alta.bootcamp.laundryapp.entities.Admin;
import com.alta.bootcamp.laundryapp.exceptions.ValidationErrorException;
import com.alta.bootcamp.laundryapp.repositories.AdminRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DeleteAdminTest {
  @Mock
  AdminRepository adminRepository;

  @InjectMocks
  AdminService serviceUnderTest = spy(new AdminService());

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test(expected = ValidationErrorException.class)
  public void givenNullId_when_deleteAdmin_then_shouldThrowException() {
    serviceUnderTest.deleteAdmin(null);
  }

  @Test
  public void givenValidId_when_deleteAdmin_then_shouldDeleteAdmin() {
    Long requestParamId = 1L;

    Optional<Admin> admin = Optional.of(new Admin());
    admin.get().setId(1L);
    admin.get().setUsername("Admin 01");

    when(adminRepository.findById(anyLong())).thenReturn(admin);

    ResponseDTO<AdminResponseDTO> deletedAdmin = serviceUnderTest.deleteAdmin(requestParamId);

    ResponseDTO<AdminResponseDTO> response = new ResponseDTO<>();
    response.setData(null);
    response.setStatus(HttpStatus.NO_CONTENT.value());
    response.setMessage("Admin ID: " + requestParamId + " (" + admin.get().getUsername() + ") deleted successfully");

    assertThat(deletedAdmin).isEqualTo(response);
  }
}
