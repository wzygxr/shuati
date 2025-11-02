package class052.problems;

/**
 * Daily Temperatures (每日温度)
 * 
 * 题目描述:
 * 给定一个整数数组 temperatures ，表示每天的温度，返回一个数组 answer，
 * 其中 answer[i] 是指对于第 i 天，下一个更高温度出现在几天后。
 * 如果气温在这之后都不会升高，请在该位置用 0 来代替。
 * 
 * 解题思路:
 * 使用单调栈来解决。维护一个单调递减栈，栈中存储索引。
 * 当遇到一个比栈顶元素温度高的温度时，说明找到了栈顶元素的下一个更高温度。
 * 
 * 时间复杂度: O(n)，每个元素最多入栈和出栈各一次
 * 空间复杂度: O(n)，栈的空间
 * 
 * 测试链接: https://leetcode.cn/problems/daily-temperatures/
 */
public class DailyTemperatures {
    
    public static int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] answer = new int[n];
        // 使用数组模拟栈，提高效率
        int[] stack = new int[n];
        int top = -1; // 栈顶指针
        
        for (int i = 0; i < n; i++) {
            // 当栈不为空且当前温度大于栈顶索引对应的温度时
            while (top >= 0 && temperatures[stack[top]] < temperatures[i]) {
                int index = stack[top--]; // 弹出栈顶元素
                answer[index] = i - index; // 计算天数差
            }
            stack[++top] = i; // 将当前索引压入栈
        }
        
        // 栈中剩余元素对应的答案都为0（默认值）
        return answer;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] temperatures1 = {73, 74, 75, 71, 69, 72, 76, 73};
        int[] result1 = dailyTemperatures(temperatures1);
        // 预期输出: [1, 1, 4, 2, 1, 1, 0, 0]
        System.out.print("测试用例1输出: ");
        for (int val : result1) {
            System.out.print(val + " ");
        }
        System.out.println();
        
        // 测试用例2
        int[] temperatures2 = {30, 40, 50, 60};
        int[] result2 = dailyTemperatures(temperatures2);
        // 预期输出: [1, 1, 1, 0]
        System.out.print("测试用例2输出: ");
        for (int val : result2) {
            System.out.print(val + " ");
        }
        System.out.println();
        
        // 测试用例3
        int[] temperatures3 = {30, 60, 90};
        int[] result3 = dailyTemperatures(temperatures3);
        // 预期输出: [1, 1, 0]
        System.out.print("测试用例3输出: ");
        for (int val : result3) {
            System.out.print(val + " ");
        }
        System.out.println();
    }
}