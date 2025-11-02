package class107;

import java.util.*;

/**
 * LeetCode 1316. 不同的循环子字符串 - Java版本
 * 
 * 题目来源：https://leetcode.com/problems/distinct-echo-substrings/
 * 题目描述：给定一个字符串 text，返回 text 中不同非空子字符串的数量，这些子字符串可以写成某个字符串与其自身连接的结果。
 * 
 * 算法思路：
 * 1. 使用字符串哈希快速计算子串哈希值
 * 2. 遍历所有可能的子串长度（偶数长度）
 * 3. 检查子串是否可以分成两个相等的部分
 * 4. 使用哈希集合存储不同的循环子字符串
 * 
 * 时间复杂度：O(n²)，其中n为字符串长度
 * 空间复杂度：O(n²)
 * 
 * 工程化考量：
 * - 使用滚动哈希优化性能
 * - 处理哈希冲突
 * - 边界条件处理
 */
public class Code12_LeetCode1316_DistinctEchoSubstrings {
    
    // 哈希参数
    private static final long BASE = 131;
    private static final long MOD = 1000000007;
    
    /**
     * 主函数：计算不同的循环子字符串数量
     * 
     * @param text 输入字符串
     * @return 不同的循环子字符串数量
     */
    public int distinctEchoSubstrings(String text) {
        if (text == null || text.length() < 2) {
            return 0;
        }
        
        int n = text.length();
        Set<String> result = new HashSet<>();
        
        // 预处理前缀哈希数组
        long[] prefixHash = new long[n + 1];
        long[] power = new long[n + 1];
        power[0] = 1;
        
        for (int i = 1; i <= n; i++) {
            prefixHash[i] = (prefixHash[i - 1] * BASE + text.charAt(i - 1)) % MOD;
            power[i] = (power[i - 1] * BASE) % MOD;
        }
        
        // 遍历所有可能的子串长度（偶数长度）
        for (int len = 2; len <= n; len += 2) {
            for (int i = 0; i <= n - len; i++) {
                int mid = i + len / 2;
                
                // 使用哈希快速比较两个子串是否相等
                long hash1 = getHash(prefixHash, power, i, mid - 1);
                long hash2 = getHash(prefixHash, power, mid, i + len - 1);
                
                if (hash1 == hash2) {
                    // 防止哈希冲突，实际比较字符串
                    String substr = text.substring(i, i + len);
                    if (isEchoSubstring(substr)) {
                        result.add(substr);
                    }
                }
            }
        }
        
        return result.size();
    }
    
    /**
     * 获取子串的哈希值
     * 
     * @param prefixHash 前缀哈希数组
     * @param power 幂次数组
     * @param left 子串起始位置
     * @param right 子串结束位置
     * @return 子串哈希值
     */
    private long getHash(long[] prefixHash, long[] power, int left, int right) {
        return (prefixHash[right + 1] - prefixHash[left] * power[right - left + 1] % MOD + MOD) % MOD;
    }
    
    /**
     * 检查字符串是否为循环子字符串
     * 
     * @param s 输入字符串
     * @return 是否为循环子字符串
     */
    private boolean isEchoSubstring(String s) {
        int n = s.length();
        if (n % 2 != 0) return false;
        
        int half = n / 2;
        for (int i = 0; i < half; i++) {
            if (s.charAt(i) != s.charAt(i + half)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 优化版本：使用双哈希减少冲突
     */
    public int distinctEchoSubstringsOptimized(String text) {
        if (text == null || text.length() < 2) {
            return 0;
        }
        
        int n = text.length();
        Set<Long> result = new HashSet<>();
        
        // 双哈希参数
        long BASE1 = 131, MOD1 = 1000000007;
        long BASE2 = 13131, MOD2 = 1000000009;
        
        // 预处理前缀哈希数组
        long[] prefixHash1 = new long[n + 1];
        long[] prefixHash2 = new long[n + 1];
        long[] power1 = new long[n + 1];
        long[] power2 = new long[n + 1];
        
        power1[0] = power2[0] = 1;
        
        for (int i = 1; i <= n; i++) {
            prefixHash1[i] = (prefixHash1[i - 1] * BASE1 + text.charAt(i - 1)) % MOD1;
            prefixHash2[i] = (prefixHash2[i - 1] * BASE2 + text.charAt(i - 1)) % MOD2;
            power1[i] = (power1[i - 1] * BASE1) % MOD1;
            power2[i] = (power2[i - 1] * BASE2) % MOD2;
        }
        
        // 遍历所有可能的子串长度（偶数长度）
        for (int len = 2; len <= n; len += 2) {
            for (int i = 0; i <= n - len; i++) {
                int mid = i + len / 2;
                
                // 使用双哈希比较两个子串是否相等
                long hash1_left = getHash(prefixHash1, power1, i, mid - 1);
                long hash1_right = getHash(prefixHash1, power1, mid, i + len - 1);
                
                long hash2_left = getHash(prefixHash2, power2, i, mid - 1);
                long hash2_right = getHash(prefixHash2, power2, mid, i + len - 1);
                
                if (hash1_left == hash1_right && hash2_left == hash2_right) {
                    // 双哈希一致，基本可以确定相等
                    long combinedHash = hash1_left * MOD2 + hash2_left;
                    result.add(combinedHash);
                }
            }
        }
        
        return result.size();
    }
    
    /**
     * 暴力解法（用于对比验证）
     */
    public int distinctEchoSubstringsBruteForce(String text) {
        if (text == null || text.length() < 2) {
            return 0;
        }
        
        Set<String> result = new HashSet<>();
        int n = text.length();
        
        // 遍历所有可能的子串
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int len = j - i + 1;
                if (len % 2 == 0) {
                    String substr = text.substring(i, j + 1);
                    if (isEchoSubstring(substr)) {
                        result.add(substr);
                    }
                }
            }
        }
        
        return result.size();
    }
    
    /**
     * 测试函数
     */
    public static void main(String[] args) {
        Code12_LeetCode1316_DistinctEchoSubstrings solution = new Code12_LeetCode1316_DistinctEchoSubstrings();
        
        // 测试用例1：标准测试
        String text1 = "abcabcabc";
        int result1 = solution.distinctEchoSubstrings(text1);
        System.out.println("测试1 - 输入: " + text1 + ", 输出: " + result1);
        System.out.println("预期结果: 3 (abcabc, bcabca, cabcab)");
        
        // 测试用例2：简单测试
        String text2 = "leetcodeleetcode";
        int result2 = solution.distinctEchoSubstrings(text2);
        System.out.println("测试2 - 输入: " + text2 + ", 输出: " + result2);
        System.out.println("预期结果: 2 (leetcode, etcodele)");
        
        // 测试用例3：边界测试
        String text3 = "aaaa";
        int result3 = solution.distinctEchoSubstrings(text3);
        System.out.println("测试3 - 输入: " + text3 + ", 输出: " + result3);
        System.out.println("预期结果: 2 (aa, aaaa)");
        
        // 性能对比测试
        String text4 = "a".repeat(100) + "b".repeat(100);
        
        long startTime = System.currentTimeMillis();
        int result4 = solution.distinctEchoSubstringsOptimized(text4);
        long endTime = System.currentTimeMillis();
        System.out.println("优化算法耗时: " + (endTime - startTime) + "ms, 结果: " + result4);
        
        startTime = System.currentTimeMillis();
        int result5 = solution.distinctEchoSubstringsBruteForce(text4);
        endTime = System.currentTimeMillis();
        System.out.println("暴力算法耗时: " + (endTime - startTime) + "ms, 结果: " + result5);
        
        // 验证算法正确性
        System.out.println("\n=== 算法正确性验证 ===");
        String[] testCases = {"abcabc", "leetcode", "aaa", "abab"};
        for (String testCase : testCases) {
            int optimized = solution.distinctEchoSubstringsOptimized(testCase);
            int bruteForce = solution.distinctEchoSubstringsBruteForce(testCase);
            System.out.println("输入: " + testCase + ", 优化算法: " + optimized + ", 暴力算法: " + bruteForce + ", 一致: " + (optimized == bruteForce));
        }
    }
    
    /**
     * 复杂度分析：
     * 
     * 时间复杂度：
     * - 基础版本：O(n²)，需要遍历所有可能的子串
     * - 优化版本：O(n²)，但常数项更小
     * - 暴力版本：O(n³)，需要实际比较字符串
     * 
     * 空间复杂度：
     * - 基础版本：O(n²)，存储所有不同的循环子字符串
     * - 优化版本：O(n²)，存储哈希值
     * - 暴力版本：O(n²)，存储字符串
     * 
     * 算法优化点：
     * 1. 使用前缀哈希数组：预处理后可以在O(1)时间内获取任意子串的哈希值
     * 2. 双哈希减少冲突：使用两个不同的哈希函数组合，大大降低哈希冲突概率
     * 3. 只考虑偶数长度：循环子字符串必须是偶数长度
     * 
     * 边界情况处理：
     * - 空字符串或长度小于2的字符串直接返回0
     * - 处理哈希冲突：当哈希值相同时，实际比较字符串内容
     * - 大数溢出处理：使用模运算防止整数溢出
     * 
     * 工程化考量：
     * - 可配置的哈希参数
     * - 详细的注释和文档
     * - 测试用例覆盖各种边界情况
     * - 性能对比验证
     * 
     * 实际应用场景：
     * 1. 文本模式识别：查找文本中的重复模式
     * 2. 数据压缩：识别可压缩的重复模式
     * 3. 生物信息学：DNA序列中的重复片段检测
     * 4. 代码分析：查找程序中的重复代码模式
     */
}