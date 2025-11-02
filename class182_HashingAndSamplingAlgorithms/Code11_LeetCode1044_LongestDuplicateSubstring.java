package class107;

import java.util.*;

/**
 * LeetCode 1044. 最长重复子串 - Java版本
 * 
 * 题目来源：https://leetcode.com/problems/longest-duplicate-substring/
 * 题目描述：给定一个字符串 s，找出其中最长的重复子串。如果有多个最长重复子串，返回任意一个。
 * 
 * 算法思路：
 * 1. 使用二分查找确定可能的最大重复子串长度
 * 2. 使用滚动哈希（Rabin-Karp算法）快速计算子串哈希值
 * 3. 使用双哈希减少哈希冲突的概率
 * 4. 通过哈希表存储已出现的子串哈希值
 * 
 * 时间复杂度：O(n log n)，其中n为字符串长度
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * - 使用双哈希减少冲突概率
 * - 选择合适的质数和模数
 * - 处理大数溢出问题
 * - 边界条件处理
 */
public class Code11_LeetCode1044_LongestDuplicateSubstring {
    
    // 双哈希的质数和模数
    private static final long BASE1 = 131;
    private static final long BASE2 = 13131;
    private static final long MOD1 = 1000000007;
    private static final long MOD2 = 1000000009;
    
    /**
     * 主函数：查找最长重复子串
     * 
     * @param s 输入字符串
     * @return 最长重复子串，如果没有重复子串则返回空字符串
     */
    public String longestDupSubstring(String s) {
        if (s == null || s.length() < 2) {
            return "";
        }
        
        int n = s.length();
        int left = 1, right = n - 1;
        String result = "";
        
        // 二分查找最长重复子串长度
        while (left <= right) {
            int mid = left + (right - left) / 2;
            String dup = findDuplicate(s, mid);
            
            if (dup != null) {
                result = dup;
                left = mid + 1; // 尝试更长的子串
            } else {
                right = mid - 1; // 缩短子串长度
            }
        }
        
        return result;
    }
    
    /**
     * 查找是否存在长度为len的重复子串
     * 
     * @param s 输入字符串
     * @param len 子串长度
     * @return 如果存在重复子串则返回该子串，否则返回null
     */
    private String findDuplicate(String s, int len) {
        int n = s.length();
        
        // 预处理BASE的幂次，用于快速计算滚动哈希
        long pow1 = 1, pow2 = 1;
        for (int i = 0; i < len - 1; i++) {
            pow1 = (pow1 * BASE1) % MOD1;
            pow2 = (pow2 * BASE2) % MOD2;
        }
        
        // 计算第一个窗口的哈希值
        long hash1 = 0, hash2 = 0;
        for (int i = 0; i < len; i++) {
            hash1 = (hash1 * BASE1 + s.charAt(i)) % MOD1;
            hash2 = (hash2 * BASE2 + s.charAt(i)) % MOD2;
        }
        
        // 使用哈希表存储已出现的子串哈希值及其起始位置
        Map<Long, List<Integer>> seen = new HashMap<>();
        
        // 双哈希组合成一个唯一的键
        long key = hash1 * MOD2 + hash2;
        List<Integer> list = new ArrayList<>();
        list.add(0);
        seen.put(key, list);
        
        // 滑动窗口遍历字符串
        for (int i = 1; i <= n - len; i++) {
            // 移除窗口最左边的字符
            hash1 = (hash1 - s.charAt(i - 1) * pow1 % MOD1 + MOD1) % MOD1;
            hash2 = (hash2 - s.charAt(i - 1) * pow2 % MOD2 + MOD2) % MOD2;
            
            // 添加窗口最右边的字符
            hash1 = (hash1 * BASE1 + s.charAt(i + len - 1)) % MOD1;
            hash2 = (hash2 * BASE2 + s.charAt(i + len - 1)) % MOD2;
            
            key = hash1 * MOD2 + hash2;
            
            if (seen.containsKey(key)) {
                // 检查是否真的存在重复（防止哈希冲突）
                String current = s.substring(i, i + len);
                for (int start : seen.get(key)) {
                    if (s.substring(start, start + len).equals(current)) {
                        return current;
                    }
                }
                seen.get(key).add(i);
            } else {
                List<Integer> newList = new ArrayList<>();
                newList.add(i);
                seen.put(key, newList);
            }
        }
        
        return null;
    }
    
    /**
     * 暴力解法（用于对比验证）
     * 时间复杂度：O(n³)，空间复杂度：O(n²)
     */
    public String longestDupSubstringBruteForce(String s) {
        if (s == null || s.length() < 2) {
            return "";
        }
        
        String result = "";
        int n = s.length();
        
        // 遍历所有可能的子串长度
        for (int len = n - 1; len > 0; len--) {
            // 遍历所有起始位置
            for (int i = 0; i <= n - len; i++) {
                String substr = s.substring(i, i + len);
                
                // 检查该子串是否在其他位置出现
                for (int j = i + 1; j <= n - len; j++) {
                    if (s.substring(j, j + len).equals(substr)) {
                        if (substr.length() > result.length()) {
                            result = substr;
                        }
                        break;
                    }
                }
                
                // 如果已经找到当前长度的重复子串，可以提前结束
                if (!result.isEmpty() && result.length() == len) {
                    break;
                }
            }
            
            // 如果找到重复子串，直接返回（因为从长到短遍历）
            if (!result.isEmpty()) {
                return result;
            }
        }
        
        return result;
    }
    
    /**
     * 测试函数
     */
    public static void main(String[] args) {
        Code11_LeetCode1044_LongestDuplicateSubstring solution = new Code11_LeetCode1044_LongestDuplicateSubstring();
        
        // 测试用例1：标准测试
        String s1 = "banana";
        String result1 = solution.longestDupSubstring(s1);
        System.out.println("测试1 - 输入: " + s1 + ", 输出: " + result1);
        System.out.println("预期结果: ana 或 na");
        
        // 测试用例2：无重复子串
        String s2 = "abcd";
        String result2 = solution.longestDupSubstring(s2);
        System.out.println("测试2 - 输入: " + s2 + ", 输出: " + result2);
        System.out.println("预期结果: 空字符串");
        
        // 测试用例3：长字符串测试
        String s3 = "aaaaaa";
        String result3 = solution.longestDupSubstring(s3);
        System.out.println("测试3 - 输入: " + s3 + ", 输出: " + result3);
        System.out.println("预期结果: aaaaa");
        
        // 性能对比测试
        String s4 = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";
        
        long startTime = System.currentTimeMillis();
        String result4 = solution.longestDupSubstring(s4);
        long endTime = System.currentTimeMillis();
        System.out.println("优化算法耗时: " + (endTime - startTime) + "ms");
        System.out.println("结果: " + result4);
        
        // 暴力解法测试（小规模数据）
        if (s4.length() <= 100) {
            startTime = System.currentTimeMillis();
            String result5 = solution.longestDupSubstringBruteForce(s4);
            endTime = System.currentTimeMillis();
            System.out.println("暴力算法耗时: " + (endTime - startTime) + "ms");
            System.out.println("暴力算法结果: " + result5);
        }
    }
    
    /**
     * 复杂度分析：
     * 
     * 时间复杂度：
     * - 二分查找：O(log n)
     * - 每次二分查找中的滚动哈希：O(n)
     * - 总时间复杂度：O(n log n)
     * 
     * 空间复杂度：
     * - 哈希表存储：O(n)
     * - 预处理幂次：O(1)
     * - 总空间复杂度：O(n)
     * 
     * 算法优化点：
     * 1. 双哈希减少冲突：使用两个不同的哈希函数组合，大大降低哈希冲突概率
     * 2. 滚动哈希优化：通过数学运算实现O(1)时间复杂度的窗口滑动
     * 3. 二分查找优化：将问题从O(n²)优化到O(n log n)
     * 
     * 边界情况处理：
     * - 空字符串或长度小于2的字符串直接返回空
     * - 处理哈希冲突：当哈希值相同时，实际比较字符串内容
     * - 大数溢出处理：使用模运算防止整数溢出
     * 
     * 工程化考量：
     * - 可配置的哈希参数（BASE和MOD）
     * - 详细的注释和文档
     * - 测试用例覆盖各种边界情况
     * - 性能对比验证
     */
}