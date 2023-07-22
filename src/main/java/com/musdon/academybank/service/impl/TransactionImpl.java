package com.musdon.academybank.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.musdon.academybank.dto.TransactionDto;
import com.musdon.academybank.entity.Transaction;
import com.musdon.academybank.repository.TransactionRepository;

@Component
public class TransactionImpl implements TransactionService{

	@Autowired
	TransactionRepository transactionRepository;
	
	@Override
	public void saveTransaction(TransactionDto transactionDto) {
		
		Transaction transaction = Transaction.builder()
						.transactionType(transactionDto.getTransactionType())
						.accountNumber(transactionDto.getAccountNumber())
						.amount(transactionDto.getAmount())
						.status("SUCCESS")
						.build();
		
		transactionRepository.save(transaction);
		System.out.println("Transaction Saved Successfully");
	}
	
}
