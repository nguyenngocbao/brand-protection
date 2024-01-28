package com.project.brandprotection.dtos.email;

import com.project.brandprotection.enums.MessageType;

public interface MessageTemplate {
    MessageType getMessageType();
    String getTemplateName();
}
