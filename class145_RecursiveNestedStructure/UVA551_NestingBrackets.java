package class039;

// UVA 551 Nesting a Bunch of Brackets (多种类型括号匹配)
// 测试链接 : https://onlinejudge.org/external/5/551.pdf

import java.util.Stack;

public class UVA551_NestingBrackets {
    public String checkBrackets(String s) {
        Stack<Character> stack = new Stack<>();
        Stack<Integer> positions = new Stack<>();
        
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            
            if (c == '(' || c == '[' || c == '{' || c == '<') {
                stack.push(c);
                positions.push(i + 1); // 位置从1开始计数
            } else if (c == ')' || c == ']' || c == '}' || c == '>') {
                if (stack.isEmpty()) {
                    return "NO " + (i + 1); // 不匹配的位置
                }
                
                char top = stack.pop();
                positions.pop();
                
                // 检查括号类型是否匹配
                if (!isMatchingPair(top, c)) {
                    return "NO " + (i + 1); // 不匹配的位置
                }
            }
        }
        
        if (!stack.isEmpty()) {
            return "NO " + positions.peek(); // 未匹配的括号位置
        }
        
        return "YES";
    }
    
    private boolean isMatchingPair(char open, char close) {
        return (open == '(' && close == ')') ||
               (open == '[' && close == ']') ||
               (open == '{' && close == '}') ||
               (open == '<' && close == '>');
    }
    
    // 测试用例
    public static void main(String[] args) {
        UVA551_NestingBrackets solution = new UVA551_NestingBrackets();
        
        // 测试用例1
        String s1 = "([]){}";
        System.out.println("输入: " + s1);
        System.out.println("输出: " + solution.checkBrackets(s1));
        System.out.println("期望: YES\n");
        
        // 测试用例2
        String s2 = "([)]";
        System.out.println("输入: " + s2);
        System.out.println("输出: " + solution.checkBrackets(s2));
        System.out.println("期望: NO 3\n");
    }
}