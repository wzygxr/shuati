package class051;

// EKO (SPOJ)
// Lumberjack Mirko needs to chop down M metres of wood. It is an easy job for him since he has a nifty new woodcutting machine that can take down forests like wildfire. 
// However, Mirko is only allowed to cut a single row of trees.
// Mirko's machine works as follows: Mirko sets a height parameter H (in metres), and the machine raises a giant sawblade to that height and cuts off all tree parts higher than H (of course, trees not higher than H meters remain intact). 
// Mirko then takes the parts that were cut off. For example, if the tree row contains trees with heights of 20, 15, 10, and 17 metres, and Mirko raises his sawblade to 15 metres, the remaining tree heights after cutting will be 15, 15, 10, and 15 metres, respectively, while Mirko will take 5 metres off the first tree and 2 metres off the fourth tree (7 metres of wood in total).
// Mirko is ecologically minded, so he doesn't want to cut off more wood than necessary. That's why he wants to set his sawblade at the height that will allow him to cut off at least M metres of wood, but with as little waste as possible.
// What is the maximum integer height of the sawblade that still allows him to cut off at least M metres of wood?
// Problem Link: https://www.spoj.com/problems/EKO/
public class Code13_EKO {

    // 时间复杂度O(n * log(max))，额外空间复杂度O(1)
    public static long eko(long[] trees, long requiredWood) {
        // 确定二分搜索的上下界
        // 下界：0（不切割任何树木）
        // 上界：树木中的最大高度
        long left = 0;
        long right = 0;
        for (long tree : trees) {
            right = Math.max(right, tree);
        }
        
        long result = 0;
        
        // 二分搜索最高的锯片高度
        while (left <= right) {
            long mid = left + ((right - left) >> 1);
            // 判断以mid为锯片高度是否能获得至少requiredWood的木材
            if (getWood(trees, mid) >= requiredWood) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    // 计算以sawHeight为锯片高度能获得的木材总量
    private static long getWood(long[] trees, long sawHeight) {
        long totalWood = 0;
        
        for (long tree : trees) {
            // 如果树木高度大于锯片高度，则可以获得木材
            if (tree > sawHeight) {
                totalWood += tree - sawHeight;
            }
        }
        
        return totalWood;
    }
    
    /*
     * 补充说明：
     * 
     * 问题解析：
     * 这是一个经典的二分答案问题，目标是找到最高的锯片高度，使得切下的木材总量至少为M米。
     * 这是一个"最大化满足条件的值"问题。
     * 
     * 解题思路：
     * 1. 确定答案范围：
     *    - 下界：0（不切割任何树木）
     *    - 上界：树木中的最大高度
     * 2. 二分搜索：在[left, right]范围内二分搜索最高的锯片高度
     * 3. 判断函数：getWood(trees, sawHeight)计算以sawHeight为锯片高度能获得的木材总量
     * 4. 贪心策略：对于每棵树，切掉高于锯片高度的部分
     * 
     * 时间复杂度分析：
     * 1. 二分搜索范围是[0, max]，二分次数是O(log(max))
     * 2. 每次二分需要调用getWood函数，该函数遍历数组一次，时间复杂度是O(n)
     * 3. 总时间复杂度：O(n * log(max))
     * 
     * 空间复杂度分析：
     * 只使用了常数个额外变量，空间复杂度是O(1)
     * 
     * 工程化考虑：
     * 1. 数据类型选择：使用long类型避免整数溢出
     * 2. 边界条件处理：注意锯片高度为0或等于最大树高的情况
     * 3. 位运算优化：(right - left) >> 1 等价于 (right - left) / 2，但效率略高
     * 
     * 相关题目扩展：
     * 1. SPOJ EKO - https://www.spoj.com/problems/EKO/
     * 2. LeetCode 1011. 在D天内送达包裹的能力 - https://leetcode.cn/problems/capacity-to-ship-packages-within-d-days/
     * 3. LeetCode 875. 爱吃香蕉的珂珂 - https://leetcode.cn/problems/koko-eating-bananas/
     * 4. LeetCode 410. 分割数组的最大值 - https://leetcode.cn/problems/split-array-largest-sum/
     * 5. LeetCode 1231. 分享巧克力 - https://leetcode.cn/problems/divide-chocolate/
     * 6. SPOJ AGGRCOW - Aggressive Cows - https://www.spoj.com/problems/AGGRCOW/
     * 7. 牛客网 NC163 机器人跳跃问题 - https://www.nowcoder.com/practice/7037a3d57bbd4336856b8e16a9cafd71
     * 8. HackerRank - Fair Rations - https://www.hackerrank.com/challenges/fair-rations/problem
     * 9. Codeforces 460C - Present - https://codeforces.com/problemset/problem/460/C
     * 10. AtCoder ABC146 - C - Buy an Integer - https://atcoder.jp/contests/abc146/tasks/abc146_c
     */
}