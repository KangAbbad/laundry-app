package com.alta.bootcamp.laundryapp.services;

import com.alta.bootcamp.laundryapp.dto.*;
import com.alta.bootcamp.laundryapp.entities.Admin;
import com.alta.bootcamp.laundryapp.entities.Role;
import com.alta.bootcamp.laundryapp.enums.RoleName;
import com.alta.bootcamp.laundryapp.exceptions.DataAlreadyExistException;
import com.alta.bootcamp.laundryapp.exceptions.ResourceNotFoundException;
import com.alta.bootcamp.laundryapp.exceptions.ValidationErrorException;
import com.alta.bootcamp.laundryapp.repositories.AdminRepository;
import com.alta.bootcamp.laundryapp.repositories.RoleRepository;
import com.alta.bootcamp.laundryapp.securities.config.JwtTokenProvider;
import com.alta.bootcamp.laundryapp.utils.ValidationUtils;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService implements IAdminService {
  private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

  @Autowired
  ModelMapper modelMapper;

  @Autowired
  AdminRepository adminRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  JwtTokenProvider tokenProvider;

  @SneakyThrows
  @Override
  @Transactional
  public ResponseDTO<AdminResponseDTO> createAdmin(AdminRequestDTO request) {
    ValidationUtils.validateAdminRequest(request);

    if (adminRepository.existsByUsername(request.getUsername())) {
      logger.error("[POST] /api/v1/signup - Username is already exists");
      throw new DataAlreadyExistException("Username is already exists");
    }

    if (adminRepository.existsByEmail(request.getEmail())) {
      logger.error("[POST] /api/v1/signup - Email is already exists");
      throw new DataAlreadyExistException("Email is already exists");
    }

    if (adminRepository.existsByPhone(request.getPhone())) {
      logger.error("[POST] /api/v1/signup - Phone is already exists");
      throw new DataAlreadyExistException("Phone is already exists");
    }

    Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
            .orElseThrow(() -> new ValidationErrorException("Admin role is not set"));

    Admin admin = convertToEntity(request);
    admin.setPassword(passwordEncoder.encode(request.getPassword()));
    admin.setRoles(Collections.singleton(adminRole));

    Admin createdAdmin = adminRepository.save(admin);

    ResponseDTO<AdminResponseDTO> response = new ResponseDTO<>();
    response.setData(convertToDto(createdAdmin));
    response.setStatus(HttpStatus.CREATED.value());
    response.setMessage("Admin created successfully");

    logger.info("[POST] /api/v1/signup - Admin created successfully");

    return response;
  }

  @Override
  public ResponseDTO<JwtAuthenticationResponseDTO> authenticateAdmin(LoginRequestDTO request) {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
            )
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = tokenProvider.generateToken(authentication);

    ResponseDTO<JwtAuthenticationResponseDTO> response = new ResponseDTO<>();
    response.setData(new JwtAuthenticationResponseDTO(jwt));
    response.setStatus(HttpStatus.OK.value());
    response.setMessage("Login successfully");

    logger.info("[POST] /api/v1/signin - Login successfully");

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

    logger.info("[GET] /api/v1/admins - status " + HttpStatus.OK.value());

    return response;
  }

  @Override
  public ResponseDTO<AdminResponseDTO> getAdmin(Long id) {
    Optional<Admin> admin = adminRepository.findById(id);
    if (admin.isPresent()) {
      ResponseDTO<AdminResponseDTO> response = new ResponseDTO<>();
      response.setData(convertToDto(admin.get()));
      response.setStatus(HttpStatus.OK.value());
      response.setMessage("");

      String logMsg = "[GET] /api/v1/admins/" + "{" + id + "}" + " - status " + HttpStatus.OK.value();
      logger.info(logMsg);
      return response;
    } else {
      String logMsg = "[GET] /api/v1/admins/" + "{" + id + "}" + " - Admin ID not found";
      logger.error(logMsg);
      throw new ResourceNotFoundException("Admin ID not found");
    }
  }

  @Override
  @Transactional
  public ResponseDTO<AdminResponseDTO> updateAdmin(Long id, AdminRequestDTO request) {
    if (id == null) throw new ValidationErrorException("ID cannot be empty");

    Optional<Admin> admin = adminRepository.findById(id);

    if (admin.isPresent()) {
      Admin tempAdmin = admin.get();

      if (adminRepository.existsByUsername(request.getUsername()) && !Objects.equals(request.getUsername(), tempAdmin.getUsername())) {
        String logMsg = "[PUT] /api/v1/admins/" + "{" + id + "}" + " - Username is already exists";
        logger.error(logMsg);
        throw new DataAlreadyExistException("Username is already exists");
      } else if (adminRepository.existsByEmail(request.getEmail()) && !Objects.equals(request.getEmail(), tempAdmin.getEmail())) {
        String logMsg = "[PUT] /api/v1/admins/" + "{" + id + "}" + " - Email is already exists";
        logger.error(logMsg);
        throw new DataAlreadyExistException("Email is already exists");
      } else if (adminRepository.existsByPhone(request.getPhone()) && !Objects.equals(request.getPhone(), tempAdmin.getPhone())) {
        String logMsg = "[PUT] /api/v1/admins/" + "{" + id + "}" + " - Phone is already exists";
        logger.error(logMsg);
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

      if (request.getPassword() != null) {
        tempAdmin.setPassword(passwordEncoder.encode(request.getPassword()));
      }

      Admin convertToEntity = modelMapper.map(tempAdmin, Admin.class);
      Admin updatedAdmin = adminRepository.save(convertToEntity);

      ResponseDTO<AdminResponseDTO> response = new ResponseDTO<>();
      response.setData(convertToDto(updatedAdmin));
      response.setStatus(HttpStatus.OK.value());
      response.setMessage("Admin updated successfully");

      String logMsg = "[PUT] /api/v1/admins/" + "{" + id + "}" + " - Admin updated successfully";
      logger.info(logMsg);

      return response;
    } else {
      String logMsg = "[PUT] /api/v1/admins/" + "{" + id + "}" + " - Admin ID not found";
      logger.error(logMsg);
      throw new ResourceNotFoundException("Admin ID not found");
    }
  }

  @Override
  public ResponseDTO<AdminResponseDTO> deleteAdmin(Long id) {
    if (id == null) throw new ValidationErrorException("Admin ID cannot be empty");

    Optional<Admin> admin = adminRepository.findById(id);

    ResponseDTO<AdminResponseDTO> response = new ResponseDTO<>();

    if (admin.isPresent()) {
      adminRepository.deleteById(id);

      response.setData(null);
      response.setStatus(HttpStatus.OK.value());
      response.setMessage("Admin ID: " + id + " (" + admin.get().getUsername() + ") deleted successfully");

      String logMsg = "[DELETE] /api/v1/admins/" + "{" + id + "}" + " - Admin ID: " + id + " (" + admin.get().getUsername() + ") deleted successfully";
      logger.info(logMsg);
    } else {
      String logMsg = "[DELETE] /api/v1/admins/" + "{" + id + "}" + " - Admin ID not found";
      logger.error(logMsg);
      throw new ResourceNotFoundException("Admin ID not found");
    }

    return response;
  }

  @Override
  public ByteArrayInputStream downloadExcel() {
    List<Admin> admins = adminRepository.findAll();
    try (
      Workbook wb = new XSSFWorkbook();
      ByteArrayOutputStream out = new ByteArrayOutputStream()
    ) {
      Sheet sheet1 = wb.createSheet("Sheet1");
      List<String> headers = new ArrayList<>();
      headers.add("User ID");
      headers.add("Username");
      headers.add("Email");
      headers.add("Phone");
      headers.add("ID Card");
      headers.add("Name");
      headers.add("Address");
      headers.add("Join Date");

      CellStyle cellStyle = wb.createCellStyle();
      CreationHelper createHelper = wb.getCreationHelper();
      cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy hh:mm:ss"));

      Row headerRow = sheet1.createRow(0);

      for (int i = 0; i < headers.size(); i++) {
        Cell col = headerRow.createCell(i);
        col.setCellValue(headers.get(i));
      }

      int rowId = 1;
      for (Admin admin : admins) {
        Row row = sheet1.createRow(rowId);
        row.createCell(0, CellType.NUMERIC).setCellValue(admin.getId());
        row.createCell(1, CellType.STRING).setCellValue(admin.getUsername());
        row.createCell(2, CellType.STRING).setCellValue(admin.getEmail());
        row.createCell(3, CellType.STRING).setCellValue(admin.getPhone());
        row.createCell(4, CellType.STRING).setCellValue(admin.getIdCard());
        row.createCell(5, CellType.STRING).setCellValue(admin.getName());
        row.createCell(6, CellType.STRING).setCellValue(admin.getAddress());

        Cell cell7 = row.createCell(7, CellType.STRING);
        cell7.setCellStyle(cellStyle);
        cell7.setCellValue(admin.getCreatedAt());

        rowId++;
      }

      wb.write(out);

      String logMsg = "[GET] /api/v1/admins/download-excel - status " + HttpStatus.OK.value();
      logger.info(logMsg);

      return new ByteArrayInputStream(out.toByteArray());
    } catch (IOError | IOException ioe) {
      ioe.printStackTrace();
      String logMsg = "[GET] /api/v1/admins/download-excel - status " + HttpStatus.INTERNAL_SERVER_ERROR.value();
      logger.info(logMsg);
      return null;
    }
  }

  private Admin convertToEntity(AdminRequestDTO request) {
    return modelMapper.map(request, Admin.class);
  }

  private AdminResponseDTO convertToDto(Admin admin) {
    return modelMapper.map(admin, AdminResponseDTO.class);
  }
}
