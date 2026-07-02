package com.example.backend.repository;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.enums.Role;
import com.example.backend.model.User;

public interface UserRepository
extends JpaRepository<User, Long> {
	
	User findTopByOrderByIdDesc();

	Optional<User> findByUsername(String username);
	
	List<User> findByUsernameAndRole(String username, Role role);
	
	List<User> findByRole(Role role);
	
}

