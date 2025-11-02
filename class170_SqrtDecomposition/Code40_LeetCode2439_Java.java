import java.util.*;

/**
 * LeetCode 2439. 最小化数组中的最大值
 * 题目要求：将数组分成k个子数组，最小化子数组最大值
 * 核心技巧：分块 + 贪心
 * 时间复杂度：O(n log n) / 操作
 * 测试链接：https://leetcode.cn/problems/minimize-maximum-of-array/
 *
 * 该问题的最优解法是二分查找，而不是分块。虽然题目中提到分块+贪心，但二分查找能达到更优的时间复杂度。
 * 二分查找的思路是：对于每个可能的最大值mid，检查是否可以将数组分成k个子数组，使得每个子数组的元素和都不超过mid。
 */

public class Code40_LeetCode2439_Java {
    /**
     * 主函数：使用二分查找最小化数组中的最大值
     * @param nums 输入数组
     * @param k 子数组数量
     * @return 最小的可能的子数组最大值
     */
    public static int minimizeArrayValue(int[] nums) {
        // 问题实际上是不需要分成k个子数组的，而是通过调整相邻元素来最小化最大元素
        // 正确的解法是前缀和贪心
        long prefixSum = 0;
        int result = 0;
        
        for (int i = 0; i < nums.length; i++) {
            prefixSum += nums[i];
            // 计算当前前缀的平均值，如果有小数则向上取整
            // 这表示如果我们可以将前面的数平均分配，最大的数至少是这个值
            long currentMax = (prefixSum + i) / (i + 1);
            result = Math.max(result, (int)currentMax);
        }
        
        return result;
    }
    
    /**
     * 二分查找解法
     * 这种方法也可以解决问题，但不是最优解
     */
    public static int minimizeArrayValueBinarySearch(int[] nums) {
        int left = 0;
        int right = Arrays.stream(nums).max().orElse(0);
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (canMinimize(nums, mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        
        return left;
    }
    
    /**
     * 检查是否可以通过调整使得所有元素都不超过maxValue
     * @param nums 输入数组
     * @param maxValue 最大允许值
     * @return 是否可以调整
     */
    private static boolean canMinimize(int[] nums, int maxValue) {
        // 从右往左调整
        // 如果当前元素超过maxValue，则将多余的部分转移给左边的元素
        long extra = 0;
        for (int i = nums.length - 1; i >= 0; i--) {
            long current = nums[i] + extra;
            if (current > maxValue) {
                extra = current - maxValue;
            } else {
                extra = 0;
            }
        }
        // 如果没有多余的，说明可以调整
        return extra == 0;
    }
    
    /**
     * 正确性测试函数
     */
    public static void correctnessTest() {
        System.out.println("=== 正确性测试 ===");
        
        // 测试用例1
        int[] nums1 = {3, 7, 1, 6};
        System.out.println("测试用例1: [3, 7, 1, 6]");
        System.out.println("前缀和贪心法结果: " + minimizeArrayValue(nums1));  // 应为5
        System.out.println("二分查找法结果: " + minimizeArrayValueBinarySearch(nums1));  // 应为5
        
        // 测试用例2
        int[] nums2 = {10, 1};
        System.out.println("\n测试用例2: [10, 1]");
        System.out.println("前缀和贪心法结果: " + minimizeArrayValue(nums2));  // 应为10
        System.out.println("二分查找法结果: " + minimizeArrayValueBinarySearch(nums2));  // 应为10
        
        // 测试用例3
        int[] nums3 = {1, 2, 3, 4, 5};
        System.out.println("\n测试用例3: [1, 2, 3, 4, 5]");
        System.out.println("前缀和贪心法结果: " + minimizeArrayValue(nums3));  // 应为3
        System.out.println("二分查找法结果: " + minimizeArrayValueBinarySearch(nums3));  // 应为3
        
        // 测试用例4：全部相同
        int[] nums4 = {5, 5, 5, 5};
        System.out.println("\n测试用例4: [5, 5, 5, 5]");
        System.out.println("前缀和贪心法结果: " + minimizeArrayValue(nums4));  // 应为5
        System.out.println("二分查找法结果: " + minimizeArrayValueBinarySearch(nums4));  // 应为5
    }
    
    /**
     * 性能测试函数
     */
    public static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");
        
        // 生成大规模测试数据
        int n = 100000;
        int[] nums = new int[n];
        Random rand = new Random(42);
        for (int i = 0; i < n; i++) {
            nums[i] = rand.nextInt(1000000) + 1;
        }
        
        // 测试前缀和贪心法
        long startTime = System.currentTimeMillis();
        int result1 = minimizeArrayValue(nums);
        long endTime = System.currentTimeMillis();
        System.out.println("前缀和贪心法处理1e5数据耗时: " + (endTime - startTime) + "ms");
        
        // 测试二分查找法
        startTime = System.currentTimeMillis();
        int result2 = minimizeArrayValueBinarySearch(nums);
        endTime = System.currentTimeMillis();
        System.out.println("二分查找法处理1e5数据耗时: " + (endTime - startTime) + "ms");
        
        // 验证结果一致性
        System.out.println("结果一致性验证: " + (result1 == result2));
    }
    
    /**
     * 边界情况测试
     */
    public static void boundaryTest() {
        System.out.println("\n=== 边界情况测试 ===");
        
        // 测试n=1的情况
        int[] nums1 = {5};
        System.out.println("n=1, nums=[5]");
        System.out.println("前缀和贪心法结果: " + minimizeArrayValue(nums1));  // 应为5
        
        // 测试全为0的情况
        int[] nums2 = {0, 0, 0, 0};
        System.out.println("\n全为0: [0, 0, 0, 0]");
        System.out.println("前缀和贪心法结果: " + minimizeArrayValue(nums2));  // 应为0
        
        // 测试递增序列
        int[] nums3 = {1, 100, 1000, 10000};
        System.out.println("\n递增序列: [1, 100, 1000, 10000]");
        System.out.println("前缀和贪心法结果: " + minimizeArrayValue(nums3));  // 应为3367
        
        // 测试递减序列
        int[] nums4 = {10000, 1000, 100, 1};
        System.out.println("\n递减序列: [10000, 1000, 100, 1]");
        System.out.println("前缀和贪心法结果: " + minimizeArrayValue(nums4));  // 应为10000
    }
    
    /**
     * 算法效率对比函数
     */
    public static void algorithmComparison() {
        System.out.println("\n=== 算法效率对比 ===");
        
        // 测试不同大小的数组
        int[] sizes = {100, 1000, 10000, 100000};
        
        for (int size : sizes) {
            int[] nums = new int[size];
            Random rand = new Random(42);
            for (int i = 0; i < size; i++) {
                nums[i] = rand.nextInt(1000000) + 1;
            }
            
            System.out.println("\n数组大小: " + size);
            
            // 前缀和贪心法
            long startTime = System.currentTimeMillis();
            int result1 = minimizeArrayValue(nums);
            long endTime = System.currentTimeMillis();
            System.out.println("前缀和贪心法耗时: " + (endTime - startTime) + "ms");
            
            // 二分查找法
            startTime = System.currentTimeMillis();
            int result2 = minimizeArrayValueBinarySearch(nums);
            endTime = System.currentTimeMillis();
            System.out.println("二分查找法耗时: " + (endTime - startTime) + "ms");
            
            // 验证结果一致性
            System.out.println("结果一致: " + (result1 == result2));
        }
    }
    
    /**
     * 运行所有测试的函数
     */
    public static void runAllTests() {
        correctnessTest();
        performanceTest();
        boundaryTest();
        algorithmComparison();
    }
    
    /**
     * 主函数
     */
    public static void main(String[] args) {
        runAllTests();
    }
}

/**
 * 算法原理解析：
 *
 * 1. 问题分析：
 *    - 给定一个数组，通过调整相邻元素（每次可以将一个元素减1，另一个加1），最小化数组中的最大值
 *    - 关键约束：只能将值从右往左移动，不能从左往右移动
 *    - 这意味着我们需要在保证前缀和的情况下，尽可能平均分配值
 *
 * 2. 前缀和贪心算法：
 *    - 对于每个位置i，计算前i+1个元素的前缀和
 *    - 计算前缀和除以元素个数的平均值（向上取整）
 *    - 这个平均值就是当前前缀中能达到的最小可能的最大值
 *    - 因为不能将值从右往左移动，所以这个最大值是必须接受的下限
 *
 * 3. 二分查找算法（次优解）：
 *    - 二分查找可能的最大值范围
 *    - 对于每个可能的最大值mid，从右往左检查是否可以通过调整使得所有元素都不超过mid
 *    - 如果当前元素超过mid，则将多余的部分转移给左边的元素
 *
 * 4. 时间复杂度分析：
 *    - 前缀和贪心法：O(n)，只需遍历数组一次
 *    - 二分查找法：O(n log maxVal)，其中maxVal是数组中的最大值
 *    - 前缀和贪心法明显优于二分查找法
 *
 * 5. 空间复杂度分析：
 *    - 两种算法都是O(1)，只需常数额外空间
 *
 * 6. 算法正确性证明：
 *    - 前缀和贪心算法的正确性基于以下观察：
 *      - 对于前i+1个元素，它们的总和是固定的
 *      - 要最小化最大值，最优情况是平均分配
 *      - 由于不能将值从右往左移动，所以前i+1个元素的最大值至少是平均值（向上取整）
 *
 * 7. 优化技巧：
 *    - 使用long类型避免前缀和溢出
 *    - 使用整数除法的技巧向上取整：(sum + i) / (i + 1)
 *    - 二分查找时使用left + (right - left) / 2避免整数溢出
 *
 * 8. 工程应用：
 *    - 这类问题在资源分配、负载均衡等场景中有广泛应用
 *    - 前缀和贪心的思想可以应用于各种需要局部最优解的问题
 *    - 在实际系统中，可能需要考虑数据类型范围和数值精度问题
 */