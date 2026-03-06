package com.brokerage.insure.rest.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class NotificationService {
    @Autowired
    private JavaMailSender mailSender;

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final int OTP_LENGTH = 6;

    public String generateSecureOtp() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(secureRandom.nextInt(10));
        }
        return otp.toString();
    }

    public void sendOtpEmail(String toEmail, String otp){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("musyokijoxhua@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Reica Verification Code");
        message.setText("Welcome to Reica! Your verification code is: " + otp +
                "\n\nThis code expires in 5 minutes. If you did not request this, please ignore this email.");

        mailSender.send(message);
        System.out.println("[Email Otp] To: " + toEmail +  " | Message: Welcome to Reica! Your verification code is: " + otp);
    }

    /**
     * Send OTP via SMS or WhatsApp
     * @param phoneNumber The customer's phone number
     * @param otp The verification code
     */
    public void sendOtpSms(String phoneNumber, String otp) {
        // TODO: Integrate with a real SMS/WhatsApp gateway (e.g., Twilio, Africa's Talking)
        // For now, we will log it to simulate the sending process.
       /* System.out.println("[Email Otp] To: " + phoneNumber +
                           " | Message: Welcome to Reica! Your verification code is: " + otp);*/
    }
}
