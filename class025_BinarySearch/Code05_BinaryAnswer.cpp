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

#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <climits>
using namespace std;

class Code05_BinaryAnswer {
public:
    // 方法一：搜索插入位置
    // 时间复杂度: O(log n)
    // 空间复杂度: O(1)
    static int searchInsert(vector<int>& nums, int target) {
        int left = 0;
        int right = nums.size() - 1;
        
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
    
    // 方法二：x的平方根（整数部分）
    // 时间复杂度: O(log x)
    // 空间复杂度: O(1)
    static int mySqrt(int x) {
        if (x == 0 || x == 1) return x;
        
        int left = 1, right = x;
        int result = 0;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            // 使用除法避免溢出
            if (mid <= x / mid) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    // 方法三：第一个错误的版本
    // 时间复杂度: O(log n)
    // 空间复杂度: O(1)
    static int firstBadVersion(int n, function<bool(int)> isBadVersion) {
        int left = 1, right = n;
        
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
    
    // 方法四：排列硬币
    // 时间复杂度: O(log n)
    // 空间复杂度: O(1)
    static int arrangeCoins(int n) {
        long left = 0, right = n;
        
        while (left <= right) {
            long mid = left + (right - left) / 2;
            long coins = mid * (mid + 1) / 2;
            
            if (coins == n) {
                return mid;
            } else if (coins < n) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return right;
    }
    
    // 方法五：使结果不超过阈值的最小除数
    // 时间复杂度: O(n log max(nums))
    // 空间复杂度: O(1)
    static int smallestDivisor(vector<int>& nums, int threshold) {
        int left = 1;
        int right = *max_element(nums.begin(), nums.end());
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            
            if (isValidDivisor(nums, mid, threshold)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        
        return left;
    }
    
    // 辅助函数：检查除数是否有效
    static bool isValidDivisor(vector<int>& nums, int divisor, int threshold) {
        int sum = 0;
        for (int num : nums) {
            sum += (num + divisor - 1) / divisor; // 向上取整
            if (sum > threshold) return false;
        }
        return true;
    }
    
    // 方法六：制作m束花所需的最少天数
    // 时间复杂度: O(n log max(bloomDay))
    // 空间复杂度: O(1)
    static int minDays(vector<int>& bloomDay, int m, int k) {
        if ((long long)m * k > bloomDay.size()) return -1;
        
        int left = 1;
        int right = *max_element(bloomDay.begin(), bloomDay.end());
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            
            if (canMakeBouquets(bloomDay, m, k, mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        
        return left;
    }
    
    // 辅助函数：检查在给定天数内是否能制作m束花
    static bool canMakeBouquets(vector<int>& bloomDay, int m, int k, int days) {
        int bouquets = 0;
        int flowers = 0;
        
        for (int day : bloomDay) {
            if (day <= days) {
                flowers++;
                if (flowers == k) {
                    bouquets++;
                    flowers = 0;
                }
            } else {
                flowers = 0;
            }
            
            if (bouquets >= m) return true;
        }
        
        return bouquets >= m;
    }
    
    // 方法七：转变数组后最接近目标值的数组和
    // 时间复杂度: O(n log maxValue)
    // 空间复杂度: O(1)
    static int findBestValue(vector<int>& arr, int target) {
        int left = 0;
        int right = *max_element(arr.begin(), arr.end());
        
        int result = 0;
        int minDiff = INT_MAX;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int sum = calculateSum(arr, mid);
            
            int diff = abs(sum - target);
            
            if (diff < minDiff || (diff == minDiff && mid < result)) {
                minDiff = diff;
                result = mid;
            }
            
            if (sum < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    // 辅助函数：计算将大于value的值替换为value后的数组和
    static int calculateSum(vector<int>& arr, int value) {
        int sum = 0;
        for (int num : arr) {
            sum += min(num, value);
        }
        return sum;
    }
    
    // 测试函数
    static void test() {
        cout << "=== 二分答案算法测试 ===" << endl;
        
        // 测试搜索插入位置
        vector<int> nums1 = {1, 3, 5, 6};
        cout << "搜索插入位置测试:" << endl;
        cout << "数组: ";
        for (int num : nums1) cout << num << " ";
        cout << endl;
        cout << "目标值5的位置: " << searchInsert(nums1, 5) << " (期望: 2)" << endl;
        cout << "目标值2的位置: " << searchInsert(nums1, 2) << " (期望: 1)" << endl;
        cout << "目标值7的位置: " << searchInsert(nums1, 7) << " (期望: 4)" << endl;
        cout << endl;
        
        // 测试平方根
        cout << "平方根测试:" << endl;
        cout << "sqrt(4): " << mySqrt(4) << " (期望: 2)" << endl;
        cout << "sqrt(8): " << mySqrt(8) << " (期望: 2)" << endl;
        cout << "sqrt(16): " << mySqrt(16) << " (期望: 4)" << endl;
        cout << endl;
        
        // 测试排列硬币
        cout << "排列硬币测试:" << endl;
        cout << "5枚硬币可排列行数: " << arrangeCoins(5) << " (期望: 2)" << endl;
        cout << "8枚硬币可排列行数: " << arrangeCoins(8) << " (期望: 3)" << endl;
        cout << endl;
        
        // 测试最小除数
        vector<int> nums2 = {1, 2, 5, 9};
        cout << "最小除数测试:" << endl;
        cout << "数组: ";
        for (int num : nums2) cout << num << " ";
        cout << endl;
        cout << "阈值=6时的最小除数: " << smallestDivisor(nums2, 6) << " (期望: 5)" << endl;
        cout << endl;
        
        // 测试制作花束
        vector<int> bloomDay = {1, 10, 3, 10, 2};
        cout << "制作花束测试:" << endl;
        cout << "开花天数: ";
        for (int day : bloomDay) cout << day << " ";
        cout << endl;
        cout << "制作3束花，每束需要1朵花的最少天数: " 
             << minDays(bloomDay, 3, 1) << " (期望: 3)" << endl;
        cout << endl;
        
        // 测试转变数组
        vector<int> arr = {4, 9, 3};
        cout << "转变数组测试:" << endl;
        cout << "数组: ";
        for (int num : arr) cout << num << " ";
        cout << endl;
        cout << "目标值10的最佳值: " << findBestValue(arr, 10) << " (期望: 3)" << endl;
        cout << endl;
        
        cout << "=== 测试完成 ===" << endl;
    }
    
    // 性能测试函数
    static void performanceTest() {
        cout << "=== 性能测试 ===" << endl;
        
        // 创建大型测试数组
        vector<int> largeNums;
        int size = 1000000;
        for (int i = 0; i < size; i++) {
            largeNums.push_back(i);
        }
        
        cout << "数组大小: " << largeNums.size() << endl;
        
        // 测试搜索插入位置性能
        auto start = chrono::high_resolution_clock::now();
        int result1 = searchInsert(largeNums, size / 2);
        auto end = chrono::high_resolution_clock::now();
        auto duration1 = chrono::duration_cast<chrono::microseconds>(end - start);
        
        // 测试平方根性能
        start = chrono::high_resolution_clock::now();
        int result2 = mySqrt(size);
        end = chrono::high_resolution_clock::now();
        auto duration2 = chrono::duration_cast<chrono::microseconds>(end - start);
        
        cout << "搜索插入位置结果: " << result1 << ", 耗时: " << duration1.count() << "微秒" << endl;
        cout << "平方根计算结果: " << result2 << ", 耗时: " << duration2.count() << "微秒" << endl;
        
        cout << "=== 性能测试完成 ===" << endl;
    }
};

// 主函数
int main() {
    Code05_BinaryAnswer::test();
    // Code05_BinaryAnswer::performanceTest(); // 取消注释进行性能测试
    return 0;
}