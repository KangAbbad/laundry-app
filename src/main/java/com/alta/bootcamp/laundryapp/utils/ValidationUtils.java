package com.alta.bootcamp.laundryapp.utils;

import com.alta.bootcamp.laundryapp.dto.AdminRequestDTO;
import com.alta.bootcamp.laundryapp.dto.SummaryRevenueRequestDTO;
import com.alta.bootcamp.laundryapp.dto.TransactionRequestDTO;
import com.alta.bootcamp.laundryapp.exceptions.ValidationErrorException;
import org.apache.commons.lang3.StringUtils;

public class ValidationUtils {
  public static void validateAdminRequest(AdminRequestDTO request) {
    if (request == null) {
      throw new ValidationErrorException("Body request cannot be empty");
    }

    if (StringUtils.isEmpty(request.getUsername())) {
      throw new ValidationErrorException("Username cannot be empty");
    }

    if (StringUtils.isEmpty(request.getEmail())) {
      throw new ValidationErrorException("Email cannot be empty");
    }

    if (StringUtils.isEmpty(request.getPhone())) {
      throw new ValidationErrorException("Phone Number cannot be empty");
    }

    if (StringUtils.isEmpty(request.getIdCard())) {
      throw new ValidationErrorException("ID Card cannot be empty");
    }
  }

  public static void validateTransactionRequest(TransactionRequestDTO request) {
    if (request == null) {
      throw new ValidationErrorException("Body request cannot be empty");
    }

    if (request.getAdminId() == null) {
      throw new ValidationErrorException("Admin ID cannot be empty");
    }

    if (request.getWeight() < 1) {
      throw new ValidationErrorException("Weight cannot be empty");
    }

    if (request.getTotalPrice() == null) {
      throw new ValidationErrorException("Total Price cannot be empty");
    }

    if (request.getStatus() == null) {
      throw new ValidationErrorException("Status cannot be empty");
    }
  }

  public static void validateSummaryRevenueRequest(SummaryRevenueRequestDTO request) {
    if (request == null) {
      throw new ValidationErrorException("Body request cannot be empty");
    }

    if (request.getAdminId() == null) {
      throw new ValidationErrorException("Admin ID cannot be empty");
    }

    if (request.getTotalRevenue() == null) {
      throw new ValidationErrorException("Total Revenue cannot be empty");
    }
  }
}
