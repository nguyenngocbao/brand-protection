package com.project.brandprotection.dtos.email;

import lombok.*;

import java.io.File;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequestDto {
    private String subject;
    private EmailId from;
    private List<EmailId> tos;
    private List<EmailId> ccs;
    private String attachmentFileName;
    private File file;
    private EmailTemplate emailTemplate;
}
