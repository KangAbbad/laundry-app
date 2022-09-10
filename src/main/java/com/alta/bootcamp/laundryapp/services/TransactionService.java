package com.alta.bootcamp.laundryapp.services;

import com.alta.bootcamp.laundryapp.dto.*;
import com.alta.bootcamp.laundryapp.entities.Admin;
import com.alta.bootcamp.laundryapp.entities.Transaction;
import com.alta.bootcamp.laundryapp.enums.TransactionStatusEnum;
import com.alta.bootcamp.laundryapp.exceptions.ResourceNotFoundException;
import com.alta.bootcamp.laundryapp.exceptions.ValidationErrorException;
import com.alta.bootcamp.laundryapp.repositories.AdminRepository;
import com.alta.bootcamp.laundryapp.repositories.TransactionRepository;
import com.alta.bootcamp.laundryapp.utils.ValidationUtils;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService implements ITransactionService {
  private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

  @Autowired
  ModelMapper modelMapper;

  @Autowired
  TransactionRepository transactionRepository;

  @Autowired
  AdminRepository adminRepository;

  @Autowired
  EntityManager entityManager;

  @SneakyThrows
  @Override
  public ResponseDTO<TransactionResponseDTO> createTransaction(TransactionRequestDTO request) {
    ValidationUtils.validateTransactionRequest(request);

    Optional<Admin> admin = adminRepository.findById(request.getAdminId());

    if (admin.isPresent()) {
//      Transaction newTransaction = new Transaction();
//      newTransaction.setAdmin(admin.get());
//      newTransaction.setWeight(request.getWeight());
//      newTransaction.setNotes(request.getNotes());
//      newTransaction.setTotalPrice(request.getTotalPrice());
//      newTransaction.setStatus(request.getStatus());

      Transaction newTransaction = modelMapper.map(request, Transaction.class);
      newTransaction.setId(null);
      newTransaction.setAdmin(admin.get());
      Transaction createdTransaction = transactionRepository.save(newTransaction);

      ResponseDTO<TransactionResponseDTO> response = new ResponseDTO<>();
      response.setData(convertToDto(createdTransaction));
      response.setStatus(HttpStatus.CREATED.value());
      response.setMessage("Transaction created successfully");

      logger.info("[POST] /api/v1/transactions - Transaction created successfully");

      return response;
    } else {
      logger.error("[POST] /api/v1/transactions - Admin ID not found");
      throw new ResourceNotFoundException("Admin ID not found");
    }
  }

  @Override
  public ResponseWithMetaDTO<List<TransactionResponseDTO>> getAllTransactions(Pageable pageable) {
    Page<Transaction> transactions = transactionRepository.findAll(pageable);

    List<TransactionResponseDTO> transactionsToDto = transactions.stream()
            .map(transaction -> modelMapper.map(transaction, TransactionResponseDTO.class))
            .collect(Collectors.toList());

    ResponseWithMetaDTO.Meta responseMeta = new ResponseWithMetaDTO.Meta();
    responseMeta.setPage(pageable.getPageNumber() + 1);
    responseMeta.setPerPage(pageable.getPageSize());
    responseMeta.setTotalPage(transactions.getTotalPages());
    responseMeta.setTotalData(transactions.getTotalElements());

    ResponseWithMetaDTO<List<TransactionResponseDTO>> response = new ResponseWithMetaDTO<>();
    response.setData(transactionsToDto);
    response.setMeta(responseMeta);
    response.setStatus(HttpStatus.OK.value());
    response.setMessage("");

    String logMsg = "[GET] /api/v1/transactions - status " + HttpStatus.OK.value();
    logger.info(logMsg);

    return response;
  }

  @Override
  @Transactional
  public ResponseDTO<TransactionResponseDTO> getTransaction(Long id) {
    if (id == null) throw new ValidationErrorException("Transaction ID cannot be empty");

    Optional<Transaction> transaction = transactionRepository.findById(id);

    if (transaction.isPresent()) {
      Transaction tempTransaction = transaction.get();

      Optional<Admin> admin = adminRepository.findById(tempTransaction.getAdmin().getId());

      if (admin.isPresent()) {
        Admin newAdmin = modelMapper.map(admin, Admin.class);
        tempTransaction.setAdmin(newAdmin);
      } else {
        throw new ResourceNotFoundException("Admin not found");
      }

      ResponseDTO<TransactionResponseDTO> response = new ResponseDTO<>();
      response.setData(convertToDto(tempTransaction));
      response.setStatus(HttpStatus.OK.value());
      response.setMessage("");

      String logMsg = "[GET] /api/v1/transactions/" + "{" + id + "}" + " - status " + HttpStatus.OK.value();
      logger.info(logMsg);

      return response;
    } else {
      String logMsg = "[GET] /api/v1/transactions/" + "{" + id + "}" + " - Transaction ID not found";
      logger.error(logMsg);
      throw new ResourceNotFoundException("Transaction ID not found");
    }
  }

  @Override
  @Transactional
  public ResponseDTO<TransactionResponseDTO> updateTransaction(Long id, TransactionRequestDTO request) {
    if (id == null) throw new ValidationErrorException("Transaction ID cannot be empty");
    if (request == null) throw new ValidationErrorException("Body request cannot be empty");

    Optional<Transaction> transaction = transactionRepository.findById(id);

    if (transaction.isPresent()) {
      Transaction tempTransaction = transaction.get();

      if (request.getAdminId() != null && !Objects.equals(request.getAdminId(), tempTransaction.getAdmin().getId())) {
        Optional<Admin> admin = adminRepository.findById(request.getAdminId());
        if (admin.isPresent()) {
          Admin newAdmin = modelMapper.map(admin, Admin.class);
          tempTransaction.setAdmin(newAdmin);
        } else {
          String logMsg = "[PUT] /api/v1/transactions/" + "{" + id + "}" + " - Admin ID not found";
          logger.error(logMsg);
          throw new ResourceNotFoundException("Admin ID not found");
        }
      }

      if (request.getWeight() > 0 && !Objects.equals(request.getWeight(), tempTransaction.getWeight())) {
        tempTransaction.setWeight(request.getWeight());
      }

      if (request.getNotes() != null && !Objects.equals(request.getNotes(), tempTransaction.getNotes())) {
        tempTransaction.setNotes(request.getNotes());
      }

      if (request.getTotalPrice() != null && !Objects.equals(request.getTotalPrice(), tempTransaction.getTotalPrice())) {
        tempTransaction.setTotalPrice(request.getTotalPrice());
      }

      if (
              request.getStatus() != null &&
              (request.getStatus().equals(TransactionStatusEnum.NEW) ||
              request.getStatus().equals(TransactionStatusEnum.DONE) ||
              request.getStatus().equals(TransactionStatusEnum.INVALID))
      ) {
        tempTransaction.setStatus(request.getStatus());
      }

      return convertTransactionEntityToDto(tempTransaction);
    } else {
      String logMsg = "[PUT] /api/v1/transactions/" + "{" + id + "}" + " - Transaction ID not found";
      logger.error(logMsg);
      throw new ResourceNotFoundException("Transaction ID not found");
    }
  }

  @Override
  public ResponseDTO<TransactionResponseDTO> updateTransactionStatus(Long id, TransactionStatusRequestDTO request) {
    if (id == null) throw new ValidationErrorException("Transaction ID cannot be empty");
    if (request == null) throw new ValidationErrorException("Body request cannot be empty");

    Optional<Transaction> transaction = transactionRepository.findById(id);

    if (transaction.isPresent()) {
      Transaction tempTransaction = transaction.get();

      if (
              request.getStatus() != null &&
              (request.getStatus().equals(TransactionStatusEnum.NEW) ||
              request.getStatus().equals(TransactionStatusEnum.DONE) ||
              request.getStatus().equals(TransactionStatusEnum.INVALID))
      ) {
        tempTransaction.setStatus(request.getStatus());
      }

      return convertTransactionEntityToDto(tempTransaction);
    } else {
      String logMsg = "[PUT] /api/v1/transactions/" + "{" + id + "}" + " - Transaction ID not found";
      logger.error(logMsg);
      throw new ResourceNotFoundException("Transaction ID not found");
    }
  }

  @Override
  public ResponseDTO<TransactionResponseDTO> deleteTransaction(Long id) {
    if (id == null) throw new ValidationErrorException("Transaction ID cannot be empty");

    Optional<Transaction> transaction = transactionRepository.findById(id);

    if (transaction.isPresent()) {
      transactionRepository.deleteById(id);

      ResponseDTO<TransactionResponseDTO> response = new ResponseDTO<>();
      response.setData(null);
      response.setStatus(HttpStatus.OK.value());
      response.setMessage("Transaction ID: " + id + " deleted successfully");

      String logMsg = "[DELETE] /api/v1/transactions/" + "{" + id + "}" + " - Transaction deleted successfully";
      logger.info(logMsg);

      return response;
    } else {
      String logMsg = "[DELETE] /api/v1/transactions/" + "{" + id + "}" + " - Transaction ID not found";
      logger.error(logMsg);
      throw new ResourceNotFoundException("Transaction ID not found");
    }
  }

  @Override
  public ByteArrayInputStream downloadExcel() {
    List<Transaction> transactions = transactionRepository.findAll();

    try (
            Workbook wb = new XSSFWorkbook();
            ByteArrayOutputStream out = new ByteArrayOutputStream()
    ) {
      Sheet sheet1 = wb.createSheet("Sheet1");
      List<String> headers = new ArrayList<>();
      headers.add("Transaction ID");
      headers.add("Admin ID");
      headers.add("Notes");
      headers.add("Weight");
      headers.add("Total Price");
      headers.add("Transaction Date");

      CellStyle cellStyle = wb.createCellStyle();
      CreationHelper createHelper = wb.getCreationHelper();
      cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy hh:mm:ss"));

      Row headerRow = sheet1.createRow(0);

      for (int i = 0; i < headers.size(); i++) {
        Cell col = headerRow.createCell(i);
        col.setCellValue(headers.get(i));
      }

      int rowId = 1;
      for (Transaction transaction : transactions) {
        Row row = sheet1.createRow(rowId);
        row.createCell(0, CellType.NUMERIC).setCellValue(transaction.getId());
        row.createCell(1, CellType.NUMERIC).setCellValue(transaction.getAdmin().getId());
        row.createCell(2, CellType.STRING).setCellValue(transaction.getNotes());
        row.createCell(3, CellType.NUMERIC).setCellValue(transaction.getWeight());
        row.createCell(4, CellType.STRING).setCellValue(String.valueOf(transaction.getTotalPrice()));

        Cell cell5 = row.createCell(5, CellType.STRING);
        cell5.setCellStyle(cellStyle);
        cell5.setCellValue(transaction.getCreatedAt());

        rowId++;
      }

      wb.write(out);

      String logMsg = "[GET] /api/v1/transactions/download-excel - status " + HttpStatus.OK.value();
      logger.info(logMsg);

      return new ByteArrayInputStream(out.toByteArray());
    } catch (IOError | IOException ioe) {
      ioe.printStackTrace();
      String logMsg = "[GET] /api/v1/transactions/download-excel - status " + HttpStatus.INTERNAL_SERVER_ERROR.value();
      logger.info(logMsg);
      return null;
    }
  }

  private ResponseDTO<TransactionResponseDTO> convertTransactionEntityToDto(Transaction tempTransaction) {
    Transaction updatedTransaction = transactionRepository.save(tempTransaction);

    ResponseDTO<TransactionResponseDTO> response = new ResponseDTO<>();
    response.setData(convertToDto(updatedTransaction));
    response.setStatus(HttpStatus.OK.value());
    response.setMessage("Transaction updated successfully");

    String logMsg = "[PUT] /api/v1/transactions/" + "{" + updatedTransaction.getId() + "}" + " - Transaction updated successfully";
    logger.info(logMsg);

    return response;
  }

  private TransactionResponseDTO convertToDto(Transaction transaction) {
    return modelMapper.map(transaction, TransactionResponseDTO.class);
  }
}
