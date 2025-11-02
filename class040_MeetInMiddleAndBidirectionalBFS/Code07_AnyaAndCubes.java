package class063;

import java.util.*;

// Anya and Cubes
// 给定n个数和一个目标值S，每个数可以：
// 1. 不选
// 2. 选，加上这个数
// 3. 选，加上这个数的阶乘（如果这个数<=18）
// 求有多少种选择方案使得所选数的和等于S。
// 测试链接 : https://codeforces.com/problemset/problem/525/E
// 
// 算法思路：
// 使用折半搜索（Meet in the Middle）算法解决，将数组分为两半分别计算所有可能的和，
// 然后通过哈希表统计不同方案数
// 时间复杂度：O(3^(n/2) * log(3^(n/2)))
// 空间复杂度：O(3^(n/2))
// 
// 工程化考量：
// 1. 异常处理：检查输入参数
// 2. 性能优化：使用折半搜索减少搜索空间，预计算阶乘
// 3. 可读性：变量命名清晰，注释详细
// 
// 语言特性差异：
// Java中使用HashMap进行计数统计，使用递归计算所有可能的和

public class Code07_AnyaAndCubes {
    
    // 预计算阶乘数组
    private static final long[] FACTORIAL = new long[19];
    
    static {
        FACTORIAL[0] = 1;
        for (int i = 1; i <= 18; i++) {
            FACTORIAL[i] = FACTORIAL[i - 1] * i;
        }
    }
    
    /**
     * 计算满足条件的选择方案数
     * @param nums 输入数组
     * @param k 最多可以使用阶乘操作的次数
     * @param S 目标值
     * @return 满足条件的方案数
     */
    public static long solve(int[] nums, int k, long S) {
        // 边界条件检查
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        
        // 使用折半搜索，将数组分为两半
        // Map<和, Map<使用阶乘次数, 方案数>>
        Map<Long, Map<Integer, Long>> leftSums = new HashMap<>();
        Map<Long, Map<Integer, Long>> rightSums = new HashMap<>();
        
        // 计算左半部分所有可能的和及其方案数
        generateSubsetSums(nums, 0, n / 2, 0, 0, k, leftSums);
        
        // 计算右半部分所有可能的和及其方案数
        generateSubsetSums(nums, n / 2, n, 0, 0, k, rightSums);
        
        // 统计满足条件的方案数
        long count = 0;
        for (Map.Entry<Long, Map<Integer, Long>> leftEntry : leftSums.entrySet()) {
            long leftSum = leftEntry.getKey();
            Map<Integer, Long> leftMap = leftEntry.getValue();
            
            for (Map.Entry<Long, Map<Integer, Long>> rightEntry : rightSums.entrySet()) {
                long rightSum = rightEntry.getKey();
                Map<Integer, Long> rightMap = rightEntry.getValue();
                
                // 如果左右两部分的和等于目标值
                if (leftSum + rightSum == S) {
                    // 统计所有可能的组合
                    for (Map.Entry<Integer, Long> leftCountEntry : leftMap.entrySet()) {
                        int leftK = leftCountEntry.getKey();
                        long leftCount = leftCountEntry.getValue();
                        
                        for (Map.Entry<Integer, Long> rightCountEntry : rightMap.entrySet()) {
                            int rightK = rightCountEntry.getKey();
                            long rightCount = rightCountEntry.getValue();
                            
                            // 如果使用的阶乘次数不超过限制
                            if (leftK + rightK <= k) {
                                count += leftCount * rightCount;
                            }
                        }
                    }
                }
            }
        }
        
        return count;
    }
    
    /**
     * 递归生成指定范围内所有可能的和及其方案数
     * @param nums 输入数组
     * @param start 起始索引
     * @param end 结束索引
     * @param currentSum 当前累积和
     * @param currentK 当前使用的阶乘次数
     * @param maxK 最多可以使用的阶乘次数
     * @param sums 存储结果的Map
     */
    private static void generateSubsetSums(int[] nums, int start, int end, long currentSum, int currentK, int maxK, Map<Long, Map<Integer, Long>> sums) {
        // 递归终止条件
        if (start == end) {
            sums.computeIfAbsent(currentSum, k -> new HashMap<>()).merge(currentK, 1L, Long::sum);
            return;
        }
        
        int num = nums[start];
        
        // 不选择当前元素
        generateSubsetSums(nums, start + 1, end, currentSum, currentK, maxK, sums);
        
        // 选择当前元素（加上这个数）
        generateSubsetSums(nums, start + 1, end, currentSum + num, currentK, maxK, sums);
        
        // 选择当前元素（加上这个数的阶乘）
        if (num <= 18 && currentK < maxK) {
            generateSubsetSums(nums, start + 1, end, currentSum + FACTORIAL[num], currentK + 1, maxK, sums);
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 1, 1};
        int k1 = 1;
        long S1 = 3;
        System.out.println("测试用例1:");
        System.out.println("nums = [1, 1, 1], k = 1, S = 3");
        System.out.println("期望输出: 6");
        System.out.println("实际输出: " + solve(nums1, k1, S1));
        System.out.println();
        
        // 测试用例2
        int[] nums2 = {1, 2, 3};
        int k2 = 2;
        long S2 = 6;
        System.out.println("测试用例2:");
        System.out.println("nums = [1, 2, 3], k = 2, S = 6");
        System.out.println("期望输出: 7");
        System.out.println("实际输出: " + solve(nums2, k2, S2));
    }
}