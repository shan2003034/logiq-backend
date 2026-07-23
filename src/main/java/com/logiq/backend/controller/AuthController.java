package com.logiq.backend.controller;

import com.logiq.backend.dto.SignInRequest;
import com.logiq.backend.dto.SignUpRequest;
import com.logiq.backend.dto.VerifyOtpRequest;
import com.logiq.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest request) {
        try {
            String response = authService.registerUser(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInRequest request) {
        try {
            String token = authService.authenticateUser(request);
            return ResponseEntity.ok(token);
        } catch (RuntimeException e) {

            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {

            e.printStackTrace();
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        try {
            String token = authService.verifyOtp(request);
            return ResponseEntity.ok(token);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/test-error")
    public void testError() {
        throw new RuntimeException("CRITICAL ERROR: Microservice communication failed at DB_CLUSTER_01!");
    }
}