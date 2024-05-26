package com.training.demo.security;

import com.training.demo.dto.JwtAuthDto;
import com.training.demo.dto.LoginRequestDto;
import com.training.demo.dto.RegisterRequestDto;
import com.training.demo.model.Role;
import com.training.demo.model.User;
import com.training.demo.repository.RoleRepository;
import com.training.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public JwtAuthDto login(LoginRequestDto loginRequestDto) {
        UsernamePasswordAuthenticationToken upat =
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword());

        // User authentification
        final Authentication auth = authManager.authenticate(upat);

        final User user = (User) auth.getPrincipal();
        System.out.println("user: " + user);
        final String subject = user.getEmail();
        System.out.println("subject: " + subject);
        final Map<String, Object> extraClaims = Map.of("roles", auth.getAuthorities());
        System.out.println("extraClaims: " + extraClaims.toString());

        // JWT generation
        String jwt = jwtService.generateToken(subject, extraClaims);

        return JwtAuthDto.builder().token(jwt).build();
    }

    public void register(RegisterRequestDto registerRequestDto) {
        final List<Role> allRoles = roleRepository.findAll();
        User newUser = User.builder()
                .firstName(registerRequestDto.getFirstName())
                .lastName(registerRequestDto.getLastName())
                .email(registerRequestDto.getEmail())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .roles(allRoles)
                .build();
        userRepository.save(newUser);
    }
}