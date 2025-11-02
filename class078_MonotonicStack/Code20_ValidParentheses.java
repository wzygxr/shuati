package class053;

import java.util.*;

/**
 * 有效的括号
 * 
 * 题目描述：
 * 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串 s ，判断字符串是否有效。
 * 有效字符串需满足：
 * 1. 左括号必须用相同类型的右括号闭合。
 * 2. 左括号必须以正确的顺序闭合。
 * 
 * 测试链接：https://leetcode.cn/problems/valid-parentheses/
 * 题目来源：LeetCode
 * 难度：简单
 * 
 * 核心算法：栈
 * 
 * 解题思路：
 * 1. 使用栈来存储遇到的左括号
 * 2. 遍历字符串中的每个字符：
 *    - 如果是左括号，压入栈中
 *    - 如果是右括号，检查栈顶是否匹配
 *    - 如果不匹配或栈为空，返回false
 * 3. 遍历结束后，检查栈是否为空
 * 
 * 时间复杂度分析：
 * O(n) - 需要遍历字符串一次，n为字符串长度
 * 
 * 空间复杂度分析：
 * O(n) - 栈的空间最多为n
 * 
 * 是否为最优解：
 * 是，这是解决该问题的最优解之一
 * 
 * 工程化考量：
 * 1. 健壮性：处理空字符串、单字符字符串等边界情况
 * 2. 性能优化：使用HashMap存储括号映射关系
 * 3. 可读性：使用清晰的变量名和注释说明算法步骤
 * 
 * 算法调试技巧：
 * 1. 打印中间过程：在循环中打印栈的状态
 * 2. 边界测试：测试各种边界情况如空字符串、单括号等
 * 3. 性能测试：测试大规模字符串下的性能表现
 */
public class Code20_ValidParentheses {
    
    /**
     * 主解法：使用栈判断括号有效性
     * @param s 输入字符串
     * @return 如果括号有效返回true，否则返回false
     */
    public static boolean isValid(String s) {
        // 边界条件检查
        if (s == null || s.length() % 2 != 0) {
            return false;
        }
        
        // 使用栈存储左括号
        Stack<Character> stack = new Stack<>();
        
        // 使用HashMap存储括号映射关系
        Map<Character, Character> bracketMap = new HashMap<>();
        bracketMap.put(')', '(');
        bracketMap.put('}', '{');
        bracketMap.put(']', '[');
        
        // 遍历字符串中的每个字符
        for (char c : s.toCharArray()) {
            if (bracketMap.containsValue(c)) {
                // 如果是左括号，压入栈中
                stack.push(c);
            } else if (bracketMap.containsKey(c)) {
                // 如果是右括号，检查栈顶是否匹配
                if (stack.isEmpty() || stack.pop() != bracketMap.get(c)) {
                    return false;
                }
            } else {
                // 非法字符
                return false;
            }
        }
        
        // 检查栈是否为空
        return stack.isEmpty();
    }
    
    /**
     * 优化解法：使用数组模拟栈（性能优化）
     */
    public static boolean isValidOptimized(String s) {
        // 边界条件检查
        if (s == null || s.length() % 2 != 0) {
            return false;
        }
        
        int n = s.length();
        // 使用字符数组模拟栈
        char[] stack = new char[n];
        int top = -1;  // 栈顶指针
        
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            
            if (c == '(' || c == '{' || c == '[') {
                // 左括号入栈
                stack[++top] = c;
            } else {
                // 右括号，检查栈是否为空
                if (top == -1) {
                    return false;
                }
                
                // 检查括号是否匹配
                char topChar = stack[top--];
                if ((c == ')' && topChar != '(') ||
                    (c == '}' && topChar != '{') ||
                    (c == ']' && topChar != '[')) {
                    return false;
                }
            }
        }
        
        // 检查栈是否为空
        return top == -1;
    }
    
    /**
     * 扩展解法：支持更多括号类型
     */
    public static boolean isValidExtended(String s) {
        if (s == null || s.length() % 2 != 0) {
            return false;
        }
        
        Stack<Character> stack = new Stack<>();
        
        // 扩展的括号映射（支持更多括号类型）
        Map<Character, Character> extendedMap = new HashMap<>();
        extendedMap.put(')', '(');
        extendedMap.put('}', '{');
        extendedMap.put(']', '[');
        extendedMap.put('>', '<');  // 支持尖括号
        extendedMap.put('»', '«');  // 支持双角括号
        
        for (char c : s.toCharArray()) {
            if (extendedMap.containsValue(c)) {
                stack.push(c);
            } else if (extendedMap.containsKey(c)) {
                if (stack.isEmpty() || stack.pop() != extendedMap.get(c)) {
                    return false;
                }
            } else {
                // 忽略非括号字符（扩展功能）
                continue;
            }
        }
        
        return stack.isEmpty();
    }
    
    /**
     * 单元测试方法
     */
    public static void testIsValid() {
        System.out.println("=== 有效的括号单元测试 ===");
        
        // 测试用例1：有效括号
        String s1 = "()";
        boolean result1 = isValid(s1);
        System.out.println("测试用例1: " + s1);
        System.out.println("输出: " + result1);
        System.out.println("期望: true");
        
        // 测试用例2：有效嵌套括号
        String s2 = "()[]{}";
        boolean result2 = isValid(s2);
        System.out.println("\n测试用例2: " + s2);
        System.out.println("输出: " + result2);
        System.out.println("期望: true");
        
        // 测试用例3：复杂有效括号
        String s3 = "([{}])";
        boolean result3 = isValid(s3);
        System.out.println("\n测试用例3: " + s3);
        System.out.println("输出: " + result3);
        System.out.println("期望: true");
        
        // 测试用例4：无效括号（不匹配）
        String s4 = "(]";
        boolean result4 = isValid(s4);
        System.out.println("\n测试用例4: " + s4);
        System.out.println("输出: " + result4);
        System.out.println("期望: false");
        
        // 测试用例5：无效括号（顺序错误）
        String s5 = "([)]";
        boolean result5 = isValid(s5);
        System.out.println("\n测试用例5: " + s5);
        System.out.println("输出: " + result5);
        System.out.println("期望: false");
        
        // 测试用例6：边界情况 - 空字符串
        String s6 = "";
        boolean result6 = isValid(s6);
        System.out.println("\n测试用例6: 空字符串");
        System.out.println("输出: " + result6);
        System.out.println("期望: true");
        
        // 测试用例7：边界情况 - 奇数长度
        String s7 = "(()";
        boolean result7 = isValid(s7);
        System.out.println("\n测试用例7: " + s7);
        System.out.println("输出: " + result7);
        System.out.println("期望: false");
        
        // 测试用例8：非法字符
        String s8 = "(a)";
        boolean result8 = isValid(s8);
        System.out.println("\n测试用例8: " + s8);
        System.out.println("输出: " + result8);
        System.out.println("期望: false");
    }
    
    /**
     * 性能对比测试：HashMap法 vs 数组模拟栈法
     */
    public static void performanceComparison() {
        System.out.println("\n=== 性能对比测试 ===");
        
        // 生成测试数据（大规模有效括号字符串）
        int n = 100000;
        StringBuilder testData = new StringBuilder();
        Random random = new Random();
        
        // 生成有效括号字符串
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < n; i++) {
            int type = random.nextInt(3);
            char left, right;
            
            switch (type) {
                case 0: left = '('; right = ')'; break;
                case 1: left = '['; right = ']'; break;
                default: left = '{'; right = '}'; break;
            }
            
            // 50%概率添加左括号，50%概率添加右括号（但保持有效性）
            if (random.nextBoolean() || stack.isEmpty()) {
                testData.append(left);
                stack.push(right);
            } else {
                testData.append(stack.pop());
            }
        }
        
        // 添加剩余的右括号
        while (!stack.isEmpty()) {
            testData.append(stack.pop());
        }
        
        String testString = testData.toString();
        
        // 测试HashMap法
        long startTime1 = System.currentTimeMillis();
        boolean result1 = isValid(testString);
        long endTime1 = System.currentTimeMillis();
        
        // 测试数组模拟栈法
        long startTime2 = System.currentTimeMillis();
        boolean result2 = isValidOptimized(testString);
        long endTime2 = System.currentTimeMillis();
        
        System.out.println("数据规模: " + testString.length() + "个字符");
        System.out.println("HashMap法执行时间: " + (endTime1 - startTime1) + "ms, 结果: " + result1);
        System.out.println("数组模拟栈法执行时间: " + (endTime2 - startTime2) + "ms, 结果: " + result2);
        System.out.println("结果一致性: " + (result1 == result2));
    }
    
    /**
     * 正确性验证：验证两种解法结果是否一致
     */
    public static void correctnessVerification() {
        System.out.println("\n=== 正确性验证 ===");
        
        String[] testCases = {
            "()",
            "()[]{}",
            "([{}])",
            "(]",
            "([)]",
            "",
            "(()",
            "(a)",
            "{[()]}",
            "{{{{}}}}",
            "[[[]]]]",
            "({[}])",
            "((()))",
            "([{}])"
        };
        
        boolean allPassed = true;
        for (int i = 0; i < testCases.length; i++) {
            String s = testCases[i];
            boolean result1 = isValid(s);
            boolean result2 = isValidOptimized(s);
            
            if (result1 != result2) {
                System.out.println("测试用例 " + i + " 不一致:");
                System.out.println("输入: " + s);
                System.out.println("解法1结果: " + result1);
                System.out.println("解法2结果: " + result2);
                allPassed = false;
            }
        }
        
        if (allPassed) {
            System.out.println("所有测试用例结果一致！");
        }
    }
    
    /**
     * 扩展功能测试：支持更多括号类型
     */
    public static void testExtendedFunctionality() {
        System.out.println("\n=== 扩展功能测试 ===");
        
        // 测试尖括号
        String s1 = "<>";
        boolean result1 = isValidExtended(s1);
        System.out.println("测试用例1 (尖括号): " + s1 + " -> " + result1 + " (期望: true)");
        
        // 测试双角括号
        String s2 = "«»";
        boolean result2 = isValidExtended(s2);
        System.out.println("测试用例2 (双角括号): " + s2 + " -> " + result2 + " (期望: true)");
        
        // 测试混合括号（包含非括号字符）
        String s3 = "(hello{world})";
        boolean result3 = isValidExtended(s3);
        System.out.println("测试用例3 (混合字符): " + s3 + " -> " + result3 + " (期望: true)");
        
        // 测试无效扩展括号
        String s4 = "<]";
        boolean result4 = isValidExtended(s4);
        System.out.println("测试用例4 (无效扩展): " + s4 + " -> " + result4 + " (期望: false)");
    }
    
    public static void main(String[] args) {
        // 运行单元测试
        testIsValid();
        
        // 运行性能对比测试
        performanceComparison();
        
        // 运行正确性验证
        correctnessVerification();
        
        // 运行扩展功能测试
        testExtendedFunctionality();
        
        System.out.println("\n=== 有效的括号算法验证完成 ===");
    }
}