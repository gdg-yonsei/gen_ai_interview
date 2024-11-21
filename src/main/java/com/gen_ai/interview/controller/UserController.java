package com.gen_ai.interview.controller;

import com.gen_ai.interview.dto.user.DuplicateEmailRequestDTO;
import com.gen_ai.interview.dto.user.UserAuthResponseDTO;
import com.gen_ai.interview.dto.user.UserCheckVerificationCodeDTO;
import com.gen_ai.interview.dto.user.UserDTO;
import com.gen_ai.interview.dto.user.UserEmailDTO;
import com.gen_ai.interview.dto.user.UserEmailLoginRequestDTO;
import com.gen_ai.interview.dto.user.UserEmailSignUpRequestDTO;
import com.gen_ai.interview.dto.user.UserGoogleLoginRequestDTO;
import com.gen_ai.interview.model.eunm.LoginType;
import com.gen_ai.interview.service.EmailService;
import com.gen_ai.interview.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    @PostMapping("/")
    public ResponseEntity<UserAuthResponseDTO> emailSignUp(@RequestBody @Valid UserEmailSignUpRequestDTO request) {
        UserAuthResponseDTO response = userService.emailSignUpAndLogin(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/email-login")
    public ResponseEntity<UserAuthResponseDTO> emailLogin(@RequestBody @Valid UserEmailLoginRequestDTO request) {
        UserAuthResponseDTO response = userService.emailLogin(request, LoginType.EMAIL);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/google-login")
    public ResponseEntity<UserAuthResponseDTO> googleLogin(@RequestBody @Valid UserGoogleLoginRequestDTO request) {
        UserAuthResponseDTO response = userService.googleLogin(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/google-user-check")
    public ResponseEntity<String> checkGoogleUser(@RequestBody UserGoogleLoginRequestDTO userGoogleLoginRequestDTO) {
        userService.googleLogin(userGoogleLoginRequestDTO);
        return ResponseEntity.ok("user check");
    }

    @PostMapping("/duplicate-email")
    public ResponseEntity<Boolean> checkDuplicateEmail(@RequestBody @Valid DuplicateEmailRequestDTO requestDTO) {
        Boolean response = userService.checkDuplicateEmail(requestDTO);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/current-user")
    public ResponseEntity<UserDTO> getCurrentUser() {
        UserDTO currentUser = userService.getCurrentUserDTO();
        return ResponseEntity.ok(currentUser);
    }

    @PostMapping("/send-verification-code")
    public ResponseEntity<String> sendVerificationCode(@RequestBody UserEmailDTO userEmailDTO) {
        emailService.sendUserEmail(userEmailDTO.getEmail());
        return ResponseEntity.ok("Verification code sent to " + userEmailDTO.getEmail());
    }

    @PostMapping("/verify-code")
    public ResponseEntity<Boolean> validateUserEmail(
            @RequestBody UserCheckVerificationCodeDTO userCheckVerificationCodeDTO) {
        boolean verified = emailService.checkUserVerificationCode(userCheckVerificationCodeDTO);
        return ResponseEntity.ok(verified);
    }

    @PostMapping("/guest")
    public ResponseEntity<UserAuthResponseDTO> gusetSignUpAndLogin() {
        UserAuthResponseDTO response = userService.guestSignUpAndLogin();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/guest/email-login")
    public ResponseEntity<UserAuthResponseDTO> guesetEmailSignUp(
            @RequestBody @Valid UserEmailSignUpRequestDTO request) {
        UserAuthResponseDTO response = userService.upgradeGuestToRegular(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/guest/google-login")
    public ResponseEntity<UserAuthResponseDTO> guestGoogleLogin(@RequestBody @Valid UserGoogleLoginRequestDTO request) {
        UserAuthResponseDTO response = userService.guestGoogleSignUpAndLogin(request);
        return ResponseEntity.ok(response);
    }
}
