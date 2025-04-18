package cn.edu.xjtlu.readingnotes.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;

@Component
public class AccountEmailSender {

    @Autowired
    private JavaMailSender mailSender;

    public void send(String to, String subject, String tpl, Object... args)
            throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        Object[] plainArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof String) {
                plainArgs[i] = ((String) args[i])
                    .replaceAll("</?.*?>", "");
            } else {
                plainArgs[i] = args[i];
            }
        }
        try {
            helper.setFrom("your@smtp.mail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(
                String.format(tpl, plainArgs),
                String.format(tpl, args));
            mailSender.send(helper.getMimeMessage());
        } catch (Exception e) {
            throw e;
        }
    }
}
