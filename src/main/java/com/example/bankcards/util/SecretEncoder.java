package com.example.bankcards.util;

import static java.nio.CharBuffer.wrap;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.fill;
import static java.util.Base64.getDecoder;

import java.nio.ByteBuffer;

public class SecretEncoder {

    public static byte[] encodeBase64(final char[] secret) {
        final ByteBuffer buffer =  UTF_8.encode(wrap(secret));
        final byte[] base64Bytes = new byte[buffer.remaining()];
        buffer.get(base64Bytes);
        try {
            return getDecoder().decode(base64Bytes);
        } finally {
            fill(base64Bytes, (byte) 0);
            fill(buffer.array(), (byte) 0);
        }
    }
}
