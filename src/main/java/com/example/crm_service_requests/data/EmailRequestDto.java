package com.example.crm_service_requests.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmailRequestDto {
    private List<String> toList;
    private String subject;
    private String text;
}
