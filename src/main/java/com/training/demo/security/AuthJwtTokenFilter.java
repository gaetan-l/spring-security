package com.training.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthJwtTokenFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String tokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            tokenHeader = StringUtils.delete(tokenHeader, "Bearer ").trim();
            if (jwtService.isTokenValid(tokenHeader)) {
                String username = jwtService.extractUsername(tokenHeader);
                UserDetails tokenUser = userService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(
                        tokenUser,
                        null,
                        tokenUser.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(upat);
            }
        }
        // Pass to the next filter
        filterChain.doFilter(request, response);
    }
}