package com.discgolf.in_the_bag.services;

import com.discgolf.in_the_bag.jwt.JwtService;
import com.discgolf.in_the_bag.models.User;
import com.discgolf.in_the_bag.models.UserDisc;
import com.discgolf.in_the_bag.records.LoginRequest;
import com.discgolf.in_the_bag.records.LoginResponse;
import com.discgolf.in_the_bag.records.SignupRequest;
import com.discgolf.in_the_bag.records.UserProfileDto;
import com.discgolf.in_the_bag.repositories.BagRepository;
import com.discgolf.in_the_bag.repositories.UserDiscRepository;
import com.discgolf.in_the_bag.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.discgolf.in_the_bag.records.ChangePasswordRequest;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserDiscRepository userDiscRepository;
    private final BagRepository bagRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse signup(SignupRequest request) {
        logger.info("📥 Signup attempt for email: {}", request.email());

        Optional<User> existingUser = userRepository.findByEmail(request.email());
        if (existingUser.isPresent()) {
            logger.warn("⚠️ Signup failed: Email already in use -> {}", request.email());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already in use.");
        }

        User newUser = new User();
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setCreatedAt(LocalDateTime.now().toString());

        userRepository.save(newUser);
        logger.info("✅ New user registered: {}", newUser.getEmail());

        String token = jwtService.generateToken(newUser.getId(), newUser.getEmail());
        logger.debug("🔐 JWT generated for userId={}", newUser.getId());

        return new LoginResponse(token, newUser.getId());
    }

    public LoginResponse authenticateUser(LoginRequest request) {
        logger.info("🔐 Login attempt for email: {}", request.email());

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    logger.warn("❌ Login failed: User not found -> {}", request.email());
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
                });

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            logger.warn("❌ Login failed: Invalid credentials for email {}", request.email());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        String jwt = jwtService.generateToken(user.getId(), user.getEmail());
        logger.info("✅ Login successful for userId={}", user.getId());
        logger.debug("🔐 JWT generated for userId={}", user.getId());

        return new LoginResponse(jwt, user.getId());
    }

    public UserProfileDto getUserProfile(Long userId) {
        logger.info("Fetching profile info for user={}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        List<UserDisc> userDiscs = userDiscRepository.findAllByUserId(userId);
        int totalDiscs = userDiscs.size();
        int discsInUse = (int) userDiscs.stream().filter(UserDisc::getInUse).count();

        int putters = (int) userDiscs.stream().filter(d -> "PUTT & APPROACH".equalsIgnoreCase(d.getDisc().getType())).count();
        int midranges = (int) userDiscs.stream().filter(d -> "MIDRANGE".equalsIgnoreCase(d.getDisc().getType())).count();
        int fairways = (int) userDiscs.stream().filter(d -> "FAIRWAY DRIVER".equalsIgnoreCase(d.getDisc().getType())).count();
        int drivers = (int) userDiscs.stream().filter(d -> "DISTANCE DRIVER".equalsIgnoreCase(d.getDisc().getType())).count();

        int totalBags = bagRepository.findByUserId(userId).size();

        return new UserProfileDto(
                user.getEmail(),
                user.getCreatedAt(),
                totalDiscs,
                totalBags,
                discsInUse,
                putters,
                midranges,
                fairways,
                drivers
        );
    }

    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    public void deleteAccount(Long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current password is incorrect");
        }

        logger.info("Deleting user with ID {}", userId);
        userRepository.delete(user); // This triggers cascading deletions by the DB
    }

}
