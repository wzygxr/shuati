package class101;

/**
 * LeetCode 1392. 最长快乐前缀
 * 
 * 题目描述：
 * 「快乐前缀」是在原字符串中既是非空前缀也是后缀（不包括原字符串自身）的字符串。
 * 给你一个字符串 s，请你返回它的最长快乐前缀。
 * 如果不存在满足题意的前缀，则返回一个空字符串 ""。
 * 
 * 示例：
 * 输入：s = "level"
 * 输出："l"
 * 
 * 输入：s = "ababab"
 * 输出："abab"
 * 
 * 算法思路：
 * 使用KMP算法的next数组来解决这个问题。
 * 最长快乐前缀就是字符串的最长相等前后缀。
 * next[n-1]表示整个字符串的最长相等前后缀的长度。
 * 因此，答案就是长度为next[n-1]的前缀。
 * 
 * 时间复杂度：O(N)，其中N是字符串长度
 * 空间复杂度：O(N)，用于存储next数组
 */
public class Code08_LongestHappyPrefix {

    /**
     * 找到字符串的最长快乐前缀
     * 
     * @param s 输入字符串
     * @return 最长快乐前缀
     */
    public static String longestPrefix(String s) {
        // 边界条件处理
        if (s == null || s.length() <= 1) {
            return "";
        }
        
        char[] str = s.toCharArray();
        int n = str.length;
        
        // 构建next数组
        int[] next = buildNextArray(str);
        
        // 最长快乐前缀的长度就是next[n-1]
        int prefixLength = next[n - 1];
        
        // 返回长度为prefixLength的前缀
        return s.substring(0, prefixLength);
    }
    
    /**
     * 构建KMP算法的next数组（部分匹配表）
     * 
     * next[i]表示str[0...i]子串的最长相等前后缀的长度
     * 
     * 算法思路：
     * 1. 初始化next[0] = 0
     * 2. 使用双指针i和j，i指向当前处理的位置，j指向前缀的末尾
     * 3. 如果str[i] == str[j]，说明前缀和后缀可以延长，next[i] = j + 1
     * 4. 如果str[i] != str[j]，需要回退j指针到next[j-1]，直到匹配或j=0
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
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        String s1 = "level";
        String result1 = longestPrefix(s1);
        System.out.println("字符串: " + s1);
        System.out.println("最长快乐前缀: \"" + result1 + "\"");
        System.out.println();
        
        // 测试用例2
        String s2 = "ababab";
        String result2 = longestPrefix(s2);
        System.out.println("字符串: " + s2);
        System.out.println("最长快乐前缀: \"" + result2 + "\"");
        System.out.println();
        
        // 测试用例3
        String s3 = "leetcodeleet";
        String result3 = longestPrefix(s3);
        System.out.println("字符串: " + s3);
        System.out.println("最长快乐前缀: \"" + result3 + "\"");
        System.out.println();
        
        // 测试用例4
        String s4 = "a";
        String result4 = longestPrefix(s4);
        System.out.println("字符串: " + s4);
        System.out.println("最长快乐前缀: \"" + result4 + "\"");
        System.out.println();
        
        // 测试用例5
        String s5 = "abcabcabc";
        String result5 = longestPrefix(s5);
        System.out.println("字符串: " + s5);
        System.out.println("最长快乐前缀: \"" + result5 + "\"");
        System.out.println();
    }
}