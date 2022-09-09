package com.alta.bootcamp.laundryapp.securities.services;

import com.alta.bootcamp.laundryapp.entities.Admin;
import com.alta.bootcamp.laundryapp.exceptions.ResourceNotFoundException;
import com.alta.bootcamp.laundryapp.repositories.AdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

  @Autowired
  AdminRepository adminRepository;

  @Override
  public UserDetails loadUserByUsername(String usernameOrEmail) throws ResourceNotFoundException {
    Admin admin = adminRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
            .orElseThrow(() -> {
              logger.error("[POST] /api/v1/signin - Username or Email not found");
              return new ResourceNotFoundException("Username or Email not found");
            });

    return UserPrincipal.create(admin);
  }

  @Transactional
  public UserDetails loadUserById(Long id) {
    Admin admin = adminRepository.findById(id)
            .orElseThrow(() -> {
              logger.error("Admin ID not found");
              return new ResourceNotFoundException("Admin ID not found");
            });

    return UserPrincipal.create(admin);
  }
}
