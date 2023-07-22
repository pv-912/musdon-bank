package com.musdon.academybank.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Component;

import com.musdon.academybank.entity.Transaction;
import com.musdon.academybank.repository.TransactionRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class BankStatement {
	/*
	 * Retrieve list of transaction in date range for given account number
	 * generate a pdf file of transactions
	 * send the file via email
	 */
	
	private TransactionRepository transactionRepository;
	
	
	public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) {
		
		LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
		LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
		
		List<Transaction> transactionList = transactionRepository.findAll().stream()
					.filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
					.filter(transaction -> transaction.getCreateAt().isAfter(start))
					.filter(transaction -> transaction.getCreateAt().isBefore(end)).toList();
		
		return transactionList;
	}
	
}
