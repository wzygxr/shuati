import java.util.*;

/**
 * Codeforces 271D. Good Substrings
 * 题目链接：https://codeforces.com/problemset/problem/271/D
 * 
 * 题目描述：
 * 给你一个字符串s，一个长度为26的字符串good（表示26个字母的好坏），和一个整数k。
 * 一个子字符串被认为是"好"的，如果它包含的坏字符数量不超过k个。
 * 计算字符串s中不同的好子字符串的数量。
 * 
 * 示例：
 * 输入：s = "ababab", good = "01000000000000000000000000", k = 1
 * 输出：5
 * 解释：好子字符串有 "a", "ab", "aba", "abab", "b", "ba", "bab", "baba"
 * 
 * 解题思路：
 * 1. 使用字符串哈希技术来高效计算子字符串
 * 2. 使用前缀和数组快速计算子字符串中的坏字符数量
 * 3. 遍历所有可能的子字符串，检查是否满足条件
 * 4. 使用哈希集合去重
 * 
 * 时间复杂度：O(n^2)，其中n是字符串长度
 * 空间复杂度：O(n^2)，最坏情况下需要存储所有子字符串的哈希值
 * 
 * 优化点：
 * - 使用双哈希减少冲突概率
 * - 使用前缀和优化坏字符计数
 * - 提前终止不必要的检查
 */
public class Code12_Codeforces271D_GoodSubstrings {
    
    // 双哈希的模数和基数
    private static final int MOD1 = 1000000007;
    private static final int MOD2 = 1000000009;
    private static final int BASE1 = 131;
    private static final int BASE2 = 13131;
    
    /**
     * 计算不同的好子字符串数量
     */
    public int countGoodSubstrings(String s, String good, int k) {
        int n = s.length();
        if (n == 0) return 0;
        
        // 预处理坏字符标记数组
        boolean[] isBad = new boolean[26];
        for (int i = 0; i < 26; i++) {
            isBad[i] = good.charAt(i) == '0';
        }
        
        // 预处理前缀和数组，用于快速计算坏字符数量
        int[] badPrefix = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            char c = s.charAt(i - 1);
            badPrefix[i] = badPrefix[i - 1] + (isBad[c - 'a'] ? 1 : 0);
        }
        
        // 预处理哈希数组和幂数组
        long[] hash1 = new long[n + 1];
        long[] hash2 = new long[n + 1];
        long[] pow1 = new long[n + 1];
        long[] pow2 = new long[n + 1];
        
        pow1[0] = 1;
        pow2[0] = 1;
        
        for (int i = 1; i <= n; i++) {
            int c = s.charAt(i - 1);
            hash1[i] = (hash1[i - 1] * BASE1 + c) % MOD1;
            hash2[i] = (hash2[i - 1] * BASE2 + c) % MOD2;
            pow1[i] = (pow1[i - 1] * BASE1) % MOD1;
            pow2[i] = (pow2[i - 1] * BASE2) % MOD2;
        }
        
        // 使用集合存储不同的好子字符串的哈希值
        Set<Long> seen = new HashSet<>();
        
        // 遍历所有可能的子字符串
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                // 计算子字符串中的坏字符数量
                int badCount = badPrefix[j] - badPrefix[i];
                
                // 检查是否满足条件
                if (badCount <= k) {
                    // 计算子字符串的哈希值（使用双哈希组合）
                    long hashVal = getHash(hash1, hash2, pow1, pow2, i, j);
                    seen.add(hashVal);
                }
            }
        }
        
        return seen.size();
    }
    
    /**
     * 获取子字符串的双哈希组合值
     */
    private long getHash(long[] hash1, long[] hash2, long[] pow1, long[] pow2, 
                        int start, int end) {
        int len = end - start;
        long h1 = (hash1[end] - hash1[start] * pow1[len] % MOD1 + MOD1) % MOD1;
        long h2 = (hash2[end] - hash2[start] * pow2[len] % MOD2 + MOD2) % MOD2;
        // 组合两个哈希值
        return h1 * MOD2 + h2;
    }
    
    /**
     * 优化版本：使用滑动窗口和哈希集合，减少重复计算
     */
    public int countGoodSubstringsOptimized(String s, String good, int k) {
        int n = s.length();
        if (n == 0) return 0;
        
        // 预处理坏字符标记数组
        boolean[] isBad = new boolean[26];
        for (int i = 0; i < 26; i++) {
            isBad[i] = good.charAt(i) == '0';
        }
        
        // 预处理哈希数组和幂数组
        long[] hash1 = new long[n + 1];
        long[] hash2 = new long[n + 1];
        long[] pow1 = new long[n + 1];
        long[] pow2 = new long[n + 1];
        
        pow1[0] = 1;
        pow2[0] = 1;
        
        for (int i = 1; i <= n; i++) {
            int c = s.charAt(i - 1);
            hash1[i] = (hash1[i - 1] * BASE1 + c) % MOD1;
            hash2[i] = (hash2[i - 1] * BASE2 + c) % MOD2;
            pow1[i] = (pow1[i - 1] * BASE1) % MOD1;
            pow2[i] = (pow2[i - 1] * BASE2) % MOD2;
        }
        
        // 使用集合存储不同的好子字符串的哈希值
        Set<Long> seen = new HashSet<>();
        
        // 对于每个起始位置，使用滑动窗口
        for (int i = 0; i < n; i++) {
            int badCount = 0;
            
            // 从i开始，向右扩展窗口
            for (int j = i; j < n; j++) {
                char c = s.charAt(j);
                if (isBad[c - 'a']) {
                    badCount++;
                }
                
                // 如果坏字符数量超过k，停止扩展
                if (badCount > k) {
                    break;
                }
                
                // 计算子字符串的哈希值
                long hashVal = getHash(hash1, hash2, pow1, pow2, i, j + 1);
                seen.add(hashVal);
            }
        }
        
        return seen.size();
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Code12_Codeforces271D_GoodSubstrings solution = new Code12_Codeforces271D_GoodSubstrings();
        
        // 测试用例1
        String s1 = "ababab";
        String good1 = "01000000000000000000000000";
        int k1 = 1;
        int result1 = solution.countGoodSubstringsOptimized(s1, good1, k1);
        System.out.println("测试用例1: s = \"" + s1 + "\", k = " + k1 + " -> " + result1);
        System.out.println("预期结果: 5");
        System.out.println("测试结果: " + (result1 == 5 ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例2
        String s2 = "aaabbb";
        String good2 = "10000000000000000000000000";
        int k2 = 0;
        int result2 = solution.countGoodSubstringsOptimized(s2, good2, k2);
        System.out.println("测试用例2: s = \"" + s2 + "\", k = " + k2 + " -> " + result2);
        System.out.println("预期结果: 3");
        System.out.println("测试结果: " + (result2 == 3 ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例3：边界情况
        String s3 = "a";
        String good3 = "10000000000000000000000000";
        int k3 = 1;
        int result3 = solution.countGoodSubstringsOptimized(s3, good3, k3);
        System.out.println("测试用例3: s = \"" + s3 + "\", k = " + k3 + " -> " + result3);
        System.out.println("预期结果: 1");
        System.out.println("测试结果: " + (result3 == 1 ? "通过" : "失败"));
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        long startTime = System.currentTimeMillis();
        String largeS = "abcdefghijklmnopqrstuvwxyz".repeat(10); // 260个字符
        String largeGood = "01010101010101010101010101";
        int largeK = 10;
        int largeResult = solution.countGoodSubstringsOptimized(largeS, largeGood, largeK);
        long endTime = System.currentTimeMillis();
        System.out.println("260个字符的性能测试，耗时: " + (endTime - startTime) + "ms");
        System.out.println("结果: " + largeResult);
        
        // 对比两种方法的性能
        System.out.println("\n=== 方法对比 ===");
        startTime = System.currentTimeMillis();
        int resultBasic = solution.countGoodSubstrings(s1, good1, k1);
        long basicTime = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        int resultOptimized = solution.countGoodSubstringsOptimized(s1, good1, k1);
        long optimizedTime = System.currentTimeMillis() - startTime;
        
        System.out.println("基础方法结果: " + resultBasic + ", 耗时: " + basicTime + "ms");
        System.out.println("优化方法结果: " + resultOptimized + ", 耗时: " + optimizedTime + "ms");
    }
}