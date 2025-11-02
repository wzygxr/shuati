import java.util.*;

/**
 * LeetCode 1316. 不同的循环子字符串
 * 题目链接：https://leetcode.cn/problems/distinct-echo-substrings/
 * 
 * 题目描述：
 * 给你一个字符串 text，请返回 text 中不同的非空循环子字符串的数目。
 * 循环子字符串定义为：某个字符串与其本身连接一次形成的字符串（比如，abcabc 是 abc 的循环字符串）。
 * 
 * 示例：
 * 输入：text = "abcabcabc"
 * 输出：3
 * 解释：3 个不同的循环子字符串是 "abcabc"，"bcabca"，"cabcab"。
 * 
 * 解题思路：
 * 1. 使用字符串哈希和滚动哈希技术来高效判断子字符串
 * 2. 遍历所有可能的子字符串长度（从1到n/2）
 * 3. 对于每个长度，使用滑动窗口检查是否满足循环条件
 * 4. 使用哈希集合去重
 * 
 * 时间复杂度：O(n^2)，其中n是字符串长度
 * 空间复杂度：O(n^2)，最坏情况下需要存储所有子字符串的哈希值
 * 
 * 优化点：
 * - 使用双哈希减少冲突概率
 * - 提前终止不必要的检查
 * - 使用滑动窗口减少重复计算
 */
public class Code11_LeetCode1316_DistinctEchoSubstrings {
    
    // 双哈希的模数和基数
    private static final int MOD1 = 1000000007;
    private static final int MOD2 = 1000000009;
    private static final int BASE1 = 131;
    private static final int BASE2 = 13131;
    
    /**
     * 计算不同的循环子字符串数量
     */
    public int distinctEchoSubstrings(String text) {
        int n = text.length();
        if (n <= 1) return 0;
        
        // 预处理哈希数组和幂数组
        long[] hash1 = new long[n + 1];
        long[] hash2 = new long[n + 1];
        long[] pow1 = new long[n + 1];
        long[] pow2 = new long[n + 1];
        
        pow1[0] = 1;
        pow2[0] = 1;
        
        for (int i = 1; i <= n; i++) {
            int c = text.charAt(i - 1);
            hash1[i] = (hash1[i - 1] * BASE1 + c) % MOD1;
            hash2[i] = (hash2[i - 1] * BASE2 + c) % MOD2;
            pow1[i] = (pow1[i - 1] * BASE1) % MOD1;
            pow2[i] = (pow2[i - 1] * BASE2) % MOD2;
        }
        
        // 使用集合存储不同的循环子字符串的哈希值
        Set<Long> seen = new HashSet<>();
        
        // 遍历所有可能的子字符串长度（从1到n/2）
        for (int len = 1; len <= n / 2; len++) {
            // 使用滑动窗口检查长度为len*2的子字符串
            for (int i = 0; i + 2 * len <= n; i++) {
                // 检查前半部分和后半部分是否相等
                if (isEqual(hash1, hash2, pow1, pow2, i, i + len, len)) {
                    // 计算子字符串的哈希值（使用双哈希组合）
                    long hashVal = getHash(hash1, hash2, pow1, pow2, i, i + 2 * len);
                    seen.add(hashVal);
                }
            }
        }
        
        return seen.size();
    }
    
    /**
     * 检查两个子字符串是否相等
     */
    private boolean isEqual(long[] hash1, long[] hash2, long[] pow1, long[] pow2, 
                           int start1, int start2, int len) {
        // 检查第一个哈希
        long h11 = (hash1[start1 + len] - hash1[start1] * pow1[len] % MOD1 + MOD1) % MOD1;
        long h12 = (hash1[start2 + len] - hash1[start2] * pow1[len] % MOD1 + MOD1) % MOD1;
        if (h11 != h12) return false;
        
        // 检查第二个哈希（双哈希验证）
        long h21 = (hash2[start1 + len] - hash2[start1] * pow2[len] % MOD2 + MOD2) % MOD2;
        long h22 = (hash2[start2 + len] - hash2[start2] * pow2[len] % MOD2 + MOD2) % MOD2;
        return h21 == h22;
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
     * 测试方法
     */
    public static void main(String[] args) {
        Code11_LeetCode1316_DistinctEchoSubstrings solution = new Code11_LeetCode1316_DistinctEchoSubstrings();
        
        // 测试用例1
        String text1 = "abcabcabc";
        int result1 = solution.distinctEchoSubstrings(text1);
        System.out.println("测试用例1: " + text1 + " -> " + result1);
        System.out.println("预期结果: 3");
        System.out.println("测试结果: " + (result1 == 3 ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例2
        String text2 = "leetcodeleetcode";
        int result2 = solution.distinctEchoSubstrings(text2);
        System.out.println("测试用例2: " + text2 + " -> " + result2);
        System.out.println("预期结果: 2");
        System.out.println("测试结果: " + (result2 == 2 ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例3：边界情况
        String text3 = "aa";
        int result3 = solution.distinctEchoSubstrings(text3);
        System.out.println("测试用例3: " + text3 + " -> " + result3);
        System.out.println("预期结果: 1");
        System.out.println("测试结果: " + (result3 == 1 ? "通过" : "失败"));
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        long startTime = System.currentTimeMillis();
        String largeText = "a".repeat(1000);
        int largeResult = solution.distinctEchoSubstrings(largeText);
        long endTime = System.currentTimeMillis();
        System.out.println("1000个字符的性能测试，耗时: " + (endTime - startTime) + "ms");
        System.out.println("结果: " + largeResult);
    }
}