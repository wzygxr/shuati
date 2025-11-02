import java.util.*;

public class Code13_RearrangeString {
    """
    相关题目13: LeetCode 767. 重构字符串
    题目链接: https://leetcode.cn/problems/reorganize-string/
    题目描述: 给定一个字符串S，检查是否能重新排布其中的字母，使得两相邻的字符不同。
    若可行，输出任意可行的结果。若不可行，返回空字符串。
    解题思路: 使用最大堆按字符频率排序，然后贪心选择频率最高的字符进行放置
    时间复杂度: O(n log k)，其中n是字符串长度，k是不同字符的数量（最大为26）
    空间复杂度: O(k)，用于存储字符频率和堆
    是否最优解: 此方法是最优解，没有更优的算法
    
    本题属于堆的应用场景：基于频率的优先级处理问题
    """
    
    static class Solution {
        /**
         * 重构字符串，使得相邻字符不同
         * 
         * @param s 输入字符串
         * @return 重构后的字符串，如果无法重构则返回空字符串
         */
        public String reorganizeString(String s) {
            // 异常处理：检查输入字符串是否为空
            if (s == null || s.isEmpty()) {
                return "";
            }
            
            // 统计每个字符的出现频率
            Map<Character, Integer> charCount = new HashMap<>();
            for (char c : s.toCharArray()) {
                charCount.put(c, charCount.getOrDefault(c, 0) + 1);
            }
            
            // 检查是否可以重构：最多的字符出现次数不能超过(len(s)+1)//2
            int maxCount = 0;
            for (int count : charCount.values()) {
                maxCount = Math.max(maxCount, count);
            }
            if (maxCount > (s.length() + 1) / 2) {
                return "";
            }
            
            // 创建最大堆（根据字符频率排序）
            PriorityQueue<Map.Entry<Character, Integer>> maxHeap = 
                new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());
            maxHeap.addAll(charCount.entrySet());
            
            // 用于存储重构后的字符串
            StringBuilder result = new StringBuilder();
            
            // 当堆中有元素时
            while (!maxHeap.isEmpty()) {
                // 获取当前频率最高的字符
                Map.Entry<Character, Integer> entry1 = maxHeap.poll();
                char char1 = entry1.getKey();
                int count1 = entry1.getValue();
                
                // 如果结果为空或当前字符与结果最后一个字符不同，直接添加
                if (result.length() == 0 || char1 != result.charAt(result.length() - 1)) {
                    result.append(char1);
                    count1--;
                    // 如果字符还有剩余，将其放回堆中
                    if (count1 > 0) {
                        entry1.setValue(count1);
                        maxHeap.offer(entry1);
                    }
                } else {
                    // 如果当前字符与结果最后一个字符相同，需要选择下一个最高频率的字符
                    // 如果堆为空，说明无法重构
                    if (maxHeap.isEmpty()) {
                        return "";
                    }
                    
                    // 获取次高频率的字符
                    Map.Entry<Character, Integer> entry2 = maxHeap.poll();
                    char char2 = entry2.getKey();
                    int count2 = entry2.getValue();
                    
                    result.append(char2);
                    count2--;
                    // 如果次高频率字符还有剩余，将其放回堆中
                    if (count2 > 0) {
                        entry2.setValue(count2);
                        maxHeap.offer(entry2);
                    }
                    
                    // 将最高频率字符放回堆中
                    maxHeap.offer(entry1);
                }
            }
            
            return result.toString();
        }
    }
    
    static class AlternativeSolution {
        /**
         * 另一种实现方式，使用贪心算法直接构建结果字符串
         * 这种方法可能在某些情况下更直观
         */
        public String reorganizeString(String s) {
            if (s == null || s.isEmpty()) {
                return "";
            }
            
            int n = s.length();
            // 统计字符频率
            int[] count = new int[26]; // 假设只有小写字母
            int maxCount = 0;
            char maxChar = ' ';
            
            for (char c : s.toCharArray()) {
                count[c - 'a']++;
                if (count[c - 'a'] > maxCount) {
                    maxCount = count[c - 'a'];
                    maxChar = c;
                }
            }
            
            // 检查是否可以重构
            if (maxCount > (n + 1) / 2) {
                return "";
            }
            
            // 创建结果数组
            char[] result = new char[n];
            int index = 0;
            
            // 首先放置频率最高的字符，间隔放置
            while (count[maxChar - 'a'] > 0) {
                result[index] = maxChar;
                index += 2;
                count[maxChar - 'a']--;
            }
            
            // 放置剩余的字符
            for (char c = 'a'; c <= 'z'; c++) {
                while (count[c - 'a'] > 0) {
                    // 如果到达数组末尾，从索引1开始
                    if (index >= n) {
                        index = 1;
                    }
                    result[index] = c;
                    index += 2;
                    count[c - 'a']--;
                }
            }
            
            return new String(result);
        }
    }
    
    static class OptimizedHeapSolution {
        /**
         * 优化的堆实现，使用更简洁的逻辑
         */
        public String reorganizeString(String s) {
            if (s == null || s.isEmpty()) {
                return "";
            }
            
            // 统计字符频率
            Map<Character, Integer> charCount = new HashMap<>();
            for (char c : s.toCharArray()) {
                charCount.put(c, charCount.getOrDefault(c, 0) + 1);
            }
            
            int n = s.length();
            // 检查是否可以重构
            int maxCount = 0;
            for (int count : charCount.values()) {
                maxCount = Math.max(maxCount, count);
            }
            if (maxCount > (n + 1) / 2) {
                return "";
            }
            
            // 创建最大堆（根据字符频率排序）
            PriorityQueue<Map.Entry<Character, Integer>> maxHeap = 
                new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());
            maxHeap.addAll(charCount.entrySet());
            
            StringBuilder result = new StringBuilder();
            
            // 不断从堆中取出两个字符添加到结果中
            // 这样可以确保不会有相同字符相邻
            while (maxHeap.size() >= 2) {
                Map.Entry<Character, Integer> entry1 = maxHeap.poll();
                Map.Entry<Character, Integer> entry2 = maxHeap.poll();
                
                char char1 = entry1.getKey();
                char char2 = entry2.getKey();
                int count1 = entry1.getValue();
                int count2 = entry2.getValue();
                
                // 添加两个不同的字符
                result.append(char1).append(char2);
                
                // 如果字符还有剩余，放回堆中
                if (count1 > 1) {
                    entry1.setValue(count1 - 1);
                    maxHeap.offer(entry1);
                }
                if (count2 > 1) {
                    entry2.setValue(count2 - 1);
                    maxHeap.offer(entry2);
                }
            }
            
            // 如果堆中还有一个字符，说明字符串长度为奇数，添加最后一个字符
            if (!maxHeap.isEmpty()) {
                result.append(maxHeap.poll().getKey());
            }
            
            return result.toString();
        }
    }
    
    /**
     * 辅助函数，验证重构后的字符串是否有效
     */
    private static boolean isValidReorganization(String original, String reorganized) {
        // 检查是否为空字符串且原字符串不为空
        if (reorganized.isEmpty() && !original.isEmpty()) {
            // 检查是否真的无法重构
            Map<Character, Integer> charCount = new HashMap<>();
            for (char c : original.toCharArray()) {
                charCount.put(c, charCount.getOrDefault(c, 0) + 1);
            }
            int maxCount = 0;
            for (int count : charCount.values()) {
                maxCount = Math.max(maxCount, count);
            }
            return maxCount > (original.length() + 1) / 2;
        }
        
        // 检查长度
        if (original.length() != reorganized.length()) {
            return false;
        }
        
        // 检查相邻字符是否不同
        for (int i = 1; i < reorganized.length(); i++) {
            if (reorganized.charAt(i) == reorganized.charAt(i - 1)) {
                return false;
            }
        }
        
        // 检查字符频率是否匹配
        Map<Character, Integer> originalCount = new HashMap<>();
        Map<Character, Integer> reorganizedCount = new HashMap<>();
        
        for (char c : original.toCharArray()) {
            originalCount.put(c, originalCount.getOrDefault(c, 0) + 1);
        }
        
        for (char c : reorganized.toCharArray()) {
            reorganizedCount.put(c, reorganizedCount.getOrDefault(c, 0) + 1);
        }
        
        return originalCount.equals(reorganizedCount);
    }
    
    /**
     * 测试函数，验证算法在不同输入情况下的正确性
     */
    public static void testReorganizeString() {
        System.out.println("=== 测试重构字符串算法 ===");
        Solution solution = new Solution();
        AlternativeSolution alternativeSolution = new AlternativeSolution();
        OptimizedHeapSolution optimizedSolution = new OptimizedHeapSolution();
        
        // 测试用例1：基本用例 - 可以重构
        System.out.println("\n测试用例1：基本用例 - 可以重构");
        String s1 = "aab";
        String result1 = solution.reorganizeString(s1);
        String altResult1 = alternativeSolution.reorganizeString(s1);
        String optResult1 = optimizedSolution.reorganizeString(s1);
        
        System.out.println("原字符串: " + s1);
        System.out.println("堆方法结果: " + result1 + ", 有效: " + isValidReorganization(s1, result1));
        System.out.println("贪心方法结果: " + altResult1 + ", 有效: " + isValidReorganization(s1, altResult1));
        System.out.println("优化堆方法结果: " + optResult1 + ", 有效: " + isValidReorganization(s1, optResult1));
        
        // 测试用例2：基本用例 - 可以重构
        System.out.println("\n测试用例2：基本用例 - 可以重构");
        String s2 = "aaab";
        String result2 = solution.reorganizeString(s2);
        String altResult2 = alternativeSolution.reorganizeString(s2);
        String optResult2 = optimizedSolution.reorganizeString(s2);
        
        System.out.println("原字符串: " + s2);
        System.out.println("堆方法结果: " + result2 + ", 有效: " + isValidReorganization(s2, result2));
        System.out.println("贪心方法结果: " + altResult2 + ", 有效: " + isValidReorganization(s2, altResult2));
        System.out.println("优化堆方法结果: " + optResult2 + ", 有效: " + isValidReorganization(s2, optResult2));
        
        // 测试用例3：无法重构的情况
        System.out.println("\n测试用例3：无法重构的情况");
        String s3 = "aaabbc";
        String result3 = solution.reorganizeString(s3);
        String altResult3 = alternativeSolution.reorganizeString(s3);
        String optResult3 = optimizedSolution.reorganizeString(s3);
        
        System.out.println("原字符串: " + s3);
        System.out.println("堆方法结果: " + result3 + ", 有效: " + isValidReorganization(s3, result3));
        System.out.println("贪心方法结果: " + altResult3 + ", 有效: " + isValidReorganization(s3, altResult3));
        System.out.println("优化堆方法结果: " + optResult3 + ", 有效: " + isValidReorganization(s3, optResult3));
        
        // 测试用例4：单字符
        System.out.println("\n测试用例4：单字符");
        String s4 = "a";
        String result4 = solution.reorganizeString(s4);
        String altResult4 = alternativeSolution.reorganizeString(s4);
        String optResult4 = optimizedSolution.reorganizeString(s4);
        
        System.out.println("原字符串: " + s4);
        System.out.println("堆方法结果: " + result4 + ", 有效: " + isValidReorganization(s4, result4));
        System.out.println("贪心方法结果: " + altResult4 + ", 有效: " + isValidReorganization(s4, altResult4));
        System.out.println("优化堆方法结果: " + optResult4 + ", 有效: " + isValidReorganization(s4, optResult4));
        
        // 测试用例5：所有字符相同
        System.out.println("\n测试用例5：所有字符相同");
        String s5 = "aaaaa";
        String result5 = solution.reorganizeString(s5);
        String altResult5 = alternativeSolution.reorganizeString(s5);
        String optResult5 = optimizedSolution.reorganizeString(s5);
        
        System.out.println("原字符串: " + s5);
        System.out.println("堆方法结果: " + result5 + ", 有效: " + isValidReorganization(s5, result5));
        System.out.println("贪心方法结果: " + altResult5 + ", 有效: " + isValidReorganization(s5, altResult5));
        System.out.println("优化堆方法结果: " + optResult5 + ", 有效: " + isValidReorganization(s5, optResult5));
        
        // 测试用例6：所有字符都不同
        System.out.println("\n测试用例6：所有字符都不同");
        String s6 = "abcdef";
        String result6 = solution.reorganizeString(s6);
        String altResult6 = alternativeSolution.reorganizeString(s6);
        String optResult6 = optimizedSolution.reorganizeString(s6);
        
        System.out.println("原字符串: " + s6);
        System.out.println("堆方法结果: " + result6 + ", 有效: " + isValidReorganization(s6, result6));
        System.out.println("贪心方法结果: " + altResult6 + ", 有效: " + isValidReorganization(s6, altResult6));
        System.out.println("优化堆方法结果: " + optResult6 + ", 有效: " + isValidReorganization(s6, optResult6));
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        
        // 创建一个较大的可重构的字符串
        StringBuilder largeSBuilder = new StringBuilder();
        String smallPattern = "aabbccddeeffgghh"; // 16个字符
        for (int i = 0; i < 1000; i++) {
            largeSBuilder.append(smallPattern);
        }
        String largeS = largeSBuilder.toString(); // 总长度 16000
        
        // 测试堆方法性能
        long startTime = System.currentTimeMillis();
        String largeResult = solution.reorganizeString(largeS);
        long heapTime = System.currentTimeMillis() - startTime;
        System.out.println("堆方法处理大字符串用时: " + heapTime + "毫秒, 结果有效: " + isValidReorganization(largeS, largeResult));
        
        // 测试贪心方法性能
        startTime = System.currentTimeMillis();
        String largeAltResult = alternativeSolution.reorganizeString(largeS);
        long greedyTime = System.currentTimeMillis() - startTime;
        System.out.println("贪心方法处理大字符串用时: " + greedyTime + "毫秒, 结果有效: " + isValidReorganization(largeS, largeAltResult));
        
        // 测试优化堆方法性能
        startTime = System.currentTimeMillis();
        String largeOptResult = optimizedSolution.reorganizeString(largeS);
        long optHeapTime = System.currentTimeMillis() - startTime;
        System.out.println("优化堆方法处理大字符串用时: " + optHeapTime + "毫秒, 结果有效: " + isValidReorganization(largeS, largeOptResult));
        
        // 性能比较
        System.out.println("\n性能比较:");
        System.out.println("堆方法 vs 贪心方法: " + 
                          (greedyTime < heapTime ? "贪心方法更快" : "堆方法更快") + " 约 " + 
                          String.format("%.2f", (double)Math.max(heapTime, greedyTime) / Math.min(heapTime, greedyTime)) + "倍");
        System.out.println("堆方法 vs 优化堆方法: " + 
                          (optHeapTime < heapTime ? "优化堆方法更快" : "堆方法更快") + " 约 " + 
                          String.format("%.2f", (double)Math.max(heapTime, optHeapTime) / Math.min(heapTime, optHeapTime)) + "倍");
        System.out.println("贪心方法 vs 优化堆方法: " + 
                          (optHeapTime < greedyTime ? "优化堆方法更快" : "贪心方法更快") + " 约 " + 
                          String.format("%.2f", (double)Math.max(greedyTime, optHeapTime) / Math.min(greedyTime, optHeapTime)) + "倍");
    }
    
    // 主方法
    public static void main(String[] args) {
        testReorganizeString();
    }
    
    /*
     * 解题思路总结：
     * 1. 问题分析：
     *    - 要重新排列字符串，使得相邻字符不同
     *    - 关键条件：最高频率字符的出现次数不能超过(len(s)+1)//2
     *    - 如果最高频率字符次数超过这个阈值，无法重构
     * 
     * 2. 堆方法（优先队列）：
     *    - 统计每个字符的频率
     *    - 将字符及其频率放入最大堆（Java中使用PriorityQueue实现）
     *    - 每次从堆中取出频率最高的字符添加到结果中
     *    - 如果当前字符与结果最后一个字符相同，则取出下一个最高频率的字符
     *    - 将使用过的字符（如果还有剩余）重新放回堆中
     *    - 时间复杂度：O(n log k)，其中n是字符串长度，k是不同字符的数量
     *    - 空间复杂度：O(k)
     * 
     * 3. 贪心方法：
     *    - 先放置频率最高的字符，间隔放置
     *    - 然后放置剩余的字符
     *    - 时间复杂度：O(n)
     *    - 空间复杂度：O(k)
     * 
     * 4. 优化堆方法：
     *    - 每次从堆中取出两个不同的字符添加到结果中
     *    - 这样可以确保相邻字符不同
     *    - 最后如果还有一个字符（字符串长度为奇数），直接添加
     *    - 时间复杂度：O(n log k)
     *    - 空间复杂度：O(k)
     * 
     * 5. 边界情况处理：
     *    - 空字符串
     *    - 单字符字符串
     *    - 所有字符相同的字符串
     *    - 所有字符都不同的字符串
     *    - 最高频率字符刚好达到阈值的情况
     * 
     * 6. 堆方法的优势：
     *    - 自动维护元素的优先级
     *    - 适合需要频繁获取最高优先级元素的场景
     *    - 在这里用于贪心选择频率最高的字符进行放置
     * 
     * 7. 应用场景：
     *    - 字符重排问题
     *    - 任务调度问题（优先处理高优先级任务）
     *    - 资源分配问题（基于某种优先级分配资源）
     */
}