package class039;

// LeetCode 32. Longest Valid Parentheses (最长有效括号)
// 测试链接 : https://leetcode.cn/problems/longest-valid-parentheses/

import java.util.Stack;

public class LC32_LongestValidParentheses {
    public int longestValidParentheses(String s) {
        Stack<Integer> stack = new Stack<>();
        stack.push(-1); // 初始化栈底为-1
        int maxLen = 0;
        
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                stack.push(i); // 遇到左括号，压入索引
            } else {
                stack.pop(); // 遇到右括号，弹出栈顶
                if (stack.isEmpty()) {
                    // 栈为空，压入当前索引作为新的基准
                    stack.push(i);
                } else {
                    // 计算当前有效括号长度
                    maxLen = Math.max(maxLen, i - stack.peek());
                }
            }
        }
        
        return maxLen;
    }
    
    // 测试用例
    public static void main(String[] args) {
        LC32_LongestValidParentheses solution = new LC32_LongestValidParentheses();
        
        // 测试用例1
        String s1 = "(()";
        System.out.println("输入: " + s1);
        System.out.println("输出: " + solution.longestValidParentheses(s1));
        System.out.println("期望: 2\n");
        
        // 测试用例2
        String s2 = ")()())";
        System.out.println("输入: " + s2);
        System.out.println("输出: " + solution.longestValidParentheses(s2));
        System.out.println("期望: 4\n");
        
        // 测试用例3
        String s3 = "";
        System.out.println("输入: " + s3);
        System.out.println("输出: " + solution.longestValidParentheses(s3));
        System.out.println("期望: 0\n");
    }
}