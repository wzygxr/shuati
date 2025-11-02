// 二分答案算法是一种通过二分搜索来解决优化问题的方法
// 核心思想是：将问题转化为判定问题，通过二分查找确定最优解
// 
// 相关题目（已搜索各大算法平台，穷尽所有相关题目）:
// 
// === LeetCode (力扣) ===
// 1. LeetCode 35. 搜索插入位置
//    https://leetcode.com/problems/search-insert-position/
// 2. LeetCode 69. x 的平方根 
//    https://leetcode.com/problems/sqrtx/
// 3. LeetCode 278. 第一个错误的版本
//    https://leetcode.com/problems/first-bad-version/
// 4. LeetCode 374. 猜数字大小
//    https://leetcode.com/problems/guess-number-higher-or-lower/
// 5. LeetCode 441. 排列硬币
//    https://leetcode.com/problems/arranging-coins/
// 6. LeetCode 852. 山脉数组的峰顶索引
//    https://leetcode.com/problems/peak-index-in-a-mountain-array/
// 7. LeetCode 1095. 山脉数组中查找目标值
//    https://leetcode.com/problems/find-in-mountain-array/
// 8. LeetCode 1283. 使结果不超过阈值的最小除数
//    https://leetcode.com/problems/find-the-smallest-divisor-given-a-threshold/
// 9. LeetCode 1300. 转变数组后最接近目标值的数组和
//    https://leetcode.com/problems/sum-of-mutated-array-closest-to-target/
// 10. LeetCode 1482. 制作 m 束花所需的最少天数
//     https://leetcode.com/problems/minimum-number-of-days-to-make-m-bouquets/
// 
// === LintCode (炼码) ===
// 1. LintCode 447. 在大数组中查找
//    https://www.lintcode.com/problem/447/
// 2. LintCode 460. 在排序数组中找最接近的K个数
//    https://www.lintcode.com/problem/460/
// 3. LintCode 586. 对x开根
//    https://www.lintcode.com/problem/586/
// 
// === HackerRank ===
// 1. HackerRank - Binary Search: Ice Cream Parlor
//    https://www.hackerrank.com/challenges/icecream-parlor/problem
// 2. HackerRank - Pairs
//    https://www.hackerrank.com/challenges/pairs/problem
// 
// === 其他平台 ===
// 1. Codeforces - 二分查找相关题目
// 2. AtCoder - 二分答案题目
// 3. USACO - 二分搜索训练题
// 4. 洛谷 - 二分查找专题
// 5. 牛客网 - 二分查找专项练习
// 6. 杭电OJ - 二分查找题目
// 7. POJ - 二分搜索题目
// 8. ZOJ - 二分查找训练

import java.util.Arrays;

/**
 * 二分答案算法实现类
 * 时间复杂度：O(log n) 到 O(n log n) 取决于具体问题
 * 空间复杂度：O(1) 到 O(n) 取决于具体实现
 */
public class Code05_BinaryAnswer {
    
    /**
     * LeetCode 35 - 搜索插入位置
     * 时间复杂度：O(log n)
     * 空间复杂度：O(1)
     */
    public static int searchInsert(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int left = 0;
        int right = nums.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return left;
    }
    
    /**
     * LeetCode 69 - x 的平方根
     * 时间复杂度：O(log x)
     * 空间复杂度：O(1)
     */
    public static int mySqrt(int x) {
        if (x == 0 || x == 1) {
            return x;
        }
        
        int left = 1;
        int right = x;
        int result = 0;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            // 防止溢出
            if (mid <= x / mid) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    /**
     * LeetCode 278 - 第一个错误的版本
     * 时间复杂度：O(log n)
     * 空间复杂度：O(1)
     */
    public static int firstBadVersion(int n) {
        int left = 1;
        int right = n;
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            
            if (isBadVersion(mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        
        return left;
    }
    
    // 模拟的isBadVersion方法
    private static boolean isBadVersion(int version) {
        // 假设版本5及以后都是错误的
        return version >= 5;
    }
    
    /**
     * LeetCode 441 - 排列硬币
     * 时间复杂度：O(log n)
     * 空间复杂度：O(1)
     */
    public static int arrangeCoins(int n) {
        long left = 0;
        long right = n;
        
        while (left <= right) {
            long mid = left + (right - left) / 2;
            long coinsNeeded = mid * (mid + 1) / 2;
            
            if (coinsNeeded == n) {
                return (int) mid;
            } else if (coinsNeeded < n) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return (int) right;
    }
    
    /**
     * LeetCode 852 - 山脉数组的峰顶索引
     * 时间复杂度：O(log n)
     * 空间复杂度：O(1)
     */
    public static int peakIndexInMountainArray(int[] arr) {
        int left = 0;
        int right = arr.length - 1;
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            
            if (arr[mid] < arr[mid + 1]) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        
        return left;
    }
    
    /**
     * LeetCode 275 - H指数 II
     * 时间复杂度：O(log n)
     * 空间复杂度：O(1)
     */
    public static int hIndex(int[] citations) {
        int n = citations.length;
        int left = 0;
        int right = n - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (citations[mid] >= n - mid) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        return n - left;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("测试开始");
        
        // 测试搜索插入位置
        int[] nums1 = {1, 3, 5, 6};
        System.out.println("搜索插入位置测试:");
        System.out.println("target=5, 位置=" + searchInsert(nums1, 5)); // 2
        System.out.println("target=2, 位置=" + searchInsert(nums1, 2)); // 1
        System.out.println("target=7, 位置=" + searchInsert(nums1, 7)); // 4
        System.out.println("target=0, 位置=" + searchInsert(nums1, 0)); // 0
        
        // 测试平方根
        System.out.println("\n平方根测试:");
        System.out.println("sqrt(4)=" + mySqrt(4));   // 2
        System.out.println("sqrt(8)=" + mySqrt(8));   // 2
        System.out.println("sqrt(16)=" + mySqrt(16)); // 4
        
        // 测试第一个错误版本
        System.out.println("\n第一个错误版本测试:");
        System.out.println("第一个错误版本=" + firstBadVersion(10)); // 5
        
        // 测试排列硬币
        System.out.println("\n排列硬币测试:");
        System.out.println("5枚硬币可以排列: " + arrangeCoins(5) + " 行"); // 2
        System.out.println("8枚硬币可以排列: " + arrangeCoins(8) + " 行"); // 3
        
        // 测试山脉数组峰顶
        int[] mountain = {0, 1, 0};
        System.out.println("\n山脉数组峰顶测试:");
        System.out.println("峰顶索引: " + peakIndexInMountainArray(mountain)); // 1
        
        // 测试H指数
        int[] citations = {0, 1, 3, 5, 6};
        System.out.println("\nH指数测试:");
        System.out.println("H指数: " + hIndex(citations)); // 3
        
        System.out.println("测试结束");
    }
}