package class013_QueueAndStackAlgorithms;

import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        System.out.println("=== 队列和栈相关算法测试 ===\n");
        
        // 测试有效的括号
        String s = "()[]{}";
        System.out.println("测试有效的括号: " + QueueStackAndCircularQueue.isValid(s));
        
        // 测试下一个更大元素 I
        int[] nums1 = {4, 1, 2};
        int[] nums2 = {1, 3, 4, 2};
        int[] result = QueueStackAndCircularQueue.nextGreaterElement(nums1, nums2);
        System.out.println("下一个更大元素 I: " + Arrays.toString(result));
        
        // 测试每日温度
        int[] temperatures = {73, 74, 75, 71, 69, 72, 76, 73};
        int[] days = QueueStackAndCircularQueue.dailyTemperatures(temperatures);
        System.out.println("每日温度: " + Arrays.toString(days));
        
        // 测试简化路径
        String path = "/a/./b/../../c/";
        System.out.println("简化路径: " + QueueStackAndCircularQueue.simplifyPath(path));
        
        // 测试比较含退格的字符串
        String s1 = "ab#c";
        String s2 = "ad#c";
        System.out.println("比较含退格的字符串(栈): " + QueueStackAndCircularQueue.backspaceCompare(s1, s2));
        System.out.println("比较含退格的字符串(优化): " + QueueStackAndCircularQueue.backspaceCompareOptimized(s1, s2));
        
        // 测试移掉K位数字
        String num = "1432219";
        int k = 3;
        System.out.println("移掉K位数字: " + QueueStackAndCircularQueue.removeKdigits(num, k));
        
        // 测试验证栈序列
        int[] pushed = {1, 2, 3, 4, 5};
        int[] popped = {4, 5, 3, 2, 1};
        System.out.println("验证栈序列: " + QueueStackAndCircularQueue.validateStackSequences(pushed, popped));
        
        System.out.println("\n=== 所有测试通过! ===");
    }
}
