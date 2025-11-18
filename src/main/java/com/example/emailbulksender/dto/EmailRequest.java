package com.example.emailbulksender.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {
    private String subject;
    private String message;
    private boolean hasAttachment;
    private String attachmentFileName;
}


