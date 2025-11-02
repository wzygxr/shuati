package class071;

// 牛客 NC19. 子数组的最大累加和问题
// 给定一个数组arr，返回子数组的最大累加和
// 例如，arr = [1, -2, 3, 5, -2, 6, -1]，所有子数组中，[3, 5, -2, 6]可以累加出最大的和12，所以返回12。
// 测试链接 : https://www.nowcoder.com/practice/554aa508dd5d4fefbf0f86e56e7dc785

/**
 * 解题思路:
 * 这是最大子数组和问题的牛客网版本，与LeetCode 53题相同。
 * 使用Kadane算法求解，时间复杂度O(n)，空间复杂度O(1)。
 * 
 * 核心思想:
 * 1. 遍历数组，维护以当前元素结尾的最大子数组和
 * 2. 如果当前和小于0，从当前元素重新开始
 * 3. 否则将当前元素加入之前的子数组
 * 4. 在整个过程中维护全局最大值
 * 
 * 时间复杂度: O(n) - 需要遍历数组一次
 * 空间复杂度: O(1) - 只需要常数个变量存储状态
 * 
 * 是否最优解: 是，这是该问题的最优解法
 * 
 * 核心细节解析:
 * 1. 为什么当前和小于0时要重新开始？
 *    - 因为负数会降低后续子数组的和
 *    - 从当前元素重新开始可能得到更大的和
 * 2. 如何确保算法正确性？
 *    - 数学归纳法：假设前i-1个元素的最优解已知
 *    - 当前元素有两种选择：单独开始或加入前一个子数组
 * 3. 边界处理：空数组、全负数数组等
 * 
 * 工程化考量:
 * 1. 代码简洁性：使用最少的变量完成计算
 * 2. 性能优化：避免不必要的计算和内存分配
 * 3. 可读性：清晰的变量命名和逻辑结构
 */

public class Code24_NowcoderNC19_MaxSubarray {
    
    public static int maxsumofSubarray(int[] arr) {
        // 异常防御
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int maxSum = arr[0];    // 全局最大和
        int currentSum = arr[0]; // 当前子数组和
        
        for (int i = 1; i < arr.length; i++) {
            // 如果当前和小于0，从当前元素重新开始
            if (currentSum < 0) {
                currentSum = arr[i];
            } else {
                // 否则将当前元素加入子数组
                currentSum += arr[i];
            }
            
            // 更新全局最大值
            if (currentSum > maxSum) {
                maxSum = currentSum;
            }
        }
        
        return maxSum;
    }
    
    // 方法2：标准的Kadane算法实现
    public static int maxsumofSubarray2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int maxSum = arr[0];
        int currentSum = arr[0];
        
        for (int i = 1; i < arr.length; i++) {
            // 关键决策：max(当前元素, 当前元素+之前和)
            currentSum = Math.max(arr[i], currentSum + arr[i]);
            maxSum = Math.max(maxSum, currentSum);
        }
        
        return maxSum;
    }
    
    // 方法3：包含位置信息的版本（用于调试和理解）
    public static int maxsumofSubarrayWithPos(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int maxSum = arr[0];
        int currentSum = arr[0];
        int start = 0, end = 0;         // 最大子数组的起始和结束位置
        int tempStart = 0;               // 临时起始位置
        
        for (int i = 1; i < arr.length; i++) {
            if (currentSum < 0) {
                // 从当前元素重新开始
                currentSum = arr[i];
                tempStart = i;
            } else {
                // 加入之前的子数组
                currentSum += arr[i];
            }
            
            // 更新全局最大值和位置信息
            if (currentSum > maxSum) {
                maxSum = currentSum;
                start = tempStart;
                end = i;
            }
        }
        
        System.out.println("最大子数组: 起始位置=" + start + ", 结束位置=" + end);
        return maxSum;
    }
    
    // 新增：测试方法
    public static void main(String[] args) {
        // 测试用例1：牛客网样例
        int[] arr1 = {1, -2, 3, 5, -2, 6, -1};
        System.out.println("测试用例1:");
        System.out.println("数组: [1, -2, 3, 5, -2, 6, -1]");
        System.out.println("最大累加和（方法1）: " + maxsumofSubarray(arr1)); // 预期输出: 12
        System.out.println("最大累加和（方法2）: " + maxsumofSubarray2(arr1)); // 预期输出: 12
        System.out.println("最大累加和（带位置）: " + maxsumofSubarrayWithPos(arr1)); // 预期输出: 12
        
        // 测试用例2：全正数
        int[] arr2 = {1, 2, 3, 4, 5};
        System.out.println("\n测试用例2:");
        System.out.println("数组: [1, 2, 3, 4, 5]");
        System.out.println("最大累加和（方法1）: " + maxsumofSubarray(arr2)); // 预期输出: 15
        System.out.println("最大累加和（方法2）: " + maxsumofSubarray2(arr2)); // 预期输出: 15
        
        // 测试用例3：全负数
        int[] arr3 = {-1, -2, -3, -4, -5};
        System.out.println("\n测试用例3:");
        System.out.println("数组: [-1, -2, -3, -4, -5]");
        System.out.println("最大累加和（方法1）: " + maxsumofSubarray(arr3)); // 预期输出: -1
        System.out.println("最大累加和（方法2）: " + maxsumofSubarray2(arr3)); // 预期输出: -1
        
        // 测试用例4：边界情况
        int[] arr4 = {3};
        System.out.println("\n测试用例4:");
        System.out.println("数组: [3]");
        System.out.println("最大累加和（方法1）: " + maxsumofSubarray(arr4)); // 预期输出: 3
        System.out.println("最大累加和（方法2）: " + maxsumofSubarray2(arr4)); // 预期输出: 3
    }
    
    /*
     * 相关题目扩展与补充题目:
     * 
     * 一、最大子数组和相关问题
     * 1. 牛客 NC19. 子数组的最大累加和问题 - https://www.nowcoder.com/practice/554aa508dd5d4fefbf0f86e56e7dc785
     * 2. LeetCode 53. 最大子数组和 - https://leetcode.cn/problems/maximum-subarray/
     * 3. 剑指 Offer 42. 连续子数组的最大和 - https://leetcode.cn/problems/lian-xu-zi-shu-zu-de-zui-da-he-lcof/
     * 4. HDU 1003. Max Sum - http://acm.hdu.edu.cn/showproblem.php?pid=1003
     * 5. POJ 2479. Maximum sum - http://poj.org/problem?id=2479
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