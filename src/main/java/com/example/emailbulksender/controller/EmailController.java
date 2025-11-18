package com.example.emailbulksender.controller;

import com.example.emailbulksender.dto.ApiResponse;
import com.example.emailbulksender.dto.EmailRequest;
import com.example.emailbulksender.service.EmailService;
import com.example.emailbulksender.service.ExcelReaderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/email")
@CrossOrigin(origins = "*")
@Slf4j
public class EmailController {

    @Autowired
    private ExcelReaderService excelReaderService;

    @Autowired
    private EmailService emailService;

    // Store uploaded emails temporarily (in production, use database or cache)
    private List<String> uploadedEmails = new ArrayList<>();

    // Store uploaded attachment file temporarily
    private File attachmentFile = null;
    private String attachmentFileName = null;

    private static final String UPLOAD_DIR = "uploads/attachments/";

    /**
     * Upload Excel/CSV file and extract email addresses
     */
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Please select a file to upload"));
            }

            // Read emails from file
            uploadedEmails = excelReaderService.readEmailsFromFile(file);

            if (uploadedEmails.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "No valid email addresses found in the file"));
            }

            log.info("Successfully extracted {} email addresses from file", uploadedEmails.size());

            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "File uploaded successfully. Found " + uploadedEmails.size() + " email addresses",
                    uploadedEmails.size()
            ));

        } catch (Exception e) {
            log.error("Error uploading file: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error uploading file: " + e.getMessage()));
        }
    }

    /**
     * Add email addresses manually
     */
    @PostMapping("/add-emails")
    public ResponseEntity<ApiResponse> addEmailsManually(@RequestBody List<String> emails) {
        try {
            if (emails == null || emails.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Please provide at least one email address"));
            }

            // Validate and add emails
            int validCount = 0;
            for (String email : emails) {
                if (email != null && !email.trim().isEmpty() && isValidEmail(email.trim())) {
                    if (!uploadedEmails.contains(email.trim())) {
                        uploadedEmails.add(email.trim());
                        validCount++;
                    }
                }
            }

            if (validCount == 0) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "No valid email addresses provided"));
            }

            log.info("Successfully added {} email addresses manually", validCount);

            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Successfully added " + validCount + " email address(es). Total: " + uploadedEmails.size(),
                    uploadedEmails.size()
            ));

        } catch (Exception e) {
            log.error("Error adding emails manually: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error adding emails: " + e.getMessage()));
        }
    }

    /**
     * Validate email format
     */
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    /**
     * Upload attachment file to send with emails
     */
    @PostMapping("/upload-attachment")
    public ResponseEntity<ApiResponse> uploadAttachment(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Please select a file to upload"));
            }

            // Create upload directory if not exists
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Save file
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Files.write(filePath, file.getBytes());

            // Clear previous attachment if exists
            if (attachmentFile != null && attachmentFile.exists()) {
                attachmentFile.delete();
            }

            attachmentFile = filePath.toFile();
            attachmentFileName = file.getOriginalFilename();

            log.info("Attachment file uploaded: {}", attachmentFileName);

            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Attachment uploaded successfully: " + attachmentFileName,
                    attachmentFileName
            ));

        } catch (Exception e) {
            log.error("Error uploading attachment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error uploading attachment: " + e.getMessage()));
        }
    }

    /**
     * Remove attachment file
     */
    @DeleteMapping("/attachment")
    public ResponseEntity<ApiResponse> removeAttachment() {
        try {
            if (attachmentFile != null && attachmentFile.exists()) {
                attachmentFile.delete();
                log.info("Attachment file removed: {}", attachmentFileName);
            }
            attachmentFile = null;
            attachmentFileName = null;

            return ResponseEntity.ok(new ApiResponse(true, "Attachment removed successfully"));
        } catch (Exception e) {
            log.error("Error removing attachment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error removing attachment: " + e.getMessage()));
        }
    }

    /**
     * Send bulk emails to all uploaded email addresses
     */
    @PostMapping("/send")
    public ResponseEntity<ApiResponse> sendBulkEmails(@RequestBody EmailRequest emailRequest) {
        try {
            if (uploadedEmails.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Please upload a file with email addresses first"));
            }

            if (emailRequest.getSubject() == null || emailRequest.getSubject().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Email subject is required"));
            }

            if (emailRequest.getMessage() == null || emailRequest.getMessage().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Email message is required"));
            }

            // Send emails asynchronously with or without attachment
            emailService.sendBulkEmails(
                    new ArrayList<>(uploadedEmails),
                    emailRequest.getSubject(),
                    emailRequest.getMessage(),
                    attachmentFile
            );

            String message = "Emails sending started successfully to " + uploadedEmails.size() + " recipients";
            if (attachmentFile != null) {
                message += " with attachment: " + attachmentFileName;
            }

            log.info("Bulk email sending initiated for {} recipients", uploadedEmails.size());

            return ResponseEntity.ok(new ApiResponse(true, message));

        } catch (Exception e) {
            log.error("Error sending emails: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error sending emails: " + e.getMessage()));
        }
    }

    /**
     * Get count of uploaded emails
     */
    @GetMapping("/count")
    public ResponseEntity<ApiResponse> getEmailCount() {
        return ResponseEntity.ok(new ApiResponse(
                true,
                "Email count retrieved successfully",
                uploadedEmails.size()
        ));
    }

    /**
     * Get attachment info
     */
    @GetMapping("/attachment-info")
    public ResponseEntity<ApiResponse> getAttachmentInfo() {
        if (attachmentFile != null && attachmentFile.exists()) {
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Attachment available",
                    attachmentFileName
            ));
        }
        return ResponseEntity.ok(new ApiResponse(false, "No attachment uploaded", null));
    }

    /**
     * Clear uploaded emails
     */
    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse> clearEmails() {
        uploadedEmails.clear();

        // Also clear attachment
        if (attachmentFile != null && attachmentFile.exists()) {
            attachmentFile.delete();
        }
        attachmentFile = null;
        attachmentFileName = null;

        return ResponseEntity.ok(new ApiResponse(true, "Email list and attachment cleared successfully"));
    }
}

