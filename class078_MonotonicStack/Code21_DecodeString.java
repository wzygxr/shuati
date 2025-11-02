package class053;

import java.util.*;

/**
 * 字符串解码
 * 
 * 题目描述：
 * 给定一个经过编码的字符串，返回它解码后的字符串。
 * 编码规则为: k[encoded_string]，表示其中方括号内部的 encoded_string 正好重复 k 次。注意 k 保证为正整数。
 * 你可以认为输入字符串总是有效的；输入字符串中没有额外的空格，且输入的方括号总是符合格式要求的。
 * 
 * 测试链接：https://leetcode.cn/problems/decode-string/
 * 题目来源：LeetCode
 * 难度：中等
 * 
 * 核心算法：双栈法（数字栈和字符串栈）
 * 
 * 解题思路：
 * 1. 使用两个栈：数字栈存储重复次数，字符串栈存储当前解码的字符串
 * 2. 遍历字符串中的每个字符：
 *    - 如果是数字，解析完整的数字
 *    - 如果是'['，将当前数字和字符串分别入栈，重置临时变量
 *    - 如果是']'，弹出数字栈和字符串栈，重复当前字符串并拼接到前一个字符串
 *    - 如果是字母，直接添加到当前字符串
 * 
 * 时间复杂度分析：
 * O(n) - 需要遍历字符串一次，n为字符串长度
 * 
 * 空间复杂度分析：
 * O(n) - 栈的空间最多为n
 * 
 * 是否为最优解：
 * 是，这是解决该问题的最优解之一
 */
public class Code21_DecodeString {
    
    /**
     * 双栈解法
     */
    public static String decodeString(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        
        // 数字栈：存储重复次数
        Stack<Integer> countStack = new Stack<>();
        // 字符串栈：存储当前解码的字符串
        Stack<StringBuilder> stringStack = new Stack<>();
        
        StringBuilder currentString = new StringBuilder();
        int currentNumber = 0;
        
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                // 解析数字
                currentNumber = currentNumber * 10 + (c - '0');
            } else if (c == '[') {
                // 遇到'['，将当前数字和字符串入栈
                countStack.push(currentNumber);
                stringStack.push(currentString);
                
                // 重置临时变量
                currentNumber = 0;
                currentString = new StringBuilder();
            } else if (c == ']') {
                // 遇到']'，弹出栈顶元素进行重复
                int repeatCount = countStack.pop();
                StringBuilder previousString = stringStack.pop();
                
                // 重复当前字符串
                for (int i = 0; i < repeatCount; i++) {
                    previousString.append(currentString);
                }
                
                currentString = previousString;
            } else {
                // 普通字母，添加到当前字符串
                currentString.append(c);
            }
        }
        
        return currentString.toString();
    }
    
    /**
     * 递归解法
     */
    private int index = 0;
    
    public String decodeStringRecursive(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        
        StringBuilder result = new StringBuilder();
        int num = 0;
        
        while (index < s.length()) {
            char c = s.charAt(index);
            index++;
            
            if (Character.isDigit(c)) {
                num = num * 10 + (c - '0');
            } else if (c == '[') {
                // 递归解码子字符串
                String sub = decodeStringRecursive(s);
                for (int i = 0; i < num; i++) {
                    result.append(sub);
                }
                num = 0;
            } else if (c == ']') {
                // 返回当前层的结果
                break;
            } else {
                result.append(c);
            }
        }
        
        return result.toString();
    }
    
    /**
     * 单元测试方法
     */
    public static void testDecodeString() {
        System.out.println("=== 字符串解码单元测试 ===");
        
        // 测试用例1：简单重复
        String s1 = "3[a]2[bc]";
        String result1 = decodeString(s1);
        System.out.println("测试用例1: " + s1);
        System.out.println("输出: " + result1);
        System.out.println("期望: aaabcbc");
        
        // 测试用例2：嵌套重复
        String s2 = "3[a2[c]]";
        String result2 = decodeString(s2);
        System.out.println("\n测试用例2: " + s2);
        System.out.println("输出: " + result2);
        System.out.println("期望: accaccacc");
        
        // 测试用例3：复杂嵌套
        String s3 = "2[abc]3[cd]ef";
        String result3 = decodeString(s3);
        System.out.println("\n测试用例3: " + s3);
        System.out.println("输出: " + result3);
        System.out.println("期望: abcabccdcdcdef");
        
        // 测试用例4：单字符
        String s4 = "abc";
        String result4 = decodeString(s4);
        System.out.println("\n测试用例4: " + s4);
        System.out.println("输出: " + result4);
        System.out.println("期望: abc");
        
        // 测试用例5：多层嵌套
        String s5 = "3[z]2[2[y]pq4[2[jk]e1[f]]]ef";
        String result5 = decodeString(s5);
        System.out.println("\n测试用例5: " + s5);
        System.out.println("输出: " + result5);
        System.out.println("期望: zzz" + "yypq" + "jkjkef".repeat(4) + "yypq" + "jkjkef".repeat(4) + "ef");
    }
    
    public static void main(String[] args) {
        testDecodeString();
        System.out.println("\n=== 字符串解码算法验证完成 ===");
    }
}