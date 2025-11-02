// 删除并获得点数 (Delete and Earn)
// 给你一个整数数组 nums ，你可以对它进行一些操作。
// 每次操作中，选择任意一个 nums[i] ，删除它并获得 nums[i] 的点数。
// 之后，你必须删除所有等于 nums[i] - 1 和 nums[i] + 1 的元素。
// 开始你拥有 0 个点数。返回你能通过这些操作获得的最大点数。
// 测试链接 : https://leetcode.cn/problems/delete-and-earn/

package class066;

import java.util.Arrays;

/**
 * 删除并获得点数 - 打家劫舍问题的变种
 * 时间复杂度分析：
 * - 预处理：O(n + k) 其中n是数组长度，k是最大值
 * - 动态规划：O(k) 其中k是数组中的最大值
 * - 总体：O(n + k)
 * 
 * 空间复杂度分析：
 * - 计数数组：O(k)
 * - dp数组：O(k) 或 O(1)（空间优化版本）
 * 
 * 工程化考量：
 * 1. 问题转化：将问题转化为打家劫舍问题
 * 2. 边界处理：空数组、单元素数组等
 * 3. 性能优化：空间优化版本应对大规模数据
 * 4. 代码清晰：明确的变量命名和状态转移逻辑
 */
public class Code22_DeleteAndEarn {

    // 方法1：动态规划（转化为打家劫舍问题）
    // 时间复杂度：O(n + k) - n为数组长度，k为最大值
    // 空间复杂度：O(k) - 计数数组和dp数组
    // 核心思路：将问题转化为不能选择相邻数字的打家劫舍问题
    public static int deleteAndEarn1(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        
        // 找到数组中的最大值
        int maxVal = 0;
        for (int num : nums) {
            maxVal = Math.max(maxVal, num);
        }
        
        // 创建计数数组，统计每个数字出现的总点数
        int[] sum = new int[maxVal + 1];
        for (int num : nums) {
            sum[num] += num;
        }
        
        // 转化为打家劫舍问题：不能选择相邻的数字
        return robHouse(sum);
    }
    
    // 打家劫舍问题的解决方案
    private static int robHouse(int[] sum) {
        int n = sum.length;
        if (n == 1) return sum[0];
        
        int[] dp = new int[n];
        dp[0] = sum[0];
        dp[1] = Math.max(sum[0], sum[1]);
        
        for (int i = 2; i < n; i++) {
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + sum[i]);
        }
        
        return dp[n - 1];
    }

    // 方法2：空间优化的动态规划
    // 时间复杂度：O(n + k) - 与方法1相同
    // 空间复杂度：O(k) - 只使用计数数组，dp使用常数空间
    // 优化：使用滚动数组减少空间使用
    public static int deleteAndEarn2(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        
        int maxVal = 0;
        for (int num : nums) {
            maxVal = Math.max(maxVal, num);
        }
        
        int[] sum = new int[maxVal + 1];
        for (int num : nums) {
            sum[num] += num;
        }
        
        return robHouseOptimized(sum);
    }
    
    private static int robHouseOptimized(int[] sum) {
        int n = sum.length;
        if (n == 1) return sum[0];
        
        int prev2 = sum[0];  // dp[i-2]
        int prev1 = Math.max(sum[0], sum[1]);  // dp[i-1]
        
        for (int i = 2; i < n; i++) {
            int current = Math.max(prev1, prev2 + sum[i]);
            prev2 = prev1;
            prev1 = current;
        }
        
        return prev1;
    }

    // 方法3：使用TreeMap优化空间（当数字范围很大但实际数字很少时）
    // 时间复杂度：O(n log n) - 排序和遍历
    // 空间复杂度：O(n) - TreeMap存储
    // 核心思路：当数字范围很大但实际出现的数字很少时，避免创建大数组
    public static int deleteAndEarn3(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        
        // 统计每个数字的总点数
        java.util.TreeMap<Integer, Integer> map = new java.util.TreeMap<>();
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + num);
        }
        
        // 如果没有数字，返回0
        if (map.isEmpty()) return 0;
        
        // 将数字按顺序排列
        int[] keys = new int[map.size()];
        int index = 0;
        for (int key : map.keySet()) {
            keys[index++] = key;
        }
        
        // 动态规划处理
        int n = keys.length;
        int[] dp = new int[n];
        dp[0] = map.get(keys[0]);
        
        for (int i = 1; i < n; i++) {
            int currentKey = keys[i];
            int currentValue = map.get(currentKey);
            
            if (currentKey == keys[i - 1] + 1) {
                // 当前数字与前一个数字相邻
                if (i >= 2) {
                    dp[i] = Math.max(dp[i - 1], dp[i - 2] + currentValue);
                } else {
                    dp[i] = Math.max(dp[i - 1], currentValue);
                }
            } else {
                // 当前数字与前一个数字不相邻
                dp[i] = dp[i - 1] + currentValue;
            }
        }
        
        return dp[n - 1];
    }

    // 方法4：记忆化搜索（自顶向下）
    // 时间复杂度：O(n + k) - 与方法1相同
    // 空间复杂度：O(k) - 递归栈和记忆化数组
    // 核心思路：递归解决，使用记忆化避免重复计算
    public static int deleteAndEarn4(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        
        int maxVal = 0;
        for (int num : nums) {
            maxVal = Math.max(maxVal, num);
        }
        
        int[] sum = new int[maxVal + 1];
        for (int num : nums) {
            sum[num] += num;
        }
        
        int[] memo = new int[maxVal + 1];
        Arrays.fill(memo, -1);
        return dfs(sum, 1, memo);
    }
    
    private static int dfs(int[] sum, int i, int[] memo) {
        if (i >= sum.length) return 0;
        if (memo[i] != -1) return memo[i];
        
        // 选择1：不取当前数字，考虑下一个
        int skip = dfs(sum, i + 1, memo);
        // 选择2：取当前数字，跳过下一个（相邻数字）
        int take = sum[i] + dfs(sum, i + 2, memo);
        
        memo[i] = Math.max(skip, take);
        return memo[i];
    }

    // 方法5：暴力递归（用于对比）
    // 时间复杂度：O(2^n) - 指数级，效率极低
    // 空间复杂度：O(n) - 递归调用栈深度
    // 问题：存在大量重复计算，仅用于教学目的
    public static int deleteAndEarn5(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        return dfsBruteForce(nums, 0);
    }
    
    private static int dfsBruteForce(int[] nums, int index) {
        if (index >= nums.length) return 0;
        
        // 选择当前数字
        int current = nums[index];
        int takeCurrent = current;
        
        // 跳过所有current-1和current+1的数字
        java.util.ArrayList<Integer> remaining = new java.util.ArrayList<>();
        for (int i = index + 1; i < nums.length; i++) {
            if (nums[i] != current - 1 && nums[i] != current + 1) {
                remaining.add(nums[i]);
            }
        }
        
        int[] remainingArray = new int[remaining.size()];
        for (int i = 0; i < remaining.size(); i++) {
            remainingArray[i] = remaining.get(i);
        }
        
        takeCurrent += dfsBruteForce(remainingArray, 0);
        
        // 不选择当前数字
        int skipCurrent = dfsBruteForce(nums, index + 1);
        
        return Math.max(takeCurrent, skipCurrent);
    }

    // 全面的测试用例
    public static void main(String[] args) {
        System.out.println("=== 删除并获得点数测试 ===");
        
        // 边界测试
        testCase(new int[]{}, 0, "空数组");
        testCase(new int[]{5}, 5, "单元素数组");
        testCase(new int[]{3, 3}, 6, "重复元素");
        
        // LeetCode示例测试
        testCase(new int[]{3, 4, 2}, 6, "示例1");
        testCase(new int[]{2, 2, 3, 3, 3, 4}, 9, "示例2");
        testCase(new int[]{1, 1, 1, 2}, 3, "示例3");
        
        // 常规测试
        testCase(new int[]{1, 2, 3, 4, 5}, 9, "连续数字");
        testCase(new int[]{5, 5, 5, 5, 5}, 25, "全部相同");
        testCase(new int[]{1, 3, 5, 7, 9}, 25, "间隔数字");
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        int[] largeNums = new int[1000];
        for (int i = 0; i < largeNums.length; i++) {
            largeNums[i] = (i % 50) + 1;  // 1-50的循环数字
        }
        
        long start = System.currentTimeMillis();
        int result1 = deleteAndEarn1(largeNums);
        long end = System.currentTimeMillis();
        System.out.println("方法1: " + result1 + ", 耗时: " + (end - start) + "ms");
        
        start = System.currentTimeMillis();
        int result2 = deleteAndEarn2(largeNums);
        end = System.currentTimeMillis();
        System.out.println("方法2: " + result2 + ", 耗时: " + (end - start) + "ms");
        
        // 暴力方法太慢，不测试
        System.out.println("暴力方法在n=1000时太慢，跳过测试");
    }
    
    private static void testCase(int[] nums, int expected, String description) {
        int result1 = deleteAndEarn1(nums);
        int result2 = deleteAndEarn2(nums);
        int result3 = deleteAndEarn3(nums);
        int result4 = deleteAndEarn4(nums);
        
        boolean allCorrect = (result1 == expected && result2 == expected && 
                            result3 == expected && result4 == expected);
        
        System.out.println(description + ": " + (allCorrect ? "✓" : "✗"));
        if (!allCorrect) {
            System.out.println("  方法1: " + result1 + " | 方法2: " + result2 + 
                             " | 方法3: " + result3 + " | 方法4: " + result4 + 
                             " | 预期: " + expected);
        }
    }
    
    /**
     * 算法总结与工程化思考：
     * 
     * 1. 问题本质：打家劫舍问题的变种
     *    - 关键洞察：选择某个数字时，不能选择其相邻数字（num-1和num+1）
     *    - 转化思路：统计每个数字的总点数，转化为不能选择相邻数字的问题
     * 
     * 2. 时间复杂度对比：
     *    - 暴力递归：O(2^n) - 不可接受
     *    - 记忆化搜索：O(n + k) - 可接受
     *    - 动态规划：O(n + k) - 推荐
     *    - 空间优化：O(n + k) - 工程首选
     * 
     * 3. 空间复杂度对比：
     *    - 暴力递归：O(n) - 栈深度
     *    - 记忆化搜索：O(k) - 递归栈+缓存
     *    - 动态规划：O(k) - 数组存储
     *    - 空间优化：O(k) - 计数数组（无法避免）
     * 
     * 4. 特殊情况处理：
     *    - 数字范围很大但实际数字很少：使用方法3（TreeMap）
     *    - 数字范围小但重复多：使用方法1或2
     *    - 极端情况：全相同数字或连续数字
     * 
     * 5. 工程选择依据：
     *    - 一般情况：方法2（空间优化）
     *    - 数字范围大但实际少：方法3（TreeMap）
     *    - 需要递归思路：方法4（记忆化搜索）
     * 
     * 6. 调试技巧：
     *    - 验证计数数组的正确性
     *    - 检查状态转移逻辑
     *    - 边界测试确保正确性
     * 
     * 7. 关联题目：
     *    - 打家劫舍I、II（基础版本）
     *    - 最大子序列和问题
     *    - 不相邻元素最大和问题
     * 
     * 8. 优化思路：
     *    - 预处理阶段优化计数统计
     *    - 动态规划阶段使用空间优化
     *    - 针对数据特点选择合适算法
     */
}