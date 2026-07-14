package com.logiq.backend.service;

import com.logiq.backend.dto.SignInRequest;
import com.logiq.backend.dto.SignUpRequest;
import com.logiq.backend.dto.VerifyOtpRequest;
import com.logiq.backend.model.User;
import com.logiq.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;


    public String registerUser(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Error: Email is already registered!");
        }


        String generatedOtp = String.format("%06d", new Random().nextInt(999999));

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCompanyName(request.getCompanyName());


        user.setOtp(generatedOtp);
        user.setOtpExpiryTime(LocalDateTime.now().plusMinutes(10));
        user.setVerified(false);

        userRepository.save(user);


        emailService.sendOtpEmail(user.getEmail(), generatedOtp, user.getFirstName());

        return "Registration successful! Please check your email for the OTP.";
    }


    public String verifyOtp(VerifyOtpRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isVerified()) {
            throw new RuntimeException("User is already verified");
        }

        if (user.getOtpExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired. Please request a new one.");
        }

        if (!user.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }


        user.setVerified(true);
        user.setOtp(null);
        user.setOtpExpiryTime(null);
        userRepository.save(user);


        return jwtService.generateToken(user);
    }


    public String authenticateUser(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));


        if (!user.isVerified()) {
            throw new RuntimeException("Please verify your email before logging in");
        }

        return jwtService.generateToken(user);
    }
}