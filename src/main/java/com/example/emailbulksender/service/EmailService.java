package com.example.emailbulksender.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Send bulk emails asynchronously
     * @param emails - List of email addresses
     * @param subject - Email subject
     * @param message - Email message body
     */
    @Async
    public void sendBulkEmails(List<String> emails, String subject, String message, File attachmentFile) {
        log.info("Starting to send {} emails", emails.size());

        int successCount = 0;
        int failureCount = 0;

        for (String email : emails) {
            try {
                if (attachmentFile != null && attachmentFile.exists()) {
                    sendEmailWithAttachment(email, subject, message, attachmentFile);
                } else {
                    sendEmail(email, subject, message);
                }
                successCount++;
                log.info("Email sent successfully to: {}", email);
            } catch (Exception e) {
                failureCount++;
                log.error("Failed to send email to: {}. Error: {}", email, e.getMessage());
            }
        }

        log.info("Bulk email sending completed. Success: {}, Failed: {}", successCount, failureCount);
    }

    /**
     * Send a single email without attachment
     */
    private void sendEmail(String to, String subject, String text) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(createHtmlEmail(subject, text), true); // true = HTML

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    /**
     * Send a single email with attachment
     */
    private void sendEmailWithAttachment(String to, String subject, String text, File attachmentFile) throws MessagingException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(createHtmlEmail(subject, text), true); // true = HTML

            // Add attachment
            byte[] fileContent = Files.readAllBytes(attachmentFile.toPath());
            helper.addAttachment(attachmentFile.getName(), new ByteArrayResource(fileContent));

            mailSender.send(message);
        } catch (Exception e) {
            log.error("Error sending email with attachment: {}", e.getMessage());
            throw new MessagingException("Failed to send email with attachment", e);
        }
    }

    /**
     * Create beautiful HTML email
     */
    private String createHtmlEmail(String subject, String message) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: 'Arial', 'Helvetica', sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }" +
                ".email-container { max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }" +
                ".email-header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; }" +
                ".email-header h1 { margin: 0; font-size: 28px; font-weight: 600; }" +
                ".email-body { padding: 40px 30px; color: #333333; line-height: 1.8; }" +
                ".email-body p { margin: 0 0 15px 0; font-size: 16px; }" +
                ".email-footer { background-color: #f8f9fa; padding: 20px; text-align: center; color: #6c757d; font-size: 14px; border-top: 1px solid #e9ecef; }" +
                ".email-footer p { margin: 5px 0; }" +
                ".divider { height: 2px; background: linear-gradient(to right, #667eea, #764ba2); margin: 20px 0; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='email-container'>" +
                "<div class='email-header'>" +
                "<h1>" + escapeHtml(subject) + "</h1>" +
                "</div>" +
                "<div class='email-body'>" +
                formatMessageAsHtml(message) +
                "</div>" +
                "<div class='divider'></div>" +
                "<div class='email-footer'>" +
                "<p><strong>Bulk Email Sender Pro</strong></p>" +
                "<p>This email was sent using our professional bulk email service.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    /**
     * Format plain text message as HTML with paragraphs
     */
    private String formatMessageAsHtml(String message) {
        if (message == null || message.trim().isEmpty()) {
            return "<p>No message content</p>";
        }

        // Split by double line breaks for paragraphs
        String[] paragraphs = message.split("\\n\\n");
        StringBuilder html = new StringBuilder();

        for (String para : paragraphs) {
            if (!para.trim().isEmpty()) {
                // Replace single line breaks with <br>
                String formatted = escapeHtml(para.trim()).replace("\n", "<br>");
                html.append("<p>").append(formatted).append("</p>");
            }
        }

        return html.toString();
    }

    /**
     * Escape HTML special characters
     */
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
}

