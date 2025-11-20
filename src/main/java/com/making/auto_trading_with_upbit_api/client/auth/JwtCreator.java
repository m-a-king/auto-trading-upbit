package com.making.auto_trading_with_upbit_api.client.auth;

import com.making.auto_trading_with_upbit_api.client.config.UpbitProperty;
import com.making.auto_trading_with_upbit_api.client.constants.ApiConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtCreator {

    public static final String QUERY_HASH = "query_hash";
    private static final String ACCESS_KEY = "access_key";
    private static final String NONCE = "nonce";

    private final String accessKey;
    private final SecretKey secretKey;

    public JwtCreator(final UpbitProperty upbitProperty) {
        this.accessKey = upbitProperty.accessKey();
        this.secretKey = Keys.hmacShaKeyFor(upbitProperty.secretKey().getBytes(StandardCharsets.UTF_8));
    }

    public Jwt create(final String queryString) {
        final Map<String, String> claims = new HashMap<>();
        claims.put(ACCESS_KEY, accessKey);
        claims.put(NONCE, UUID.randomUUID().toString());

        hashQueryString(queryString, claims);

        return new Jwt(
                Jwts.builder()
                        .claims(claims)
                        .signWith(secretKey, Jwts.SIG.HS256)
                        .compact());
    }

    public Jwt create() {
        return create(ApiConstants.EMPTY_STRING);
    }

    private void hashQueryString(
            final String queryString,
            final Map<String, String> claims
    ) {
        if (queryString.isBlank()) {
            return;
        }
        claims.put(QUERY_HASH, HashUtil.sha512(queryString));
    }
}
