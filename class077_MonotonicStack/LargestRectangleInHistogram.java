package class052.problems;

/**
 * Largest Rectangle in Histogram (柱状图中最大的矩形)
 * 
 * 题目描述:
 * 给定 n 个非负整数，用来表示柱状图中各个柱子的高度，每个柱子彼此相邻，且宽度为 1 。
 * 求在该柱状图中，能够勾勒出来的矩形的最大面积。
 * 
 * 解题思路:
 * 使用单调栈来解决。维护一个单调递增栈，栈中存储柱子的索引。
 * 当遇到一个比栈顶元素高度小的柱子时，说明以栈顶元素为高度的矩形右边界确定了。
 * 此时可以计算以栈顶元素为高度的矩形面积。
 * 
 * 时间复杂度: O(n)，每个元素最多入栈和出栈各一次
 * 空间复杂度: O(n)，栈的空间
 * 
 * 测试链接: https://leetcode.cn/problems/largest-rectangle-in-histogram/
 */
public class LargestRectangleInHistogram {
    
    public static int largestRectangleArea(int[] heights) {
        int n = heights.length;
        // 使用数组模拟栈，提高效率
        int[] stack = new int[n + 1];
        int top = -1; // 栈顶指针
        int maxArea = 0;
        
        // 遍历每个柱子
        for (int i = 0; i < n; i++) {
            // 当栈不为空且当前高度小于栈顶索引对应的高度时
            while (top >= 0 && heights[stack[top]] > heights[i]) {
                int height = heights[stack[top--]]; // 弹出栈顶元素作为高度
                // 计算宽度：如果栈为空，宽度为i；否则宽度为i - stack[top] - 1
                int width = top < 0 ? i : i - stack[top] - 1;
                maxArea = Math.max(maxArea, height * width);
            }
            stack[++top] = i; // 将当前索引压入栈
        }
        
        // 处理栈中剩余元素
        while (top >= 0) {
            int height = heights[stack[top--]]; // 弹出栈顶元素作为高度
            // 计算宽度：如果栈为空，宽度为n；否则宽度为n - stack[top] - 1
            int width = top < 0 ? n : n - stack[top] - 1;
            maxArea = Math.max(maxArea, height * width);
        }
        
        return maxArea;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] heights1 = {2, 1, 5, 6, 2, 3};
        int result1 = largestRectangleArea(heights1);
        // 预期输出: 10
        System.out.println("测试用例1输出: " + result1);
        
        // 测试用例2
        int[] heights2 = {2, 4};
        int result2 = largestRectangleArea(heights2);
        // 预期输出: 4
        System.out.println("测试用例2输出: " + result2);
        
        // 测试用例3
        int[] heights3 = {1, 2, 3, 4, 5};
        int result3 = largestRectangleArea(heights3);
        // 预期输出: 9
        System.out.println("测试用例3输出: " + result3);
    }
}