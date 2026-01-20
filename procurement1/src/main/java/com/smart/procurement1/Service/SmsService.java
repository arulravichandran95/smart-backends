package com.smart.procurement1.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class SmsService {

    @Value("${twilio.account_sid:}")
    private String accountSid;

    @Value("${twilio.auth_token:}")
    private String authToken;

    @Value("${twilio.phone_number:}")
    private String fromPhoneNumber;

    private boolean isTwilioInitialized = false;

    @PostConstruct
    public void init() {
        // Initialize Twilio with credentials
        // Use a simple check to avoid crashing if placeholder values are left
        if (accountSid != null && !accountSid.isEmpty() &&
                authToken != null && !authToken.isEmpty() &&
                !"your_account_sid".equals(accountSid)) {
            try {
                Twilio.init(accountSid, authToken);
                isTwilioInitialized = true;
                System.out.println("Twilio initialized successfully.");
            } catch (Exception e) {
                System.err.println("Twilio initialization failed: " + e.getMessage());
            }
        } else {
            System.out.println("Twilio credentials not configured. SMS service will be disabled.");
        }
    }

    public void sendSms(String to, String messageBody) {
        if (!isTwilioInitialized) {
            System.out.println(" [SMS SERVICE] Skipped: Twilio not initialized.");
            return;
        }

        System.out.println("========================================");
        System.out.println(" [SMS SERVICE] Request received for: " + to);

        // Simple E.164 formatting for India (defaulting to +91 if missing)
        String formattedTo = to.trim();
        if (formattedTo.matches("\\d{10}")) {
            formattedTo = "+91" + formattedTo;
            System.out.println(" [SMS SERVICE] Auto-formatted to: " + formattedTo);
        } else if (!formattedTo.startsWith("+")) {
            // If it doesn't start with +, warn or try to send anyway (Twilio might reject)
            System.out.println(" [SMS SERVICE] Warning: Phone number might be missing country code.");
        }

        try {
            Message message = Message.creator(
                    new PhoneNumber(formattedTo), // To
                    new PhoneNumber(fromPhoneNumber), // From
                    messageBody).create();

            System.out.println(" [SMS SERVICE] SMS sent successfully! SID: " + message.getSid());
        } catch (Exception e) {
            System.err.println(" [SMS SERVICE] FAILED to send SMS: " + e.getMessage());
            e.printStackTrace(); // Print full stack trace to see exactly why it failed
        }
        System.out.println("========================================");
    }
}
