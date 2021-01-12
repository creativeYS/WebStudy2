package com.example.demo.Security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class UserAccountChangedEvent extends ApplicationEvent {
    private String userId;

    public UserAccountChangedEvent(Object source, String userId)
    {
        super(source);
        this.userId = userId;
    }
}
