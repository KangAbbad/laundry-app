package com.alta.bootcamp.laundryapp.unittest.services.admin;

import com.alta.bootcamp.laundryapp.dto.AdminResponseDTO;
import com.alta.bootcamp.laundryapp.dto.ResponseDTO;
import com.alta.bootcamp.laundryapp.dto.ResponseWithMetaDTO;
import com.alta.bootcamp.laundryapp.entities.Admin;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReadAdminTest {
  @Mock
  AdminRepository adminRepository;

  @InjectMocks
  AdminService serviceUnderTest = spy(new AdminService());

  ModelMapper modelMapper = spy(new ModelMapper());

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void getAllAdmins_then_shouldReturnListOfAdmins() {
    Admin admin1 = new Admin();
    admin1.setUsername("kangabbad");
    admin1.setEmail("email1@gmail.com");
    admin1.setPhone("087739999776");
    admin1.setName("Naufal Abbad");
    admin1.setAddress("Laweyan, Solo");
    admin1.setIdCard("3337201117380007");
    admin1.setPassword("Waduh");

    List<Admin> admins = new ArrayList<>();
    admins.add(admin1);
    Page<Admin> pagedAdmins = new PageImpl<>(admins);

    Pageable pageable = PageRequest.of(0, 5);

    when(adminRepository.findAll(pageable)).thenReturn(pagedAdmins);

    List<AdminResponseDTO> adminsToDto = admins.stream()
            .map(admin -> modelMapper.map(admin, AdminResponseDTO.class))
            .collect(Collectors.toList());

    ResponseWithMetaDTO.Meta responseMeta = new ResponseWithMetaDTO.Meta();
    responseMeta.setPage(pageable.getPageNumber() + 1);
    responseMeta.setPerPage(pageable.getPageSize());
    responseMeta.setTotalPage(pagedAdmins.getTotalPages());
    responseMeta.setTotalData(pagedAdmins.getTotalElements());

    ResponseWithMetaDTO<List<AdminResponseDTO>> response = new ResponseWithMetaDTO<>();
    response.setData(adminsToDto);
    response.setMeta(responseMeta);
    response.setStatus(HttpStatus.OK.value());
    response.setMessage("");

    ResponseWithMetaDTO<List<AdminResponseDTO>> listOfAdmins = serviceUnderTest.getAllAdmins(pageable);

    assertThat(listOfAdmins).isEqualTo(response);
  }

  @Test
  public void getAdminDetail_then_shouldReturnDetailOfAdmin() {
    Long requestParamId = 1L;

    Optional<Admin> admin1 = Optional.of(new Admin());
    admin1.get().setId(requestParamId);
    admin1.get().setUsername("kangabbad");
    admin1.get().setEmail("email1@gmail.com");
    admin1.get().setPhone("087739999776");
    admin1.get().setName("Naufal Abbad");
    admin1.get().setAddress("Laweyan, Solo");
    admin1.get().setIdCard("3337201117380007");
    admin1.get().setPassword("Waduh");

    when(adminRepository.findById(requestParamId)).thenReturn(admin1);

    Admin admin2 = new Admin();
    admin2.setId(requestParamId);
    admin2.setUsername("kangabbad");
    admin2.setEmail("email1@gmail.com");
    admin2.setPhone("087739999776");
    admin2.setName("Naufal Abbad");
    admin2.setAddress("Laweyan, Solo");
    admin2.setIdCard("3337201117380007");
    admin2.setPassword("Waduh");

    ResponseDTO<AdminResponseDTO> response = new ResponseDTO<>();
    response.setData(convertToDto(admin2));
    response.setStatus(HttpStatus.OK.value());
    response.setMessage("");

    ResponseDTO<AdminResponseDTO> adminDetail = serviceUnderTest.getAdmin(requestParamId);

    assertThat(adminDetail).isEqualTo(response);
  }

  private AdminResponseDTO convertToDto(Admin admin) {
    return modelMapper.map(admin, AdminResponseDTO.class);
  }
}
