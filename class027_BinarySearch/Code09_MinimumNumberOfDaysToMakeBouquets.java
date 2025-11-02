package class051;

import java.util.Arrays;

// 制作m束花所需的时间
// 给你一个整数数组 bloomDay，以及两个整数 m 和 k 。
// 现需要制作 m 束花。制作花束时，需要使用花园中 相邻的 k 朵花 。
// 花园中有 n 朵花，第 i 朵花会在 bloomDay[i] 时盛开，恰好 可以用于 一束 花中。
// 请你返回从花园中摘 m 束花需要等待的最少的天数。如果不能摘到 m 束花则返回 -1 。
// 测试链接 : https://leetcode.cn/problems/minimum-number-of-days-to-make-m-bouquets/
public class Code09_MinimumNumberOfDaysToMakeBouquets {

    // 二分答案法
    // 时间复杂度O(n * log(max))，额外空间复杂度O(1)
    public static int minDays(int[] bloomDay, int m, int k) {
        // 如果花的总数不够制作m束花，直接返回-1
        if ((long) m * k > bloomDay.length) {
            return -1;
        }

        // 确定二分搜索的上下界
        // 下界：数组中的最小值（最早盛开的花的时间）
        // 上界：数组中的最大值（最晚盛开的花的时间）
        int minDay = Arrays.stream(bloomDay).min().orElse(0);
        int maxDay = Arrays.stream(bloomDay).max().orElse(0);

        int left = minDay;
        int right = maxDay;
        int result = -1;

        // 二分搜索最少等待天数
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            // 判断在mid天内是否能制作m束花
            if (canMakeBouquets(bloomDay, m, k, mid)) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return result;
    }

    // 判断在day天内是否能制作m束花，每束花需要k朵相邻的花
    private static boolean canMakeBouquets(int[] bloomDay, int m, int k, int day) {
        int bouquets = 0;  // 已制作的花束数量
        int consecutive = 0; // 连续盛开的花朵数量

        for (int bloom : bloomDay) {
            if (bloom <= day) {
                // 当前花在day天内已经盛开
                consecutive++;
                // 如果连续盛开的花朵数量达到了k，可以制作一束花
                if (consecutive == k) {
                    bouquets++;
                    consecutive = 0; // 重置连续计数
                }
            } else {
                // 当前花在day天内未盛开，中断连续
                consecutive = 0;
            }
        }

        // 判断是否能制作至少m束花
        return bouquets >= m;
    }
    
    /*
     * 补充说明：
     * 
     * 问题解析：
     * 这是一个典型的二分答案问题。需要找到最少等待天数，使得能制作m束花，每束花需要k朵相邻的花。
     * 
     * 解题思路：
     * 1. 确定答案范围：
     *    - 下界：数组中的最小值（最早盛开的花的时间）
     *    - 上界：数组中的最大值（最晚盛开的花的时间）
     * 2. 二分搜索：在[left, right]范围内二分搜索等待天数
     * 3. 判断函数：canMakeBouquets(bloomDay, m, k, day)判断在day天内是否能制作m束花
     * 4. 贪心策略：尽可能连续地收集盛开的花朵制作花束
     * 
     * 时间复杂度分析：
     * 1. 二分搜索范围是[min, max]，二分次数是O(log(max))
     * 2. 每次二分需要调用canMakeBouquets函数，该函数遍历数组一次，时间复杂度是O(n)
     * 3. 总时间复杂度：O(n * log(max))
     * 
     * 空间复杂度分析：
     * 只使用了常数个额外变量，空间复杂度是O(1)
     * 
     * 工程化考虑：
     * 1. 边界条件处理：检查是否有足够的花朵制作m束花
     * 2. 贪心策略：连续收集盛开的花朵，中断时重置计数
     * 3. 整数溢出处理：使用long类型处理m*k可能的溢出
     * 
     * 相关题目扩展：
     * 1. LeetCode 1482. 制作m束花所需的时间 - https://leetcode.cn/problems/minimum-number-of-days-to-make-m-bouquets/
     * 2. LeetCode 1011. 在D天内送达包裹的能力 - https://leetcode.cn/problems/capacity-to-ship-packages-within-d-days/
     * 3. LeetCode 875. 爱吃香蕉的珂珂 - https://leetcode.cn/problems/koko-eating-bananas/
     * 4. LeetCode 1283. 使结果不超过阈值的最小除数 - https://leetcode.cn/problems/find-the-smallest-divisor-given-a-threshold/
     * 5. LeetCode 1552. 两球之间的磁力 - https://leetcode.cn/problems/magnetic-force-between-two-balls/
     * 6. HackerRank - Cut the Tree - https://www.hackerrank.com/challenges/cut-the-tree/problem
     * 7. Codeforces 1355B - Young Explorers - https://codeforces.com/problemset/problem/1355/B
     * 8. AtCoder ABC146 - D - Coloring Edges on Tree - https://atcoder.jp/contests/abc146/tasks/abc146_d
     */

}