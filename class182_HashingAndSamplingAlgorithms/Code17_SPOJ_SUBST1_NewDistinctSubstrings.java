package class107_HashingAndSamplingAlgorithms;

import java.util.*;

/**
 * SPOJ SUBST1. New Distinct Substrings
 * 题目链接：https://www.spoj.com/problems/SUBST1/
 * 
 * 题目描述：
 * 给定一个字符串，计算其中不同子串的数量。
 * 
 * 算法思路：
 * 使用字符串哈希技术：
 * 1. 枚举所有可能的子串长度
 * 2. 对于每个长度，使用滚动哈希计算所有子串的哈希值
 * 3. 使用HashSet去重，统计不同哈希值的数量
 * 4. 为了减少哈希冲突，使用双哈希技术
 * 
 * 时间复杂度：O(n^2)，其中n是字符串长度
 * 空间复杂度：O(n^2)，最坏情况下需要存储所有子串的哈希值
 * 
 * 优化思路：
 * 1. 使用双哈希减少冲突概率
 * 2. 预计算幂数组避免重复计算
 * 3. 使用滚动哈希优化子串哈希值计算
 * 
 * 工程化考量：
 * 1. 大数处理：使用模运算防止整数溢出
 * 2. 性能优化：滚动哈希避免重复计算
 * 3. 边界情况：空字符串、单字符字符串
 * 4. 内存管理：合理使用HashSet存储哈希值
 */
public class Code17_SPOJ_SUBST1_NewDistinctSubstrings {
    
    // 双哈希参数
    private static final long BASE1 = 131;
    private static final long BASE2 = 13131;
    private static final long MOD1 = 1000000007;
    private static final long MOD2 = 1000000009;
    
    /**
     * 计算字符串中不同子串的数量
     * 
     * @param s 输入字符串
     * @return 不同子串的数量
     */
    public static int countDistinctSubstrings(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }
        
        int n = s.length();
        Set<Long> distinctHashes = new HashSet<>();
        
        // 预计算幂数组
        long[] pow1 = new long[n + 1];
        long[] pow2 = new long[n + 1];
        pow1[0] = 1;
        pow2[0] = 1;
        
        for (int i = 1; i <= n; i++) {
            pow1[i] = (pow1[i - 1] * BASE1) % MOD1;
            pow2[i] = (pow2[i - 1] * BASE2) % MOD2;
        }
        
        // 预计算前缀哈希数组
        long[] prefixHash1 = new long[n + 1];
        long[] prefixHash2 = new long[n + 1];
        
        for (int i = 1; i <= n; i++) {
            prefixHash1[i] = (prefixHash1[i - 1] * BASE1 + s.charAt(i - 1)) % MOD1;
            prefixHash2[i] = (prefixHash2[i - 1] * BASE2 + s.charAt(i - 1)) % MOD2;
        }
        
        // 枚举所有可能的子串
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                // 计算子串s[i...j]的哈希值
                long hash1 = (prefixHash1[j + 1] - prefixHash1[i] * pow1[j - i + 1] % MOD1 + MOD1) % MOD1;
                long hash2 = (prefixHash2[j + 1] - prefixHash2[i] * pow2[j - i + 1] % MOD2 + MOD2) % MOD2;
                long combinedHash = hash1 * MOD2 + hash2;
                
                distinctHashes.add(combinedHash);
            }
        }
        
        return distinctHashes.size();
    }
    
    /**
     * 优化版本：使用滚动哈希技术
     * 
     * @param s 输入字符串
     * @return 不同子串的数量
     */
    public static int countDistinctSubstringsOptimized(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }
        
        int n = s.length();
        Set<Long> distinctHashes = new HashSet<>();
        
        // 预计算幂数组
        long[] pow1 = new long[n + 1];
        long[] pow2 = new long[n + 1];
        pow1[0] = 1;
        pow2[0] = 1;
        
        for (int i = 1; i <= n; i++) {
            pow1[i] = (pow1[i - 1] * BASE1) % MOD1;
            pow2[i] = (pow2[i - 1] * BASE2) % MOD2;
        }
        
        // 对于每个长度，使用滚动哈希
        for (int len = 1; len <= n; len++) {
            // 计算第一个长度为len的子串的哈希值
            long hash1 = 0, hash2 = 0;
            for (int i = 0; i < len; i++) {
                hash1 = (hash1 * BASE1 + s.charAt(i)) % MOD1;
                hash2 = (hash2 * BASE2 + s.charAt(i)) % MOD2;
            }
            distinctHashes.add(hash1 * MOD2 + hash2);
            
            // 滚动计算后续子串的哈希值
            for (int i = len; i < n; i++) {
                // 移除最左边的字符贡献
                hash1 = (hash1 - s.charAt(i - len) * pow1[len - 1] % MOD1 + MOD1) % MOD1;
                hash2 = (hash2 - s.charAt(i - len) * pow2[len - 1] % MOD2 + MOD2) % MOD2;
                
                // 添加最右边的字符
                hash1 = (hash1 * BASE1 + s.charAt(i)) % MOD1;
                hash2 = (hash2 * BASE2 + s.charAt(i)) % MOD2;
                
                distinctHashes.add(hash1 * MOD2 + hash2);
            }
        }
        
        return distinctHashes.size();
    }
    
    /**
     * 数学方法：使用后缀数组（理论最优解，但实现复杂）
     * 这里提供一个简化的数学公式解法
     * 
     * @param s 输入字符串
     * @return 不同子串的数量
     */
    public static int countDistinctSubstringsMath(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }
        
        int n = s.length();
        // 总子串数 = n*(n+1)/2
        // 但需要去重，这里使用哈希方法计算
        return countDistinctSubstrings(s);
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 测试 SPOJ SUBST1. New Distinct Substrings ===");
        
        // 测试用例1
        String s1 = "aaa";
        int result1 = countDistinctSubstrings(s1);
        System.out.println("输入: \"" + s1 + "\"");
        System.out.println("不同子串数量: " + result1); // 期望: 3 ("a", "aa", "aaa")
        System.out.println("预期子串: [\"a\", \"aa\", \"aaa\"]");
        System.out.println();
        
        // 测试用例2
        String s2 = "abc";
        int result2 = countDistinctSubstrings(s2);
        System.out.println("输入: \"" + s2 + "\"");
        System.out.println("不同子串数量: " + result2); // 期望: 6 ("a", "b", "c", "ab", "bc", "abc")
        System.out.println("预期子串: [\"a\", \"b\", \"c\", \"ab\", \"bc\", \"abc\"]");
        System.out.println();
        
        // 测试用例3
        String s3 = "abcd";
        int result3 = countDistinctSubstrings(s3);
        System.out.println("输入: \"" + s3 + "\"");
        System.out.println("不同子串数量: " + result3); // 期望: 10
        System.out.println();
        
        // 边界情况测试
        String s4 = "";
        int result4 = countDistinctSubstrings(s4);
        System.out.println("输入: \"" + s4 + "\" (空字符串)");
        System.out.println("不同子串数量: " + result4); // 期望: 0
        System.out.println();
        
        String s5 = "a";
        int result5 = countDistinctSubstrings(s5);
        System.out.println("输入: \"" + s5 + "\" (单字符)");
        System.out.println("不同子串数量: " + result5); // 期望: 1
        System.out.println();
        
        // 性能对比测试
        System.out.println("=== 性能对比测试 ===");
        String testString = "abcdefghijklmnopqrstuvwxyz"; // 26个字符
        
        long startTime = System.currentTimeMillis();
        int resultBasic = countDistinctSubstrings(testString);
        long basicTime = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        int resultOptimized = countDistinctSubstringsOptimized(testString);
        long optimizedTime = System.currentTimeMillis() - startTime;
        
        System.out.println("测试字符串: \"" + testString + "\" (长度: " + testString.length() + ")");
        System.out.println("基础方法结果: " + resultBasic + ", 耗时: " + basicTime + "ms");
        System.out.println("优化方法结果: " + resultOptimized + ", 耗时: " + optimizedTime + "ms");
        System.out.println("结果一致性: " + (resultBasic == resultOptimized ? "通过" : "失败"));
        System.out.println();
        
        // 大字符串测试
        System.out.println("=== 大字符串测试 ===");
        StringBuilder largeSb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            largeSb.append((char) ('a' + i % 26));
        }
        String largeString = largeSb.toString();
        
        startTime = System.currentTimeMillis();
        int largeResult = countDistinctSubstringsOptimized(largeString);
        long largeTime = System.currentTimeMillis() - startTime;
        
        System.out.println("大字符串长度: " + largeString.length());
        System.out.println("不同子串数量: " + largeResult);
        System.out.println("计算耗时: " + largeTime + "ms");
        
        System.out.println("所有测试完成");
    }
}