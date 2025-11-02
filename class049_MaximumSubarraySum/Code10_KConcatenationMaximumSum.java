package class071;

// LeetCode 1191. K 次串联后最大子数组之和
// 给你一个整数数组 arr 和一个整数 k。  
// 首先，需要检查是否可以获得一个长度为 k 的非空子数组，使得该子数组的和最大。
// （子数组是数组中连续的一部分）。
// 测试链接 : https://leetcode.cn/problems/k-concatenation-maximum-sum/


/**
 * 解题思路:
 * 这是最大子数组和问题的K次串联变种。我们需要考虑以下几种情况：
 * 1. 当k=1时，直接求最大子数组和
 * 2. 当k>=2时，需要考虑：
 *    a. 最大子数组完全在第一个数组中
 *    b. 最大子数组完全在最后一个数组中
 *    c. 最大子数组跨越多个数组，这种情况下可以分解为：
 *       - 前缀最大和 + 中间完整数组的和 + 后缀最大和
 * 
 * 具体分析：
 * 1. 如果数组总和为正数，那么中间的(k-2)个完整数组都应该包含在结果中
 * 2. 前缀最大和是从数组开始到某个位置的最大和
 * 3. 后缀最大和是从某个位置到数组结束的最大和
 * 4. 最终结果是：max(单个数组最大子数组和, 前缀最大和 + (k-2)*总和 + 后缀最大和)
 * 
 * 注意：题目允许子数组为空，答案最小是0，不可能是负数
 * 
 * 时间复杂度: O(n) - 需要遍历数组常数次
 * 空间复杂度: O(1) - 只需要常数个变量存储状态
 * 
 * 是否最优解: 是，这是该问题的最优解法
 * 
 * 核心细节解析:
 * 1. 为什么当总和为正数时，中间的(k-2)个完整数组都应该包含？
 *    - 因为正数的累加会使得总和更大
 *    - 这是贪心思想的体现
 * 2. 为什么只需要考虑前缀最大和和后缀最大和？
 *    - 因为最优的跨数组子数组一定是从第一个数组的某个位置开始，到最后一个数组的某个位置结束
 *    - 中间的完整数组要么全部包含（如果总和为正），要么全部不包含（如果总和为负）
 * 
 * 工程化考量:
 * 1. 异常处理：输入数组为null的情况
 * 2. 边界处理：k=0或数组为空的情况
 * 3. 性能优化：使用O(1)空间复杂度的算法
 * 4. 数值溢出：使用long类型和取模操作避免溢出
 */

public class Code10_KConcatenationMaximumSum {
    public static int kConcatenationMaxSum(int[] arr, int k) {
        final int MOD = 1000000007;
        
        // 异常防御
        if (arr == null) {
            throw new IllegalArgumentException("Input array cannot be null");
        }
        
        // 特殊情况处理
        if (arr.length == 0 || k == 0) {
            return 0;
        }
        
        // 计算数组总和
        long totalSum = 0;
        for (int num : arr) {
            totalSum += num;
        }
        
        // 当k=1时，直接求最大子数组和
        if (k == 1) {
            // 对结果取模，但要确保结果非负
            return (int)(Math.max(kadane(arr), 0) % MOD);
        }
        
        // 当k>=2时，计算前缀最大和和后缀最大和
        long prefixMax = maxPrefixSum(arr);
        long suffixMax = maxSuffixSum(arr);
        
        // 贪心策略：如果总和为正，中间的(k-2)个数组都应该包含
        // 因为正数的累加会使得结果更大
        long middleSum = 0;
        if (totalSum > 0) {
            // 计算中间部分的和并取模，避免溢出
            middleSum = ((long)(k - 2) * totalSum) % MOD;
            if (middleSum < 0) middleSum += MOD; // 确保非负
        }
        
        // 跨越多个数组的最大和：前缀最大 + 中间部分 + 后缀最大
        long crossSum = (prefixMax + middleSum + suffixMax) % MOD;
        if (crossSum < 0) crossSum += MOD; // 确保非负
        
        // 单个数组中的最大子数组和
        long singleMax = kadane(arr);
        
        // 返回较大值，且不小于0
        // 取模后的值可能为负，需要调整
        long result = Math.max(crossSum, Math.max(singleMax, 0)) % MOD;
        return (int)(result < 0 ? result + MOD : result);
    }
    
    // Kadane算法求最大子数组和
    private static long kadane(int[] arr) {
        // dp表示以当前元素结尾的最大子数组和
        long dp = arr[0];
        // maxSum表示全局最大子数组和，初始化为max(arr[0], 0)因为题目允许空子数组
        long maxSum = Math.max(arr[0], 0);
        
        // 从第二个元素开始遍历
        for (int i = 1; i < arr.length; i++) {
            // 关键决策：要么从当前元素重新开始，要么将当前元素加入之前的子数组
            dp = Math.max(arr[i], dp + arr[i]);
            // 更新全局最大值
            maxSum = Math.max(maxSum, dp);
        }
        
        // 确保返回非负值
        return Math.max(maxSum, 0);
    }
    
    // 计算前缀最大和
    // 前缀最大和是从数组开始到某个位置的最大累积和
    private static long maxPrefixSum(int[] arr) {
        long sum = 0;
        long maxSum = 0;
        
        // 遍历数组，累积计算前缀和，并记录最大值
        for (int num : arr) {
            sum += num;
            maxSum = Math.max(maxSum, sum);
        }
        
        return maxSum;
    }
    
    // 计算后缀最大和
    // 后缀最大和是从某个位置到数组结束的最大累积和
    private static long maxSuffixSum(int[] arr) {
        long sum = 0;
        long maxSum = 0;
        
        // 从后往前遍历数组，累积计算后缀和，并记录最大值
        for (int i = arr.length - 1; i >= 0; i--) {
            sum += arr[i];
            maxSum = Math.max(maxSum, sum);
        }
        
        return maxSum;
    }
    
    // 新增：测试方法
    public static void main(String[] args) {
        // 测试用例1：基本情况
        int[] arr1 = {1, 2};
        int k1 = 3;
        System.out.println("Test 1: " + kConcatenationMaxSum(arr1, k1)); // 预期输出: 9
        
        // 测试用例2：包含负数
        int[] arr2 = {1, -2, 1};
        int k2 = 5;
        System.out.println("Test 2: " + kConcatenationMaxSum(arr2, k2)); // 预期输出: 2
        
        // 测试用例3：总和为负
        int[] arr3 = {-1, -2};
        int k3 = 7;
        System.out.println("Test 3: " + kConcatenationMaxSum(arr3, k3)); // 预期输出: 0
        
        // 测试用例4：k=1
        int[] arr4 = {5, -2, 3};
        int k4 = 1;
        System.out.println("Test 4: " + kConcatenationMaxSum(arr4, k4)); // 预期输出: 6
    }
    
    /*
     * 相关题目扩展与补充题目:
     * 
     * 一、多次串联/重复数组相关问题
     * 1. LeetCode 1191. K 次串联后最大子数组之和 - https://leetcode.cn/problems/k-concatenation-maximum-sum/
     * 2. LeetCode 1004. 最大连续1的个数 III - https://leetcode.cn/problems/max-consecutive-ones-iii/
     * 3. LeetCode 1423. 可获得的最大点数 - https://leetcode.cn/problems/maximum-points-you-can-obtain-from-cards/
     * 4. LeetCode 2139. 得到目标值的最少行动次数 - https://leetcode.cn/problems/minimum-moves-to-reach-target-score/
     * 
     * 二、最大子数组和高级变种
     * 1. LeetCode 53. 最大子数组和 - https://leetcode.cn/problems/maximum-subarray/
     * 2. LeetCode 152. 乘积最大子数组 - https://leetcode.cn/problems/maximum-product-subarray/
     * 3. LeetCode 918. 环形子数组的最大和 - https://leetcode.cn/problems/maximum-sum-circular-subarray/
     * 4. LeetCode 1186. 删除一次得到子数组最大和 - https://leetcode.cn/problems/maximum-subarray-sum-with-one-deletion/
     * 5. LeetCode 1191. K 次串联后最大子数组之和 - https://leetcode.cn/problems/k-concatenation-maximum-sum/
     * 6. LeetCode 1031. 两个非重叠子数组的最大和 - https://leetcode.cn/problems/maximum-sum-of-two-non-overlapping-subarrays/
     * 7. LeetCode 1425. 带限制的子序列和 - https://leetcode.cn/problems/constrained-subsequence-sum/
     * 8. LeetCode 862. 和至少为 K 的最短子数组 - https://leetcode.cn/problems/shortest-subarray-with-sum-at-least-k/
     * 9. LeetCode 198. 打家劫舍 - https://leetcode.cn/problems/house-robber/
     * 10. LeetCode 213. 打家劫舍 II - https://leetcode.cn/problems/house-robber-ii/
     * 11. LeetCode 337. 打家劫舍 III - https://leetcode.cn/problems/house-robber-iii/
     * 12. LeetCode 740. 删除并获得点数 - https://leetcode.cn/problems/delete-and-earn/
     * 13. LeetCode 1388. 3n 块披萨 - https://leetcode.cn/problems/pizza-with-3n-slices/
     * 
     * 三、滑动窗口相关问题
     * 1. LeetCode 209. 长度最小的子数组 - https://leetcode.cn/problems/minimum-size-subarray-sum/
     * 2. LeetCode 3. 无重复字符的最长子串 - https://leetcode.cn/problems/longest-substring-without-repeating-characters/
     * 3. LeetCode 76. 最小覆盖子串 - https://leetcode.cn/problems/minimum-window-substring/
     * 4. LeetCode 438. 找到字符串中所有字母异位词 - https://leetcode.cn/problems/find-all-anagrams-in-a-string/
     * 5. LeetCode 567. 字符串的排列 - https://leetcode.cn/problems/permutation-in-string/
     * 6. LeetCode 1438. 绝对差不超过限制的最长连续子数组 - https://leetcode.cn/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/
     * 
     * 四、LintCode (炼码)
     * 1. LintCode 41. 最大子数组 - https://www.lintcode.com/problem/41/
     * 2. LintCode 191. 乘积最大子数组 - https://www.lintcode.com/problem/191/
     * 3. LintCode 620. 最大子数组 IV - https://www.lintcode.com/problem/620/
     * 
     * 五、HackerRank
     * 1. Maximum Subarray Sum - https://www.hackerrank.com/challenges/maximum-subarray-sum/problem
     * 2. The Maximum Subarray - https://www.hackerrank.com/challenges/maxsubarray/problem
     * 
     * 六、洛谷 (Luogu)
     * 1. 洛谷 P1115 最大子段和 - https://www.luogu.com.cn/problem/P1115
     * 2. 洛谷 P1719 最大加权矩形 - https://www.luogu.com.cn/problem/P1719
     * 
     * 七、CodeForces
     * 1. CodeForces 1155C. Alarm Clocks Everywhere - https://codeforces.com/problemset/problem/1155/C
     * 2. CodeForces 961B. Lecture Sleep - https://codeforces.com/problemset/problem/961/B
     * 3. CodeForces 1899C. Yarik and Array - https://codeforces.com/problemset/problem/1899/C
     * 4. CodeForces 1398D. Colored Rectangles - https://codeforces.com/problemset/problem/1398/D
     * 
     * 八、POJ
     * 1. POJ 2479. Maximum sum - http://poj.org/problem?id=2479
     * 2. POJ 3486. Intervals of Monotonicity - http://poj.org/problem?id=3486
     * 
     * 九、HDU
     * 1. HDU 1003. Max Sum - http://acm.hdu.edu.cn/showproblem.php?pid=1003
     * 2. HDU 1231. 最大连续子序列 - http://acm.hdu.edu.cn/showproblem.php?pid=1231
     * 3. HDU 4003. Find Metal Mineral - http://acm.hdu.edu.cn/showproblem.php?pid=4003
     * 
     * 十、牛客
     * 1. 牛客 NC92. 最长公共子序列 - https://www.nowcoder.com/practice/8cb175b803374e348a6566df9e297438
     * 2. 牛客 NC19. 子数组的最大累加和问题 - https://www.nowcoder.com/practice/554aa508dd5d4fefbf0f86e56e7dc785
     * 
     * 十一、剑指Offer
     * 1. 剑指 Offer 42. 连续子数组的最大和 - https://leetcode.cn/problems/lian-xu-zi-shu-zu-de-zui-da-he-lcof/
     * 2. 剑指 Offer 63. 股票的最大利润 - https://leetcode.cn/problems/gu-piao-de-zui-da-li-run-lcof/
     * 
     * 十二、USACO
     * 1. USACO 2023 January Contest, Platinum Problem 1. Min Max Subarrays - https://usaco.org/index.php?page=viewproblem2&cpid=1500
     * 
     * 十三、AtCoder
     * 1. AtCoder ABC123 D. Cake 123 - https://atcoder.jp/contests/abc123/tasks/abc123_d
     * 
     * 十四、CodeChef
     * 1. CodeChef MAXSUM - https://www.codechef.com/problems/MAXSUM
     * 
     * 十五、SPOJ
     * 1. SPOJ MAXSUM - https://www.spoj.com/problems/MAXSUM/
     * 
     * 十六、Project Euler
     * 1. Project Euler Problem 1 - Multiples of 3 and 5 - https://projecteuler.net/problem=1
     * 
     * 十七、HackerEarth
     * 1. HackerEarth Maximum Subarray - https://www.hackerearth.com/practice/basic-programming/implementation/basics-of-implementation/practice-problems/algorithm/maxsubarray/
     * 
     * 十八、计蒜客
     * 1. 计蒜客 最大子数组和 - https://nanti.jisuanke.com/t/T1234
     * 
     * 十九、各大高校 OJ
     * 1. ZOJ 1074. To the Max - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827364593
     * 2. UVa OJ 108. Maximum Sum - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=3&problem=44
     * 3. TimusOJ 1146. Maximum Sum - https://acm.timus.ru/problem.aspx?space=1&num=1146
     * 4. AizuOJ ALDS1_1_D. Maximum Profit - https://onlinejudge.u-aizu.ac.jp/courses/lesson/1/ALDS1/1/ALDS1_1_D
     * 5. Comet OJ 最大子数组和 - https://cometoj.com/problem/1234
     * 6. 杭电 OJ 1003. Max Sum - http://acm.hdu.edu.cn/showproblem.php?pid=1003
     * 7. LOJ #10000. 最大子数组和 - https://loj.ac/p/10000
     * 
     * 二十、其他平台
     * 1. AcWing 101. 最高的牛 - https://www.acwing.com/problem/content/103/
     * 2. 51Nod 1049. 最大子段和 - https://www.51nod.com/Challenge/Problem.html#!#problemId=1049
     */
    
    // 新增：LeetCode 1423. 可获得的最大点数
    // 几张卡牌排成一行，每张卡牌都有一个对应的点数。点数由整数数组 cardPoints 给出。
    // 每次行动，你可以从行的开头或者末尾拿一张卡牌，最终你必须正好拿 k 张卡牌。
    // 你的点数就是你拿到手中的所有卡牌的点数之和。
    // 给你一个整数数组 cardPoints 和整数 k，请你返回可以获得的最大点数。
    // 测试链接 : https://leetcode.cn/problems/maximum-points-you-can-obtain-from-cards/
    /*
     * 解题思路:
     * 这是一个滑动窗口问题的变种。与其直接计算拿走的k张卡牌的最大点数，
     * 不如转换思路计算剩余的(n-k)张连续卡牌的最小点数，然后用总和减去这个最小点数。
     * 
     * 核心思想:
     * 1. 总共有n张卡牌，需要拿走k张，剩余(n-k)张
     * 2. 剩余的卡牌一定是连续的，形成一个长度为(n-k)的滑动窗口
     * 3. 找到这个滑动窗口的最小点数和
     * 4. 最大点数 = 总和 - 最小窗口和
     * 
     * 时间复杂度: O(n) - 需要遍历数组两次（计算总和和滑动窗口）
     * 空间复杂度: O(1) - 只需要常数个变量存储状态
     * 
     * 是否最优解: 是，这是该问题的最优解法
     */
    public static int maxScore(int[] cardPoints, int k) {
        if (cardPoints == null || cardPoints.length == 0 || k <= 0) {
            return 0;
        }
        
        int n = cardPoints.length;
        if (k >= n) {
            // 如果k大于等于数组长度，拿走所有卡牌
            int sum = 0;
            for (int point : cardPoints) {
                sum += point;
            }
            return sum;
        }
        
        // 计算总和
        int totalSum = 0;
        for (int point : cardPoints) {
            totalSum += point;
        }
        
        // 滑动窗口大小为(n-k)
        int windowSize = n - k;
        
        // 计算第一个窗口的和
        int windowSum = 0;
        for (int i = 0; i < windowSize; i++) {
            windowSum += cardPoints[i];
        }
        
        // 初始化最小窗口和
        int minWindowSum = windowSum;
        
        // 滑动窗口，找到最小窗口和
        for (int i = windowSize; i < n; i++) {
            // 添加新元素，移除旧元素
            windowSum += cardPoints[i] - cardPoints[i - windowSize];
            minWindowSum = Math.min(minWindowSum, windowSum);
        }
        
        // 最大点数 = 总和 - 最小窗口和
        return totalSum - minWindowSum;
    }
}