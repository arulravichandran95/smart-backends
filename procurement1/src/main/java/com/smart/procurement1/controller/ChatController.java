package com.smart.procurement1.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @PostMapping
    public Map<String, String> chat(@RequestBody Map<String, String> body) {
        String msg = body.get("message");

        if (msg == null || msg.trim().isEmpty()) {
            return Map.of("reply", "Please type a message.");
        }

        msg = msg.toLowerCase();
        String reply;

        if (msg.contains("hello") || msg.contains("hi")) {
            reply = "Hello,How can I assist you with procurement today?";
        } else if (msg.contains("quantity")) {
            reply = "Minimum 3 items are required for submission.";
        } else if (msg.contains("minimum price") || msg.contains("price")) {
            reply = "Pricing depends on quantity and approval rules.";
        } else if (msg.contains("approval")) {
            reply = "Requests above ₹30,000 require admin approval.";
        } else if (msg.contains("reject")) {
            reply = "Requests above 3 items to get Approval.";
        } else {
            reply = "Our procurement experts will assist you shortly.";
        }

        return Map.of("reply", reply);
    }
}
