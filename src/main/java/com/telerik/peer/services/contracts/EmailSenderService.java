package com.telerik.peer.services.contracts;

import com.telerik.peer.models.User;
import org.springframework.mail.SimpleMailMessage;

public interface EmailSenderService {
    void sendMail(User recipient, SimpleMailMessage mailMessage);

    void sendMail(User recipient, String subject, String body);
}
