/**
 * LeetCode 327. 区间和的个数 (Count of Range Sum) - C++版本
 * 题目链接: https://leetcode.cn/problems/count-of-range-sum/
 * 
 * 题目描述:
 * 给定一个整数数组 nums 以及两个整数 lower 和 upper，
 * 求出数组中所有子数组的和在 [lower, upper] 范围内的个数。
 * 
 * 解题思路:
 * 使用归并排序的思想解决区间和计数问题。
 * 1. 首先计算前缀和数组，将问题转化为：对于每个前缀和sum[i]，
 *    统计在它之前的前缀和sum[j] (j < i) 中，有多少个满足
 *    lower <= sum[i] - sum[j] <= upper，即 sum[i] - upper <= sum[j] <= sum[i] - lower
 * 2. 利用归并排序的分治思想，在合并过程中统计满足条件的区间和个数
 * 3. 在合并两个有序数组时，使用滑动窗口技术统计满足条件的元素对
 * 
 * 时间复杂度分析:
 * - 计算前缀和: O(n)
 * - 归并排序: O(n log n)
 * - 总时间复杂度: O(n log n)
 * 空间复杂度: O(n) 用于存储前缀和数组和辅助数组
 */

#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

class Code01_CountOfRangeSum1 {
private:
    vector<long long> sum;  // 前缀和数组
    vector<long long> help; // 辅助数组，用于归并排序
    long long low, up;      // 区间下界和上界
    
    /**
     * 归并排序分治求解
     * @param l 区间左边界
     * @param r 区间右边界
     * @return 满足条件的区间和个数
     */
    int f(int l, int r) {
        if (l == r) {
            // 单个元素的情况，检查是否在区间内
            return (sum[l] >= low && sum[l] <= up) ? 1 : 0;
        }
        
        int mid = l + ((r - l) >> 1);
        int ans = f(l, mid) + f(mid + 1, r);
        
        // 统计跨越中点的区间和个数
        int windowL = l, windowR = l;
        for (int i = mid + 1; i <= r; i++) {
            long long minVal = sum[i] - up;  // sum[j]的最小值
            long long maxVal = sum[i] - low; // sum[j]的最大值
            
            // 移动左窗口指针，找到第一个满足sum[j] >= minVal的位置
            while (windowL <= mid && sum[windowL] < minVal) {
                windowL++;
            }
            
            // 移动右窗口指针，找到最后一个满足sum[j] <= maxVal的位置
            while (windowR <= mid && sum[windowR] <= maxVal) {
                windowR++;
            }
            
            // 统计满足条件的个数
            if (windowL <= mid && windowR > windowL) {
                ans += windowR - windowL;
            }
        }
        
        // 合并两个有序数组
        merge(l, mid, r);
        
        return ans;
    }
    
    /**
     * 合并两个有序数组
     * @param l 左边界
     * @param mid 中间位置
     * @param r 右边界
     */
    void merge(int l, int mid, int r) {
        int i = l, j = mid + 1, k = l;
        
        while (i <= mid && j <= r) {
            if (sum[i] <= sum[j]) {
                help[k++] = sum[i++];
            } else {
                help[k++] = sum[j++];
            }
        }
        
        while (i <= mid) {
            help[k++] = sum[i++];
        }
        
        while (j <= r) {
            help[k++] = sum[j++];
        }
        
        // 将辅助数组的结果复制回原数组
        for (int idx = l; idx <= r; idx++) {
            sum[idx] = help[idx];
        }
    }
    
public:
    /**
     * 计算数组中区间和在指定范围内的子数组个数
     * @param nums 输入数组
     * @param lower 区间下界
     * @param upper 区间上界
     * @return 满足条件的子数组个数
     */
    int countRangeSum(vector<int>& nums, int lower, int upper) {
        int n = nums.size();
        if (n == 0) return 0;
        
        // 初始化数组
        sum.resize(n);
        help.resize(n);
        
        // 计算前缀和数组
        sum[0] = nums[0];
        for (int i = 1; i < n; i++) {
            sum[i] = sum[i - 1] + nums[i];
        }
        
        low = lower;
        up = upper;
        
        // 使用归并排序分治求解
        return f(0, n - 1);
    }
};

/**
 * 测试函数
 */
int main() {
    Code01_CountOfRangeSum1 solution;
    
    // 测试用例1
    vector<int> nums1 = {-2, 5, -1};
    int lower1 = -2, upper1 = 2;
    int result1 = solution.countRangeSum(nums1, lower1, upper1);
    cout << "测试用例1: nums = [-2, 5, -1], lower = -2, upper = 2" << endl;
    cout << "结果: " << result1 << " (期望: 3)" << endl;
    
    // 测试用例2
    vector<int> nums2 = {0};
    int lower2 = 0, upper2 = 0;
    int result2 = solution.countRangeSum(nums2, lower2, upper2);
    cout << "测试用例2: nums = [0], lower = 0, upper = 0" << endl;
    cout << "结果: " << result2 << " (期望: 1)" << endl;
    
    // 测试用例3
    vector<int> nums3 = {1, 2, 3, 4};
    int lower3 = 3, upper3 = 8;
    int result3 = solution.countRangeSum(nums3, lower3, upper3);
    cout << "测试用例3: nums = [1, 2, 3, 4], lower = 3, upper = 8" << endl;
    cout << "结果: " << result3 << " (期望: 6)" << endl;
    
    return 0;
}