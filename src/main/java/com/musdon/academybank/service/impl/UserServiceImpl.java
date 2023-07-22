package com.musdon.academybank.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.musdon.academybank.dto.AccountInfo;
import com.musdon.academybank.dto.BankResponse;
import com.musdon.academybank.dto.CreditDebitRequest;
import com.musdon.academybank.dto.EmailDetails;
import com.musdon.academybank.dto.EnquiryRequest;
import com.musdon.academybank.dto.TransactionDto;
import com.musdon.academybank.dto.TransferRequest;
import com.musdon.academybank.dto.UserRequest;
import com.musdon.academybank.entity.User;
import com.musdon.academybank.repository.UserRepository;
import com.musdon.academybank.utils.AccountUtils;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	TransactionService transactionService;
	
	@Override
	public BankResponse createAccount(UserRequest userRequest) {
		/**
		 * Creating an account - saving a new user into db
		 * check if user already has account
		 */
		
		if(userRepository.existsByEmail(userRequest.getEmail())) {
			return BankResponse.builder()
								.responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
								.responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
								.accountInfo(null)
								.build();
		}
		
		User newUser = User.builder()
							.firstName(userRequest.getFirstName())
							.lastName(userRequest.getLastName())
							.otherName(userRequest.getOtherName())
							.gender(userRequest.getGender())
							.address(userRequest.getAddress())
							.stateOfOrigin(userRequest.getStateOfOrigin())
							.accountNumber(AccountUtils.generateAccountNumber())
							.accountBalance(BigDecimal.ZERO)
							.email(userRequest.getEmail())
							.phoneNumber(userRequest.getPhoneNumber())
							.alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
							.status("ACTIVE")
							.build();
		
		User savedUser = userRepository.save(newUser);
		
		EmailDetails emailDetails = EmailDetails.builder()
									.recipient(savedUser.getEmail())
									.subject("Account Creation")
									.messageBody("Congrats!! Your account has been successfully created. \n Account details: " 
									+ savedUser.getFirstName() + savedUser.getLastName() + ".")
									.build();

		emailService.sendEmailAlerts(emailDetails);
		
		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_CREATION_CODE)
				.responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountBalance(savedUser.getAccountBalance())
						.accountNumber(savedUser.getAccountNumber())
						.accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
						.build())
				.build();
		
	}

	@Override
	public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
		
		Boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
		
		if(!isAccountExist) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
					.accountInfo(null)
					.build();
		}
		
		User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
				
		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_CODE_FOUND)
				.responseMessage(AccountUtils.ACCOUNT_CODE_FOUND_MESSAGE)
				.accountInfo(AccountInfo.builder()
										.accountBalance(foundUser.getAccountBalance())
										.accountName(foundUser.getFirstName() + " " + foundUser.getLastName())
										.accountNumber(foundUser.getAccountNumber())
										.build())
				.build();
	}

	@Override
	public String nameEnquiry(EnquiryRequest enquiryRequest) {
		
		Boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());

		if(!isAccountExist) {
			return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
		}
		
		User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
				
		return foundUser.getFirstName() + " " + foundUser.getLastName();
	}

	@Override
	public BankResponse creditAccount(CreditDebitRequest creditDebitRequest) {
		
		Boolean isAccountExist = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
		
		if(!isAccountExist) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
					.accountInfo(null)
					.build();
		}
		
		User userToCredit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
		
		userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditDebitRequest.getAmount()));
		userRepository.save(userToCredit);
		
		// Save Transaction
		TransactionDto transactionDto = TransactionDto.builder()
										.accountNumber(userToCredit.getAccountNumber())
										.transactionType("CREDIT")
										.amount(creditDebitRequest.getAmount())
										.build();
		transactionService.saveTransaction(transactionDto);
		
		return BankResponse.builder()
				.responseCode(AccountUtils.CREDIT_SUCCESS_CODE)
				.responseMessage(AccountUtils.CREDIT_SUCCESS_MESSAGE)
				.accountInfo(AccountInfo.builder()
										.accountBalance(userToCredit.getAccountBalance())
										.accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName())
										.accountNumber(userToCredit.getAccountNumber())
										.build())
				.build();
	}

	@Override
	public BankResponse debitAccount(CreditDebitRequest creditDebitRequest) {
		
		Boolean isAccountExist = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
		
		if(!isAccountExist) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
					.accountInfo(null)
					.build();
		}
		
		User userToDebit= userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
		BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
		BigInteger debitAmount = creditDebitRequest.getAmount().toBigInteger();
		if(availableBalance.intValue() < debitAmount.intValue()) {
			return BankResponse.builder()
					.responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
					.responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
					.accountInfo(null)
					.build();
		}
		
		userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(creditDebitRequest.getAmount()));
		userRepository.save(userToDebit);
		
		TransactionDto transactionDto = TransactionDto.builder()
				.accountNumber(userToDebit.getAccountNumber())
				.transactionType("DEBIT")
				.amount(creditDebitRequest.getAmount())
				.build();
		transactionService.saveTransaction(transactionDto);
		
		return BankResponse.builder()
				.responseCode(AccountUtils.DEBIT_SUCCESS_CODE)
				.responseMessage(AccountUtils.DEBIT_SUCCESS_MESSAGE)
				.accountInfo(AccountInfo.builder()
										.accountBalance(userToDebit.getAccountBalance())
										.accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName())
										.accountNumber(userToDebit.getAccountNumber())
										.build())
				.build();
	}

	@Override
	public BankResponse transfer(TransferRequest transferRequest) {
		if(!userRepository.existsByAccountNumber(transferRequest.getDestinationAccountNumber())) {
			return BankResponse.builder()
					.responseCode(AccountUtils.DEST_ACCOUNT_CODE_FOUND)
					.responseMessage(AccountUtils.DEST_ACCOUNT_CODE_FOUND_MESSAGE)
					.accountInfo(null)
					.build();
		}
		
		User sourceAccountUser = userRepository.findByAccountNumber(transferRequest.getSourceAccountNumber());
		System.out.println(sourceAccountUser.getAccountBalance());
		if(transferRequest.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0) {
			return BankResponse.builder()
					.responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
					.responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
					.accountInfo(null)
					.build();
		}
		
		sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(transferRequest.getAmount()));
		userRepository.save(sourceAccountUser);
		
		EmailDetails debitAlertDetails = EmailDetails.builder()
										.subject("DEBIT ALERT")
										.recipient(sourceAccountUser.getEmail())
										.messageBody("The sum of " + transferRequest.getAmount() + " has been deducted from your account! Your current balance is " + sourceAccountUser.getAccountBalance())
										.build();
		
		emailService.sendEmailAlerts(debitAlertDetails);
		
		TransactionDto transactionDtoSource = TransactionDto.builder()
				.accountNumber(sourceAccountUser.getAccountNumber())
				.transactionType("DEBIT")
				.amount(transferRequest.getAmount())
				.build();
		transactionService.saveTransaction(transactionDtoSource);
			
		User destAccountUser = userRepository.findByAccountNumber(transferRequest.getDestinationAccountNumber());
		destAccountUser.setAccountBalance(destAccountUser.getAccountBalance().add(transferRequest.getAmount()));
		userRepository.save(destAccountUser);
		
		EmailDetails creditAlertDetails = EmailDetails.builder()
				.subject("CREDIT ALERT")
				.recipient(destAccountUser.getEmail())
				.messageBody("The sum of " + transferRequest.getAmount() + " has been credited from your account! Your current balance is " + destAccountUser.getAccountBalance())
				.build();
		
		TransactionDto transactionDtoDest = TransactionDto.builder()
				.accountNumber(destAccountUser.getAccountNumber())
				.transactionType("CREDIT")
				.amount(transferRequest.getAmount())
				.build();
		transactionService.saveTransaction(transactionDtoDest);

		emailService.sendEmailAlerts(creditAlertDetails);
		return BankResponse.builder()
				.responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
				.responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
				.accountInfo(null)
				.build();
	}

	
}
