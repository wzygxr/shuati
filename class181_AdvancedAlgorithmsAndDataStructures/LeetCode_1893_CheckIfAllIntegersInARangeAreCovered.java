package class008_AdvancedAlgorithmsAndDataStructures.difference_array_problems;

import java.util.*;

/**
 * LeetCode 1893. 检查是否区域内所有整数都被覆盖
 * 
 * 题目来源：https://leetcode.cn/problems/check-if-all-the-integers-in-a-range-are-covered/
 * 
 * 题目描述：
 * 给你一个二维整数数组 ranges 和两个整数 left 和 right。
 * 每个 ranges[i] = [starti, endi] 表示一个从 starti 到 endi 的闭区间。
 * 如果闭区间 [left, right] 内每个整数都被 ranges 中至少一个区间覆盖，
 * 那么返回 true，否则返回 false。
 * 
 * 算法思路：
 * 这个问题可以通过以下方法解决：
 * 1. 差分数组：记录每个位置的覆盖次数
 * 2. 前缀和：计算每个位置的实际覆盖次数
 * 3. 暴力法：直接检查每个位置是否被覆盖
 * 
 * 使用差分数组的方法：
 * 1. 对于每个区间，在差分数组的起始位置增加1，在结束位置的下一个位置减少1
 * 2. 计算差分数组的前缀和得到每个位置的覆盖次数
 * 3. 检查目标区间内每个位置的覆盖次数是否大于0
 * 
 * 时间复杂度：
 * - 差分数组：O(n + m)
 * - 暴力法：O(n * m)
 * - 空间复杂度：O(m)
 * 
 * 应用场景：
 * 1. 资源分配：检查资源覆盖情况
 * 2. 时间安排：验证时间区间是否完全覆盖
 * 3. 数据分析：区间数据完整性检查
 * 
 * 相关题目：
 * 1. LeetCode 370. 区间加法
 * 2. LeetCode 1109. 航班预订统计
 * 3. LeetCode 1094. 拼车
 */
public class LeetCode_1893_CheckIfAllIntegersInARangeAreCovered {
    
    /**
     * 方法1：差分数组
     * 时间复杂度：O(n + m)
     * 空间复杂度：O(m)
     * @param ranges 区间数组
     * @param left 目标区间左边界
     * @param right 目标区间右边界
     * @return 是否完全覆盖
     */
    public static boolean isCoveredDifferenceArray(int[][] ranges, int left, int right) {
        // 创建差分数组，大小为51（因为题目说明数值范围是1-50）
        int[] diff = new int[52];
        
        // 处理每个区间
        for (int[] range : ranges) {
            int start = range[0];
            int end = range[1];
            
            // 在差分数组中标记覆盖变化
            diff[start] += 1;
            diff[end + 1] -= 1;
        }
        
        // 计算差分数组的前缀和得到每个位置的覆盖次数
        int[] coverage = new int[52];
        coverage[0] = diff[0];
        for (int i = 1; i < 52; i++) {
            coverage[i] = coverage[i - 1] + diff[i];
        }
        
        // 检查目标区间内每个位置是否都被覆盖
        for (int i = left; i <= right; i++) {
            if (coverage[i] <= 0) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 方法2：优化的差分数组（空间优化）
     * 时间复杂度：O(n + m)
     * 空间复杂度：O(m)
     * @param ranges 区间数组
     * @param left 目标区间左边界
     * @param right 目标区间右边界
     * @return 是否完全覆盖
     */
    public static boolean isCoveredOptimized(int[][] ranges, int left, int right) {
        // 创建差分数组，只需要覆盖目标区间即可
        int[] diff = new int[right - left + 3];
        
        // 处理每个区间
        for (int[] range : ranges) {
            int start = range[0];
            int end = range[1];
            
            // 只处理与目标区间有交集的区间
            if (start <= right && end >= left) {
                // 调整到目标区间的相对位置
                int adjustedStart = Math.max(start, left) - left;
                int adjustedEnd = Math.min(end, right) - left;
                
                // 在差分数组中标记覆盖变化
                diff[adjustedStart] += 1;
                diff[adjustedEnd + 1] -= 1;
            }
        }
        
        // 计算前缀和并检查覆盖情况
        int coverage = 0;
        for (int i = 0; i <= right - left; i++) {
            coverage += diff[i];
            if (coverage <= 0) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 方法3：暴力法（用于对比）
     * 时间复杂度：O(n * m)
     * 空间复杂度：O(1)
     * @param ranges 区间数组
     * @param left 目标区间左边界
     * @param right 目标区间右边界
     * @return 是否完全覆盖
     */
    public static boolean isCoveredBruteForce(int[][] ranges, int left, int right) {
        // 检查目标区间内每个位置是否被至少一个区间覆盖
        for (int i = left; i <= right; i++) {
            boolean covered = false;
            for (int[] range : ranges) {
                if (range[0] <= i && i <= range[1]) {
                    covered = true;
                    break;
                }
            }
            if (!covered) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 方法4：排序+合并区间
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     * @param ranges 区间数组
     * @param left 目标区间左边界
     * @param right 目标区间右边界
     * @return 是否完全覆盖
     */
    public static boolean isCoveredMergeIntervals(int[][] ranges, int left, int right) {
        // 过滤与目标区间有交集的区间
        List<int[]> relevantRanges = new ArrayList<>();
        for (int[] range : ranges) {
            if (range[0] <= right && range[1] >= left) {
                relevantRanges.add(new int[]{Math.max(range[0], left), Math.min(range[1], right)});
            }
        }
        
        // 按起始位置排序
        relevantRanges.sort((a, b) -> Integer.compare(a[0], b[0]));
        
        // 合并区间并检查是否完全覆盖目标区间
        int coveredStart = left;
        for (int[] range : relevantRanges) {
            // 如果当前区间的起始位置大于已覆盖的结束位置+1，说明有空隙
            if (range[0] > coveredStart) {
                return false;
            }
            // 更新已覆盖的结束位置
            coveredStart = Math.max(coveredStart, range[1] + 1);
            // 如果已经完全覆盖目标区间，提前返回
            if (coveredStart > right) {
                return true;
            }
        }
        
        // 检查是否完全覆盖目标区间
        return coveredStart > right;
    }
    
    /**
     * 测试函数
     */
    public static void main(String[] args) {
        System.out.println("=== 测试 LeetCode 1893. 检查是否区域内所有整数都被覆盖 ===");
        
        // 测试用例1
        int[][] ranges1 = {{1,2},{3,4},{5,6}};
        int left1 = 2, right1 = 5;
        System.out.println("测试用例1:");
        System.out.println("区间: [[1,2],[3,4],[5,6]], left: " + left1 + ", right: " + right1);
        System.out.println("差分数组解法结果: " + isCoveredDifferenceArray(ranges1, left1, right1));
        System.out.println("优化差分数组解法结果: " + isCoveredOptimized(ranges1, left1, right1));
        System.out.println("暴力解法结果: " + isCoveredBruteForce(ranges1, left1, right1));
        System.out.println("合并区间解法结果: " + isCoveredMergeIntervals(ranges1, left1, right1));
        System.out.println("期望结果: false");
        System.out.println();
        
        // 测试用例2
        int[][] ranges2 = {{1,10},{10,20}};
        int left2 = 21, right2 = 21;
        System.out.println("测试用例2:");
        System.out.println("区间: [[1,10],[10,20]], left: " + left2 + ", right: " + right2);
        System.out.println("差分数组解法结果: " + isCoveredDifferenceArray(ranges2, left2, right2));
        System.out.println("优化差分数组解法结果: " + isCoveredOptimized(ranges2, left2, right2));
        System.out.println("暴力解法结果: " + isCoveredBruteForce(ranges2, left2, right2));
        System.out.println("合并区间解法结果: " + isCoveredMergeIntervals(ranges2, left2, right2));
        System.out.println("期望结果: false");
        System.out.println();
        
        // 测试用例3
        int[][] ranges3 = {{1,50}};
        int left3 = 1, right3 = 50;
        System.out.println("测试用例3:");
        System.out.println("区间: [[1,50]], left: " + left3 + ", right: " + right3);
        System.out.println("差分数组解法结果: " + isCoveredDifferenceArray(ranges3, left3, right3));
        System.out.println("优化差分数组解法结果: " + isCoveredOptimized(ranges3, left3, right3));
        System.out.println("暴力解法结果: " + isCoveredBruteForce(ranges3, left3, right3));
        System.out.println("合并区间解法结果: " + isCoveredMergeIntervals(ranges3, left3, right3));
        System.out.println("期望结果: true");
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        Random random = new Random(42);
        int n = 10000;
        int[][] ranges = new int[n][2];
        for (int i = 0; i < n; i++) {
            int start = random.nextInt(10000) + 1;
            int end = start + random.nextInt(1000) + 1;
            ranges[i] = new int[]{start, end};
        }
        int left = 1000, right = 2000;
        
        long startTime = System.nanoTime();
        boolean result1 = isCoveredDifferenceArray(ranges, left, right);
        long endTime = System.nanoTime();
        System.out.println("差分数组解法处理" + n + "个区间时间: " + (endTime - startTime) / 1_000_000.0 + " ms, 结果: " + result1);
        
        startTime = System.nanoTime();
        boolean result2 = isCoveredOptimized(ranges, left, right);
        endTime = System.nanoTime();
        System.out.println("优化差分数组解法处理" + n + "个区间时间: " + (endTime - startTime) / 1_000_000.0 + " ms, 结果: " + result2);
        
        startTime = System.nanoTime();
        boolean result3 = isCoveredBruteForce(ranges, left, right);
        endTime = System.nanoTime();
        System.out.println("暴力解法处理" + n + "个区间时间: " + (endTime - startTime) / 1_000_000.0 + " ms, 结果: " + result3);
        
        startTime = System.nanoTime();
        boolean result4 = isCoveredMergeIntervals(ranges, left, right);
        endTime = System.nanoTime();
        System.out.println("合并区间解法处理" + n + "个区间时间: " + (endTime - startTime) / 1_000_000.0 + " ms, 结果: " + result4);
    }
}