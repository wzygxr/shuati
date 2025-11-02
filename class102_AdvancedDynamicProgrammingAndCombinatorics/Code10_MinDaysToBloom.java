package class128;

// 制作 m 束花所需的最少天数
// 给你一个整数数组 bloomDay，以及两个整数 m 和 k 。
// 现需要制作 m 束花。制作花束时，需要使用花园中相邻的 k 朵花 。
// 花园中有 n 朵花，第 i 朵花会在 bloomDay[i] 时盛开，恰好可以用于一束花中。
// 请你返回从花园中摘 m 束花需要等待的最少的天数。如果不能摘到 m 束花则返回 -1 。

// 算法思路：
// 这是一个典型的二分答案问题。
// 1. 答案具有单调性：等待天数越多，盛开的花朵越多，能制作的花束也越多
// 2. 二分搜索答案的范围：左边界是数组中的最小值，右边界是数组中的最大值
// 3. 对于每个中间值，使用贪心算法检查是否能在该天数内制作出 m 束花
// 
// 时间复杂度：O(n * log(max-min))
// 空间复杂度：O(1)
// 
// 测试链接 : https://leetcode.cn/problems/minimum-number-of-days-to-make-m-bouquets/

public class Code10_MinDaysToBloom {
    
    /**
     * 计算制作 m 束花所需的最少天数
     * 
     * @param bloomDay 每朵花盛开的天数
     * @param m 需要制作的花束数量
     * @param k 每束花需要的相邻花朵数量
     * @return 制作 m 束花所需的最少天数，如果不能制作则返回 -1
     * 
     * 时间复杂度：O(n * log(max-min))
     * 空间复杂度：O(1)
     */
    public static int minDaysToBloom(int[] bloomDay, int m, int k) {
        int n = bloomDay.length;
        
        // 如果需要的花朵总数超过了花园中的花朵数，无法完成任务
        if ((long) m * k > n) {
            return -1;
        }
        
        // 确定二分搜索的边界
        // 左边界：数组中的最小值
        // 右边界：数组中的最大值
        int left = Integer.MAX_VALUE, right = Integer.MIN_VALUE;
        for (int day : bloomDay) {
            left = Math.min(left, day);
            right = Math.max(right, day);
        }
        
        int result = -1;
        
        // 二分搜索答案
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            // 检查是否能在 mid 天内制作出 m 束花
            if (canMakeBouquets(bloomDay, m, k, mid)) {
                result = mid;
                right = mid - 1;  // 尝试寻找更少的天数
            } else {
                left = mid + 1;   // 需要更多的天数
            }
        }
        
        return result;
    }
    
    /**
     * 检查是否能在给定天数内制作出指定数量的花束
     * 使用贪心算法实现
     * 
     * @param bloomDay 每朵花盛开的天数
     * @param m 需要制作的花束数量
     * @param k 每束花需要的相邻花朵数量
     * @param days 给定的天数
     * @return 是否能在给定天数内制作出指定数量的花束
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    private static boolean canMakeBouquets(int[] bloomDay, int m, int k, int days) {
        int bouquets = 0;   // 已制作的花束数量
        int consecutive = 0; // 当前连续盛开的花朵数量
        
        for (int day : bloomDay) {
            if (day <= days) {
                // 如果当前花朵在给定天数内盛开
                consecutive++;
                
                // 如果连续盛开的花朵数量达到了 k 朵，可以制作一束花
                if (consecutive == k) {
                    bouquets++;
                    consecutive = 0;  // 重置连续计数
                }
            } else {
                // 如果当前花朵在给定天数内未盛开，重置连续计数
                consecutive = 0;
            }
        }
        
        // 检查是否能制作出至少 m 束花
        return bouquets >= m;
    }
    
    // 为了测试
    public static void main(String[] args) {
        // 测试用例1
        int[] bloomDay1 = {1, 10, 3, 10, 2};
        int m1 = 3, k1 = 1;
        System.out.println("bloomDay: " + java.util.Arrays.toString(bloomDay1) + 
                          ", m = " + m1 + ", k = " + k1 + 
                          ", 结果 = " + minDaysToBloom(bloomDay1, m1, k1));  // 输出: 3
        
        // 测试用例2
        int[] bloomDay2 = {1, 10, 3, 10, 2};
        int m2 = 3, k2 = 2;
        System.out.println("bloomDay: " + java.util.Arrays.toString(bloomDay2) + 
                          ", m = " + m2 + ", k = " + k2 + 
                          ", 结果 = " + minDaysToBloom(bloomDay2, m2, k2));  // 输出: -1
        
        // 测试用例3
        int[] bloomDay3 = {7, 7, 7, 7, 12, 7, 7};
        int m3 = 2, k3 = 3;
        System.out.println("bloomDay: " + java.util.Arrays.toString(bloomDay3) + 
                          ", m = " + m3 + ", k = " + k3 + 
                          ", 结果 = " + minDaysToBloom(bloomDay3, m3, k3));  // 输出: 12
    }
}