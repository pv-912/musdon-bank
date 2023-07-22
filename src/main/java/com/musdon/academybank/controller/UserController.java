package com.musdon.academybank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musdon.academybank.dto.BankResponse;
import com.musdon.academybank.dto.CreditDebitRequest;
import com.musdon.academybank.dto.EnquiryRequest;
import com.musdon.academybank.dto.TransferRequest;
import com.musdon.academybank.dto.UserRequest;
import com.musdon.academybank.service.impl.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Account Management APIs")
public class UserController {

	@Autowired
	UserService userService;
	
	@Operation(
			summary = "Create new user account",
			description = "Create new user and assign account id"
			)
	@ApiResponse(
			responseCode = "201",
			description = "Http Status 201 Created"
			)
	@PostMapping
	public BankResponse createAccount(@RequestBody UserRequest userRequest) {
		return userService.createAccount(userRequest);
	}
	
	@GetMapping("/balanceEnquiry")
	public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
		return userService.balanceEnquiry(enquiryRequest);
	}
	
	@GetMapping("/nameEnquiry")
	public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
		return userService.nameEnquiry(enquiryRequest);
	}
	
	@PostMapping("/credit")
	public BankResponse creditAccount(@RequestBody CreditDebitRequest creditDebitRequest) {
		return userService.creditAccount(creditDebitRequest);
	}
	
	@PostMapping("/debit")
	public BankResponse debitAccount(@RequestBody CreditDebitRequest creditDebitRequest) {
		return userService.debitAccount(creditDebitRequest);
	}
	
	@PostMapping("/transfer")
	public BankResponse transfer(@RequestBody TransferRequest transferRequest) {
		return userService.transfer(transferRequest);
	}
}
