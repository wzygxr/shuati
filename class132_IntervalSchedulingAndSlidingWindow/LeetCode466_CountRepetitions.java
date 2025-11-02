package class129;

import java.util.Arrays;

/**
 * LeetCode 466. 统计重复个数
 * 
 * 题目描述：
 * 定义 str = [str, n] 表示重复字符串，由 n 个连续的字符串 str 组成。
 * 例如 ["abc", 3] = "abcabcabc"。
 * 如果我们可以从 s2 中删除某些字符使其变为 s1，则称字符串 s1 可以从字符串 s2 获得。
 * 现在给你两个非空字符串 s1 和 s2（每个最多 100 个字符长）和两个整数 0 <= n1 <= 10^6 和 1 <= n2 <= 10^6。
 * 现在考虑字符串 S1 和 S2，其中 S1=[s1,n1] 、S2=[s2,n2] 。
 * 请你找出一个可以满足使 [S2, M] 从 S1 获得的最大整数 M 。
 * 
 * 解题思路：
 * 这是一道需要寻找循环节的字符串匹配问题。
 * 
 * 核心思想：
 * 1. 预处理：计算从s1的每个位置开始，匹配s2中每个字符需要的最小长度
 * 2. 倍增优化：预处理从每个位置开始匹配一个s2需要的长度，然后使用倍增思想计算匹配多个s2
 * 3. 循环节：寻找循环节，利用循环节快速计算结果
 * 
 * 具体步骤：
 * 1. 预处理 next 数组：next[i][j] 表示从 s1 的位置 i 开始，至少需要多少长度才能找到字符 'a'+j
 * 2. 预处理 st 数组：st[i][p] 表示从 s1 的位置 i 开始，至少需要多少长度才能匹配 2^p 个 s2
 * 3. 倍增计算：使用 st 数组快速计算能匹配多少个 s2
 * 4. 结果计算：总匹配数除以 n2 得到最终结果
 * 
 * 时间复杂度：O(len1 * len2 + log(n1 * len1))
 * 空间复杂度：O(len1 * log(n1 * len1))
 * 
 * 相关题目：
 * - LeetCode 686. 重复叠加字符串匹配
 * - LeetCode 28. 实现 strStr()
 * - LeetCode 139. 单词拆分
 */
public class LeetCode466_CountRepetitions {
    
    /**
     * 计算最大重复数M
     * 
     * @param str1 字符串s1
     * @param n1 s1重复次数
     * @param str2 字符串s2
     * @param n2 s2重复次数
     * @return 最大整数M，使得[S2,M]可以从S1获得
     */
    public static int getMaxRepetitions(String str1, int n1, String str2, int n2) {
        char[] s1 = str1.toCharArray();
        char[] s2 = str2.toCharArray();
        int len1 = s1.length;
        int len2 = s2.length;
        
        // next[i][j] : 从i位置出发，至少需要多少长度，能找到j字符
        int[][] next = new int[len1][26];
        
        // 时间复杂度O(s1长度 + s2长度)
        if (!find(s1, len1, next, s2)) {
            return 0;
        }
        
        // st[i][p] : 从i位置出发，至少需要多少长度，可以获得2^p个s2
        long[][] st = new long[len1][30];
        
        // 时间复杂度O(s1长度 * s2长度)
        for (int i = 0, cur, len; i < len1; i++) {
            cur = i;
            len = 0;
            for (char c : s2) {
                len += next[cur][c - 'a'];
                cur = (cur + next[cur][c - 'a']) % len1;
            }
            st[i][0] = len;
        }
        
        // 时间复杂度O(s1长度)
        for (int p = 1; p <= 29; p++) {
            for (int i = 0; i < len1; i++) {
                st[i][p] = st[i][p - 1] + st[(int) ((st[i][p - 1] + i) % len1)][p - 1];
            }
        }
        
        long ans = 0;
        // 时间复杂度O(1)
        for (int p = 29, start = 0; p >= 0; p--) {
            if (st[start % len1][p] + start <= (long) len1 * n1) {
                ans += 1L << p;
                start += st[start % len1][p];
            }
        }
        
        return (int) (ans / n2);
    }
    
    /**
     * 预处理next数组
     * 
     * @param s1 字符数组s1
     * @param len1 s1长度
     * @param next next数组
     * @param s2 字符数组s2
     * @return s2中的字符是否都能在s1中找到
     */
    public static boolean find(char[] s1, int len1, int[][] next, char[] s2) {
        int[] right = new int[26];
        Arrays.fill(right, -1);
        
        // 从右到左扫描，记录每个字符最后出现的位置
        for (int i = len1 - 1; i >= 0; i--) {
            right[s1[i] - 'a'] = i + len1;
        }
        
        // 计算next数组
        for (int i = len1 - 1; i >= 0; i--) {
            right[s1[i] - 'a'] = i;
            for (int j = 0; j < 26; j++) {
                if (right[j] != -1) {
                    next[i][j] = right[j] - i + 1;
                } else {
                    next[i][j] = -1;
                }
            }
        }
        
        // 检查s2中的每个字符是否都能在s1中找到
        for (char c : s2) {
            if (next[0][c - 'a'] == -1) {
                return false;
            }
        }
        
        return true;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        String s1 = "acb";
        int n1 = 4;
        String s2 = "ab";
        int n2 = 2;
        System.out.println("测试用例1:");
        System.out.println("输入: s1 = \"" + s1 + "\", n1 = " + n1 + ", s2 = \"" + s2 + "\", n2 = " + n2);
        System.out.println("输出: " + getMaxRepetitions(s1, n1, s2, n2)); // 期望输出: 2
        
        // 测试用例2
        String s1_2 = "aaa";
        int n1_2 = 3;
        String s2_2 = "aa";
        int n2_2 = 1;
        System.out.println("\n测试用例2:");
        System.out.println("输入: s1 = \"" + s1_2 + "\", n1 = " + n1_2 + ", s2 = \"" + s2_2 + "\", n2 = " + n2_2);
        System.out.println("输出: " + getMaxRepetitions(s1_2, n1_2, s2_2, n2_2)); // 期望输出: 4
        
        // 测试用例3
        String s1_3 = "bacaba";
        int n1_3 = 3;
        String s2_3 = "abacab";
        int n2_3 = 1;
        System.out.println("\n测试用例3:");
        System.out.println("输入: s1 = \"" + s1_3 + "\", n1 = " + n1_3 + ", s2 = \"" + s2_3 + "\", n2 = " + n2_3);
        System.out.println("输出: " + getMaxRepetitions(s1_3, n1_3, s2_3, n2_3)); // 期望输出: 2
    }
}