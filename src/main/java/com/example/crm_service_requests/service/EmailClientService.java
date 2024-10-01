package com.example.crm_service_requests.service;

import java.util.List;

public interface EmailClientService {
    //public void sendSimpleMessage(String to, String subject, String text);
    public void sendEmails(List<String> toList, String subject, String text);
    public void sendEmail(String to, String subject, String text);
}
