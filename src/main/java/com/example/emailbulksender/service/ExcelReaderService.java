package com.example.emailbulksender.service;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelReaderService {

    /**
     * Read email addresses from uploaded Excel or CSV file
     * @param file - uploaded file (.xlsx or .csv)
     * @return List of email addresses
     * @throws IOException
     */
    public List<String> readEmailsFromFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        if (fileName == null) {
            throw new IllegalArgumentException("File name is null");
        }

        if (fileName.endsWith(".csv")) {
            return readEmailsFromCsv(file);
        } else if (fileName.endsWith(".xlsx") || fileName.endsWith(".xls")) {
            return readEmailsFromExcel(file);
        } else {
            throw new IllegalArgumentException("Unsupported file format. Please upload .xlsx or .csv file");
        }
    }

    /**
     * Read emails from Excel file (.xlsx or .xls)
     */
    private List<String> readEmailsFromExcel(MultipartFile file) throws IOException {
        List<String> emails = new ArrayList<>();

        try {
            // WorkbookFactory automatically detects the format
            Workbook workbook = WorkbookFactory.create(file.getInputStream());

            Sheet sheet = workbook.getSheetAt(0); // Read first sheet

            for (Row row : sheet) {
                if (row == null) continue;

                Cell cell = row.getCell(0); // Read first column
                if (cell != null) {
                    String email = getCellValueAsString(cell);
                    if (email != null && !email.trim().isEmpty() && isValidEmail(email)) {
                        emails.add(email.trim());
                    }
                }
            }

            workbook.close();
        } catch (Exception e) {
            throw new IOException("Error reading Excel file: " + e.getMessage(), e);
        }

        return emails;
    }

    /**
     * Read emails from CSV file
     */
    private List<String> readEmailsFromCsv(MultipartFile file) throws IOException {
        List<String> emails = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String email = line.split(",")[0].trim(); // Get first column
                if (!email.isEmpty() && isValidEmail(email)) {
                    emails.add(email);
                }
            }
        }

        return emails;
    }

    /**
     * Convert cell value to string
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    /**
     * Basic email validation
     */
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}

