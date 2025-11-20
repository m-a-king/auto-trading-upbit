package com.making.auto_trading_with_upbit_api.client.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 해시 생성 유틸리티
 */
@NoArgsConstructor(access = AccessLevel.NONE)
public final class HashUtil {

    private static final String SHA_512 = "SHA-512";

    /**
     * SHA-512 해시 생성
     */
    public static String sha512(final String input) {
        try {
            final MessageDigest digest = MessageDigest.getInstance(SHA_512);
            final byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-512 알고리즘을 사용할 수 없습니다", e);
        }
    }

    /**
     * 바이트 배열을 16진수 문자열로 변환
     */
    private static String bytesToHex(final byte[] bytes) {
        final StringBuilder result = new StringBuilder();
        for (final byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
