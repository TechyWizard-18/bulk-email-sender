package com.example.emailbulksender.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HealthController {

    /**
     * Root health check endpoint
     */
    @GetMapping("/health")
    @ResponseBody
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Email Bulk Sender");
        return response;
    }

    /**
     * Root endpoint - redirects to index.html
     */
    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }
}

