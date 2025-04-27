package utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class MailUtil {

    private static final String HOST = "smtp.gmail.com";
    private static final int PORT = 587;
    private static final String EMAIL = "thohpce181027@fpt.edu.vn"; // Ghi trực tiếp
    private static final String APP_PASSWORD = "ybjbahltvuomtjeo";   // Ghi trực tiếp
    private static final Properties PROPERTIES = new Properties();

    static {
        PROPERTIES.put("mail.smtp.auth", "true");
        PROPERTIES.put("mail.smtp.starttls.enable", "true");
        PROPERTIES.put("mail.smtp.host", HOST);
        PROPERTIES.put("mail.smtp.port", String.valueOf(PORT));
    }

    // Gửi email HTML
    public static void sendEmail(List<String> recipients, String subject, String htmlContent) throws MessagingException {
        Session session = Session.getInstance(PROPERTIES, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL, APP_PASSWORD);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(EMAIL));

        Address[] toAddresses = recipients.stream()
                .map(email -> {
                    try {
                        return new InternetAddress(email);
                    } catch (AddressException e) {
                        throw new RuntimeException(e);
                    }
                }).toArray(Address[]::new);

        message.setRecipients(Message.RecipientType.TO, toAddresses);
        message.setSubject(subject);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(htmlContent, "text/html; charset=UTF-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);
    }

    public static String loadTemplate(String path) {
        try {
            String realPath = new java.io.File("src/main/webapp/" + path).getAbsolutePath();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new java.io.FileInputStream(realPath), "UTF-8"));
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void sendVerificationEmail(String to, String name, String code) {
        try {
            String subject = "Verify Your Food Delivery App Account";
            String content = "<h3>Dear " + name + ",</h3>"
                    + "<p>Thank you for registering with our Food Delivery App!</p>"
                    + "<p><strong>Verification Code:</strong> " + code + "</p>"
                    + "<p>This code is valid for 1 minute.</p>"
                    + "<br><p>Best regards,<br>Food Delivery Team</p>";

            sendEmail(List.of(to), subject, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
