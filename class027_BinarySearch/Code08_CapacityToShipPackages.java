package class051;

// 在D天内送达包裹的能力
// 传送带上的包裹必须在 days 天内从一个港口运送到另一个港口。
// 传送带上的第 i 个包裹的重量为 weights[i]。
// 每一天，我们都会按给出重量的顺序往传送带上装载包裹。我们装载的重量不能超过船的最大运载能力。
// 返回能在 days 天内将传送带上的所有包裹送达的船的最低运载能力。
// 测试链接 : https://leetcode.cn/problems/capacity-to-ship-packages-within-d-days/
public class Code08_CapacityToShipPackages {

    // 二分答案法
    // 时间复杂度O(n * log(sum))，额外空间复杂度O(1)
    public static int shipWithinDays(int[] weights, int days) {
        // 确定二分搜索的上下界
        // 下界：数组中的最大值（至少要能运输最重的包裹）
        // 上界：数组元素和（一天运输完所有包裹）
        int maxWeight = 0;
        int totalWeight = 0;
        for (int weight : weights) {
            maxWeight = Math.max(maxWeight, weight);
            totalWeight += weight;
        }

        int left = maxWeight;
        int right = totalWeight;
        int result = totalWeight;

        // 二分搜索最低运载能力
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            // 判断以mid为运载能力是否能在days天内运输完所有包裹
            if (canShipInDays(weights, days, mid)) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return result;
    }

    // 判断以capacity为运载能力是否能在days天内运输完所有包裹
    private static boolean canShipInDays(int[] weights, int days, int capacity) {
        int requiredDays = 1; // 需要的天数，初始为1
        int currentLoad = 0;  // 当前船上的重量

        for (int weight : weights) {
            // 如果当前包裹重量加上当前负载超过了运载能力
            if (currentLoad + weight > capacity) {
                // 需要增加一天，并将当前包裹放到下一天运输
                requiredDays++;
                currentLoad = weight;
                
                // 如果需要的天数超过了给定天数，返回false
                if (requiredDays > days) {
                    return false;
                }
            } else {
                // 否则将当前包裹加入当前负载
                currentLoad += weight;
            }
        }

        return true;
    }
    
    /*
     * 补充说明：
     * 
     * 问题解析：
     * 这是一个典型的二分答案问题。需要找到最低的运载能力，使得能在指定天数内运输完所有包裹。
     * 
     * 解题思路：
     * 1. 确定答案范围：
     *    - 下界：数组中的最大值（至少要能运输最重的包裹）
     *    - 上界：数组元素和（一天运输完所有包裹）
     * 2. 二分搜索：在[left, right]范围内二分搜索运载能力
     * 3. 判断函数：canShipInDays(weights, days, capacity)判断以capacity运载能力是否能在days天内运输完所有包裹
     * 4. 贪心策略：按顺序装载包裹，尽可能在每天装更多包裹
     * 
     * 时间复杂度分析：
     * 1. 二分搜索范围是[max, sum]，二分次数是O(log(sum))
     * 2. 每次二分需要调用canShipInDays函数，该函数遍历数组一次，时间复杂度是O(n)
     * 3. 总时间复杂度：O(n * log(sum))
     * 
     * 空间复杂度分析：
     * 只使用了常数个额外变量，空间复杂度是O(1)
     * 
     * 工程化考虑：
     * 1. 边界条件处理：注意天数和运载能力的限制
     * 2. 贪心策略：按顺序装载包裹，不重新排序
     * 3. 整数溢出处理：适当使用long类型（本题数据范围未超过int）
     * 
     * 相关题目扩展：
     * 1. LeetCode 1011. 在D天内送达包裹的能力 - https://leetcode.cn/problems/capacity-to-ship-packages-within-d-days/
     * 2. LeetCode 875. 爱吃香蕉的珂珂 - https://leetcode.cn/problems/koko-eating-bananas/
     * 3. LeetCode 410. 分割数组的最大值 - https://leetcode.cn/problems/split-array-largest-sum/
     * 4. LeetCode 1231. 分享巧克力 - https://leetcode.cn/problems/divide-chocolate/
     * 5. LeetCode 1482. 制作m束花所需的时间 - https://leetcode.cn/problems/minimum-number-of-days-to-make-m-bouquets/
     * 6. HackerRank - Maximum Subarray Sum - https://www.hackerrank.com/challenges/maximum-subarray-sum/problem
     * 7. Codeforces 1324B - Yet Another Palindrome Problem - https://codeforces.com/problemset/problem/1324/B
     * 8. AtCoder ABC146 - C - Buy an Integer - https://atcoder.jp/contests/abc146/tasks/abc146_c
     */

}