package class_advanced_algorithms.hashing;

import java.util.*;

/**
 * 洛谷 P3370 【模板】字符串哈希
 * 
 * 题目描述：
 * 如题，给定 N 个字符串（第 i 个字符串长度为 M_i，字符串内包含数字、大小写字母，大小写敏感），
 * 请求出 N 个字符串中共有多少个不同的字符串。
 * 
 * 解题思路：
 * 这是字符串哈希的模板题。通过将每个字符串映射为一个整数（哈希值），我们可以快速比较两个字符串是否相等。
 * 对于每个字符串，我们计算其哈希值并存储在集合中，最后集合的大小即为不同字符串的个数。
 * 
 * 字符串哈希原理：
 * 字符串哈希通过将字符串看作一个P进制数来计算哈希值，公式为：
 * hash(s) = (s[0]*P^(n-1) + s[1]*P^(n-2) + ... + s[n-1]*P^0) mod M
 * 其中P通常选择一个质数（如31、131等），M也是一个大质数。
 * 
 * 时间复杂度：O(N*M)，其中N是字符串个数，M是字符串平均长度
 * 空间复杂度：O(N)，用于存储哈希集合
 */
public class LuoguP3370 {
    
    // 常用的大质数，用于哈希计算
    private static final long MOD = 1000000007L;  // 10^9 + 7
    private static final long BASE = 31L;          // 哈希基数
    
    /**
     * 计算字符串的哈希值
     * @param s 输入字符串
     * @return 字符串的哈希值
     */
    public static long computeHash(String s) {
        long hash = 0;
        long pow = 1;
        
        // 从右到左计算哈希值
        for (int i = s.length() - 1; i >= 0; i--) {
            hash = (hash + (s.charAt(i) - 'a' + 1) * pow) % MOD;
            pow = (pow * BASE) % MOD;
        }
        
        return hash;
    }
    
    /**
     * 计算不同字符串的个数
     * @param strings 字符串数组
     * @return 不同字符串的个数
     */
    public static int countDistinctStrings(String[] strings) {
        // 使用Set存储不同的哈希值
        Set<Long> hashSet = new HashSet<>();
        
        // 计算每个字符串的哈希值并加入集合
        for (String s : strings) {
            long hash = computeHash(s);
            hashSet.add(hash);
        }
        
        // 集合大小即为不同字符串的个数
        return hashSet.size();
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例
        String[] strings = {
            "aaaa",
            "abc",
            "abcc",
            "abc",
            "12345"
        };
        
        System.out.println("输入字符串:");
        for (String s : strings) {
            System.out.println(s);
        }
        
        int result = countDistinctStrings(strings);
        System.out.println("不同字符串的个数: " + result);
        // 预期输出: 4
    }
}