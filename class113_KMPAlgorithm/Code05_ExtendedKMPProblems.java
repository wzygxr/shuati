package class100;

import java.util.*;

/**
 * KMP算法扩展题目集合 - Java版本
 * 
 * 本类包含来自多个算法平台的KMP算法相关题目，包括：
 * - LeetCode
 * - HackerRank  
 * - Codeforces
 * - 洛谷
 * - 牛客网
 * - SPOJ
 * - USACO
 * - AtCoder
 * 
 * 每个题目都包含：
 * 1. 题目描述和来源链接
 * 2. 完整的KMP算法实现
 * 3. 详细的时间复杂度和空间复杂度分析
 * 4. 完整的测试用例
 * 5. 工程化考量（异常处理、边界条件等）
 * 
 * @author Algorithm Journey
 * @version 1.0
 * @since 2024-01-01
 */
public class Code05_ExtendedKMPProblems {

    /**
     * HackerRank: Knuth-Morris-Pratt Algorithm
     * 题目链接: https://www.hackerrank.com/challenges/kmp-fp/problem
     * 
     * 题目描述: 实现KMP算法，查找模式串在文本串中的所有出现位置
     * 
     * 算法思路:
     * 1. 使用KMP算法进行字符串匹配
     * 2. 记录所有匹配的起始位置
     * 3. 返回所有匹配位置的列表
     * 
     * 时间复杂度: O(n + m)，其中n是文本串长度，m是模式串长度
     * 空间复杂度: O(m)，用于存储next数组
     * 
     * @param text 文本串
     * @param pattern 模式串
     * @return 所有匹配位置的列表
     */
    public static List<Integer> kmpAllMatches(String text, String pattern) {
        List<Integer> result = new ArrayList<>();
        
        // 边界条件处理
        if (text == null || pattern == null || pattern.length() == 0) {
            return result;
        }
        
        int n = text.length(), m = pattern.length();
        if (m > n) {
            return result;
        }
        
        // 构建next数组
        int[] next = buildNextArray(pattern);
        
        int i = 0, j = 0;
        while (i < n) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            } else if (j == 0) {
                i++;
            } else {
                j = next[j];
            }
            
            // 找到完整匹配
            if (j == m) {
                result.add(i - j);
                j = next[j]; // 继续寻找重叠匹配
            }
        }
        
        return result;
    }
    
    /**
     * Codeforces 126B: Password
     * 题目链接: https://codeforces.com/contest/126/problem/B
     * 
     * 题目描述: 给定一个字符串s，找出一个最长的子串，该子串同时作为前缀、后缀和中间子串出现
     * 
     * 算法思路:
     * 1. 计算整个字符串的next数组
     * 2. 找到最大的k，使得s[0...k-1]既是前缀又是后缀
     * 3. 检查这个前缀是否在字符串中间出现
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     * 
     * @param s 输入字符串
     * @return 满足条件的最长子串，如果不存在返回空字符串
     */
    public static String findPassword(String s) {
        if (s == null || s.length() < 3) {
            return "";
        }
        
        int n = s.length();
        int[] next = buildNextArray(s);
        
        // 找到最长的既是前缀又是后缀的子串
        int maxLen = next[n];
        
        // 检查这个前缀是否在中间出现
        boolean foundInMiddle = false;
        for (int i = 1; i < n - 1; i++) {
            if (next[i] == maxLen) {
                foundInMiddle = true;
                break;
            }
        }
        
        if (maxLen > 0 && foundInMiddle) {
            return s.substring(0, maxLen);
        }
        
        // 如果最长的不行，尝试次长的
        int candidate = next[maxLen];
        if (candidate > 0) {
            for (int i = 1; i < n - 1; i++) {
                if (next[i] == candidate) {
                    return s.substring(0, candidate);
                }
            }
        }
        
        return "";
    }
    
    /**
     * 洛谷 P3375: 【模板】KMP
     * 题目链接: https://www.luogu.com.cn/problem/P3375
     * 
     * 题目描述: KMP算法模板题，输出模式串在文本串中的所有出现位置
     * 
     * 算法思路:
     * 1. 标准的KMP算法实现
     * 2. 输出所有匹配位置（从1开始计数）
     * 
     * 时间复杂度: O(n + m)
     * 空间复杂度: O(m)
     * 
     * @param text 文本串
     * @param pattern 模式串
     * @return 所有匹配位置（从1开始）
     */
    public static List<Integer> luoguKMP(String text, String pattern) {
        List<Integer> result = new ArrayList<>();
        
        if (text == null || pattern == null || pattern.length() == 0) {
            return result;
        }
        
        int n = text.length(), m = pattern.length();
        if (m > n) {
            return result;
        }
        
        int[] next = buildNextArray(pattern);
        int i = 0, j = 0;
        
        while (i < n) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            } else if (j == 0) {
                i++;
            } else {
                j = next[j];
            }
            
            if (j == m) {
                result.add(i - j + 1); // 从1开始计数
                j = next[j];
            }
        }
        
        return result;
    }
    
    /**
     * SPOJ: NAJPF - Pattern Find
     * 题目链接: https://www.spoj.com/problems/NAJPF/
     * 
     * 题目描述: 查找模式串在文本串中的所有出现位置
     * 
     * 算法思路: 标准KMP算法
     * 
     * 时间复杂度: O(n + m)
     * 空间复杂度: O(m)
     * 
     * @param text 文本串
     * @param pattern 模式串
     * @return 所有匹配位置（从0开始）
     */
    public static List<Integer> spojPatternFind(String text, String pattern) {
        return kmpAllMatches(text, pattern);
    }
    
    /**
     * 牛客网: 字符串匹配
     * 题目链接: 牛客网相关题目
     * 
     * 题目描述: 实现字符串匹配功能
     * 
     * 算法思路: 标准KMP算法
     * 
     * 时间复杂度: O(n + m)
     * 空间复杂度: O(m)
     * 
     * @param text 文本串
     * @param pattern 模式串
     * @return 第一个匹配位置，如果没有返回-1
     */
    public static int nowcoderStrStr(String text, String pattern) {
        if (text == null || pattern == null) {
            return -1;
        }
        
        if (pattern.length() == 0) {
            return 0;
        }
        
        int n = text.length(), m = pattern.length();
        if (m > n) {
            return -1;
        }
        
        int[] next = buildNextArray(pattern);
        int i = 0, j = 0;
        
        while (i < n && j < m) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            } else if (j == 0) {
                i++;
            } else {
                j = next[j];
            }
        }
        
        return j == m ? i - j : -1;
    }
    
    /**
     * USACO: String Transformation
     * 题目描述: 字符串变换相关题目
     * 
     * 算法思路: 使用KMP算法进行模式匹配
     * 
     * 时间复杂度: O(n + m)
     * 空间复杂度: O(m)
     * 
     * @param text 文本串
     * @param pattern 模式串
     * @return 是否匹配
     */
    public static boolean usacoStringMatch(String text, String pattern) {
        return nowcoderStrStr(text, pattern) != -1;
    }
    
    /**
     * AtCoder: String Algorithms
     * 题目描述: 字符串算法相关题目
     * 
     * 算法思路: 使用KMP算法进行高效匹配
     * 
     * 时间复杂度: O(n + m)
     * 空间复杂度: O(m)
     * 
     * @param text 文本串
     * @param pattern 模式串
     * @return 匹配次数
     */
    public static int atCoderKMP(String text, String pattern) {
        List<Integer> matches = kmpAllMatches(text, pattern);
        return matches.size();
    }
    
    /**
     * 构建next数组的通用方法
     * 
     * 算法思路:
     * 1. 初始化next数组
     * 2. 使用双指针技术构建next数组
     * 3. 处理边界情况
     * 
     * 时间复杂度: O(m)
     * 空间复杂度: O(m)
     * 
     * @param pattern 模式串
     * @return next数组
     */
    private static int[] buildNextArray(String pattern) {
        int m = pattern.length();
        if (m == 0) {
            return new int[0];
        }
        
        int[] next = new int[m + 1];
        next[0] = -1;
        if (m == 1) {
            return next;
        }
        
        next[1] = 0;
        int i = 2, cn = 0;
        
        while (i <= m) {
            if (pattern.charAt(i - 1) == pattern.charAt(cn)) {
                next[i++] = ++cn;
            } else if (cn > 0) {
                cn = next[cn];
            } else {
                next[i++] = 0;
            }
        }
        
        return next;
    }
    
    /**
     * 测试HackerRank题目
     */
    public static void testHackerRankKMP() {
        System.out.println("=== HackerRank: Knuth-Morris-Pratt Algorithm ===");
        
        String text = "ABABDABACDABABCABAB";
        String pattern = "ABABCABAB";
        List<Integer> result = kmpAllMatches(text, pattern);
        
        System.out.println("文本串: " + text);
        System.out.println("模式串: " + pattern);
        System.out.println("匹配位置: " + result);
        System.out.println("期望: [10]");
        System.out.println();
    }
    
    /**
     * 测试Codeforces题目
     */
    public static void testCodeforcesPassword() {
        System.out.println("=== Codeforces 126B: Password ===");
        
        String[] testCases = {
            "fixprefixsuffix", // 期望: "fix"
            "abcdabc",         // 期望: ""
            "abcabcabc",       // 期望: "abcabc"
            "aaa"              // 期望: "a"
        };
        
        for (String testCase : testCases) {
            String result = findPassword(testCase);
            System.out.println("输入: " + testCase);
            System.out.println("输出: " + result);
            System.out.println();
        }
    }
    
    /**
     * 测试洛谷题目
     */
    public static void testLuoguKMP() {
        System.out.println("=== 洛谷 P3375: 【模板】KMP ===");
        
        String text = "ABABABABCABAABABABAB";
        String pattern = "ABABAB";
        List<Integer> result = luoguKMP(text, pattern);
        
        System.out.println("文本串: " + text);
        System.out.println("模式串: " + pattern);
        System.out.println("匹配位置(从1开始): " + result);
        System.out.println("期望: [1, 3, 5, 13, 15]");
        System.out.println();
    }
    
    /**
     * 测试SPOJ题目
     */
    public static void testSPOJPatternFind() {
        System.out.println("=== SPOJ: NAJPF - Pattern Find ===");
        
        String text = "AAAAA";
        String pattern = "AA";
        List<Integer> result = spojPatternFind(text, pattern);
        
        System.out.println("文本串: " + text);
        System.out.println("模式串: " + pattern);
        System.out.println("匹配位置: " + result);
        System.out.println("期望: [0, 1, 2, 3]");
        System.out.println();
    }
    
    /**
     * 测试牛客网题目
     */
    public static void testNowcoderStrStr() {
        System.out.println("=== 牛客网: 字符串匹配 ===");
        
        String text = "hello world";
        String pattern = "world";
        int result = nowcoderStrStr(text, pattern);
        
        System.out.println("文本串: " + text);
        System.out.println("模式串: " + pattern);
        System.out.println("匹配位置: " + result);
        System.out.println("期望: 6");
        System.out.println();
    }
    
    /**
     * 测试USACO题目
     */
    public static void testUSACOStringMatch() {
        System.out.println("=== USACO: String Transformation ===");
        
        String text = "transformation";
        String pattern = "form";
        boolean result = usacoStringMatch(text, pattern);
        
        System.out.println("文本串: " + text);
        System.out.println("模式串: " + pattern);
        System.out.println("是否匹配: " + result);
        System.out.println("期望: true");
        System.out.println();
    }
    
    /**
     * 测试AtCoder题目
     */
    public static void testAtCoderKMP() {
        System.out.println("=== AtCoder: String Algorithms ===");
        
        String text = "abcabcabc";
        String pattern = "abc";
        int result = atCoderKMP(text, pattern);
        
        System.out.println("文本串: " + text);
        System.out.println("模式串: " + pattern);
        System.out.println("匹配次数: " + result);
        System.out.println("期望: 3");
        System.out.println();
    }
    
    /**
     * 工程化考量: 性能测试
     */
    public static void performanceTest() {
        System.out.println("=== 性能测试 ===");
        
        // 生成大规模测试数据
        StringBuilder largeText = new StringBuilder();
        for (int i = 0; i < 100000; i++) {
            largeText.append("ABCDEFG");
        }
        String pattern = "DEF";
        
        long startTime = System.nanoTime();
        int count = atCoderKMP(largeText.toString(), pattern);
        long endTime = System.nanoTime();
        
        System.out.println("文本长度: " + largeText.length());
        System.out.println("模式串长度: " + pattern.length());
        System.out.println("匹配次数: " + count);
        System.out.println("执行时间: " + (endTime - startTime) / 1000000.0 + " ms");
        System.out.println();
    }
    
    /**
     * 工程化考量: 内存使用测试
     */
    public static void memoryTest() {
        System.out.println("=== 内存使用测试 ===");
        
        Runtime runtime = Runtime.getRuntime();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        
        String text = "A".repeat(10000) + "B";
        String pattern = "A".repeat(1000);
        
        List<Integer> result = kmpAllMatches(text, pattern);
        
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        
        System.out.println("匹配结果数量: " + result.size());
        System.out.println("内存使用: " + (memoryAfter - memoryBefore) / 1024.0 + " KB");
        System.out.println();
    }
    
    /**
     * 主测试方法
     */
    public static void main(String[] args) {
        System.out.println("KMP算法扩展题目测试集\n");
        
        // 运行所有测试
        testHackerRankKMP();
        testCodeforcesPassword();
        testLuoguKMP();
        testSPOJPatternFind();
        testNowcoderStrStr();
        testUSACOStringMatch();
        testAtCoderKMP();
        
        // 工程化测试
        performanceTest();
        memoryTest();
        
        System.out.println("所有测试完成!");
    }
}