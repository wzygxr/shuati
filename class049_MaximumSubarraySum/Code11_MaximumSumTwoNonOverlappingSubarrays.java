package class071;

// LeetCode 1031. 两个无重叠子数组的最大和
// 给出非负整数数组 A ，返回两个非重叠（连续）子数组中元素的最大和，
// 子数组的长度分别为 L 和 M，其中 L、M 是给定的整数。
// 测试链接 : https://leetcode.cn/problems/maximum-sum-of-two-non-overlapping-subarrays/

/**
 * 解题思路:
 * 这是一个动态规划问题，需要找到两个不重叠的子数组，使得它们的和最大。
 * 
 * 我们可以考虑两种情况：
 * 1. 长度为L的子数组在长度为M的子数组前面
 * 2. 长度为M的子数组在长度为L的子数组前面
 * 
 * 对于每种情况，我们可以使用以下方法：
 * 1. 预处理计算所有长度为L和M的子数组的和
 * 2. 对于每个位置，计算到该位置为止的最大子数组和（前缀最大值）
 * 3. 对于每个位置，计算从该位置开始的最大子数组和（后缀最大值）
 * 4. 枚举分界点，计算两种情况下的最大值
 * 
 * 具体步骤：
 * 1. 计算前缀和数组，便于快速计算子数组和
 * 2. 计算长度为L的子数组和数组Lsums和长度为M的子数组和数组Msums
 * 3. 计算Lsums的前缀最大值和Msums的后缀最大值
 * 4. 计算Msums的前缀最大值和Lsums的后缀最大值
 * 5. 枚举分界点，计算两种情况下的最大值
 * 
 * 时间复杂度: O(n) - 需要遍历数组常数次
 * 空间复杂度: O(n) - 需要额外数组存储子数组和及前缀/后缀最大值
 * 
 * 是否最优解: 是，这是该问题的最优解法
 * 
 * 核心细节解析:
 * 1. 为什么要考虑两种情况？
 *    - 因为L和M的长度可能不同，两种排列会产生不同的结果
 *    - 例如L=1, M=3时，L在前和M在前的最优解可能不同
 * 2. 为什么使用前缀最大值和后缀最大值？
 *    - 前缀最大值表示到当前位置为止的最大子数组和
 *    - 后缀最大值表示从当前位置开始的最大子数组和
 *    - 这样可以确保两个子数组不重叠
 * 3. 如何避免越界？
 *    - 在枚举分界点时需要检查索引是否有效
 * 
 * 工程化考量:
 * 1. 异常处理：输入数组为null或长度不足的情况
 * 2. 边界处理：L和M的长度约束
 * 3. 性能优化：使用前缀和优化子数组和的计算
 * 4. 代码可读性：清晰的变量命名和注释
 */

public class Code11_MaximumSumTwoNonOverlappingSubarrays {
    public static int maxSumTwoNoOverlap(int[] A, int L, int M) {
        // 异常防御
        if (A == null || A.length < L + M) {
            return 0;
        }
        
        // 计算前缀和数组
        int[] prefixSum = new int[A.length + 1];
        prefixSum[0] = 0;
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
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1：LeetCode样例
        int[] A1 = {0, 6, 5, 2, 2, 5, 1, 9, 4};
        int L1 = 1, M1 = 2;
        System.out.println("测试用例1:");
        System.out.println("数组: [0, 6, 5, 2, 2, 5, 1, 9, 4], L = 1, M = 2");
        System.out.println("最大和: " + maxSumTwoNoOverlap(A1, L1, M1)); // 预期输出: 20
        
        // 测试用例2：L在前
        int[] A2 = {3, 8, 1, 3, 2, 1, 8, 9, 0};
        int L2 = 3, M2 = 2;
        System.out.println("\n测试用例2:");
        System.out.println("数组: [3, 8, 1, 3, 2, 1, 8, 9, 0], L = 3, M = 2");
        System.out.println("最大和: " + maxSumTwoNoOverlap(A2, L2, M2)); // 预期输出: 29
        
        // 测试用例3：M在前
        int[] A3 = {2, 1, 5, 6, 0, 9, 5, 0, 3, 8};
        int L3 = 4, M3 = 3;
        System.out.println("\n测试用例3:");
        System.out.println("数组: [2, 1, 5, 6, 0, 9, 5, 0, 3, 8], L = 4, M = 3");
        System.out.println("最大和: " + maxSumTwoNoOverlap(A3, L3, M3)); // 预期输出: 31
    }
    
    /*
     * 相关题目扩展与补充题目:
     * 
     * 一、两个子数组相关问题
     * 1. LeetCode 1031. 两个无重叠子数组的最大和 - https://leetcode.cn/problems/maximum-sum-of-two-non-overlapping-subarrays/
     * 2. LeetCode 1191. K 次串联后最大子数组之和 - https://leetcode.cn/problems/k-concatenation-maximum-sum/
     * 3. POJ 2479. Maximum sum - http://poj.org/problem?id=2479
     * 4. HDU 1003. Max Sum - http://acm.hdu.edu.cn/showproblem.php?pid=1003
     * 5. HDU 1231. 最大连续子序列 - http://acm.hdu.edu.cn/showproblem.php?pid=1231
     * 
     * 二、最大子数组和相关问题
     * 1. LeetCode 53. 最大子数组和 - https://leetcode.cn/problems/maximum-subarray/
     * 2. LeetCode 152. 乘积最大子数组 - https://leetcode.cn/problems/maximum-product-subarray/
     * 3. LeetCode 918. 环形子数组的最大和 - https://leetcode.cn/problems/maximum-sum-circular-subarray/
     * 4. LeetCode 1186. 删除一次得到子数组最大和 - https://leetcode.cn/problems/maximum-subarray-sum-with-one-deletion/
     * 5. LeetCode 1425. 带限制的子序列和 - https://leetcode.cn/problems/constrained-subsequence-sum/
     * 6. LeetCode 862. 和至少为 K 的最短子数组 - https://leetcode.cn/problems/shortest-subarray-with-sum-at-least-k/
     * 7. LeetCode 198. 打家劫舍 - https://leetcode.cn/problems/house-robber/
     * 8. LeetCode 213. 打家劫舍 II - https://leetcode.cn/problems/house-robber-ii/
     * 9. LeetCode 337. 打家劫舍 III - https://leetcode.cn/problems/house-robber-iii/
     * 10. LeetCode 740. 删除并获得点数 - https://leetcode.cn/problems/delete-and-earn/
     * 11. LeetCode 1388. 3n 块披萨 - https://leetcode.cn/problems/pizza-with-3n-slices/
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
    
    // 新增：POJ 2479. Maximum sum
    // 给定一个整数数组，找到两个不相交的连续子数组，使得它们的和最大。
    // 测试链接 : http://poj.org/problem?id=2479
    /*
     * 解题思路:
     * 这是LeetCode 1031的变种，但没有固定子数组长度。
     * 我们可以使用动态规划预处理前缀最大子数组和和后缀最大子数组和。
     * 
     * 核心思想:
     * 1. 计算每个位置结尾的最大子数组和（前缀最大子数组和）
     * 2. 计算每个位置开始的最大子数组和（后缀最大子数组和）
     * 3. 枚举分界点，计算两种情况下的最大值
     * 
     * 时间复杂度: O(n) - 需要遍历数组常数次
     * 空间复杂度: O(n) - 需要额外数组存储前缀和后缀最大值
     * 
     * 是否最优解: 是，这是该问题的最优解法
     */
    public static int maxSumTwoSubarrays(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        
        int n = arr.length;
        
        // 计算前缀最大子数组和
        int[] prefixMax = new int[n];
        int currentSum = arr[0];
        prefixMax[0] = arr[0];
        for (int i = 1; i < n; i++) {
            currentSum = Math.max(arr[i], currentSum + arr[i]);
            prefixMax[i] = Math.max(prefixMax[i - 1], currentSum);
        }
        
        // 计算后缀最大子数组和
        int[] suffixMax = new int[n];
        currentSum = arr[n - 1];
        suffixMax[n - 1] = arr[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            currentSum = Math.max(arr[i], currentSum + arr[i]);
            suffixMax[i] = Math.max(suffixMax[i + 1], currentSum);
        }
        
        // 枚举分界点，计算最大和
        int maxSum = Integer.MIN_VALUE;
        for (int i = 0; i < n - 1; i++) {
            maxSum = Math.max(maxSum, prefixMax[i] + suffixMax[i + 1]);
        }
        
        return maxSum;
    }
}