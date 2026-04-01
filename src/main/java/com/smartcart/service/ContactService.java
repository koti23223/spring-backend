package com.smartcart.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.smartcart.dto.ContactRequest;
import com.smartcart.entity.ContactQuery;
import com.smartcart.repository.ContactQueryRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class ContactService {

    @Autowired
    private ContactQueryRepository contactQueryRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.logo.url:https://via.placeholder.com/180x60?text=LIFESTYLE}")
    private String logoUrl;

    @Value("${app.company.name:LIFESTYLE}")
    private String companyName;

    @Value("${app.support.email:support@lifestyle.com}")
    private String supportEmail;

    @Value("${app.admin.email:admin@example.com}")
    private String adminEmail;

    public void saveQueryAndSendMail(ContactRequest request) {
        ContactQuery query = new ContactQuery();
        query.setName(request.getName());
        query.setEmail(request.getEmail());
        query.setMessage(request.getMessage());
        query.setCreatedAt(LocalDateTime.now());

        contactQueryRepository.save(query);

        try {
            sendAdminMail(request, query.getCreatedAt());
        } catch (Exception e) {
            throw new RuntimeException("Failed to send admin mail", e);
        }

        try {
            sendUserConfirmationMail(request, query.getCreatedAt());
        } catch (Exception e) {
            throw new RuntimeException("Failed to send user confirmation mail", e);
        }
    }

    private void sendAdminMail(ContactRequest request, LocalDateTime submittedAt) throws MessagingException {
        String formattedDate = submittedAt != null
                ? submittedAt.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"))
                : "N/A";

        String html = """
            <div style="margin:0; padding:24px; background:#eef2f7; font-family:Arial, sans-serif;">
              <div style="max-width:760px; margin:0 auto; background:#ffffff; border-radius:20px; overflow:hidden; box-shadow:0 12px 30px rgba(15,23,42,0.08);">

                <!-- Header -->
                <div style="background:linear-gradient(135deg,#0f172a,#2563eb); padding:30px 28px; text-align:center;">
                  <img src="%s" alt="%s" style="height:52px; max-width:190px; object-fit:contain; display:block; margin:0 auto 14px;" />
                  <div style="display:inline-block; background:rgba(255,255,255,0.12); color:#ffffff; font-size:12px; letter-spacing:1px; padding:6px 12px; border-radius:999px; margin-bottom:12px;">
                    NEW CONTACT QUERY
                  </div>
                  <h1 style="margin:0; font-size:30px; color:#ffffff;">New Message Received 📩</h1>
                  <p style="margin:10px 0 0; font-size:14px; color:#dbeafe;">
                    A new customer query has been submitted from the contact form
                  </p>
                </div>

                <!-- Body -->
                <div style="padding:30px 28px;">
                  <p style="margin:0 0 12px; font-size:17px; color:#111827;">
                    Hello Admin,
                  </p>

                  <p style="margin:0 0 24px; font-size:15px; color:#4b5563; line-height:1.8;">
                    You have received a new support or inquiry message from your website contact page.
                    Below are the submitted details.
                  </p>

                  <!-- Query Info -->
                  <div style="background:#f8fafc; border:1px solid #e5e7eb; border-radius:16px; padding:18px 20px; margin-bottom:24px;">
                    <table style="width:100%%; border-collapse:collapse; font-size:14px;">
                      <tr>
                        <td style="padding:10px 0; color:#6b7280;">Customer Name</td>
                        <td style="padding:10px 0; text-align:right; font-weight:700; color:#111827;">%s</td>
                      </tr>
                      <tr>
                        <td style="padding:10px 0; color:#6b7280;">Email Address</td>
                        <td style="padding:10px 0; text-align:right; font-weight:700; color:#111827;">%s</td>
                      </tr>
                      <tr>
                        <td style="padding:10px 0; color:#6b7280;">Submitted At</td>
                        <td style="padding:10px 0; text-align:right; font-weight:700; color:#111827;">%s</td>
                      </tr>
                    </table>
                  </div>

                  <!-- Message -->
                  <div style="margin-bottom:24px;">
                    <h2 style="margin:0 0 14px; font-size:21px; color:#111827;">Customer Message</h2>
                    <div style="background:#ffffff; border:1px solid #e5e7eb; border-radius:16px; padding:18px 20px; color:#374151; font-size:14px; line-height:1.8;">
                      %s
                    </div>
                  </div>

                  <!-- CTA -->
                  <div style="text-align:center; margin:0 0 26px;">
                    <a href="mailto:%s"
                       style="display:inline-block; background:#2563eb; color:#ffffff; text-decoration:none; font-weight:700; font-size:14px; padding:12px 22px; border-radius:10px; margin:0 6px 10px;">
                      Reply to Customer
                    </a>
                  </div>

                  <!-- Support -->
                  <div style="background:#fff7ed; border:1px solid #fed7aa; border-radius:14px; padding:16px;">
                    <p style="margin:0; color:#9a3412; font-size:14px; line-height:1.7;">
                      Please review and respond to this query as soon as possible for a better customer experience.
                    </p>
                  </div>
                </div>

                <!-- Footer -->
                <div style="background:#f8fafc; border-top:1px solid #e5e7eb; padding:18px; text-align:center;">
                  <p style="margin:0 0 6px; font-size:13px; color:#374151; font-weight:700;">%s</p>
                  <p style="margin:0; font-size:12px; color:#6b7280;">
                    Contact form notification system
                  </p>
                </div>
              </div>
            </div>
            """.formatted(
                logoUrl,
                escapeHtml(companyName),
                escapeHtml(valueOrNA(request.getName())),
                escapeHtml(valueOrNA(request.getEmail())),
                formattedDate,
                formatMessage(request.getMessage()),
                escapeHtml(valueOrNA(request.getEmail())),
                escapeHtml(companyName)
        );

        sendHtmlEmail(adminEmail, "New Contact Query Received", html);
    }

    private void sendUserConfirmationMail(ContactRequest request, LocalDateTime submittedAt) throws MessagingException {
        String formattedDate = submittedAt != null
                ? submittedAt.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"))
                : "N/A";

        String html = """
            <div style="margin:0; padding:24px; background:#eef2f7; font-family:Arial, sans-serif;">
              <div style="max-width:760px; margin:0 auto; background:#ffffff; border-radius:20px; overflow:hidden; box-shadow:0 12px 30px rgba(15,23,42,0.08);">

                <!-- Header -->
                <div style="background:linear-gradient(135deg,#0f172a,#2563eb); padding:30px 28px; text-align:center;">
                  <img src="%s" alt="%s" style="height:52px; max-width:190px; object-fit:contain; display:block; margin:0 auto 14px;" />
                  <div style="display:inline-block; background:rgba(255,255,255,0.12); color:#ffffff; font-size:12px; letter-spacing:1px; padding:6px 12px; border-radius:999px; margin-bottom:12px;">
                    QUERY RECEIVED
                  </div>
                  <h1 style="margin:0; font-size:30px; color:#ffffff;">Thank You for Contacting Us 💙</h1>
                  <p style="margin:10px 0 0; font-size:14px; color:#dbeafe;">
                    We have successfully received your message
                  </p>
                </div>

                <!-- Body -->
                <div style="padding:30px 28px;">
                  <p style="margin:0 0 12px; font-size:17px; color:#111827;">
                    Hello <strong>%s</strong>,
                  </p>

                  <p style="margin:0 0 24px; font-size:15px; color:#4b5563; line-height:1.8;">
                    Thank you for reaching out to %s. Your message has been submitted successfully.
                    Our support team will review it and get back to you as soon as possible.
                  </p>

                  <!-- Query Summary -->
                  <div style="background:#f8fafc; border:1px solid #e5e7eb; border-radius:16px; padding:18px 20px; margin-bottom:24px;">
                    <table style="width:100%%; border-collapse:collapse; font-size:14px;">
                      <tr>
                        <td style="padding:10px 0; color:#6b7280;">Submitted At</td>
                        <td style="padding:10px 0; text-align:right; font-weight:700; color:#111827;">%s</td>
                      </tr>
                      <tr>
                        <td style="padding:10px 0; color:#6b7280;">Support Email</td>
                        <td style="padding:10px 0; text-align:right; font-weight:700; color:#111827;">%s</td>
                      </tr>
                    </table>
                  </div>

                  <!-- Customer Message -->
                  <h2 style="margin:0 0 14px; font-size:21px; color:#111827;">Your Message</h2>
                  <div style="margin-bottom:24px; background:#ffffff; border:1px solid #e5e7eb; border-radius:16px; padding:18px 20px; color:#374151; font-size:14px; line-height:1.8;">
                    %s
                  </div>

                  <!-- CTA -->
                  <div style="text-align:center; margin:0 0 26px;">
                    <a href="mailto:%s"
                       style="display:inline-block; background:#2563eb; color:#ffffff; text-decoration:none; font-weight:700; font-size:14px; padding:12px 22px; border-radius:10px; margin:0 6px 10px;">
                      Contact Support
                    </a>
                  </div>

                  <!-- Support Box -->
                  <div style="background:#eff6ff; border:1px solid #bfdbfe; border-radius:16px; padding:18px 20px; margin-bottom:24px;">
                    <h3 style="margin:0 0 10px; font-size:17px; color:#111827;">What Happens Next?</h3>
                    <p style="margin:0; color:#374151; line-height:1.8; font-size:14px;">
                      Our team will review your request and respond to your email address if any further details are needed.
                    </p>
                  </div>

                  <!-- Note -->
                  <div style="background:#fff7ed; border:1px solid #fed7aa; border-radius:14px; padding:16px;">
                    <p style="margin:0; color:#9a3412; font-size:14px; line-height:1.7;">
                      This is an automated confirmation email. Please do not reply directly unless monitored by your team.
                    </p>
                  </div>
                </div>

                <!-- Footer -->
                <div style="background:#f8fafc; border-top:1px solid #e5e7eb; padding:18px; text-align:center;">
                  <p style="margin:0 0 6px; font-size:13px; color:#374151; font-weight:700;">%s</p>
                  <p style="margin:0; font-size:12px; color:#6b7280;">
                    Premium support and customer communication experience
                  </p>
                </div>
              </div>
            </div>
            """.formatted(
                logoUrl,
                escapeHtml(companyName),
                escapeHtml(valueOrNA(request.getName())),
                escapeHtml(companyName),
                formattedDate,
                escapeHtml(valueOrNA(supportEmail)),
                formatMessage(request.getMessage()),
                escapeHtml(valueOrNA(supportEmail)),
                escapeHtml(companyName)
        );

        sendHtmlEmail(request.getEmail(), "We Received Your Query", html);
    }

    private void sendHtmlEmail(String toEmail, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper =
                new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    private String valueOrNA(String value) {
        return (value == null || value.isBlank()) ? "N/A" : value;
    }

    private String escapeHtml(String value) {
        return value == null ? "" : value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private String formatMessage(String message) {
        return escapeHtml(valueOrNA(message)).replace("\n", "<br>");
    }
}