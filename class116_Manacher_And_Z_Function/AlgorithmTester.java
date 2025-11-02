package class103;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 算法测试器 - 用于验证Manacher算法和Z函数实现的正确性
 * 提供便捷的测试入口和比较功能
 */
public class AlgorithmTester {
    private static final Logger logger = Logger.getLogger(AlgorithmTester.class.getName());
    
    public static void main(String[] args) {
        logger.info("开始测试Manacher算法和Z函数...");
        
        // 测试Manacher算法
        testManacher();
        
        // 测试Z函数
        testZFunction();
        
        logger.info("所有测试完成！");
    }
    
    /**
     * 测试Manacher算法的功能
     */
    private static void testManacher() {
        logger.info("===== 测试Manacher算法 =====");
        
        try {
            // 启用调试模式
            Code01_Manacher.setDebugMode(true);
            
            // 测试用例
            String[] testCases = {
                "babad",
                "cbbd",
                "a",
                "ac",
                "racecar",
                "",
                "aaa"
            };
            
            // 预期的最长回文子串结果
            String[] expectedPalindromes = {
                "bab",  // 或 "aba"
                "bb",
                "a",
                "a",   // 或 "c"
                "racecar",
                "",
                "aaa"
            };
            
            // 预期的回文子串数量
            int[] expectedCounts = {
                7,  // b, a, b, a, d, bab, aba
                5,  // c, b, b, d, bb
                1,  // a
                2,  // a, c
                10, // r, a, c, e, c, a, r, aceca, cec, racecar
                0,  // 空字符串
                6   // a, a, a, aa, aa, aaa
            };
            
            Code01_Manacher manacher = new Code01_Manacher();
            
            for (int i = 0; i < testCases.length; i++) {
                String s = testCases[i];
                logger.info("测试用例[" + (i+1) + "]: \"" + s + "\"");
                
                // 测试最长回文子串
                String result = manacher.longestPalindrome(s);
                String expected = expectedPalindromes[i];
                boolean passed = result.equals(expected) || 
                               (s.equals("babad") && result.equals("aba")) || 
                               (s.equals("ac") && result.equals("c"));
                
                logger.info("最长回文子串: \"" + result + "\" (" + (passed ? "通过" : "失败") + ")");
                
                // 测试回文子串计数
                int countResult = manacher.countSubstrings(s);
                boolean countPassed = countResult == expectedCounts[i];
                logger.info("回文子串数量: " + countResult + " (" + (countPassed ? "通过" : "失败") + ")");
                
                // 测试最短回文串（对于非空字符串）
                if (!s.isEmpty()) {
                    String shortestPalindrome = manacher.shortestPalindrome(s);
                    boolean isPalindrome = isPalindrome(shortestPalindrome);
                    boolean startsWith = shortestPalindrome.endsWith(s);
                    logger.info("最短回文串: \"" + shortestPalindrome + "\" (是回文: " + isPalindrome + ", 以原串结尾: " + startsWith + ")");
                }
                
                logger.info("------------------------");
            }
            
            // 运行内置单元测试
            logger.info("运行Manacher内置单元测试...");
            manacher.runUnitTests();
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Manacher算法测试出错: " + e.getMessage(), e);
        } finally {
            // 关闭调试模式
            Code01_Manacher.setDebugMode(false);
        }
    }
    
    /**
     * 测试Z函数的功能
     */
    private static void testZFunction() {
        logger.info("===== 测试Z函数（扩展KMP）=====");
        
        try {
            // 启用调试模式
            Code02_ExpandKMP.setDebugMode(true);
            
            // 测试Z数组计算
            testZArrayCalculation();
            
            // 测试LeetCode 2223
            testLeetCode2223();
            
            // 测试LeetCode 3031
            testLeetCode3031();
            
            // 运行内置单元测试
            logger.info("运行Z函数内置单元测试...");
            Code02_ExpandKMP.runUnitTests();
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Z函数测试出错: " + e.getMessage(), e);
        } finally {
            // 关闭调试模式
            Code02_ExpandKMP.setDebugMode(false);
        }
    }
    
    /**
     * 测试Z数组计算
     */
    private static void testZArrayCalculation() {
        logger.info("测试Z数组计算:");
        
        String[] testCases = {
            "aaaaa",
            "ababc",
            "babab",
            "abcdef",
            "aabaa"
        };
        
        int[][] expectedResults = {
            {5, 4, 3, 2, 1},
            {5, 0, 2, 0, 1},
            {5, 0, 3, 0, 1},
            {6, 0, 0, 0, 0, 0},
            {5, 1, 0, 1, 0}
        };
        
        // 确保数组足够大
        if (Code02_ExpandKMP.MAXN < 100) {
            Code02_ExpandKMP.MAXN = 100;
            Code02_ExpandKMP.z = Arrays.copyOf(Code02_ExpandKMP.z, Code02_ExpandKMP.MAXN);
        }
        
        for (int i = 0; i < testCases.length; i++) {
            String s = testCases[i];
            char[] chars = s.toCharArray();
            int n = s.length();
            
            logger.info("字符串: \"" + s + "\"");
            
            // 计算Z数组
            Code02_ExpandKMP.zArray(chars, n);
            
            // 获取计算结果
            int[] result = Arrays.copyOf(Code02_ExpandKMP.z, n);
            int[] expected = expectedResults[i];
            
            boolean passed = Arrays.equals(result, expected);
            logger.info("Z数组: " + Arrays.toString(result));
            logger.info("结果: " + (passed ? "通过" : "失败"));
            
            if (!passed) {
                logger.info("期望: " + Arrays.toString(expected));
            }
            
            logger.info("------------------------");
        }
    }
    
    /**
     * 测试LeetCode 2223 - 构造字符串的总得分和
     */
    private static void testLeetCode2223() {
        logger.info("测试LeetCode 2223 - 构造字符串的总得分和:");
        
        String[] testCases = {
            "babab",
            "azbazbzaz",
            "a",
            "aaa"
        };
        
        long[] expectedResults = {
            9,
            14,
            1,
            6
        };
        
        for (int i = 0; i < testCases.length; i++) {
            String s = testCases[i];
            long result = Code02_ExpandKMP.sumScores(s);
            long expected = expectedResults[i];
            boolean passed = result == expected;
            
            logger.info("字符串: \"" + s + "\" -> 得分: " + result + " (" + (passed ? "通过" : "失败") + ")");
            
            if (!passed) {
                logger.info("期望得分: " + expected);
            }
        }
        
        logger.info("------------------------");
    }
    
    /**
     * 测试LeetCode 3031 - 将单词恢复初始状态所需的最短时间 II
     */
    private static void testLeetCode3031() {
        logger.info("测试LeetCode 3031 - 将单词恢复初始状态所需的最短时间 II:");
        
        String[] words = {
            "abacaba",
            "abacaba",
            "abcdef",
            "a",
            "aaa"
        };
        
        int[] ks = {
            3,
            4,
            2,
            1,
            2
        };
        
        int[] expectedResults = {
            2,
            1,
            3,
            1,
            1
        };
        
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            int k = ks[i];
            int result = Code02_ExpandKMP.minimumTimeToInitialState(word, k);
            int expected = expectedResults[i];
            boolean passed = result == expected;
            
            logger.info("word: \"" + word + "\", k: " + k + " -> 时间: " + result + " (" + (passed ? "通过" : "失败") + ")");
            
            if (!passed) {
                logger.info("期望时间: " + expected);
            }
        }
        
        logger.info("------------------------");
    }
    
    /**
     * 辅助方法：检查字符串是否为回文
     */
    private static boolean isPalindrome(String s) {
        if (s == null || s.isEmpty()) {
            return true;
        }
        
        int left = 0;
        int right = s.length() - 1;
        
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        
        return true;
    }
    
    /**
     * 性能对比测试
     */
    public static void performanceComparison() {
        logger.info("===== 性能对比测试 =====");
        
        try {
            // 启用性能监控
            Code01_Manacher.setPerformanceMonitoring(true);
            Code02_ExpandKMP.setPerformanceMonitoring(true);
            
            // 生成测试数据
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 100000; i++) {
                sb.append((char) ('a' + (i % 26)));
            }
            String longString = sb.toString();
            
            logger.info("测试字符串长度: " + longString.length());
            
            // 测试Manacher性能
            long startTime = System.currentTimeMillis();
            Code01_Manacher manacher = new Code01_Manacher();
            manacher.longestPalindrome(longString);
            long manacherTime = System.currentTimeMillis() - startTime;
            logger.info("Manacher算法耗时: " + manacherTime + " ms");
            
            // 测试Z函数性能
            startTime = System.currentTimeMillis();
            Code02_ExpandKMP.sumScores(longString);
            long zFunctionTime = System.currentTimeMillis() - startTime;
            logger.info("Z函数耗时: " + zFunctionTime + " ms");
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "性能测试出错: " + e.getMessage(), e);
        } finally {
            // 关闭性能监控
            Code01_Manacher.setPerformanceMonitoring(false);
            Code02_ExpandKMP.setPerformanceMonitoring(false);
        }
    }
}