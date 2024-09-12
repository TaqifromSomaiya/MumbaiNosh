package com.example.mumbainosh;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendOTPActivity extends AppCompatActivity {

    private String generatedOTP;
    private EditText etEmail;
    private Button btnSendOTP;

    private final String smtpHost = "smtp.gmail.com";
    private final String smtpPort = "587";
    private final String fromEmail = "equiflow.equitec@gmail.com";
    private final String fromPassword = "jhng evex etbk weco";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_send_otpactivity);
        etEmail = findViewById(R.id.etEmail);
        btnSendOTP = findViewById(R.id.btnSendOTP);

        btnSendOTP.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (isValidEmail(email)) {
                generatedOTP = generateOTP();  // Generate OTP
                sendOTPViaSMTP(email, generatedOTP);
                Intent intent = new Intent(SendOTPActivity.this, VerifyOTPActvity.class);
                intent.putExtra("otp_key",generatedOTP);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);  // Generates a number between 100000 and 999999
        return String.valueOf(otp);
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Method to send OTP via SMTP
    private void sendOTPViaSMTP(String recipientEmail, String otp) {
        new Thread(() -> {
            try {
                // Set up SMTP properties
                Properties props = new Properties();
                props.put("mail.smtp.host", smtpHost);
                props.put("mail.smtp.port", smtpPort);
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");  // Enable TLS

                // Get the Session object
                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmail, fromPassword);
                    }
                });

                // Create a MimeMessage object
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(fromEmail));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
                message.setSubject("Your OTP Code");
                message.setText("Your OTP code is: " + otp);

                // Send message
                Transport.send(message);

                // Notify user on success
                runOnUiThread(() -> Toast.makeText(SendOTPActivity.this, "OTP sent to your email!", Toast.LENGTH_SHORT).show());
            } catch (MessagingException e) {
                Log.e("SendOTPActivity", "Failed to send OTP", e);
                runOnUiThread(() -> Toast.makeText(SendOTPActivity.this, "Failed to send OTP. Please try again.", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
