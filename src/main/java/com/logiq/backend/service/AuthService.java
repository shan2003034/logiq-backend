package com.logiq.backend.service;

import com.logiq.backend.dto.SignInRequest;
import com.logiq.backend.dto.SignUpRequest;
import com.logiq.backend.model.User;
import com.logiq.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public String registerUser(SignUpRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Error: Email is already registered!");
        }


        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());


        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCompanyName(request.getCompanyName());

        userRepository.save(user);


        return "User registered successfully!";
    }


    public String authenticateUser(SignInRequest request) {


        return "JWT_TOKEN_WILL_BE_GENERATED_HERE";
    }
}