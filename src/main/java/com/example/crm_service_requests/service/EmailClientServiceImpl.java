package com.example.crm_service_requests.service;

import com.example.crm_service_requests.data.EmailRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class EmailClientServiceImpl implements EmailClientService{

    @Autowired
    private RestTemplate restTemplate;

    public void sendEmails(List<String> toList, String subject, String text) {
        String emailServiceUrl = "http://localhost:4040/api/emails/send";
//        String emailServiceUrl = "http://crm-email-service:8080/api/emails/send";

        EmailRequestDto emailRequest = new EmailRequestDto();
        emailRequest.setToList(toList);
        emailRequest.setSubject(subject);
        emailRequest.setText(text);

        System.out.println(emailRequest.getSubject());

        restTemplate.postForObject(emailServiceUrl, emailRequest, String.class);
    }

    public void sendEmail(String to, String subject, String text) {
        sendEmails(List.of(to), subject, text);
    }
}