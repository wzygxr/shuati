package class053;

import java.util.*;

/**
 * 使括号有效的最少删除
 * 
 * 题目描述：
 * 给你一个由 '('、')' 和小写字母组成的字符串 s。
 * 你需要从字符串中删除最少数目的 '(' 或者 ')'（可以删除任意位置的括号)，使得剩下的「括号字符串」有效。
 * 请返回任意一个合法字符串。
 * 
 * 测试链接：https://leetcode.cn/problems/minimum-remove-to-make-valid-parentheses/
 * 题目来源：LeetCode
 * 难度：中等
 * 
 * 核心算法：栈 + 标记删除
 * 
 * 解题思路：
 * 1. 使用栈来记录左括号的位置
 * 2. 遍历字符串，遇到左括号入栈，遇到右括号时：
 *    - 如果栈不为空，弹出栈顶（匹配成功）
 *    - 如果栈为空，标记这个右括号需要删除
 * 3. 遍历结束后，栈中剩余的左括号都需要删除
 * 4. 根据标记构建结果字符串
 * 
 * 时间复杂度分析：
 * O(n) - 需要遍历字符串两次，n为字符串长度
 * 
 * 空间复杂度分析：
 * O(n) - 栈的空间最多为n，标记数组需要n空间
 * 
 * 是否为最优解：
 * 是，这是解决该问题的最优解之一
 * 
 * 工程化考量：
 * 1. 健壮性：处理空字符串、纯字母字符串等边界情况
 * 2. 性能优化：使用标记数组避免频繁的字符串操作
 * 3. 可读性：使用清晰的变量名和注释说明算法步骤
 * 
 * 算法调试技巧：
 * 1. 打印中间过程：在循环中打印栈的状态和标记数组
 * 2. 边界测试：测试各种边界情况如全字母、全括号等
 * 3. 性能测试：测试大规模字符串下的性能表现
 */
public class Code18_MinimumRemoveToMakeValidParentheses {
    
    /**
     * 主解法：使用栈和标记数组
     * @param s 输入字符串
     * @return 有效的括号字符串
     */
    public static String minRemoveToMakeValid(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        
        int n = s.length();
        // 标记数组：true表示保留，false表示删除
        boolean[] keep = new boolean[n];
        Arrays.fill(keep, true);
        
        // 使用栈记录左括号的位置
        Stack<Integer> stack = new Stack<>();
        
        // 第一次遍历：标记需要删除的括号
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            if (c == '(') {
                // 左括号入栈，暂时标记为保留
                stack.push(i);
            } else if (c == ')') {
                if (stack.isEmpty()) {
                    // 没有匹配的左括号，这个右括号需要删除
                    keep[i] = false;
                } else {
                    // 有匹配的左括号，弹出栈顶
                    stack.pop();
                }
            }
            // 字母字符保持保留状态
        }
        
        // 栈中剩余的左括号都需要删除
        while (!stack.isEmpty()) {
            keep[stack.pop()] = false;
        }
        
        // 构建结果字符串
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (keep[i]) {
                result.append(s.charAt(i));
            }
        }
        
        return result.toString();
    }
    
    /**
     * 优化解法：两次遍历法（空间优化）
     * 第一次遍历：删除多余的右括号
     * 第二次遍历：删除多余的左括号
     */
    public static String minRemoveToMakeValidOptimized(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        
        // 第一次遍历：删除多余的右括号
        StringBuilder sb = new StringBuilder();
        int balance = 0;  // 括号平衡计数器
        
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                balance++;
                sb.append(c);
            } else if (c == ')') {
                if (balance > 0) {
                    balance--;
                    sb.append(c);
                }
                // 如果balance <= 0，不添加这个右括号
            } else {
                sb.append(c);
            }
        }
        
        // 第二次遍历：删除多余的左括号（从右向左）
        StringBuilder result = new StringBuilder();
        int removeLeft = balance;  // 需要删除的左括号数量
        
        for (int i = sb.length() - 1; i >= 0; i--) {
            char c = sb.charAt(i);
            if (c == '(' && removeLeft > 0) {
                removeLeft--;
            } else {
                result.append(c);
            }
        }
        
        return result.reverse().toString();
    }
    
    /**
     * 单元测试方法
     */
    public static void testMinRemoveToMakeValid() {
        System.out.println("=== 使括号有效的最少删除单元测试 ===");
        
        // 测试用例1：常规情况
        String s1 = "lee(t(c)o)de)";
        String result1 = minRemoveToMakeValid(s1);
        System.out.println("测试用例1: " + s1);
        System.out.println("输出: " + result1);
        System.out.println("期望: lee(t(c)o)de 或 lee(t(co)de) 或 lee(t(c)ode)");
        
        // 测试用例2：需要删除多个括号
        String s2 = "a)b(c)d";
        String result2 = minRemoveToMakeValid(s2);
        System.out.println("\n测试用例2: " + s2);
        System.out.println("输出: " + result2);
        System.out.println("期望: ab(c)d");
        
        // 测试用例3：删除所有括号
        String s3 = "))((";
        String result3 = minRemoveToMakeValid(s3);
        System.out.println("\n测试用例3: " + s3);
        System.out.println("输出: " + result3);
        System.out.println("期望: \"\" (空字符串)");
        
        // 测试用例4：已经是有效括号
        String s4 = "(a(b(c)d))";
        String result4 = minRemoveToMakeValid(s4);
        System.out.println("\n测试用例4: " + s4);
        System.out.println("输出: " + result4);
        System.out.println("期望: (a(b(c)d))");
        
        // 测试用例5：纯字母字符串
        String s5 = "abcdefg";
        String result5 = minRemoveToMakeValid(s5);
        System.out.println("\n测试用例5: " + s5);
        System.out.println("输出: " + result5);
        System.out.println("期望: abcdefg");
        
        // 测试用例6：边界情况 - 空字符串
        String s6 = "";
        String result6 = minRemoveToMakeValid(s6);
        System.out.println("\n测试用例6: 空字符串");
        System.out.println("输出: " + result6);
        System.out.println("期望: \"\"");
        
        // 测试用例7：复杂嵌套
        String s7 = "((a)(b)((c)))d))";
        String result7 = minRemoveToMakeValid(s7);
        System.out.println("\n测试用例7: " + s7);
        System.out.println("输出: " + result7);
        System.out.println("期望: ((a)(b)((c)))d");
    }
    
    /**
     * 性能对比测试：标记数组法 vs 两次遍历法
     */
    public static void performanceComparison() {
        System.out.println("\n=== 性能对比测试 ===");
        
        // 生成测试数据
        int n = 100000;
        StringBuilder testData = new StringBuilder();
        Random random = new Random();
        
        // 生成包含括号和字母的混合字符串
        for (int i = 0; i < n; i++) {
            int type = random.nextInt(3);
            switch (type) {
                case 0: testData.append('('); break;
                case 1: testData.append(')'); break;
                case 2: testData.append((char)('a' + random.nextInt(26))); break;
            }
        }
        String testString = testData.toString();
        
        // 测试标记数组法
        long startTime1 = System.currentTimeMillis();
        String result1 = minRemoveToMakeValid(testString);
        long endTime1 = System.currentTimeMillis();
        
        // 测试两次遍历法
        long startTime2 = System.currentTimeMillis();
        String result2 = minRemoveToMakeValidOptimized(testString);
        long endTime2 = System.currentTimeMillis();
        
        System.out.println("数据规模: " + n + "个字符");
        System.out.println("标记数组法执行时间: " + (endTime1 - startTime1) + "ms");
        System.out.println("两次遍历法执行时间: " + (endTime2 - startTime2) + "ms");
        System.out.println("结果长度对比: " + result1.length() + " vs " + result2.length());
        System.out.println("结果是否相等: " + result1.equals(result2));
    }
    
    /**
     * 正确性验证：验证两种解法结果是否一致
     */
    public static void correctnessVerification() {
        System.out.println("\n=== 正确性验证 ===");
        
        String[] testCases = {
            "lee(t(c)o)de)",
            "a)b(c)d",
            "))((",
            "(a(b(c)d))",
            "abcdefg",
            "",
            "((a)(b)((c)))d))",
            "()()()()",
            "(((((())))))",
            "))))((((()"
        };
        
        boolean allPassed = true;
        for (int i = 0; i < testCases.length; i++) {
            String s = testCases[i];
            String result1 = minRemoveToMakeValid(s);
            String result2 = minRemoveToMakeValidOptimized(s);
            
            boolean isValid1 = isValidParentheses(result1);
            boolean isValid2 = isValidParentheses(result2);
            boolean resultsEqual = result1.equals(result2);
            
            if (!isValid1 || !isValid2 || !resultsEqual) {
                System.out.println("测试用例 " + i + " 失败:");
                System.out.println("输入: " + s);
                System.out.println("解法1结果: " + result1 + " (有效: " + isValid1 + ")");
                System.out.println("解法2结果: " + result2 + " (有效: " + isValid2 + ")");
                System.out.println("结果相等: " + resultsEqual);
                allPassed = false;
            }
        }
        
        if (allPassed) {
            System.out.println("所有测试用例通过！");
        }
    }
    
    /**
     * 验证括号字符串是否有效
     */
    private static boolean isValidParentheses(String s) {
        int balance = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') {
                balance++;
            } else if (c == ')') {
                balance--;
                if (balance < 0) return false;
            }
        }
        return balance == 0;
    }
    
    public static void main(String[] args) {
        // 运行单元测试
        testMinRemoveToMakeValid();
        
        // 运行性能对比测试
        performanceComparison();
        
        // 运行正确性验证
        correctnessVerification();
        
        System.out.println("\n=== 算法验证完成 ===");
    }
}