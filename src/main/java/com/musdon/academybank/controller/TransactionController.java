package com.musdon.academybank.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.musdon.academybank.entity.Transaction;
import com.musdon.academybank.service.impl.BankStatement;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/bankStatement")
@AllArgsConstructor
public class TransactionController {
	
	private BankStatement bankStatement;
	
	@GetMapping
	public List<Transaction> generateBankStatement(@RequestParam String accountNumber,
												  @RequestParam String startDate,
												  @RequestParam String endDate) throws FileNotFoundException, DocumentException {
		return bankStatement.generateStatement(accountNumber, startDate, endDate);
	}
	
}
