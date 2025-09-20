package org.example.config;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.util.Date;

import static io.jsonwebtoken.Jwts.parserBuilder;
import static java.time.LocalTime.now;

@Component
public class JwtUtil {

    private final SecretKey key= Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String username){
        String jwt= Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (15*60*1000)))
                .signWith(key)
                .compact();
        return jwt;
    }

    public String validateToken(String token){
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }catch(JwtException | IllegalArgumentException e){
            return null;
        }
    }


}
