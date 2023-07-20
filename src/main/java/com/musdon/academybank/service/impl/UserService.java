package com.musdon.academybank.service.impl;

import com.musdon.academybank.dto.BankResponse;
import com.musdon.academybank.dto.CreditDebitRequest;
import com.musdon.academybank.dto.EnquiryRequest;
import com.musdon.academybank.dto.TransferRequest;
import com.musdon.academybank.dto.UserRequest;

public interface UserService {
	
	BankResponse createAccount(UserRequest userRequest);
	
	BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
	
	String nameEnquiry(EnquiryRequest enquiryRequest);
	
	BankResponse creditAccount(CreditDebitRequest creditDebitRequest);

	BankResponse debitAccount(CreditDebitRequest creditDebitRequest);
	
	BankResponse transfer(TransferRequest transferRequest);
	
}
