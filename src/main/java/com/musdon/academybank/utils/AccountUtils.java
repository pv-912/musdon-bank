package com.musdon.academybank.utils;

import java.time.Year;


public class AccountUtils {
	
	public static final String ACCOUNT_EXISTS_CODE = "001";
	public static final String ACCOUNT_EXISTS_MESSAGE = "This user already exists";
	
	public static final String ACCOUNT_CREATION_CODE = "002";
	public static final String ACCOUNT_CREATION_MESSAGE = "Account successfully created";

	public static final String ACCOUNT_NOT_EXIST_CODE = "003";
	public static final String ACCOUNT_NOT_EXIST_MESSAGE = "Account Number does not exists";
	
	public static final String ACCOUNT_CODE_FOUND = "004";
	public static final String ACCOUNT_CODE_FOUND_MESSAGE = "User Account found.";
	
	public static final String INSUFFICIENT_BALANCE_CODE = "005";
	public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient Balance.";
	
	public static final String CREDIT_SUCCESS_CODE = "006";
	public static final String CREDIT_SUCCESS_MESSAGE = "Amount credited successfully.";
	
	public static final String DEBIT_SUCCESS_CODE = "007";
	public static final String DEBIT_SUCCESS_MESSAGE = "Amount debited Succesfully";
	
	public static final String SOURCE_ACCOUNT_CODE_FOUND = "008";
	public static final String SOURCE_ACCOUNT_CODE_FOUND_MESSAGE = "Source Account found.";
	
	public static final String DEST_ACCOUNT_CODE_FOUND = "009";
	public static final String DEST_ACCOUNT_CODE_FOUND_MESSAGE = "Destination Account found.";
	
	public static final String TRANSFER_SUCCESSFUL_CODE = "010";
	public static final String TRANSFER_SUCCESSFUL_MESSAGE = "Transfer Successful.";
	
	public static String generateAccountNumber() {
		/**
		 * 2023 + random 6 digits
		 */
		Year currentYear = Year.now();
		int min = 100000;
		int max = 999999;
		
		/**
		 * generate Random number between main and max
		 */
		int randNumber = (int) Math.floor(Math.random()*(max - min +1) + min);
		String year = String.valueOf(currentYear);
		String randomNumber = String.valueOf(randNumber);
		StringBuilder accountNumber = new StringBuilder();
		
		return accountNumber.append(year).append(randomNumber).toString();
	}
}
