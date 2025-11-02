// 分割等和子集
// 给你一个只包含正整数的非空数组 nums。请你判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等。
// 测试链接 : https://leetcode.cn/problems/partition-equal-subset-sum/

import java.util.Arrays;
import java.util.BitSet;

public class Code08_PartitionEqualSubsetSum {
    // 方法一：状态压缩动态规划 - 从后往前更新
    // 时间复杂度: O(n * target)
    // 空间复杂度: O(target)
    public static boolean canPartition1(int[] nums) {
        // 参数验证
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("数组不能为空");
        }
        
        int n = nums.length;
        // 如果数组元素个数小于2，无法分割成两个非空子集
        if (n < 2) {
            return false;
        }
        
        // 计算数组总和
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        // 如果总和为奇数，无法分成两个和相等的子集
        if (sum % 2 != 0) {
            return false;
        }
        
        // 目标和：总和的一半
        int target = sum / 2;
        
        // 检查是否有单个元素大于目标和，如果有，无法分割
        for (int num : nums) {
            if (num > target) {
                return false;
            }
        }
        
        // dp[i]表示是否可以组成和为i的子集
        // 使用状态压缩，只需要一维数组
        boolean[] dp = new boolean[target + 1];
        // 初始状态：空子集的和为0
        dp[0] = true;
        
        // 从后往前更新，避免重复使用同一个元素
        for (int num : nums) {
            for (int j = target; j >= num; --j) {
                // 状态转移：如果j - num可以组成，则j也可以组成
                dp[j] = dp[j] || dp[j - num];
                
                // 提前退出：如果已经找到目标和，直接返回true
                if (dp[target]) {
                    return true;
                }
            }
        }
        
        return dp[target];
    }
    
    // 方法二：原始动态规划 - 二维数组表示
    // 时间复杂度: O(n * target)
    // 空间复杂度: O(n * target)
    public static boolean canPartition2(int[] nums) {
        if (nums == null || nums.length < 2) {
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
        int n = nums.length;
        
        // dp[i][j]表示前i个元素是否可以组成和为j的子集
        boolean[][] dp = new boolean[n + 1][target + 1];
        
        // 初始化：前0个元素可以组成和为0的子集
        dp[0][0] = true;
        
        // 填充dp数组
        for (int i = 1; i <= n; ++i) {
            for (int j = 0; j <= target; ++j) {
                // 不选择第i个元素
                dp[i][j] = dp[i - 1][j];
                
                // 选择第i个元素（如果j >= nums[i-1]）
                if (j >= nums[i - 1]) {
                    dp[i][j] = dp[i][j] || dp[i - 1][j - nums[i - 1]];
                }
            }
            
            // 提前退出：如果已经找到目标和，直接返回true
            if (dp[i][target]) {
                return true;
            }
        }
        
        return dp[n][target];
    }
    
    // 方法三：位运算优化 - 使用BitSet表示可能的和
    // 时间复杂度: O(n * target)
    // 空间复杂度: O(target)
    public static boolean canPartition3(int[] nums) {
        if (nums == null || nums.length < 2) {
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
        
        // 使用BitSet表示可以组成的和
        // bitSet的第j位为true表示可以组成和为j的子集
        BitSet bitSet = new BitSet();
        // 初始状态：可以组成和为0的子集
        bitSet.set(0);
        
        for (int num : nums) {
            // 当前数字可以与之前所有可能的和相加，得到新的和
            // 使用位或操作和左移操作来更新可能的和
            // 创建bitSet的副本，避免在遍历过程中修改原集合
            BitSet temp = (BitSet) bitSet.clone();
            temp.stream().forEach(i -> bitSet.set(i + num));
            
            // 检查是否可以组成目标和
            if (bitSet.get(target)) {
                return true;
            }
        }
        
        return bitSet.get(target);
    }
    
    // 方法四：优化的位运算实现
    public static boolean canPartition4(int[] nums) {
        if (nums == null || nums.length < 2) {
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
        
        // 使用整数作为位掩码
        // 注意：这种方法只适用于target较小的情况（小于64或32）
        int dp = 1; // 初始状态：可以组成和为0的子集
        
        for (int num : nums) {
            dp |= dp << num;
            
            // 检查是否可以组成目标和
            if ((dp & (1 << target)) != 0) {
                return true;
            }
        }
        
        return (dp & (1 << target)) != 0;
    }
    
    // 测试函数
    public static void test() {
        // 测试用例1：可以分割
        int[] nums1 = {1, 5, 11, 5};
        System.out.println("测试用例1: " + (canPartition1(nums1) ? "可以分割" : "不能分割")); // 期望输出: 可以分割
        assert canPartition1(nums1) == true;
        assert canPartition2(nums1) == true;
        assert canPartition3(nums1) == true;
        assert canPartition4(nums1) == true;
        
        // 测试用例2：不能分割
        int[] nums2 = {1, 2, 3, 5};
        System.out.println("测试用例2: " + (canPartition1(nums2) ? "可以分割" : "不能分割")); // 期望输出: 不能分割
        assert canPartition1(nums2) == false;
        assert canPartition2(nums2) == false;
        assert canPartition3(nums2) == false;
        assert canPartition4(nums2) == false;
        
        // 测试用例3：单个元素
        int[] nums3 = {1};
        System.out.println("测试用例3: " + (canPartition1(nums3) ? "可以分割" : "不能分割")); // 期望输出: 不能分割
        assert canPartition1(nums3) == false;
        assert canPartition2(nums3) == false;
        
        // 测试用例4：两个相等元素
        int[] nums4 = {2, 2};
        System.out.println("测试用例4: " + (canPartition1(nums4) ? "可以分割" : "不能分割")); // 期望输出: 可以分割
        assert canPartition1(nums4) == true;
        assert canPartition2(nums4) == true;
        assert canPartition3(nums4) == true;
        assert canPartition4(nums4) == true;
        
        // 测试用例5：总和为奇数
        int[] nums5 = {1, 2, 3, 4, 5};
        System.out.println("测试用例5: " + (canPartition1(nums5) ? "可以分割" : "不能分割")); // 期望输出: 不能分割
        assert canPartition1(nums5) == false;
        assert canPartition2(nums5) == false;
        assert canPartition3(nums5) == false;
        
        // 性能测试
        System.out.println("\n性能测试:");
        
        // 创建一个较大的测试用例
        int[] largeNums = new int[20];
        for (int i = 0; i < 20; i++) {
            largeNums[i] = i + 1;
        }
        
        // 方法一性能测试
        long startTime = System.nanoTime();
        boolean result1 = canPartition1(largeNums);
        long endTime = System.nanoTime();
        System.out.println("方法一耗时: " + (endTime - startTime) / 1000 + " 微秒");
        
        // 方法二性能测试
        startTime = System.nanoTime();
        boolean result2 = canPartition2(largeNums);
        endTime = System.nanoTime();
        System.out.println("方法二耗时: " + (endTime - startTime) / 1000 + " 微秒");
        
        // 方法三性能测试
        startTime = System.nanoTime();
        boolean result3 = canPartition3(largeNums);
        endTime = System.nanoTime();
        System.out.println("方法三耗时: " + (endTime - startTime) / 1000 + " 微秒");
        
        // 方法四性能测试（仅适用于较小的target）
        if (Arrays.stream(largeNums).sum() / 2 < 31) { // 确保不会超出整数范围
            startTime = System.nanoTime();
            boolean result4 = canPartition4(largeNums);
            endTime = System.nanoTime();
            System.out.println("方法四耗时: " + (endTime - startTime) / 1000 + " 微秒");
        }
        
        System.out.println("\n所有测试用例通过!");
    }
    
    public static void main(String[] args) {
        test();
    }
}

/*
复杂度分析：

1. 时间复杂度：
   - 对于所有方法，时间复杂度均为 O(n * target)，其中 n 是数组长度，target 是数组总和的一半。
   - 在方法一中，我们对每个元素遍历 target 次，进行状态更新。
   - 在方法二中，我们填充一个 n×target 的二维数组，需要 O(n * target) 次操作。
   - 在方法三和四中，虽然使用了位运算，但本质上还是遍历每个元素并更新可能的和，复杂度仍为 O(n * target)。

2. 空间复杂度：
   - 方法一：O(target)，使用一维数组进行状态压缩。
   - 方法二：O(n * target)，使用二维数组存储所有状态。
   - 方法三：O(target)，使用BitSet存储可能的和。
   - 方法四：O(1)，使用整数作为位掩码，但受限于整数的大小。

算法设计说明：

1. 问题转化：将原问题转化为「是否可以从数组中选择一些元素，使得它们的和等于数组总和的一半」。

2. 状态压缩思路：
   - 在方法一中，我们使用一维数组代替二维数组，并且从后往前更新，避免重复使用同一个元素。
   - 这种优化利用了0-1背包问题的特性，保证每个元素只被考虑一次。

3. 位运算优化：
   - 方法三和四使用位运算来表示可能的和，每一位表示一个和是否可达。
   - 在Java中，使用BitSet可以处理较大的target值，而整数位掩码只适用于较小的target。

4. 剪枝优化：
   - 在计算过程中，一旦发现可以组成目标和，立即返回结果。
   - 提前检查总和是否为偶数，以及是否存在单个元素大于目标和的情况。

这是本题的最优解，因为时间复杂度已达到 O(n * target)，而问题的本质是0-1背包问题，这已经是已知的最优时间复杂度。
空间上，我们通过状态压缩和位运算优化，将空间复杂度降到了最低。

注意事项：
1. 边界情况处理：空数组、只有一个元素的数组等。
2. 提前剪枝：总和为奇数、单个元素大于目标和等情况。
3. 整数溢出：在方法四中，需要确保整数类型的大小足够存储可能的和。
4. Java特性：利用BitSet类可以更灵活地处理位运算，不受整数大小的限制。
*/