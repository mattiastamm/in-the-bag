package com.discgolf.in_the_bag.services;

import com.discgolf.in_the_bag.jwt.JwtService;
import com.discgolf.in_the_bag.models.User;
import com.discgolf.in_the_bag.records.LoginRequest;
import com.discgolf.in_the_bag.records.LoginResponse;
import com.discgolf.in_the_bag.records.SignupRequest;
import com.discgolf.in_the_bag.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(BagService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse signup(SignupRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.email());

        if (existingUser.isPresent()) {
            throw new RuntimeException("Email is already in use.");
        }

        User newUser = new User();
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setCreatedAt(LocalDateTime.now().toString());

        userRepository.save(newUser);

        String token = jwtService.generateToken(newUser.getId(), newUser.getEmail());

        return new LoginResponse(token, newUser.getId());
    }

    public LoginResponse authenticateUser(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String jwt = jwtService.generateToken(user.getId(), user.getEmail());
        return new LoginResponse(jwt, user.getId());
    }

}
