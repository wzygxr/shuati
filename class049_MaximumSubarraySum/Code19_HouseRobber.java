package class071;

// LeetCode 198. 打家劫舍
// 你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，
// 影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，
// 如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
// 给定一个代表每个房屋存放金额的非负整数数组，计算你不触动警报装置的情况下，一夜之内能够偷窃到的最高金额。
// 测试链接 : https://leetcode.cn/problems/house-robber/

/**
 * 解题思路:
 * 这是一个经典的动态规划问题，属于线性DP的一种。
 * 
 * 核心思想:
 * 1. 定义dp[i]为偷窃前i间房屋能获得的最大金额
 * 2. 对于第i间房屋，有两种选择：
 *    - 不偷第i间房屋：最大金额为dp[i-1]
 *    - 偷第i间房屋：最大金额为dp[i-2] + nums[i]
 * 3. 状态转移方程：dp[i] = max(dp[i-1], dp[i-2] + nums[i])
 * 
 * 时间复杂度: O(n) - 需要遍历数组一次
 * 空间复杂度: O(n) - 可以使用滚动数组优化到O(1)
 * 
 * 是否最优解: 是，这是该问题的最优解法
 * 
 * 核心细节解析:
 * 1. 为什么状态转移方程是dp[i] = max(dp[i-1], dp[i-2] + nums[i])？
 *    - 如果偷第i间房屋，就不能偷第i-1间，所以只能从dp[i-2]转移
 *    - 如果不偷第i间房屋，最大金额就是dp[i-1]
 * 2. 如何初始化？
 *    - dp[0] = nums[0]（只有一间房屋）
 *    - dp[1] = max(nums[0], nums[1])（两间房屋取最大值）
 * 3. 空间优化：由于当前状态只依赖于前两个状态，可以使用两个变量代替数组
 * 
 * 工程化考量:
 * 1. 异常处理：空数组、单元素数组等边界情况
 * 2. 数值范围：使用int足够，因为金额是非负整数
 * 3. 代码可读性：清晰的变量命名和注释
 */

public class Code19_HouseRobber {
    
    // 方法1：使用数组的经典DP解法
    public static int rob(int[] nums) {
        // 异常防御
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        if (n == 1) {
            return nums[0];
        }
        
        // 创建dp数组
        int[] dp = new int[n];
        
        // 初始化
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]);
        
        // 状态转移
        for (int i = 2; i < n; i++) {
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
        }
        
        return dp[n - 1];
    }
    
    // 方法2：空间优化版本（滚动数组）
    public static int robOptimized(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        if (n == 1) {
            return nums[0];
        }
        
        // 使用两个变量代替数组
        int prev2 = nums[0];           // dp[i-2]
        int prev1 = Math.max(nums[0], nums[1]); // dp[i-1]
        
        for (int i = 2; i < n; i++) {
            int current = Math.max(prev1, prev2 + nums[i]);
            prev2 = prev1;
            prev1 = current;
        }
        
        return prev1;
    }
    
    // 方法3：更通用的空间优化版本
    public static int robBest(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        if (n == 1) {
            return nums[0];
        }
        
        int prev = 0;    // dp[i-2]
        int curr = 0;    // dp[i-1]
        
        for (int num : nums) {
            int temp = curr;
            curr = Math.max(curr, prev + num);
            prev = temp;
        }
        
        return curr;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1：正常情况
        int[] nums1 = {1, 2, 3, 1};
        System.out.println("测试用例1:");
        System.out.println("房屋金额: [1, 2, 3, 1]");
        System.out.println("最大金额（方法1）: " + rob(nums1)); // 预期输出: 4
        System.out.println("最大金额（方法2）: " + robOptimized(nums1)); // 预期输出: 4
        System.out.println("最大金额（方法3）: " + robBest(nums1)); // 预期输出: 4
        
        // 测试用例2：间隔偷窃
        int[] nums2 = {2, 7, 9, 3, 1};
        System.out.println("\n测试用例2:");
        System.out.println("房屋金额: [2, 7, 9, 3, 1]");
        System.out.println("最大金额（方法1）: " + rob(nums2)); // 预期输出: 12
        System.out.println("最大金额（方法2）: " + robOptimized(nums2)); // 预期输出: 12
        System.out.println("最大金额（方法3）: " + robBest(nums2)); // 预期输出: 12
        
        // 测试用例3：两间房屋
        int[] nums3 = {2, 1};
        System.out.println("\n测试用例3:");
        System.out.println("房屋金额: [2, 1]");
        System.out.println("最大金额（方法1）: " + rob(nums3)); // 预期输出: 2
        System.out.println("最大金额（方法2）: " + robOptimized(nums3)); // 预期输出: 2
        System.out.println("最大金额（方法3）: " + robBest(nums3)); // 预期输出: 2
        
        // 测试用例4：单间房屋
        int[] nums4 = {5};
        System.out.println("\n测试用例4:");
        System.out.println("房屋金额: [5]");
        System.out.println("最大金额（方法1）: " + rob(nums4)); // 预期输出: 5
        System.out.println("最大金额（方法2）: " + robOptimized(nums4)); // 预期输出: 5
        System.out.println("最大金额（方法3）: " + robBest(nums4)); // 预期输出: 5
    }
    
    /*
     * 相关题目扩展与补充题目:
     * 
     * 一、打家劫舍系列问题
     * 1. LeetCode 198. 打家劫舍 - https://leetcode.cn/problems/house-robber/
     * 2. LeetCode 213. 打家劫舍 II - https://leetcode.cn/problems/house-robber-ii/
     * 3. LeetCode 337. 打家劫舍 III - https://leetcode.cn/problems/house-robber-iii/
     * 4. LeetCode 740. 删除并获得点数 - https://leetcode.cn/problems/delete-and-earn/
     * 5. LeetCode 1388. 3n 块披萨 - https://leetcode.cn/problems/pizza-with-3n-slices/
     * 
     * 二、最大子数组和变种
     * 1. LeetCode 53. 最大子数组和 - https://leetcode.cn/problems/maximum-subarray/
     * 2. LeetCode 152. 乘积最大子数组 - https://leetcode.cn/problems/maximum-product-subarray/
     * 3. LeetCode 918. 环形子数组的最大和 - https://leetcode.cn/problems/maximum-sum-circular-subarray/
     * 4. LeetCode 1186. 删除一次得到子数组最大和 - https://leetcode.cn/problems/maximum-subarray-sum-with-one-deletion/
     * 5. LeetCode 1191. K 次串联后最大子数组之和 - https://leetcode.cn/problems/k-concatenation-maximum-sum/
     * 6. LeetCode 1031. 两个非重叠子数组的最大和 - https://leetcode.cn/problems/maximum-sum-of-two-non-overlapping-subarrays/
     * 7. LeetCode 209. 长度最小的子数组 - https://leetcode.cn/problems/minimum-size-subarray-sum/
     * 8. LeetCode 862. 和至少为 K 的最短子数组 - https://leetcode.cn/problems/shortest-subarray-with-sum-at-least-k/
     * 
     * 三、滑动窗口相关问题
     * 1. LeetCode 1004. 最大连续1的个数 III - https://leetcode.cn/problems/max-consecutive-ones-iii/
     * 2. LeetCode 3. 无重复字符的最长子串 - https://leetcode.cn/problems/longest-substring-without-repeating-characters/
     * 3. LeetCode 76. 最小覆盖子串 - https://leetcode.cn/problems/minimum-window-substring/
     * 4. LeetCode 438. 找到字符串中所有字母异位词 - https://leetcode.cn/problems/find-all-anagrams-in-a-string/
     * 5. LeetCode 567. 字符串的排列 - https://leetcode.cn/problems/permutation-in-string/
     * 6. LeetCode 1438. 绝对差不超过限制的最长连续子数组 - https://leetcode.cn/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/
     * 7. LeetCode 1425. 带限制的子序列和 - https://leetcode.cn/problems/constrained-subsequence-sum/
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
     * 
     * 八、POJ
     * 1. POJ 2479. Maximum sum - http://poj.org/problem?id=2479
     * 2. POJ 3486. Intervals of Monotonicity - http://poj.org/problem?id=3486
     * 
     * 九、HDU
     * 1. HDU 1003. Max Sum - http://acm.hdu.edu.cn/showproblem.php?pid=1003
     * 2. HDU 1231. 最大连续子序列 - http://acm.hdu.edu.cn/showproblem.php?pid=1231
     * 
     * 十、牛客
     * 1. 牛客 NC92. 最长公共子序列 - https://www.nowcoder.com/practice/8cb175b803374e348a6566df9e297438
     * 2. 牛客 NC19. 子数组最大和 - https://www.nowcoder.com/practice/32139c198be041feb3bb2ea8bc4dbb01
     * 
     * 十一、剑指Offer
     * 1. 剑指 Offer 42. 连续子数组的最大和 - https://leetcode.cn/problems/lian-xu-zi-shu-zu-de-zui-da-he-lcof/
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
    
    // 新增：LeetCode 213. 打家劫舍 II
    // 你是一个专业的小偷，计划偷窃沿街的房屋，每间房内都藏有一定的现金。
    // 这个地方所有的房屋都围成一圈，这意味着第一个房屋和最后一个房屋是紧挨着的。
    // 同时，相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
    // 给定一个代表每个房屋存放金额的非负整数数组，计算你在不触动警报装置的情况下，今晚能够偷窃到的最高金额。
    // 测试链接 : https://leetcode.cn/problems/house-robber-ii/
    /*
     * 解题思路:
     * 这是打家劫舍问题的环形变种。由于房屋围成一圈，第一个房屋和最后一个房屋不能同时偷窃。
     * 
     * 解决方案：
     * 将环形问题分解为两个线性问题：
     * 1. 不偷窃第一个房屋：问题转化为在房屋[1, n-1]上打家劫舍
     * 2. 不偷窃最后一个房屋：问题转化为在房屋[0, n-2]上打家劫舍
     * 3. 返回两种情况的最大值
     * 
     * 核心思想：
     * 1. 环形结构的处理技巧：分解为两个线性问题
     * 2. 复用打家劫舍I的解法
     * 
     * 时间复杂度: O(n) - 需要遍历数组两次
     * 空间复杂度: O(1) - 使用空间优化的版本
     * 
     * 是否最优解: 是，这是该问题的最优解法
     */
    public static int robII(int[] nums) {
        // 异常防御
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        if (n == 1) {
            return nums[0];
        }
        
        if (n == 2) {
            return Math.max(nums[0], nums[1]);
        }
        
        // 情况1：不偷窃第一个房屋，问题转化为在房屋[1, n-1]上打家劫舍
        int max1 = robRange(nums, 1, n - 1);
        
        // 情况2：不偷窃最后一个房屋，问题转化为在房屋[0, n-2]上打家劫舍
        int max2 = robRange(nums, 0, n - 2);
        
        // 返回两种情况的最大值
        return Math.max(max1, max2);
    }
    
    // 辅助方法：在指定范围内打家劫舍
    private static int robRange(int[] nums, int start, int end) {
        int prev = 0, curr = 0;
        
        for (int i = start; i <= end; i++) {
            int temp = curr;
            curr = Math.max(curr, prev + nums[i]);
            prev = temp;
        }
        
        return curr;
    }
}