package com.clara.productapi.modules.jwt.service;

import com.clara.productapi.modules.jwt.dto.JwtUserResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;

import java.nio.charset.StandardCharsets;

import static org.springframework.util.ObjectUtils.isEmpty;


@Service
public class JwtService {


    private static final String EMPTY_SPACE = " ";
    private static final Integer TOKEN_INDEX = 1;

    @Value("${app-config.secrets.api-secret}")
    private String apiSecret;

    public void validateAuthorization(String token) throws ValidationException {
        var accessToken = extractToken(token);
        try {
            var claims = Jwts
                    .parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(apiSecret.getBytes()))
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
            var user = JwtUserResponse.getUser(claims);
            if (isEmpty(user) || isEmpty(user.getId())) {
                throw new ValidationException("The user is not valid.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ValidationException("Error while trying to proccess the Access Token.");
        }
    }

    private String extractToken(String token) throws ValidationException {
        if (isEmpty(token)) {
            throw new ValidationException("The access token was not informed.");
        }
        if (token.contains(EMPTY_SPACE)) {
            return token.split(EMPTY_SPACE)[TOKEN_INDEX];
        }
        return token;
    }
}


