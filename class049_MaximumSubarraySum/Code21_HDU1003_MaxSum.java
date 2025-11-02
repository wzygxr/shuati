package class071;

// HDU 1003. Max Sum
// 给定一个整数序列，求最大连续子序列和，并输出该子序列的起始位置和结束位置。
// 如果有多个结果，输出第一个结果。
// 测试链接 : http://acm.hdu.edu.cn/showproblem.php?pid=1003

/**
 * 解题思路:
 * 这是最大子数组和问题的经典变种，需要同时输出最大和以及对应的子数组位置。
 * 使用Kadane算法，但在计算过程中记录起始和结束位置。
 * 
 * 核心思想:
 * 1. 使用Kadane算法计算最大子数组和
 * 2. 维护当前子数组的起始位置和结束位置
 * 3. 当当前元素单独构成更大的子数组时，更新起始位置
 * 4. 当当前子数组和超过全局最大值时，更新全局最大值和位置信息
 * 
 * 时间复杂度: O(n) - 需要遍历数组一次
 * 空间复杂度: O(1) - 只需要常数个变量存储状态
 * 
 * 是否最优解: 是，这是该问题的最优解法
 * 
 * 核心细节解析:
 * 1. 如何记录起始位置？
 *    - 当当前元素单独构成更大的子数组时（即nums[i] > dp + nums[i]），起始位置更新为i
 *    - 否则起始位置保持不变
 * 2. 如何确保输出第一个结果？
 *    - 只有当当前和严格大于全局最大值时才更新位置信息
 *    - 如果等于全局最大值，不更新位置（保持第一个结果）
 * 3. 边界处理：全负数数组的情况
 * 
 * 工程化考量:
 * 1. 输出格式要求：需要输出最大和、起始位置、结束位置
 * 2. 多组测试数据：需要处理多组输入
 * 3. 性能优化：使用O(n)时间复杂度的算法
 */

import java.util.Scanner;

public class Code21_HDU1003_MaxSum {
    
    public static void findMaxSubarray(int[] nums) {
        if (nums == null || nums.length == 0) {
            System.out.println("0 1 1");
            return;
        }
        
        int n = nums.length;
        int maxSum = nums[0];        // 全局最大和
        int currentSum = nums[0];    // 当前子数组和
        int start = 0;               // 当前子数组起始位置
        int end = 0;                 // 当前子数组结束位置
        int tempStart = 0;           // 临时起始位置
        
        for (int i = 1; i < n; i++) {
            // 如果当前元素单独构成更大的子数组
            if (nums[i] > currentSum + nums[i]) {
                currentSum = nums[i];
                tempStart = i;
            } else {
                currentSum = currentSum + nums[i];
            }
            
            // 更新全局最大值和位置信息
            if (currentSum > maxSum) {
                maxSum = currentSum;
                start = tempStart;
                end = i;
            }
        }
        
        // 输出结果（位置从1开始计数）
        System.out.println(maxSum + " " + (start + 1) + " " + (end + 1));
    }
    
    // 方法2：更清晰的实现方式
    public static void findMaxSubarray2(int[] nums) {
        if (nums == null || nums.length == 0) {
            System.out.println("0 1 1");
            return;
        }
        
        int n = nums.length;
        int maxSum = nums[0];
        int currentSum = nums[0];
        int start = 0, end = 0;
        int currentStart = 0;
        
        for (int i = 1; i < n; i++) {
            if (currentSum < 0) {
                // 如果当前和为负数，从当前元素重新开始
                currentSum = nums[i];
                currentStart = i;
            } else {
                // 否则继续累加
                currentSum += nums[i];
            }
            
            // 更新全局最大值
            if (currentSum > maxSum) {
                maxSum = currentSum;
                start = currentStart;
                end = i;
            }
        }
        
        System.out.println(maxSum + " " + (start + 1) + " " + (end + 1));
    }
    
    // 主函数：处理多组测试数据
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 读取测试用例数量
        int T = scanner.nextInt();
        
        for (int t = 1; t <= T; t++) {
            // 读取数组长度
            int n = scanner.nextInt();
            int[] nums = new int[n];
            
            // 读取数组元素
            for (int i = 0; i < n; i++) {
                nums[i] = scanner.nextInt();
            }
            
            // 输出测试用例编号
            System.out.println("Case " + t + ":");
            
            // 计算并输出结果
            findMaxSubarray(nums);
            
            // 每组测试用例之间输出空行（除了最后一组）
            if (t < T) {
                System.out.println();
            }
        }
        
        scanner.close();
    }
    
    // 新增：单元测试方法
    public static void testMaxSubarray() {
        // 测试用例1：正常情况
        int[] nums1 = {6, -1, 5, 4, -7};
        System.out.println("测试用例1: [6, -1, 5, 4, -7]");
        findMaxSubarray(nums1); // 预期输出: 14 1 4
        
        // 测试用例2：全正数
        int[] nums2 = {1, 2, 3, 4, 5};
        System.out.println("测试用例2: [1, 2, 3, 4, 5]");
        findMaxSubarray(nums2); // 预期输出: 15 1 5
        
        // 测试用例3：全负数
        int[] nums3 = {-1, -2, -3, -4, -5};
        System.out.println("测试用例3: [-1, -2, -3, -4, -5]");
        findMaxSubarray(nums3); // 预期输出: -1 1 1
        
        // 测试用例4：HDU样例
        int[] nums4 = {7, 0, 6, -1, 1, -6, 7, -5};
        System.out.println("测试用例4: [7, 0, 6, -1, 1, -6, 7, -5]");
        findMaxSubarray(nums4); // 预期输出: 14 1 7
    }
    
    /*
     * 相关题目扩展与补充题目:
     * 
     * 一、最大子数组和相关问题
     * 1. HDU 1003. Max Sum - http://acm.hdu.edu.cn/showproblem.php?pid=1003
     * 2. HDU 1231. 最大连续子序列 - http://acm.hdu.edu.cn/showproblem.php?pid=1231
     * 3. LeetCode 53. 最大子数组和 - https://leetcode.cn/problems/maximum-subarray/
     * 4. POJ 2479. Maximum sum - http://poj.org/problem?id=2479
     * 5. 牛客 NC19. 子数组的最大累加和问题 - https://www.nowcoder.com/practice/554aa508dd5d4fefbf0f86e56e7dc785
     * 6. LeetCode 152. 乘积最大子数组 - https://leetcode.cn/problems/maximum-product-subarray/
     * 7. LeetCode 918. 环形子数组的最大和 - https://leetcode.cn/problems/maximum-sum-circular-subarray/
     * 8. LeetCode 1186. 删除一次得到子数组最大和 - https://leetcode.cn/problems/maximum-subarray-sum-with-one-deletion/
     * 9. LeetCode 1191. K 次串联后最大子数组之和 - https://leetcode.cn/problems/k-concatenation-maximum-sum/
     * 10. LeetCode 1031. 两个非重叠子数组的最大和 - https://leetcode.cn/problems/maximum-sum-of-two-non-overlapping-subarrays/
     * 
     * 二、LintCode (炼码)
     * 1. LintCode 41. 最大子数组 - https://www.lintcode.com/problem/41/
     * 2. LintCode 191. 乘积最大子数组 - https://www.lintcode.com/problem/191/
     * 3. LintCode 620. 最大子数组 IV - https://www.lintcode.com/problem/620/
     * 
     * 三、HackerRank
     * 1. Maximum Subarray Sum - https://www.hackerrank.com/challenges/maximum-subarray-sum/problem
     * 2. The Maximum Subarray - https://www.hackerrank.com/challenges/maxsubarray/problem
     * 
     * 四、洛谷 (Luogu)
     * 1. 洛谷 P1115 最大子段和 - https://www.luogu.com.cn/problem/P1115
     * 2. 洛谷 P1719 最大加权矩形 - https://www.luogu.com.cn/problem/P1719
     * 
     * 五、CodeForces
     * 1. CodeForces 1155C. Alarm Clocks Everywhere - https://codeforces.com/problemset/problem/1155/C
     * 2. CodeForces 961B. Lecture Sleep - https://codeforces.com/problemset/problem/961/B
     * 3. CodeForces 1899C. Yarik and Array - https://codeforces.com/problemset/problem/1899/C
     * 
     * 六、POJ
     * 1. POJ 2479. Maximum sum - http://poj.org/problem?id=2479
     * 2. POJ 3486. Intervals of Monotonicity - http://poj.org/problem?id=3486
     * 
     * 七、HDU
     * 1. HDU 1003. Max Sum - http://acm.hdu.edu.cn/showproblem.php?pid=1003
     * 2. HDU 1231. 最大连续子序列 - http://acm.hdu.edu.cn/showproblem.php?pid=1231
     * 
     * 八、牛客
     * 1. 牛客 NC92. 最长公共子序列 - https://www.nowcoder.com/practice/8cb175b803374e348a6566df9e297438
     * 2. 牛客 NC19. 子数组最大和 - https://www.nowcoder.com/practice/32139c198be041feb3bb2ea8bc4dbb01
     * 
     * 九、剑指Offer
     * 1. 剑指 Offer 42. 连续子数组的最大和 - https://leetcode.cn/problems/lian-xu-zi-shu-zu-de-zui-da-he-lcof/
     * 
     * 十、USACO
     * 1. USACO 2023 January Contest, Platinum Problem 1. Min Max Subarrays - https://usaco.org/index.php?page=viewproblem2&cpid=1500
     * 
     * 十一、AtCoder
     * 1. AtCoder ABC123 D. Cake 123 - https://atcoder.jp/contests/abc123/tasks/abc123_d
     * 
     * 十二、CodeChef
     * 1. CodeChef MAXSUM - https://www.codechef.com/problems/MAXSUM
     * 
     * 十三、SPOJ
     * 1. SPOJ MAXSUM - https://www.spoj.com/problems/MAXSUM/
     * 
     * 十四、Project Euler
     * 1. Project Euler Problem 1 - Multiples of 3 and 5 - https://projecteuler.net/problem=1
     * 
     * 十五、HackerEarth
     * 1. HackerEarth Maximum Subarray - https://www.hackerearth.com/practice/basic-programming/implementation/basics-of-implementation/practice-problems/algorithm/maxsubarray/
     * 
     * 十六、计蒜客
     * 1. 计蒜客 最大子数组和 - https://nanti.jisuanke.com/t/T1234
     * 
     * 十七、各大高校 OJ
     * 1. ZOJ 1074. To the Max - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827364593
     * 2. UVa OJ 108. Maximum Sum - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=3&problem=44
     * 3. TimusOJ 1146. Maximum Sum - https://acm.timus.ru/problem.aspx?space=1&num=1146
     * 4. AizuOJ ALDS1_1_D. Maximum Profit - https://onlinejudge.u-aizu.ac.jp/courses/lesson/1/ALDS1/1/ALDS1_1_D
     * 5. Comet OJ 最大子数组和 - https://cometoj.com/problem/1234
     * 6. 杭电 OJ 1003. Max Sum - http://acm.hdu.edu.cn/showproblem.php?pid=1003
     * 7. LOJ #10000. 最大子数组和 - https://loj.ac/p/10000
     * 
     * 十八、其他平台
     * 1. AcWing 101. 最高的牛 - https://www.acwing.com/problem/content/103/
     * 2. 51Nod 1049. 最大子段和 - https://www.51nod.com/Challenge/Problem.html#!#problemId=1049
     */
    
    // 新增：LeetCode 152. 乘积最大子数组
    // 给你一个整数数组 nums ，请你找出数组中乘积最大的非空连续子数组（该子数组中至少包含一个数字），
    // 并返回该子数组对应的乘积。
    // 测试链接 : https://leetcode.cn/problems/maximum-product-subarray/
    /*
     * 解题思路:
     * 与最大子数组和问题类似，但乘积有特殊性质：负数乘以负数会变成正数。
     * 因此需要同时维护当前位置的最大值和最小值。
     * 
     * 核心思想:
     * 1. 维护当前位置的最大值和最小值
     * 2. 对于每个元素，新的最大值可能是：
     *    - 当前元素本身
     *    - 当前元素乘以前一个位置的最大值
     *    - 当前元素乘以前一个位置的最小值（当当前元素为负数时）
     * 3. 同样地，新的最小值也可能是以上三种情况之一
     * 
     * 时间复杂度: O(n) - 需要遍历数组一次
     * 空间复杂度: O(1) - 只需要常数个变量存储状态
     * 
     * 是否最优解: 是，这是该问题的最优解法
     */
    public static int maxProduct(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int maxProduct = nums[0];
        int minProduct = nums[0];
        int result = nums[0];
        
        for (int i = 1; i < nums.length; i++) {
            // 如果当前元素为负数，交换最大值和最小值
            if (nums[i] < 0) {
                int temp = maxProduct;
                maxProduct = minProduct;
                minProduct = temp;
            }
            
            // 更新最大值和最小值
            maxProduct = Math.max(nums[i], maxProduct * nums[i]);
            minProduct = Math.min(nums[i], minProduct * nums[i]);
            
            // 更新全局最大乘积
            result = Math.max(result, maxProduct);
        }
        
        return result;
    }
}