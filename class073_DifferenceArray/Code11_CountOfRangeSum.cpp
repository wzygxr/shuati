#include <iostream>
#include <vector>
#include <climits>

/**
 * LeetCode 327. 区间和的个数 (Count of Range Sum)
 * 
 * 题目描述:
 * 给定一个整数数组 nums，返回区间和在 [lower, upper] 之间的区间个数，按数组索引 i, j 满足 0 <= i <= j < n。
 * 
 * 示例:
 * 输入: nums = [-2,5,-1], lower = -2, upper = 2
 * 输出: 3 
 * 解释:
 * 三个区间: [0,0], [2,2], [0,2]，它们的区间和分别为: -2, -1, 2。
 * 
 * 提示:
 * 1. 最直观的算法复杂度是 O(n^2) ，请尝试在线性时间复杂度 O(n log n) 内解决此问题。
 * 
 * 题目链接: https://leetcode.com/problems/count-of-range-sum/
 * 
 * 解题思路:
 * 这个问题可以用归并排序的思想来解决，通过前缀和和归并排序相结合：
 * 1. 计算前缀和数组 prefixSum，其中 prefixSum[i] 表示 nums[0...i-1] 的和
 * 2. 对于每个 j，我们需要找到有多少个 i (i < j) 满足 lower <= prefixSum[j] - prefixSum[i] <= upper
 * 3. 这等价于 prefixSum[j] - upper <= prefixSum[i] <= prefixSum[j] - lower
 * 4. 使用归并排序过程中的有序性质，可以高效地统计满足条件的 i 的数量
 * 
 * 具体步骤：
 * 1. 计算前缀和数组
 * 2. 对前缀和数组进行归并排序，并在归并排序的过程中统计满足条件的区间数量
 * 3. 在归并排序的合并阶段，对于右半部分的每个元素，在左半部分中找到满足条件的元素范围
 * 4. 使用双指针技术在 O(n) 时间内完成对每个右半部分元素的统计
 * 
 * 时间复杂度: O(n log n) - 归并排序的时间复杂度
 * 空间复杂度: O(n) - 需要额外空间存储前缀和数组和归并排序的临时数组
 * 
 * 这是最优解，因为我们利用了归并排序的特性，将问题转化为在有序数组中查找范围，避免了暴力枚举的 O(n^2) 复杂度。
 */
using namespace std;

class Solution {
public:
    /**
     * 计算区间和在 [lower, upper] 之间的区间个数
     * 
     * @param nums 整数数组
     * @param lower 区间和的下限
     * @param upper 区间和的上限
     * @return 满足条件的区间个数
     */
    int countRangeSum(vector<int>& nums, int lower, int upper) {
        if (nums.empty()) {
            return 0;
        }
        
        int n = nums.size();
        // 计算前缀和数组，prefixSum[i] 表示 nums[0...i-1] 的和
        vector<long> prefixSum(n + 1, 0);
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
        
        // 对前缀和数组进行归并排序，并统计满足条件的区间数量
        return mergeSort(prefixSum, 0, prefixSum.size() - 1, lower, upper);
    }
    
private:
    /**
     * 对前缀和数组进行归并排序，并在过程中统计满足条件的区间数量
     * 
     * @param prefixSum 前缀和数组
     * @param left 当前排序区间的左边界
     * @param right 当前排序区间的右边界
     * @param lower 区间和的下限
     * @param upper 区间和的上限
     * @return 满足条件的区间个数
     */
    int mergeSort(vector<long>& prefixSum, int left, int right, int lower, int upper) {
        // 递归终止条件：区间只有一个元素
        if (left >= right) {
            return 0;
        }
        
        // 分治：将数组分成左右两部分
        int mid = left + (right - left) / 2;
        
        // 统计左半部分和右半部分各自满足条件的区间数量
        int count = mergeSort(prefixSum, left, mid, lower, upper) + 
                    mergeSort(prefixSum, mid + 1, right, lower, upper);
        
        // 统计跨越中点的满足条件的区间数量
        count += countCrossRange(prefixSum, left, mid, right, lower, upper);
        
        // 合并左右两个有序数组
        merge(prefixSum, left, mid, right);
        
        return count;
    }
    
    /**
     * 统计跨越中点的满足条件的区间数量
     * 
     * @param prefixSum 前缀和数组
     * @param left 左边界
     * @param mid 中点
     * @param right 右边界
     * @param lower 区间和的下限
     * @param upper 区间和的上限
     * @return 满足条件的跨中点区间个数
     */
    int countCrossRange(vector<long>& prefixSum, int left, int mid, int right, int lower, int upper) {
        int count = 0;
        // 对于右半部分的每个元素 j，找到左半部分中满足条件的元素 i 的范围
        int i = left;
        int lowerBound = left; // 左边界指针，寻找 prefixSum[i] >= prefixSum[j] - upper
        int upperBound = left; // 右边界指针，寻找 prefixSum[i] <= prefixSum[j] - lower
        
        for (int j = mid + 1; j <= right; j++) {
            // 计算当前 j 对应的 i 的范围条件
            long targetLower = prefixSum[j] - upper;
            long targetUpper = prefixSum[j] - lower;
            
            // 找到第一个大于等于 targetLower 的位置
            while (lowerBound <= mid && prefixSum[lowerBound] < targetLower) {
                lowerBound++;
            }
            
            // 找到第一个大于 targetUpper 的位置
            while (upperBound <= mid && prefixSum[upperBound] <= targetUpper) {
                upperBound++;
            }
            
            // 满足条件的 i 的数量是 upperBound - lowerBound
            count += upperBound - lowerBound;
        }
        
        return count;
    }
    
    /**
     * 合并两个有序数组
     * 
     * @param prefixSum 前缀和数组
     * @param left 左边界
     * @param mid 中点
     * @param right 右边界
     */
    void merge(vector<long>& prefixSum, int left, int mid, int right) {
        // 创建临时数组
        vector<long> temp(right - left + 1);
        int i = left;     // 左半部分的指针
        int j = mid + 1;  // 右半部分的指针
        int k = 0;        // 临时数组的指针
        
        // 合并两个有序数组
        while (i <= mid && j <= right) {
            if (prefixSum[i] <= prefixSum[j]) {
                temp[k++] = prefixSum[i++];
            } else {
                temp[k++] = prefixSum[j++];
            }
        }
        
        // 处理左半部分的剩余元素
        while (i <= mid) {
            temp[k++] = prefixSum[i++];
        }
        
        // 处理右半部分的剩余元素
        while (j <= right) {
            temp[k++] = prefixSum[j++];
        }
        
        // 将临时数组复制回原数组
        for (i = 0; i < temp.size(); i++) {
            prefixSum[left + i] = temp[i];
        }
    }
};

/**
 * 使用暴力法解决，时间复杂度 O(n^2)，仅用于测试
 */
int countRangeSumBruteForce(vector<int>& nums, int lower, int upper) {
    int count = 0;
    int n = nums.size();
    
    for (int i = 0; i < n; i++) {
        long sum = 0;
        for (int j = i; j < n; j++) {
            sum += nums[j];
            if (sum >= lower && sum <= upper) {
                count++;
            }
        }
    }
    
    return count;
}

// 辅助函数：打印向量
void printVector(const vector<int>& vec) {
    cout << "[";
    for (size_t i = 0; i < vec.size(); i++) {
        cout << vec[i];
        if (i < vec.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]" << endl;
}

// 测试代码
int main() {
    Solution solution;
    
    // 测试用例1
    vector<int> nums1 = {-2, 5, -1};
    int lower1 = -2;
    int upper1 = 2;
    cout << "测试用例1 结果: " << solution.countRangeSum(nums1, lower1, upper1) << endl; // 预期输出: 3
    cout << "测试用例1 (暴力法) 结果: " << countRangeSumBruteForce(nums1, lower1, upper1) << endl; // 预期输出: 3

    // 测试用例2
    vector<int> nums2 = {0};
    int lower2 = 0;
    int upper2 = 0;
    cout << "测试用例2 结果: " << solution.countRangeSum(nums2, lower2, upper2) << endl; // 预期输出: 1

    // 测试用例3 - 空数组
    vector<int> nums3 = {};
    int lower3 = -1;
    int upper3 = 1;
    cout << "测试用例3 (空数组) 结果: " << solution.countRangeSum(nums3, lower3, upper3) << endl; // 预期输出: 0
    
    // 测试用例4 - 大量数据
    vector<int> nums4(1000);
    for (int i = 0; i < 1000; i++) {
        nums4[i] = i % 10 - 5; // 生成-5到4的随机数
    }
    int lower4 = -10;
    int upper4 = 10;
    auto startTime = chrono::high_resolution_clock::now();
    int result4 = solution.countRangeSum(nums4, lower4, upper4);
    auto endTime = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime);
    cout << "测试用例4 (大数据) 结果: " << result4 << endl;
    cout << "测试用例4 耗时: " << duration.count() << "ms" << endl;
    
    // 测试用例5 - 边界情况，有大数值
    vector<int> nums5 = {INT_MAX, INT_MIN, -1, 0};
    int lower5 = -1;
    int upper5 = 0;
    cout << "测试用例5 (边界值) 结果: " << solution.countRangeSum(nums5, lower5, upper5) << endl; // 预期输出: 4
    
    return 0;
}