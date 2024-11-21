package com.gen_ai.interview.service;

import com.gen_ai.interview.dto.user.UserCheckVerificationCodeDTO;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final Map<String, String> verificationCodeStorage = new ConcurrentHashMap<>();
    private final Map<String, Long> verificationCodeTimestamp = new ConcurrentHashMap<>();
    private static final long CODE_EXPIRATION_TIME = 5 * 60 * 1000;


    public String sendUserEmail(String userEmail) {
        String verificationCode = generateVerificationCode();

        verificationCodeStorage.put(userEmail, verificationCode);
        log.info("VERIFY EMAIL : Stored {} for {}.", verificationCodeStorage.get(userEmail), userEmail);
        verificationCodeTimestamp.put(userEmail, System.currentTimeMillis());

        sendEmail(userEmail, verificationCode);

        return verificationCode;
    }

    public boolean checkUserVerificationCode(UserCheckVerificationCodeDTO userCheckVerificationCodeDTO) {
        String storedCode = verificationCodeStorage.get(userCheckVerificationCodeDTO.getEmail());
        log.info("VERIFY EMAIL : stored code for user {} is {}.", userCheckVerificationCodeDTO.getEmail(), storedCode);
        log.info("VERIFY EMAIL : received code for user {} is {}.", userCheckVerificationCodeDTO.getEmail(),
                userCheckVerificationCodeDTO.getVerificationCode());
        Long storedTimestamp = verificationCodeTimestamp.get(userCheckVerificationCodeDTO.getEmail());

        if (storedCode == null || storedTimestamp == null) {
            return false;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - storedTimestamp > CODE_EXPIRATION_TIME) {
            verificationCodeStorage.remove(userCheckVerificationCodeDTO.getEmail());
            verificationCodeTimestamp.remove(userCheckVerificationCodeDTO.getEmail());
            return false;
        }

        return storedCode.equals(userCheckVerificationCodeDTO.getVerificationCode());
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    private void sendEmail(String toEmail, String verificationCode) {
        final String fromEmail = "resumeplus365@gmail.com";
        final String password = "cvbr czzy bxgv bfsi";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Your Verification Code from Interview+");
            message.setText("Your verification code is: " + verificationCode);

            Transport.send(message);
            log.info("Email sent successfully to {}.", toEmail);

        } catch (MessagingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
