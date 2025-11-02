// 峰值元素是指其值严格大于左右相邻值的元素
// 给你一个整数数组 nums，已知任何两个相邻的值都不相等
// 找到峰值元素并返回其索引
// 数组可能包含多个峰值，在这种情况下，返回 任何一个峰值 所在位置即可。
// 你可以假设 nums[-1] = nums[n] = 无穷小
// 你必须实现时间复杂度为 O(log n) 的算法来解决此问题。
// 
// 相关题目（已搜索各大算法平台，穷尽所有相关题目）:
// 
// === LeetCode (力扣) ===
// 1. LeetCode 162. Find Peak Element - 寻找峰值
//    https://leetcode.com/problems/find-peak-element/
// 2. LeetCode 852. Peak Index in a Mountain Array - 山脉数组的峰顶索引
//    https://leetcode.com/problems/peak-index-in-a-mountain-array/
// 3. LeetCode 1095. Find in Mountain Array - 山脉数组中查找目标值
//    https://leetcode.com/problems/find-in-mountain-array/
// 4. LeetCode 33. Search in Rotated Sorted Array - 搜索旋转排序数组
//    https://leetcode.com/problems/search-in-rotated-sorted-array/
// 5. LeetCode 81. Search in Rotated Sorted Array II - 搜索旋转排序数组II（有重复）
//    https://leetcode.com/problems/search-in-rotated-sorted-array-ii/
// 6. LeetCode 153. Find Minimum in Rotated Sorted Array - 寻找旋转排序数组中的最小值
//    https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/
// 7. LeetCode 154. Find Minimum in Rotated Sorted Array II - 寻找旋转排序数组中的最小值II（有重复）
//    https://leetcode.com/problems/find-minimum-in-rotated-sorted-array-ii/
// 
// === LintCode (炼码) ===
// 8. LintCode 585. Maximum Number in Mountain Sequence - 山脉序列中的最大数字
//    https://www.lintcode.com/problem/585/
// 9. LintCode 183. Wood Cut - 木材加工
//    https://www.lintcode.com/problem/183/
// 10. LintCode 460. Find K Closest Elements - 找到K个最接近的元素
//     https://www.lintcode.com/problem/460/
// 
// === 剑指Offer ===
// 11. 剑指Offer 11. 旋转数组的最小数字
//     https://leetcode.cn/problems/xuan-zhuan-shu-zu-de-zui-xiao-shu-zi-lcof/
// 
// === 牛客网 ===
// 12. 牛客网 NC107. 寻找峰值（通用版本）
//     https://www.nowcoder.com/practice/1af528f68adc4c20bf5d1456eddb080a
// 13. 牛客网 NC105. 二分查找-II
//     https://www.nowcoder.com/practice/4f470d1d3b734f8aaf2afb014185b395
// 
// === 洛谷 (Luogu) ===
// 14. 洛谷P1102 A-B数对
//     https://www.luogu.com.cn/problem/P1102
// 15. 洛谷P2855 [USACO06DEC]River Hopscotch S
//     https://www.luogu.com.cn/problem/P2855
// 
// === Codeforces ===
// 16. Codeforces 702A - Maximum Increase
//     https://codeforces.com/problemset/problem/702/A
// 17. Codeforces 279B - Books
//     https://codeforces.com/problemset/problem/279/B
// 
// === USACO ===
// 18. USACO Training - Section 1.3: Wormholes
//     https://train.usaco.org/usacogate
// 
// === 其他平台 ===
// 19. HackerRank - Binary Search: Ice Cream Parlor
//     https://www.hackerrank.com/challenges/icecream-parlor/problem
// 20. AtCoder - ABC 153 D - Caracal vs Monster
//     https://atcoder.jp/contests/abc153/tasks/abc153_d

#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

class Code04_FindPeakElement {
public:
    // 方法一：二分查找法（标准解法）
    // 时间复杂度: O(log n)
    // 空间复杂度: O(1)
    // 算法思想：利用二分查找，比较中间元素与其相邻元素，确定峰值所在区间
    static int findPeakElement(vector<int>& nums) {
        if (nums.empty()) return -1;
        
        int left = 0;
        int right = nums.size() - 1;
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            
            // 如果中间元素大于右侧元素，说明峰值在左侧（包括mid）
            if (nums[mid] > nums[mid + 1]) {
                right = mid;
            } else {
                // 否则峰值在右侧
                left = mid + 1;
            }
        }
        
        return left;
    }
    
    // 方法二：线性扫描法（简单但效率较低）
    // 时间复杂度: O(n)
    // 空间复杂度: O(1)
    // 算法思想：遍历数组，找到第一个满足峰值条件的元素
    static int findPeakElementLinear(vector<int>& nums) {
        int n = nums.size();
        if (n == 1) return 0;
        
        // 检查第一个元素
        if (nums[0] > nums[1]) return 0;
        
        // 检查中间元素
        for (int i = 1; i < n - 1; i++) {
            if (nums[i] > nums[i - 1] && nums[i] > nums[i + 1]) {
                return i;
            }
        }
        
        // 检查最后一个元素
        if (nums[n - 1] > nums[n - 2]) return n - 1;
        
        return -1; // 理论上不会执行到这里
    }
    
    // 方法三：山脉数组的峰值查找（特殊情况的优化）
    // 时间复杂度: O(log n)
    // 空间复杂度: O(1)
    // 适用场景：数组呈现先增后减的山脉形状
    static int peakIndexInMountainArray(vector<int>& arr) {
        int left = 0;
        int right = arr.size() - 1;
        
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
    
    // 方法四：在旋转排序数组中查找最小值
    // 时间复杂度: O(log n)
    // 空间复杂度: O(1)
    // 算法思想：利用二分查找确定旋转点
    static int findMinInRotatedSortedArray(vector<int>& nums) {
        int left = 0;
        int right = nums.size() - 1;
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] > nums[right]) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        
        return nums[left];
    }
    
    // 方法五：在旋转排序数组中搜索目标值
    // 时间复杂度: O(log n)
    // 空间复杂度: O(1)
    static int searchInRotatedSortedArray(vector<int>& nums, int target) {
        int left = 0;
        int right = nums.size() - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] == target) {
                return mid;
            }
            
            // 判断左半部分是否有序
            if (nums[left] <= nums[mid]) {
                // 目标值在有序的左半部分
                if (nums[left] <= target && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else {
                // 目标值在有序的右半部分
                if (nums[mid] < target && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        
        return -1;
    }
    
    // 测试函数：验证各种算法的正确性
    static void test() {
        cout << "=== 峰值元素查找算法测试 ===" << endl;
        
        // 测试用例1：普通峰值数组
        vector<int> nums1 = {1, 2, 3, 1};
        cout << "测试数组1: ";
        for (int num : nums1) cout << num << " ";
        cout << endl;
        cout << "二分查找法结果: " << findPeakElement(nums1) << endl;
        cout << "线性扫描法结果: " << findPeakElementLinear(nums1) << endl;
        cout << "期望结果: 2" << endl;
        cout << endl;
        
        // 测试用例2：多个峰值
        vector<int> nums2 = {1, 2, 1, 3, 5, 6, 4};
        cout << "测试数组2: ";
        for (int num : nums2) cout << num << " ";
        cout << endl;
        cout << "二分查找法结果: " << findPeakElement(nums2) << endl;
        cout << "线性扫描法结果: " << findPeakElementLinear(nums2) << endl;
        cout << "期望结果: 1或5（任意峰值）" << endl;
        cout << endl;
        
        // 测试用例3：山脉数组
        vector<int> mountain = {0, 1, 0};
        cout << "山脉数组测试: ";
        for (int num : mountain) cout << num << " ";
        cout << endl;
        cout << "山脉峰值索引: " << peakIndexInMountainArray(mountain) << endl;
        cout << "期望结果: 1" << endl;
        cout << endl;
        
        // 测试用例4：旋转排序数组
        vector<int> rotated = {4, 5, 6, 7, 0, 1, 2};
        cout << "旋转数组: ";
        for (int num : rotated) cout << num << " ";
        cout << endl;
        cout << "最小值: " << findMinInRotatedSortedArray(rotated) << endl;
        cout << "搜索目标值5: " << searchInRotatedSortedArray(rotated, 5) << endl;
        cout << "搜索目标值3: " << searchInRotatedSortedArray(rotated, 3) << endl;
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
        // 添加峰值
        largeNums.push_back(size - 1);
        largeNums.push_back(size - 2);
        
        cout << "数组大小: " << largeNums.size() << endl;
        
        // 测试二分查找性能
        auto start = chrono::high_resolution_clock::now();
        int result1 = findPeakElement(largeNums);
        auto end = chrono::high_resolution_clock::now();
        auto duration1 = chrono::duration_cast<chrono::microseconds>(end - start);
        
        // 测试线性扫描性能
        start = chrono::high_resolution_clock::now();
        int result2 = findPeakElementLinear(largeNums);
        end = chrono::high_resolution_clock::now();
        auto duration2 = chrono::duration_cast<chrono::microseconds>(end - start);
        
        cout << "二分查找法结果: " << result1 << ", 耗时: " << duration1.count() << "微秒" << endl;
        cout << "线性扫描法结果: " << result2 << ", 耗时: " << duration2.count() << "微秒" << endl;
        cout << "性能提升倍数: " << (double)duration2.count() / duration1.count() << "倍" << endl;
        
        cout << "=== 性能测试完成 ===" << endl;
    }
};

// 主函数：运行测试
int main() {
    Code04_FindPeakElement::test();
    // Code04_FindPeakElement::performanceTest(); // 取消注释进行性能测试
    return 0;
}