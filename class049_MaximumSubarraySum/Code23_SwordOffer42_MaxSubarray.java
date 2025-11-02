package class071;

// 剑指 Offer 42. 连续子数组的最大和
// 输入一个整型数组，数组中的一个或连续多个整数组成一个子数组。求所有子数组的和的最大值。
// 要求时间复杂度为O(n)。
// 测试链接 : https://leetcode.cn/problems/lian-xu-zi-shu-zu-de-zui-da-he-lcof/

/**
 * 解题思路:
 * 这是经典的Kadane算法问题，也是面试中的高频题目。
 * 使用动态规划思想，维护以当前元素结尾的最大子数组和。
 * 
 * 核心思想:
 * 1. 定义dp[i]为以nums[i]结尾的最大子数组和
 * 2. 状态转移方程：dp[i] = max(nums[i], dp[i-1] + nums[i])
 * 3. 在整个过程中维护全局最大值
 * 4. 使用滚动数组优化空间复杂度
 * 
 * 时间复杂度: O(n) - 需要遍历数组一次
 * 空间复杂度: O(1) - 使用滚动数组优化
 * 
 * 是否最优解: 是，这是该问题的最优解法
 * 
 * 核心细节解析:
 * 1. 为什么状态转移方程是max(nums[i], dp[i-1] + nums[i])？
 *    - 如果dp[i-1]是负数，那么从当前元素重新开始会更好
 *    - 如果dp[i-1]是正数，那么将当前元素加入之前的子数组会更好
 * 2. 如何初始化？
 *    - dp[0] = nums[0]，maxSum = nums[0]
 * 3. 边界处理：空数组、全负数数组等特殊情况
 * 
 * 工程化考量:
 * 1. 异常处理：输入数组为null或空的情况
 * 2. 性能优化：使用O(1)空间复杂度的算法
 * 3. 代码可读性：清晰的变量命名和注释
 */

public class Code23_SwordOffer42_MaxSubarray {
    
    public static int maxSubArray(int[] nums) {
        // 异常防御
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("Input array cannot be null or empty");
        }
        
        int n = nums.length;
        if (n == 1) {
            return nums[0];
        }
        
        // 使用滚动数组优化空间复杂度
        int dp = nums[0];        // 以当前元素结尾的最大子数组和
        int maxSum = nums[0];    // 全局最大子数组和
        
        for (int i = 1; i < n; i++) {
            // 关键决策：要么从当前元素重新开始，要么将当前元素加入之前的子数组
            dp = Math.max(nums[i], dp + nums[i]);
            // 更新全局最大值
            maxSum = Math.max(maxSum, dp);
        }
        
        return maxSum;
    }
    
    // 方法2：更直观的实现方式
    public static int maxSubArray2(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("Input array cannot be null or empty");
        }
        
        int maxSum = nums[0];
        int currentSum = nums[0];
        
        for (int i = 1; i < nums.length; i++) {
            // 如果当前和小于0，从当前元素重新开始
            if (currentSum < 0) {
                currentSum = nums[i];
            } else {
                currentSum += nums[i];
            }
            
            // 更新全局最大值
            if (currentSum > maxSum) {
                maxSum = currentSum;
            }
        }
        
        return maxSum;
    }
    
    // 方法3：包含详细调试信息的版本（面试时可用于解释思路）
    public static int maxSubArrayWithDebug(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("Input array cannot be null or empty");
        }
        
        int dp = nums[0];
        int maxSum = nums[0];
        
        System.out.println("初始状态: dp = " + dp + ", maxSum = " + maxSum);
        
        for (int i = 1; i < nums.length; i++) {
            int option1 = nums[i];           // 选项1：从当前元素重新开始
            int option2 = dp + nums[i];      // 选项2：加入之前的子数组
            
            System.out.println("位置 " + i + ": nums[" + i + "] = " + nums[i] + 
                             ", 选项1 = " + option1 + ", 选项2 = " + option2);
            
            dp = Math.max(option1, option2);
            maxSum = Math.max(maxSum, dp);
            
            System.out.println("选择: dp = " + dp + ", maxSum = " + maxSum);
        }
        
        return maxSum;
    }
    
    // 新增：测试方法
    public static void main(String[] args) {
        // 测试用例1：剑指Offer样例
        int[] nums1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        System.out.println("测试用例1:");
        System.out.println("数组: [-2, 1, -3, 4, -1, 2, 1, -5, 4]");
        System.out.println("最大子数组和（方法1）: " + maxSubArray(nums1)); // 预期输出: 6
        System.out.println("最大子数组和（方法2）: " + maxSubArray2(nums1)); // 预期输出: 6
        
        // 测试用例2：全正数
        int[] nums2 = {1, 2, 3, 4, 5};
        System.out.println("\n测试用例2:");
        System.out.println("数组: [1, 2, 3, 4, 5]");
        System.out.println("最大子数组和（方法1）: " + maxSubArray(nums2)); // 预期输出: 15
        System.out.println("最大子数组和（方法2）: " + maxSubArray2(nums2)); // 预期输出: 15
        
        // 测试用例3：全负数
        int[] nums3 = {-1, -2, -3, -4, -5};
        System.out.println("\n测试用例3:");
        System.out.println("数组: [-1, -2, -3, -4, -5]");
        System.out.println("最大子数组和（方法1）: " + maxSubArray(nums3)); // 预期输出: -1
        System.out.println("最大子数组和（方法2）: " + maxSubArray2(nums3)); // 预期输出: -1
        
        // 测试用例4：调试版本
        int[] nums4 = {1, -2, 3, 10, -4, 7, 2, -5};
        System.out.println("\n测试用例4（调试版本）:");
        System.out.println("数组: [1, -2, 3, 10, -4, 7, 2, -5]");
        System.out.println("最大子数组和: " + maxSubArrayWithDebug(nums4)); // 预期输出: 18
    }
    
    /*
     * 相关题目扩展与补充题目:
     * 
     * 一、最大子数组和相关问题
     * 1. 剑指 Offer 42. 连续子数组的最大和 - https://leetcode.cn/problems/lian-xu-zi-shu-zu-de-zui-da-he-lcof/
     * 2. LeetCode 53. 最大子数组和 - https://leetcode.cn/problems/maximum-subarray/
     * 3. LeetCode 152. 乘积最大子数组 - https://leetcode.cn/problems/maximum-product-subarray/
     * 4. 牛客 NC19. 子数组的最大累加和问题 - https://www.nowcoder.com/practice/554aa508dd5d4fefbf0f86e56e7dc785
     * 5. HDU 1003. Max Sum - http://acm.hdu.edu.cn/showproblem.php?pid=1003
     * 6. LeetCode 918. 环形子数组的最大和 - https://leetcode.cn/problems/maximum-sum-circular-subarray/
     * 7. LeetCode 1186. 删除一次得到子数组最大和 - https://leetcode.cn/problems/maximum-subarray-sum-with-one-deletion/
     * 8. LeetCode 1191. K 次串联后最大子数组之和 - https://leetcode.cn/problems/k-concatenation-maximum-sum/
     * 9. LeetCode 1031. 两个非重叠子数组的最大和 - https://leetcode.cn/problems/maximum-sum-of-two-non-overlapping-subarrays/
     * 10. LeetCode 198. 打家劫舍 - https://leetcode.cn/problems/house-robber/
     * 11. LeetCode 213. 打家劫舍 II - https://leetcode.cn/problems/house-robber-ii/
     * 12. LeetCode 337. 打家劫舍 III - https://leetcode.cn/problems/house-robber-iii/
     * 
     * 二、滑动窗口相关问题
     * 1. LeetCode 209. 长度最小的子数组 - https://leetcode.cn/problems/minimum-size-subarray-sum/
     * 2. LeetCode 862. 和至少为 K 的最短子数组 - https://leetcode.cn/problems/shortest-subarray-with-sum-at-least-k/
     * 3. LeetCode 1004. 最大连续1的个数 III - https://leetcode.cn/problems/max-consecutive-ones-iii/
     * 4. LeetCode 3. 无重复字符的最长子串 - https://leetcode.cn/problems/longest-substring-without-repeating-characters/
     * 5. LeetCode 76. 最小覆盖子串 - https://leetcode.cn/problems/minimum-window-substring/
     * 6. LeetCode 438. 找到字符串中所有字母异位词 - https://leetcode.cn/problems/find-all-anagrams-in-a-string/
     * 7. LeetCode 567. 字符串的排列 - https://leetcode.cn/problems/permutation-in-string/
     * 8. LeetCode 1438. 绝对差不超过限制的最长连续子数组 - https://leetcode.cn/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/
     * 9. LeetCode 1425. 带限制的子序列和 - https://leetcode.cn/problems/constrained-subsequence-sum/
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
    
    // 新增：LeetCode 918. 环形子数组的最大和
    // 给定一个长度为 n 的环形整数数组 nums ，返回 nums 的非空子数组的最大可能和。
    // 环形数组意味着数组的末端将会与开头相连呈环状。
    // 测试链接 : https://leetcode.cn/problems/maximum-sum-circular-subarray/
    /*
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
     */
    public static int maxSubarraySumCircular(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("Input array cannot be null or empty");
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
        int maxCircular = totalSum - minKadane;
        
        // 特殊情况处理：如果所有元素都是负数，maxCircular会是0，但子数组不能为空
        if (maxCircular == 0) {
            return maxKadane;
        }
        
        // 返回两种情况的最大值
        return Math.max(maxKadane, maxCircular);
    }
    
    // Kadane算法求最大子数组和
    private static int kadaneMax(int[] nums) {
        int dp = nums[0];
        int maxSum = nums[0];
        
        for (int i = 1; i < nums.length; i++) {
            dp = Math.max(nums[i], dp + nums[i]);
            maxSum = Math.max(maxSum, dp);
        }
        
        return maxSum;
    }
    
    // Kadane算法求最小子数组和
    private static int kadaneMin(int[] nums) {
        int dp = nums[0];
        int minSum = nums[0];
        
        for (int i = 1; i < nums.length; i++) {
            dp = Math.min(nums[i], dp + nums[i]);
            minSum = Math.min(minSum, dp);
        }
        
        return minSum;
    }
}