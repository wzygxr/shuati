package class090;

import java.util.*;

// 重构字符串
// 给定一个字符串 s ，检查是否能重新排布其中的字母，使得两相邻的字符不同。
// 如果可以，输出任意可行的结果。如果不可行，返回空字符串。
// 测试链接: https://leetcode.cn/problems/reorganize-string/
public class Code27_ReorganizeString {

    /**
     * 重构字符串问题的贪心解法
     * 
     * 解题思路：
     * 1. 统计每个字符的出现频率
     * 2. 使用最大堆存储字符及其频率，按频率降序排列
     * 3. 每次从堆中取出频率最高的两个字符，交替放置
     * 4. 如果某个字符频率过高，无法重构，返回空字符串
     * 
     * 贪心策略的正确性：
     * 局部最优：每次选择频率最高的两个字符交替放置
     * 全局最优：得到相邻字符不同的字符串
     * 
     * 时间复杂度：O(n log k)，其中k是字符种类数
     * 空间复杂度：O(k)，用于存储字符频率的堆
     * 
     * @param s 输入字符串
     * @return 重构后的字符串或空字符串
     */
    public static String reorganizeString(String s) {
        // 边界条件处理
        if (s == null || s.length() == 0) return "";
        
        // 统计字符频率
        int[] freq = new int[26];
        for (char c : s.toCharArray()) {
            freq[c - 'a']++;
        }
        
        // 使用最大堆存储字符频率（按频率降序排列）
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> b[1] - a[1]);
        
        // 将字符及其频率加入堆中
        for (int i = 0; i < 26; i++) {
            if (freq[i] > 0) {
                maxHeap.offer(new int[]{i, freq[i]});
            }
        }
        
        // 如果最高频率超过一半加一，无法重构
        int maxFreq = maxHeap.peek()[1];
        if (maxFreq > (s.length() + 1) / 2) {
            return "";
        }
        
        // 构建结果字符串
        StringBuilder result = new StringBuilder();
        
        while (maxHeap.size() >= 2) {
            // 取出频率最高的两个字符
            int[] first = maxHeap.poll();
            int[] second = maxHeap.poll();
            
            // 交替放置这两个字符
            result.append((char)('a' + first[0]));
            result.append((char)('a' + second[0]));
            
            // 减少频率并重新加入堆中
            if (--first[1] > 0) {
                maxHeap.offer(first);
            }
            if (--second[1] > 0) {
                maxHeap.offer(second);
            }
        }
        
        // 处理最后一个字符（如果有）
        if (!maxHeap.isEmpty()) {
            int[] last = maxHeap.poll();
            result.append((char)('a' + last[0]));
        }
        
        return result.toString();
    }

    /**
     * 重构字符串问题的另一种解法（基于奇偶位置）
     * 
     * 解题思路：
     * 1. 统计字符频率，找到最高频率字符
     * 2. 如果最高频率超过一半加一，无法重构
     * 3. 将最高频率字符放在偶数位置，其他字符放在奇数位置
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public static String reorganizeString2(String s) {
        if (s == null || s.length() == 0) return "";
        
        // 统计字符频率
        int[] freq = new int[26];
        for (char c : s.toCharArray()) {
            freq[c - 'a']++;
        }
        
        // 找到最高频率字符
        int maxFreq = 0;
        char maxChar = 'a';
        for (int i = 0; i < 26; i++) {
            if (freq[i] > maxFreq) {
                maxFreq = freq[i];
                maxChar = (char)('a' + i);
            }
        }
        
        // 检查是否可重构
        if (maxFreq > (s.length() + 1) / 2) {
            return "";
        }
        
        // 创建结果数组
        char[] result = new char[s.length()];
        int index = 0;
        
        // 先放置最高频率字符在偶数位置
        while (freq[maxChar - 'a'] > 0) {
            result[index] = maxChar;
            index += 2;
            freq[maxChar - 'a']--;
            
            // 如果偶数位置用完，转到奇数位置
            if (index >= s.length()) {
                index = 1;
            }
        }
        
        // 放置其他字符
        for (int i = 0; i < 26; i++) {
            while (freq[i] > 0) {
                if (index >= s.length()) {
                    index = 1;
                }
                result[index] = (char)('a' + i);
                index += 2;
                freq[i]--;
            }
        }
        
        return new String(result);
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        // 输入: s = "aab"
        // 输出: "aba"
        String s1 = "aab";
        System.out.println("测试用例1结果: " + reorganizeString(s1)); // 期望输出: "aba"
        
        // 测试用例2
        // 输入: s = "aaab"
        // 输出: ""
        // 解释: 无法重构，因为'a'出现次数过多
        String s2 = "aaab";
        System.out.println("测试用例2结果: " + reorganizeString(s2)); // 期望输出: ""
        
        // 测试用例3
        // 输入: s = "vvvlo"
        // 输出: "vlvov" 或 "vovlv"
        String s3 = "vvvlo";
        System.out.println("测试用例3结果: " + reorganizeString(s3)); // 期望输出非空
        
        // 测试用例4：边界情况
        // 输入: s = "a"
        // 输出: "a"
        String s4 = "a";
        System.out.println("测试用例4结果: " + reorganizeString(s4)); // 期望输出: "a"
        
        // 测试用例5：复杂情况
        // 输入: s = "abbcccdddd"
        // 输出: 非空字符串
        String s5 = "abbcccdddd";
        System.out.println("测试用例5结果: " + reorganizeString(s5)); // 期望输出非空
        
        // 测试用例6：极限情况
        // 输入: s = "aaaaabbbcc"
        // 输出: 非空字符串
        String s6 = "aaaaabbbcc";
        System.out.println("测试用例6结果: " + reorganizeString(s6)); // 期望输出非空
    }
}