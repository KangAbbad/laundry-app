package com.alta.bootcamp.laundryapp.unittest.services.admin;

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

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DeleteAdminTest {
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
  public void givenNullId_when_deleteAdmin_then_shouldThrowException() {
    serviceUnderTest.deleteAdmin(null);
  }

  @Test
  public void givenValidId_when_deleteAdmin_then_shouldDeleteAdmin() {
    Long requestParamId = 1L;

    Optional<Admin> admin = Optional.of(new Admin());
    admin.get().setId(requestParamId);

    when(adminRepository.findById(requestParamId)).thenReturn(admin);

    serviceUnderTest.deleteAdmin(requestParamId);

    verify(adminRepository).deleteById(requestParamId);
  }
}
