package com.discgolf.in_the_bag.services;

import com.discgolf.in_the_bag.jwt.JwtService;
import com.discgolf.in_the_bag.models.Bag;
import com.discgolf.in_the_bag.models.User;
import com.discgolf.in_the_bag.models.UserDisc;
import com.discgolf.in_the_bag.records.*;
import com.discgolf.in_the_bag.repositories.*;
import com.discgolf.in_the_bag.util.MockDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDiscRepository userDiscRepository;

    @Mock
    private BagRepository bagRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;


    // METHOD: sidnup()
    @Test
    void testSignup_Success() {
        // Arrange
        SignupRequest request = new SignupRequest("newuser@example.com", "password");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L); // simulate generated ID
            return user;
        });

        when(jwtService.generateToken(eq(1L), eq(request.email()))).thenReturn("mocked-jwt-token");

        // Act
        LoginResponse response = userService.signup(request);

        // Assert
        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.token());
        assertEquals(1L, response.userId());

        verify(userRepository).findByEmail(request.email());
        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken(eq(1L), eq(request.email()));
    }
    @Test
    void testSignup_EmailAlreadyExists() {
        SignupRequest request = new SignupRequest("test@example.com", "password123");
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(new User()));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.signup(request));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }


    // METHOD: authenticateUser()
    @Test
    void testAuthenticateUser_Success() {
        LoginRequest request = new LoginRequest("test@example.com", "password123");
        User user = new User();
        user.setId(1L);
        user.setEmail(request.email());
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user.getId(), user.getEmail())).thenReturn("mocked-jwt-token");

        LoginResponse response = userService.authenticateUser(request);

        assertNotNull(response.token());
        assertEquals(1L, response.userId());
    }
    @Test
    void testAuthenticateUser_UserNotFound() {
        LoginRequest request = new LoginRequest("notfound@example.com", "password");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.authenticateUser(request));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User not found", exception.getReason());
    }
    @Test
    void testAuthenticateUser_InvalidPassword() {
        LoginRequest request = new LoginRequest("test@example.com", "wrongPassword");
        User user = new User();
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.authenticateUser(request));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Invalid credentials", exception.getReason());
    }


    // METHOD: getUserProfile()
    @Test
    void testGetUserProfile_Success() {
        Long userId = 1L;
        User user = new User();
        user.setEmail("test@example.com");
        user.setCreatedAt("2024-01-01T12:00:00");
        List<Bag> bags = new ArrayList<>();
        bags.add(MockDataFactory.createMockBagWithoutDiscs());

        UserDisc disc = MockDataFactory.createMockUserDiscDestroyer();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userDiscRepository.findAllByUserId(userId)).thenReturn(List.of(disc));
        when(bagRepository.findByUserId(userId)).thenReturn(bags);

        UserProfileDto profile = userService.getUserProfile(userId);

        assertEquals("test@example.com", profile.email());
        assertEquals(1, profile.totalDiscs());
        assertEquals(1, profile.totalBags());
        assertEquals(1, profile.discsInUse());
    }
    @Test
    void testGetUserProfile_UserNotFound_ThrowsException() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act + Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.getUserProfile(userId)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User not found", exception.getReason());

        verify(userRepository, times(1)).findById(userId);
        verify(userDiscRepository, times(0)).findAllByUserId(userId);
        verify(bagRepository, times(0)).findByUserId(userId);
    }


    // METHOD: changePassword()
    @Test
    void testChangePassword_Success() {
        Long userId = 1L;
        ChangePasswordRequest request = new ChangePasswordRequest("oldPassword", "newPassword");

        User user = new User();
        user.setId(userId);
        user.setPassword("encodedOldPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.currentPassword(), user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(request.newPassword())).thenReturn("encodedNewPassword");

        userService.changePassword(userId, request);

        verify(userRepository).save(any(User.class));
    }
    @Test
    void testChangePassword_CurrentPasswordIncorrect() {
        Long userId = 1L;
        ChangePasswordRequest request = new ChangePasswordRequest("wrongPassword", "newPassword");

        User user = new User();
        user.setPassword("encodedOldPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.changePassword(userId, request));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Current password is incorrect", exception.getReason());
    }
    @Test
    void testChangePassword_UserNotFound() {
        Long userId = 1L;
        ChangePasswordRequest request = new ChangePasswordRequest("password", "newPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.changePassword(userId, request));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User not found", exception.getReason());
    }


    // METHOD: deleteAccount()
    @Test
    void testDeleteAccount_Success() {
        Long userId = 1L;
        String password = "password";

        User user = new User();
        user.setPassword("encodedPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

        userService.deleteAccount(userId, password);

        verify(userRepository).delete(user);
    }
    @Test
    void testDeleteAccount_WrongPassword() {
        Long userId = 1L;
        String password = "wrongPassword";

        User user = new User();
        user.setPassword("encodedPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.deleteAccount(userId, password));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Current password is incorrect", exception.getReason());
    }
    @Test
    void testDeleteAccount_UserNotFound() {
        Long userId = 1L;
        String password = "password";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.deleteAccount(userId, password));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User not found", exception.getReason());
    }

}
