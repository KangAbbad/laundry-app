package com.alta.bootcamp.laundryapp.utils;

import com.alta.bootcamp.laundryapp.dto.AdminRequestDTO;
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
}
