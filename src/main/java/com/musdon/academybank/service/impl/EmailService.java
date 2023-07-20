package com.musdon.academybank.service.impl;

import com.musdon.academybank.dto.EmailDetails;

public interface EmailService {
	void sendEmailAlerts(EmailDetails emailDetails);
}
