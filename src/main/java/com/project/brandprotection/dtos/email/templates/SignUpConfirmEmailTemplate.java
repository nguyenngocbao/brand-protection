package com.project.brandprotection.dtos.email.templates;

import com.project.brandprotection.dtos.email.EmailTemplate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpConfirmEmailTemplate extends EmailTemplate {

    private String recipientName;

    private String confirmationUrl;

    @Override
    public String getTemplateName() {
        return "confirmation-email";
    }
}
