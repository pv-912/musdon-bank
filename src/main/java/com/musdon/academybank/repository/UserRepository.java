package com.musdon.academybank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.musdon.academybank.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	Boolean existsByEmail(String email);
	Boolean existsByAccountNumber(String accountNumber);
	User findByAccountNumber(String accountNumber);

}
