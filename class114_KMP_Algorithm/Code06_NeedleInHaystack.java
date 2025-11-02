package class101;

import java.util.*;

/**
 * SPOJ NHAY - A Needle in the Haystack
 * 
 * 题目描述：
 * 编写一个程序，在给定的输入字符串中找到所有给定模式的出现位置。
 * 这通常被称为在干草堆中找针。
 * 
 * 输入格式：
 * 输入包含多个测试用例。
 * 每个测试用例由两行组成：
 * 第一行包含模式的长度m (1 <= m <= 10000)
 * 第二行包含模式本身
 * 第三行包含文本的长度n (1 <= n <= 1000000)
 * 第四行包含文本本身
 * 
 * 输出格式：
 * 对于每个测试用例，输出所有匹配位置的索引（从0开始）。
 * 如果没有匹配，则不输出任何内容。
 * 
 * 算法思路：
 * 使用KMP算法进行字符串匹配，找到所有匹配位置。
 * 
 * 时间复杂度：O(n + m)，其中n是文本串长度，m是模式串长度
 * 空间复杂度：O(m)，用于存储next数组
 */
public class Code06_NeedleInHaystack {

    /**
     * 在文本串中查找模式串的所有出现位置
     * 
     * @param text 文本串
     * @param pattern 模式串
     * @return 所有匹配位置的列表（从0开始计数）
     */
    public static List<Integer> findAllOccurrences(String text, String pattern) {
        List<Integer> positions = new ArrayList<>();
        
        // 边界条件处理
        if (pattern == null || pattern.length() == 0 || 
            text == null || text.length() < pattern.length()) {
            return positions;
        }
        
        char[] textArr = text.toCharArray();
        char[] patternArr = pattern.toCharArray();
        
        // 构建next数组
        int[] next = buildNextArray(patternArr);
        
        int textIndex = 0;      // 文本串指针
        int patternIndex = 0;   // 模式串指针
        
        // 匹配过程
        while (textIndex < textArr.length) {
            // 字符匹配，两个指针都向前移动
            if (textArr[textIndex] == patternArr[patternIndex]) {
                textIndex++;
                patternIndex++;
            } 
            // 字符不匹配且模式串指针不为0，根据next数组调整模式串指针
            else if (patternIndex > 0) {
                patternIndex = next[patternIndex - 1];
            } 
            // 字符不匹配且模式串指针为0，文本串指针向前移动
            else {
                textIndex++;
            }
            
            // 如果模式串指针等于模式串长度，说明匹配成功
            if (patternIndex == patternArr.length) {
                // 记录匹配位置（从0开始计数）
                positions.add(textIndex - patternIndex);
                // 根据next数组调整模式串指针，继续查找下一个匹配
                patternIndex = next[patternIndex - 1];
            }
        }
        
        return positions;
    }
    
    /**
     * 构建KMP算法的next数组（部分匹配表）
     * 
     * next[i]表示pattern[0...i]子串的最长相等前后缀的长度
     * 
     * 算法思路：
     * 1. 初始化next[0] = 0
     * 2. 使用双指针i和j，i指向当前处理的位置，j指向前缀的末尾
     * 3. 如果pattern[i] == pattern[j]，说明前缀和后缀可以延长，next[i] = j + 1
     * 4. 如果pattern[i] != pattern[j]，需要回退j指针到next[j-1]，直到匹配或j=0
     * 
     * @param pattern 模式串字符数组
     * @return next数组
     */
    private static int[] buildNextArray(char[] pattern) {
        int length = pattern.length;
        int[] next = new int[length];
        
        // 初始化
        next[0] = 0;
        int prefixLen = 0;  // 当前最长相等前后缀的长度
        int i = 1;          // 当前处理的位置
        
        // 从位置1开始处理
        while (i < length) {
            // 如果当前字符匹配，可以延长相等前后缀
            if (pattern[i] == pattern[prefixLen]) {
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
        String text1 = "abcabcabcabc";
        String pattern1 = "abc";
        List<Integer> result1 = findAllOccurrences(text1, pattern1);
        System.out.println("文本串: " + text1);
        System.out.println("模式串: " + pattern1);
        System.out.print("匹配位置: ");
        for (int pos : result1) {
            System.out.print(pos + " ");
        }
        System.out.println("\n");
        
        // 测试用例2
        String text2 = "abababab";
        String pattern2 = "aba";
        List<Integer> result2 = findAllOccurrences(text2, pattern2);
        System.out.println("文本串: " + text2);
        System.out.println("模式串: " + pattern2);
        System.out.print("匹配位置: ");
        for (int pos : result2) {
            System.out.print(pos + " ");
        }
        System.out.println("\n");
        
        // 测试用例3
        String text3 = "aaaaa";
        String pattern3 = "aa";
        List<Integer> result3 = findAllOccurrences(text3, pattern3);
        System.out.println("文本串: " + text3);
        System.out.println("模式串: " + pattern3);
        System.out.print("匹配位置: ");
        for (int pos : result3) {
            System.out.print(pos + " ");
        }
        System.out.println("\n");
        
        // 测试用例4 - 无匹配
        String text4 = "abcdef";
        String pattern4 = "xyz";
        List<Integer> result4 = findAllOccurrences(text4, pattern4);
        System.out.println("文本串: " + text4);
        System.out.println("模式串: " + pattern4);
        System.out.print("匹配位置: ");
        for (int pos : result4) {
            System.out.print(pos + " ");
        }
        System.out.println("\n");
    }
}