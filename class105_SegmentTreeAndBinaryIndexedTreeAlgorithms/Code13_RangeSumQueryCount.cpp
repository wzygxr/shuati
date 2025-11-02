// LeetCode 327. 区间和的个数
// 给定一个整数数组 nums 以及两个整数 lower 和 upper，求区间 [lower, upper] 内的区间和的个数。
// 测试链接: https://leetcode.cn/problems/count-of-range-sum/

#include <iostream>
#include <vector>
#include <algorithm>
#include <unordered_set>
#include <unordered_map>
#include <climits>
#include <chrono>
using namespace std;
using namespace std::chrono;

/**
 * 区间和的个数 - 树状数组解法
 * 
 * 解题思路:
 * 1. 题目要求计算数组中区间和位于 [lower, upper] 范围内的子数组个数
 * 2. 利用前缀和思想：区间和 sum(i,j) = prefix[j+1] - prefix[i]
 * 3. 问题转化为：对于每个 j，计算有多少个 i < j 满足 lower <= prefix[j] - prefix[i] <= upper
 * 4. 进一步转化为：对于每个 j，统计 prefix[i] 的范围为 [prefix[j] - upper, prefix[j] - lower] 的 i 的个数
 * 5. 使用树状数组可以高效地统计这个范围查询
 * 6. 由于前缀和可能很大，需要进行离散化处理
 * 
 * 时间复杂度分析:
 * - 计算前缀和: O(n)
 * - 离散化: O(n log n)
 * - 构建和操作树状数组: O(n log n)
 * - 总时间复杂度: O(n log n)
 * 
 * 空间复杂度分析:
 * - 前缀和数组: O(n)
 * - 离散化数组和映射: O(n)
 * - 树状数组: O(n)
 * - 总空间复杂度: O(n)
 * 
 * 工程化考量:
 * 1. 离散化处理：由于前缀和可能超出整数范围，使用long long类型存储，并进行离散化
 * 2. 边界条件处理：处理空数组、单元素数组等特殊情况
 * 3. 数据类型溢出：使用long long类型避免溢出
 * 4. 异常输入检查：验证输入的有效性
 * 5. 代码可读性：使用清晰的变量命名和详细的注释
 */

class Solution {
private:
    // 树状数组类
    class FenwickTree {
    private:
        vector<int> tree;  // 树状数组
        int size;          // 数组大小

    public:
        /**
         * 构造函数
         * @param size 树状数组大小
         */
        FenwickTree(int size) : size(size) {
            tree.resize(size + 1, 0);  // 树状数组下标从1开始
        }

        /**
         * lowbit操作，获取x的二进制表示中最低位的1所代表的值
         * @param x 输入整数
         * @return x & (-x)
         */
        int lowbit(int x) {
            return x & (-x);
        }

        /**
         * 在指定位置增加delta
         * @param index 索引位置（从1开始）
         * @param delta 增加的值
         */
        void update(int index, int delta) {
            // 沿树状数组向上更新所有相关节点
            while (index <= size) {
                tree[index] += delta;
                index += lowbit(index);
            }
        }

        /**
         * 查询前缀和[1, index]
         * @param index 查询的右边界（从1开始）
         * @return 前缀和
         */
        int query(int index) {
            int sum = 0;
            // 沿树状数组向下累加所有相关节点的值
            while (index > 0) {
                sum += tree[index];
                index -= lowbit(index);
            }
            return sum;
        }

        /**
         * 查询区间和[left, right]
         * @param left 区间左边界（从1开始）
         * @param right 区间右边界（从1开始）
         * @return 区间和
         */
        int queryRange(int left, int right) {
            if (left > right) {
                return 0;
            }
            return query(right) - query(left - 1);
        }
    };

    /**
     * 合并两个有序数组，用于归并排序解法
     */
    void merge(vector<long long>& prefixSums, int left, int mid, int right) {
        vector<long long> temp(right - left + 1);
        int i = left, j = mid + 1, k = 0;

        // 合并两个有序数组
        while (i <= mid && j <= right) {
            if (prefixSums[i] <= prefixSums[j]) {
                temp[k++] = prefixSums[i++];
            } else {
                temp[k++] = prefixSums[j++];
            }
        }

        // 处理剩余元素
        while (i <= mid) {
            temp[k++] = prefixSums[i++];
        }

        while (j <= right) {
            temp[k++] = prefixSums[j++];
        }

        // 将临时数组复制回原数组
        for (int p = 0; p < temp.size(); p++) {
            prefixSums[left + p] = temp[p];
        }
    }

    /**
     * 归并排序并统计满足条件的子数组个数，用于归并排序解法
     */
    int mergeSortAndCount(vector<long long>& prefixSums, int left, int right, int lower, int upper) {
        if (left >= right) {
            return 0;
        }

        int mid = left + (right - left) / 2;
        // 递归处理左右两部分
        int count = mergeSortAndCount(prefixSums, left, mid, lower, upper) +
                   mergeSortAndCount(prefixSums, mid + 1, right, lower, upper);

        // 统计满足条件的子数组个数
        int j = mid + 1, k = mid + 1;
        for (int i = left; i <= mid; i++) {
            // 找到最小的j，使得prefixSums[j] - prefixSums[i] >= lower
            while (j <= right && prefixSums[j] - prefixSums[i] < lower) {
                j++;
            }
            // 找到最大的k，使得prefixSums[k] - prefixSums[i] <= upper
            while (k <= right && prefixSums[k] - prefixSums[i] <= upper) {
                k++;
            }
            // 区间[j, k-1]内的所有前缀和都满足条件
            count += k - j;
        }

        // 合并两个有序数组
        merge(prefixSums, left, mid, right);

        return count;
    }

public:
    /**
     * 计算区间和的个数 - 树状数组解法
     * @param nums 输入数组
     * @param lower 区间下界
     * @param upper 区间上界
     * @return 满足条件的子数组个数
     */
    int countRangeSum(vector<int>& nums, int lower, int upper) {
        // 边界条件检查
        if (nums.empty()) {
            return 0;
        }

        int n = nums.size();
        vector<long long> prefixSums(n + 1);  // 前缀和数组，使用long long避免溢出

        // 计算前缀和
        for (int i = 0; i < n; i++) {
            prefixSums[i + 1] = prefixSums[i] + nums[i];
        }

        // 离散化处理
        // 收集所有可能需要查询的值
        unordered_set<long long> valuesSet;
        for (long long sum : prefixSums) {
            valuesSet.insert(sum);
            valuesSet.insert(sum - lower);
            valuesSet.insert(sum - upper);
        }

        // 排序并去重
        vector<long long> sortedValues(valuesSet.begin(), valuesSet.end());
        sort(sortedValues.begin(), sortedValues.end());

        // 建立值到索引的映射
        unordered_map<long long, int> valueToIndex;
        for (int i = 0; i < sortedValues.size(); i++) {
            valueToIndex[sortedValues[i]] = i + 1;  // 索引从1开始
        }

        // 创建树状数组
        FenwickTree fenwickTree(sortedValues.size());
        int count = 0;

        // 从前向后遍历前缀和数组
        for (long long prefixSum : prefixSums) {
            // 查询满足条件的前缀和的数量：prefixSum[j] - upper <= prefixSum[i] <= prefixSum[j] - lower
            int leftIndex = valueToIndex[prefixSum - upper];
            int rightIndex = valueToIndex[prefixSum - lower];
            count += fenwickTree.queryRange(leftIndex, rightIndex);

            // 将当前前缀和加入树状数组
            int currentIndex = valueToIndex[prefixSum];
            fenwickTree.update(currentIndex, 1);
        }

        return count;
    }

    /**
     * 暴力解法（仅供比较，时间复杂度较高）
     * 时间复杂度: O(n²)
     * 空间复杂度: O(n)
     * @param nums 输入数组
     * @param lower 区间下界
     * @param upper 区间上界
     * @return 满足条件的子数组个数
     */
    int countRangeSumBruteForce(vector<int>& nums, int lower, int upper) {
        if (nums.empty()) {
            return 0;
        }

        int n = nums.size();
        vector<long long> prefixSums(n + 1);  // 使用long long避免溢出

        // 计算前缀和
        for (int i = 0; i < n; i++) {
            prefixSums[i + 1] = prefixSums[i] + nums[i];
        }

        int count = 0;
        // 暴力枚举所有可能的子数组
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                long long rangeSum = prefixSums[j] - prefixSums[i];
                if (rangeSum >= lower && rangeSum <= upper) {
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * 归并排序解法（另一种O(n log n)的解法）
     * @param nums 输入数组
     * @param lower 区间下界
     * @param upper 区间上界
     * @return 满足条件的子数组个数
     */
    int countRangeSumMergeSort(vector<int>& nums, int lower, int upper) {
        if (nums.empty()) {
            return 0;
        }

        int n = nums.size();
        vector<long long> prefixSums(n + 1);  // 使用long long避免溢出

        // 计算前缀和
        for (int i = 0; i < n; i++) {
            prefixSums[i + 1] = prefixSums[i] + nums[i];
        }

        // 归并排序过程中统计满足条件的子数组个数
        return mergeSortAndCount(prefixSums, 0, n, lower, upper);
    }
};

/**
 * 打印向量的辅助函数
 */
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

/**
 * 测试函数
 */
void testSolution() {
    Solution solution;
    
    // 测试用例1
    vector<int> nums1 = {-2, 5, -1};
    int lower1 = -2;
    int upper1 = 2;
    cout << "测试用例1:" << endl;
    cout << "输入数组: [";
    printVector(nums1);
    cout << "lower: " << lower1 << ", upper: " << upper1 << endl;
    cout << "树状数组解法结果: " << solution.countRangeSum(nums1, lower1, upper1) << endl;  // 期望输出: 3
    cout << "归并排序解法结果: " << solution.countRangeSumMergeSort(nums1, lower1, upper1) << endl;  // 期望输出: 3
    cout << "暴力解法结果: " << solution.countRangeSumBruteForce(nums1, lower1, upper1) << endl;  // 期望输出: 3
    
    // 测试用例2
    vector<int> nums2 = {0};
    int lower2 = 0;
    int upper2 = 0;
    cout << "\n测试用例2:" << endl;
    cout << "输入数组: [";
    printVector(nums2);
    cout << "lower: " << lower2 << ", upper: " << upper2 << endl;
    cout << "树状数组解法结果: " << solution.countRangeSum(nums2, lower2, upper2) << endl;  // 期望输出: 1
    
    // 测试用例3 - 空数组
    vector<int> nums3 = {};
    int lower3 = 0;
    int upper3 = 0;
    cout << "\n测试用例3:" << endl;
    cout << "输入数组: []" << endl;
    cout << "lower: " << lower3 << ", upper: " << upper3 << endl;
    cout << "树状数组解法结果: " << solution.countRangeSum(nums3, lower3, upper3) << endl;  // 期望输出: 0
    
    // 测试用例4 - 大规模数据
    int size = 1000;
    vector<int> nums4(size);
    for (int i = 0; i < size; i++) {
        nums4[i] = (i % 3 == 0) ? -1 : (i % 3 == 1) ? 0 : 1;
    }
    int lower4 = -2;
    int upper4 = 2;
    cout << "\n测试用例4 (大规模数据):" << endl;
    cout << "数组长度: " << size << endl;
    cout << "lower: " << lower4 << ", upper: " << upper4 << endl;
    
    // 测量树状数组解法的时间
    auto start1 = high_resolution_clock::now();
    int result1 = solution.countRangeSum(nums4, lower4, upper4);
    auto end1 = high_resolution_clock::now();
    auto duration1 = duration_cast<microseconds>(end1 - start1);
    cout << "树状数组解法结果: " << result1 << endl;
    cout << "树状数组解法耗时: " << duration1.count() / 1000.0 << "ms" << endl;
    
    // 测量归并排序解法的时间
    auto start2 = high_resolution_clock::now();
    int result2 = solution.countRangeSumMergeSort(nums4, lower4, upper4);
    auto end2 = high_resolution_clock::now();
    auto duration2 = duration_cast<microseconds>(end2 - start2);
    cout << "归并排序解法结果: " << result2 << endl;
    cout << "归并排序解法耗时: " << duration2.count() / 1000.0 << "ms" << endl;
    
    // 验证两种方法结果是否一致
    cout << "两种方法结果一致: " << (result1 == result2 ? "true" : "false") << endl;
    
    // 对比暴力解法（仅在小规模数据上测试）
    if (size <= 1000) {
        int smallSize = min(size, 300);  // 限制暴力解法的数组大小，避免超时
        vector<int> smallNums(nums4.begin(), nums4.begin() + smallSize);
        
        auto start3 = high_resolution_clock::now();
        int result3 = solution.countRangeSumBruteForce(smallNums, lower4, upper4);
        auto end3 = high_resolution_clock::now();
        auto duration3 = duration_cast<microseconds>(end3 - start3);
        cout << "暴力解法(前" << smallSize << "个元素)结果: " << result3 << endl;
        cout << "暴力解法耗时: " << duration3.count() / 1000.0 << "ms" << endl;
        
        // 验证暴力解法与树状数组解法在前smallSize个元素上是否一致
        int result4 = solution.countRangeSum(smallNums, lower4, upper4);
        cout << "暴力解法与树状数组解法结果一致: " << (result3 == result4 ? "true" : "false") << endl;
    }
}

// 主函数
int main() {
    testSolution();
    return 0;
}