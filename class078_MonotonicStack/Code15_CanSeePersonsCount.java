package class053;

import java.util.*;

/**
 * 队列中可以看到的人数
 * 
 * 题目描述：
 * 有 n 个人排成一个队列，从左到右编号为 0 到 n - 1 。给你以一个整数数组 heights ，
 * 每个整数互不相同，heights[i] 表示第 i 个人的高度。
 * 一个人能看见他右边另一个人的条件是这两人之间的所有人都比他们两人矮。
 * 更正式的，第 i 个人能看到第 j 个人的条件是 i < j 且 
 * min(heights[i], heights[j]) > max(heights[i+1], heights[i+2], ..., heights[j-1]) 。
 * 请你返回一个长度为 n 的数组 answer ，其中 answer[i] 是第 i 个人在他右侧队列中能看到的人数。
 * 
 * 测试链接：https://leetcode.cn/problems/number-of-visible-people-in-a-queue/
 * 
 * 解题思路：
 * 使用单调栈来解决这个问题。我们从右向左遍历数组，维护一个单调递减栈：
 * 1. 对于当前元素，栈中所有比它小的元素都能被看到，直到遇到一个比它大的元素
 * 2. 如果栈中还有元素（比当前元素大的元素），那么这个元素也能被看到
 * 3. 将当前元素入栈
 * 
 * 具体步骤：
 * 1. 从右向左遍历数组
 * 2. 对于每个元素，弹出栈中所有比它小的元素并计数
 * 3. 如果栈不为空，说明还有一个比当前元素大的元素能看到，计数加1
 * 4. 将当前元素入栈
 * 5. 记录计数结果
 * 
 * 时间复杂度分析：
 * O(n) - 每个元素最多入栈和出栈各一次，n为数组长度
 * 
 * 空间复杂度分析：
 * O(n) - 栈的空间最多为n
 * 
 * 是否为最优解：
 * 是，这是解决该问题的最优解之一
 */
public class Code15_CanSeePersonsCount {
    
    public static int[] canSeePersonsCount(int[] heights) {
        int n = heights.length;
        int[] result = new int[n];
        
        // 使用单调递减栈存储高度
        Stack<Integer> stack = new Stack<>();
        
        // 从右向左遍历数组
        for (int i = n - 1; i >= 0; i--) {
            int count = 0;
            
            // 弹出栈中所有比当前元素小的元素并计数
            while (!stack.isEmpty() && stack.peek() < heights[i]) {
                stack.pop();
                count++;
            }
            
            // 如果栈不为空，说明还有一个比当前元素大的元素能看到
            if (!stack.isEmpty()) {
                count++;
            }
            
            result[i] = count;
            // 将当前元素入栈
            stack.push(heights[i]);
        }
        
        return result;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int[] heights1 = {10, 6, 8, 5, 11, 9};
        System.out.println("测试用例1: " + Arrays.toString(heights1));
        System.out.println("输出: " + Arrays.toString(canSeePersonsCount(heights1))); // 期望输出: [3, 1, 2, 1, 1, 0]
        
        // 测试用例2
        int[] heights2 = {5, 1, 2, 3, 10};
        System.out.println("测试用例2: " + Arrays.toString(heights2));
        System.out.println("输出: " + Arrays.toString(canSeePersonsCount(heights2))); // 期望输出: [4, 1, 1, 1, 0]
        
        // 测试用例3
        int[] heights3 = {1, 2, 3, 4, 5};
        System.out.println("测试用例3: " + Arrays.toString(heights3));
        System.out.println("输出: " + Arrays.toString(canSeePersonsCount(heights3))); // 期望输出: [1, 1, 1, 1, 0]
    }
}