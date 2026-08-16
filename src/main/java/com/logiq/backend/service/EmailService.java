package com.logiq.backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    //OTP send mail content
    public void sendOtpEmail(String toEmail, String otp, String firstName) {
        String subject = "LogIQ - Verify Your Email Address";
        String logoUrl = "https://i.postimg.cc/ZqQsttRw/pure-logo.png";

        String htmlContent = """
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #0f1117; margin: 0; padding: 0; }
                        .container { max-width: 600px; margin: 40px auto; background-color: #1A1D27; border-radius: 12px; overflow: hidden; box-shadow: 0 8px 30px rgba(0,0,0,0.5); border: 1px solid #2d3748; }
                        .header { background: linear-gradient(135deg, #3b82f6 0%%, #8b5cf6 100%%); padding: 30px; text-align: center; }
                        .header img { max-width: 120px; height: auto; margin-bottom: 10px; }
                        .header h1 { color: #ffffff; margin: 0; font-size: 24px; font-weight: 700; letter-spacing: 1px; }
                        .content { padding: 40px 30px; color: #e2e8f0; line-height: 1.6; }
                        .content p { margin: 0 0 20px 0; font-size: 16px; }
                        .otp-container { background-color: #0f1117; border-radius: 8px; padding: 25px; text-align: center; margin: 30px 0; border: 1px dashed #4c1d95; }
                        .otp-code { font-size: 36px; font-weight: 800; color: #a78bfa; letter-spacing: 8px; margin: 0; }
                        .footer { background-color: #13151c; padding: 20px; text-align: center; font-size: 13px; color: #64748b; border-top: 1px solid #2d3748; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <img src="%s" alt="LogIQ Logo">
                            <h1>Verify Your Account</h1>
                        </div>
                        <div class="content">
                            <p>Hi <strong>%s</strong>,</p>
                            <p>Welcome to LogIQ! We're thrilled to have you on board. To complete your registration and secure your new account, please enter the verification code below:</p>
                            
                            <div class="otp-container">
                                <p class="otp-code">%s</p>
                            </div>
                            
                            <p>This secure code will expire in <strong>10 minutes</strong>. If you did not request this verification, please ignore this email.</p>
                        </div>
                        <div class="footer">
                            <p>&copy; 2026 LogIQ Observability. All rights reserved.</p>
                            <p> Sri Lanka</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(logoUrl, firstName, otp);

        sendHtmlEmail(toEmail, subject, htmlContent);
    }

//send collaboration invite mail
    public void sendCollaborationInvite(String toEmail, String projectName, String inviterName, String role, Long projectId) {
        String subject = "LogIQ - Invitation to collaborate on " + projectName;
        String logoUrl = "https://i.postimg.cc/ZqQsttRw/pure-logo.png";


        String frontendUrl = "http://localhost:5173/project/" + projectId;

        String htmlContent = """
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #0f1117; margin: 0; padding: 0; }
                        .container { max-width: 600px; margin: 40px auto; background-color: #1A1D27; border-radius: 12px; overflow: hidden; box-shadow: 0 8px 30px rgba(0,0,0,0.5); border: 1px solid #2d3748; }
                        .header { background: linear-gradient(135deg, #3b82f6 0%%, #8b5cf6 100%%); padding: 30px; text-align: center; }
                        .header img { max-width: 120px; height: auto; margin-bottom: 10px; }
                        .header h1 { color: #ffffff; margin: 0; font-size: 24px; font-weight: 700; letter-spacing: 1px; }
                        .content { padding: 40px 30px; color: #e2e8f0; line-height: 1.6; }
                        .content p { margin: 0 0 20px 0; font-size: 16px; }
                        .highlight { color: #a78bfa; font-weight: 600; }
                        .btn-container { text-align: center; margin: 35px 0; }
                        .btn { background: linear-gradient(135deg, #3b82f6 0%%, #8b5cf6 100%%); color: #ffffff; padding: 14px 32px; text-decoration: none; border-radius: 8px; font-weight: 700; font-size: 16px; display: inline-block; box-shadow: 0 4px 15px rgba(139, 92, 246, 0.3); }
                        .btn:hover { background: linear-gradient(135deg, #2563eb 0%%, #7c3aed 100%%); }
                        .footer { background-color: #13151c; padding: 20px; text-align: center; font-size: 13px; color: #64748b; border-top: 1px solid #2d3748; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <img src="%s" alt="LogIQ Logo">
                            <h1>Project Invitation</h1>
                        </div>
                        <div class="content">
                            <p>Hello,</p>
                            <p>You have been invited by <span class="highlight">%s</span> to collaborate on the LogIQ project <strong>%s</strong>.</p>
                            <p>You have been assigned the role of <strong>%s</strong>. Click the button below to access the project dashboard and start collaborating.</p>
                            
                            <div class="btn-container">
                                <a href="%s" class="btn">View Project</a>
                            </div>
                            
                            <p>If you don't have a LogIQ account yet, please register using this email address to accept the invitation.</p>
                        </div>
                        <div class="footer">
                            <p>&copy; 2026 LogIQ Observability. All rights reserved.</p>
                            <p>Sri Lanka</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(logoUrl, inviterName, projectName, role, frontendUrl);

        sendHtmlEmail(toEmail, subject, htmlContent);
    }

    public void sendRoleUpdateEmail(String toEmail, String projectName, String newRole, Long projectId) {
        String subject = "LogIQ - Your role in " + projectName + " has been updated";
        String logoUrl = "https://i.postimg.cc/ZqQsttRw/pure-logo.png";
        String frontendUrl = "http://localhost:5173/project/" + projectId;

        String htmlContent = """
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #0f1117; margin: 0; padding: 0; }
                        .container { max-width: 600px; margin: 40px auto; background-color: #1A1D27; border-radius: 12px; overflow: hidden; box-shadow: 0 8px 30px rgba(0,0,0,0.5); border: 1px solid #2d3748; }
                        .header { background: linear-gradient(135deg, #3b82f6 0%%, #8b5cf6 100%%); padding: 30px; text-align: center; }
                        .header img { max-width: 120px; height: auto; margin-bottom: 10px; }
                        .header h1 { color: #ffffff; margin: 0; font-size: 24px; font-weight: 700; letter-spacing: 1px; }
                        .content { padding: 40px 30px; color: #e2e8f0; line-height: 1.6; }
                        .content p { margin: 0 0 20px 0; font-size: 16px; }
                        .highlight { color: #a78bfa; font-weight: 600; }
                        .btn-container { text-align: center; margin: 35px 0; }
                        .btn { background: linear-gradient(135deg, #3b82f6 0%%, #8b5cf6 100%%); color: #ffffff; padding: 14px 32px; text-decoration: none; border-radius: 8px; font-weight: 700; font-size: 16px; display: inline-block; box-shadow: 0 4px 15px rgba(139, 92, 246, 0.3); }
                        .btn:hover { background: linear-gradient(135deg, #2563eb 0%%, #7c3aed 100%%); }
                        .footer { background-color: #13151c; padding: 20px; text-align: center; font-size: 13px; color: #64748b; border-top: 1px solid #2d3748; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <img src="%s" alt="LogIQ Logo">
                            <h1>Role Updated</h1>
                        </div>
                        <div class="content">
                            <p>Hello,</p>
                            <p>Your access level for the project <strong class="highlight">%s</strong> has been updated.</p>
                            <p>Your new role is: <strong>%s</strong>.</p>
                            
                            <div class="btn-container">
                                <a href="%s" class="btn">Go to Project</a>
                            </div>
                        </div>
                        <div class="footer">
                            <p>&copy; 2026 LogIQ Observability. All rights reserved.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(logoUrl, projectName, newRole, frontendUrl);

        sendHtmlEmail(toEmail, subject, htmlContent);
    }


    public void sendProjectRemovalEmail(String toEmail, String projectName) {
        String subject = "LogIQ - Access removed from " + projectName;
        String logoUrl = "https://i.postimg.cc/ZqQsttRw/pure-logo.png";

        String htmlContent = """
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #0f1117; margin: 0; padding: 0; }
                        .container { max-width: 600px; margin: 40px auto; background-color: #1A1D27; border-radius: 12px; overflow: hidden; box-shadow: 0 8px 30px rgba(0,0,0,0.5); border: 1px solid #2d3748; }
                        .header { background: linear-gradient(135deg, #ef4444 0%%, #b91c1c 100%%); padding: 30px; text-align: center; } /* රතු පාටින් Warning එකක් විදිහට */
                        .header img { max-width: 120px; height: auto; margin-bottom: 10px; }
                        .header h1 { color: #ffffff; margin: 0; font-size: 24px; font-weight: 700; letter-spacing: 1px; }
                        .content { padding: 40px 30px; color: #e2e8f0; line-height: 1.6; }
                        .content p { margin: 0 0 20px 0; font-size: 16px; }
                        .highlight { color: #ef4444; font-weight: 600; }
                        .footer { background-color: #13151c; padding: 20px; text-align: center; font-size: 13px; color: #64748b; border-top: 1px solid #2d3748; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <img src="%s" alt="LogIQ Logo">
                            <h1>Access Revoked</h1>
                        </div>
                        <div class="content">
                            <p>Hello,</p>
                            <p>Your access to the project <strong class="highlight">%s</strong> has been removed by the project owner.</p>
                            <p>You will no longer be able to view or manage logs, health metrics, or settings for this project. If you believe this is a mistake, please contact the project owner directly.</p>
                        </div>
                        <div class="footer">
                            <p>&copy; 2026 LogIQ Observability. All rights reserved.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(logoUrl, projectName);

        sendHtmlEmail(toEmail, subject, htmlContent);
    }
}