package com.training.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import io.jsonwebtoken.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String JWT_SECRET = "AZERTYUIOPAZERTYUIOPAZERTYUIOPAZERTYUIOPAZERTYUIOPAZERTYUIOPAZERTYUIOPAZERTYUIOPAZERTYUIOPAZERTYUIOPAZERTYUIOPAZERTYUIOPAZERTYUIOPAZERTYUIOPAZERTYUIOPAZERTYUIOPAZERTYUIOPAZERTYUIOPAZERTYUIOPAZERTYUIOPAZERTYUIOPAZERTYUIOPAZERTYUIOPAZERTYUIOPAZERTYUIOP";
    private static final long EXPIRATION_TIME = 864_000_000;

    public String generateToken(final String subject, final Map<String, Object> extraClaims) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public boolean isTokenValid(final String tokenHeader) {
        try {
            Jwts
                    .parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(tokenHeader);
            return true;
        } catch (SignatureException
                 | MalformedJwtException
                 | ExpiredJwtException
                 | UnsupportedJwtException
                 | IllegalArgumentException e) {
            throw new AuthenticationCredentialsNotFoundException(String.format(
                    "Invalid JWT signature (%s): %s",
                    e.getClass().getSimpleName(),
                    e.getMessage()));
        }
    }

    public String extractUsername(final String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(final String token, final Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }
}