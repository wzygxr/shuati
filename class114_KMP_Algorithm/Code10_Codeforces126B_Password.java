package class101;

/**
 * Codeforces 126B Password
 * 
 * 题目来源：Codeforces
 * 题目链接：https://codeforces.com/problemset/problem/126/B
 * 
 * 题目描述：
 * 给定一个字符串s，找到一个子串，它既是前缀又是后缀，同时在字符串中间也出现过。
 * 如果有多个这样的子串，输出最长的那个。如果没有这样的子串，输出"Just a legend"。
 * 
 * 示例：
 * 输入："fixprefixsuffix"
 * 输出："fix"
 * 
 * 输入："abcdabc"
 * 输出："Just a legend"
 * 
 * 算法思路：
 * 使用KMP算法的next数组来解决这个问题。
 * 1. 构建字符串的next数组
 * 2. 通过next[n-1]找到最长的既是前缀又是后缀的子串
 * 3. 检查这个子串是否在中间出现过
 * 4. 如果没有，则通过next数组继续查找更短的候选子串
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 */
public class Code10_Codeforces126B_Password {

    /**
     * 找到符合条件的最长子串
     * 
     * @param s 输入字符串
     * @return 符合条件的最长子串，如果不存在则返回"Just a legend"
     */
    public static String findPassword(String s) {
        // 边界条件处理
        if (s == null || s.length() <= 2) {
            return "Just a legend";
        }
        
        char[] str = s.toCharArray();
        int n = str.length;
        
        // 构建next数组
        int[] next = buildNextArray(str);
        
        // 从最长的候选子串开始检查
        int candidateLength = next[n - 1];
        
        // 检查是否有符合条件的子串
        while (candidateLength > 0) {
            // 检查这个长度的子串是否在中间出现过
            if (isSubstringPresent(str, candidateLength, next)) {
                return s.substring(0, candidateLength);
            }
            // 尝试更短的候选子串
            candidateLength = next[candidateLength - 1];
        }
        
        return "Just a legend";
    }
    
    /**
     * 构建KMP算法的next数组（部分匹配表）
     * 
     * next[i]表示str[0...i]子串的最长相等前后缀的长度
     * 
     * @param str 字符数组
     * @return next数组
     */
    private static int[] buildNextArray(char[] str) {
        int length = str.length;
        int[] next = new int[length];
        
        // 初始化
        next[0] = 0;
        int prefixLen = 0;  // 当前最长相等前后缀的长度
        int i = 1;          // 当前处理的位置
        
        // 从位置1开始处理
        while (i < length) {
            // 如果当前字符匹配，可以延长相等前后缀
            if (str[i] == str[prefixLen]) {
                prefixLen++;
                next[i] = prefixLen;
                i++;
            } 
            // 如果不匹配且前缀长度大于0，需要回退
            else if (prefixLen > 0) {
                prefixLen = next[prefixLen - 1];
            } 
            // 如果不匹配且前缀长度为0，next[i] = 0
            else {
                next[i] = 0;
                i++;
            }
        }
        
        return next;
    }
    
    /**
     * 检查指定长度的前缀是否在字符串中间出现过
     * 
     * @param str 字符数组
     * @param length 子串长度
     * @param next next数组
     * @return 是否在中间出现过
     */
    private static boolean isSubstringPresent(char[] str, int length, int[] next) {
        // 在next数组中查找是否有等于length的值（除了最后一个位置）
        for (int i = 0; i < str.length - 1; i++) {
            if (next[i] == length) {
                return true;
            }
        }
        return false;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        String s1 = "fixprefixsuffix";
        String result1 = findPassword(s1);
        System.out.println("测试用例1:");
        System.out.println("输入字符串: " + s1);
        System.out.println("输出: " + result1);
        System.out.println("预期输出: fix\n");
        
        // 测试用例2
        String s2 = "abcdabc";
        String result2 = findPassword(s2);
        System.out.println("测试用例2:");
        System.out.println("输入字符串: " + s2);
        System.out.println("输出: " + result2);
        System.out.println("预期输出: Just a legend\n");
        
        // 测试用例3
        String s3 = "abcabcabcabc";
        String result3 = findPassword(s3);
        System.out.println("测试用例3:");
        System.out.println("输入字符串: " + s3);
        System.out.println("输出: " + result3);
        System.out.println("预期输出: abcabcabc\n");
        
        // 测试用例4
        String s4 = "aaaa";
        String result4 = findPassword(s4);
        System.out.println("测试用例4:");
        System.out.println("输入字符串: " + s4);
        System.out.println("输出: " + result4);
        System.out.println("预期输出: aaa\n");
        
        // 测试用例5
        String s5 = "abc";
        String result5 = findPassword(s5);
        System.out.println("测试用例5:");
        System.out.println("输入字符串: " + s5);
        System.out.println("输出: " + result5);
        System.out.println("预期输出: Just a legend");
    }
}