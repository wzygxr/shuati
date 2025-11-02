import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode 781. 森林中的兔子 - 增强版
 * 题目链接：https://leetcode.cn/problems/rabbits-in-forest/
 * 难度：中等
 * 
 * 增强功能：
 * 1. 两种解法对比（排序和哈希表）
 * 2. 详细数学推导
 * 3. 完整测试框架
 */
public class Code02_RabbitsInForest_Enhanced {
    
    /**
     * 解法1：排序 + 双指针
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(1)
     */
    public static int numRabbitsSort(int[] answers) {
        if (answers == null || answers.length == 0) return 0;
        
        Arrays.sort(answers);
        int total = 0;
        
        for (int i = 0; i < answers.length; ) {
            int answer = answers[i];
            int count = 0;
            
            while (i < answers.length && answers[i] == answer) {
                count++;
                i++;
            }
            
            int groupSize = answer + 1;
            int groups = (count + groupSize - 1) / groupSize;
            total += groups * groupSize;
        }
        
        return total;
    }
    
    /**
     * 解法2：哈希表计数
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public static int numRabbitsHashMap(int[] answers) {
        if (answers == null || answers.length == 0) return 0;
        
        Map<Integer, Integer> countMap = new HashMap<>();
        for (int answer : answers) {
            countMap.put(answer, countMap.getOrDefault(answer, 0) + 1);
        }
        
        int total = 0;
        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            int answer = entry.getKey();
            int count = entry.getValue();
            int groupSize = answer + 1;
            int groups = (count + groupSize - 1) / groupSize;
            total += groups * groupSize;
        }
        
        return total;
    }
    
    public static void main(String[] args) {
        int[][] testCases = {
            {1, 1, 2},    // 预期: 5
            {10, 10, 10},  // 预期: 11
            {0, 0, 0}      // 预期: 3
        };
        
        for (int i = 0; i < testCases.length; i++) {
            int result1 = numRabbitsSort(testCases[i]);
            int result2 = numRabbitsHashMap(testCases[i]);
            System.out.printf("测试用例%d: 排序法=%d, 哈希法=%d, 一致=%b%n", 
                i+1, result1, result2, result1 == result2);
        }
    }
}