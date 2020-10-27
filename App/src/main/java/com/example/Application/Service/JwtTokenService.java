package com.example.Application.Service;

import com.example.Contracts.Requests.UserRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenService
{

    private final String signingKey;

    @Autowired
    public JwtTokenService(@Value("${signingKey}") String signingKey) {
        this.signingKey = signingKey;
        System.out.println(signingKey);
    }

    public String getToken(String bearerToken)
    {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7);

        return null;
    }

    public Claims validateToken(String token)
    {
        if (StringUtils.hasText(token)) {
            try {
                return Jwts
                        .parser()
                        .setSigningKey(signingKey.getBytes(StandardCharsets.UTF_8))
                        .parseClaimsJws(token)
                        .getBody();
            }
            catch (Exception e)
            {
                System.out.println(e);
                return null;
            }
        }
        return null;
    }

    public String generateToken(UserRequest userRequest) throws UnsupportedEncodingException {
            Map<String, Object> claims = new HashMap<>();
            claims.put("username", userRequest.getName());
            claims.put("emailId", userRequest.getEmailId());
            claims.put("contactNumber", userRequest.getContactNumber());

            return Jwts
                        .builder()
                        .setClaims(claims)
                        .signWith(SignatureAlgorithm.HS256, signingKey.getBytes(StandardCharsets.UTF_8))
                        .compact();
    }
}
