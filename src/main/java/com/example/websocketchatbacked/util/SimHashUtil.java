package com.example.websocketchatbacked.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class SimHashUtil {

    private static final int HASH_BITS = 64;
    private static final Pattern CHINESE_PATTERN = Pattern.compile("[\\u4e00-\\u9fa5]");
    private static final Pattern WORD_PATTERN = Pattern.compile("[\\w\\u4e00-\\u9fa5]+");
    private static final Pattern SPECIAL_CHARS_PATTERN = Pattern.compile("[^\\w\\u4e00-\\u9fa5\\s]");

    public static String getSimHash(String content) {
        if (content == null || content.trim().isEmpty()) {
            return "0";
        }

        String preprocessedText = preprocessText(content);
        List<String> words = tokenize(preprocessedText);
        Map<String, Integer> wordWeights = calculateWordWeights(words);

        int[] v = new int[HASH_BITS];

        for (Map.Entry<String, Integer> entry : wordWeights.entrySet()) {
            String word = entry.getKey();
            int weight = entry.getValue();
            BigInteger wordHash = hashWord(word);

            for (int i = 0; i < HASH_BITS; i++) {
                BigInteger bitmask = BigInteger.ONE.shiftLeft(HASH_BITS - 1 - i);
                if (wordHash.and(bitmask).compareTo(BigInteger.ZERO) != 0) {
                    v[i] += weight;
                } else {
                    v[i] -= weight;
                }
            }
        }

        BigInteger simHash = BigInteger.ZERO;
        for (int i = 0; i < HASH_BITS; i++) {
            if (v[i] > 0) {
                simHash = simHash.add(BigInteger.ONE.shiftLeft(HASH_BITS - 1 - i));
            }
        }

        return simHash.toString();
    }

    public static String preprocessText(String text) {
        if (text == null) {
            return "";
        }

        String cleaned = SPECIAL_CHARS_PATTERN.matcher(text).replaceAll(" ");
        cleaned = cleaned.replaceAll("\\s+", " ").trim();
        cleaned = cleaned.toLowerCase();

        return cleaned;
    }

    public static List<String> tokenize(String text) {
        List<String> tokens = new ArrayList<>();
        if (text == null || text.trim().isEmpty()) {
            return tokens;
        }

        String[] words = text.split("\\s+");
        for (String word : words) {
            if (word != null && !word.trim().isEmpty() && word.trim().length() > 0) {
                tokens.add(word.trim());
            }
        }

        return tokens;
    }

    private static Map<String, Integer> calculateWordWeights(List<String> words) {
        Map<String, Integer> wordWeights = new HashMap<>();
        for (String word : words) {
            wordWeights.put(word, wordWeights.getOrDefault(word, 0) + 1);
        }
        return wordWeights;
    }

    private static BigInteger hashWord(String word) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(word.getBytes("UTF-8"));
            return new BigInteger(1, digest);
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to hash word: " + word, e);
        }
    }

    public static int hammingDistance(String simHash1, String simHash2) {
        BigInteger hash1 = new BigInteger(simHash1);
        BigInteger hash2 = new BigInteger(simHash2);
        BigInteger xor = hash1.xor(hash2);
        return xor.bitCount();
    }

    public static double similarity(String simHash1, String simHash2) {
        int distance = hammingDistance(simHash1, simHash2);
        return 1.0 - (double) distance / HASH_BITS;
    }

    public static boolean isSimilar(String simHash1, String simHash2, double threshold) {
        return similarity(simHash1, simHash2) >= threshold;
    }

    public static boolean isSimilar(String simHash1, String simHash2, int maxDistance) {
        return hammingDistance(simHash1, simHash2) <= maxDistance;
    }
}
