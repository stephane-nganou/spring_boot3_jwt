package com.masteranything.security.service;

import com.masteranything.security.dto.EmailTemplateName;

import jakarta.mail.MessagingException;

public interface EmailService {

    public void sendEmail(
      String to,
      String username,
      EmailTemplateName emailTemplateName,
      String confirmationUrl,
      String activationCode,
      String subject
  ) throws MessagingException;
}
