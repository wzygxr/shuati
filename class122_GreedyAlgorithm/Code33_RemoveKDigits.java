package class091;

import java.util.*;

/**
 * 移掉K位数字
 * 
 * 题目描述：
 * 给你一个以字符串表示的非负整数 num 和一个整数 k ，移除这个数中的 k 位数字，使得剩下的数字最小。
 * 请你以字符串形式返回这个最小的数字。
 * 
 * 来源：LeetCode 402
 * 链接：https://leetcode.cn/problems/remove-k-digits/
 * 
 * 算法思路：
 * 使用贪心算法 + 单调栈：
 * 1. 使用栈来保存最终结果
 * 2. 遍历字符串中的每个字符：
 *    - 当栈非空且栈顶元素大于当前字符且还有删除次数时，弹出栈顶元素
 *    - 将当前字符入栈
 * 3. 如果遍历完成后还有剩余的删除次数，从栈顶删除（因为栈是单调递增的）
 * 4. 处理前导零并返回结果
 * 
 * 时间复杂度：O(n) - 每个字符最多入栈出栈一次
 * 空间复杂度：O(n) - 栈的空间
 * 
 * 关键点分析：
 * - 贪心策略：移除高位较大的数字可以最大化减少数值
 * - 单调栈：维护一个单调递增的栈
 * - 边界处理：处理前导零和空结果
 * 
 * 工程化考量：
 * - 输入验证：检查字符串和k的有效性
 * - 性能优化：使用StringBuilder而非String
 * - 可读性：清晰的变量命名和注释
 */
public class Code33_RemoveKDigits {
    
    /**
     * 移除k位数字使得剩下的数字最小
     * 
     * @param num 输入数字字符串
     * @param k 要移除的数字个数
     * @return 最小的数字字符串
     */
    public static String removeKdigits(String num, int k) {
        // 输入验证
        if (num == null || num.length() == 0) {
            return "0";
        }
        if (k < 0) {
            throw new IllegalArgumentException("k必须是非负整数");
        }
        if (k >= num.length()) {
            return "0";
        }
        
        // 使用栈来保存结果
        Deque<Character> stack = new ArrayDeque<>();
        
        for (int i = 0; i < num.length(); i++) {
            char current = num.charAt(i);
            
            // 当栈非空且栈顶元素大于当前字符且还有删除次数时，弹出栈顶元素
            while (!stack.isEmpty() && k > 0 && stack.peek() > current) {
                stack.pop();
                k--;
            }
            
            // 将当前字符入栈（避免前导零）
            if (!stack.isEmpty() || current != '0') {
                stack.push(current);
            }
        }
        
        // 如果还有剩余的删除次数，从栈顶删除（因为栈是单调递增的）
        while (!stack.isEmpty() && k > 0) {
            stack.pop();
            k--;
        }
        
        // 处理空栈的情况
        if (stack.isEmpty()) {
            return "0";
        }
        
        // 构建结果字符串
        StringBuilder result = new StringBuilder();
        while (!stack.isEmpty()) {
            result.append(stack.pop());
        }
        
        // 反转字符串（因为栈是后进先出的）
        return result.reverse().toString();
    }
    
    /**
     * 另一种实现：使用数组模拟栈
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public static String removeKdigitsArray(String num, int k) {
        if (num == null || num.length() == 0) {
            return "0";
        }
        if (k < 0) {
            throw new IllegalArgumentException("k必须是非负整数");
        }
        if (k >= num.length()) {
            return "0";
        }
        
        // 使用数组模拟栈
        char[] stack = new char[num.length()];
        int top = -1;
        
        for (int i = 0; i < num.length(); i++) {
            char current = num.charAt(i);
            
            // 当栈非空且栈顶元素大于当前字符且还有删除次数时，弹出栈顶元素
            while (top >= 0 && k > 0 && stack[top] > current) {
                top--;
                k--;
            }
            
            // 将当前字符入栈（避免前导零）
            if (top >= 0 || current != '0') {
                stack[++top] = current;
            }
        }
        
        // 如果还有剩余的删除次数，从栈顶删除
        while (top >= 0 && k > 0) {
            top--;
            k--;
        }
        
        // 处理空栈的情况
        if (top < 0) {
            return "0";
        }
        
        // 构建结果字符串
        return new String(stack, 0, top + 1);
    }
    
    /**
     * 递归解法（用于理解思路）
     * 时间复杂度：O(C(n, k)) - 组合数，效率较低
     * 空间复杂度：O(n) - 递归栈深度
     */
    public static String removeKdigitsRecursive(String num, int k) {
        if (num == null || num.length() == 0) {
            return "0";
        }
        if (k < 0) {
            throw new IllegalArgumentException("k必须是非负整数");
        }
        if (k >= num.length()) {
            return "0";
        }
        if (k == 0) {
            // 去除前导零
            int start = 0;
            while (start < num.length() && num.charAt(start) == '0') {
                start++;
            }
            return start == num.length() ? "0" : num.substring(start);
        }
        
        String minNum = num;
        
        // 尝试移除每一位数字
        for (int i = 0; i < num.length(); i++) {
            // 移除第i位数字
            String newNum = num.substring(0, i) + num.substring(i + 1);
            String result = removeKdigitsRecursive(newNum, k - 1);
            
            // 比较大小
            if (compare(result, minNum) < 0) {
                minNum = result;
            }
        }
        
        return minNum;
    }
    
    /**
     * 比较两个数字字符串的大小
     * 
     * @param num1 第一个数字字符串
     * @param num2 第二个数字字符串
     * @return 比较结果：-1表示num1 < num2，0表示相等，1表示num1 > num2
     */
    private static int compare(String num1, String num2) {
        // 去除前导零
        String s1 = removeLeadingZeros(num1);
        String s2 = removeLeadingZeros(num2);
        
        if (s1.length() != s2.length()) {
            return s1.length() - s2.length();
        }
        
        return s1.compareTo(s2);
    }
    
    /**
     * 去除字符串的前导零
     * 
     * @param num 输入字符串
     * @return 去除前导零后的字符串
     */
    private static String removeLeadingZeros(String num) {
        int start = 0;
        while (start < num.length() && num.charAt(start) == '0') {
            start++;
        }
        return start == num.length() ? "0" : num.substring(start);
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1: num = "1432219", k = 3 -> "1219"
        String num1 = "1432219";
        int k1 = 3;
        System.out.println("测试用例1: num = \"" + num1 + "\", k = " + k1);
        System.out.println("方法1结果: \"" + removeKdigits(num1, k1) + "\""); // "1219"
        System.out.println("方法2结果: \"" + removeKdigitsArray(num1, k1) + "\""); // "1219"
        
        // 测试用例2: num = "10200", k = 1 -> "200"
        String num2 = "10200";
        int k2 = 1;
        System.out.println("\n测试用例2: num = \"" + num2 + "\", k = " + k2);
        System.out.println("方法1结果: \"" + removeKdigits(num2, k2) + "\""); // "200"
        System.out.println("方法2结果: \"" + removeKdigitsArray(num2, k2) + "\""); // "200"
        
        // 测试用例3: num = "10", k = 2 -> "0"
        String num3 = "10";
        int k3 = 2;
        System.out.println("\n测试用例3: num = \"" + num3 + "\", k = " + k3);
        System.out.println("方法1结果: \"" + removeKdigits(num3, k3) + "\""); // "0"
        System.out.println("方法2结果: \"" + removeKdigitsArray(num3, k3) + "\""); // "0"
        
        // 测试用例4: num = "9", k = 1 -> "0"
        String num4 = "9";
        int k4 = 1;
        System.out.println("\n测试用例4: num = \"" + num4 + "\", k = " + k4);
        System.out.println("方法1结果: \"" + removeKdigits(num4, k4) + "\""); // "0"
        System.out.println("方法2结果: \"" + removeKdigitsArray(num4, k4) + "\""); // "0"
        
        // 测试用例5: num = "123456", k = 3 -> "123"
        String num5 = "123456";
        int k5 = 3;
        System.out.println("\n测试用例5: num = \"" + num5 + "\", k = " + k5);
        System.out.println("方法1结果: \"" + removeKdigits(num5, k5) + "\""); // "123"
        System.out.println("方法2结果: \"" + removeKdigitsArray(num5, k5) + "\""); // "123"
        
        // 边界测试：k = 0
        String num6 = "123";
        int k6 = 0;
        System.out.println("\n测试用例6: num = \"" + num6 + "\", k = " + k6);
        System.out.println("方法1结果: \"" + removeKdigits(num6, k6) + "\""); // "123"
        System.out.println("方法2结果: \"" + removeKdigitsArray(num6, k6) + "\""); // "123"
        
        // 性能测试
        performanceTest();
    }
    
    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        // 生成大规模测试数据
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            sb.append(random.nextInt(10));
        }
        String largeNum = sb.toString();
        int k = 500;
        
        System.out.println("\n=== 性能测试 ===");
        
        long startTime1 = System.currentTimeMillis();
        String result1 = removeKdigits(largeNum, k);
        long endTime1 = System.currentTimeMillis();
        System.out.println("方法1执行时间: " + (endTime1 - startTime1) + "ms");
        System.out.println("结果长度: " + result1.length());
        
        long startTime2 = System.currentTimeMillis();
        String result2 = removeKdigitsArray(largeNum, k);
        long endTime2 = System.currentTimeMillis();
        System.out.println("方法2执行时间: " + (endTime2 - startTime2) + "ms");
        System.out.println("结果长度: " + result2.length());
        
        // 验证结果一致性
        System.out.println("结果一致性: " + result1.equals(result2));
    }
    
    /**
     * 算法复杂度分析
     */
    public static void analyzeComplexity() {
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("方法1（单调栈）:");
        System.out.println("- 时间复杂度: O(n)");
        System.out.println("  - 每个字符最多入栈出栈一次");
        System.out.println("  - 总体线性时间复杂度");
        System.out.println("- 空间复杂度: O(n)");
        System.out.println("  - 栈的空间: O(n)");
        
        System.out.println("\n方法2（数组模拟栈）:");
        System.out.println("- 时间复杂度: O(n)");
        System.out.println("  - 每个字符处理一次");
        System.out.println("  - 数组操作效率高");
        System.out.println("- 空间复杂度: O(n)");
        System.out.println("  - 数组空间: O(n)");
        
        System.out.println("\n方法3（递归）:");
        System.out.println("- 时间复杂度: O(C(n, k))");
        System.out.println("  - 组合数复杂度，指数级");
        System.out.println("  - 仅用于理解思路，不适用于实际应用");
        System.out.println("- 空间复杂度: O(n)");
        System.out.println("  - 递归栈深度: O(n)");
        
        System.out.println("\n贪心策略证明:");
        System.out.println("1. 高位数字对数值影响更大，优先移除高位较大的数字");
        System.out.println("2. 单调栈确保每次移除的都是当前最优选择");
        System.out.println("3. 数学归纳法证明贪心选择性质");
        
        System.out.println("\n工程化考量:");
        System.out.println("1. 输入验证：处理非法输入和边界情况");
        System.out.println("2. 性能优化：选择高效的数据结构");
        System.out.println("3. 可读性：清晰的算法逻辑和注释");
        System.out.println("4. 测试覆盖：全面的测试用例设计");
    }
}