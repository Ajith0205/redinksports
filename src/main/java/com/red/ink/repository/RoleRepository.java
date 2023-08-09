package com.red.ink.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.red.ink.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

	Optional<Role> findByRole(String role);

	

}
