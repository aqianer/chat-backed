package com.example.websocketchatbacked.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class SimHashUtilTest {

    @Test
    @DisplayName("测试空文本的 SimHash 计算")
    void testGetSimHashWithEmptyText() {
        String result = SimHashUtil.getSimHash("");
        assertEquals("0", result, "空文本应该返回 '0'");
    }

    @Test
    @DisplayName("测试 null 文本的 SimHash 计算")
    void testGetSimHashWithNullText() {
        String result = SimHashUtil.getSimHash(null);
        assertEquals("0", result, "null 文本应该返回 '0'");
    }

    @Test
    @DisplayName("测试相同文本的 SimHash 值一致性")
    void testGetSimHashConsistency() {
        String text = "这是一个测试文本，用于验证 SimHash 的一致性。";
        String hash1 = SimHashUtil.getSimHash(text);
        String hash2 = SimHashUtil.getSimHash(text);
        assertEquals(hash1, hash2, "相同文本应该生成相同的 SimHash 值");
    }

    @Test
    @DisplayName("测试不同文本的 SimHash 值差异性")
    void testGetSimHashDifferentTexts() {
        String text1 = "这是第一个测试文本";
        String text2 = "这是第二个完全不同的测试文本";
        String hash1 = SimHashUtil.getSimHash(text1);
        String hash2 = SimHashUtil.getSimHash(text2);
        assertNotNull(hash1, "第一个文本的 SimHash 不应为 null");
        assertNotNull(hash2, "第二个文本的 SimHash 不应为 null");
        assertNotEquals("0", hash1, "第一个文本的 SimHash 不应为 '0'");
        assertNotEquals("0", hash2, "第二个文本的 SimHash 不应为 '0'");
    }

    @Test
    @DisplayName("测试相似文本的 SimHash 相似度")
    void testSimilarityWithSimilarTexts() {
        String text1 = "这是一个关于人工智能的测试文本";
        String text2 = "这是一个关于人工智能的测试文本，内容很相似";
        String hash1 = SimHashUtil.getSimHash(text1);
        String hash2 = SimHashUtil.getSimHash(text2);
        double similarity = SimHashUtil.similarity(hash1, hash2);
        assertTrue(similarity > 0.8, "相似文本的相似度应该大于 0.8，实际为: " + similarity);
    }

    @Test
    @DisplayName("测试不相似文本的 SimHash 相似度")
    void testSimilarityWithDifferentTexts() {
        String text1 = "人工智能技术正在快速发展";
        String text2 = "今天天气很好，适合出去散步";
        String hash1 = SimHashUtil.getSimHash(text1);
        String hash2 = SimHashUtil.getSimHash(text2);
        assertNotNull(hash1, "第一个文本的 SimHash 不应为 null");
        assertNotNull(hash2, "第二个文本的 SimHash 不应为 null");
        assertNotEquals("0", hash1, "第一个文本的 SimHash 不应为 '0'");
        assertNotEquals("0", hash2, "第二个文本的 SimHash 不应为 '0'");
        double similarity = SimHashUtil.similarity(hash1, hash2);
        assertTrue(similarity < 0.6, "不相似文本的相似度应该小于 0.6，实际为: " + similarity);
    }

    @Test
    @DisplayName("测试汉明距离计算")
    void testHammingDistance() {
        String hash1 = SimHashUtil.getSimHash("测试文本一");
        String hash2 = SimHashUtil.getSimHash("测试文本二");
        int distance = SimHashUtil.hammingDistance(hash1, hash2);
        assertTrue(distance >= 0 && distance <= 64, "汉明距离应该在 0 到 64 之间，实际为: " + distance);
    }

    @Test
    @DisplayName("测试相同文本的汉明距离")
    void testHammingDistanceWithSameText() {
        String text = "这是一个测试文本";
        String hash1 = SimHashUtil.getSimHash(text);
        String hash2 = SimHashUtil.getSimHash(text);
        int distance = SimHashUtil.hammingDistance(hash1, hash2);
        assertEquals(0, distance, "相同文本的汉明距离应该为 0");
    }

    @Test
    @DisplayName("测试相似度阈值判断")
    void testIsSimilarWithThreshold() {
        String text1 = "这是一个测试文本";
        String text2 = "这是一个测试文本，内容相似";
        String hash1 = SimHashUtil.getSimHash(text1);
        String hash2 = SimHashUtil.getSimHash(text2);
        double similarity = SimHashUtil.similarity(hash1, hash2);
        assertTrue(similarity > 0.6, "相似文本的相似度应该大于 0.6，实际为: " + similarity);
    }

    @Test
    @DisplayName("测试汉明距离阈值判断")
    void testIsSimilarWithMaxDistance() {
        String text1 = "这是一个测试文本";
        String text2 = "这是一个测试文本，内容相似";
        String hash1 = SimHashUtil.getSimHash(text1);
        String hash2 = SimHashUtil.getSimHash(text2);
        int distance = SimHashUtil.hammingDistance(hash1, hash2);
        assertTrue(distance < 25, "相似文本的汉明距离应该小于 25，实际为: " + distance);
    }

    @Test
    @DisplayName("测试文本预处理功能")
    void testPreprocessText() {
        String text = "这是一个测试文本！包含特殊字符@#$%^&*()";
        String processed = SimHashUtil.preprocessText(text);
        assertFalse(processed.contains("!"), "预处理后不应包含特殊字符");
        assertFalse(processed.contains("@"), "预处理后不应包含特殊字符");
        assertTrue(processed.contains("这是一个测试文本"), "预处理后应保留中文内容");
    }

    @Test
    @DisplayName("测试文本分词功能")
    void testTokenize() {
        String text = "这是一个测试文本 for tokenization test";
        var tokens = SimHashUtil.tokenize(text);
        assertFalse(tokens.isEmpty(), "分词结果不应为空");
        assertTrue(tokens.contains("这是一个测试文本"), "分词结果应包含中文词");
        assertTrue(tokens.contains("for"), "分词结果应包含英文词");
        assertTrue(tokens.contains("tokenization"), "分词结果应包含英文词");
        assertTrue(tokens.contains("test"), "分词结果应包含英文词");
    }

    @Test
    @DisplayName("测试空文本的分词")
    void testTokenizeWithEmptyText() {
        var tokens = SimHashUtil.tokenize("");
        assertTrue(tokens.isEmpty(), "空文本的分词结果应该为空");
    }

    @Test
    @DisplayName("测试性能 - 大文本处理")
    void testPerformanceWithLargeText() {
        StringBuilder largeText = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            largeText.append("这是一个测试文本，用于性能测试。");
        }
        
        long startTime = System.currentTimeMillis();
        String hash = SimHashUtil.getSimHash(largeText.toString());
        long endTime = System.currentTimeMillis();
        
        assertNotNull(hash, "大文本应该能正常生成 SimHash");
        long duration = endTime - startTime;
        assertTrue(duration < 1000, "大文本处理应该在 1 秒内完成，实际耗时: " + duration + "ms");
    }

    @Test
    @DisplayName("测试性能 - 批量相似度计算")
    void testPerformanceWithBatchComparison() {
        String baseText = "这是一个基准文本，用于批量相似度测试";
        String baseHash = SimHashUtil.getSimHash(baseText);
        
        String[] testTexts = new String[100];
        for (int i = 0; i < 100; i++) {
            testTexts[i] = "这是一个测试文本" + i + "，用于批量测试";
        }
        
        long startTime = System.currentTimeMillis();
        for (String text : testTexts) {
            String hash = SimHashUtil.getSimHash(text);
            SimHashUtil.similarity(baseHash, hash);
        }
        long endTime = System.currentTimeMillis();
        
        long duration = endTime - startTime;
        assertTrue(duration < 5000, "100 次相似度计算应该在 5 秒内完成，实际耗时: " + duration + "ms");
    }

    @Test
    @DisplayName("测试中英文混合文本")
    void testMixedChineseAndEnglish() {
        String text = "This is a test 这是一个测试文本 for mixed language";
        String hash = SimHashUtil.getSimHash(text);
        assertNotNull(hash, "中英文混合文本应该能正常生成 SimHash");
        assertNotEquals("0", hash, "中英文混合文本的 SimHash 不应为 '0'");
    }

    @Test
    @DisplayName("测试特殊字符处理")
    void testSpecialCharacters() {
        String text = "测试文本!@#$%^&*()_+-={}[]|\\:\";'<>?,./";
        String hash = SimHashUtil.getSimHash(text);
        assertNotNull(hash, "包含特殊字符的文本应该能正常生成 SimHash");
    }

    @Test
    @DisplayName("测试数字文本")
    void testNumericText() {
        String text = "1234567890 9876543210 111222333";
        String hash = SimHashUtil.getSimHash(text);
        assertNotNull(hash, "数字文本应该能正常生成 SimHash");
        assertNotEquals("0", hash, "数字文本的 SimHash 不应为 '0'");
    }

    @Test
    @DisplayName("测试相似度范围")
    void testSimilarityRange() {
        String text1 = "测试文本";
        String text2 = "完全不同的文本";
        String hash1 = SimHashUtil.getSimHash(text1);
        String hash2 = SimHashUtil.getSimHash(text2);
        double similarity = SimHashUtil.similarity(hash1, hash2);
        assertTrue(similarity >= 0.0 && similarity <= 1.0, "相似度应该在 0.0 到 1.0 之间，实际为: " + similarity);
    }
}
