package class039;

// LeetCode 20. Valid Parentheses (有效的括号)
// 测试链接 : https://leetcode.cn/problems/valid-parentheses/

import java.util.Stack;

public class LC20_ValidParentheses {
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c); // 遇到左括号入栈
            } else {
                if (stack.isEmpty()) return false; // 栈为空但遇到右括号
                
                char top = stack.pop(); // 弹出栈顶元素
                // 检查括号是否匹配
                if ((c == ')' && top != '(') ||
                    (c == ']' && top != '[') ||
                    (c == '}' && top != '{')) {
                    return false;
                }
            }
        }
        
        return stack.isEmpty(); // 栈为空表示所有括号都匹配
    }
    
    // 测试用例
    public static void main(String[] args) {
        LC20_ValidParentheses solution = new LC20_ValidParentheses();
        
        // 测试用例1
        String s1 = "()";
        System.out.println("输入: " + s1);
        System.out.println("输出: " + solution.isValid(s1));
        System.out.println("期望: true\n");
        
        // 测试用例2
        String s2 = "()[]{}";
        System.out.println("输入: " + s2);
        System.out.println("输出: " + solution.isValid(s2));
        System.out.println("期望: true\n");
        
        // 测试用例3
        String s3 = "(]";
        System.out.println("输入: " + s3);
        System.out.println("输出: " + solution.isValid(s3));
        System.out.println("期望: false\n");
    }
}