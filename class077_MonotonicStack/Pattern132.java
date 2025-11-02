// package class052.problems;

import java.util.Arrays;

/**
 * 456. 132 模式 (132 Pattern)
 * 
 * 题目描述:
 * 给你一个整数数组 nums ，数组中共有 n 个整数。
 * 132 模式的子序列 由三个整数 nums[i]、nums[j] 和 nums[k] 组成，
 * 并同时满足：i < j < k 和 nums[i] < nums[k] < nums[j]。
 * 如果 nums 中存在 132 模式的子序列，返回 true；否则，返回 false。
 * 
 * 解题思路:
 * 使用单调栈来解决。从右往左遍历数组，维护一个单调递减栈。
 * 同时记录一个变量 second，表示可能的最大中间值（即3后面的最大2）。
 * 当遇到比 second 小的元素时，说明找到了132模式。
 * 
 * 时间复杂度: O(n)，每个元素最多入栈和出栈各一次
 * 空间复杂度: O(n)，用于存储单调栈
 * 
 * 测试链接: https://leetcode.cn/problems/132-pattern/
 * 
 * 工程化考量:
 * 1. 异常处理：空数组、单元素数组边界情况
 * 2. 性能优化：使用数组模拟栈提高效率
 * 3. 代码可读性：详细注释和有意义变量名
 * 4. 单元测试：多种边界测试用例
 */
public class Pattern132 {
    
    /**
     * 判断数组中是否存在132模式的子序列
     * 
     * @param nums 输入整数数组
     * @return 如果存在132模式返回true，否则返回false
     */
    public static boolean find132pattern(int[] nums) {
        // 边界条件检查
        if (nums == null || nums.length < 3) {
            return false; // 至少需要3个元素才能形成132模式
        }
        
        int n = nums.length;
        // 使用数组模拟栈，提高性能
        int[] stack = new int[n];
        int top = -1; // 栈顶指针
        int second = Integer.MIN_VALUE; // 记录可能的最大中间值（3后面的最大2）
        
        // 从右往左遍历数组
        for (int i = n - 1; i >= 0; i--) {
            // 如果当前元素小于second，说明找到了132模式
            if (nums[i] < second) {
                return true;
            }
            
            // 维护单调递减栈，找到更大的元素作为3，并更新second
            while (top >= 0 && nums[i] > nums[stack[top]]) {
                // 更新second为栈顶元素（即当前找到的最大2）
                second = nums[stack[top--]];
            }
            
            // 将当前索引入栈
            stack[++top] = i;
        }
        
        return false; // 没有找到132模式
    }
    
    /**
     * 测试方法 - 验证算法正确性
     */
    public static void main(String[] args) {
        // 测试用例1: [1, 2, 3, 4] - 预期: false
        int[] nums1 = {1, 2, 3, 4};
        boolean result1 = find132pattern(nums1);
        System.out.println("测试用例1 [1, 2, 3, 4]: " + result1 + " (预期: false)");
        
        // 测试用例2: [3, 1, 4, 2] - 预期: true
        int[] nums2 = {3, 1, 4, 2};
        boolean result2 = find132pattern(nums2);
        System.out.println("测试用例2 [3, 1, 4, 2]: " + result2 + " (预期: true)");
        
        // 测试用例3: [-1, 3, 2, 0] - 预期: true
        int[] nums3 = {-1, 3, 2, 0};
        boolean result3 = find132pattern(nums3);
        System.out.println("测试用例3 [-1, 3, 2, 0]: " + result3 + " (预期: true)");
        
        // 测试用例4: [1, 0, 1, -4, -3] - 预期: false
        int[] nums4 = {1, 0, 1, -4, -3};
        boolean result4 = find132pattern(nums4);
        System.out.println("测试用例4 [1, 0, 1, -4, -3]: " + result4 + " (预期: false)");
        
        // 测试用例5: 边界情况 - 空数组
        int[] nums5 = {};
        boolean result5 = find132pattern(nums5);
        System.out.println("测试用例5 []: " + result5 + " (预期: false)");
        
        // 测试用例6: 边界情况 - 两个元素
        int[] nums6 = {1, 2};
        boolean result6 = find132pattern(nums6);
        System.out.println("测试用例6 [1, 2]: " + result6 + " (预期: false)");
        
        // 测试用例7: 重复元素 [1, 3, 2, 4, 5, 6, 7, 8, 9, 10] - 预期: true
        int[] nums7 = {1, 3, 2, 4, 5, 6, 7, 8, 9, 10};
        boolean result7 = find132pattern(nums7);
        System.out.println("测试用例7 [1, 3, 2, 4, 5, 6, 7, 8, 9, 10]: " + result7 + " (预期: true)");
        
        // 性能测试：大规模数据
        int[] nums8 = new int[10000];
        for (int i = 0; i < nums8.length; i++) {
            nums8[i] = i; // 严格递增，预期false
        }
        long startTime = System.currentTimeMillis();
        boolean result8 = find132pattern(nums8);
        long endTime = System.currentTimeMillis();
        System.out.println("性能测试 [10000个元素]: " + result8 + " (预期: false), 耗时: " + (endTime - startTime) + "ms");
        
        System.out.println("所有测试用例执行完成！");
    }
    
    /**
     * 调试辅助方法：打印中间过程
     * 
     * @param nums 输入数组
     * @param i 当前索引
     * @param stack 栈数组
     * @param top 栈顶指针
     * @param second 当前second值
     */
    private static void debugPrint(int[] nums, int i, int[] stack, int top, int second) {
        System.out.println("i=" + i + ", nums[i]=" + nums[i] + ", second=" + second);
        System.out.print("栈内容: [");
        for (int j = 0; j <= top; j++) {
            System.out.print(nums[stack[j]]);
            if (j < top) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("---");
    }
    
    /**
     * 算法复杂度分析:
     * 
     * 时间复杂度: O(n)
     * - 每个元素最多入栈一次和出栈一次
     * - 虽然有两层循环，但内层循环的总操作次数不超过n次
     * 
     * 空间复杂度: O(n)
     * - 使用了一个大小为n的数组作为栈
     * - 没有使用递归，栈空间为O(1)
     * 
     * 最优解分析:
     * - 这是132模式问题的最优解
     * - 无法在O(n)时间内获得更好的时间复杂度
     * - 空间复杂度也是最优的，因为需要存储中间结果
     */
}