package class071;

// LeetCode 209. 长度最小的子数组
// 给定一个含有 n 个正整数的数组和一个正整数 target。
// 找出该数组中满足其和 ≥ target 的长度最小的连续子数组，并返回其长度。
// 如果不存在符合条件的子数组，返回 0。
// 测试链接 : https://leetcode.cn/problems/minimum-size-subarray-sum/

/**
 * 解题思路:
 * 这是一个典型的滑动窗口问题。我们可以使用双指针技巧来找到满足条件的最短子数组。
 * 
 * 核心思想:
 * 1. 使用左右指针维护一个滑动窗口，窗口内的元素和要满足 ≥ target
 * 2. 右指针向右移动扩大窗口，直到窗口和 ≥ target
 * 3. 然后左指针向右移动缩小窗口，寻找更短的满足条件的子数组
 * 4. 在整个过程中记录最小的窗口长度
 * 
 * 时间复杂度: O(n) - 每个元素最多被访问两次（右指针一次，左指针一次）
 * 空间复杂度: O(1) - 只需要常数个变量存储状态
 * 
 * 是否最优解: 是，这是该问题的最优解法
 * 
 * 核心细节解析:
 * 1. 为什么滑动窗口能保证正确性？
 *    - 当窗口和 ≥ target 时，我们尝试缩小窗口，这不会错过更优解
 *    - 因为如果当前窗口已经满足条件，更小的窗口可能也满足条件
 * 2. 如何处理不存在满足条件的子数组？
 *    - 如果遍历结束后minLength仍然是初始值，说明没有找到满足条件的子数组
 * 3. 为什么使用while循环而不是if？
 *    - 因为左指针可能需要移动多次才能让窗口和 < target
 * 
 * 工程化考量:
 * 1. 异常处理：检查输入数组是否为空或target ≤ 0
 * 2. 边界处理：单元素数组、全大于target的数组等
 * 3. 性能优化：避免不必要的计算，使用简洁的循环结构
 */

public class Code14_MinimumSizeSubarraySum {
    
    public static int minSubArrayLen(int target, int[] nums) {
        // 异常防御：处理非法输入
        if (nums == null || nums.length == 0 || target <= 0) {
            return 0;
        }
        
        int n = nums.length;
        int left = 0;           // 滑动窗口左边界
        int sum = 0;            // 当前窗口的和
        int minLength = Integer.MAX_VALUE; // 最小长度，初始化为最大值
        
        // 遍历数组，右指针从0到n-1
        for (int right = 0; right < n; right++) {
            // 将当前右指针指向的元素加入窗口和
            sum += nums[right];
            
            // 当窗口和满足条件时，尝试缩小窗口
            while (sum >= target) {
                // 更新最小长度
                minLength = Math.min(minLength, right - left + 1);
                
                // 缩小窗口：左指针右移，从窗口和中减去左指针指向的元素
                sum -= nums[left];
                left++;
            }
        }
        
        // 如果找到了满足条件的子数组，返回最小长度；否则返回0
        return minLength == Integer.MAX_VALUE ? 0 : minLength;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1：正常情况
        int[] nums1 = {2, 3, 1, 2, 4, 3};
        int target1 = 7;
        System.out.println("测试用例1:");
        System.out.println("数组: [2, 3, 1, 2, 4, 3], target: 7");
        System.out.println("最小长度: " + minSubArrayLen(target1, nums1)); // 预期输出: 2
        
        // 测试用例2：不存在满足条件的子数组
        int[] nums2 = {1, 1, 1, 1, 1};
        int target2 = 11;
        System.out.println("\n测试用例2:");
        System.out.println("数组: [1, 1, 1, 1, 1], target: 11");
        System.out.println("最小长度: " + minSubArrayLen(target2, nums2)); // 预期输出: 0
        
        // 测试用例3：单元素满足条件
        int[] nums3 = {1, 4, 4};
        int target3 = 4;
        System.out.println("\n测试用例3:");
        System.out.println("数组: [1, 4, 4], target: 4");
        System.out.println("最小长度: " + minSubArrayLen(target3, nums3)); // 预期输出: 1
        
        // 测试用例4：全大于target
        int[] nums4 = {10, 20, 30};
        int target4 = 5;
        System.out.println("\n测试用例4:");
        System.out.println("数组: [10, 20, 30], target: 5");
        System.out.println("最小长度: " + minSubArrayLen(target4, nums4)); // 预期输出: 1
    }
    
    /*
     * 相关题目扩展与补充题目:
     * 
     * 一、滑动窗口相关问题
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
     * 二、最大子数组和变种
     * 1. LeetCode 53. 最大子数组和 - https://leetcode.cn/problems/maximum-subarray/
     * 2. LeetCode 152. 乘积最大子数组 - https://leetcode.cn/problems/maximum-product-subarray/
     * 3. LeetCode 918. 环形子数组的最大和 - https://leetcode.cn/problems/maximum-sum-circular-subarray/
     * 4. LeetCode 1186. 删除一次得到子数组最大和 - https://leetcode.cn/problems/maximum-subarray-sum-with-one-deletion/
     * 5. LeetCode 1191. K 次串联后最大子数组之和 - https://leetcode.cn/problems/k-concatenation-maximum-sum/
     * 6. LeetCode 1031. 两个非重叠子数组的最大和 - https://leetcode.cn/problems/maximum-sum-of-two-non-overlapping-subarrays/
     * 7. LeetCode 198. 打家劫舍 - https://leetcode.cn/problems/house-robber/
     * 8. LeetCode 213. 打家劫舍 II - https://leetcode.cn/problems/house-robber-ii/
     * 9. LeetCode 337. 打家劫舍 III - https://leetcode.cn/problems/house-robber-iii/
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
    
    // 新增：LeetCode 862. 和至少为 K 的最短子数组
    // 给你一个整数数组 nums 和一个整数 k ，找出 nums 中和至少为 k 的最短非空子数组，并返回该子数组的长度。
    // 如果不存在这样的子数组，返回 -1。
    // 测试链接 : https://leetcode.cn/problems/shortest-subarray-with-sum-at-least-k/
    /*
     * 解题思路:
     * 这是滑动窗口问题的变种，但与LeetCode 209不同的是，数组中可能包含负数。
     * 当数组中包含负数时，滑动窗口的单调性被破坏，不能简单地使用双指针技巧。
     * 
     * 解决方案：
     * 使用前缀和 + 单调队列的方法。
     * 
     * 核心思想：
     * 1. 计算前缀和数组prefixSum，其中prefixSum[i]表示nums[0..i-1]的和
     * 2. 对于每个位置j，我们需要找到最小的i (i < j)，使得prefixSum[j] - prefixSum[i] >= k
     * 3. 这等价于找到最大的prefixSum[i]，使得prefixSum[i] <= prefixSum[j] - k
     * 4. 使用单调递增队列维护可能的prefixSum[i]值
     * 
     * 算法步骤：
     * 1. 计算前缀和数组
     * 2. 使用双端队列维护单调递增的前缀和索引
     * 3. 对于每个位置j：
     *    - 从队首取出满足条件的索引i，更新最小长度
     *    - 从队尾移除大于等于当前前缀和的索引，保持队列单调性
     *    - 将当前索引加入队尾
     * 
     * 时间复杂度: O(n) - 每个元素最多被加入和移除队列一次
     * 空间复杂度: O(n) - 需要额外的队列存储索引
     * 
     * 是否最优解: 是，这是该问题的最优解法
     */
    public static int shortestSubarray(int[] nums, int k) {
        // 异常防御
        if (nums == null || nums.length == 0 || k <= 0) {
            return -1;
        }
        
        int n = nums.length;
        
        // 计算前缀和数组
        long[] prefixSum = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
        
        // 使用双端队列维护单调递增的前缀和索引
        java.util.Deque<Integer> deque = new java.util.LinkedList<>();
        int minLength = Integer.MAX_VALUE;
        
        // 遍历前缀和数组
        for (int j = 0; j <= n; j++) {
            // 从队首取出满足条件的索引i，更新最小长度
            while (!deque.isEmpty() && prefixSum[j] - prefixSum[deque.peekFirst()] >= k) {
                minLength = Math.min(minLength, j - deque.pollFirst());
            }
            
            // 从队尾移除大于等于当前前缀和的索引，保持队列单调性
            while (!deque.isEmpty() && prefixSum[deque.peekLast()] >= prefixSum[j]) {
                deque.pollLast();
            }
            
            // 将当前索引加入队尾
            deque.offerLast(j);
        }
        
        // 如果找到了满足条件的子数组，返回最小长度；否则返回-1
        return minLength == Integer.MAX_VALUE ? -1 : minLength;
    }
}