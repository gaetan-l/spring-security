package com.training.demo.controller;

import com.training.demo.dto.JwtAuthDto;
import com.training.demo.dto.LoginRequestDto;
import com.training.demo.dto.RegisterRequestDto;
import com.training.demo.exception.UserAlreadyExistsException;
import com.training.demo.repository.UserRepository;
import com.training.demo.security.AuthService;
import com.training.demo.security.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthRestController {
    private final AuthService authService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(final @RequestBody RegisterRequestDto registerRequestDto) throws UserAlreadyExistsException {
        if (userRepository.findByEmail(registerRequestDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(registerRequestDto.getEmail());
        }
        authService.register(registerRequestDto);

        return ResponseEntity.ok("");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthDto> login(final @RequestBody LoginRequestDto loginRequestDto) {
        log.debug("Ceci est un message de log");
        JwtAuthDto token = authService.login(loginRequestDto);

        return ResponseEntity.ok(token);
    }
}
