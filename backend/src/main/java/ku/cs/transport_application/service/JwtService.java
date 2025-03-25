package ku.cs.transport_application.service;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    private long expirationTime = 86400000;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        JwtBuilder builder = Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(new Date(nowMillis + expirationTime))
                .signWith(getSigningKey());
        return builder.compact();
    }

}
