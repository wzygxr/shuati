package class186;

import java.util.*;
import java.io.*;

/**
 * SPOJ SUBST1 - New Distinct Substrings (高级版本)
 * 
 * 题目描述：给定一个字符串，计算其中不同子串的个数
 * 平台：SPOJ
 * 链接：https://www.spoj.com/problems/SUBST1/
 * 
 * 解题思路：
 * 使用后缀数组和height数组来解决这个问题。
 * 1. 对字符串建立后缀数组
 * 2. 计算height数组（相邻后缀的最长公共前缀）
 * 3. 所有子串数量为 n*(n+1)/2
 * 4. 重复的子串数量为 height数组的和
 * 5. 不同子串数量 = 总子串数 - 重复子串数
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 */
public class Code01_Subst1Advanced {
    
    /**
     * 后缀数组实现类
     */
    static class SuffixArray {
        private String s;
        private int[] sa;     // 后缀数组
        private int[] rank;   // 排名数组
        private int[] height; // LCP数组
        
        public SuffixArray(String s) {
            this.s = s;
            buildSuffixArray();
            buildLCP();
        }
        
        /**
         * 构建后缀数组（使用倍增算法）
         */
        private void buildSuffixArray() {
            int n = s.length();
            sa = new int[n];
            rank = new int[n];
            int[] tmp = new int[n];
            
            // 初始化
            for (int i = 0; i < n; i++) {
                sa[i] = i;
                rank[i] = s.charAt(i);
            }
            
            // 倍增排序
            for (int k = 1; k < n; k <<= 1) {
                // 定义比较器
                final int fk = k;
                Integer[] indices = new Integer[n];
                for (int i = 0; i < n; i++) indices[i] = i;
                
                // 按第二关键字排序
                Arrays.sort(indices, (a, b) -> {
                    int ra = a + fk < n ? rank[a + fk] : -1;
                    int rb = b + fk < n ? rank[b + fk] : -1;
                    return Integer.compare(ra, rb);
                });
                
                // 按第一关键字排序（稳定排序）
                Arrays.sort(indices, (a, b) -> {
                    int ra = rank[a];
                    int rb = rank[b];
                    if (ra != rb) return Integer.compare(ra, rb);
                    int ra2 = a + fk < n ? rank[a + fk] : -1;
                    int rb2 = b + fk < n ? rank[b + fk] : -1;
                    return Integer.compare(ra2, rb2);
                });
                
                // 更新sa和rank
                for (int i = 0; i < n; i++) {
                    sa[i] = indices[i];
                }
                
                tmp[sa[0]] = 0;
                for (int i = 1; i < n; i++) {
                    int a = sa[i - 1], b = sa[i];
                    int ra1 = rank[a], rb1 = rank[b];
                    int ra2 = a + k < n ? rank[a + k] : -1;
                    int rb2 = b + k < n ? rank[b + k] : -1;
                    tmp[b] = tmp[a] + ((ra1 != rb1 || ra2 != rb2) ? 1 : 0);
                }
                
                for (int i = 0; i < n; i++) {
                    rank[sa[i]] = tmp[sa[i]];
                }
                
                if (rank[sa[n - 1]] == n - 1) break;
            }
        }
        
        /**
         * 构建LCP数组（使用Kasai算法）
         */
        private void buildLCP() {
            int n = s.length();
            height = new int[n];
            int[] inv = new int[n];
            
            // 计算rank的逆数组
            for (int i = 0; i < n; i++) {
                inv[sa[i]] = i;
            }
            
            // Kasai算法
            for (int i = 0, k = 0; i < n; i++) {
                if (inv[i] == n - 1) {
                    k = 0;
                    continue;
                }
                
                int j = sa[inv[i] + 1];
                while (i + k < n && j + k < n && s.charAt(i + k) == s.charAt(j + k)) {
                    k++;
                }
                
                height[inv[i]] = k;
                if (k > 0) k--;
            }
        }
        
        /**
         * 计算不同子串的数量
         * @return 不同子串的数量
         */
        public long countDistinctSubstrings() {
            int n = s.length();
            // 总子串数
            long total = (long) n * (n + 1) / 2;
            // 重复子串数（height数组的和）
            long repeated = 0;
            for (int i = 1; i < n; i++) {  // 从1开始，因为height[0]是无效的
                repeated += height[i];
            }
            // 不同子串数 = 总子串数 - 重复子串数
            return total - repeated;
        }
    }
    
    /**
     * 主函数，用于测试
     */
    public static void main(String[] args) {
        // 由于SPOJ需要特定的输入输出格式，这里仅提供测试用例
        testCases();
    }
    
    /**
     * 测试用例
     */
    private static void testCases() {
        // 测试用例1
        String s1 = "aaa";
        SuffixArray sa1 = new SuffixArray(s1);
        System.out.println("输入: " + s1);
        System.out.println("不同子串数: " + sa1.countDistinctSubstrings());
        // 预期输出: 3 (a, aa, aaa)
        
        // 测试用例2
        String s2 = "abc";
        SuffixArray sa2 = new SuffixArray(s2);
        System.out.println("输入: " + s2);
        System.out.println("不同子串数: " + sa2.countDistinctSubstrings());
        // 预期输出: 6 (a, b, c, ab, bc, abc)
        
        // 测试用例3
        String s3 = "abcd";
        SuffixArray sa3 = new SuffixArray(s3);
        System.out.println("输入: " + s3);
        System.out.println("不同子串数: " + sa3.countDistinctSubstrings());
        // 预期输出: 10 (a, b, c, d, ab, bc, cd, abc, bcd, abcd)
    }
}