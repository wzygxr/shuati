package class104;

// 回文子串
// 给你一个字符串 s ，请你统计并返回这个字符串中 回文子串 的数目
// 测试链接 : https://leetcode.cn/problems/palindromic-substrings/
public class Code12_PalindromicSubstrings {

    /**
     * 计算字符串s中的回文子串数量
     * 时间复杂度: O(n)，其中n为字符串长度
     * 空间复杂度: O(n)，用于存储预处理字符串和回文半径数组
     * 
     * @param s 输入字符串
     * @return 回文子串的数量
     */
    public static int countSubstrings(String s) {
        // 使用Manacher算法统计回文子串数量
        return manacher(s);
    }

    // 定义最大字符串长度
    public static int MAXN = 1001;

    // 预处理后的字符串数组
    public static char[] ss = new char[MAXN << 1];

    // 回文半径数组
    public static int[] p = new int[MAXN << 1];

    // 预处理后字符串的长度
    public static int n;

    /**
     * Manacher算法核心实现
     * 除了计算最长回文子串外，还可以统计所有回文子串的数量
     * 
     * @param str 原始字符串
     * @return 回文子串的数量
     */
    public static int manacher(String str) {
        // 预处理字符串
        manacherss(str.toCharArray());
        
        // 初始化结果计数器
        int ans = 0;
        
        // 遍历预处理后的字符串
        for (int i = 0, c = 0, r = 0, len; i < n; i++) {
            // 利用回文对称性优化
            len = r > i ? Math.min(p[2 * c - i], r - i) : 1;
            
            // 尝试扩展回文串
            while (i + len < n && i - len >= 0 && ss[i + len] == ss[i - len]) {
                len++;
            }
            
            // 更新最右回文边界和中心
            if (i + len > r) {
                r = i + len;
                c = i;
            }
            
            // 统计回文子串数量
            // 对于预处理后的字符串，每个位置i的回文半径len对应的回文子串数量为len / 2
            // 因为每个有效的回文子串会被预处理后的特殊字符分隔
            ans += len / 2;
            
            // 记录当前位置的回文半径
            p[i] = len;
        }
        
        return ans;
    }

    /**
     * 预处理函数，在字符间插入特殊字符
     * 目的：统一处理奇数长度和偶数长度的回文串
     * 
     * @param a 原始字符数组
     */
    public static void manacherss(char[] a) {
        n = a.length * 2 + 1;
        for (int i = 0, j = 0; i < n; i++) {
            ss[i] = (i & 1) == 0 ? '#' : a[j++];
        }
    }

    /**
     * 测试用例
     * 输入: s = "abc"
     * 输出: 3
     * 解释: 三个回文子串: "a", "b", "c"
     * 
     * 输入: s = "aaa"
     * 输出: 6
     * 解释: 六个回文子串: "a", "a", "a", "aa", "aa", "aaa"
     */
    public static void main(String[] args) {
        // 测试用例1
        String s1 = "abc";
        System.out.println("输入: " + s1 + ", 输出: " + countSubstrings(s1)); // 应输出3
        
        // 测试用例2
        String s2 = "aaa";
        System.out.println("输入: " + s2 + ", 输出: " + countSubstrings(s2)); // 应输出6
        
        // 测试边界情况
        String s3 = "";
        System.out.println("输入: " + s3 + ", 输出: " + countSubstrings(s3)); // 应输出0
        
        String s4 = "a";
        System.out.println("输入: " + s4 + ", 输出: " + countSubstrings(s4)); // 应输出1
    }
}