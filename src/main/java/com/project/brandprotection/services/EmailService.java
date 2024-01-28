package com.project.brandprotection.services;

import com.project.brandprotection.dtos.email.EmailRequestDto;

public interface EmailService {
    void sendEmail(EmailRequestDto emailRequestDto);
}
