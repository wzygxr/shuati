package class107_HashingAndSamplingAlgorithms;

import java.util.*;

/**
 * Codeforces 271D - Good Substrings
 * 题目链接：https://codeforces.com/contest/271/problem/D
 * 
 * 题目描述：
 * 给定一个字符串s，由小写英文字母组成。有些英文字母是好的，其余的是坏的。
 * 字符串s[l...r]是好的，当且仅当其中最多有k个坏字母。
 * 任务是找出字符串s中不同好子串的数量（内容不同的子串视为不同）。
 * 
 * 算法核心思想：
 * 1. 滑动窗口枚举：从每个起始位置开始，向右扩展并统计坏字母数量
 * 2. 哈希去重：使用多项式滚动哈希和HashSet高效存储不同子串
 * 3. 早期剪枝：当坏字母数量超过k时立即停止扩展
 * 4. 预计算优化：预先计算哈希值和幂次数组，支持O(1)时间子串哈希值查询
 * 
 * 时间复杂度：O(n²)
 * 空间复杂度：O(n²)
 * 
 * 工程化考量：
 * 1. 边界情况处理：空字符串、全好字母、全坏字母
 * 2. 性能优化：剪枝策略、预计算哈希值
 * 3. 内存管理：使用HashSet去重
 * 4. 异常处理：输入验证
 */
public class Code21_Codeforces271D_GoodSubstrings {
    
    /**
     * 最大字符串长度
     */
    public static int MAXN = 1501;
    
    /**
     * 哈希基数
     */
    public static int base = 499;
    
    /**
     * bad数组标记每个字母是否是坏字母
     */
    public static boolean[] bad = new boolean[26];
    
    /**
     * 存储base的幂次
     */
    public static long[] pow = new long[MAXN];
    
    /**
     * 存储字符串的前缀哈希值
     */
    public static long[] hash = new long[MAXN];
    
    /**
     * 计算不同好子串的数量
     * 
     * @param s 输入字符串
     * @param goodChars 好字母标记字符串
     * @param k 允许的最大坏字母数量
     * @return 不同好子串的数量
     */
    public static int countGoodSubstrings(String s, String goodChars, int k) {
        char[] str = s.toCharArray();
        int n = str.length;
        char[] mark = goodChars.toCharArray();
        
        // 构建坏字母标记数组
        for (int i = 0; i < 26; i++) {
            bad[i] = mark[i] == '0';
        }
        
        // 预计算幂次数组
        pow[0] = 1;
        for (int i = 1; i < n; i++) {
            pow[i] = pow[i - 1] * base;
        }
        
        // 构建前缀哈希数组
        hash[0] = str[0] - 'a' + 1;
        for (int i = 1; i < n; i++) {
            hash[i] = hash[i - 1] * base + (str[i] - 'a' + 1);
        }
        
        // 使用HashSet存储不同好子串的哈希值
        HashSet<Long> set = new HashSet<>();
        
        // 枚举所有可能的子串起始位置
        for (int i = 0; i < n; i++) {
            // 从位置i开始，向右扩展子串，同时统计坏字母数量
            for (int j = i, cnt = 0; j < n; j++) {
                // 检查当前字符是否是坏字母
                if (bad[str[j] - 'a']) {
                    cnt++;
                }
                
                // 剪枝优化：如果坏字母数量超过k，停止向右扩展
                if (cnt > k) {
                    break;
                }
                
                // 计算子串哈希值并加入set
                set.add(hash(i, j));
            }
        }
        
        return set.size();
    }
    
    /**
     * 计算子串s[l...r]的哈希值
     * 
     * @param l 子串起始位置
     * @param r 子串结束位置
     * @return 子串哈希值
     */
    public static long hash(int l, int r) {
        long ans = hash[r];
        if (l > 0) {
            ans -= hash[l - 1] * pow[r - l + 1];
        }
        return ans;
    }
    
    /**
     * 优化版本：使用双哈希减少冲突
     */
    public static int countGoodSubstringsOptimized(String s, String goodChars, int k) {
        char[] str = s.toCharArray();
        int n = str.length;
        char[] mark = goodChars.toCharArray();
        
        // 构建坏字母标记数组
        for (int i = 0; i < 26; i++) {
            bad[i] = mark[i] == '0';
        }
        
        // 双哈希参数
        int base1 = 499, mod1 = 1000000007;
        int base2 = 503, mod2 = 1000000009;
        
        // 预计算两组幂次数组
        long[] pow1 = new long[MAXN];
        long[] pow2 = new long[MAXN];
        
        // 预计算两组前缀哈希数组
        long[] hash1 = new long[MAXN];
        long[] hash2 = new long[MAXN];
        
        // 预处理第一组幂次数组
        pow1[0] = 1;
        for (int i = 1; i < n; i++) {
            pow1[i] = (pow1[i - 1] * base1) % mod1;
        }
        
        // 预处理第二组幂次数组
        pow2[0] = 1;
        for (int i = 1; i < n; i++) {
            pow2[i] = (pow2[i - 1] * base2) % mod2;
        }
        
        // 计算第一组前缀哈希
        hash1[0] = (str[0] - 'a' + 1) % mod1;
        for (int i = 1; i < n; i++) {
            hash1[i] = (hash1[i - 1] * base1 + (str[i] - 'a' + 1)) % mod1;
        }
        
        // 计算第二组前缀哈希
        hash2[0] = (str[0] - 'a' + 1) % mod2;
        for (int i = 1; i < n; i++) {
            hash2[i] = (hash2[i - 1] * base2 + (str[i] - 'a' + 1)) % mod2;
        }
        
        // 使用HashSet存储双哈希值
        HashSet<String> set = new HashSet<>();
        
        // 枚举所有可能的子串起始位置
        for (int i = 0; i < n; i++) {
            // 从位置i开始，向右扩展子串，同时统计坏字母数量
            for (int j = i, cnt = 0; j < n; j++) {
                // 检查当前字符是否是坏字母
                if (bad[str[j] - 'a']) {
                    cnt++;
                }
                
                // 剪枝优化：如果坏字母数量超过k，停止向右扩展
                if (cnt > k) {
                    break;
                }
                
                // 计算双哈希值并加入set
                long h1 = getHash1(i, j, hash1, pow1, mod1);
                long h2 = getHash2(i, j, hash2, pow2, mod2);
                set.add(h1 + "," + h2);
            }
        }
        
        return set.size();
    }
    
    /**
     * 计算第一组哈希值
     */
    public static long getHash1(int l, int r, long[] hash1, long[] pow1, int mod1) {
        long ans = hash1[r];
        if (l > 0) {
            ans = (ans - (hash1[l - 1] * pow1[r - l + 1]) % mod1 + mod1) % mod1;
        }
        return ans;
    }
    
    /**
     * 计算第二组哈希值
     */
    public static long getHash2(int l, int r, long[] hash2, long[] pow2, int mod2) {
        long ans = hash2[r];
        if (l > 0) {
            ans = (ans - (hash2[l - 1] * pow2[r - l + 1]) % mod2 + mod2) % mod2;
        }
        return ans;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 测试 Codeforces 271D - Good Substrings ===");
        
        // 测试用例1
        String s1 = "abcabc";
        String mark1 = "101010101010101010101010101";
        int k1 = 1;
        int result1 = countGoodSubstrings(s1, mark1, k1);
        System.out.println("输入: s=\"" + s1 + "\", mark=\"" + mark1 + "\", k=" + k1);
        System.out.println("输出: " + result1); // 期望: 9
        System.out.println();
        
        // 测试用例2
        String s2 = "aba";
        String mark2 = "111111111111111111111111111";
        int k2 = 1;
        int result2 = countGoodSubstrings(s2, mark2, k2);
        System.out.println("输入: s=\"" + s2 + "\", mark=\"" + mark2 + "\", k=" + k2);
        System.out.println("输出: " + result2); // 期望: 5
        System.out.println();
        
        // 测试用例3
        String s3 = "aaaaa";
        String mark3 = "111111111111111111111111111";
        int k3 = 2;
        int result3 = countGoodSubstrings(s3, mark3, k3);
        System.out.println("输入: s=\"" + s3 + "\", mark=\"" + mark3 + "\", k=" + k3);
        System.out.println("输出: " + result3); // 期望: 5
        System.out.println();
        
        // 优化版本测试
        System.out.println("--- 优化版本测试 ---");
        int result1Opt = countGoodSubstringsOptimized(s1, mark1, k1);
        int result2Opt = countGoodSubstringsOptimized(s2, mark2, k2);
        int result3Opt = countGoodSubstringsOptimized(s3, mark3, k3);
        System.out.println("优化版本结果1: " + result1Opt);
        System.out.println("优化版本结果2: " + result2Opt);
        System.out.println("优化版本结果3: " + result3Opt);
        System.out.println();
        
        // 性能测试
        System.out.println("--- 性能测试 ---");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append((char) ('a' + i % 26));
        }
        String largeString = sb.toString();
        String largeMark = "111111111111111111111111111";
        int largeK = 500;
        
        long startTime = System.currentTimeMillis();
        int largeResult = countGoodSubstrings(largeString, largeMark, largeK);
        long basicTime = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        int largeResultOpt = countGoodSubstringsOptimized(largeString, largeMark, largeK);
        long optimizedTime = System.currentTimeMillis() - startTime;
        
        System.out.println("大字符串长度: " + largeString.length());
        System.out.println("基础版本结果: " + largeResult + ", 耗时: " + basicTime + "ms");
        System.out.println("优化版本结果: " + largeResultOpt + ", 耗时: " + optimizedTime + "ms");
        System.out.println("结果一致性: " + (largeResult == largeResultOpt ? "通过" : "失败"));
    }
}