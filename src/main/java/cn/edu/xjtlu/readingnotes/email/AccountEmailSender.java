package cn.edu.xjtlu.readingnotes.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;

@Component
public class AccountEmailSender {
    private static final Logger logger = LoggerFactory.getLogger(AccountEmailSender.class);
    
    // Move the configuration injection to the class scope
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private JavaMailSender mailSender;

    public void send(String to, String subject, String tpl, Object... args) 
            throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        Object[] plainArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof String) {
                // XSS protection: Filter HTML tags in email parameters
                plainArgs[i] = ((String) args[i]).replaceAll("</?.*?>", "");
            } else {
                plainArgs[i] = args[i];
            }
        }
        try {
            helper.setFrom(fromEmail);  // Use class member variables
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(
                String.format(tpl, plainArgs),
                String.format(tpl, args));
            mailSender.send(helper.getMimeMessage());
        } 
        catch (MailException ex) {
            logger.error("e-mail sending failed: {}", ex.getMessage());
            throw ex;
        }
        catch (Exception e) {
            logger.error("An unexpected error occurred: {}", e.getMessage());
            throw e;
        }
    }
}
