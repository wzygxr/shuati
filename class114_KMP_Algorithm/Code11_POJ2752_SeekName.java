package class101;

import java.util.*;

/**
 * POJ 2752 Seek the Name, Seek the Fame
 * 
 * 题目来源：POJ (北京大学在线评测系统)
 * 题目链接：http://poj.org/problem?id=2752
 * 
 * 题目描述：
 * 给定一个字符串，找到所有既是前缀又是后缀的子串。
 * 输出这些子串的长度，按升序排列。
 * 
 * 示例：
 * 输入："alala"
 * 输出："a", "ala", "alala"，对应的长度为1, 3, 5
 * 
 * 算法思路：
 * 使用KMP算法的next数组来解决这个问题。
 * 1. 构建字符串的next数组
 * 2. 通过next[n-1]找到最长的既是前缀又是后缀的子串
 * 3. 通过递归应用next函数，找到所有符合条件的子串
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 */
public class Code11_POJ2752_SeekName {

    /**
     * 找到所有既是前缀又是后缀的子串的长度
     * 
     * @param s 输入字符串
     * @return 所有符合条件的子串长度，按升序排列
     */
    public static List<Integer> findAllPrefixSuffixLengths(String s) {
        List<Integer> result = new ArrayList<>();
        
        // 边界条件处理
        if (s == null || s.length() == 0) {
            return result;
        }
        
        char[] str = s.toCharArray();
        int n = str.length;
        
        // 构建next数组
        int[] next = buildNextArray(str);
        
        // 通过next数组找到所有符合条件的长度
        int pos = n - 1;
        while (pos >= 0) {
            if (next[pos] > 0) {
                result.add(next[pos]);
                pos = next[pos] - 1;
            } else {
                pos--;
            }
        }
        
        // 添加整个字符串的长度
        result.add(n);
        
        // 按升序排列
        Collections.sort(result);
        
        return result;
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
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        String s1 = "alala";
        List<Integer> result1 = findAllPrefixSuffixLengths(s1);
        System.out.println("测试用例1:");
        System.out.println("输入字符串: " + s1);
        System.out.print("输出长度: ");
        for (int i = 0; i < result1.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result1.get(i));
        }
        System.out.println();
        System.out.print("对应子串: ");
        for (int i = 0; i < result1.size(); i++) {
            if (i > 0) System.out.print(", ");
            System.out.print("\"" + s1.substring(0, result1.get(i)) + "\"");
        }
        System.out.println("\n");
        
        // 测试用例2
        String s2 = "abcabcab";
        List<Integer> result2 = findAllPrefixSuffixLengths(s2);
        System.out.println("测试用例2:");
        System.out.println("输入字符串: " + s2);
        System.out.print("输出长度: ");
        for (int i = 0; i < result2.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result2.get(i));
        }
        System.out.println();
        System.out.print("对应子串: ");
        for (int i = 0; i < result2.size(); i++) {
            if (i > 0) System.out.print(", ");
            System.out.print("\"" + s2.substring(0, result2.get(i)) + "\"");
        }
        System.out.println("\n");
        
        // 测试用例3
        String s3 = "aaaa";
        List<Integer> result3 = findAllPrefixSuffixLengths(s3);
        System.out.println("测试用例3:");
        System.out.println("输入字符串: " + s3);
        System.out.print("输出长度: ");
        for (int i = 0; i < result3.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result3.get(i));
        }
        System.out.println();
        System.out.print("对应子串: ");
        for (int i = 0; i < result3.size(); i++) {
            if (i > 0) System.out.print(", ");
            System.out.print("\"" + s3.substring(0, result3.get(i)) + "\"");
        }
        System.out.println("\n");
        
        // 测试用例4
        String s4 = "abcdef";
        List<Integer> result4 = findAllPrefixSuffixLengths(s4);
        System.out.println("测试用例4:");
        System.out.println("输入字符串: " + s4);
        System.out.print("输出长度: ");
        for (int i = 0; i < result4.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result4.get(i));
        }
        System.out.println();
        System.out.print("对应子串: ");
        for (int i = 0; i < result4.size(); i++) {
            if (i > 0) System.out.print(", ");
            System.out.print("\"" + s4.substring(0, result4.get(i)) + "\"");
        }
        System.out.println();
    }
}