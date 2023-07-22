package com.musdon.academybank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.musdon.academybank.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, String>{
	
}
