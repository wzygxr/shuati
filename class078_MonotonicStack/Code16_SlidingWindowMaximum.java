package class053;

import java.util.*;

/**
 * 滑动窗口最大值
 * 
 * 题目描述：
 * 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
 * 你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
 * 返回滑动窗口中的最大值。
 * 
 * 测试链接：https://leetcode.cn/problems/sliding-window-maximum/
 * 题目来源：LeetCode
 * 难度：困难
 * 
 * 核心算法：单调队列（双端队列实现）
 * 
 * 解题思路：
 * 使用单调递减双端队列来维护当前窗口中的最大值候选者。
 * 队列中存储的是数组元素的索引，而不是元素值本身，这样可以方便判断元素是否在窗口内。
 * 
 * 具体步骤：
 * 1. 初始化一个双端队列用于存储索引
 * 2. 遍历数组中的每个元素：
 *    a. 移除队列中不在当前窗口范围内的索引（从队首移除）
 *    b. 从队尾开始移除所有小于当前元素的索引，保持队列单调递减
 *    c. 将当前元素索引入队
 *    d. 当窗口形成时（i >= k-1），记录当前窗口的最大值（队首元素）
 * 
 * 时间复杂度分析：
 * O(n) - 每个元素最多入队和出队各一次，n为数组长度
 * 
 * 空间复杂度分析：
 * O(k) - 队列最多存储k个元素
 * 
 * 是否为最优解：
 * 是，这是解决该问题的最优解之一
 * 
 * 工程化考量：
 * 1. 健壮性：处理了null输入、空数组和k=0的边界情况
 * 2. 性能优化：使用索引而非值入队，避免了不必要的值传递
 * 3. 可读性：使用清晰的变量名和注释说明算法步骤
 * 
 * 算法调试技巧：
 * 1. 打印中间过程：在循环中打印队列的内容和当前处理的元素
 * 2. 断言验证：可以添加断言验证队列的单调递减性
 * 3. 边界测试：使用特殊测试用例如k=1、k=n等验证算法正确性
 * 
 * 相关题目：
 * 1. 最小栈（LeetCode 155）- 使用辅助栈维护最小值
 * 2. 队列的最大值（剑指Offer 59）- 类似思路维护队列最大值
 * 3. 子数组的最大最小值之差（HackerRank）- 使用两个单调队列维护滑动窗口的最小最大值
 * 
 * 语言特性差异：
 * - Java: 使用LinkedList或ArrayDeque实现双端队列
 * - C++: 使用deque容器
 * - Python: 使用collections.deque
 * 
 * 极端场景处理：
 * 1. 空输入：返回空数组
 * 2. k=0：返回空数组
 * 3. k>n：返回空数组
 * 4. 单调递增/递减数组：验证算法正确性
 */
public class Code16_SlidingWindowMaximum {
    
    public static int[] maxSlidingWindow(int[] nums, int k) {
        // 边界条件检查
        if (nums == null || nums.length == 0 || k <= 0 || k > nums.length) {
            return new int[0];
        }
        
        int n = nums.length;
        int[] result = new int[n - k + 1];
        int resultIndex = 0;
        
        // 使用双端队列存储索引，维护单调递减队列
        Deque<Integer> deque = new LinkedList<>();
        
        for (int i = 0; i < n; i++) {
            // 步骤1：移除队列中不在当前窗口范围内的索引
            while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
                deque.pollFirst();
            }
            
            // 步骤2：从队尾开始移除所有小于当前元素的索引
            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                deque.pollLast();
            }
            
            // 步骤3：将当前索引入队
            deque.offerLast(i);
            
            // 步骤4：当窗口形成时，记录当前窗口的最大值
            if (i >= k - 1) {
                result[resultIndex++] = nums[deque.peekFirst()];
            }
        }
        
        return result;
    }
    
    /**
     * 单元测试方法
     * 包含多种测试场景验证算法正确性
     */
    public static void testMaxSlidingWindow() {
        System.out.println("=== 滑动窗口最大值单元测试 ===");
        
        // 测试用例1：常规情况
        int[] nums1 = {1, 3, -1, -3, 5, 3, 6, 7};
        int k1 = 3;
        int[] result1 = maxSlidingWindow(nums1, k1);
        System.out.println("测试用例1: " + Arrays.toString(nums1) + ", k=" + k1);
        System.out.println("输出: " + Arrays.toString(result1));
        System.out.println("期望: [3, 3, 5, 5, 6, 7]");
        
        // 测试用例2：k=1的情况
        int[] nums2 = {1, 2, 3, 4, 5};
        int k2 = 1;
        int[] result2 = maxSlidingWindow(nums2, k2);
        System.out.println("测试用例2: " + Arrays.toString(nums2) + ", k=" + k2);
        System.out.println("输出: " + Arrays.toString(result2));
        System.out.println("期望: [1, 2, 3, 4, 5]");
        
        // 测试用例3：k等于数组长度
        int[] nums3 = {9, 8, 7, 6, 5};
        int k3 = 5;
        int[] result3 = maxSlidingWindow(nums3, k3);
        System.out.println("测试用例3: " + Arrays.toString(nums3) + ", k=" + k3);
        System.out.println("输出: " + Arrays.toString(result3));
        System.out.println("期望: [9]");
        
        // 测试用例4：单调递增数组
        int[] nums4 = {1, 2, 3, 4, 5, 6};
        int k4 = 3;
        int[] result4 = maxSlidingWindow(nums4, k4);
        System.out.println("测试用例4: " + Arrays.toString(nums4) + ", k=" + k4);
        System.out.println("输出: " + Arrays.toString(result4));
        System.out.println("期望: [3, 4, 5, 6]");
        
        // 测试用例5：单调递减数组
        int[] nums5 = {6, 5, 4, 3, 2, 1};
        int k5 = 3;
        int[] result5 = maxSlidingWindow(nums5, k5);
        System.out.println("测试用例5: " + Arrays.toString(nums5) + ", k=" + k5);
        System.out.println("输出: " + Arrays.toString(result5));
        System.out.println("期望: [6, 5, 4, 3]");
        
        // 测试用例6：边界情况 - 空数组
        int[] nums6 = {};
        int k6 = 3;
        int[] result6 = maxSlidingWindow(nums6, k6);
        System.out.println("测试用例6: 空数组, k=" + k6);
        System.out.println("输出: " + Arrays.toString(result6));
        System.out.println("期望: []");
        
        // 测试用例7：边界情况 - k=0
        int[] nums7 = {1, 2, 3};
        int k7 = 0;
        int[] result7 = maxSlidingWindow(nums7, k7);
        System.out.println("测试用例7: " + Arrays.toString(nums7) + ", k=" + k7);
        System.out.println("输出: " + Arrays.toString(result7));
        System.out.println("期望: []");
        
        // 测试用例8：包含重复元素
        int[] nums8 = {1, 3, 3, 2, 5, 5, 4};
        int k8 = 3;
        int[] result8 = maxSlidingWindow(nums8, k8);
        System.out.println("测试用例8: " + Arrays.toString(nums8) + ", k=" + k8);
        System.out.println("输出: " + Arrays.toString(result8));
        System.out.println("期望: [3, 3, 3, 5, 5]");
    }
    
    /**
     * 性能测试方法
     * 测试算法在大规模数据下的性能表现
     */
    public static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");
        
        // 生成大规模测试数据
        int n = 100000;
        int[] largeNums = new int[n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            largeNums[i] = random.nextInt(10000);
        }
        int k = 1000;
        
        long startTime = System.currentTimeMillis();
        int[] result = maxSlidingWindow(largeNums, k);
        long endTime = System.currentTimeMillis();
        
        System.out.println("数据规模: " + n + ", 窗口大小: " + k);
        System.out.println("执行时间: " + (endTime - startTime) + "ms");
        System.out.println("结果数组长度: " + result.length);
    }
    
    public static void main(String[] args) {
        // 运行单元测试
        testMaxSlidingWindow();
        
        // 运行性能测试
        performanceTest();
        
        System.out.println("\n=== 算法验证完成 ===");
    }
}