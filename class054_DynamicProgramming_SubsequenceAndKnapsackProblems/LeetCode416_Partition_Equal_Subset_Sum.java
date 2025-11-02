package class086;

// LeetCode 416. 分割等和子集
// 给你一个只包含正整数的非空数组 nums 。请你判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等。
// 测试链接 : https://leetcode.cn/problems/partition-equal-subset-sum/

/**
 * 算法详解：分割等和子集（LeetCode 416）
 * 
 * 问题描述：
 * 给定一个只包含正整数的非空数组nums，判断是否可以将数组分割成两个子集，使得两个子集的元素和相等。
 * 
 * 算法思路：
 * 1. 转化为0-1背包问题：寻找子集使得和为总和的二分之一
 * 2. 动态规划：dp[i][j]表示前i个元素能否组成和为j
 * 3. 空间优化：使用一维数组进行状态压缩
 * 
 * 时间复杂度分析：
 * 1. 计算总和：O(n)
 * 2. 填充dp表：O(n * target)，其中target = sum/2
 * 3. 总体时间复杂度：O(n * target)
 * 
 * 空间复杂度分析：
 * 1. 基础版本：O(n * target)
 * 2. 空间优化版本：O(target)
 * 3. 总体空间复杂度：O(n * target) 或 O(target)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入数组是否为空
 * 2. 边界处理：总和为奇数的情况直接返回false
 * 3. 性能优化：使用位运算优化空间
 * 4. 代码可读性：清晰的变量命名和注释
 * 
 * 极端场景验证：
 * 1. 输入数组为空的情况
 * 2. 总和为奇数的情况
 * 3. 单个元素数组的情况
 * 4. 大规模数组的性能测试
 */
public class LeetCode416_Partition_Equal_Subset_Sum {
    
    /**
     * 基础动态规划解法（0-1背包问题）
     * 时间复杂度：O(n * target)
     * 空间复杂度：O(n * target)
     * 
     * 算法思想：
     * 将问题转化为0-1背包问题：从n个物品中选择一些物品，使得它们的总重量等于背包容量target。
     */
    public static boolean canPartition(int[] nums) {
        // 异常处理
        if (nums == null || nums.length == 0) {
            return false;
        }
        
        int n = nums.length;
        
        // 计算数组总和
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        // 如果总和是奇数，不可能分割成两个和相等的子集
        if (sum % 2 != 0) {
            return false;
        }
        
        int target = sum / 2;
        
        // 特殊情况：如果最大元素大于target，直接返回false
        int maxNum = 0;
        for (int num : nums) {
            maxNum = Math.max(maxNum, num);
        }
        if (maxNum > target) {
            return false;
        }
        
        // dp[i][j]表示前i个元素能否组成和为j
        boolean[][] dp = new boolean[n + 1][target + 1];
        
        // 初始化：前0个元素组成和为0是可能的
        dp[0][0] = true;
        
        // 填充dp表
        for (int i = 1; i <= n; i++) {
            int num = nums[i - 1];
            for (int j = 0; j <= target; j++) {
                if (j < num) {
                    // 当前元素大于j，不能选择
                    dp[i][j] = dp[i - 1][j];
                } else {
                    // 可以选择当前元素或不选择
                    dp[i][j] = dp[i - 1][j] || dp[i - 1][j - num];
                }
            }
        }
        
        return dp[n][target];
    }
    
    /**
     * 空间优化版本（使用一维数组）
     * 时间复杂度：O(n * target)
     * 空间复杂度：O(target)
     * 
     * 优化思路：
     * 观察状态转移方程，dp[i][j]只依赖于dp[i-1][j]和dp[i-1][j-num]
     * 可以使用一维数组，从后往前更新避免覆盖。
     */
    public static boolean canPartitionOptimized(int[] nums) {
        if (nums == null || nums.length == 0) {
            return false;
        }
        
        int n = nums.length;
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        if (sum % 2 != 0) {
            return false;
        }
        
        int target = sum / 2;
        
        // 检查最大元素
        int maxNum = 0;
        for (int num : nums) {
            maxNum = Math.max(maxNum, num);
        }
        if (maxNum > target) {
            return false;
        }
        
        // 使用一维数组
        boolean[] dp = new boolean[target + 1];
        dp[0] = true; // 和为0总是可能的
        
        // 遍历每个元素
        for (int num : nums) {
            // 从后往前更新，避免重复使用同一个元素
            for (int j = target; j >= num; j--) {
                dp[j] = dp[j] || dp[j - num];
            }
            
            // 提前终止：如果已经找到target，直接返回
            if (dp[target]) {
                return true;
            }
        }
        
        return dp[target];
    }
    
    /**
     * 位运算优化版本（进一步空间优化）
     * 时间复杂度：O(n * target)
     * 空间复杂度：O(target/32) ≈ O(target)
     * 
     * 优化思路：
     * 使用位运算表示状态，每个bit表示一个和是否可达。
     * 这种方法可以进一步减少内存使用。
     */
    public static boolean canPartitionBitMask(int[] nums) {
        if (nums == null || nums.length == 0) {
            return false;
        }
        
        int n = nums.length;
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        if (sum % 2 != 0) {
            return false;
        }
        
        int target = sum / 2;
        
        // 检查最大元素
        int maxNum = 0;
        for (int num : nums) {
            maxNum = Math.max(maxNum, num);
        }
        if (maxNum > target) {
            return false;
        }
        
        // 使用位掩码表示可达的和
        // 每个bit表示一个和是否可达，bits[i]的第j位表示和j是否可达
        int[] bits = new int[target / 32 + 1];
        bits[0] = 1; // 和为0可达
        
        for (int num : nums) {
            // 创建新的位掩码，表示加上当前元素后的可达状态
            int[] newBits = bits.clone();
            
            // 更新位掩码
            for (int i = 0; i < bits.length; i++) {
                if (bits[i] != 0) {
                    // 将当前位掩码左移num位，表示加上num后的新状态
                    int shift = num;
                    int newIndex = i + (shift / 32);
                    shift %= 32;
                    
                    if (newIndex < newBits.length) {
                        newBits[newIndex] |= (bits[i] << shift);
                        if (shift > 0 && newIndex + 1 < newBits.length) {
                            newBits[newIndex + 1] |= (bits[i] >>> (32 - shift));
                        }
                    }
                }
            }
            
            bits = newBits;
            
            // 检查target是否可达
            int targetIndex = target / 32;
            int targetBit = target % 32;
            if (targetIndex < bits.length && (bits[targetIndex] & (1 << targetBit)) != 0) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 优化的位运算版本（更简洁的实现）
     * 时间复杂度：O(n * target)
     * 空间复杂度：O(target/32)
     */
    public static boolean canPartitionBitMaskSimple(int[] nums) {
        if (nums == null || nums.length == 0) {
            return false;
        }
        
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        if (sum % 2 != 0) {
            return false;
        }
        
        int target = sum / 2;
        
        // 使用long类型的位掩码（支持更大的target）
        long bitmask = 1L; // 初始状态：和为0可达
        
        for (int num : nums) {
            // 将当前位掩码左移num位，然后与原始位掩码进行或操作
            bitmask |= (bitmask << num);
            
            // 检查target是否可达
            if ((bitmask & (1L << target)) != 0) {
                return true;
            }
        }
        
        return (bitmask & (1L << target)) != 0;
    }
    
    /**
     * 单元测试方法
     * 验证算法的正确性和各种边界情况
     */
    public static void main(String[] args) {
        System.out.println("=== LeetCode 416 分割等和子集测试 ===\n");
        
        // 测试用例1：基本功能测试
        testCase("测试用例1 - 基本功能", new int[]{1, 5, 11, 5}, true);
        
        // 测试用例2：LeetCode官方示例
        testCase("测试用例2 - 官方示例", new int[]{1, 2, 3, 5}, false);
        
        // 测试用例3：总和为奇数
        testCase("测试用例3 - 总和奇数", new int[]{1, 2, 3, 4, 5}, false);
        
        // 测试用例4：单个元素
        testCase("测试用例4 - 单个元素", new int[]{1}, false);
        
        // 测试用例5：空数组
        testCase("测试用例5 - 空数组", new int[]{}, false);
        
        // 测试用例6：两个相同元素
        testCase("测试用例6 - 两个相同", new int[]{2, 2}, true);
        
        // 测试用例7：复杂情况
        testCase("测试用例7 - 复杂情况", new int[]{1, 2, 3, 4, 5, 6, 7}, true);
        
        // 性能测试
        performanceTest();
    }
    
    /**
     * 测试用例辅助方法
     */
    private static void testCase(String description, int[] nums, boolean expected) {
        System.out.println(description);
        System.out.println("输入数组: " + java.util.Arrays.toString(nums));
        System.out.println("期望结果: " + expected);
        
        boolean result1 = canPartition(nums);
        boolean result2 = canPartitionOptimized(nums);
        boolean result3 = canPartitionBitMaskSimple(nums);
        
        System.out.println("基础DP: " + result1 + " " + (result1 == expected ? "✓" : "✗"));
        System.out.println("优化DP: " + result2 + " " + (result2 == expected ? "✓" : "✗"));
        System.out.println("位运算: " + result3 + " " + (result3 == expected ? "✓" : "✗"));
        
        if (result1 == result2 && result2 == result3 && result1 == expected) {
            System.out.println("测试通过 ✓\n");
        } else {
            System.out.println("测试失败 ✗\n");
        }
    }
    
    /**
     * 性能测试方法
     * 测试算法在大规模数据下的表现
     */
    private static void performanceTest() {
        System.out.println("=== 性能测试 ===");
        
        // 生成测试数据：中等规模数组
        int n = 100;
        int[] nums = new int[n];
        java.util.Random random = new java.util.Random();
        
        // 生成随机数组（总和为偶数）
        int sum = 0;
        for (int i = 0; i < n; i++) {
            nums[i] = random.nextInt(100) + 1;
            sum += nums[i];
        }
        
        // 确保总和为偶数
        if (sum % 2 != 0) {
            nums[0]++; // 调整第一个元素使总和为偶数
        }
        
        System.out.println("测试数据规模: " + n + "个元素");
        System.out.println("目标总和: " + (sum / 2));
        
        // 测试优化DP算法
        long startTime = System.currentTimeMillis();
        boolean result1 = canPartitionOptimized(nums);
        long endTime = System.currentTimeMillis();
        System.out.println("优化DP算法:");
        System.out.println("  结果: " + result1);
        System.out.println("  耗时: " + (endTime - startTime) + "ms");
        
        // 测试位运算算法
        startTime = System.currentTimeMillis();
        boolean result2 = canPartitionBitMaskSimple(nums);
        endTime = System.currentTimeMillis();
        System.out.println("位运算算法:");
        System.out.println("  结果: " + result2);
        System.out.println("  耗时: " + (endTime - startTime) + "ms");
        
        // 验证结果一致性
        if (result1 == result2) {
            System.out.println("结果一致性验证: 通过 ✓");
        } else {
            System.out.println("结果一致性验证: 失败 ✗");
        }
    }
    
    /**
     * 复杂度分析详细计算：
     * 
     * 基础动态规划：
     * - 时间：计算总和O(n) + 填充dp表O(n * target) → O(n * target)
     * - 空间：二维dp数组大小n×target → O(n * target)
     * 
     * 空间优化版本：
     * - 时间：O(n * target)
     * - 空间：一维数组大小target → O(target)
     * - 最优解：是，综合性能最好
     * 
     * 位运算版本：
     * - 时间：O(n * target)
     * - 空间：O(target/32) ≈ O(target)
     * - 最优解：是，内存使用更少但代码更复杂
     * 
     * 工程选择依据：
     * 1. 对于小规模数据：任意方法都可
     * 2. 对于中等规模数据：优先选择空间优化版本
     * 3. 对于大规模数据：位运算版本可以处理更大的target
     * 
     * 算法调试技巧：
     * 1. 打印dp表观察填充过程
     * 2. 使用小规模测试用例验证正确性
     * 3. 添加断言验证关键假设
     * 4. 测试边界情况（空数组、单个元素等）
     */
}