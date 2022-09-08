package com.alta.bootcamp.laundryapp.securities.services;

import com.alta.bootcamp.laundryapp.entities.Admin;
import com.alta.bootcamp.laundryapp.exceptions.ResourceNotFoundException;
import com.alta.bootcamp.laundryapp.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  @Autowired
  AdminRepository adminRepository;

  @Override
  public UserDetails loadUserByUsername(String usernameOrEmail) throws ResourceNotFoundException {
    Admin admin = adminRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
            .orElseThrow(() -> new ResourceNotFoundException("Username or Email not found"));

    return UserPrincipal.create(admin);
  }

  @Transactional
  public UserDetails loadUserById(Long id) {
    Admin admin = adminRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Admin ID not found"));

    return UserPrincipal.create(admin);
  }
}
