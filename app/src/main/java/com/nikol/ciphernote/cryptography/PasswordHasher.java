package com.nikol.ciphernote.cryptography;

import android.util.Base64;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

public final class PasswordHasher {
    private static final SecureRandom RNG = new SecureRandom();

    private static final int MEMORY_KIB = 32768; // 32 MiB
    private static final int ITERATIONS = 3;
    private static final int PARALLELISM = 1;

    private static final int SALT_LEN = 16;
    private static final int HASH_LEN = 32;

    private PasswordHasher() {}

    public static String hash(String password) {
        byte[] salt = new byte[SALT_LEN];
        RNG.nextBytes(salt);

        byte[] hash = argon2id(password, salt, MEMORY_KIB, ITERATIONS, PARALLELISM, HASH_LEN);

        String saltB64 = Base64.encodeToString(salt, Base64.NO_WRAP);
        String hashB64 = Base64.encodeToString(hash, Base64.NO_WRAP);

        return "$argon2id$v=19$m=" + MEMORY_KIB + ",t=" + ITERATIONS + ",p=" + PARALLELISM
                + "$" + saltB64
                + "$" + hashB64;
    }

    public static boolean verify(String password, String encoded) {
        if (encoded == null) return false;
        if (!encoded.startsWith("$argon2id$")) return false;

        String[] parts = encoded.split("\\$");
        if (parts.length != 6) return false;

        String versionPart = parts[2]; // "v=19"
        if (!"v=19".equals(versionPart)) return false;

        String paramsPart = parts[3];
        int m = -1, t = -1, p = -1;
        for (String kv : paramsPart.split(",")) {
            String[] pair = kv.split("=");
            if (pair.length != 2) return false;
            switch (pair[0]) {
                case "m": m = Integer.parseInt(pair[1]); break;
                case "t": t = Integer.parseInt(pair[1]); break;
                case "p": p = Integer.parseInt(pair[1]); break;
                default: return false;
            }
        }
        if (m <= 0 || t <= 0 || p <= 0) return false;

        byte[] salt = Base64.decode(parts[4], Base64.NO_WRAP);
        byte[] expected = Base64.decode(parts[5], Base64.NO_WRAP);

        byte[] actual = argon2id(password, salt, m, t, p, expected.length);

        return MessageDigest.isEqual(expected, actual);
    }

    public static byte[] deriveMasterKey(String password, String encodedHash) {
        if (encodedHash == null || !encodedHash.startsWith("$argon2id$")) return null;

        String[] parts = encodedHash.split("\\$");
        if (parts.length != 6) return null;

        String paramsPart = parts[3];
        int m = -1, t = -1, p = -1;
        for (String kv : paramsPart.split(",")) {
            String[] pair = kv.split("=");
            if (pair.length != 2) return null;
            switch (pair[0]) {
                case "m": m = Integer.parseInt(pair[1]); break;
                case "t": t = Integer.parseInt(pair[1]); break;
                case "p": p = Integer.parseInt(pair[1]); break;
            }
        }

        byte[] salt = Base64.decode(parts[4], Base64.NO_WRAP);
        
        byte[] masterKeySalt = new byte[salt.length];
        for (int i = 0; i < salt.length; i++) {
            masterKeySalt[i] = (byte) (salt[i] ^ 0xFF);
        }

        return argon2id(password, masterKeySalt, m, t, p, 32); // 256-bit master key
    }

    private static byte[] argon2id(
            String password,
            byte[] salt,
            int memoryKiB,
            int iterations,
            int parallelism,
            int outLen
    ) {
        Argon2Parameters params = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                .withMemoryAsKB(memoryKiB)
                .withIterations(iterations)
                .withParallelism(parallelism)
                .withSalt(salt)
                .build();

        Argon2BytesGenerator gen = new Argon2BytesGenerator();
        gen.init(params);

        byte[] out = new byte[outLen];
        byte[] pwdBytes = password.getBytes(StandardCharsets.UTF_8);
        gen.generateBytes(pwdBytes, out);

        java.util.Arrays.fill(pwdBytes, (byte) 0);
        return out;
    }
}
