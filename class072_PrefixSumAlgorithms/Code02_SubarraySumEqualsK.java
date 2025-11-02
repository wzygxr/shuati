package class046;

import java.util.HashMap;

/**
 * 和为K的子数组 (Subarray Sum Equals K)
 * 
 * 题目描述:
 * 给你一个整数数组 nums 和一个整数 k，请你统计并返回该数组中和为 k 的子数组的个数。
 * 子数组是数组中元素的连续非空序列。
 * 
 * 示例:
 * 输入：nums = [1,1,1], k = 2
 * 输出：2
 * 
 * 输入：nums = [1,2,3], k = 3
 * 输出：2
 * 
 * 提示:
 * 1 <= nums.length <= 2 * 10^4
 * -1000 <= nums[i] <= 1000
 * -10^7 <= k <= 10^7
 * 
 * 题目链接: https://leetcode.com/problems/subarray-sum-equals-k/
 * 
 * 解题思路:
 * 使用前缀和 + 哈希表的方法。
 * 1. 遍历数组，计算前缀和
 * 2. 对于当前位置的前缀和sum，查找是否存在前缀和为(sum - k)的历史记录
 * 3. 如果存在，则说明存在子数组和为k
 * 4. 使用哈希表记录每个前缀和出现的次数
 * 
 * 时间复杂度: O(n) - 需要遍历数组一次
 * 空间复杂度: O(n) - 哈希表最多存储n个不同的前缀和
 * 
 * 工程化考量:
 * 1. 边界条件处理：空数组、k值极端情况
 * 2. 哈希表选择：HashMap提供O(1)的平均查找时间
 * 3. 整数溢出：使用long避免大数溢出
 * 4. 负数处理：k可能为负数，但算法本身支持负数
 * 
 * 最优解分析:
 * 这是最优解，因为必须遍历所有元素才能统计所有子数组。
 * 哈希表方法将时间复杂度从O(n^2)优化到O(n)。
 * 
 * 算法核心:
 * 设prefix[i]为前i个元素的和，则子数组[i,j]的和为prefix[j] - prefix[i-1] = k
 * 即prefix[j] - k = prefix[i-1]，因此统计prefix[j] - k出现的次数即可。
 * 
 * 算法调试技巧:
 * 1. 打印中间过程：可以在循环中打印每个位置的前缀和和哈希表状态
 * 2. 边界测试：测试空数组、k=0、负数等情况
 * 3. 性能测试：测试大规模数据下的性能表现
 * 
 * 语言特性差异:
 * Java的HashMap自动处理哈希冲突，但需要注意哈希函数的选择。
 * 与C++相比，Java有自动内存管理，无需手动释放哈希表内存。
 * 与Python相比，Java是静态类型语言，需要显式声明类型。
 */
public class Code02_SubarraySumEqualsK {

    /**
     * 计算和为k的子数组个数
     * 
     * @param nums 输入数组
     * @param k 目标和
     * @return 和为k的子数组个数
     * 
     * 异常场景处理:
     * - 空数组：返回0
     * - k值极端：可能为极大值或极小值
     * - 数组元素包含负数：算法本身支持
     * 
     * 边界条件:
     * - 数组长度为0
     * - k=0的情况（需要特殊处理空子数组）
     * - 数组元素全为0且k=0
     */
    public static int subarraySum(int[] nums, int k) {
        // 边界情况处理
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // 使用HashMap记录前缀和及其出现次数
        // 初始化：前缀和为0出现1次（表示空数组）
        HashMap<Long, Integer> map = new HashMap<>();
        map.put(0L, 1);
        
        int count = 0;          // 结果计数
        long prefixSum = 0;      // 当前前缀和，使用long避免溢出
        
        // 遍历数组
        for (int i = 0; i < nums.length; i++) {
            // 更新前缀和
            prefixSum += nums[i];
            
            // 调试打印：显示中间过程
            // System.out.println("位置 " + i + ": 前缀和 = " + prefixSum + ", 目标 = " + (prefixSum - k));
            
            // 查找是否存在前缀和为(prefixSum - k)的历史记录
            // 如果存在，说明存在子数组和为k
            if (map.containsKey(prefixSum - k)) {
                count += map.get(prefixSum - k);
                // 调试打印：找到子数组
                // System.out.println("找到子数组，当前计数: " + count);
            }
            
            // 更新当前前缀和的出现次数
            map.put(prefixSum, map.getOrDefault(prefixSum, 0) + 1);
            
            // 调试打印：哈希表状态
            // System.out.println("哈希表更新: " + prefixSum + " -> " + map.get(prefixSum));
        }
        
        return count;
    }

    /**
     * 单元测试方法
     */
    public static void testSubarraySum() {
        System.out.println("=== 和为K的子数组单元测试 ===");
        
        // 测试用例1：经典情况
        int[] nums1 = {1, 1, 1};
        int k1 = 2;
        int result1 = subarraySum(nums1, k1);
        System.out.println("测试用例1 [1,1,1] k=2: " + result1 + " (预期: 2)");
        
        // 测试用例2：多个子数组
        int[] nums2 = {1, 2, 3};
        int k2 = 3;
        int result2 = subarraySum(nums2, k2);
        System.out.println("测试用例2 [1,2,3] k=3: " + result2 + " (预期: 2)");
        
        // 测试用例3：包含0和负数
        int[] nums3 = {1, -1, 0};
        int k3 = 0;
        int result3 = subarraySum(nums3, k3);
        System.out.println("测试用例3 [1,-1,0] k=0: " + result3 + " (预期: 3)");
        
        // 测试用例4：单个元素
        int[] nums4 = {5};
        int k4 = 5;
        int result4 = subarraySum(nums4, k4);
        System.out.println("测试用例4 [5] k=5: " + result4 + " (预期: 1)");
        
        // 测试用例5：空数组
        int[] nums5 = {};
        int k5 = 1;
        int result5 = subarraySum(nums5, k5);
        System.out.println("测试用例5 [] k=1: " + result5 + " (预期: 0)");
        
        // 测试用例6：大k值
        int[] nums6 = {1, 2, 3};
        int k6 = 100;
        int result6 = subarraySum(nums6, k6);
        System.out.println("测试用例6 [1,2,3] k=100: " + result6 + " (预期: 0)");
        
        // 测试用例7：全0数组且k=0
        int[] nums7 = {0, 0, 0};
        int k7 = 0;
        int result7 = subarraySum(nums7, k7);
        System.out.println("测试用例7 [0,0,0] k=0: " + result7 + " (预期: 6)");
    }

    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");
        int size = 100000; // 10万元素
        int[] largeArray = new int[size];
        
        // 初始化大数组（避免溢出）
        for (int i = 0; i < size; i++) {
            largeArray[i] = i % 100 - 50; // 包含正负数
        }
        
        long startTime = System.currentTimeMillis();
        int result = subarraySum(largeArray, 0); // 测试k=0的情况
        long endTime = System.currentTimeMillis();
        
        System.out.println("处理 " + size + " 个元素，结果: " + result + ", 耗时: " + (endTime - startTime) + "ms");
    }

    /**
     * 主函数 - 测试入口
     */
    public static void main(String[] args) {
        // 运行单元测试
        testSubarraySum();
        
        // 运行性能测试（可选）
        // performanceTest();
        
        System.out.println("\n=== 测试完成 ===");
    }
}