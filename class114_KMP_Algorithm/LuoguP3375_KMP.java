package class101.extended;

import java.io.*;
import java.util.*;

/**
 * 洛谷 P3375 【模板】KMP字符串匹配
 * 
 * 题目描述：
 * 给定两个字符串text和pattern，要求输出pattern在text中出现的位置（从1开始），
 * 并输出pattern的next数组。
 * 
 * 输入格式：
 * 两行，每行一个字符串，分别表示text和pattern。
 * 
 * 输出格式：
 * 第一行输出pattern在text中出现的位置，以空格隔开。
 * 第二行输出pattern的next数组，以空格隔开。
 * 
 * 算法思路：
 * 这是KMP算法的经典模板题。需要实现：
 * 1. 构建next数组
 * 2. 使用KMP算法匹配字符串
 * 3. 输出所有匹配位置和next数组
 * 
 * 时间复杂度：O(n + m)，其中n是文本串长度，m是模式串长度
 * 空间复杂度：O(m)，用于存储next数组
 */
public class LuoguP3375_KMP {
    
    /**
     * KMP算法主函数
     * 
     * @param text 文本串
     * @param pattern 模式串
     * @return 包含所有匹配位置和next数组的Pair对象
     */
    public static Pair<List<Integer>, int[]> kmp(String text, String pattern) {
        char[] textArr = text.toCharArray();
        char[] patternArr = pattern.toCharArray();
        
        // 构建next数组
        int[] next = buildNextArray(patternArr);
        
        // 查找所有匹配位置
        List<Integer> positions = findAllMatches(textArr, patternArr, next);
        
        return new Pair<>(positions, next);
    }
    
    /**
     * 构建KMP算法的next数组（部分匹配表）
     * 
     * next[i]表示pattern[0...i-1]子串的最长相等前后缀的长度
     * 
     * 算法思路：
     * 1. 初始化next[0] = -1, next[1] = 0
     * 2. 使用双指针i和cn，i指向当前处理的位置，cn表示当前最长相等前后缀的长度
     * 3. 如果pattern[i-1] == pattern[cn]，说明前缀和后缀可以延长，next[i] = ++cn
     * 4. 如果pattern[i-1] != pattern[cn]且cn > 0，需要回退cn指针到next[cn]
     * 5. 如果pattern[i-1] != pattern[cn]且cn == 0，next[i] = 0
     * 
     * @param pattern 模式串字符数组
     * @return next数组
     */
    private static int[] buildNextArray(char[] pattern) {
        int length = pattern.length;
        int[] next = new int[length + 1]; // next数组长度为pattern.length + 1
        
        // 初始化
        next[0] = -1;
        next[1] = 0;
        
        int i = 2;   // 当前处理的位置
        int cn = 0;  // 当前最长相等前后缀的长度
        
        // 从位置2开始处理
        while (i <= length) {
            // 如果当前字符匹配，可以延长相等前后缀
            if (pattern[i - 1] == pattern[cn]) {
                next[i] = ++cn;
                i++;
            } 
            // 如果不匹配且cn > 0，需要回退
            else if (cn > 0) {
                cn = next[cn];
            } 
            // 如果不匹配且cn == 0，next[i] = 0
            else {
                next[i] = 0;
                i++;
            }
        }
        
        return next;
    }
    
    /**
     * 使用KMP算法查找文本串中所有匹配模式串的位置
     * 
     * @param text 文本串字符数组
     * @param pattern 模式串字符数组
     * @param next next数组
     * @return 所有匹配位置的列表（从1开始计数）
     */
    private static List<Integer> findAllMatches(char[] text, char[] pattern, int[] next) {
        List<Integer> positions = new ArrayList<>();
        
        int textLength = text.length;
        int patternLength = pattern.length;
        
        int textIndex = 0;      // 文本串指针
        int patternIndex = 0;   // 模式串指针
        
        // 匹配过程
        while (textIndex < textLength && patternIndex < patternLength) {
            // 字符匹配，两个指针都向前移动
            if (text[textIndex] == pattern[patternIndex]) {
                textIndex++;
                patternIndex++;
            } 
            // 字符不匹配且模式串指针不为0，根据next数组调整模式串指针
            else if (patternIndex > 0) {
                patternIndex = next[patternIndex];
            } 
            // 字符不匹配且模式串指针为0，文本串指针向前移动
            else {
                textIndex++;
            }
            
            // 如果模式串指针等于模式串长度，说明匹配成功
            if (patternIndex == patternLength) {
                // 记录匹配位置（从1开始计数）
                positions.add(textIndex - patternIndex + 1);
                // 根据next数组调整模式串指针，继续查找下一个匹配
                patternIndex = next[patternIndex];
            }
        }
        
        return positions;
    }
    
    /**
     * 用于存储结果的简单Pair类
     */
    static class Pair<T, U> {
        public final T first;
        public final U second;
        
        public Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 示例测试
        String text = "ABABABC";
        String pattern = "ABA";
        
        Pair<List<Integer>, int[]> result = kmp(text, pattern);
        
        System.out.println("文本串: " + text);
        System.out.println("模式串: " + pattern);
        System.out.println();
        
        // 输出所有匹配位置
        System.out.print("匹配位置: ");
        for (int pos : result.first) {
            System.out.print(pos + " ");
        }
        System.out.println();
        
        // 输出next数组
        System.out.print("next数组: ");
        int[] next = result.second;
        for (int i = 1; i < next.length; i++) {
            System.out.print(next[i] + " ");
        }
        System.out.println();
        
        // 更多测试用例
        System.out.println("\n=== 更多测试用例 ===");
        
        // 测试用例1
        String text1 = "abcabcabcabc";
        String pattern1 = "abc";
        Pair<List<Integer>, int[]> result1 = kmp(text1, pattern1);
        System.out.println("文本串: " + text1);
        System.out.println("模式串: " + pattern1);
        System.out.print("匹配位置: ");
        for (int pos : result1.first) {
            System.out.print(pos + " ");
        }
        System.out.println();
        System.out.print("next数组: ");
        int[] next1 = result1.second;
        for (int i = 1; i < next1.length; i++) {
            System.out.print(next1[i] + " ");
        }
        System.out.println("\n");
        
        // 测试用例2
        String text2 = "aaaaa";
        String pattern2 = "aa";
        Pair<List<Integer>, int[]> result2 = kmp(text2, pattern2);
        System.out.println("文本串: " + text2);
        System.out.println("模式串: " + pattern2);
        System.out.print("匹配位置: ");
        for (int pos : result2.first) {
            System.out.print(pos + " ");
        }
        System.out.println();
        System.out.print("next数组: ");
        int[] next2 = result2.second;
        for (int i = 1; i < next2.length; i++) {
            System.out.print(next2[i] + " ");
        }
        System.out.println();
    }
}