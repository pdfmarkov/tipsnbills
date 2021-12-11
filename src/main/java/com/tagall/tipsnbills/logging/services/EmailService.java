package com.tagall.tipsnbills.logging.services;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
}
