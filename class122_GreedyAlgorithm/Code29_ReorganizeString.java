package class091;

import java.util.*;

/**
 * 重构字符串
 * 
 * 题目描述：
 * 给定一个字符串 s ，检查是否能重新排布其中的字母，使得两相邻的字符不同。
 * 若可行，输出任意可行的结果。若不可行，返回空字符串。
 * 
 * 来源：LeetCode 767
 * 链接：https://leetcode.cn/problems/reorganize-string/
 * 
 * 算法思路：
 * 使用贪心算法 + 优先队列：
 * 1. 统计每个字符的出现频率
 * 2. 如果某个字符的频率超过字符串长度的一半，则无法重构，返回空字符串
 * 3. 使用最大堆（按频率排序）存储字符
 * 4. 每次从堆中取出频率最高的两个字符，交替排列
 * 5. 如果堆中还有剩余字符，继续处理
 * 
 * 时间复杂度：O(n * logk) - n是字符串长度，k是字符种类数
 * 空间复杂度：O(k) - 优先队列和频率统计的空间
 * 
 * 关键点分析：
 * - 贪心策略：每次选择频率最高的两个字符交替排列
 * - 边界处理：检查是否有字符频率超过一半
 * - 异常场景：单字符字符串的处理
 * 
 * 工程化考量：
 * - 输入验证：检查字符串是否为空或null
 * - 性能优化：使用数组而非HashMap统计频率
 * - 可读性：清晰的变量命名和注释
 */
public class Code29_ReorganizeString {
    
    /**
     * 重构字符串，使得相邻字符不同
     * 
     * @param s 输入字符串
     * @return 重构后的字符串，如果无法重构返回空字符串
     */
    public static String reorganizeString(String s) {
        // 输入验证
        if (s == null || s.length() == 0) {
            return "";
        }
        
        int n = s.length();
        
        // 统计字符频率
        int[] freq = new int[26];
        for (char c : s.toCharArray()) {
            freq[c - 'a']++;
        }
        
        // 检查是否有字符频率超过一半（向上取整）
        int maxFreq = 0;
        char maxChar = 'a';
        for (int i = 0; i < 26; i++) {
            if (freq[i] > maxFreq) {
                maxFreq = freq[i];
                maxChar = (char) ('a' + i);
            }
        }
        
        // 如果最大频率超过 (n+1)/2，则无法重构
        if (maxFreq > (n + 1) / 2) {
            return "";
        }
        
        // 使用优先队列存储字符和频率（最大堆）
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> b[1] - a[1]);
        for (int i = 0; i < 26; i++) {
            if (freq[i] > 0) {
                maxHeap.offer(new int[]{i, freq[i]});
            }
        }
        
        StringBuilder result = new StringBuilder();
        
        while (!maxHeap.isEmpty()) {
            // 取出频率最高的字符
            int[] first = maxHeap.poll();
            
            if (result.length() == 0 || result.charAt(result.length() - 1) != (char) ('a' + first[0])) {
                // 如果结果为空或最后一个字符不同，直接添加
                result.append((char) ('a' + first[0]));
                first[1]--;
                
                if (first[1] > 0) {
                    maxHeap.offer(first);
                }
            } else {
                // 如果需要交替，但堆为空，无法重构
                if (maxHeap.isEmpty()) {
                    return "";
                }
                
                // 取出第二个字符
                int[] second = maxHeap.poll();
                result.append((char) ('a' + second[0]));
                second[1]--;
                
                // 将两个字符重新加入堆中
                if (second[1] > 0) {
                    maxHeap.offer(second);
                }
                maxHeap.offer(first);
            }
        }
        
        return result.toString();
    }
    
    /**
     * 另一种实现：更简洁的交替排列方法
     * 时间复杂度：O(n * logk)
     * 空间复杂度：O(k)
     */
    public static String reorganizeStringAlternate(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        
        int n = s.length();
        int[] freq = new int[26];
        
        // 统计频率并找到最大频率字符
        int maxFreq = 0;
        int maxCharIndex = 0;
        for (char c : s.toCharArray()) {
            int index = c - 'a';
            freq[index]++;
            if (freq[index] > maxFreq) {
                maxFreq = freq[index];
                maxCharIndex = index;
            }
        }
        
        // 检查是否可重构
        if (maxFreq > (n + 1) / 2) {
            return "";
        }
        
        // 先放置最大频率字符
        char[] result = new char[n];
        int idx = 0;
        
        // 先填充偶数位置
        while (freq[maxCharIndex] > 0) {
            result[idx] = (char) ('a' + maxCharIndex);
            idx += 2;
            freq[maxCharIndex]--;
            
            // 如果偶数位置填满，转到奇数位置
            if (idx >= n) {
                idx = 1;
            }
        }
        
        // 填充其他字符
        for (int i = 0; i < 26; i++) {
            while (freq[i] > 0) {
                if (idx >= n) {
                    idx = 1;
                }
                result[idx] = (char) ('a' + i);
                idx += 2;
                freq[i]--;
            }
        }
        
        return new String(result);
    }
    
    /**
     * 验证字符串是否满足相邻字符不同的条件
     * 
     * @param s 要验证的字符串
     * @return 是否满足条件
     */
    public static boolean isValidReorganization(String s) {
        if (s == null || s.length() <= 1) {
            return true;
        }
        
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == s.charAt(i - 1)) {
                return false;
            }
        }
        return true;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1: "aab" -> "aba"
        String s1 = "aab";
        System.out.println("测试用例1: \"" + s1 + "\"");
        String result1 = reorganizeString(s1);
        String result1Alt = reorganizeStringAlternate(s1);
        System.out.println("结果1: \"" + result1 + "\", 有效: " + isValidReorganization(result1));
        System.out.println("结果2: \"" + result1Alt + "\", 有效: " + isValidReorganization(result1Alt));
        
        // 测试用例2: "aaab" -> "" (无法重构)
        String s2 = "aaab";
        System.out.println("\n测试用例2: \"" + s2 + "\"");
        String result2 = reorganizeString(s2);
        String result2Alt = reorganizeStringAlternate(s2);
        System.out.println("结果1: \"" + result2 + "\"");
        System.out.println("结果2: \"" + result2Alt + "\"");
        
        // 测试用例3: "abc" -> 任意有效排列
        String s3 = "abc";
        System.out.println("\n测试用例3: \"" + s3 + "\"");
        String result3 = reorganizeString(s3);
        String result3Alt = reorganizeStringAlternate(s3);
        System.out.println("结果1: \"" + result3 + "\", 有效: " + isValidReorganization(result3));
        System.out.println("结果2: \"" + result3Alt + "\", 有效: " + isValidReorganization(result3Alt));
        
        // 测试用例4: "a" -> "a"
        String s4 = "a";
        System.out.println("\n测试用例4: \"" + s4 + "\"");
        String result4 = reorganizeString(s4);
        String result4Alt = reorganizeStringAlternate(s4);
        System.out.println("结果1: \"" + result4 + "\", 有效: " + isValidReorganization(result4));
        System.out.println("结果2: \"" + result4Alt + "\", 有效: " + isValidReorganization(result4Alt));
        
        // 测试用例5: "aa" -> "" (无法重构)
        String s5 = "aa";
        System.out.println("\n测试用例5: \"" + s5 + "\"");
        String result5 = reorganizeString(s5);
        String result5Alt = reorganizeStringAlternate(s5);
        System.out.println("结果1: \"" + result5 + "\"");
        System.out.println("结果2: \"" + result5Alt + "\"");
        
        // 测试用例6: "aabbcc" -> 有效排列
        String s6 = "aabbcc";
        System.out.println("\n测试用例6: \"" + s6 + "\"");
        String result6 = reorganizeString(s6);
        String result6Alt = reorganizeStringAlternate(s6);
        System.out.println("结果1: \"" + result6 + "\", 有效: " + isValidReorganization(result6));
        System.out.println("结果2: \"" + result6Alt + "\", 有效: " + isValidReorganization(result6Alt));
        
        // 性能测试
        performanceTest();
    }
    
    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        // 生成大规模测试数据
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            sb.append((char) ('a' + random.nextInt(26)));
        }
        String largeString = sb.toString();
        
        System.out.println("\n=== 性能测试 ===");
        
        long startTime1 = System.currentTimeMillis();
        String result1 = reorganizeString(largeString);
        long endTime1 = System.currentTimeMillis();
        System.out.println("方法1执行时间: " + (endTime1 - startTime1) + "ms");
        System.out.println("方法1结果有效: " + isValidReorganization(result1));
        
        long startTime2 = System.currentTimeMillis();
        String result2 = reorganizeStringAlternate(largeString);
        long endTime2 = System.currentTimeMillis();
        System.out.println("方法2执行时间: " + (endTime2 - startTime2) + "ms");
        System.out.println("方法2结果有效: " + isValidReorganization(result2));
    }
    
    /**
     * 算法复杂度分析
     */
    public static void analyzeComplexity() {
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("方法1（优先队列）:");
        System.out.println("- 时间复杂度: O(n * logk)");
        System.out.println("  - 统计频率: O(n)");
        System.out.println("  - 堆操作: O(n * logk)，k为字符种类数");
        System.out.println("- 空间复杂度: O(k)");
        System.out.println("  - 频率数组: O(26) ≈ O(1)");
        System.out.println("  - 优先队列: O(k)");
        
        System.out.println("\n方法2（交替填充）:");
        System.out.println("- 时间复杂度: O(n)");
        System.out.println("  - 统计频率: O(n)");
        System.out.println("  - 填充数组: O(n)");
        System.out.println("- 空间复杂度: O(n)");
        System.out.println("  - 结果数组: O(n)");
        
        System.out.println("\n贪心策略证明:");
        System.out.println("1. 优先处理频率最高的字符可以避免冲突");
        System.out.println("2. 交替排列确保相邻字符不同");
        System.out.println("3. 数学证明：当最大频率 ≤ (n+1)/2 时可重构");
    }
}