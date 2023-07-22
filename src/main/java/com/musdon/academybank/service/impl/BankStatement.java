package com.musdon.academybank.service.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Component;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.musdon.academybank.dto.EmailDetails;
import com.musdon.academybank.entity.Transaction;
import com.musdon.academybank.entity.User;
import com.musdon.academybank.repository.TransactionRepository;
import com.musdon.academybank.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {
	/*
	 * Retrieve list of transaction in date range for given account number
	 * generate a pdf file of transactions
	 * send the file via email
	 */
	
	private TransactionRepository transactionRepository;
	private UserRepository userRepository;
	private EmailService emailService;
	
	private static final String FILE = "/Users/prasverm/Documents/eclipse-workspace/java-academy-bank/statement/statement.pdf";
	
	public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) throws FileNotFoundException, DocumentException {
		
		LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
		LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
		
		List<Transaction> transactionList = transactionRepository.findAll().stream()
					.filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
					.filter(transaction -> transaction.getCreateAt().isAfter(start))
					.filter(transaction -> transaction.getCreateAt().isBefore(end)).toList();
		
		/*
		 * Generate PDF template
		 */
		
		User user = userRepository.findByAccountNumber(accountNumber);
		String customerName = user.getFirstName() + " " + user.getLastName();
		
		Rectangle statementSize = new Rectangle(PageSize.A4);
		Document document = new Document(statementSize);
		log.info("Setting size of document");
	
		OutputStream outputStream = new FileOutputStream(FILE);
		PdfWriter.getInstance(document, outputStream);
		document.open();
		
		// Header table
		PdfPTable bankInfoTable = new PdfPTable(1);
		PdfPCell bankName = new PdfPCell(new Phrase("The Mosdun Bank"));
		bankName.setBorder(0);
		bankName.setBackgroundColor(BaseColor.LIGHT_GRAY);
		bankName.setPadding(20f);
		
		PdfPCell bankAddress = new PdfPCell(new Phrase("301, Elite Homes, Bangalore"));
		bankAddress.setBorder(0);
		bankInfoTable.addCell(bankName);
		bankInfoTable.addCell(bankAddress);
		
		// Information table
		PdfPTable statementInfo = new PdfPTable(2);
		
		PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date: " + startDate));
		customerInfo.setBorder(0);
		
		PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
		statement.setBorder(0);
		
		PdfPCell stopDate = new PdfPCell(new Phrase("End Date: " + endDate));
		stopDate.setBorder(0);
		
		PdfPCell name = new PdfPCell(new Phrase("Customer Name: " + customerName));
		name.setBorder(0);
		
		PdfPCell space = new PdfPCell();
		space.setBorder(0);
		
		PdfPCell address = new PdfPCell(new Phrase("Customer Address: " + user.getAddress()));
		address.setBorder(0);
		
		// Statement table
		PdfPTable transactionTable = new PdfPTable(4);
		//column 1
		PdfPCell date = new PdfPCell(new Phrase("DATE"));
		date.setBackgroundColor(BaseColor.LIGHT_GRAY);
		date.setBorder(0);
		
		//column 2
		PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
		transactionType.setBackgroundColor(BaseColor.LIGHT_GRAY);
		transactionType.setBorder(0);
		
		//column 3
		PdfPCell transactionAmount = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
		transactionAmount.setBackgroundColor(BaseColor.LIGHT_GRAY);
		transactionAmount.setBorder(0);
		
		//column 4
		PdfPCell status = new PdfPCell(new Phrase("STATUS"));
		status.setBackgroundColor(BaseColor.LIGHT_GRAY);
		status.setBorder(0);
		
		// Add headers for statement table
		transactionTable.addCell(date);
		transactionTable.addCell(transactionType);
		transactionTable.addCell(transactionAmount);
		transactionTable.addCell(status);
		
		transactionList.forEach(transaction -> {
			transactionTable.addCell(new Phrase(transaction.getCreateAt().toString()));
			transactionTable.addCell(new Phrase(transaction.getTransactionType()));
			transactionTable.addCell(new Phrase(transaction.getAmount().toString()));
			transactionTable.addCell(new Phrase(transaction.getStatus()));
		});
		
		statementInfo.addCell(customerInfo);
		statementInfo.addCell(statement);
		statementInfo.addCell(endDate);
		statementInfo.addCell(name);
		statementInfo.addCell(space);
		statementInfo.addCell(address);
		
		document.add(bankInfoTable);
		document.add(statementInfo);
		document.add(transactionTable);
		
		document.close();
		
		// Send email with attachment
		EmailDetails emailDetails = EmailDetails.builder()
									.recipient(user.getEmail())
									.subject("BANK STATEMENT")
									.messageBody("Kindly fine your requested account statement attached!!")
									.attachment(FILE)
									.build();
		
		emailService.sendEmailWithAttachment(emailDetails);
		
		return transactionList;
	}

	
}
