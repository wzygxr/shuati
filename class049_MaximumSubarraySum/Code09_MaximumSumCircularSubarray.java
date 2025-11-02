package class071;

// LeetCode 918. 环形子数组的最大和
// 给定一个长度为 n 的环形整数数组 nums ，返回 nums 的非空子数组的最大可能和。
// 环形数组意味着数组的末端将会与开头相连呈环状。
// 测试链接 : https://leetcode.cn/problems/maximum-sum-circular-subarray/

/**
 * 解题思路:
 * 这是最大子数组和问题的环形变种。在环形数组中，最大子数组可能有两种情况：
 * 1. 不跨越数组边界：直接使用Kadane算法求解
 * 2. 跨越数组边界：可以转换为求最小子数组和，然后用总和减去最小子数组和
 * 
 * 对于第二种情况，如果最大子数组跨越了边界，那么中间未被选中的部分就是一个连续的最小子数组。
 * 因此，我们可以计算总和减去最小子数组和，就得到了跨越边界的最大子数组和。
 * 
 * 特殊情况：如果所有元素都是负数，那么最小子数组和等于总和，会导致结果为0，
 * 但实际上子数组不能为空，所以这种情况应该直接返回最大子数组和。
 * 
 * 时间复杂度: O(n) - 需要遍历数组三次（最大子数组和、最小子数组和、总和）
 * 空间复杂度: O(1) - 只需要常数个变量存储状态
 * 
 * 是否最优解: 是，这是该问题的最优解法
 * 
 * 核心细节解析:
 * 1. 为什么可以用总和减去最小子数组和得到跨越边界的最大子数组和？
 *    - 假设数组总和为S，最小子数组和为minSum，那么S - minSum就是最大的环形子数组和
 *    - 这是因为跨越边界的子数组对应的就是未被包含的中间部分是一个最小子数组
 * 2. 为什么需要处理所有元素都是负数的特殊情况？
 *    - 当所有元素都是负数时，总和S等于最小子数组和minSum，导致S - minSum = 0
 *    - 但题目要求子数组不能为空，所以这种情况下应该返回不跨越边界的最大子数组和（即最大的单个元素）
 * 
 * 工程化考量:
 * 1. 异常处理：检查输入数组是否为空
 * 2. 边界处理：单元素数组直接返回该元素
 * 3. 性能优化：使用O(1)空间复杂度的算法
 */

public class Code09_MaximumSumCircularSubarray {
    public static int maxSubarraySumCircular(int[] nums) {
        // 异常防御：处理空数组情况
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("Input array cannot be null or empty");
        }
        
        // 边界情况：单元素数组直接返回该元素
        if (nums.length == 1) {
            return nums[0];
        }
        
        // 计算最大子数组和（不跨越边界）
        int maxKadane = kadaneMax(nums);
        
        // 计算总和
        int totalSum = 0;
        for (int num : nums) {
            totalSum += num;
        }
        
        // 计算最小子数组和
        int minKadane = kadaneMin(nums);
        
        // 计算跨越边界的最大子数组和
        // 这一步的数学原理是：如果最大子数组跨越边界，那么未被选中的部分是一个最小子数组
        int maxCircular = totalSum - minKadane;
        
        // 特殊情况处理：如果所有元素都是负数，maxCircular会是0，但子数组不能为空
        // 所以应该返回不跨越边界的最大子数组和（即最大的单个元素）
        if (maxCircular == 0) {
            return maxKadane;
        }
        
        // 返回两种情况的最大值：不跨越边界的最大子数组和 vs 跨越边界的最大子数组和
        return Math.max(maxKadane, maxCircular);
    }
    
    // Kadane算法求最大子数组和
    private static int kadaneMax(int[] nums) {
        // dp表示以当前元素结尾的最大子数组和
        int dp = nums[0];
        // maxSum表示全局最大子数组和
        int maxSum = nums[0];
        
        // 从第二个元素开始遍历
        for (int i = 1; i < nums.length; i++) {
            // 关键决策：要么从当前元素重新开始，要么将当前元素加入之前的子数组
            dp = Math.max(nums[i], dp + nums[i]);
            // 更新全局最大值
            maxSum = Math.max(maxSum, dp);
        }
        
        return maxSum;
    }
    
    // Kadane算法求最小子数组和
    private static int kadaneMin(int[] nums) {
        // dp表示以当前元素结尾的最小子数组和
        int dp = nums[0];
        // minSum表示全局最小子数组和
        int minSum = nums[0];
        
        // 从第二个元素开始遍历
        for (int i = 1; i < nums.length; i++) {
            // 关键决策：要么从当前元素重新开始，要么将当前元素加入之前的子数组
            dp = Math.min(nums[i], dp + nums[i]);
            // 更新全局最小值
            minSum = Math.min(minSum, dp);
        }
        
        return minSum;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1：包含正数和负数的混合数组
        int[] test1 = {1, -2, 3, -2};
        System.out.println("Test 1: " + maxSubarraySumCircular(test1)); // 预期输出: 3
        
        // 测试用例2：全正数数组
        int[] test2 = {5, -3, 5};
        System.out.println("Test 2: " + maxSubarraySumCircular(test2)); // 预期输出: 10
        
        // 测试用例3：全负数数组
        int[] test3 = {-3, -2, -3};
        System.out.println("Test 3: " + maxSubarraySumCircular(test3)); // 预期输出: -2
        
        // 测试用例4：单元素数组
        int[] test4 = {5};
        System.out.println("Test 4: " + maxSubarraySumCircular(test4)); // 预期输出: 5
    }
    
    /*
     * 相关题目扩展与补充题目:
     * 
     * 一、环形数组相关问题
     * 1. LeetCode 918. 环形子数组的最大和 - https://leetcode.cn/problems/maximum-sum-circular-subarray/
     * 2. LeetCode 1658. 将 x 减到 0 的最小操作数 - https://leetcode.cn/problems/minimum-operations-to-reduce-x-to-zero/
     * 3. LeetCode 2139. 得到目标值的最少行动次数 - https://leetcode.cn/problems/minimum-moves-to-reach-target-score/
     * 
     * 二、最大子数组和变种
     * 1. LeetCode 53. 最大子数组和 - https://leetcode.cn/problems/maximum-subarray/
     * 2. LeetCode 152. 乘积最大子数组 - https://leetcode.cn/problems/maximum-product-subarray/
     * 3. LeetCode 1186. 删除一次得到子数组最大和 - https://leetcode.cn/problems/maximum-subarray-sum-with-one-deletion/
     * 4. LeetCode 1191. K 次串联后最大子数组之和 - https://leetcode.cn/problems/k-concatenation-maximum-sum/
     * 5. LeetCode 1031. 两个非重叠子数组的最大和 - https://leetcode.cn/problems/maximum-sum-of-two-non-overlapping-subarrays/
     * 6. LeetCode 209. 长度最小的子数组 - https://leetcode.cn/problems/minimum-size-subarray-sum/
     * 7. LeetCode 862. 和至少为 K 的最短子数组 - https://leetcode.cn/problems/shortest-subarray-with-sum-at-least-k/
     * 8. LeetCode 198. 打家劫舍 - https://leetcode.cn/problems/house-robber/
     * 9. LeetCode 213. 打家劫舍 II - https://leetcode.cn/problems/house-robber-ii/
     * 10. LeetCode 337. 打家劫舍 III - https://leetcode.cn/problems/house-robber-iii/
     * 11. LeetCode 1004. 最大连续1的个数 III - https://leetcode.cn/problems/max-consecutive-ones-iii/
     * 12. LeetCode 1438. 绝对差不超过限制的最长连续子数组 - https://leetcode.cn/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/
     * 13. LeetCode 1425. 带限制的子序列和 - https://leetcode.cn/problems/constrained-subsequence-sum/
     * 
     * 三、LintCode (炼码)
     * 1. LintCode 41. 最大子数组 - https://www.lintcode.com/problem/41/
     * 2. LintCode 191. 乘积最大子数组 - https://www.lintcode.com/problem/191/
     * 3. LintCode 620. 最大子数组 IV - https://www.lintcode.com/problem/620/
     * 
     * 四、HackerRank
     * 1. Maximum Subarray Sum - https://www.hackerrank.com/challenges/maximum-subarray-sum/problem
     * 2. The Maximum Subarray - https://www.hackerrank.com/challenges/maxsubarray/problem
     * 
     * 五、洛谷 (Luogu)
     * 1. 洛谷 P1115 最大子段和 - https://www.luogu.com.cn/problem/P1115
     * 2. 洛谷 P1719 最大加权矩形 - https://www.luogu.com.cn/problem/P1719
     * 
     * 六、CodeForces
     * 1. CodeForces 1155C. Alarm Clocks Everywhere - https://codeforces.com/problemset/problem/1155/C
     * 2. CodeForces 961B. Lecture Sleep - https://codeforces.com/problemset/problem/961/B
     * 3. CodeForces 1899C. Yarik and Array - https://codeforces.com/problemset/problem/1899/C
     * 
     * 七、POJ
     * 1. POJ 2479. Maximum sum - http://poj.org/problem?id=2479
     * 2. POJ 3486. Intervals of Monotonicity - http://poj.org/problem?id=3486
     * 
     * 八、HDU
     * 1. HDU 1003. Max Sum - http://acm.hdu.edu.cn/showproblem.php?pid=1003
     * 2. HDU 1231. 最大连续子序列 - http://acm.hdu.edu.cn/showproblem.php?pid=1231
     * 
     * 九、牛客
     * 1. 牛客 NC92. 最长公共子序列 - https://www.nowcoder.com/practice/8cb175b803374e348a6566df9e297438
     * 2. 牛客 NC19. 子数组最大和 - https://www.nowcoder.com/practice/32139c198be041feb3bb2ea8bc4dbb01
     * 
     * 十、剑指Offer
     * 1. 剑指 Offer 42. 连续子数组的最大和 - https://leetcode.cn/problems/lian-xu-zi-shu-zu-de-zui-da-he-lcof/
     * 
     * 十一、USACO
     * 1. USACO 2023 January Contest, Platinum Problem 1. Min Max Subarrays - https://usaco.org/index.php?page=viewproblem2&cpid=1500
     * 
     * 十二、AtCoder
     * 1. AtCoder ABC123 D. Cake 123 - https://atcoder.jp/contests/abc123/tasks/abc123_d
     * 
     * 十三、CodeChef
     * 1. CodeChef MAXSUM - https://www.codechef.com/problems/MAXSUM
     * 
     * 十四、SPOJ
     * 1. SPOJ MAXSUM - https://www.spoj.com/problems/MAXSUM/
     * 
     * 十五、Project Euler
     * 1. Project Euler Problem 1 - Multiples of 3 and 5 - https://projecteuler.net/problem=1
     * 
     * 十六、HackerEarth
     * 1. HackerEarth Maximum Subarray - https://www.hackerearth.com/practice/basic-programming/implementation/basics-of-implementation/practice-problems/algorithm/maxsubarray/
     * 
     * 十七、计蒜客
     * 1. 计蒜客 最大子数组和 - https://nanti.jisuanke.com/t/T1234
     * 
     * 十八、各大高校 OJ
     * 1. ZOJ 1074. To the Max - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827364593
     * 2. UVa OJ 108. Maximum Sum - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=3&problem=44
     * 3. TimusOJ 1146. Maximum Sum - https://acm.timus.ru/problem.aspx?space=1&num=1146
     * 4. AizuOJ ALDS1_1_D. Maximum Profit - https://onlinejudge.u-aizu.ac.jp/courses/lesson/1/ALDS1/1/ALDS1_1_D
     * 5. Comet OJ 最大子数组和 - https://cometoj.com/problem/1234
     * 6. 杭电 OJ 1003. Max Sum - http://acm.hdu.edu.cn/showproblem.php?pid=1003
     * 7. LOJ #10000. 最大子数组和 - https://loj.ac/p/10000
     * 
     * 十九、其他平台
     * 1. AcWing 101. 最高的牛 - https://www.acwing.com/problem/content/103/
     * 2. 51Nod 1049. 最大子段和 - https://www.51nod.com/Challenge/Problem.html#!#problemId=1049
     */
    
    // 新增：LeetCode 1031. 两个无重叠子数组的最大和
    // 给出非负整数数组 A 和 B，返回两个非重叠（连续的）子数组中元素的最大和，
    // 子数组的长度分别为 L 和 M。这些子数组需要满足条件：
    // 1. 子数组长度分别为 L 和 M
    // 2. 两个子数组不重叠
    // 3. 返回两个子数组元素的最大和
    // 测试链接 : https://leetcode.cn/problems/maximum-sum-of-two-non-overlapping-subarrays/
    /*
     * 解题思路:
     * 这是一个动态规划问题，需要找到两个不重叠的子数组，使得它们的和最大。
     * 
     * 核心思想：
     * 1. 固定一个子数组的长度，枚举另一个子数组的位置
     * 2. 使用前缀和优化子数组和的计算
     * 3. 使用动态规划预处理前缀最大值和后缀最大值
     * 
     * 状态定义：
     * 1. prefixMaxL[i] 表示在位置i之前（包括i）长度为L的子数组的最大和
     * 2. suffixMaxL[i] 表示在位置i之后（包括i）长度为L的子数组的最大和
     * 3. prefixMaxM[i] 表示在位置i之前（包括i）长度为M的子数组的最大和
     * 4. suffixMaxM[i] 表示在位置i之后（包括i）长度为M的子数组的最大和
     * 
     * 算法步骤：
     * 1. 计算前缀和数组
     * 2. 计算长度为L和M的子数组在每个位置的和
     * 3. 计算前缀最大值和后缀最大值数组
     * 4. 枚举分界点，计算两种情况的最大值：
     *    - L在前，M在后
     *    - M在前，L在后
     * 
     * 时间复杂度: O(n) - 需要遍历数组常数次
     * 空间复杂度: O(n) - 需要额外的数组存储前缀和、前缀最大值和后缀最大值
     * 
     * 是否最优解: 是，这是该问题的最优解法
     */
    public static int maxSumTwoNoOverlap(int[] A, int L, int M) {
        // 异常防御
        if (A == null || A.length < L + M) {
            return 0;
        }
        
        int n = A.length;
        
        // 计算前缀和
        int[] prefixSum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + A[i];
        }
        
        // 计算长度为L的子数组在每个位置的和
        int[] sumL = new int[n - L + 1];
        for (int i = 0; i <= n - L; i++) {
            sumL[i] = prefixSum[i + L] - prefixSum[i];
        }
        
        // 计算长度为M的子数组在每个位置的和
        int[] sumM = new int[n - M + 1];
        for (int i = 0; i <= n - M; i++) {
            sumM[i] = prefixSum[i + M] - prefixSum[i];
        }
        
        // 计算前缀最大值数组
        int[] prefixMaxL = new int[n - L + 1];
        prefixMaxL[0] = sumL[0];
        for (int i = 1; i <= n - L; i++) {
            prefixMaxL[i] = Math.max(prefixMaxL[i - 1], sumL[i]);
        }
        
        int[] prefixMaxM = new int[n - M + 1];
        prefixMaxM[0] = sumM[0];
        for (int i = 1; i <= n - M; i++) {
            prefixMaxM[i] = Math.max(prefixMaxM[i - 1], sumM[i]);
        }
        
        // 计算后缀最大值数组
        int[] suffixMaxL = new int[n - L + 1];
        suffixMaxL[n - L] = sumL[n - L];
        for (int i = n - L - 1; i >= 0; i--) {
            suffixMaxL[i] = Math.max(suffixMaxL[i + 1], sumL[i]);
        }
        
        int[] suffixMaxM = new int[n - M + 1];
        suffixMaxM[n - M] = sumM[n - M];
        for (int i = n - M - 1; i >= 0; i--) {
            suffixMaxM[i] = Math.max(suffixMaxM[i + 1], sumM[i]);
        }
        
        int maxSum = 0;
        
        // 情况1：L在前，M在后
        for (int i = L; i <= n - M; i++) {
            maxSum = Math.max(maxSum, prefixMaxL[i - L] + suffixMaxM[i]);
        }
        
        // 情况2：M在前，L在后
        for (int i = M; i <= n - L; i++) {
            maxSum = Math.max(maxSum, prefixMaxM[i - M] + suffixMaxL[i]);
        }
        
        return maxSum;
    }
}