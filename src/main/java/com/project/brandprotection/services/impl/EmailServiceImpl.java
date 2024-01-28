package com.project.brandprotection.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.brandprotection.dtos.email.EmailId;
import com.project.brandprotection.dtos.email.EmailRequestDto;
import com.project.brandprotection.dtos.email.EmailTemplate;
import com.project.brandprotection.services.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JavaMailSender emailSender;
    private final ITemplateEngine templateEngine;

    @Async
    public void sendEmail(EmailRequestDto emailRequestDto) {
        String[] tos = emailRequestDto.getTos().stream().map(EmailId::getEmail).toArray(String[]::new);
        logger.info("Sending email to {}...", tos);
        try {
            String processedBody = getBody(emailRequestDto);
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(tos);
            helper.setSubject(emailRequestDto.getSubject());
            helper.setText(processedBody, true);
            if (emailRequestDto.getFile() != null && StringUtils.hasLength(emailRequestDto.getAttachmentFileName())) {
                helper.addAttachment(emailRequestDto.getAttachmentFileName(), emailRequestDto.getFile());
            }
            emailSender.send(message);
            logger.info("Email sent!");
        } catch (Exception e) {
            logger.error("Error during sending email to {}", tos, e);
        }
    }

    private String getBody(EmailRequestDto emailRequestDto) {
        Map<String, Object> params = getParams(emailRequestDto.getEmailTemplate());
        Context context = new Context();
        params.forEach(context::setVariable);
        return templateEngine.process(emailRequestDto.getEmailTemplate().getTemplateName(), context);
    }

    private Map<String, Object> getParams(EmailTemplate emailTemplate) {
        TypeReference<Map<String, Object>> paramsTypeRef = new TypeReference<>() {
        };
        Map<String, Object> params = new ObjectMapper().convertValue(emailTemplate, paramsTypeRef);
        for (Map.Entry<String, Object> stringObjectEntry : params.entrySet()) {
            this.convertParamEntryToString(stringObjectEntry, params);
        }
        return params;
    }


    private void convertParamEntryToString(Map.Entry<String, Object> paramEntry, Map<String, Object> params) {
        Object mapValue = paramEntry.getValue();
        if (mapValue == null) {
            return;
        }
        String convertedValue;
        if (!(mapValue instanceof Integer) && !(mapValue instanceof Long)) {
            if (mapValue instanceof Double || mapValue instanceof Float) {
                convertedValue = String.format("%.2f", mapValue);
                params.put(paramEntry.getKey(), convertedValue);
            }
        } else {
            convertedValue = mapValue.toString();
            params.put(paramEntry.getKey(), convertedValue);
        }
    }

}

