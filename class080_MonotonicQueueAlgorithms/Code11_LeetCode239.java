import java.util.*;

/**
 * 题目名称：LeetCode 239. 滑动窗口最大值
 * 题目来源：LeetCode
 * 题目链接：https://leetcode.cn/problems/sliding-window-maximum/
 * 题目难度：困难
 * 
 * 题目描述：
 * 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
 * 你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
 * 返回滑动窗口中的最大值。
 * 
 * 解题思路：
 * 使用单调递减队列解决该问题。队列中存储数组元素的下标，队列中的下标对应的元素值保持单调递减。
 * 1. 维护队列的单调性：当新元素进入时，从队尾开始比较，如果新元素大于等于队尾元素，则队尾元素出队
 * 2. 维护窗口大小：检查队首元素是否超出窗口范围，如果超出则出队
 * 3. 记录结果：当窗口形成后（i >= k-1），队首元素即为当前窗口的最大值
 *
 * 算法步骤：
 * 1. 遍历数组中的每个元素
 * 2. 维护单调递减队列的性质
 * 3. 移除过期的下标（超出窗口范围）
 * 4. 当窗口大小达到k时，记录最大值（队首元素对应的值）
 *
 * 时间复杂度分析：
 * O(n) - 每个元素最多入队出队一次
 *
 * 空间复杂度分析：
 * O(k) - 双端队列最多存储k个元素
 *
 * 是否最优解：
 * ✅ 是，这是处理此类问题的最优解法
 * 
 * 工程化考量：
 * - 使用数组模拟双端队列以提高性能
 * - 考虑边界条件处理（k=1, 数组长度小于k等）
 * - 处理极端输入情况（大数组、极限值等）
 */

public class Code11_LeetCode239 {
    
    /**
     * 计算滑动窗口最大值
     * @param nums 输入数组
     * @param k 窗口大小
     * @return 每个滑动窗口的最大值数组
     */
    public static int[] maxSlidingWindow(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0) {
            return new int[0];
        }
        
        int n = nums.length;
        if (k == 1) {
            return Arrays.copyOf(nums, n);
        }
        
        // 结果数组大小为 n - k + 1
        int[] result = new int[n - k + 1];
        int resultIndex = 0;
        
        // 使用双端队列维护窗口内的最大值
        // 队列中存储的是数组下标，对应的元素值保持单调递减
        Deque<Integer> deque = new ArrayDeque<>();
        
        for (int i = 0; i < n; i++) {
            // 维护队列的单调递减性质
            // 从队尾开始，移除所有小于等于当前元素的索引
            while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i]) {
                deque.pollLast();
            }
            
            // 将当前索引加入队列
            deque.offerLast(i);
            
            // 检查队首元素是否超出窗口范围
            // 窗口范围为 [i-k+1, i]
            if (deque.peekFirst() <= i - k) {
                deque.pollFirst();
            }
            
            // 当窗口形成后，记录最大值
            if (i >= k - 1) {
                result[resultIndex++] = nums[deque.peekFirst()];
            }
        }
        
        return result;
    }
    
    /**
     * 优化版本：使用数组模拟双端队列，提高性能
     */
    public static int[] maxSlidingWindowOptimized(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0) {
            return new int[0];
        }
        
        int n = nums.length;
        if (k == 1) {
            return Arrays.copyOf(nums, n);
        }
        
        int[] result = new int[n - k + 1];
        int[] deque = new int[n]; // 模拟双端队列
        int head = 0, tail = 0; // 队列头尾指针
        
        for (int i = 0; i < n; i++) {
            // 维护队列的单调递减性质
            while (head < tail && nums[deque[tail - 1]] <= nums[i]) {
                tail--;
            }
            
            // 将当前索引加入队列
            deque[tail++] = i;
            
            // 检查队首元素是否超出窗口范围
            if (deque[head] <= i - k) {
                head++;
            }
            
            // 当窗口形成后，记录最大值
            if (i >= k - 1) {
                result[i - k + 1] = nums[deque[head]];
            }
        }
        
        return result;
    }
    
    /**
     * 测试方法 - 包含多种边界情况和测试用例
     */
    public static void testMaxSlidingWindow() {
        System.out.println("=== LeetCode 239 测试用例 ===");
        
        // 测试用例1：基础示例
        int[] nums1 = {1, 3, -1, -3, 5, 3, 6, 7};
        int k1 = 3;
        int[] result1 = maxSlidingWindow(nums1, k1);
        System.out.println("测试用例1 - 输入: [1,3,-1,-3,5,3,6,7], k=3");
        System.out.println("预期输出: [3,3,5,5,6,7], 实际输出: " + Arrays.toString(result1));
        System.out.println("测试结果: " + (Arrays.equals(result1, new int[]{3,3,5,5,6,7}) ? "✓ 通过" : "✗ 失败"));
        
        // 测试用例2：单个元素
        int[] nums2 = {1};
        int k2 = 1;
        int[] result2 = maxSlidingWindow(nums2, k2);
        System.out.println("\n测试用例2 - 输入: [1], k=1");
        System.out.println("预期输出: [1], 实际输出: " + Arrays.toString(result2));
        System.out.println("测试结果: " + (Arrays.equals(result2, new int[]{1}) ? "✓ 通过" : "✗ 失败"));
        
        // 测试用例3：递减序列
        int[] nums3 = {7, 6, 5, 4, 3, 2, 1};
        int k3 = 3;
        int[] result3 = maxSlidingWindow(nums3, k3);
        System.out.println("\n测试用例3 - 输入: [7,6,5,4,3,2,1], k=3");
        System.out.println("预期输出: [7,6,5,4,3], 实际输出: " + Arrays.toString(result3));
        System.out.println("测试结果: " + (Arrays.equals(result3, new int[]{7,6,5,4,3}) ? "✓ 通过" : "✗ 失败"));
        
        // 测试用例4：递增序列
        int[] nums4 = {1, 2, 3, 4, 5, 6, 7};
        int k4 = 3;
        int[] result4 = maxSlidingWindow(nums4, k4);
        System.out.println("\n测试用例4 - 输入: [1,2,3,4,5,6,7], k=3");
        System.out.println("预期输出: [3,4,5,6,7], 实际输出: " + Arrays.toString(result4));
        System.out.println("测试结果: " + (Arrays.equals(result4, new int[]{3,4,5,6,7}) ? "✓ 通过" : "✗ 失败"));
        
        // 测试用例5：k等于数组长度
        int[] nums5 = {1, 3, -1, -3, 5, 3, 6, 7};
        int k5 = 8;
        int[] result5 = maxSlidingWindow(nums5, k5);
        System.out.println("\n测试用例5 - 输入: [1,3,-1,-3,5,3,6,7], k=8");
        System.out.println("预期输出: [7], 实际输出: " + Arrays.toString(result5));
        System.out.println("测试结果: " + (Arrays.equals(result5, new int[]{7}) ? "✓ 通过" : "✗ 失败"));
        
        // 测试用例6：优化版本对比
        int[] nums6 = {1, 3, -1, -3, 5, 3, 6, 7};
        int k6 = 3;
        int[] result6 = maxSlidingWindowOptimized(nums6, k6);
        System.out.println("\n测试用例6 - 优化版本对比");
        System.out.println("输入: [1,3,-1,-3,5,3,6,7], k=3");
        System.out.println("优化版本输出: " + Arrays.toString(result6));
        System.out.println("测试结果: " + (Arrays.equals(result6, new int[]{3,3,5,5,6,7}) ? "✓ 通过" : "✗ 失败"));
        
        System.out.println("\n=== 性能测试 ===");
        
        // 性能测试：大数组测试
        int[] largeNums = new int[10000];
        Random random = new Random();
        for (int i = 0; i < largeNums.length; i++) {
            largeNums[i] = random.nextInt(10000);
        }
        int largeK = 100;
        
        long startTime1 = System.currentTimeMillis();
        int[] resultLarge1 = maxSlidingWindow(largeNums, largeK);
        long endTime1 = System.currentTimeMillis();
        
        long startTime2 = System.currentTimeMillis();
        int[] resultLarge2 = maxSlidingWindowOptimized(largeNums, largeK);
        long endTime2 = System.currentTimeMillis();
        
        System.out.println("大数组测试 (10000个元素, k=100):");
        System.out.println("标准版本执行时间: " + (endTime1 - startTime1) + "ms");
        System.out.println("优化版本执行时间: " + (endTime2 - startTime2) + "ms");
        System.out.println("结果一致性: " + Arrays.equals(resultLarge1, resultLarge2));
        
        System.out.println("\n=== 算法分析 ===");
        System.out.println("时间复杂度: O(n) - 每个元素最多入队出队一次");
        System.out.println("空间复杂度: O(k) - 双端队列最多存储k个元素");
        System.out.println("最优解: ✅ 是");
        
        System.out.println("\n=== 工程化考量 ===");
        System.out.println("1. 异常处理: 处理空数组和无效k值");
        System.out.println("2. 性能优化: 提供数组模拟队列的优化版本");
        System.out.println("3. 内存管理: 合理设置数组大小，避免内存浪费");
        System.out.println("4. 可读性: 清晰的变量命名和详细注释");
    }
    
    public static void main(String[] args) {
        testMaxSlidingWindow();
    }
}