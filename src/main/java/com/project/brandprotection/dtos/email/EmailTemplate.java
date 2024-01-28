package com.project.brandprotection.dtos.email;

import com.project.brandprotection.enums.MessageType;

public abstract class EmailTemplate implements MessageTemplate {
    @Override
    public MessageType getMessageType() {
        return MessageType.EMAIL;
    }
}
