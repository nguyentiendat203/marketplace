package vn.datnguy3n.marketplace.modules.mail;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import vn.datnguy3n.marketplace.modules.user.entity.User;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${application.mail.base-url}")
    private String baseUrl;

    @Value("${application.mail.activation-ttl-minutes}")
    private long ttlMinutes;

    @Async
    public void sendPasswordResetEmail(User user, String token) {
        try {
            Context context = new Context();
            context.setVariable("username", user.getFullName());
            context.setVariable("token", token);

            String htmlContent = templateEngine.process("mail/password-reset-email", context);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(user.getEmail());
            helper.setSubject("Mã khôi phục mật khẩu của bạn");
            helper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
            log.info("Password reset email sent successfully to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send password reset email to: {}", user.getEmail(), e);
        }
    }

    @Async
    public void sendActivationEmail(User user) {
        try {
            Context context = new Context();
            context.setVariable("username", user.getFullName());
            context.setVariable("activationUrl", baseUrl + "/api/v1/auth/activate?key=" + user.getActivationKey());
            context.setVariable("ttlMinutes", ttlMinutes);

            String htmlContent = templateEngine.process("mail/activation-email", context);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(user.getEmail());
            helper.setSubject("Xác thực tài khoản của bạn");
            helper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
            log.info("Activation email sent successfully to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send activation email to: {}", user.getEmail(), e);
        }
    }
}
