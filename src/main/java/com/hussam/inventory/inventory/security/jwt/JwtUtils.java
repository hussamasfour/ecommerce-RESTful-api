package com.hussam.inventory.inventory.security.jwt;

import com.hussam.inventory.inventory.entities.User;
import com.hussam.inventory.inventory.security.services.UserDetailsImp;
import io.jsonwebtoken.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtUtils {

    @Value("${hussam.app.jwtSecret}")
    private String jwtSecret;

    @Value("${hussam.app.jwtExpiration}")
    private int jwtExpiration;

    public String generateJwtToken(UserDetailsImp userDetailsImp){
        return Jwts.builder()
                .setSubject((userDetailsImp.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration* 1000L))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean parseToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException | IllegalArgumentException | UnsupportedJwtException | ExpiredJwtException | MalformedJwtException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }
}

