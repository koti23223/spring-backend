package com.smartcart.service;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.smartcart.entity.Order;
import com.smartcart.entity.OrderItem;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Replace with your real public logo URL
    @Value("${app.logo.url:https://via.placeholder.com/180x60?text=LIFESTYLE}")
    private String logoUrl;

    // Replace with your real frontend URL
    @Value("${app.frontend.url:http://localhost:5173}")
    private String frontendUrl;

    public void sendOtp(String toEmail, String otp) {
        try {
            String html = """
                <div style="margin:0; padding:24px; background:#f3f4f6; font-family:Arial, sans-serif;">
                  <div style="max-width:620px; margin:0 auto; background:#ffffff; border-radius:18px; overflow:hidden; box-shadow:0 10px 28px rgba(15,23,42,0.08);">

                    <div style="background:linear-gradient(135deg,#0f172a,#2563eb); padding:28px; text-align:center;">
                      <img src="%s" alt="LIFESTYLE" style="height:46px; max-width:180px; object-fit:contain; display:block; margin:0 auto 12px;" />
                      <h1 style="margin:0; font-size:24px; color:#ffffff;">Password Reset OTP</h1>
                      <p style="margin:8px 0 0; color:#dbeafe; font-size:14px;">Secure verification for your account</p>
                    </div>

                    <div style="padding:30px;">
                      <p style="margin:0 0 14px; font-size:16px; color:#111827;">Hello,</p>

                      <p style="margin:0 0 18px; font-size:15px; color:#4b5563; line-height:1.7;">
                        Use the OTP below to reset your password. This OTP is valid for 5 minutes.
                      </p>

                      <div style="text-align:center; margin:24px 0;">
                        <span style="display:inline-block; padding:16px 28px; border-radius:12px; background:#eff6ff; color:#2563eb; font-size:30px; font-weight:700; letter-spacing:8px;">
                          %s
                        </span>
                      </div>

                      <div style="background:#fff7ed; border:1px solid #fed7aa; color:#9a3412; border-radius:12px; padding:14px; font-size:14px; line-height:1.6;">
                        Do not share this OTP with anyone. Our team will never ask for your OTP.
                      </div>
                    </div>

                    <div style="padding:16px; background:#f8fafc; border-top:1px solid #e5e7eb; text-align:center; font-size:12px; color:#6b7280;">
                      © LIFESTYLE • Secure Account Communication
                    </div>
                  </div>
                </div>
                """.formatted(logoUrl, otp);

            sendHtmlEmail(toEmail, "Password Reset OTP", html);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    public void sendOrderConfirmationEmail(Order order) {
        try {
            String formattedDate = order.getOrderDate() != null
                    ? order.getOrderDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"))
                    : "N/A";

            String itemsHtml = order.getItems()
                    .stream()
                    .map(this::buildPremiumItemCard)
                    .collect(Collectors.joining());

            String trackOrderUrl = frontendUrl + "/orders";
            String continueShoppingUrl = frontendUrl + "/";

            String html = """
                <div style="margin:0; padding:24px; background:#eef2f7; font-family:Arial, sans-serif;">
                  <div style="max-width:760px; margin:0 auto; background:#ffffff; border-radius:20px; overflow:hidden; box-shadow:0 12px 30px rgba(15,23,42,0.08);">

                    <!-- Header -->
                    <div style="background:linear-gradient(135deg,#0f172a,#2563eb); padding:30px 28px; text-align:center;">
                      <img src="%s" alt="LIFESTYLE" style="height:52px; max-width:190px; object-fit:contain; display:block; margin:0 auto 14px;" />
                      <div style="display:inline-block; background:rgba(255,255,255,0.12); color:#ffffff; font-size:12px; letter-spacing:1px; padding:6px 12px; border-radius:999px; margin-bottom:12px;">
                        ORDER SUCCESS
                      </div>
                      <h1 style="margin:0; font-size:30px; color:#ffffff;">Order Confirmed 🎉</h1>
                      <p style="margin:10px 0 0; font-size:14px; color:#dbeafe;">
                        Thank you for shopping with LIFESTYLE
                      </p>
                    </div>

                    <!-- Body -->
                    <div style="padding:30px 28px;">
                      <p style="margin:0 0 12px; font-size:17px; color:#111827;">
                        Hello <strong>%s</strong>,
                      </p>

                      <p style="margin:0 0 24px; font-size:15px; color:#4b5563; line-height:1.8;">
                        Your order has been placed successfully. We’re getting everything ready for delivery.
                        Below are your order details.
                      </p>

                      <!-- Order info -->
                      <div style="background:#f8fafc; border:1px solid #e5e7eb; border-radius:16px; padding:18px 20px; margin-bottom:24px;">
                        <table style="width:100%%; border-collapse:collapse; font-size:14px;">
                          <tr>
                            <td style="padding:8px 0; color:#6b7280;">Order ID</td>
                            <td style="padding:8px 0; text-align:right; font-weight:700; color:#111827;">%s</td>
                          </tr>
                          <tr>
                            <td style="padding:8px 0; color:#6b7280;">Order Date</td>
                            <td style="padding:8px 0; text-align:right; font-weight:700; color:#111827;">%s</td>
                          </tr>
                          <tr>
                            <td style="padding:8px 0; color:#6b7280;">Payment Method</td>
                            <td style="padding:8px 0; text-align:right; font-weight:700; color:#111827;">%s</td>
                          </tr>
                          <tr>
                            <td style="padding:8px 0; color:#6b7280;">Payment ID</td>
                            <td style="padding:8px 0; text-align:right; font-weight:700; color:#111827;">%s</td>
                          </tr>
                        </table>
                      </div>

                      <!-- CTA buttons -->
                      <div style="text-align:center; margin:0 0 26px;">
                        <a href="%s"
                           style="display:inline-block; background:#2563eb; color:#ffffff; text-decoration:none; font-weight:700; font-size:14px; padding:12px 22px; border-radius:10px; margin:0 6px 10px;">
                          Track Order
                        </a>
                        <a href="%s"
                           style="display:inline-block; background:#ffffff; color:#111827; text-decoration:none; font-weight:700; font-size:14px; padding:12px 22px; border-radius:10px; border:1px solid #d1d5db; margin:0 6px 10px;">
                          Continue Shopping
                        </a>
                      </div>

                      <!-- Products -->
                      <h2 style="margin:0 0 14px; font-size:21px; color:#111827;">Your Products</h2>
                      <div style="margin-bottom:24px;">
                        %s
                      </div>

                      <!-- Summary -->
                      <div style="background:#f8fafc; border:1px solid #e5e7eb; border-radius:16px; padding:18px 20px; margin-bottom:24px;">
                        <h3 style="margin:0 0 14px; font-size:17px; color:#111827;">Order Summary</h3>
                        <table style="width:100%%; border-collapse:collapse; font-size:14px;">
                          <tr>
                            <td style="padding:8px 0; color:#6b7280;">Subtotal</td>
                            <td style="padding:8px 0; text-align:right; font-weight:600; color:#111827;">₹%s</td>
                          </tr>
                          <tr>
                            <td style="padding:8px 0; color:#6b7280;">GST</td>
                            <td style="padding:8px 0; text-align:right; font-weight:600; color:#111827;">₹%s</td>
                          </tr>
                          <tr>
                            <td style="padding:12px 0 0; font-size:16px; font-weight:700; color:#111827;">Total Amount</td>
                            <td style="padding:12px 0 0; text-align:right; font-size:20px; font-weight:700; color:#2563eb;">₹%s</td>
                          </tr>
                        </table>
                      </div>

                      <!-- Address -->
                      <div style="background:#eff6ff; border:1px solid #bfdbfe; border-radius:16px; padding:18px 20px; margin-bottom:24px;">
                        <h3 style="margin:0 0 10px; font-size:17px; color:#111827;">Delivery Address</h3>
                        <p style="margin:0; color:#374151; line-height:1.8; font-size:14px;">
                          %s<br>
                          %s, %s<br>
                          %s
                        </p>
                      </div>

                      <!-- Support -->
                      <div style="background:#fff7ed; border:1px solid #fed7aa; border-radius:14px; padding:16px;">
                        <p style="margin:0; color:#9a3412; font-size:14px; line-height:1.7;">
                          Need help with your order? Reply to this email or contact our support team from your account.
                        </p>
                      </div>
                    </div>

                    <!-- Footer -->
                    <div style="background:#f8fafc; border-top:1px solid #e5e7eb; padding:18px; text-align:center;">
                      <p style="margin:0 0 6px; font-size:13px; color:#374151; font-weight:700;">LIFESTYLE</p>
                      <p style="margin:0; font-size:12px; color:#6b7280;">
                        Premium fashion & lifestyle shopping experience
                      </p>
                    </div>
                  </div>
                </div>
                """.formatted(
                    logoUrl,
                    valueOrNA(order.getFullName()),
                    valueOrNA(order.getOrderId()),
                    formattedDate,
                    valueOrNA(order.getPaymentMethod()),
                    valueOrNA(order.getPaymentId()),
                    trackOrderUrl,
                    continueShoppingUrl,
                    itemsHtml,
                    formatMoney(order.getSubtotal()),
                    formatMoney(order.getTax()),
                    formatMoney(order.getTotalAmount()),
                    valueOrNA(order.getStreetAddress()),
                    valueOrNA(order.getCity()),
                    valueOrNA(order.getZipCode()),
                    valueOrNA(order.getCountry())
                );

            sendHtmlEmail(
                    order.getUserEmail(),
                    "Order Confirmation - " + order.getOrderId(),
                    html
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to send order confirmation email", e);
        }
    }

    private String buildPremiumItemCard(OrderItem item) {
        String imageUrl = valueOrPlaceholder(item.getProductImageUrl());

        return """
            <div style="border:1px solid #e5e7eb; border-radius:16px; padding:14px; margin-bottom:14px; background:#ffffff;">
              <table style="width:100%%; border-collapse:collapse;">
                <tr>
                  <td style="width:96px; vertical-align:top; padding-right:14px;">
                    <img src="%s" alt="%s"
                         style="width:90px; height:90px; object-fit:cover; border-radius:12px; display:block; background:#f3f4f6; border:1px solid #e5e7eb;" />
                  </td>
                  <td style="vertical-align:top;">
                    <div style="font-size:16px; font-weight:700; color:#111827; margin-bottom:8px;">
                      %s
                    </div>
                    <div style="font-size:14px; color:#6b7280; margin-bottom:6px;">
                      Quantity: <strong style="color:#111827;">%s</strong>
                    </div>
                    <div style="font-size:14px; color:#6b7280;">
                      Price: <strong style="color:#111827;">₹%s</strong>
                    </div>
                  </td>
                </tr>
              </table>
            </div>
            """.formatted(
                imageUrl,
                escapeHtml(valueOrNA(item.getProductTitle())),
                escapeHtml(valueOrNA(item.getProductTitle())),
                item.getQuantity() == null ? 0 : item.getQuantity(),
                formatMoney(item.getPrice())
            );
    }

    private void sendHtmlEmail(String toEmail, String subject, String htmlContent)
            throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper =
                new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    private String formatMoney(Double value) {
        return String.format("%.2f", value == null ? 0.0 : value);
    }

    private String valueOrNA(String value) {
        return (value == null || value.isBlank()) ? "N/A" : value;
    }

    private String valueOrPlaceholder(String value) {
        return (value == null || value.isBlank())
                ? "https://via.placeholder.com/90x90?text=Product"
                : value;
    }

    private String escapeHtml(String value) {
        return value == null ? "" : value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}