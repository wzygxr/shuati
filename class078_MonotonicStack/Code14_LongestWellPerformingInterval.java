package class053;

import java.util.*;

/**
 * 表现良好的最长时间段
 * 
 * 题目描述：
 * 给你一份工作时间表 hours，上面记录着某一位员工每天的工作小时数。
 * 我们认为当员工一天中的工作小时数大于 8 小时的时候，那么这一天就是「劳累的一天」。
 * 所谓「表现良好的时间段」，意味在这段时间内，「劳累的天数」是严格大于「不劳累的天数」。
 * 返回最长的「表现良好时间段」的长度。
 * 
 * 测试链接：https://leetcode.cn/problems/longest-well-performing-interval/
 * 
 * 解题思路：
 * 使用单调栈来解决这个问题。我们将问题转化为前缀和问题：
 * 1. 将工作小时数大于8的记为1，否则记为-1，这样问题就转化为找和大于0的最长子数组
 * 2. 计算前缀和数组
 * 3. 使用单调递减栈存储前缀和的索引，维护栈中前缀和的单调递减性
 * 4. 从右向左遍历前缀和数组，对于每个元素，如果它大于栈顶元素对应的前缀和，
 *    说明找到了一个和大于0的子数组，更新最大长度
 * 
 * 具体步骤：
 * 1. 将原数组转换为1/-1数组并计算前缀和
 * 2. 使用单调递减栈存储前缀和索引
 * 3. 从右向左遍历前缀和数组，计算最长表现良好时间段
 * 
 * 时间复杂度分析：
 * O(n) - 需要遍历数组两次，n为数组长度
 * 
 * 空间复杂度分析：
 * O(n) - 需要额外的数组存储前缀和，栈的空间最多为n
 * 
 * 是否为最优解：
 * 是，这是解决该问题的最优解之一
 */
public class Code14_LongestWellPerformingInterval {
    
    public static int longestWPI(int[] hours) {
        int n = hours.length;
        // 计算前缀和数组
        int[] prefixSum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            // 大于8小时记为1，否则记为-1
            prefixSum[i + 1] = prefixSum[i] + (hours[i] > 8 ? 1 : -1);
        }
        
        // 使用单调递减栈存储前缀和索引
        Stack<Integer> stack = new Stack<>();
        // 初始化栈，存储前缀和递减的索引
        for (int i = 0; i <= n; i++) {
            if (stack.isEmpty() || prefixSum[stack.peek()] > prefixSum[i]) {
                stack.push(i);
            }
        }
        
        int maxLength = 0;
        // 从右向左遍历前缀和数组
        for (int i = n; i >= 0; i--) {
            // 当栈不为空且栈顶元素对应的前缀和小于当前前缀和时
            while (!stack.isEmpty() && prefixSum[stack.peek()] < prefixSum[i]) {
                // 更新最大长度
                maxLength = Math.max(maxLength, i - stack.pop());
            }
        }
        
        return maxLength;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int[] hours1 = {9, 9, 6, 0, 6, 6, 9};
        System.out.println("测试用例1: " + Arrays.toString(hours1));
        System.out.println("输出: " + longestWPI(hours1)); // 期望输出: 3
        
        // 测试用例2
        int[] hours2 = {6, 6, 6};
        System.out.println("测试用例2: " + Arrays.toString(hours2));
        System.out.println("输出: " + longestWPI(hours2)); // 期望输出: 0
        
        // 测试用例3
        int[] hours3 = {6, 9, 9};
        System.out.println("测试用例3: " + Arrays.toString(hours3));
        System.out.println("输出: " + longestWPI(hours3)); // 期望输出: 3
    }
}