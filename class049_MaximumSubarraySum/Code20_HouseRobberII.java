package class071;

// LeetCode 213. 打家劫舍 II
// 你是一个专业的小偷，计划偷窃沿街的房屋，每间房内都藏有一定的现金。
// 这个地方所有的房屋都围成一圈，这意味着第一个房屋和最后一个房屋是紧挨着的。
// 同时，相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
// 给定一个代表每个房屋存放金额的非负整数数组，计算你在不触动警报装置的情况下，能够偷窃到的最高金额。
// 测试链接 : https://leetcode.cn/problems/house-robber-ii/

/**
 * 解题思路:
 * 这是打家劫舍问题的环形版本。由于房屋围成一圈，第一个和最后一个房屋不能同时被偷。
 * 我们可以将问题分解为两个子问题：
 * 1. 不偷第一个房屋：问题转化为偷窃nums[1...n-1]的线性打家劫舍问题
 * 2. 不偷最后一个房屋：问题转化为偷窃nums[0...n-2]的线性打家劫舍问题
 * 3. 取两种情况的最大值
 * 
 * 核心思想:
 * 1. 环形问题的关键：第一个和最后一个房屋不能同时被偷
 * 2. 通过分解为两个线性问题来解决环形约束
 * 3. 使用打家劫舍I的解法解决每个线性问题
 * 
 * 时间复杂度: O(n) - 需要解决两个线性问题，每个O(n)
 * 空间复杂度: O(1) - 使用滚动数组优化
 * 
 * 是否最优解: 是，这是该问题的最优解法
 * 
 * 核心细节解析:
 * 1. 为什么分解为两个子问题能解决环形约束？
 *    - 情况1确保不偷第一个房屋，因此最后一个房屋可以安全地被偷
 *    - 情况2确保不偷最后一个房屋，因此第一个房屋可以安全地被偷
 *    - 两种情况覆盖了所有可能的最优解
 * 2. 如何处理数组长度为1的特殊情况？
 *    - 如果只有一间房屋，只能偷这一间
 * 3. 如何处理数组长度为2的特殊情况？
 *    - 如果只有两间房屋，由于环形约束，只能偷其中一间
 * 
 * 工程化考量:
 * 1. 代码复用：使用打家劫舍I的解法作为辅助函数
 * 2. 边界处理：各种特殊情况都需要考虑
 * 3. 性能优化：使用O(1)空间复杂度的解法
 */

public class Code20_HouseRobberII {
    
    // 辅助函数：解决线性打家劫舍问题（打家劫舍I的解法）
    private static int robLinear(int[] nums, int start, int end) {
        if (start > end) {
            return 0;
        }
        
        int prev = 0, curr = 0;
        for (int i = start; i <= end; i++) {
            int temp = curr;
            curr = Math.max(curr, prev + nums[i]);
            prev = temp;
        }
        return curr;
    }
    
    // 主函数：解决环形打家劫舍问题
    public static int rob(int[] nums) {
        // 异常防御
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        
        // 特殊情况处理
        if (n == 1) {
            return nums[0];
        }
        if (n == 2) {
            return Math.max(nums[0], nums[1]);
        }
        
        // 情况1：不偷第一个房屋（偷窃范围：1到n-1）
        int case1 = robLinear(nums, 1, n - 1);
        
        // 情况2：不偷最后一个房屋（偷窃范围：0到n-2）
        int case2 = robLinear(nums, 0, n - 2);
        
        // 取两种情况的最大值
        return Math.max(case1, case2);
    }
    
    // 方法2：更直观的分解方式
    public static int rob2(int[] nums) {
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
        
        // 偷窃第一个到倒数第二个房屋
        int[] dp1 = new int[n - 1];
        dp1[0] = nums[0];
        dp1[1] = Math.max(nums[0], nums[1]);
        for (int i = 2; i < n - 1; i++) {
            dp1[i] = Math.max(dp1[i - 1], dp1[i - 2] + nums[i]);
        }
        
        // 偷窃第二个到最后一个房屋
        int[] dp2 = new int[n - 1];
        dp2[0] = nums[1];
        dp2[1] = Math.max(nums[1], nums[2]);
        for (int i = 2; i < n - 1; i++) {
            dp2[i] = Math.max(dp2[i - 1], dp2[i - 2] + nums[i + 1]);
        }
        
        return Math.max(dp1[n - 2], dp2[n - 2]);
    }
    
    // 新增：测试方法
    public static void main(String[] args) {
        // 测试用例1：正常环形情况
        int[] nums1 = {2, 3, 2};
        System.out.println("测试用例1:");
        System.out.println("房屋金额: [2, 3, 2]");
        System.out.println("最大金额（方法1）: " + rob(nums1)); // 预期输出: 3
        System.out.println("最大金额（方法2）: " + rob2(nums1)); // 预期输出: 3
        
        // 测试用例2：更大的环形数组
        int[] nums2 = {1, 2, 3, 1};
        System.out.println("\n测试用例2:");
        System.out.println("房屋金额: [1, 2, 3, 1]");
        System.out.println("最大金额（方法1）: " + rob(nums2)); // 预期输出: 4
        System.out.println("最大金额（方法2）: " + rob2(nums2)); // 预期输出: 4
        
        // 测试用例3：两间房屋
        int[] nums3 = {1, 2};
        System.out.println("\n测试用例3:");
        System.out.println("房屋金额: [1, 2]");
        System.out.println("最大金额（方法1）: " + rob(nums3)); // 预期输出: 2
        System.out.println("最大金额（方法2）: " + rob2(nums3)); // 预期输出: 2
        
        // 测试用例4：单间房屋
        int[] nums4 = {5};
        System.out.println("\n测试用例4:");
        System.out.println("房屋金额: [5]");
        System.out.println("最大金额（方法1）: " + rob(nums4)); // 预期输出: 5
        System.out.println("最大金额（方法2）: " + rob2(nums4)); // 预期输出: 5
    }
    
    /*
     * 相关题目扩展与补充题目:
     * 
     * 一、打家劫舍系列问题
     * 1. LeetCode 213. 打家劫舍 II - https://leetcode.cn/problems/house-robber-ii/
     * 2. LeetCode 198. 打家劫舍 - https://leetcode.cn/problems/house-robber/
     * 3. LeetCode 337. 打家劫舍 III - https://leetcode.cn/problems/house-robber-iii/
     * 4. LeetCode 740. 删除并获得点数 - https://leetcode.cn/problems/delete-and-earn/
     * 5. LeetCode 1388. 3n 块披萨 - https://leetcode.cn/problems/pizza-with-3n-slices/
     * 
     * 二、最大子数组和相关问题
     * 1. LeetCode 53. 最大子数组和 - https://leetcode.cn/problems/maximum-subarray/
     * 2. LeetCode 152. 乘积最大子数组 - https://leetcode.cn/problems/maximum-product-subarray/
     * 3. LeetCode 918. 环形子数组的最大和 - https://leetcode.cn/problems/maximum-sum-circular-subarray/
     * 4. LeetCode 1186. 删除一次得到子数组最大和 - https://leetcode.cn/problems/maximum-subarray-sum-with-one-deletion/
     * 5. LeetCode 1191. K 次串联后最大子数组之和 - https://leetcode.cn/problems/k-concatenation-maximum-sum/
     * 6. LeetCode 1031. 两个非重叠子数组的最大和 - https://leetcode.cn/problems/maximum-sum-of-two-non-overlapping-subarrays/
     * 7. LeetCode 1425. 带限制的子序列和 - https://leetcode.cn/problems/constrained-subsequence-sum/
     * 8. LeetCode 862. 和至少为 K 的最短子数组 - https://leetcode.cn/problems/shortest-subarray-with-sum-at-least-k/
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
    
    // 新增：LeetCode 337. 打家劫舍 III
    // 在上次打劫完一条街道之后和一圈房屋后，小偷又发现了一个新的可行窃的地区。
    // 这个地区只有一个入口，我们称之为"根"。除了"根"之外，每栋房子有且只有一个"父"房子与之相连。
    // 一番侦察之后，聪明的小偷意识到"这个地方的所有房屋的排列类似于一棵二叉树"。
    // 如果两个直接相连的房子在同一天晚上被打劫，房屋将自动报警。
    // 计算小偷一晚能够盗取的最高金额。
    // 测试链接 : https://leetcode.cn/problems/house-robber-iii/
    /*
     * 解题思路:
     * 这是打家劫舍问题的树形版本。对于每个节点，我们有两种选择：
     * 1. 偷当前节点：那么不能偷其左右子节点
     * 2. 不偷当前节点：那么可以偷其左右子节点
     * 
     * 核心思想:
     * 1. 树形动态规划：对每个节点维护两个状态
     * 2. 状态定义：
     *    - rob[node]：偷当前节点时能获得的最大金额
     *    - notRob[node]：不偷当前节点时能获得的最大金额
     * 3. 状态转移：
     *    - rob[node] = node.val + notRob[left] + notRob[right]
     *    - notRob[node] = max(rob[left], notRob[left]) + max(rob[right], notRob[right])
     * 
     * 时间复杂度: O(n) - 需要遍历每个节点一次
     * 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
     * 
     * 是否最优解: 是，这是该问题的最优解法
     */
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
    
    public static int robIII(TreeNode root) {
        int[] result = dfs(root);
        // result[0]表示不偷当前节点的最大金额
        // result[1]表示偷当前节点的最大金额
        return Math.max(result[0], result[1]);
    }
    
    // 返回数组：[不偷当前节点的最大金额, 偷当前节点的最大金额]
    private static int[] dfs(TreeNode node) {
        if (node == null) {
            return new int[]{0, 0};
        }
        
        int[] left = dfs(node.left);
        int[] right = dfs(node.right);
        
        // 不偷当前节点：左右子节点可以偷也可以不偷，取较大值
        int notRob = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
        
        // 偷当前节点：左右子节点都不能偷
        int rob = node.val + left[0] + right[0];
        
        return new int[]{notRob, rob};
    }
}