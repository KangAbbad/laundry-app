package com.alta.bootcamp.laundryapp.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Entity(name = "Admin")
@Table(name = "admins")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Admin {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NaturalId
  @NotBlank
  @Column(nullable = false)
  private String username;

  @NaturalId
  @NotBlank
  @Column(nullable = false)
  private String email;

  @NaturalId
  @NotBlank
  @Column(nullable = false)
  private String phone;

  private String idCard;
  private String name;
  private String address;

  @NotBlank
  private String password;

  // @OneToMany(fetch = FetchType.LAZY, mappedBy = "admin")
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "admin_id", referencedColumnName = "id")
  private List<Transaction> transactions = new ArrayList<>();

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "admin_roles",
          joinColumns = @JoinColumn(name = "admin_id"),
          inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  @Column(nullable = false, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @CreatedDate
  private Date createdAt;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @LastModifiedDate
  private Date updatedAt;
}
