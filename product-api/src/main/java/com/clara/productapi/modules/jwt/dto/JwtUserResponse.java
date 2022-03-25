package com.clara.productapi.modules.jwt.dto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtUserResponse {
    private Integer id;
    private String email;
    private String name;

    public static JwtUserResponse getUser(Claims jwtClaims) {
        try {
            return new ObjectMapper().convertValue(jwtClaims.get("authUser"), JwtUserResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}