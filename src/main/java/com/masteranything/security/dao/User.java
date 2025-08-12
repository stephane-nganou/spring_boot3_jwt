package com.masteranything.security.dao;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "_user")
public class User extends BaseEntity implements UserDetails, Principal {

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  private LocalDate birthDate;
  @Column(unique = true, nullable = false)

  private String email;
  @Column(nullable = false)
  
  private String password;
  private boolean accountLocked;
  private boolean enabled;


  @ManyToMany(fetch = FetchType.EAGER)
  private List<Role> roles;

  @OneToMany(mappedBy="owner")
  private List<Book> books; 

  @OneToMany(mappedBy="user")
  private List<BookTransactionHistory> transactionHistories;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // return List.of(new SimpleGrantedAuthority(role.name()));
    return this.roles
        .stream()
        .map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toList());
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return UserDetails.super.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return UserDetails.super.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return UserDetails.super.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {
    return UserDetails.super.isEnabled();
  }

  @Override
  public String getName() {
    return this.email;
  }

  public String getFullName() {
    return this.firstName + " " + this.lastName;
  }
}
