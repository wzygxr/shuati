package class039;

// LeetCode 856. Score of Parentheses (括号的分数)
// 测试链接 : https://leetcode.cn/problems/score-of-parentheses/

import java.util.Stack;

public class LC856_ScoreOfParentheses {
    public int scoreOfParentheses(String s) {
        Stack<Integer> stack = new Stack<>();
        stack.push(0); // 初始化栈底为0
        
        for (char c : s.toCharArray()) {
            if (c == '(') {
                stack.push(0); // 遇到左括号，压入0
            } else {
                int v = stack.pop(); // 弹出当前值
                int w = stack.pop(); // 弹出前一个值
                // 计算当前括号对的分数并加到前一个值上
                stack.push(w + Math.max(2 * v, 1));
            }
        }
        
        return stack.pop(); // 返回最终结果
    }
    
    // 测试用例
    public static void main(String[] args) {
        LC856_ScoreOfParentheses solution = new LC856_ScoreOfParentheses();
        
        // 测试用例1
        String s1 = "()";
        System.out.println("输入: " + s1);
        System.out.println("输出: " + solution.scoreOfParentheses(s1));
        System.out.println("期望: 1\n");
        
        // 测试用例2
        String s2 = "(())";
        System.out.println("输入: " + s2);
        System.out.println("输出: " + solution.scoreOfParentheses(s2));
        System.out.println("期望: 2\n");
        
        // 测试用例3
        String s3 = "()()";
        System.out.println("输入: " + s3);
        System.out.println("输出: " + solution.scoreOfParentheses(s3));
        System.out.println("期望: 2\n");
        
        // 测试用例4
        String s4 = "(()(()))";
        System.out.println("输入: " + s4);
        System.out.println("输出: " + solution.scoreOfParentheses(s4));
        System.out.println("期望: 6\n");
    }
}