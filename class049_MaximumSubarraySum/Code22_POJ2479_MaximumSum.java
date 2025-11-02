package class071;

// POJ 2479. Maximum sum
// 给定一个整数数组，找到两个不重叠的子数组，使得它们的和最大。
// 输出这两个子数组的和的最大值。
// 测试链接 : http://poj.org/problem?id=2479

/**
 * 解题思路:
 * 这是最大子数组和问题的高级变种，需要找到两个不重叠子数组的最大和。
 * 我们可以将问题分解为：
 * 1. 计算从左到右的每个位置的最大子数组和（前缀最大值）
 * 2. 计算从右到左的每个位置的最大子数组和（后缀最大值）
 * 3. 枚举分界点，计算前缀最大值 + 后缀最大值的最大值
 * 
 * 核心思想:
 * 1. 预处理从左到右的最大子数组和数组leftMax
 *    - leftMax[i]表示数组[0...i]范围内的最大子数组和
 * 2. 预处理从右到左的最大子数组和数组rightMax
 *    - rightMax[i]表示数组[i...n-1]范围内的最大子数组和
 * 3. 枚举分界点i，计算leftMax[i] + rightMax[i+1]的最大值
 * 
 * 时间复杂度: O(n) - 需要遍历数组三次
 * 空间复杂度: O(n) - 需要两个辅助数组
 * 
 * 是否最优解: 是，这是该问题的最优解法
 * 
 * 核心细节解析:
 * 1. 为什么需要leftMax和rightMax数组？
 *    - leftMax[i]确保第一个子数组在[0...i]范围内
 *    - rightMax[i+1]确保第二个子数组在[i+1...n-1]范围内
 *    - 这样两个子数组就不会重叠
 * 2. 如何处理分界点？
 *    - 分界点i表示第一个子数组在i处结束，第二个子数组在i+1处开始
 *    - 需要确保i从0到n-2遍历
 * 3. 边界处理：数组长度小于2的情况
 * 
 * 工程化考量:
 * 1. 异常处理：空数组、单元素数组等边界情况
 * 2. 性能优化：使用O(n)时间复杂度的算法
 * 3. 代码可读性：清晰的变量命名和注释
 */

public class Code22_POJ2479_MaximumSum {
    
    public static int maximumSum(int[] nums) {
        // 异常防御
        if (nums == null || nums.length < 2) {
            throw new IllegalArgumentException("Array must have at least 2 elements");
        }
        
        int n = nums.length;
        
        // 特殊情况：如果只有两个元素，直接返回它们的和
        if (n == 2) {
            return nums[0] + nums[1];
        }
        
        // 1. 计算从左到右的最大子数组和数组
        int[] leftMax = new int[n];
        int currentSum = nums[0];
        leftMax[0] = nums[0];
        
        for (int i = 1; i < n; i++) {
            currentSum = Math.max(nums[i], currentSum + nums[i]);
            leftMax[i] = Math.max(leftMax[i - 1], currentSum);
        }
        
        // 2. 计算从右到左的最大子数组和数组
        int[] rightMax = new int[n];
        currentSum = nums[n - 1];
        rightMax[n - 1] = nums[n - 1];
        
        for (int i = n - 2; i >= 0; i--) {
            currentSum = Math.max(nums[i], currentSum + nums[i]);
            rightMax[i] = Math.max(rightMax[i + 1], currentSum);
        }
        
        // 3. 枚举分界点，计算最大和
        int maxSum = Integer.MIN_VALUE;
        for (int i = 0; i < n - 1; i++) {
            maxSum = Math.max(maxSum, leftMax[i] + rightMax[i + 1]);
        }
        
        return maxSum;
    }
    
    // 方法2：空间优化版本（只存储必要信息）
    public static int maximumSumOptimized(int[] nums) {
        if (nums == null || nums.length < 2) {
            throw new IllegalArgumentException("Array must have at least 2 elements");
        }
        
        int n = nums.length;
        if (n == 2) {
            return nums[0] + nums[1];
        }
        
        // 计算从左到右的最大子数组和（不存储整个数组）
        int[] leftMax = new int[n];
        int current = nums[0];
        leftMax[0] = nums[0];
        
        for (int i = 1; i < n; i++) {
            current = Math.max(nums[i], current + nums[i]);
            leftMax[i] = Math.max(leftMax[i - 1], current);
        }
        
        // 从右到左计算，同时枚举分界点
        int rightMax = nums[n - 1];
        current = nums[n - 1];
        int maxSum = leftMax[n - 2] + rightMax;
        
        for (int i = n - 2; i > 0; i--) {
            current = Math.max(nums[i], current + nums[i]);
            rightMax = Math.max(rightMax, current);
            maxSum = Math.max(maxSum, leftMax[i - 1] + rightMax);
        }
        
        return maxSum;
    }
    
    // 新增：测试方法
    public static void main(String[] args) {
        // 测试用例1：POJ样例
        int[] nums1 = {1, -1, 2, 2, 3, -3, 4, -4, 5, -5};
        System.out.println("测试用例1:");
        System.out.println("数组: [1, -1, 2, 2, 3, -3, 4, -4, 5, -5]");
        System.out.println("最大两个子数组和（方法1）: " + maximumSum(nums1)); // 预期输出: 13
        System.out.println("最大两个子数组和（方法2）: " + maximumSumOptimized(nums1)); // 预期输出: 13
        
        // 测试用例2：正常情况
        int[] nums2 = {1, 2, 3, 4, 5};
        System.out.println("\n测试用例2:");
        System.out.println("数组: [1, 2, 3, 4, 5]");
        System.out.println("最大两个子数组和（方法1）: " + maximumSum(nums2)); // 预期输出: 15
        System.out.println("最大两个子数组和（方法2）: " + maximumSumOptimized(nums2)); // 预期输出: 15
        
        // 测试用例3：包含负数
        int[] nums3 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        System.out.println("\n测试用例3:");
        System.out.println("数组: [-2, 1, -3, 4, -1, 2, 1, -5, 4]");
        System.out.println("最大两个子数组和（方法1）: " + maximumSum(nums3)); // 预期输出: 10
        System.out.println("最大两个子数组和（方法2）: " + maximumSumOptimized(nums3)); // 预期输出: 10
        
        // 测试用例4：两个元素
        int[] nums4 = {5, 8};
        System.out.println("\n测试用例4:");
        System.out.println("数组: [5, 8]");
        System.out.println("最大两个子数组和（方法1）: " + maximumSum(nums4)); // 预期输出: 13
        System.out.println("最大两个子数组和（方法2）: " + maximumSumOptimized(nums4)); // 预期输出: 13
    }
    
    /*
     * 相关题目扩展与补充题目:
     * 
     * 一、两个子数组相关问题
     * 1. POJ 2479. Maximum sum - http://poj.org/problem?id=2479
     * 2. LeetCode 1031. 两个非重叠子数组的最大和 - https://leetcode.cn/problems/maximum-sum-of-two-non-overlapping-subarrays/
     * 3. HDU 1003. Max Sum - http://acm.hdu.edu.cn/showproblem.php?pid=1003
     * 4. HDU 1231. 最大连续子序列 - http://acm.hdu.edu.cn/showproblem.php?pid=1231
     * 
     * 二、最大子数组和相关问题
     * 1. LeetCode 53. 最大子数组和 - https://leetcode.cn/problems/maximum-subarray/
     * 2. LeetCode 152. 乘积最大子数组 - https://leetcode.cn/problems/maximum-product-subarray/
     * 3. LeetCode 918. 环形子数组的最大和 - https://leetcode.cn/problems/maximum-sum-circular-subarray/
     * 4. LeetCode 1186. 删除一次得到子数组最大和 - https://leetcode.cn/problems/maximum-subarray-sum-with-one-deletion/
     * 5. LeetCode 1191. K 次串联后最大子数组之和 - https://leetcode.cn/problems/k-concatenation-maximum-sum/
     * 6. LeetCode 1425. 带限制的子序列和 - https://leetcode.cn/problems/constrained-subsequence-sum/
     * 7. LeetCode 862. 和至少为 K 的最短子数组 - https://leetcode.cn/problems/shortest-subarray-with-sum-at-least-k/
     * 8. LeetCode 198. 打家劫舍 - https://leetcode.cn/problems/house-robber/
     * 9. LeetCode 213. 打家劫舍 II - https://leetcode.cn/problems/house-robber-ii/
     * 10. LeetCode 337. 打家劫舍 III - https://leetcode.cn/problems/house-robber-iii/
     * 11. LeetCode 740. 删除并获得点数 - https://leetcode.cn/problems/delete-and-earn/
     * 12. LeetCode 1388. 3n 块披萨 - https://leetcode.cn/problems/pizza-with-3n-slices/
     * 
     * 三、滑动窗口相关问题
     * 1. LeetCode 209. 长度最小的子数组 - https://leetcode.cn/problems/minimum-size-subarray-sum/
     * 2. LeetCode 1004. 最大连续1的个数 III - https://leetcode.cn/problems/max-consecutive-ones-iii/
     * 3. LeetCode 3. 无重复字符的最长子串 - https://leetcode.cn/problems/longest-substring-without-repeating-characters/
     * 4. LeetCode 76. 最小覆盖子串 - https://leetcode.cn/problems/minimum-window-substring/
     * 5. LeetCode 438. 找到字符串中所有字母异位词 - https://leetcode.cn/problems/find-all-anagrams-in-a-string/
     * 6. LeetCode 567. 字符串的排列 - https://leetcode.cn/problems/permutation-in-string/
     * 7. LeetCode 1438. 绝对差不超过限制的最长连续子数组 - https://leetcode.cn/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/
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
    
    // 新增：LeetCode 1031. 两个非重叠子数组的最大和
    // 给出非负整数数组 A ，返回两个非重叠（连续）子数组中元素的最大和，
    // 子数组的长度分别为 L 和 M，其中 L、M 是给定的整数。
    // 测试链接 : https://leetcode.cn/problems/maximum-sum-of-two-non-overlapping-subarrays/
    /*
     * 解题思路:
     * 这是POJ 2479的变种，但增加了子数组长度限制。
     * 我们可以考虑两种情况：
     * 1. 长度为L的子数组在长度为M的子数组前面
     * 2. 长度为M的子数组在长度为L的子数组前面
     * 
     * 核心思想:
     * 1. 预处理计算所有长度为L和M的子数组的和
     * 2. 对于每个位置，计算到该位置为止的最大子数组和（前缀最大值）
     * 3. 对于每个位置，计算从该位置开始的最大子数组和（后缀最大值）
     * 4. 枚举分界点，计算两种情况下的最大值
     * 
     * 时间复杂度: O(n) - 需要遍历数组常数次
     * 空间复杂度: O(n) - 需要额外数组存储子数组和及前缀/后缀最大值
     * 
     * 是否最优解: 是，这是该问题的最优解法
     */
    public static int maxSumTwoNoOverlap(int[] A, int L, int M) {
        if (A == null || A.length < L + M) {
            return 0;
        }
        
        // 计算前缀和数组
        int[] prefixSum = new int[A.length + 1];
        for (int i = 0; i < A.length; i++) {
            prefixSum[i + 1] = prefixSum[i] + A[i];
        }
        
        // 计算长度为L的子数组和数组
        int[] Lsums = new int[A.length - L + 1];
        for (int i = 0; i <= A.length - L; i++) {
            Lsums[i] = prefixSum[i + L] - prefixSum[i];
        }
        
        // 计算长度为M的子数组和数组
        int[] Msums = new int[A.length - M + 1];
        for (int i = 0; i <= A.length - M; i++) {
            Msums[i] = prefixSum[i + M] - prefixSum[i];
        }
        
        // 情况1: L长度子数组在M长度子数组前面
        int result1 = helper(Lsums, Msums, L, M);
        
        // 情况2: M长度子数组在L长度子数组前面
        int result2 = helper(Msums, Lsums, M, L);
        
        return Math.max(result1, result2);
    }
    
    // 辅助函数，计算一种情况下的最大和
    private static int helper(int[] firstSums, int[] secondSums, int firstL, int secondL) {
        // 计算firstSums的前缀最大值
        int[] firstPrefixMax = new int[firstSums.length];
        firstPrefixMax[0] = firstSums[0];
        for (int i = 1; i < firstSums.length; i++) {
            firstPrefixMax[i] = Math.max(firstPrefixMax[i - 1], firstSums[i]);
        }
        
        // 计算secondSums的后缀最大值
        int[] secondSuffixMax = new int[secondSums.length];
        secondSuffixMax[secondSums.length - 1] = secondSums[secondSums.length - 1];
        for (int i = secondSums.length - 2; i >= 0; i--) {
            secondSuffixMax[i] = Math.max(secondSuffixMax[i + 1], secondSums[i]);
        }
        
        // 枚举分界点，计算最大和
        int maxSum = 0;
        for (int i = 0; i < firstSums.length; i++) {
            // 确保不会越界
            int secondIndex = i + firstL;
            if (secondIndex < secondSums.length) {
                int currentSum = firstPrefixMax[i] + secondSuffixMax[secondIndex];
                maxSum = Math.max(maxSum, currentSum);
            }
        }
        
        return maxSum;
    }
}