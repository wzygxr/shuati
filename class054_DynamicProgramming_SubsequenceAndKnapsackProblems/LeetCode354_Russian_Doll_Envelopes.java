package class086;

import java.util.Arrays;
import java.util.Comparator;

// LeetCode 354. 俄罗斯套娃信封问题
// 给你一个二维整数数组 envelopes ，其中 envelopes[i] = [wi, hi] ，表示第 i 个信封的宽度和高度。
// 当另一个信封的宽度和高度都比这个信封大的时候，这个信封就可以放进另一个信封里，如同俄罗斯套娃一样。
// 请计算最多能有多少个信封能组成一组"俄罗斯套娃"信封（即可以把一个信封放到另一个信封里面）。
// 注意：不允许旋转信封。
// 测试链接 : https://leetcode.cn/problems/russian-doll-envelopes/

/**
 * 算法详解：俄罗斯套娃信封问题（LeetCode 354）
 * 
 * 问题描述：
 * 给定信封的宽度和高度数组，找到最多可以嵌套的信封数量。
 * 一个信封可以放入另一个信封当且仅当宽度和高度都严格大于。
 * 
 * 算法思路：
 * 1. 排序优化：先按宽度升序排序，宽度相同时按高度降序排序
 * 2. 转化为LIS问题：在高度序列上寻找最长递增子序列
 * 3. 贪心+二分查找：优化传统DP解法
 * 
 * 时间复杂度分析：
 * 1. 排序：O(n log n)
 * 2. LIS计算：O(n log n)
 * 3. 总体时间复杂度：O(n log n)
 * 
 * 空间复杂度分析：
 * 1. 排序：O(log n) 或 O(n)（取决于排序算法）
 * 2. LIS数组：O(n)
 * 3. 总体空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入数组是否为空
 * 2. 边界处理：单个信封的情况
 * 3. 性能优化：使用贪心+二分查找优化
 * 4. 代码可读性：清晰的排序逻辑和LIS实现
 * 
 * 极端场景验证：
 * 1. 输入数组为空的情况
 * 2. 单个信封的情况
 * 3. 所有信封尺寸相同的情况
 * 4. 大规模信封数组的性能测试
 */
public class LeetCode354_Russian_Doll_Envelopes {
    
    /**
     * 最优解法：排序 + LIS（贪心+二分查找）
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     * 
     * 算法思想：
     * 1. 先按宽度升序排序，宽度相同时按高度降序排序
     * 2. 这样可以将问题转化为在高度序列上寻找最长递增子序列
     * 3. 使用贪心+二分查找优化LIS计算
     */
    public static int maxEnvelopes(int[][] envelopes) {
        // 异常处理
        if (envelopes == null || envelopes.length == 0) {
            return 0;
        }
        
        int n = envelopes.length;
        
        // 特殊情况：单个信封
        if (n == 1) {
            return 1;
        }
        
        // 排序：按宽度升序，宽度相同时按高度降序
        // 这样保证在宽度相同的情况下，不会出现多个信封可以嵌套的情况
        Arrays.sort(envelopes, new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                if (a[0] == b[0]) {
                    // 宽度相同，按高度降序排列
                    return b[1] - a[1];
                } else {
                    // 宽度不同，按宽度升序排列
                    return a[0] - b[0];
                }
            }
        });
        
        // 在高度序列上寻找最长递增子序列
        int[] heights = new int[n];
        for (int i = 0; i < n; i++) {
            heights[i] = envelopes[i][1];
        }
        
        return lengthOfLIS(heights);
    }
    
    /**
     * 计算最长递增子序列长度（贪心+二分查找优化）
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     */
    private static int lengthOfLIS(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        int[] tails = new int[n]; // tails[i]表示长度为i+1的递增子序列的最小尾部值
        int len = 0; // 当前最长递增子序列的长度
        
        for (int num : nums) {
            // 使用二分查找找到num应该插入的位置
            int left = 0, right = len;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails[mid] < num) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            
            // 更新tails数组
            tails[left] = num;
            
            // 如果插入位置等于当前长度，说明找到了更长的子序列
            if (left == len) {
                len++;
            }
        }
        
        return len;
    }
    
    /**
     * 基础动态规划解法
     * 时间复杂度：O(n²)
     * 空间复杂度：O(n)
     * 
     * 算法思想：
     * 1. 先对信封进行排序（宽度升序，高度升序）
     * 2. 使用动态规划计算每个信封能嵌套的最大数量
     * 3. dp[i]表示以第i个信封结尾的最大嵌套数量
     */
    public static int maxEnvelopesDP(int[][] envelopes) {
        if (envelopes == null || envelopes.length == 0) {
            return 0;
        }
        
        int n = envelopes.length;
        
        // 排序：按宽度升序，宽度相同时按高度升序
        Arrays.sort(envelopes, new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                if (a[0] == b[0]) {
                    return a[1] - b[1];
                } else {
                    return a[0] - b[0];
                }
            }
        });
        
        // dp[i]表示以第i个信封结尾的最大嵌套数量
        int[] dp = new int[n];
        Arrays.fill(dp, 1); // 每个信封至少可以嵌套自己
        
        int maxCount = 1;
        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // 检查信封j是否可以放入信封i
                if (envelopes[i][0] > envelopes[j][0] && envelopes[i][1] > envelopes[j][1]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxCount = Math.max(maxCount, dp[i]);
        }
        
        return maxCount;
    }
    
    /**
     * 优化的动态规划解法（处理宽度相同的情况）
     * 时间复杂度：O(n²)
     * 空间复杂度：O(n)
     * 
     * 优化点：在宽度相同的情况下，只考虑高度较小的信封
     */
    public static int maxEnvelopesOptimizedDP(int[][] envelopes) {
        if (envelopes == null || envelopes.length == 0) {
            return 0;
        }
        
        int n = envelopes.length;
        
        // 排序：按宽度升序，宽度相同时按高度降序
        Arrays.sort(envelopes, new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                if (a[0] == b[0]) {
                    return b[1] - a[1]; // 高度降序
                } else {
                    return a[0] - b[0]; // 宽度升序
                }
            }
        });
        
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        
        int maxCount = 1;
        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // 由于排序时宽度相同的高度降序，所以只需要检查高度
                // 如果宽度相同，高度降序保证了不会出现错误嵌套
                if (envelopes[i][1] > envelopes[j][1]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxCount = Math.max(maxCount, dp[i]);
        }
        
        return maxCount;
    }
    
    /**
     * 单元测试方法
     * 验证算法的正确性和各种边界情况
     */
    public static void main(String[] args) {
        System.out.println("=== LeetCode 354 俄罗斯套娃信封问题测试 ===\n");
        
        // 测试用例1：基本功能测试
        testCase("测试用例1 - 基本功能", new int[][]{{5,4},{6,4},{6,7},{2,3}}, 3);
        
        // 测试用例2：LeetCode官方示例
        testCase("测试用例2 - 官方示例", new int[][]{{1,1},{1,1},{1,1}}, 1);
        
        // 测试用例3：单个信封
        testCase("测试用例3 - 单个信封", new int[][]{{5,4}}, 1);
        
        // 测试用例4：空数组
        testCase("测试用例4 - 空数组", new int[][]{}, 0);
        
        // 测试用例5：复杂情况
        testCase("测试用例5 - 复杂情况", new int[][]{{2,3},{5,4},{6,7},{6,4},{7,5}}, 3);
        
        // 测试用例6：所有信封尺寸相同
        testCase("测试用例6 - 尺寸相同", new int[][]{{1,1},{1,1},{1,1},{1,1}}, 1);
        
        // 性能测试
        performanceTest();
    }
    
    /**
     * 测试用例辅助方法
     */
    private static void testCase(String description, int[][] envelopes, int expected) {
        System.out.println(description);
        System.out.println("输入信封: " + Arrays.deepToString(envelopes));
        System.out.println("期望结果: " + expected);
        
        int result1 = maxEnvelopes(envelopes);
        int result2 = maxEnvelopesDP(envelopes);
        int result3 = maxEnvelopesOptimizedDP(envelopes);
        
        System.out.println("最优解法: " + result1 + " " + (result1 == expected ? "✓" : "✗"));
        System.out.println("基础DP: " + result2 + " " + (result2 == expected ? "✓" : "✗"));
        System.out.println("优化DP: " + result3 + " " + (result3 == expected ? "✓" : "✗"));
        
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
        
        // 生成测试数据：大规模信封数组
        int n = 10000;
        int[][] envelopes = new int[n][2];
        java.util.Random random = new java.util.Random();
        
        // 生成随机信封（避免重复）
        for (int i = 0; i < n; i++) {
            envelopes[i][0] = random.nextInt(1000) + 1; // 宽度1-1000
            envelopes[i][1] = random.nextInt(1000) + 1; // 高度1-1000
        }
        
        System.out.println("测试数据规模: " + n + "个信封");
        
        // 测试最优解法（贪心+二分）
        long startTime = System.currentTimeMillis();
        int result1 = maxEnvelopes(envelopes);
        long endTime = System.currentTimeMillis();
        System.out.println("最优解法（贪心+二分）:");
        System.out.println("  结果: " + result1);
        System.out.println("  耗时: " + (endTime - startTime) + "ms");
        
        // 测试优化DP解法（仅测试小规模）
        if (n <= 1000) {
            startTime = System.currentTimeMillis();
            int result2 = maxEnvelopesOptimizedDP(envelopes);
            endTime = System.currentTimeMillis();
            System.out.println("优化DP解法:");
            System.out.println("  结果: " + result2);
            System.out.println("  耗时: " + (endTime - startTime) + "ms");
            
            // 验证结果一致性
            if (result1 == result2) {
                System.out.println("结果一致性验证: 通过 ✓");
            } else {
                System.out.println("结果一致性验证: 失败 ✗");
            }
        } else {
            System.out.println("基础DP算法在大规模数据下性能较差，跳过测试");
        }
        
        System.out.println("注意：基础DP算法时间复杂度O(n²)，仅适用于小规模数据");
    }
    
    /**
     * 复杂度分析详细计算：
     * 
     * 最优解法（排序+LIS）：
     * - 时间：排序O(n log n) + LIS计算O(n log n) → O(n log n)
     * - 空间：排序O(log n) + LIS数组O(n) → O(n)
     * - 最优解：是，理论最优复杂度
     * 
     * 基础动态规划：
     * - 时间：排序O(n log n) + 双重循环O(n²) → O(n²)
     * - 空间：排序O(log n) + dp数组O(n) → O(n)
     * - 最优解：否，时间复杂度较高
     * 
     * 优化动态规划：
     * - 时间：O(n²)，但实际运行可能稍快
     * - 空间：O(n)
     * - 最优解：否，但比基础DP稍好
     * 
     * 算法关键点：
     * 1. 排序策略：宽度升序，宽度相同时高度降序
     * 2. 问题转化：将二维问题转化为一维LIS问题
     * 3. 贪心优化：使用二分查找加速LIS计算
     * 
     * 工程选择依据：
     * 1. 对于小规模数据（n ≤ 1000）：任意方法都可
     * 2. 对于中等规模数据（1000 < n ≤ 10000）：优先选择最优解法
     * 3. 对于大规模数据（n > 10000）：必须使用最优解法
     * 
     * 算法调试技巧：
     * 1. 打印排序后的信封序列
     * 2. 观察高度序列的LIS计算过程
     * 3. 使用小规模测试用例验证正确性
     */
}