package com.project.brandprotection.events.listener;

import com.project.brandprotection.dtos.email.EmailId;
import com.project.brandprotection.dtos.email.EmailRequestDto;
import com.project.brandprotection.dtos.email.EmailTemplate;
import com.project.brandprotection.dtos.email.templates.SignUpConfirmEmailTemplate;
import com.project.brandprotection.events.SignupCompleteEvent;
import com.project.brandprotection.models.User;
import com.project.brandprotection.services.AuthService;
import com.project.brandprotection.services.EmailService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SignupCompleteEventListener implements ApplicationListener<SignupCompleteEvent> {

    private final EmailService emailService;
    private final AuthService authService;

    @Override
    @Async
    public void onApplicationEvent(@NonNull SignupCompleteEvent event) {
        log.info("Listen signup event {}", event);
        User user = event.getUser();
        String verificationToken = UUID.randomUUID().toString();
        authService.saveUserVerificationToken(user, verificationToken);
        String url = event.getVerifyUrl() + verificationToken;
        EmailTemplate emailTemplate = new SignUpConfirmEmailTemplate(user.getFullName(), url);
        EmailRequestDto emailRequestDto = EmailRequestDto.builder()
                .subject("Signup Confirmation")
                .tos(Collections.singletonList(new EmailId(user.getFullName(), user.getEmail())))
                .emailTemplate(emailTemplate)
                .build();
        emailService.sendEmail(emailRequestDto);
    }

}
