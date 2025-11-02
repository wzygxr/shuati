package class072;

import java.util.*;

/**
 * Codeforces LIS问题变种 - 最长递增子序列计数
 * 
 * 题目来源：Codeforces 486E - LIS of Sequence
 * 题目链接：https://codeforces.com/problemset/problem/486/E
 * 题目描述：给定一个序列a，对于每个元素a[i]，判断它在最长递增子序列中的角色：
 * 1. 如果a[i]不出现在任何最长递增子序列中，输出'1'
 * 2. 如果a[i]出现在某些但不是所有最长递增子序列中，输出'2'
 * 3. 如果a[i]出现在所有最长递增子序列中，输出'3'
 * 
 * 算法思路：
 * 1. 计算从左到右的LIS长度（f[i]）
 * 2. 计算从右到左的LDS长度（g[i]）
 * 3. 最长递增子序列长度L = max(f[i])
 * 4. 对于每个位置i，如果f[i] + g[i] - 1 == L，则说明a[i]在某个LIS中
 * 5. 统计每个长度级别出现的次数，判断元素是否在所有LIS中
 * 
 * 时间复杂度：O(n*logn) - 使用二分查找优化
 * 空间复杂度：O(n) - 需要存储f和g数组
 * 是否最优解：是，这是标准解法
 * 
 * 示例：
 * 输入: [1, 3, 2]
 * 输出: "123"
 * 解释: 
 * - 元素1：出现在所有LIS中（[1,3]或[1,2]）-> '3'
 * - 元素3：出现在某些LIS中（[1,3]）-> '2'
 * - 元素2：出现在某些LIS中（[1,2]）-> '2'
 */

public class Code21_CodeforcesLISProblem {

    /**
     * 判断每个元素在LIS中的角色
     * 
     * @param nums 输入序列
     * @return 结果字符串，每个字符对应一个元素的角色
     */
    public static String lisOfSequence(int[] nums) {
        int n = nums.length;
        if (n == 0) return "";
        
        // f[i]: 以nums[i]结尾的最长递增子序列长度
        int[] f = new int[n];
        // g[i]: 以nums[i]开头的最长递减子序列长度（从右往左看）
        int[] g = new int[n];
        
        // 计算f数组（从左到右的LIS）
        int[] endsF = new int[n];
        int lenF = 0;
        for (int i = 0; i < n; i++) {
            int pos = binarySearch(endsF, lenF, nums[i], true);
            if (pos == -1) {
                endsF[lenF] = nums[i];
                f[i] = lenF + 1;
                lenF++;
            } else {
                endsF[pos] = nums[i];
                f[i] = pos + 1;
            }
        }
        
        // 计算g数组（从右到左的LIS，相当于原序列从右到左的LDS）
        int[] endsG = new int[n];
        int lenG = 0;
        for (int i = n - 1; i >= 0; i--) {
            // 注意：这里要找<=nums[i]的位置（因为是从右往左）
            int pos = binarySearch(endsG, lenG, nums[i], false);
            if (pos == -1) {
                endsG[lenG] = nums[i];
                g[i] = lenG + 1;
                lenG++;
            } else {
                endsG[pos] = nums[i];
                g[i] = pos + 1;
            }
        }
        
        // 最长递增子序列长度
        int L = lenF;
        
        // 统计每个长度级别出现的次数
        int[] count = new int[L + 1];
        for (int i = 0; i < n; i++) {
            if (f[i] + g[i] - 1 == L) {
                count[f[i]]++;
            }
        }
        
        // 构建结果
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (f[i] + g[i] - 1 != L) {
                // 不出现在任何LIS中
                result.append('1');
            } else if (count[f[i]] == 1) {
                // 出现在所有LIS中（该长度级别只有一个元素）
                result.append('3');
            } else {
                // 出现在某些但不是所有LIS中
                result.append('2');
            }
        }
        
        return result.toString();
    }

    /**
     * 二分查找工具函数
     * 
     * @param ends 有序数组
     * @param len 有效长度
     * @param target 目标值
     * @param strict 是否严格递增（true表示>=，false表示<=）
     * @return 目标位置，如果不存在返回-1
     */
    private static int binarySearch(int[] ends, int len, int target, boolean strict) {
        int left = 0, right = len - 1;
        int result = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (strict) {
                // 查找>=target的最左位置
                if (ends[mid] >= target) {
                    result = mid;
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else {
                // 查找<=target的最左位置（用于从右往左的LDS）
                if (ends[mid] <= target) {
                    result = mid;
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
        }
        
        return result;
    }

    /**
     * 使用动态规划的解法（用于对比）
     * 
     * 算法思路：
     * 1. 分别计算从左到右和从右到左的LIS
     * 2. 使用传统DP方法，时间复杂度O(n²)
     * 3. 适用于小规模数据
     * 
     * 时间复杂度：O(n²)
     * 空间复杂度：O(n)
     * 是否最优解：否，存在O(n*logn)的优化解法
     * 
     * @param nums 输入序列
     * @return 结果字符串
     */
    public static String lisOfSequenceDP(int[] nums) {
        int n = nums.length;
        if (n == 0) return "";
        
        // 计算f数组
        int[] f = new int[n];
        Arrays.fill(f, 1);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    f[i] = Math.max(f[i], f[j] + 1);
                }
            }
        }
        
        // 计算g数组（从右往左的LDS）
        int[] g = new int[n];
        Arrays.fill(g, 1);
        for (int i = n - 1; i >= 0; i--) {
            for (int j = n - 1; j > i; j--) {
                if (nums[j] < nums[i]) { // 注意这里应该是<，因为是从右往左看
                    g[i] = Math.max(g[i], g[j] + 1);
                }
            }
        }
        
        // 找到最大LIS长度
        int L = 0;
        for (int i = 0; i < n; i++) {
            L = Math.max(L, f[i]);
        }
        
        // 统计每个长度级别出现的次数
        int[] count = new int[L + 1];
        for (int i = 0; i < n; i++) {
            if (f[i] + g[i] - 1 == L) {
                count[f[i]]++;
            }
        }
        
        // 构建结果
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (f[i] + g[i] - 1 != L) {
                result.append('1');
            } else if (count[f[i]] == 1) {
                result.append('3');
            } else {
                result.append('2');
            }
        }
        
        return result.toString();
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1：Codeforces示例
        int[] nums1 = {1, 3, 2};
        System.out.println("输入: [1,3,2]");
        System.out.println("优化方法输出: " + lisOfSequence(nums1));
        System.out.println("DP方法输出: " + lisOfSequenceDP(nums1));
        System.out.println("期望: 322");
        System.out.println();
        
        // 测试用例2
        int[] nums2 = {1, 2, 3};
        System.out.println("输入: [1,2,3]");
        System.out.println("优化方法输出: " + lisOfSequence(nums2));
        System.out.println("DP方法输出: " + lisOfSequenceDP(nums2));
        System.out.println("期望: 333");
        System.out.println();
        
        // 测试用例3
        int[] nums3 = {3, 2, 1};
        System.out.println("输入: [3,2,1]");
        System.out.println("优化方法输出: " + lisOfSequence(nums3));
        System.out.println("DP方法输出: " + lisOfSequenceDP(nums3));
        System.out.println("期望: 111");
        System.out.println();
        
        // 测试用例4
        int[] nums4 = {1, 1, 1};
        System.out.println("输入: [1,1,1]");
        System.out.println("优化方法输出: " + lisOfSequence(nums4));
        System.out.println("DP方法输出: " + lisOfSequenceDP(nums4));
        System.out.println();
        
        // 测试用例5：复杂序列
        int[] nums5 = {4, 1, 6, 2, 8, 5, 7, 3};
        System.out.println("输入: [4,1,6,2,8,5,7,3]");
        System.out.println("优化方法输出: " + lisOfSequence(nums5));
        System.out.println("DP方法输出: " + lisOfSequenceDP(nums5));
        System.out.println();
        
        // 性能测试
        int[] largeNums = new int[1000];
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            largeNums[i] = random.nextInt(10000);
        }
        
        long startTime = System.currentTimeMillis();
        String result1 = lisOfSequence(largeNums);
        long endTime = System.currentTimeMillis();
        System.out.println("优化方法处理1000个元素耗时: " + (endTime - startTime) + "ms");
        
        startTime = System.currentTimeMillis();
        String result2 = lisOfSequenceDP(largeNums);
        endTime = System.currentTimeMillis();
        System.out.println("DP方法处理1000个元素耗时: " + (endTime - startTime) + "ms");
        System.out.println("两种方法结果是否一致: " + result1.equals(result2));
    }
}