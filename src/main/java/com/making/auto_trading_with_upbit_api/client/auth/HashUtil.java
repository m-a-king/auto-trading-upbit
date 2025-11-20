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

    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();
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
     * 바이트 배열을 16진수 문자열로 변환 String.format("%02x", b) 대신 미리 할당한 char 배열에 HEX 테이블로 직접 채워 넣어 GC·포맷팅 오버헤드 감소
     */
    private static String bytesToHex(final byte[] bytes) {
        final char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            final int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
