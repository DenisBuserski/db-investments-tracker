package com.investments.tracker.service.email;

import com.investments.tracker.controller.report.WeeklyViewResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${application.mail.recepient}")
    private String recepient;

    private static final String WEEKLY_VIEW_REPORT_EMAIL_TEMPLATE = "weekly-view-report-email";

    public void sendEmailForWeeklyView(String subject, WeeklyViewResponse content, boolean html) {
        try {
            Context context = new Context();
            context.setVariable("content", content);
            String htmlContent = templateEngine.process(WEEKLY_VIEW_REPORT_EMAIL_TEMPLATE, context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(recepient);
            helper.setSubject(subject);
            helper.setText(htmlContent, html);

            mailSender.send(message);
            log.info("Email sent to {} successfully", recepient);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
