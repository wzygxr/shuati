package class032;

import java.util.*;
import java.util.stream.*;

/**
 * LeetCode 416 Partition Equal Subset Sum - 子集和问题
 * 题目链接: https://leetcode.com/problems/partition-equal-subset-sum/
 * 题目描述: 给定一个只包含正整数的非空数组，判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等
 * 
 * 解题思路:
 * 方法1: 回溯法 - 暴力搜索所有子集，时间复杂度高
 * 方法2: 动态规划（0-1背包问题） - 使用二维DP数组
 * 方法3: 动态规划优化 - 使用一维DP数组，空间优化
 * 方法4: Bitset优化 - 使用位运算加速DP
 * 
 * 时间复杂度分析:
 * 方法1: O(2^N) - 指数级，不可行
 * 方法2: O(N * sum) - 伪多项式时间
 * 方法3: O(N * sum) - 空间优化版本
 * 方法4: O(N * sum/32) - 使用bitset优化常数因子
 * 
 * 空间复杂度分析:
 * 方法1: O(N) - 递归栈空间
 * 方法2: O(N * sum) - 二维DP数组
 * 方法3: O(sum) - 一维DP数组
 * 方法4: O(sum/32) - bitset空间
 * 
 * 工程化考量:
 * 1. 输入验证: 检查数组是否为空，元素是否为正整数
 * 2. 边界处理: 处理总和为奇数的情况（直接返回false）
 * 3. 性能优化: 根据数据规模选择最优算法
 * 4. 内存管理: 使用bitset优化大内存消耗
 */

public class Code14_SubsetSum {
    
    /**
     * 方法1: 回溯法（仅用于教学，实际不可行）
     * 优点: 实现简单，易于理解
     * 缺点: 时间复杂度高，仅适用于小规模数据
     */
    public static boolean canPartitionBacktrack(int[] nums) {
        if (nums == null || nums.length == 0) return false;
        
        int totalSum = Arrays.stream(nums).sum();
        // 如果总和是奇数，不可能平分
        if (totalSum % 2 != 0) return false;
        
        int target = totalSum / 2;
        return backtrack(nums, 0, 0, target);
    }
    
    private static boolean backtrack(int[] nums, int index, int currentSum, int target) {
        if (currentSum == target) return true;
        if (currentSum > target || index >= nums.length) return false;
        
        // 选择当前元素
        if (backtrack(nums, index + 1, currentSum + nums[index], target)) {
            return true;
        }
        
        // 不选择当前元素
        return backtrack(nums, index + 1, currentSum, target);
    }
    
    /**
     * 方法2: 动态规划（二维DP）
     * 使用标准的0-1背包问题解法
     * dp[i][j]表示前i个元素能否组成和为j的子集
     */
    public static boolean canPartitionDP2D(int[] nums) {
        if (nums == null || nums.length == 0) return false;
        
        int totalSum = Arrays.stream(nums).sum();
        if (totalSum % 2 != 0) return false;
        
        int target = totalSum / 2;
        int n = nums.length;
        
        boolean[][] dp = new boolean[n + 1][target + 1];
        
        // 初始化：和为0的子集总是存在（不选任何元素）
        for (int i = 0; i <= n; i++) {
            dp[i][0] = true;
        }
        
        // 动态规划填表
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= target; j++) {
                // 不选当前元素
                dp[i][j] = dp[i - 1][j];
                
                // 选当前元素（如果j >= nums[i-1]）
                if (j >= nums[i - 1]) {
                    dp[i][j] = dp[i][j] || dp[i - 1][j - nums[i - 1]];
                }
            }
        }
        
        return dp[n][target];
    }
    
    /**
     * 方法3: 动态规划优化（一维DP）
     * 空间优化版本，使用滚动数组
     * 注意：需要从后往前遍历避免重复计算
     */
    public static boolean canPartitionDP1D(int[] nums) {
        if (nums == null || nums.length == 0) return false;
        
        int totalSum = Arrays.stream(nums).sum();
        if (totalSum % 2 != 0) return false;
        
        int target = totalSum / 2;
        int n = nums.length;
        
        boolean[] dp = new boolean[target + 1];
        dp[0] = true;  // 和为0的子集总是存在
        
        for (int i = 0; i < n; i++) {
            // 从后往前遍历，避免重复计算
            for (int j = target; j >= nums[i]; j--) {
                dp[j] = dp[j] || dp[j - nums[i]];
            }
            
            // 提前终止：如果已经找到目标值
            if (dp[target]) return true;
        }
        
        return dp[target];
    }
    
    /**
     * 方法4: Bitset优化（最优解）
     * 使用BitSet来加速动态规划，将时间复杂度优化常数因子
     * 原理：使用位运算来同时更新多个状态
     */
    public static boolean canPartitionBitset(int[] nums) {
        if (nums == null || nums.length == 0) return false;
        
        int totalSum = Arrays.stream(nums).sum();
        if (totalSum % 2 != 0) return false;
        
        int target = totalSum / 2;
        
        // 使用BitSet来记录可达的和
        BitSet bitSet = new BitSet(target + 1);
        bitSet.set(0);  // 和为0总是可达
        
        for (int num : nums) {
            // 创建当前bitSet的副本，用于左移操作
            BitSet newBitSet = (BitSet) bitSet.clone();
            
            // 将bitSet左移num位，相当于给所有可达和加上num
            for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i + 1)) {
                int newSum = i + num;
                if (newSum <= target) {
                    newBitSet.set(newSum);
                }
            }
            
            bitSet.or(newBitSet);  // 合并新的可达和
            
            // 提前终止
            if (bitSet.get(target)) {
                return true;
            }
        }
        
        return bitSet.get(target);
    }
    
    /**
     * 方法5: 更高效的Bitset优化
     * 使用单个BitSet和位运算技巧
     */
    public static boolean canPartitionBitsetOptimized(int[] nums) {
        if (nums == null || nums.length == 0) return false;
        
        int totalSum = Arrays.stream(nums).sum();
        if (totalSum % 2 != 0) return false;
        
        int target = totalSum / 2;
        
        BitSet bitSet = new BitSet(target + 1);
        bitSet.set(0);
        
        for (int num : nums) {
            // 使用位运算技巧：bitSet左移num位然后与原来的bitSet取或
            BitSet temp = bitSet.get(0, target - num + 1);  // 获取0到target-num的子集
            for (int i = temp.nextSetBit(0); i >= 0; i = temp.nextSetBit(i + 1)) {
                int newSum = i + num;
                if (newSum <= target) {
                    bitSet.set(newSum);
                }
            }
            
            if (bitSet.get(target)) {
                return true;
            }
        }
        
        return bitSet.get(target);
    }
    
    /**
     * 方法6: 终极Bitset优化（竞赛常用）
     * 使用位运算直接操作long数组，性能最优
     */
    public static boolean canPartitionBitsetUltimate(int[] nums) {
        if (nums == null || nums.length == 0) return false;
        
        int totalSum = Arrays.stream(nums).sum();
        if (totalSum % 2 != 0) return false;
        
        int target = totalSum / 2;
        
        // 使用long数组模拟bitset，每个long可以表示64位
        int size = (target >> 6) + 1;  // target/64 + 1
        long[] bitset = new long[size];
        bitset[0] = 1L;  // 第0位设为1（表示和为0可达）
        
        for (int num : nums) {
            // 复制当前bitset
            long[] newBitset = bitset.clone();
            
            // 对每个long进行位操作
            for (int i = 0; i < size; i++) {
                if (bitset[i] != 0) {
                    // 计算左移num位后的位置
                    int shift = num;
                    int newIndex = i + (shift >> 6);
                    int bitPos = shift & 63;
                    
                    if (newIndex < size) {
                        newBitset[newIndex] |= bitset[i] << bitPos;
                        
                        // 处理跨long的情况
                        if (bitPos > 0 && newIndex + 1 < size) {
                            newBitset[newIndex + 1] |= bitset[i] >>> (64 - bitPos);
                        }
                    }
                }
            }
            
            // 合并新的bitset
            for (int i = 0; i < size; i++) {
                bitset[i] |= newBitset[i];
            }
            
            // 检查目标是否可达
            int targetIndex = target >> 6;
            int targetBit = target & 63;
            if ((bitset[targetIndex] & (1L << targetBit)) != 0) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 优化版本：根据数据规模选择算法
     */
    public static boolean canPartitionOptimized(int[] nums) {
        if (nums == null || nums.length == 0) return false;
        
        int totalSum = Arrays.stream(nums).sum();
        if (totalSum % 2 != 0) return false;
        
        int target = totalSum / 2;
        int n = nums.length;
        
        // 根据数据规模选择算法
        if (n <= 20) {
            // 小规模数据使用回溯法（更简单）
            return canPartitionBacktrack(nums);
        } else if (target <= 10000) {
            // 中等规模使用一维DP
            return canPartitionDP1D(nums);
        } else {
            // 大规模数据使用bitset优化
            return canPartitionBitsetOptimized(nums);
        }
    }
    
    /**
     * 工程化改进版本：完整的异常处理和验证
     */
    public static boolean canPartitionWithValidation(int[] nums) {
        try {
            // 输入验证
            if (nums == null) {
                throw new IllegalArgumentException("Input array cannot be null");
            }
            if (nums.length == 0) {
                return false;
            }
            
            // 检查元素是否为正整数
            for (int num : nums) {
                if (num <= 0) {
                    throw new IllegalArgumentException("All elements must be positive integers");
                }
            }
            
            return canPartitionOptimized(nums);
            
        } catch (Exception e) {
            System.err.println("Error in canPartition: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 单元测试方法
     */
    public static void runTests() {
        System.out.println("=== LeetCode 416 Partition Equal Subset Sum - 单元测试 ===");
        
        // 测试用例1: 可以平分
        int[] nums1 = {1, 5, 11, 5};
        boolean expected1 = true;
        boolean result1 = canPartitionOptimized(nums1);
        System.out.printf("测试1: %s, 期望=%b, 实际=%b, %s%n",
                         Arrays.toString(nums1), expected1, result1,
                         result1 == expected1 ? "通过" : "失败");
        
        // 测试用例2: 不能平分（总和为奇数）
        int[] nums2 = {1, 2, 3, 5};
        boolean expected2 = false;
        boolean result2 = canPartitionOptimized(nums2);
        System.out.printf("测试2: %s, 期望=%b, 实际=%b, %s%n",
                         Arrays.toString(nums2), expected2, result2,
                         result2 == expected2 ? "通过" : "失败");
        
        // 测试用例3: 边界情况（空数组）
        int[] nums3 = {};
        boolean expected3 = false;
        boolean result3 = canPartitionOptimized(nums3);
        System.out.printf("测试3: %s, 期望=%b, 实际=%b, %s%n",
                         Arrays.toString(nums3), expected3, result3,
                         result3 == expected3 ? "通过" : "失败");
        
        // 测试不同方法的结果一致性
        System.out.println("\n=== 方法一致性测试 ===");
        int[] testNums = {1, 5, 10, 6};
        boolean r1 = canPartitionDP2D(testNums);
        boolean r2 = canPartitionDP1D(testNums);
        boolean r3 = canPartitionBitset(testNums);
        boolean r4 = canPartitionOptimized(testNums);
        
        System.out.printf("二维DP: %b%n", r1);
        System.out.printf("一维DP: %b%n", r2);
        System.out.printf("Bitset: %b%n", r3);
        System.out.printf("优化法: %b%n", r4);
        System.out.printf("所有方法结果一致: %s%n", 
                         (r1 == r2 && r2 == r3 && r3 == r4) ? "是" : "否");
    }
    
    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");
        
        // 生成测试数据
        int[][] testCases = {
            generateRandomArray(20, 100),    // 小规模
            generateRandomArray(100, 100),   // 中等规模
            generateRandomArray(200, 100)    // 较大规模
        };
        
        for (int i = 0; i < testCases.length; i++) {
            int[] nums = testCases[i];
            System.out.printf("测试用例 %d: 数组长度=%d%n", i + 1, nums.length);
            
            // 测试一维DP
            long startTime = System.nanoTime();
            boolean result1 = canPartitionDP1D(nums);
            long time1 = System.nanoTime() - startTime;
            System.out.printf("  一维DP: %d ns, 结果: %b%n", time1, result1);
            
            // 测试Bitset优化
            startTime = System.nanoTime();
            boolean result2 = canPartitionBitsetOptimized(nums);
            long time2 = System.nanoTime() - startTime;
            System.out.printf("  Bitset优化: %d ns, 结果: %b%n", time2, result2);
            
            if (time1 > 0) {
                double ratio = (double) time1 / time2;
                System.out.printf("  Bitset比一维DP快: %.2f倍%n", ratio);
            }
            System.out.println();
        }
    }
    
    /**
     * 生成随机数组用于测试
     */
    private static int[] generateRandomArray(int size, int maxValue) {
        Random random = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(maxValue) + 1;  // 生成1到maxValue的随机数
        }
        return arr;
    }
    
    /**
     * 复杂度分析
     */
    public static void complexityAnalysis() {
        System.out.println("=== 复杂度分析 ===");
        System.out.println("方法1（回溯法）:");
        System.out.println("  时间复杂度: O(2^N) - 指数级，仅适用于N <= 20");
        System.out.println("  空间复杂度: O(N) - 递归栈空间");
        
        System.out.println("\n方法2（二维DP）:");
        System.out.println("  时间复杂度: O(N * sum) - 伪多项式时间");
        System.out.println("  空间复杂度: O(N * sum) - 二维数组");
        
        System.out.println("\n方法3（一维DP）:");
        System.out.println("  时间复杂度: O(N * sum)");
        System.out.println("  空间复杂度: O(sum) - 一维数组");
        
        System.out.println("\n方法4（Bitset优化）:");
        System.out.println("  时间复杂度: O(N * sum/32) - 常数优化");
        System.out.println("  空间复杂度: O(sum/32) - bitset空间");
        
        System.out.println("\n工程化建议:");
        System.out.println("1. 对于小规模数据（N <= 20），回溯法更简单");
        System.out.println("2. 对于中等规模，一维DP是平衡的选择");
        System.out.println("3. 对于大规模数据，必须使用bitset优化");
        System.out.println("4. 注意提前终止优化：一旦找到目标就返回");
    }
    
    public static void main(String[] args) {
        System.out.println("LeetCode 416 Partition Equal Subset Sum - 子集和问题");
        System.out.println("判断是否可以将数组分割成两个和相等的子集");
        
        // 运行单元测试
        runTests();
        
        // 运行性能测试
        performanceTest();
        
        // 复杂度分析
        complexityAnalysis();
        
        // 示例使用
        System.out.println("\n=== 示例使用 ===");
        int[][] examples = {
            {1, 5, 11, 5},
            {1, 2, 3, 5},
            {1, 2, 3, 4, 5, 6, 7}
        };
        
        for (int[] nums : examples) {
            boolean result = canPartitionWithValidation(nums);
            System.out.printf("数组 %s 是否可以平分: %b%n", Arrays.toString(nums), result);
        }
    }
}