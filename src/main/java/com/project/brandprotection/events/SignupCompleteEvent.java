package com.project.brandprotection.events;

import com.project.brandprotection.models.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class SignupCompleteEvent extends ApplicationEvent {
   private User user;
   private String verifyUrl;

    public SignupCompleteEvent(User user, String verifyUrl) {
        super(user);
        this.user = user;
        this.verifyUrl = verifyUrl;
    }
}
