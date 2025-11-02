// 乘积最大子数组 (Maximum Product Subarray)
// 给你一个整数数组 nums ，请你找出数组中乘积最大的非空连续子数组（该子数组中至少包含一个数字），并返回该子数组所对应的乘积。
// 测试链接 : https://leetcode.cn/problems/maximum-product-subarray/

package class066;

/**
 * 乘积最大子数组 - 动态规划处理正负号问题
 * 时间复杂度分析：
 * - 暴力解法：O(n^2) - 枚举所有子数组
 * - 动态规划：O(n) - 线性扫描一次
 * - 空间优化：O(1) - 只保存必要的前一个状态
 * 
 * 空间复杂度分析：
 * - 暴力解法：O(1) - 只保存当前最大值
 * - 动态规划：O(n) - dp数组存储所有状态
 * - 空间优化：O(1) - 工程首选
 * 
 * 工程化考量：
 * 1. 正负号处理：需要同时维护最大值和最小值
 * 2. 边界处理：空数组、单元素数组、包含0的数组
 * 3. 性能优化：空间优化版本应对大规模数据
 * 4. 代码清晰：明确的变量命名和状态转移逻辑
 */
public class Code23_MaximumProductSubarray {

    // 方法1：动态规划（同时维护最大值和最小值）
    // 时间复杂度：O(n) - 遍历数组一次
    // 空间复杂度：O(n) - 使用两个dp数组
    // 核心思路：由于存在负数，最小值可能变成最大值，需要同时维护
    public static int maxProduct1(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        
        int n = nums.length;
        int[] maxDp = new int[n];  // 存储以i结尾的最大乘积
        int[] minDp = new int[n];  // 存储以i结尾的最小乘积
        
        maxDp[0] = nums[0];
        minDp[0] = nums[0];
        int result = nums[0];
        
        for (int i = 1; i < n; i++) {
            // 三种可能：当前数字、当前数字×最大乘积、当前数字×最小乘积
            int num = nums[i];
            int option1 = num;
            int option2 = num * maxDp[i - 1];
            int option3 = num * minDp[i - 1];
            
            maxDp[i] = Math.max(option1, Math.max(option2, option3));
            minDp[i] = Math.min(option1, Math.min(option2, option3));
            
            result = Math.max(result, maxDp[i]);
        }
        
        return result;
    }

    // 方法2：空间优化的动态规划
    // 时间复杂度：O(n) - 与方法1相同
    // 空间复杂度：O(1) - 只使用常数空间
    // 优化：使用变量代替数组，减少空间使用
    public static int maxProduct2(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        
        int n = nums.length;
        int maxSoFar = nums[0];  // 当前最大乘积
        int minSoFar = nums[0];  // 当前最小乘积
        int result = nums[0];
        
        for (int i = 1; i < n; i++) {
            int num = nums[i];
            int tempMax = maxSoFar;  // 保存之前的值，避免覆盖
            
            // 更新最大值和最小值
            maxSoFar = Math.max(num, Math.max(num * maxSoFar, num * minSoFar));
            minSoFar = Math.min(num, Math.min(num * tempMax, num * minSoFar));
            
            result = Math.max(result, maxSoFar);
        }
        
        return result;
    }

    // 方法3：分治解法（用于对比）
    // 时间复杂度：O(n log n) - 分治递归
    // 空间复杂度：O(log n) - 递归栈深度
    // 核心思路：将数组分成左右两部分，最大乘积可能在左、右或跨越中间
    public static int maxProduct3(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        return divideAndConquer(nums, 0, nums.length - 1);
    }
    
    private static int divideAndConquer(int[] nums, int left, int right) {
        if (left == right) return nums[left];
        
        int mid = left + (right - left) / 2;
        
        // 左半部分的最大乘积
        int leftMax = divideAndConquer(nums, left, mid);
        // 右半部分的最大乘积
        int rightMax = divideAndConquer(nums, mid + 1, right);
        // 跨越中间的最大乘积
        int crossMax = maxCrossingProduct(nums, left, mid, right);
        
        return Math.max(leftMax, Math.max(rightMax, crossMax));
    }
    
    private static int maxCrossingProduct(int[] nums, int left, int mid, int right) {
        // 从左到右计算包含mid的最大乘积
        int leftMax = nums[mid];
        int leftMin = nums[mid];
        int product = nums[mid];
        
        for (int i = mid - 1; i >= left; i--) {
            product *= nums[i];
            leftMax = Math.max(leftMax, product);
            leftMin = Math.min(leftMin, product);
        }
        
        // 从右到左计算包含mid+1的最大乘积
        int rightMax = nums[mid + 1];
        int rightMin = nums[mid + 1];
        product = nums[mid + 1];
        
        for (int i = mid + 2; i <= right; i++) {
            product *= nums[i];
            rightMax = Math.max(rightMax, product);
            rightMin = Math.min(rightMin, product);
        }
        
        // 跨越中间的最大乘积可能是各种组合
        return Math.max(leftMax * rightMax, Math.max(leftMax * rightMin, 
                Math.max(leftMin * rightMax, leftMin * rightMin)));
    }

    // 方法4：暴力解法（用于对比）
    // 时间复杂度：O(n^2) - 枚举所有子数组
    // 空间复杂度：O(1) - 只保存当前最大值
    // 问题：效率低，仅用于教学目的
    public static int maxProduct4(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        
        int n = nums.length;
        int result = Integer.MIN_VALUE;
        
        for (int i = 0; i < n; i++) {
            int product = 1;
            for (int j = i; j < n; j++) {
                product *= nums[j];
                result = Math.max(result, product);
            }
        }
        
        return result;
    }

    // 方法5：前缀积解法（处理0的特殊情况）
    // 时间复杂度：O(n) - 遍历数组两次
    // 空间复杂度：O(1) - 只使用常数空间
    // 核心思路：计算前缀积，遇到0时重置
    public static int maxProduct5(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        
        int n = nums.length;
        int result = nums[0];
        int product = 1;
        
        // 从左到右计算
        for (int i = 0; i < n; i++) {
            product *= nums[i];
            result = Math.max(result, product);
            if (nums[i] == 0) {
                product = 1;  // 遇到0重置
            }
        }
        
        // 从右到左计算（处理负数情况）
        product = 1;
        for (int i = n - 1; i >= 0; i--) {
            product *= nums[i];
            result = Math.max(result, product);
            if (nums[i] == 0) {
                product = 1;  // 遇到0重置
            }
        }
        
        return result;
    }

    // 全面的测试用例
    public static void main(String[] args) {
        System.out.println("=== 乘积最大子数组测试 ===");
        
        // 边界测试
        testCase(new int[]{}, 0, "空数组");
        testCase(new int[]{5}, 5, "单元素数组");
        testCase(new int[]{-5}, -5, "单负数元素");
        
        // LeetCode示例测试
        testCase(new int[]{2, 3, -2, 4}, 6, "示例1");
        testCase(new int[]{-2, 0, -1}, 0, "示例2");
        testCase(new int[]{-2, 3, -4}, 24, "示例3");
        
        // 常规测试
        testCase(new int[]{1, 2, 3, 4, 5}, 120, "全正数");
        testCase(new int[]{-1, -2, -3, -4, -5}, 120, "全负数（偶数个）");
        testCase(new int[]{-1, -2, -3, -4}, 24, "全负数（奇数个）");
        
        // 包含0的测试
        testCase(new int[]{2, 0, 3, 4}, 12, "包含0");
        testCase(new int[]{-2, 0, 3, 4}, 12, "负数后接0");
        testCase(new int[]{0, 0, 0, 5}, 5, "多个0");
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        int[] largeNums = new int[1000];
        for (int i = 0; i < largeNums.length; i++) {
            largeNums[i] = (i % 10) - 5;  // -5到4的循环数字
        }
        
        long start = System.currentTimeMillis();
        int result2 = maxProduct2(largeNums);
        long end = System.currentTimeMillis();
        System.out.println("空间优化方法: " + result2 + ", 耗时: " + (end - start) + "ms");
        
        start = System.currentTimeMillis();
        int result5 = maxProduct5(largeNums);
        end = System.currentTimeMillis();
        System.out.println("前缀积方法: " + result5 + ", 耗时: " + (end - start) + "ms");
        
        // 暴力方法太慢，不测试
        System.out.println("暴力方法在n=1000时太慢，跳过测试");
    }
    
    private static void testCase(int[] nums, int expected, String description) {
        int result1 = maxProduct1(nums);
        int result2 = maxProduct2(nums);
        int result3 = maxProduct3(nums);
        int result5 = maxProduct5(nums);
        
        boolean allCorrect = (result1 == expected && result2 == expected && 
                            result3 == expected && result5 == expected);
        
        System.out.println(description + ": " + (allCorrect ? "✓" : "✗"));
        if (!allCorrect) {
            System.out.println("  方法1: " + result1 + " | 方法2: " + result2 + 
                             " | 方法3: " + result3 + " | 方法5: " + result5 + 
                             " | 预期: " + expected);
        }
    }
    
    /**
     * 算法总结与工程化思考：
     * 
     * 1. 问题本质：处理正负号的最大连续子数组乘积问题
     *    - 关键洞察：负数可能使最小值变成最大值，需要同时维护最大最小值
     *    - 状态转移：max = max(num, num*max, num*min), min = min(num, num*max, num*min)
     * 
     * 2. 时间复杂度对比：
     *    - 暴力解法：O(n^2) - 不可接受
     *    - 分治解法：O(n log n) - 可接受但非最优
     *    - 动态规划：O(n) - 推荐
     *    - 空间优化：O(n) - 工程首选
     * 
     * 3. 空间复杂度对比：
     *    - 暴力解法：O(1) - 但效率低
     *    - 分治解法：O(log n) - 递归栈
     *    - 动态规划：O(n) - 数组存储
     *    - 空间优化：O(1) - 最优
     * 
     * 4. 特殊情况处理：
     *    - 包含0的情况：乘积会重置为1
     *    - 全负数情况：需要考虑乘积的正负性
     *    - 单个元素情况：直接返回该元素
     * 
     * 5. 工程选择依据：
     *    - 一般情况：方法2（空间优化动态规划）
     *    - 需要分治思路：方法3（分治解法）
     *    - 简单实现：方法5（前缀积解法）
     * 
     * 6. 调试技巧：
     *    - 分别跟踪最大值和最小值的变化
     *    - 验证包含0时的重置逻辑
     *    - 检查负数相乘的正负号处理
     * 
     * 7. 关联题目：
     *    - 最大子数组和（Kadane算法）
     *    - 最大连续1的个数
     *    - 子数组最小乘积的最大值
     * 
     * 8. 优化思路：
     *    - 提前终止：当乘积为0时重置
     *    - 空间优化：使用变量代替数组
     *    - 边界处理：单独处理空数组和单元素情况
     */
}