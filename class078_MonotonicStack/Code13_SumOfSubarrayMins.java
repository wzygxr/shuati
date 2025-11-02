package class053;

import java.util.*;

/**
 * 子数组的最小值之和
 * 
 * 题目描述：
 * 给定一个整数数组 arr，找到 min(b) 的总和，其中 b 的范围为 arr 的每个（连续）子数组。
 * 由于答案可能很大，因此返回答案模 10^9 + 7 。
 * 
 * 测试链接：https://leetcode.cn/problems/sum-of-subarray-minimums/
 * 
 * 解题思路：
 * 使用单调栈来解决这个问题。对于每个元素，我们需要找到它作为最小值的子数组数量。
 * 1. 对于每个元素，找到它左边第一个比它小的元素位置left[i]
 * 2. 对于每个元素，找到它右边第一个比它小或等于的元素位置right[i]
 * 3. 以该元素为最小值的子数组数量为：(i - left[i]) * (right[i] - i)
 * 4. 累加所有元素的贡献：sum += arr[i] * (i - left[i]) * (right[i] - i)
 * 
 * 具体步骤：
 * 1. 使用单调递增栈找到每个元素左边第一个更小元素的位置
 * 2. 使用单调递增栈找到每个元素右边第一个更小或等于元素的位置
 * 3. 计算每个元素作为最小值的子数组数量并累加贡献
 * 
 * 时间复杂度分析：
 * O(n) - 每个元素最多入栈和出栈各一次，n为数组长度
 * 
 * 空间复杂度分析：
 * O(n) - 栈的空间最多为n，还需要额外的数组存储左右边界位置
 * 
 * 是否为最优解：
 * 是，这是解决该问题的最优解之一
 */
public class Code13_SumOfSubarrayMins {
    
    private static final int MOD = 1000000007;
    
    public static int sumSubarrayMins(int[] arr) {
        int n = arr.length;
        // left[i] 表示左边第一个比 arr[i] 小的元素位置，不存在则为 -1
        int[] left = new int[n];
        // right[i] 表示右边第一个比 arr[i] 小或等于的元素位置，不存在则为 n
        int[] right = new int[n];
        
        // 使用单调递增栈找到左边第一个更小元素的位置
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < n; i++) {
            // 当栈不为空且栈顶元素大于等于当前元素时，弹出栈顶元素
            while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) {
                stack.pop();
            }
            // 左边第一个更小元素的位置
            left[i] = stack.isEmpty() ? -1 : stack.peek();
            // 将当前元素索引入栈
            stack.push(i);
        }
        
        // 使用单调递增栈找到右边第一个更小或等于元素的位置
        stack.clear();
        for (int i = n - 1; i >= 0; i--) {
            // 当栈不为空且栈顶元素大于当前元素时，弹出栈顶元素
            while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) {
                stack.pop();
            }
            // 右边第一个更小或等于元素的位置
            right[i] = stack.isEmpty() ? n : stack.peek();
            // 将当前元素索引入栈
            stack.push(i);
        }
        
        // 计算结果
        long result = 0;
        for (int i = 0; i < n; i++) {
            // 以 arr[i] 为最小值的子数组数量
            long count = (long)(i - left[i]) * (right[i] - i) % MOD;
            // 累加贡献
            result = (result + (long)arr[i] * count) % MOD;
        }
        
        return (int)result;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int[] arr1 = {3, 1, 2, 4};
        System.out.println("测试用例1: " + Arrays.toString(arr1));
        System.out.println("输出: " + sumSubarrayMins(arr1)); // 期望输出: 17
        
        // 测试用例2
        int[] arr2 = {11, 81, 94, 43, 3};
        System.out.println("测试用例2: " + Arrays.toString(arr2));
        System.out.println("输出: " + sumSubarrayMins(arr2)); // 期望输出: 444
    }
}